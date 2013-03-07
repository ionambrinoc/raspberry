package Visualization;
import DisplayStrategy.DisplayLastOrderPriceStrategy;
import DisplayStrategy.DisplayStatisticStrategy;


public class LastOrderPrice extends Statistic{
	protected final DisplayStatisticStrategy displayStrategy;
	
	public LastOrderPrice(byte[] b){
		super(b);
		displayStrategy = new DisplayLastOrderPriceStrategy(this);
	}

	public int getPrice(){
		return super.getStat1();
	}
	
	public int getVolume(){
		return super.getStat2();
	}
	
	@Override
	public DisplayStatisticStrategy getDisplayStrategy() {
		return displayStrategy;
	}
}
