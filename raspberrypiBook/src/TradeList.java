import java.util.*;

public class TradeList 
{
	public int symbol;
	public SortedList<Integer> prices;
	
	private int totalvol; // private int totalprice;
	private int vwap; private int avg; private int size;
	private int openprice;
	private int percentagechange;
	private int highPrice; private int lowPrice;
	
	public TradeList(int symbol)
	{
		this.symbol=symbol; size = 0; totalvol=0;
		prices = new SortedList<Integer>();
	}
	
	public void add(int price, int vol)
	{
		avg = (avg*size+price)/(size+1);
		if (prices.size()==0) openprice=price;
		prices.add(price);
		vwap = (vwap * totalvol + vol*price)/(totalvol+vol);
		totalvol+=vol; // totalprice+=price;		
		percentagechange = price*100/openprice - 100;
		if (price>highPrice) highPrice=price;
		if (price<lowPrice) lowPrice=price;
		size+=1;
	}
	
	public int open() {return openprice;}
	public int vwap() {return vwap;}
	public int percChange() {return percentagechange; }
	public int high() {return highPrice;}
	public int low()  {return lowPrice; }
	public int median() {return prices.get(size/2); }
	public int avg() {return avg;}
	}
