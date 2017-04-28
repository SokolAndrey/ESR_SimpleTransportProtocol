package asokol.esr.application.solution.message;

import asokol.esr.application.solution.Constants;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;
import static asokol.esr.application.solution.Utils.parseShort;

@Getter
public class DataMessage implements Message {

  private static int ID_INDEX = 0;
  private static int BATCH_NUMBER_INDEX = 2;
  private static int CHECK_SUM_INDEX = 4;
  private static int DATA_INDEX = 5;


  private short id;
  private short batchNumber;
  private byte checkSum;
  private byte[] dataChunk;

  public DataMessage(short id, short batchNumber, byte[] dataChunk) {
    this.id = id;
    this.batchNumber = batchNumber;
    this.dataChunk = dataChunk;
    this.checkSum = generateCheckSum();
  }

  public DataMessage(byte[] data) {
    if (data.length != Constants.BATCH_SIZE) {
      throw new IllegalArgumentException("Incorrect data size");
    }

    this.id = parseShort(data, ID_INDEX);
    this.batchNumber = parseShort(data, BATCH_NUMBER_INDEX);
    this.checkSum = data[CHECK_SUM_INDEX];
    if (generateCheckSum() != checkSum) {
      throw new IllegalStateException("Check sums do not match");
    }
    this.dataChunk = Arrays.copyOfRange(data, DATA_INDEX, data.length - 1);
  }

  public MessageType getMessageType() {
    return MessageType.DATA_MESSAGE;
  }

  public byte[] getBytes() {
    return ByteBuffer.allocate(BATCH_SIZE)
        .putShort(id)
        .putShort(batchNumber)
        .put(dataChunk, 0, dataChunk.length)
        .put(checkSum)
        .array();
  }

  private byte generateCheckSum() {
    // TODO(asokol): 4/28/17 Imagine hash function
    return 0;
  }
}
