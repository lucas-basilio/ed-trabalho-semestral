package view;

import view.components.PopupView;

import javax.swing.*;

//Classe abstrata para reduzir as 300 linhas de cada arquivo
public abstract class BaseView<T> extends JPanel {

    protected JTextField[] fields;
    protected JCheckBox checkBoxAtivo;
    protected JTable table;
    protected JButton btnListar, btnCadastrar;
    protected JLabel labelMessage;
    protected PopupView popupView;
    protected JPanel parentData;

    protected int selectedValueId;
    protected int countItems;

    public BaseView() {
        this.setLayout(null);
        popupView = new PopupView();
        initCommonComponents();
    }

    private void initCommonComponents() {
        btnListar = new JButton("Listar");
        btnCadastrar = new JButton("Cadastrar");

        btnListar.addActionListener(e -> {
            clearPanel();
            try {
                populateTable();
            } catch (Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        btnCadastrar.addActionListener(e -> showForm(false));

        labelMessage = new JLabel("", SwingConstants.CENTER);
        labelMessage.setVisible(false);

        this.add(btnListar);
        this.add(btnCadastrar);
    }

    protected abstract void populateTable() throws Exception;
    protected abstract void showForm(boolean edit);
    protected abstract void submitItem(boolean edit) throws Exception;
    protected abstract T getItemFromForm(boolean edit) throws Exception;
    protected abstract void loadItemToForm(T item);
    protected abstract void deleteItem(int id) throws Exception;

    //region Utils
    public void applyPopup(JPanel parent) {
        this.parentData = parent;
    }

    public void showMessage(String message) {
        this.clearPanel();
        labelMessage.setText(message);
        labelMessage.setVisible(true);
        this.add(labelMessage);
        this.repaint();
    }

    public void clearPanel() {
        this.removeAll();
        this.setLayout(null);
        this.add(btnListar);
        this.add(btnCadastrar);
        this.repaint();
    }
    //endregion
}

