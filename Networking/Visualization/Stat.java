import java.nio.ByteBuffer;
	
public class Stat {
		
	int i1,i2,i3,i4;

	public Stat(byte[] bs) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		i1 = bb.getInt(0);
		i2 = bb.getInt(4);
		i3 = bb.getInt(8);
		i4 = bb.getInt(12);
	}

	public static void main(String[] args) {
		byte[] bs = {
				0,0,0,(byte)255,
				0,0,(byte)255,0,
				0,(byte)255,0,0,
				(byte)127,0,0,0};
		Stat s = new Stat(bs);
		System.out.println(s.i1);
		System.out.println(s.i2);
		System.out.println(s.i3);
		System.out.println(s.i4);
	}
}
