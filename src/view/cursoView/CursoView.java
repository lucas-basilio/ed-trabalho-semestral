package view.cursoView;

import controller.curso.CursoController;
import model.curso.Curso;
import view.components.PopupView;
import view.components.TableView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CursoView extends JPanel {

	private static final long serialVersionUID = 1L;

	CursoController cursoController;

	private JTextField nomeCurso;
	private JTextField areaCurso;

	private JButton btnListarCursos;
	private JButton btnCadastrarCurso;
	private JLabel labelMessage;

	private PopupView popupView;
	private JPanel parentData;

	private int selectedCodCurso;
	private int countCursos;

	public CursoView(JFrame parent) {
		setSize(parent.getSize());
		this.setLayout(null);

		popupView = new PopupView();
		cursoController = new CursoController();

		btnListarCursos = new JButton("Listar Cursos");
		btnListarCursos.setBounds(160, 11, 160, 35);

		btnCadastrarCurso = new JButton("Cadastrar Curso");
		btnCadastrarCurso.setBounds(460, 11, 160, 35);

		labelMessage = new JLabel("", SwingConstants.CENTER);
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setSize(220, 35);
		labelMessage.setVisible(false);

		nomeCurso = new JTextField();
		areaCurso = new JTextField();

		btnListarCursos.addActionListener(e -> {
			clearPanel();

			try {
				populateCursos();
			} catch (Exception ex) {
				showMessage(ex.getMessage());
			}
		});

		btnCadastrarCurso.addActionListener(e -> cursoForm(false));

		this.add(btnListarCursos);
		this.add(btnCadastrarCurso);

		try {
			populateCursos();
		} catch (Exception ex) {
			showMessage(ex.getMessage());
		}
	}

	private void populateCursos() throws Exception {
		clearPanel();

		JTable tableCursos = new TableView(cursoController.get(), cursoController.getFields());
		countCursos = tableCursos.getRowCount();

		tableCursos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tableCursos.rowAtPoint(e.getPoint());
				int column = tableCursos.columnAtPoint(e.getPoint());

				if ((row >= 0 && column >= 0)) {
					if (column == 3) { // Editar
						selectedCodCurso = (int) tableCursos.getValueAt(row, 0);
						nomeCurso.setText((String) tableCursos.getValueAt(row, 1));
						areaCurso.setText((String) tableCursos.getValueAt(row, 2));
						cursoForm(true);
					} else if (column == 4) { // Excluir
						try {
							cursoController.delete((int) tableCursos.getValueAt(row, 0));
							countCursos--;
							popupView.showPopup(parentData, "Curso excluído com sucesso", "Sucesso");
							populateCursos();
						} catch (Exception ex) {
							clearPanel();
							showMessage(ex.getMessage());
						}
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(tableCursos);
		scrollPane.setBounds(tableCursos.getBounds());
		this.add(scrollPane);
	}

	private void cursoForm(boolean edit) {
		clearPanel();

		if (!edit) {
			nomeCurso.setText("");
			areaCurso.setText("");
		}

		int width = 300;
		int height = 25;
		int x = 100;

		JLabel labelNome = new JLabel("Nome do curso:");
		labelNome.setBounds(x, 70, width, height);
		nomeCurso.setBounds(x, 100, width, height);

		JLabel labelArea = new JLabel("Área do curso:");
		labelArea.setBounds(x, 150, width, height);
		areaCurso.setBounds(x, 180, width, height);

		JButton btnSubmitCurso = new JButton(edit ? "Editar Curso" : "Cadastrar Curso");
		btnSubmitCurso.setBounds(x, 310, 160, height + 10);

		btnSubmitCurso.addActionListener(e -> {
			try {
				submitCurso(edit);
			} catch (Exception ex) {
				popupView.showPopup(parentData, ex.getMessage(), "Falha");
			}
		});

		this.add(labelNome);
		this.add(labelArea);
		this.add(nomeCurso);
		this.add(areaCurso);
		this.add(btnSubmitCurso);
		this.repaint();
	}

	public void submitCurso(boolean edit) throws Exception {
		Curso curso = new Curso();
		Border borderLineError = BorderFactory.createLineBorder(Color.RED, 3);
		Border borderLine = new JTextField().getBorder();
		boolean error = false;

		nomeCurso.setBorder(borderLine);
		areaCurso.setBorder(borderLine);

		if (nomeCurso.getText().trim().isEmpty()) {
			nomeCurso.setBorder(borderLineError);
			error = true;
		}
		if (areaCurso.getText().trim().isEmpty()) {
			areaCurso.setBorder(borderLineError);
			error = true;
		}

		if (error) {
			throw new Exception("Preencha os campos corretamente");
		}

		curso.setCodCurso(Integer.parseInt(Integer.toString(countCursos)));
		curso.setNome(nomeCurso.getText().trim());
		curso.setArea(areaCurso.getText().trim());

		if (!edit)
		{
			curso.setCodCurso(countCursos);
			cursoController.post(curso);
		}
		else
		{
			curso.setCodCurso(selectedCodCurso);
			cursoController.put(curso);
		}

		popupView.showPopup(parentData, edit ? "Curso editado com sucesso." : "Curso criado com sucesso", "Sucesso");

		nomeCurso.setText("");
		areaCurso.setText("");

		populateCursos();
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
		this.setLayout(null);
		this.add(btnListarCursos);
		this.add(btnCadastrarCurso);
		this.updateUI();
	}
}