package asokol.esr.application.server;

public interface IDataReceiveListener {

  /**
   * @param data data from the lower layer.
   */
  void onData(byte[] data);
}
