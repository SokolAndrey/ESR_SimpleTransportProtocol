package asokol.esr.application.solution.message;

import lombok.Builder;

@Builder
public class ByeMessage implements Message {
  private short id;

  public MessageType getMessageType() {
    return MessageType.BYE_MESSAGE;
  }

  public byte[] getBytes() {
    return new byte[0];
  }

  private byte generateCheckSum() {
    return 0;
  }
}
