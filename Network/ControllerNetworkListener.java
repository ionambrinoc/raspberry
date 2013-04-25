
public interface ControllerNetworkListener {
	public void piUp(String piId);
	public void piDown(String piId);
	public void orderConfirmed(int orderId, String piId);
}