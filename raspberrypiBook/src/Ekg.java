import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Ekg  //electrokardiogramm :)
{	
	InetAddress ip;
	int rate;
	
	Timer timer = new Timer(rate, new ActionListener() { public void actionPerformed (ActionEvent e)  
	{ 
		// pass on mac address here
	}		
	} );
	
	public Ekg (int rate)
	{
		try
		{
			ip= InetAddress.getLocalHost();;
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress(); //local MAC address
		}
		catch (UnknownHostException ex)
		{	}
		catch (SocketException ex)
		{	}
		
		rate = this.rate;
		timer.start();
	}	
}
