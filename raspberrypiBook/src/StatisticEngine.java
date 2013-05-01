import java.util.ArrayList;
import java.util.LinkedList;


public class StatisticEngine 
{
	
	private ArrayList<TradeList> symbolsList;
	
	public StatisticEngine()
	{
		symbolsList = new ArrayList<TradeList>();
	}
	
	public Statistic addData (int symbol, int price, int vol, int time)
	{
		if (symbolsList.get(symbol)==null) symbolsList.add(symbol, new TradeList(symbol));
		TradeList tlist = symbolsList.get(symbol);
		tlist.add(price,vol);
		return new Statistic(symbol,price,tlist.percChange(), vol, tlist.open(), tlist.high(), tlist.low(), tlist.vwap(), tlist.median(), tlist.avg(), time);
	}
	
}
