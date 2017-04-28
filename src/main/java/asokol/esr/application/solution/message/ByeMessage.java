package asokol.esr.application.solution.message;

import asokol.esr.application.solution.Constants;
import lombok.Getter;

import java.nio.ByteBuffer;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;
import static asokol.esr.application.solution.Utils.parseShort;

@Getter
public class ByeMessage implements Message {

  private static int ID_INDEX = 0;
  private static int CHECK_SUM_INDEX = 0;

  private short id;
  private byte checkSum;

  public ByeMessage(short id) {
    this.id = id;
    this.checkSum = generateCheckSum();
  }

  public ByeMessage(byte[] data) {
    if (data.length != Constants.DATA_BATCH_SIZE) {
      throw new IllegalArgumentException("Incorrect data size");
    }

    this.id = parseShort(data, ID_INDEX);
    checkSum = data[CHECK_SUM_INDEX];
    if (generateCheckSum() != checkSum) {
      throw new IllegalStateException("Check sums do not match");
    }
  }

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
