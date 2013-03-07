public class ControllerSimulator {
	public static void main(String[] args) throws InterruptedException {
		ControllerNetwork network = new ControllerNetwork();
		
		for(int i=0; true; i++){
			network.send(Integer.toString(i).getBytes(),i%5);
			System.out.println(Integer.toString(i)+" sent");
		}
	}
}
