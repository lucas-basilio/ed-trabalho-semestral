package controller.curso;

import model.curso.Curso;
import model.curso.CursoModel;

public class CursoController {
    private final CursoModel cursoModel = new CursoModel();

    public Object[][] get() throws Exception
    {
        return this.cursoModel.populateTable();
    }

    public String[] getFields()
    {
        return new Curso().properties();
    }

    public Curso getByCpf(int cod) throws Exception
    {
        return this.cursoModel.getCursoByCod(cod);
    }

    public void post(Curso curso) throws Exception
    {
        this.cursoModel.postCurso(curso.toString());
    }

    public void put(Curso curso) throws Exception
    {
        this.cursoModel.putCurso(curso);
    }

    public void delete(int codCurso) throws Exception
    {
        this.cursoModel.deleteCurso(codCurso);
    }
}
