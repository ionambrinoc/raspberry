package Visualization;

import java.util.concurrent.LinkedBlockingDeque;

public interface StreamListener {
	public void dataUpdate(LinkedBlockingDeque<Statistic> q);
}
