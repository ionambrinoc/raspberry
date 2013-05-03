import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Tester {
	public static void main(String[] args) {
		SampleDataCreator sdc = new SampleDataCreator();
		sdc.putAddMessage(1, 1, 1, 1, 'B');
		sdc.putAddMessage(1, 1, 1, 1, 'B');
		sdc.putAddMessage(1, 1, 1, 1, 'B');
		sdc.putDeleteMessage(1, 1, 'C');
		sdc.putDeleteMessage(1, 1, 'C');
		sdc.finish();
		try {
			System.out.println(new DataInputStream(new FileInputStream(new File("data.dat"))).available());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
