
public interface ControllerNetworkListener {
	public void piUp(int piId);
	public void piDown(int piId);
	public void orderConfirmed(int orderId, int piId);
}