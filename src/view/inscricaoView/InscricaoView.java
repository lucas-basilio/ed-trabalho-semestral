package view.inscricaoView;

import controller.inscricao.InscricaoController;
import model.inscricao.Inscricao;
import view.components.PopupView;
import view.components.TableView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InscricaoView extends JPanel {

	private static final long serialVersionUID = 1L;

	private InscricaoController inscricaoController;

	private JComboBox<Integer> codProcesso;
	private JComboBox<String> cpfProfessor;
	private JTextField codDisciplina;

	private JButton btnListarInscricoes;
	private JButton btnCadastrarInscricao;
	private JLabel labelMessage;

	private PopupView popupView;
	private JPanel parentData;

	private int selectedCodProcesso;
	private int countInscricoes;
	private Object[][] infosDisciplinas;

	public InscricaoView() {
		this.setLayout(null);
		popupView = new PopupView();
		inscricaoController = new InscricaoController();

		btnListarInscricoes = new JButton("Listar Inscrições");
		btnListarInscricoes.setBounds(160, 11, 160, 35);

		codDisciplina = new JTextField();

		try
		{
			infosDisciplinas = inscricaoController.dadosDisciplina();
		}
		catch (Exception ex)
		{

		}

		btnCadastrarInscricao = new JButton("Cadastrar Inscrição");
		btnCadastrarInscricao.setBounds(460, 11, 180, 35);

		labelMessage = new JLabel("", SwingConstants.CENTER);
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setSize(300, 35);
		labelMessage.setVisible(false);

		try
		{
			populateCheckBoxes();
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}

		btnListarInscricoes.addActionListener(e ->
		{
			try
			{
				populateInscricoes();
			}
			catch (Exception ex)
			{
				showMessage(ex.getMessage());
			}
		});

		btnCadastrarInscricao.addActionListener(e -> inscricaoForm(false));

		this.add(btnListarInscricoes);
		this.add(btnCadastrarInscricao);

		try
		{
			populateInscricoes();
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}
	}

	private void populateInscricoes() throws Exception {
		clearPanel();

		JTable tableInscricoes = new TableView(inscricaoController.get(), inscricaoController.getFields());

		countInscricoes = tableInscricoes.getRowCount();

		tableInscricoes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableInscricoes.rowAtPoint(e.getPoint());
				int column = tableInscricoes.columnAtPoint(e.getPoint());

				if ((row >= 0 && column >= 0)) {
					if (column == 4) { // EDITAR
						selectedCodProcesso = (int) tableInscricoes.getValueAt(row, 0);
						codProcesso.setSelectedItem(Integer.parseInt(tableInscricoes.getValueAt(row, 1).toString()));
						cpfProfessor.setSelectedItem(tableInscricoes.getValueAt(row, 2).toString());
						codDisciplina.setText(tableInscricoes.getValueAt(row, 3).toString());

						inscricaoForm(true);
					} else if (column == 5) { // EXCLUIR
						try {
							inscricaoController.delete(Integer.parseInt(tableInscricoes.getValueAt(row, 0).toString()));
							countInscricoes--;
							popupView.showPopup(parentData, "Inscrição excluída com sucesso", "Sucesso");
							populateInscricoes();
						} catch (Exception ex) {
							showMessage(ex.getMessage());
						}
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tableInscricoes);
		scroll.setBounds(tableInscricoes.getBounds());
		this.add(scroll);
	}

	private void populateCheckBoxes() throws Exception
	{
		StringBuffer msg = new StringBuffer();

		try {
			codProcesso = new JComboBox<>(inscricaoController.getCodProcessos());
			codProcesso.setSelectedItem(0);
		}
		catch (Exception ex)
		{
			msg.append(ex.getMessage());
			msg.append(". ");
		}

		try {
			cpfProfessor = new JComboBox<>(inscricaoController.getCpfProfessores());
		}
		catch (Exception ex)
		{
			msg.append(ex.getMessage());
			msg.append(". ");
		}

		//Para evitar que uma pessoa possa se inscrever 2x no mesmo processo
		codProcesso.addItemListener(e -> {

            try
            {
				if (countInscricoes > 0)
				{
					int cod = Integer.parseInt(codProcesso.getSelectedItem().toString());
					//Redefine os valores do select
					cpfProfessor.setModel(new DefaultComboBoxModel<>(inscricaoController.filterCpfByInscricao(cod)));
				}

				try
				{
					String texto = "";

					for (var item : infosDisciplinas)
					{
						if (item[0].toString().equals(codProcesso.getSelectedItem().toString()))
						{
							codDisciplina.setText(item[1].toString());
						}
					}
				}
				catch (Exception ex)
				{
					codDisciplina.setText("");
				}
            }
            catch (Exception ex)
            {
                showMessage(ex.getMessage());
            }
        });

		if (!msg.toString().isEmpty())
		{
			throw new Exception(msg.toString());
		}

		this.add(codProcesso);
		this.add(cpfProfessor);
		this.add(codDisciplina);
	}

	private void inscricaoForm(boolean edit) {
		clearPanel();

		if (!edit) {
			codProcesso.setSelectedItem(0);
			cpfProfessor.setSelectedItem(0);
			try
			{
				String texto = "";

				for (var item : infosDisciplinas)
				{
					if (item[0].toString().equals(codProcesso.getSelectedItem().toString()))
					{
						codDisciplina.setText(item[1].toString());
					}
				}
			}
			catch (Exception ex)
			{
				codDisciplina.setText("");
			}
		}

		try
		{
			//atualiza todos os valores dos selects
			codProcesso.setModel(new DefaultComboBoxModel<>(inscricaoController.getCodProcessos()));
			int selected = Integer.parseInt(codProcesso.getSelectedItem().toString());

			if (countInscricoes > 0)
			{
				cpfProfessor.setModel(new DefaultComboBoxModel<>(inscricaoController.filterCpfByInscricao(selected)));
			}
			else
			{
				cpfProfessor.setModel(new DefaultComboBoxModel<>(inscricaoController.getCpfProfessores()));
			}
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}

		int width = 300;
		int height = 25;
		int x = 100;

		JLabel lblCodProcesso = new JLabel("Código do Processo:");
		lblCodProcesso.setBounds(x, 70, width, height);
		codProcesso.setBounds(x, 100, width, height);

		JLabel lblCpfProfessor = new JLabel("CPF do Professor:");
		lblCpfProfessor.setBounds(x, codProcesso.getY() + 50, width, height);
		cpfProfessor.setBounds(x, codProcesso.getY() + 70, width, height);

		JLabel lblCodDisciplina = new JLabel("Código da Disciplina:");
		lblCodDisciplina.setBounds(x, cpfProfessor.getY() + 50, width, height);
		codDisciplina.setBounds(x, cpfProfessor.getY() + 70, width, height);
		codDisciplina.setEditable(false);

		JButton btnSubmit = new JButton(edit ? "Editar Inscrição" : "Cadastrar Inscrição");
		btnSubmit.setBounds(x, codDisciplina.getY() + 40, 200, height + 10);

		btnSubmit.addActionListener(e -> {
			try {
				submitInscricao(edit);
			} catch (Exception ex) {
				popupView.showPopup(parentData, ex.getMessage(), "Erro");
			}
		});

		this.add(lblCodProcesso);
		this.add(codProcesso);

		this.add(lblCpfProfessor);
		this.add(cpfProfessor);

		this.add(lblCodDisciplina);
		this.add(codDisciplina);

		this.add(btnSubmit);
		this.repaint();
	}

	private void submitInscricao(boolean edit) throws Exception {
		Inscricao inscricao = new Inscricao();
		Border borderError = BorderFactory.createLineBorder(Color.RED, 3);
		Border borderNormal = new JTextField().getBorder();
		boolean error = false;

		codProcesso.setBorder(borderNormal);
		cpfProfessor.setBorder(borderNormal);
		codDisciplina.setBorder(borderNormal);

		if (codProcesso.getSelectedItem() == null) {
			codProcesso.setBorder(borderError);
			error = true;
		}
		if (cpfProfessor.getSelectedItem() == null) {
			cpfProfessor.setBorder(borderError);
			error = true;
		}

		if (error)
		{
			throw new Exception("Preencha os campos corretamente!");
		}

		inscricao.setCodInscricao(inscricaoController.lastId() + 1);
		inscricao.setCodProcesso(Integer.parseInt(codProcesso.getSelectedItem().toString()));
		inscricao.setCpfProfessor(cpfProfessor.getSelectedItem().toString());
		inscricao.setCodDisciplina(Integer.parseInt(codDisciplina.getText()));

		if (edit)
		{
			inscricaoController.put(inscricao);
		}
		else
		{
			inscricaoController.post(inscricao);
		}

		popupView.showPopup(parentData, edit ? "Inscrição editada com sucesso!" : "Inscrição cadastrada com sucesso!", "Sucesso");

		codProcesso.setSelectedIndex(0);
		cpfProfessor.setSelectedIndex(0);
		codDisciplina.setText(Integer.toString(inscricaoController.getByCodigo(Integer.parseInt(codProcesso.getSelectedItem().toString())).getCodDisciplina()));

		populateInscricoes();
	}

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
		this.revalidate();
		this.repaint();
		this.setLayout(null);

		this.add(btnListarInscricoes);
		this.add(btnCadastrarInscricao);
		this.updateUI();
	}
}