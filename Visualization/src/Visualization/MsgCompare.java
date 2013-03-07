package Visualization;
import java.util.Comparator;


public class MsgCompare implements Comparator<Statistic> {
	public int compare(Statistic x1, Statistic x2){
		//if(x1.timestamp < x2.timestamp) return 1;
		//if(x1.timestamp == x2.timestamp) return 0;
		//else
			return -1;
	}
	public boolean equals(Object obj){
		return this==obj;
	}
}
