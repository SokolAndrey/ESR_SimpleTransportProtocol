package asokol.esr.application.given;

public interface IDataReceiveListener {

  /**
   * @param data data from the lower layer.
   */
  void onData(byte[] data);

}
