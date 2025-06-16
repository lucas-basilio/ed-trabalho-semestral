package view.disciplinaView;

import controller.disciplina.DisciplinaController;
import model.disciplina.Disciplina;
import view.components.PopupView;
import view.components.TableView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisciplinaView extends JPanel {

	private static final long serialVersionUID = 1L;

	DisciplinaController disciplinaController;

	private JTextField nomeDisciplina;
	private JComboBox<String> diaDisciplina;
	private JTextField horarioDisciplina;
	private JTextField horasDisciplina;
	private JComboBox<Integer> codCursoBox;

	private JButton btnListarDisciplinas;
	private JButton btnCadastarDisciplina;
	private JLabel labelMessage;

	private PopupView popupView;
	private JPanel parentData;

	private int selectedCodDisciplina;
	private int countDisciplinas;

	public DisciplinaView(JFrame parent) {
		setSize(parent.getSize());
		this.setLayout(null);

		popupView = new PopupView();
		disciplinaController = new DisciplinaController();

		btnListarDisciplinas = new JButton("Listar Disciplinas");
		btnListarDisciplinas.setBounds(160, 11, 160, 35);

		btnCadastarDisciplina = new JButton("Cadastrar Disciplina");
		btnCadastarDisciplina.setBounds(460, 11, 160, 35);

		labelMessage = new JLabel("", SwingConstants.CENTER);
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setSize(220, 35);
		labelMessage.setVisible(false);

		//Pra não ter risco de digitar diferente
		String[] dias = {
				"Segunda-feira", "Terça-feira", "Quarta-feira",
				"Quinta-feira", "Sexta-feira", "Sábado", "Domingo"
		};

		nomeDisciplina = new JTextField();
		diaDisciplina = new JComboBox<>(dias);
		horarioDisciplina = new JTextField();
		horasDisciplina = new JTextField();

		try
		{
			codCursoBox = new JComboBox<>(this.disciplinaController.getCodCursos());
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}

		btnListarDisciplinas.addActionListener(e -> {
			clearPanel();

			try
			{
				populateDisciplinas();
			}
			catch (Exception ex)
			{
				showMessage(ex.getMessage());
			}
		});

		btnCadastarDisciplina.addActionListener(e -> {
			try
			{
				disciplinaForm(false);
			}
			catch (Exception ex)
			{
				showMessage(ex.getMessage());
			}
		});

		this.add(btnListarDisciplinas);
		this.add(btnCadastarDisciplina);

		try
		{
			populateDisciplinas();
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}
	}

	private void populateDisciplinas() throws Exception
	{
		clearPanel();

		JTable tableDisciplinas = new TableView(disciplinaController.get(), disciplinaController.getFields());

		countDisciplinas = tableDisciplinas.getRowCount();

		tableDisciplinas.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = tableDisciplinas.rowAtPoint(e.getPoint());
				int column = tableDisciplinas.columnAtPoint(e.getPoint());

				if ((row >= 0 && column >= 0)) {
					//EDITAR
					if (column == 6)
					{
						selectedCodDisciplina = (int) tableDisciplinas.getValueAt(row, 0);
						nomeDisciplina.setText((String) tableDisciplinas.getValueAt(row, 1));
						diaDisciplina.setSelectedItem(tableDisciplinas.getValueAt(row, 2));
						horarioDisciplina.setText((String) tableDisciplinas.getValueAt(row, 3));
						horasDisciplina.setText(Double.toString((double)tableDisciplinas.getValueAt(row, 4)));
						codCursoBox.setSelectedItem(tableDisciplinas.getValueAt(row, 5));

						try
						{
							disciplinaForm(true);
						}
						catch (Exception ex)
						{
							showMessage(ex.getMessage());
						}
					}
					//EXCLUIR
					else if (column == 7)
					{
						try
						{
							disciplinaController.delete(Integer.parseInt(tableDisciplinas.getValueAt(row, 0).toString()));
							countDisciplinas--;
							popupView.showPopup(parentData, "Disciplina excluida com sucesso", "Sucesso");
							populateDisciplinas();
						}
						catch (Exception ex)
						{
							clearPanel();
							showMessage(ex.getMessage());
						}
					}
				}
			}
		});

		JScrollPane tableProfessorScroll = new JScrollPane(tableDisciplinas);
		tableProfessorScroll.setBounds(tableDisciplinas.getBounds());
		this.add(tableProfessorScroll);
	}

	private void disciplinaForm(boolean edit) throws Exception
	{
		clearPanel();

		//Caso a pessoa cadastre o curso, atualiza o select
		try
		{
			codCursoBox = new JComboBox<>(this.disciplinaController.getCodCursos());
		}
		catch (Exception ex)
		{
			this.clearPanel();
			throw new Exception(ex.getMessage() + ". Insira ao menos um curso para continuar");
		}

		if (!edit)
		{
			nomeDisciplina.setText("");
			diaDisciplina.setSelectedIndex(0);
			horarioDisciplina.setText("");
			horasDisciplina.setText("");
		}

		int width = 300;
		int height = 25;
		int x = 100;

		JLabel labelNome = new JLabel("Nome da disciplina:");
		labelNome.setBounds(x, 70, width, height);
		nomeDisciplina.setBounds(x, 100, width, height);

		JLabel labelDia = new JLabel("Dia da disciplina:");
		labelDia.setBounds(x, nomeDisciplina.getY() + 50, width, height);
		diaDisciplina.setBounds(x, nomeDisciplina.getY() + 70, width / 2, height);

		JLabel labelHorario = new JLabel("Horário da disciplina:");
		labelHorario.setBounds(x, diaDisciplina.getY() + 50, width, height);
		horarioDisciplina.setBounds(x, diaDisciplina.getY() + 70, width, height);

		JLabel labelHorasDia = new JLabel("Horas por dia:");
		labelHorasDia.setBounds(x, horarioDisciplina.getY() + 50, width, height);
		horasDisciplina.setBounds(x, horarioDisciplina.getY() + 70, width, height);

		JLabel labelCodCurso = new JLabel("Código do curso:");
		labelCodCurso.setBounds(x, horasDisciplina.getY() + 50, width, height);
		codCursoBox.setBounds(x, horasDisciplina.getY() + 70, width / 3, height);

		JButton btnSubmitDisciplina = new JButton(edit ? "Editar Disciplina" : "Cadastrar Disciplina");
		btnSubmitDisciplina.setBounds(x, codCursoBox.getY() + 40, 160, height + 10);

		btnSubmitDisciplina.addActionListener(e -> {
			try
			{
				submitDisciplina(edit);
			}
			catch (Exception ex)
			{
				popupView.showPopup(parentData, ex.getMessage(), "Falha");
			}
		});

		this.add(labelNome);
		this.add(labelDia);
		this.add(labelHorario);
		this.add(labelHorasDia);
		this.add(labelCodCurso);

		this.add(nomeDisciplina);
		this.add(diaDisciplina);
		this.add(horarioDisciplina);
		this.add(horasDisciplina);
		this.add(codCursoBox);

		this.add(btnSubmitDisciplina);

		this.repaint();
	}

	public void submitDisciplina(boolean edit) throws Exception
	{
		Disciplina disciplina = new Disciplina();
		Border borderLineError = BorderFactory.createLineBorder(Color.RED, 3);
		Border borderLine = new JTextField().getBorder();
		boolean error = false;

		nomeDisciplina.setBorder(borderLine);
		diaDisciplina.setBorder(borderLine);
		horarioDisciplina.setBorder(borderLine);
		horasDisciplina.setBorder(borderLine);
		codCursoBox.setBorder(borderLine);

		if (this.nomeDisciplina.getText().trim().isEmpty())
		{
			this.nomeDisciplina.setBorder(borderLineError);
			error = true;
		}

		if (this.diaDisciplina.getSelectedItem() == null)
		{
			this.diaDisciplina.setBorder(borderLineError);
			error = true;
		}

		if (this.horarioDisciplina.getText().trim().isEmpty())
		{
			this.horarioDisciplina.setBorder(borderLineError);
			error = true;
		}

		if (this.horasDisciplina.getText().trim().isEmpty())
		{
			this.horasDisciplina.setBorder(borderLineError);
			error = true;
		}

		if (codCursoBox.getSelectedItem() == null)
		{
			this.codCursoBox.setBorder(borderLineError);
			throw new Exception("Cadastre um curso antes");
		}

		if (error)
		{
			throw new Exception("Preencha os campos corretamente");
		}

		disciplina.setCodDisciplina(selectedCodDisciplina);
		disciplina.setNome(this.nomeDisciplina.getText().trim());
		disciplina.setDia((String) this.diaDisciplina.getSelectedItem());
		disciplina.setHorario(this.horarioDisciplina.getText().trim());
		disciplina.setHorasDia(Double.parseDouble(this.horasDisciplina.getText().trim()));
		disciplina.setCodCurso((Integer) codCursoBox.getSelectedItem());

		if (!edit)
		{
			disciplina.setCodDisciplina(countDisciplinas);
			disciplinaController.post(disciplina);
		}
		else
		{
			disciplinaController.put(disciplina);
		}

		popupView.showPopup(parentData, edit ? "Disciplina editado com sucesso." : "Disciplina criado com sucesso", "Sucesso");

		nomeDisciplina.setText("");
		diaDisciplina.setSelectedIndex(0);
		horarioDisciplina.setText("");
		horasDisciplina.setText("");
		codCursoBox.setSelectedIndex(0);

		populateDisciplinas();
	}

	//region Utils da classe
	//Para setar o painel pai, apesar de ser o this
	public void applyPopup(JPanel parent)
	{
		this.parentData = parent;
	}

	public void showMessage(String message) {
		clearPanel();
		labelMessage.setText(message);

		// Centraliza no painel atual
		int x = (getWidth() - labelMessage.getWidth()) / 2;
		int y = (getHeight() - labelMessage.getHeight()) / 2;
		labelMessage.setLocation(x, y);
		labelMessage.setVisible(true);

		setLayout(new BorderLayout());
		add(labelMessage, BorderLayout.CENTER);

		repaint();
	}

	public void clearPanel()
	{
		this.removeAll();
		this.setLayout(null);

		this.add(btnListarDisciplinas);
		this.add(btnCadastarDisciplina);

		this.updateUI();
	}
	//endregion
}
