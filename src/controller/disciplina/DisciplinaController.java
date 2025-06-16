package controller.disciplina;

import model.disciplina.Disciplina;
import model.disciplina.DisciplinaModel;

public class DisciplinaController {

    private final DisciplinaModel disciplinaModel = new DisciplinaModel();

    public Object[][] get() throws Exception
    {
        return this.disciplinaModel.populateTable();
    }

    public String[] getFields()
    {
        return this.disciplinaModel.getFields();
    }

    public Integer[] getCodCursos() throws Exception
    {
        return this.disciplinaModel.getCodCursos();
    }

    //public HashTable<Disciplina>

    public Disciplina getByCod(int cod) throws Exception
    {
        return this.disciplinaModel.getDisciplinaByCod(cod);
    }

    public void post(Disciplina disciplina) throws Exception
    {
        this.disciplinaModel.postDisciplina(disciplina.toString());
    }

    public void put(Disciplina disciplina) throws Exception
    {
        this.disciplinaModel.putDisciplina(disciplina);
    }

    public void delete(int cod) throws Exception
    {
        this.disciplinaModel.deleteDisciplina(cod);
    }

    public int count()
    {
        return this.disciplinaModel.count();
    }
}
