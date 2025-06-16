package view.professorView;

import controller.professor.ProfessorController;
import jdk.jshell.spi.ExecutionControl;
import model.professor.Professor;
import view.components.PopupView;
import view.components.TableButtonView;
import view.components.TableView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProfessorView extends JPanel {

	private static final long serialVersionUID = 1L;

	private ProfessorController professorController;

	private JButton btnListarProfessores;
	private JButton btnCadastraProfessor;

	private JTextField nomeProfessor;
	private JTextField cpfProfessor;
	private JTextField areaProfessor;
	private JFormattedTextField pontosProfessor;

	private String editCpf;

	private PopupView popupView;
	private JPanel parentData;
	private NumberFormatter formatter;

	private JLabel labelMessage;

	public ProfessorView(JFrame parent) {
		setSize(parent.getSize());

		professorController = new ProfessorController();

		formatter = new NumberFormatter(new DecimalFormat("#0.0"));
		formatter.setValueClass(Double.class);
		formatter.setAllowsInvalid(false);
		formatter.setMinimum(0.0);

		popupView = new PopupView();
		this.setLayout(null);

		btnListarProfessores = new JButton("Listar Professores");
		btnListarProfessores.setBounds(160, 11, 160, 35);

		btnCadastraProfessor = new JButton("Cadastrar Professor");
		btnCadastraProfessor.setBounds(460, 11, 160, 35);

		labelMessage = new JLabel("", SwingConstants.CENTER);
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setSize(220, 35);
		labelMessage.setVisible(false);

		nomeProfessor = new JTextField();
		cpfProfessor = new JTextField();
		areaProfessor = new JTextField();
		pontosProfessor = new JFormattedTextField(formatter);

		btnListarProfessores.addActionListener(e -> {
			clearPanel();

			try
			{
				populateProfessores();
			}
			catch (Exception ex)
			{
				showMessage(ex.getMessage());
			}
		});

		btnCadastraProfessor.addActionListener(e -> professorForm(false));

		this.add(btnListarProfessores);
		this.add(btnCadastraProfessor);

		try
		{
			populateProfessores();
		}
		catch (Exception ex)
		{
			showMessage(ex.getMessage());
		}
	}

	//Cria e popula a listagem de professores
	private void populateProfessores() throws Exception
	{
		clearPanel();

		JTable tableProfessor = new TableView(professorController.get(), professorController.getFields());

		tableProfessor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableProfessor.rowAtPoint(e.getPoint());
				int column = tableProfessor.columnAtPoint(e.getPoint());

				if ((row >= 0 && column >= 0)) {
					if (column == 4)
					{
						nomeProfessor.setText((String) tableProfessor.getValueAt(row, 0));
						cpfProfessor.setText((String) tableProfessor.getValueAt(row, 1));
						areaProfessor.setText((String) tableProfessor.getValueAt(row, 2));
						pontosProfessor.setText(Double.toString((double)tableProfessor.getValueAt(row, 3)));

						professorForm(true);
					}
					else if (column == 5)
					{
						try
						{
							professorController.delete((String) tableProfessor.getValueAt(row, 1));
							popupView.showPopup(parentData, "Professor excluido com sucesso", "Sucesso");
							populateProfessores();
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

		JScrollPane tableProfessorScroll = new JScrollPane(tableProfessor);
		tableProfessorScroll.setBounds(tableProfessor.getBounds());
		this.add(tableProfessorScroll);
	}

	//Cria a aba de Cadastro de Professor
	private void professorForm(boolean edit)
	{
		clearPanel();

		if (!edit)
		{
			nomeProfessor.setText("");
			cpfProfessor.setText("");
			areaProfessor.setText("");
			pontosProfessor.setText("");
		}
		else
		{
			editCpf = cpfProfessor.getText();
		}

		int width = 300;
		int height = 25;
		int x = 100;

		JLabel labelNome = new JLabel("Nome do professor:");
		labelNome.setBounds(x, 70, width, height);
		nomeProfessor.setBounds(x, 100, width, height);

		JLabel labelCpf = new JLabel("CPF do professor:");
		labelCpf.setBounds(x, nomeProfessor.getY() + 60, width, height);
		cpfProfessor.setBounds(x, nomeProfessor.getY() + 90, width, height);

		JLabel labelArea = new JLabel("Área de atuação do professor:");
		labelArea.setBounds(x, cpfProfessor.getY() + 60, width, height);
		areaProfessor.setBounds(x, cpfProfessor.getY() + 90, width, height);

		JLabel labelPontos = new JLabel("Pontos do professor:");
		labelPontos.setBounds(x, areaProfessor.getY() + 60, width, height);
		pontosProfessor.setBounds(x, areaProfessor.getY() + 90, width, height);

		JButton btnSubmitProfessor = new JButton(edit ? "Editar Professor" : "Cadastrar Professor");
		btnSubmitProfessor.setBounds(x, pontosProfessor.getY() + 40, 160, height + 10);

		btnSubmitProfessor.addActionListener(e -> {
			try
			{
				submitProfessor(edit);
			}
			catch (Exception ex)
			{
				popupView.showPopup(parentData, ex.getMessage(), "Falha");
			}
		});

		this.add(labelNome);
		this.add(labelCpf);
		this.add(labelArea);
		this.add(labelPontos);

		this.add(nomeProfessor);
		this.add(cpfProfessor);
		this.add(areaProfessor);
		this.add(pontosProfessor);

		this.add(btnSubmitProfessor);

		this.repaint();
	}

	public void submitProfessor(boolean edit) throws Exception
	{
		Professor professor = new Professor();
		Border borderLineError = BorderFactory.createLineBorder(Color.RED, 3);
		Border borderLine = new JTextField().getBorder();
		boolean error = false;

		nomeProfessor.setBorder(borderLine);
		cpfProfessor.setBorder(borderLine);
		areaProfessor.setBorder(borderLine);
		pontosProfessor.setBorder(borderLine);

		if (this.nomeProfessor.getText().trim().isEmpty())
		{
			this.nomeProfessor.setBorder(borderLineError);
			error = true;
		}

		if (this.cpfProfessor.getText().trim().isEmpty())
		{
			this.cpfProfessor.setBorder(borderLineError);
			error = true;
		}

		if (this.areaProfessor.getText().trim().isEmpty())
		{
			this.areaProfessor.setBorder(borderLineError);
			error = true;
		}

		if (this.pontosProfessor.getText().trim().isEmpty())
		{
			this.pontosProfessor.setBorder(borderLineError);
			error = true;
		}

		if (error)
		{
			throw new Exception("Preencha os campos corretamente");
		}

		professor.setNome(this.nomeProfessor.getText().trim());
		professor.setCpf(this.cpfProfessor.getText().trim());
		professor.setArea(this.areaProfessor.getText().trim());
		professor.setPontos(Double.parseDouble(this.pontosProfessor.getText().trim().replace(",", ".")));

		if (!edit)
		{
			professorController.post(professor);
		}
		else
		{
			professorController.put(professor, editCpf);
		}

		popupView.showPopup(parentData, edit ? "Professor editado com sucesso." : "Professor criado com sucesso", "Sucesso");

		nomeProfessor.setText("");
		cpfProfessor.setText("");
		areaProfessor.setText("");
		pontosProfessor.setText("");

		populateProfessores();
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

		this.add(btnListarProfessores);
		this.add(btnCadastraProfessor);

		this.updateUI();
	}
	//endregion
}
