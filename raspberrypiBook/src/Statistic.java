public class Statistic 
{
	public final String symbol;
	public final int timestamp;
	public final int     price;
	public final int    volume; 
	
	public Statistic (String symbol, int time, int price, int volume)
	{
		this.symbol    = symbol; 	this.price = price;
		this.timestamp = time;		this.volume=volume;
	}
}
