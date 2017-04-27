package asokol.esr.application.solution.message;

import asokol.esr.application.solution.Constants;
import lombok.Getter;

import java.nio.ByteBuffer;

import static asokol.esr.application.solution.Utils.parseShort;

@Getter
public class HelloMessage implements Message {

  private static int SENDER_INDEX = 0;
  private static int RECEIVER_INDEX = 2;
  private static int BATCH_COUNT_INDEX = 4;
  private static int CHECK_SUM_INDEX = 6;

  private short sender;
  private short receiver;
  private short batchCount;
  private byte checkSum;

  public HelloMessage(short sender, short receiver, short batchCount) {
    this.sender = sender;
    this.receiver = receiver;
    this.batchCount = batchCount;
    this.checkSum = generateCheckSum();
  }

  public HelloMessage(byte[] data) {
    if (data.length != Constants.DATA_BATCH_SIZE) {
      throw new IllegalArgumentException("Incorrect data size");
    }

    sender = parseShort(data, SENDER_INDEX);
    receiver = parseShort(data, RECEIVER_INDEX);
    batchCount = parseShort(data, BATCH_COUNT_INDEX);
    checkSum = data[CHECK_SUM_INDEX];
    if (generateCheckSum() != checkSum) {
      throw new IllegalStateException("Check sum do not match");
    }
  }

  public MessageType getMessageType() {
    return MessageType.HELLO_MESSAGE;
  }

  public byte[] getBytes() {
    return ByteBuffer.allocate(Constants.DATA_BATCH_SIZE)
        .putShort(SENDER_INDEX, sender)
        .putShort(RECEIVER_INDEX, receiver)
        .putShort(BATCH_COUNT_INDEX, batchCount)
        .put(CHECK_SUM_INDEX, checkSum)
        .array();
  }

  private byte generateCheckSum() {
    return 0;
  }
}
