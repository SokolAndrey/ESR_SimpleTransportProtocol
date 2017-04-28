package asokol.esr.application.solution.message;

public interface Message {

  MessageType getMessageType();

  byte[] getBytes();

  enum MessageType {
    HELLO_MESSAGE,
    DATA_MESSAGE,
    BYE_MESSAGE,
    RESPONSE_MESSAGE
  }
}
