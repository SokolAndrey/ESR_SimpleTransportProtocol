package asokol.esr.application.solution.message;

import asokol.esr.application.solution.Constants;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Getter;

import java.nio.ByteBuffer;

import static asokol.esr.application.solution.Utils.parseShort;
import static asokol.esr.application.solution.message.Message.MessageType.RESPONSE_MESSAGE;

@Getter
public class ResponseMessage implements Message {

  private static int ID_INDEX = 0;
  private static int MESSAGE_NUMBER_INDEX = 2;
  private static int CHECK_SUM_INDEX = 4;

  private short id;
  private short messageNumber;
  private byte checkSum;

  public ResponseMessage(short id, short messageNumber) {
    this.id = id;
    this.messageNumber = messageNumber;
    this.checkSum = generateCheckSum();
  }

  public ResponseMessage(byte[] data) {
    if (data.length != Constants.DATA_BATCH_SIZE) {
      throw new IllegalArgumentException("Incorrect data size");
    }

    this.id = parseShort(data, ID_INDEX);
    this.messageNumber = parseShort(data, MESSAGE_NUMBER_INDEX);
    checkSum = data[CHECK_SUM_INDEX];
    if (generateCheckSum() != checkSum) {
      throw new IllegalStateException("Check sums do not match");
    }
  }

  @Override
  public MessageType getMessageType() {
    return RESPONSE_MESSAGE;
  }

  @Override
  public byte[] getBytes() {
    return ByteBuffer.allocate(4)
        .putShort(id)
        .putShort(messageNumber)
        .put(checkSum)
        .array();
  }

  private byte generateCheckSum() {
    return 0;
  }

}
