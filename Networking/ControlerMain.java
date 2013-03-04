public class ControlerMain {
	public static void main(String[] args) {
		ControlerSender sender = new ControlerSender();
		sender.start();
		sender.send("111".getBytes(), "0");
		sender.terminate();
	}
}
