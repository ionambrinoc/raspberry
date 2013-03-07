
public class PiClusterSimulator {
	
	public static void main(String[] args) {
		PiSimulator pi0 = new PiSimulator("tcp://192.168.1.100:20000", "tcp://192.168.1.100:30000");
		PiSimulator pi1 = new PiSimulator("tcp://192.168.1.100:20001", "tcp://192.168.1.100:30001");
		PiSimulator pi2 = new PiSimulator("tcp://192.168.1.100:20002", "tcp://192.168.1.100:30002");
		PiSimulator pi3 = new PiSimulator("tcp://192.168.1.100:20003", "tcp://192.168.1.100:30003");
		PiSimulator pi4 = new PiSimulator("tcp://192.168.1.100:20004", "tcp://192.168.1.100:30004");
		
		pi0.start();
		pi1.start();
		pi2.start();
		pi3.start();
		pi4.start();
	}

}
