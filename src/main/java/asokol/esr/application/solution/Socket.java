package asokol.esr.application.solution;

import asokol.esr.application.given.IDataReceiveListener;
import asokol.esr.application.given.ILinkLayer;
import asokol.esr.application.solution.message.ByeMessage;
import asokol.esr.application.solution.message.DataMessage;
import asokol.esr.application.solution.message.HelloMessage;
import asokol.esr.application.solution.message.Message;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;
import static asokol.esr.application.solution.Constants.DATA_BATCH_SIZE;
import static asokol.esr.application.solution.Constants.MESSAGE_MAX_LENGTH;
import static asokol.esr.application.solution.Utils.splitInputData;

public class Socket {

  private ReentrantLock lock;
  private short address;
  private Map<Short, List<Short>> addressToIds;
  private Map<Short, List<Message>> idToData;

  private ILinkLayer linkLayer;
  private IDataReceiveListener receiveListener;

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

    val helloResponse = getHelloResponse();

    // TODO(asokol): 4/26/17 validate response
    validateHelloResponse(helloResponse);

    // Send each package and wait response. If there is something wrong with response send package again.
    // As soon as there is no batches, send ByeMessage.
    while (!batches.isEmpty()) {
      for (val batch : batches) {
        sendDataMessage(batch);
      }
    }

    sendByeMessage(address, destinationAddress, (short) batches.size());
    val byeResponse = getByeResponse();

    // TODO(asokol): 4/27/17 close session?
  }

  private void subscribeSocket(short address) {
    // TODO(asokol): 4/25/17 subscribe destination listener?
    linkLayer.subscribeReceiveListener(this.receiveListener);
  }

  /*********************HELLO MESSAGE*****************************/
  private boolean sendHelloMessage(short from, short to, short batchCount) {
    val helloMessage = new HelloMessage(from, to, batchCount);
    return linkLayer.send(helloMessage.getBytes());
  }

  private HelloMessage getHelloResponse() {
    val incomingData = ByteBuffer.allocate(BATCH_SIZE).array();
    HelloMessage incomingMessage;
    lock.tryLock();
    try {
      receiveListener.onData(incomingData);
      incomingMessage = new HelloMessage(incomingData);
    } finally {
      lock.unlock();
    }
    return incomingMessage;
  }

  private void validateHelloResponse(HelloMessage helloResponse) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /*********************DATA MESSAGE*****************************/
  private boolean sendDataMessage(byte[] batch) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private DataMessage getDataResponse() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private void validateDataResponse(DataMessage dataMessage) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private byte[] prepareDataBatch(byte[] dataBatch, short address, short batchNumber) {
    return ByteBuffer.allocate(BATCH_SIZE)
        .putShort(address)
        .putShort(batchNumber)
        .put(dataBatch, 0, dataBatch.length)
        .array();
  }

  /*********************BYE MESSAGE******************************/
  private boolean sendByeMessage(short from, short to, short batchCount) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private ByeMessage getByeResponse() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private void validateByeResponse(ByeMessage byeMessage) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

}
