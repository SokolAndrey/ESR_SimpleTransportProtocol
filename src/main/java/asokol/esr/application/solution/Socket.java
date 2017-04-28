package asokol.esr.application.solution;

import asokol.esr.application.given.IDataReceiveListener;
import asokol.esr.application.given.ILinkLayer;
import asokol.esr.application.solution.message.ByeMessage;
import asokol.esr.application.solution.message.DataMessage;
import asokol.esr.application.solution.message.HelloMessage;
import asokol.esr.application.solution.message.Message;
import asokol.esr.application.solution.message.ResponseMessage;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;
import static asokol.esr.application.solution.Constants.DATA_BATCH_SIZE;
import static asokol.esr.application.solution.Constants.MESSAGE_MAX_LENGTH;
import static asokol.esr.application.solution.Utils.splitInputData;

public class Socket {

  private ReentrantLock lock;
  private short address;
  private ConcurrentMap<Short, Boolean> packageNumberToResponse;

  private ILinkLayer linkLayer;
  private IDataReceiveListener receiveListener;

  public Socket(short address,
                ILinkLayer linkLayer,
                IDataReceiveListener receiveListener
  ) {
    this.address = address;
    this.linkLayer = linkLayer;
    this.receiveListener = receiveListener;
    lock = new ReentrantLock();
    this.packageNumberToResponse = new ConcurrentHashMap<>();
  }

  public void send(String data, short destinationAddress) {

    val batches = splitInputData(data);

    if (batches.size() > MESSAGE_MAX_LENGTH) {
      throw new IllegalArgumentException(
          "The size of input data bigger than " + MESSAGE_MAX_LENGTH * DATA_BATCH_SIZE
      );
    }

    subscribeSocket(destinationAddress);
    boolean isHelloSent = sendHelloMessage(
        address,
        destinationAddress,
        (short) batches.size()
    );

    if (!isHelloSent) {
      throw new IllegalStateException("Message cannot be sent");
    }

    val helloResponse = getResponse();
    val messageId = helloResponse.getId();

    val preparedData = generateDataMessages(batches, messageId);
    // Send each package and wait response. If there is something wrong with response send package again.
    sendData(preparedData);

    // As soon as there is no batches, send ByeMessage.
    sendByeMessage(messageId, (short) batches.size());
    val byeResponse = getResponse();
    // TODO(asokol): 4/27/17 close session?
  }


  public String getData(short senderAddress) {

    subscribeSocket(senderAddress);
    val message = (HelloMessage)receiveMessage();

    if (!isHelloSent) {
      throw new IllegalStateException("Message cannot be sent");
    }

    val helloResponse = getResponse();
    val messageId = helloResponse.getId();

    val preparedData = generateDataMessages(batches, messageId);
    // Send each package and wait response. If there is something wrong with response send package again.
    sendData(preparedData);

    // As soon as there is no batches, send ByeMessage.
    sendByeMessage(messageId, (short) batches.size());
    val byeResponse = getResponse();
    // TODO(asokol): 4/27/17 close session?
  }

  private void subscribeSocket(short address) {
    // TODO(asokol): 4/25/17 subscribe destination listener?
    linkLayer.subscribeReceiveListener(this.receiveListener);
  }

  private Message receiveMessage(){

  }

  /***************************************************************/
  /*********************HELLO MESSAGE*****************************/
  /***************************************************************/
  private boolean sendHelloMessage(short from, short to, short batchCount) {
    val helloMessage = new HelloMessage(from, to, batchCount);
    return linkLayer.send(helloMessage.getBytes());
  }

  private ResponseMessage getResponse() {
    val incomingData = ByteBuffer.allocate(BATCH_SIZE).array();
    ResponseMessage incomingMessage;
    lock.tryLock();
    try {
      receiveListener.onData(incomingData);
      incomingMessage = new ResponseMessage(incomingData);
    } finally {
      lock.unlock();
    }
    return incomingMessage;
  }

  /***************************************************************/
  /*********************DATA MESSAGE*****************************/
  /***************************************************************/
  private boolean sendDataMessage(DataMessage dataMessage) {
    return linkLayer.send(dataMessage.getBytes());
  }

  private void sendData(List<DataMessage> dataMessages) {
    listenResponses();
    List<DataMessage> notConfirmedData = dataMessages;
    while (packageNumberToResponse.containsValue(false)) {
      notConfirmedData.forEach(this::sendDataMessage);
      notConfirmedData = packageNumberToResponse.entrySet()
          .stream()
          .filter(Map.Entry::getValue)
          .map(entry -> dataMessages.get(entry.getKey()))
          .collect(Collectors.toList());
    }
  }

  private List<DataMessage> generateDataMessages(List<byte[]> batches, short messageId) {
    List<DataMessage> dataMessages = new ArrayList<>(batches.size());
    for (int i = 0; i < batches.size(); i++) {
      dataMessages.add(new DataMessage(messageId, (short) i, batches.get(i)));
      packageNumberToResponse.putIfAbsent((short) i, false);
    }
    return dataMessages;
  }

  private void listenResponses() {
    new Thread(() -> {
      while (packageNumberToResponse.containsValue(false)) {
        val response = getResponse();
        if (packageNumberToResponse.containsKey(response.getId())) {
          packageNumberToResponse.replace(response.getId(), true);
        }
      }
    }).run();
  }

  /***************************************************************/
  /*********************BYE MESSAGE*****************************/
  /***************************************************************/
  private boolean sendByeMessage(short messageId, short batchCount) {
    return linkLayer.send(new ByeMessage().getBytes());
  }

}
