package DisplayStrategy;

import Visualization.LastOrderPrice;

public class DisplayLastOrderPriceStrategy implements DisplayStatisticStrategy{
	protected LastOrderPrice lastOrderPrice;
	
	public DisplayLastOrderPriceStrategy(LastOrderPrice lastOrderPrice){
		this.lastOrderPrice = lastOrderPrice;
	}
	
	@Override
	public String display() {
		label.setText("Last Order Price: Symbol: "+lastOrderPrice.getSymbol()+", Price: "+lastOrderPrice.getPrice()+", Volume: "+lastOrderPrice.getVolume()+".");
	}

}
