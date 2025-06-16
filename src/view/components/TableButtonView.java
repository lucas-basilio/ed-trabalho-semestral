package view.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableButtonView extends JButton implements TableCellRenderer {

    public TableButtonView(String texto) {
        setText(texto);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
