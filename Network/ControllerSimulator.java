public class ControllerSimulator implements ControllerNetworkListener{
	private static ControllerNetwork network;
	public static void main(String[] args) throws InterruptedException {
		network = new ControllerNetwork(new ControllerSimulator());

	}

	@Override
	public void piUp(String piId) {
		System.out.println("Controller: "+piId+" is up");
		for(int i=0;i<10;i++){
			network.send(Integer.toString(i).getBytes(), piId);
			System.out.println("msg sent");
		}
	}

	@Override
	public void piDown(String piId) {
		System.out.println("Controller: "+piId+" is down");
	}

	@Override
	public void orderConfirmed(int orderId, String piId) {
		System.out.println("Controller: "+piId+" sends confirmation of "+orderId);
	}
}
