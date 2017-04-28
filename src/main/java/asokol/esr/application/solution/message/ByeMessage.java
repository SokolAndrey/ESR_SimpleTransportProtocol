package asokol.esr.application.solution.message;

import java.nio.ByteBuffer;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;

public class ByeMessage implements Message {
  private short id;
  private byte checkSum;

  public MessageType getMessageType() {
    return MessageType.BYE_MESSAGE;
  }

  public byte[] getBytes() {
    return ByteBuffer.allocate(BATCH_SIZE)
        .putShort(id)
        .put(generateCheckSum())
        .array();
  }

  private byte generateCheckSum() {
    return 0;
  }
}
