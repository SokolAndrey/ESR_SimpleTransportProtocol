package asokol.esr.application.solution;

import lombok.val;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static asokol.esr.application.solution.Constants.DATA_BATCH_SIZE;

public class Utils {
  public static short parseShort(byte[] array, int firstByteIndex) {
    return (short) ((array[firstByteIndex + 1] << 8) + (array[firstByteIndex] & 0xff));
  }

  public static List<byte[]> splitInputData(String data) {
    val dataInBytes = data.getBytes();
    int numberOfBatches = dataInBytes.length % DATA_BATCH_SIZE == 0 ?
        dataInBytes.length / DATA_BATCH_SIZE :
        dataInBytes.length / DATA_BATCH_SIZE + 1;
    val batches = new ArrayList<byte[]>(numberOfBatches);
    for (int i = 0; i < numberOfBatches; i++) {
      byte[] currentBatch = ByteBuffer.allocate(DATA_BATCH_SIZE).array();
      System.arraycopy(dataInBytes, DATA_BATCH_SIZE * i, currentBatch, 0, DATA_BATCH_SIZE);
    }
    return batches;
  }
}
