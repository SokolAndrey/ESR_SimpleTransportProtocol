package asokol.esr.application.solution.message;

import lombok.Getter;

import java.nio.ByteBuffer;

import static asokol.esr.application.solution.Constants.BATCH_SIZE;

@Getter
public class DataMessage implements Message {
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
