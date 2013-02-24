public class ParserMain {
	public static void main(String[] args) {
		ParserSender sender = new ParserSender();
		sender.start();
		sender.send("111".getBytes(), 0);
		sender.terminate();
	}
}
