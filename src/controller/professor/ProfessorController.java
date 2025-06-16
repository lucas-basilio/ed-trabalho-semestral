package controller.professor;

import model.dataStructures.Lista;
import model.professor.Professor;
import model.professor.ProfessorModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfessorController implements ActionListener {

    private final ProfessorModel professorModel = new ProfessorModel();

    public Object[][] get() throws Exception
    {
        return this.professorModel.populateTable();
    }

    public String[] getFields()
    {
        return this.professorModel.getFields();
    }

    public Professor getByCpf(String cpf) throws Exception
    {
        return this.professorModel.getProfessorByCPF(cpf);
    }

    public Lista<Professor> getInscritos(int codigo) throws Exception
    {
        return this.professorModel.getProfessorByDisciplina(codigo);
    }

    public void post(Professor professor) throws Exception
    {
        this.professorModel.postProfessor(professor.toString());
    }

    public void put(Professor professor, String cpf) throws Exception
    {
        this.professorModel.putProfessor(professor, cpf);
    }

    public void delete(String cpf) throws Exception
    {
        this.professorModel.deleteProfessor(cpf);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

    }
}
