package gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class WrappedTableCellRenderer extends JTextArea implements TableCellRenderer {

	
    public WrappedTableCellRenderer() {
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
     }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object
            value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText((String) value);//or something in value, like value.getNote()...
        setSize(table.getColumnModel().getColumn(column).getWidth(),
                getPreferredSize().height);
        if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
	
	
	/*public static final String HTML_1 = "<html><body style='width: ";
	public static final String HTML_2 = "px'>";
	public static final String HTML_3 = "</html>";
	private int width;

	public WrappedTableCellRenderer(int width) {
		this.width = width;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String text = HTML_1 + String.valueOf(width) + HTML_2
				+ value.toString() + HTML_3;
		return super.getTableCellRendererComponent(table, text,
				isSelected, hasFocus, row, column);
	}*/

}
