package Visualization;

import java.util.Comparator;

public class VisualizationQueue extends PriorityBlockingDeque<Statistic>{

	public VisualizationQueue(int capacity) {
		super(new Comparator<Statistic>() {
			public int compare(Statistic o1, Statistic o2) {
				return o1.time-o2.time;
			}
		}, capacity);
	}
}
