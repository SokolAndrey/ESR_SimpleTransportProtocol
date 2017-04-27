package asokol.esr.application.solution.message;

public class DataMessage implements Message {
  short id;
  byte[] dataChunk;

  public MessageType getMessageType() {
    return MessageType.DATA_MESSAGE;
  }

  public byte[] getBytes() {
    return new byte[0];
  }

  private byte generateCheckSum() {
    return 0;
  }
}
