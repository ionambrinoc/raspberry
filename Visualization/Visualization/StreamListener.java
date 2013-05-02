package Visualization;

import java.util.concurrent.LinkedBlockingDeque;

public interface StreamListener {
	void dataUpdate(LinkedBlockingDeque<Statistic> q);
}
