package networking;

public interface ControllerNetworkListener {
	public void piUp(String piId);
	public void piDown(String piId);
	public void newMessage(byte[] msg);
}
