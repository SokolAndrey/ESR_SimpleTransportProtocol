package asokol.esr.application.solution.message;

public interface Message {

  MessageType getMessageType();

  byte[] getBytes();

}
