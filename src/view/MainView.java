package view;

import view.cursoView.CursoView;
import view.disciplinaView.DisciplinaView;
import view.inscricaoView.InscricaoView;
import view.processoView.ProcessoView;
import view.professorView.ProfessorView;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private ProfessorView professorTab;
	private DisciplinaView disciplinaTab;
	private InscricaoView inscricaoView;
	private ProcessoView processoView;
	private CursoView cursoView;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					MainView frame = new MainView();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLayout(null);

		//setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(46, 36, 824, 489);

		professorTab = new ProfessorView(this);
		tabbedPane.addTab("Professores", null, professorTab, null);
		professorTab.applyPopup(professorTab);

		disciplinaTab = new DisciplinaView(this);
		tabbedPane.addTab("Disciplinas", null, disciplinaTab, null);
		disciplinaTab.applyPopup(disciplinaTab);

		cursoView = new CursoView(this);
		tabbedPane.addTab("Cursos", null, cursoView, null);
		cursoView.applyPopup(cursoView);

		inscricaoView = new InscricaoView();
		tabbedPane.addTab("Inscrições", null, inscricaoView, null);
		inscricaoView.applyPopup(inscricaoView);

		processoView = new ProcessoView();
		tabbedPane.addTab("Processos", null, processoView, null);
		processoView.applyPopup(processoView);

		contentPane.add(tabbedPane);
	}

}
