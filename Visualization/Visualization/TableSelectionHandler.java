package Visualization;

import javax.swing.event.ListSelectionListener;

public class TableSelectionHandler implements ListSelectionListener{
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel tableSelectionModel = (ListSelectionModel)e.getSource();
 
		int firstIndex = e.getFirstIndex();
		int lastIndex = e.getLastIndex();
		boolean isAdjusting = e.getValueIsAdjusting();
		MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
 
		if (lsm.isSelectionEmpty()) {
                output.append(" <none>");
		} else {
				// Find out which indexes are selected.
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        output.append(" " + i);
                    }
                }
            }
            output.append(newline);
            output.setCaretPosition(output.getDocument().getLength());
        }
}
