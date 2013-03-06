public class ControllerMain {
	public static void main(String[] args) throws InterruptedException {
		ControllerNetwork network = new ControllerNetwork();
		network.start();
		
		for(int i=0; true; i++){
			network.send(Integer.toString(i).getBytes(),0);
			System.out.println(Integer.toString(i)+" sent");
			Thread.sleep(1000);
		}
	}
}
