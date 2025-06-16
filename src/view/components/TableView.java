package view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableView extends JTable
{
    public TableView(Object[][] data, String[] fields)
    {
        DefaultTableModel tableModel = new DefaultTableModel(data, fields)
        {
            //Pra impedir que os campos sejam alteráveis
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Editar");
        tableModel.addColumn("Excluir");

        this.setBounds(10, 50, 800, 450);

        //Força a reconhecer os campos novos
        this.setModel(tableModel);

        this.getColumn("Editar").setCellRenderer(new TableButtonView("Editar"));
        this.getColumn("Excluir").setCellRenderer(new TableButtonView("Excluir"));
    }
}
