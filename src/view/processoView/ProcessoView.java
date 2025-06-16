package view.processoView;

import controller.processo.ProcessoController;
import model.processo.Processo;
import view.components.PopupView;
import view.components.TableView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProcessoView extends JPanel {

	private static final long serialVersionUID = 1L;

	private ProcessoController processoController;

	private JButton btnListarProcessos;
	private JButton btnCadastrarProcesso;

	private JComboBox<Integer> codDisciplina;
	private JCheckBox ativo;

	private int selectedCodProcesso;
	private int selectedCodDisciplina;
	private int countProcessos;

	private PopupView popupView;
	private JPanel parentData;
	private JLabel labelMessage;

	public ProcessoView() {
		this.setLayout(null);

		processoController = new ProcessoController();
		popupView = new PopupView();

		btnListarProcessos = new JButton("Listar Processos");
		btnListarProcessos.setBounds(160, 11, 160, 35);

		btnCadastrarProcesso = new JButton("Cadastrar Processo");
		btnCadastrarProcesso.setBounds(460, 11, 180, 35);

		labelMessage = new JLabel("", SwingConstants.CENTER);
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setSize(300, 35);
		labelMessage.setVisible(false);

		try
		{
			codDisciplina = new JComboBox<>(this.processoController.getCodDisciplinas(false));
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}

		ativo = new JCheckBox("Ativo");

		btnListarProcessos.addActionListener(e -> {
			clearPanel();
			try {
				populateProcessos();
			} catch (Exception ex) {
				showMessage(ex.getMessage());
			}
		});

		btnCadastrarProcesso.addActionListener(e ->
		{
			try
			{
				processoForm(false);
			}
			catch (Exception ex)
			{
				this.clearPanel();
				this.showMessage(ex.getMessage());
			}
		});

		this.add(btnListarProcessos);
		this.add(btnCadastrarProcesso);

		try {
			populateProcessos();
		} catch (Exception ex) {
			showMessage(ex.getMessage());
		}
	}

	private void populateProcessos() throws Exception {
		clearPanel();

		JTable tableProcessos = new TableView(processoController.get(), processoController.getFields());

		countProcessos = tableProcessos.getRowCount();

		tableProcessos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableProcessos.rowAtPoint(e.getPoint());
				int column = tableProcessos.columnAtPoint(e.getPoint());

				if ((row >= 0 && column >= 0)) {
					if (column == 3) { // EDITAR
						selectedCodProcesso = (int) tableProcessos.getValueAt(row, 0);
						codDisciplina.setSelectedItem(Integer.parseInt(tableProcessos.getValueAt(row, 1).toString()));
						//Setar o valor default na hora da atualização
						selectedCodDisciplina = Integer.parseInt(tableProcessos.getValueAt(row, 1).toString());
						ativo.setSelected((boolean) tableProcessos.getValueAt(row, 2));

						try
						{
							processoForm(true);
						}
						catch (Exception ex)
						{
							showMessage(ex.getMessage());
						}
					} else if (column == 4) { // EXCLUIR
						try {
							processoController.delete(Integer.parseInt(tableProcessos.getValueAt(row, 0).toString()));
							countProcessos--;
							popupView.showPopup(parentData, "Processo excluído com sucesso", "Sucesso");
							populateProcessos();
						} catch (Exception ex) {
							showMessage(ex.getMessage());
						}
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tableProcessos);
		scroll.setBounds(tableProcessos.getBounds());
		this.add(scroll);
	}

	private void processoForm(boolean edit) throws Exception
	{
		clearPanel();

		try
		{
			boolean filter = countProcessos > 0;
			codDisciplina = new JComboBox<>(this.processoController.getCodDisciplinas(filter));

			//Para setar o valor default na hora da atualização
			if (edit)
			{
				codDisciplina.setSelectedItem(selectedCodDisciplina);
			}
		}
		catch (Exception ex)
		{
			this.clearPanel();
			throw new Exception(ex.getMessage() + ". Insira ao menos uma disciplina para continuar");
		}

		if (!edit) {
			codDisciplina.setSelectedIndex(0);
			ativo.setSelected(false);
		}

		int width = 300;
		int height = 25;
		int x = 100;

		JLabel lblCodDisciplina = new JLabel("Código da Disciplina:");
		lblCodDisciplina.setBounds(x, 70, width, height);
		codDisciplina.setBounds(x, 100, width, height);

		ativo.setBounds(x, codDisciplina.getY() + 50, width, height);

		JButton btnSubmit = new JButton(edit ? "Editar Processo" : "Cadastrar Processo");
		btnSubmit.setBounds(x, ativo.getY() + 40, 200, height + 10);

		btnSubmit.addActionListener(e -> {
			try {
				submitProcesso(edit);
			} catch (Exception ex) {
				popupView.showPopup(parentData, ex.getMessage(), "Erro");
			}
		});

		this.add(lblCodDisciplina);
		this.add(codDisciplina);

		this.add(ativo);
		this.add(btnSubmit);
		this.repaint();
	}

	private void submitProcesso(boolean edit) throws Exception {
		Processo processo = new Processo();
		Border borderError = BorderFactory.createLineBorder(Color.RED, 3);
		Border borderNormal = new JTextField().getBorder();
		boolean error = false;

		codDisciplina.setBorder(borderNormal);

		if (codDisciplina.getSelectedItem() == null) {
			codDisciplina.setBorder(borderError);
			error = true;
		}

		if(error)
		{
			throw new Exception("Preencha os campos corretamente!");
		}

		processo.setCodProcesso(edit ? selectedCodProcesso : countProcessos);
		processo.setCodDisciplina(Integer.parseInt(codDisciplina.getSelectedItem().toString()));
		processo.setAtivo(ativo.isSelected());

		if (edit) {
			processoController.put(processo, selectedCodProcesso);
		} else {
			processo.setCodProcesso(countProcessos);
			processoController.post(processo);
		}

		popupView.showPopup(parentData, edit ? "Processo editado com sucesso!" : "Processo cadastrado com sucesso!", "Sucesso");

		codDisciplina.setSelectedIndex(0);
		ativo.setSelected(false);

		populateProcessos();
	}

	//region Utils
	public void applyPopup(JPanel parent) {
		this.parentData = parent;
	}

	public void showMessage(String message) {
		clearPanel();
		labelMessage.setText(message);

		int x = (getWidth() - labelMessage.getWidth()) / 2;
		int y = (getHeight() - labelMessage.getHeight()) / 2;
		labelMessage.setLocation(x, y);
		labelMessage.setVisible(true);

		setLayout(new BorderLayout());
		add(labelMessage, BorderLayout.CENTER);

		repaint();
	}

	public void clearPanel() {
		this.removeAll();
		this.setLayout(null);

		this.add(btnListarProcessos);
		this.add(btnCadastrarProcesso);
		this.updateUI();
	}
	//endregion
}