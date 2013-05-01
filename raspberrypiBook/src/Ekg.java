package main;
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
	byte[] mac;
	
	public Ekg (int rate) throws InterruptedException
	{
		try
		{
			ip= InetAddress.getLocalHost();;
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			mac = network.getHardwareAddress(); //local MAC address
		}
		catch (UnknownHostException ex)
		{	} // stub, won't fail.
		catch (SocketException ex)
		{	} // neither will this.
		
		rate = this.rate;

		while(true)
		{
			try 
			{
				Thread.currentThread().sleep(rate);
				System.out.println(mac.toString()); //send to controller here.
			}
			catch (InterruptedException ie) // stub, won't fail.
			{	}
		}	
	}	
}