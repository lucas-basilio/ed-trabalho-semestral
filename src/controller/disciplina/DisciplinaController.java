package controller.disciplina;

import model.dataStructures.Fila;
import model.dataStructures.HashTable;
import model.disciplina.Disciplina;
import model.disciplina.DisciplinaModel;

public class DisciplinaController {

    private final DisciplinaModel disciplinaModel = new DisciplinaModel();

    public Object[][] get() throws Exception
    {
        return this.disciplinaModel.populateTable();
    }

    //public HashTable<Disciplina>

    public Disciplina getByCod(int cod) throws Exception
    {
        return this.disciplinaModel.getDisciplinaByCod(cod);
    }

    public void post(Disciplina disciplina) throws Exception
    {
        this.disciplinaModel.postDisciplina(disciplina.toCSV());
    }

    public void put(Disciplina disciplina) throws Exception
    {
        this.disciplinaModel.putDisciplina(disciplina);
    }

    public void delete(Disciplina disciplina) throws Exception
    {
        this.disciplinaModel.deleteDisciplina(disciplina);
    }
}
