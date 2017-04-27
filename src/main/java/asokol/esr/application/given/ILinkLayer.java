package asokol.esr.application.given;

public interface ILinkLayer {

  /**
   * Sends data to the remote part.
   *
   * @param data data to send.
   */
  boolean send(byte[] data);

  /**
   * Subscription for data listeners.
   *
   * @param listener listener object.
   */
  void subscribeReceiveListener(IDataReceiveListener listener);
}
