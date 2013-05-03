import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Reader {

	public static void main(String[] args) throws IOException {
		FileInputStream in;
			
			in = new FileInputStream("/home/yuchen/Downloads/20111219-ARCA_XDP_IBF_2.dat");
			DataInputStream d = new DataInputStream(in);
			
			// standard way to do little endian, using bytebuffer
			byte[] bs = new byte[2];
			d.read(bs);
			ByteBuffer bb = ByteBuffer.wrap(bs);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			System.out.println(bb.getShort());	// PktSize 	this prints 30
			
			System.out.println(d.readByte());	// DeliveryFlag 
			System.out.println(d.readByte());	// NumberMsgs 
			
			// another way to do little endian, using reverse
			System.out.println(Integer.reverseBytes(d.readInt()));	// SeqNum
			System.out.println(Integer.reverseBytes(d.readInt()));	// SendTime 
			System.out.println(Integer.reverseBytes(d.readInt()));	// SendTime
			
			// the whole packet header is 16
			
			System.out.println(Short.reverseBytes(d.readShort()));	// first msg length, 14
			// so 14 + 16 == 30
			System.out.println(Short.reverseBytes(d.readShort()));	// msg type, 1, contrller msg
			
			d.skipBytes(10);	// skip the next 10 bytes
			
			// second packet
			System.out.println(Short.reverseBytes(d.readShort()));	// size, 320
			System.out.println(d.readByte());	// DeliveryFlag, 11
			System.out.println(d.readByte());	// NumberMsgs, 8
	}

}
