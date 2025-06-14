package model.curso;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.disciplina.Disciplina;
import model.professor.Professor;
import model.utils.FileManager;

public class CursoModel {

    private final FileManager manager = new FileManager("src/data/cursos.csv");

    //region Usar filas no GET e POST
    private Fila<Curso> getCursos() throws Exception
    {
        String[] values = this.manager.readFileToString().split(",");
        Fila<Curso> cursos = new Fila<>();

        for (int x = 0; x < values.length; x += 3)
        {
            Curso curso = new Curso();

            curso.setCodCurso(Integer.parseInt(values[x].trim()));
            curso.setNome(values[x + 1].trim());
            curso.setArea(values[x + 2].trim());

            cursos.insert(curso);
        }

        return cursos;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Curso> fila = getCursos();
        int size = fila.size();

        Object[][] dados = new Object[size][3];

        for (int x = 0; x < size; x++)
        {
            Curso temp = fila.remove();

            dados[x][0] = temp.getCodCurso();
            dados[x][1] = temp.getNome();
            dados[x][2] = temp.getArea();
        }

        return dados;
    }

    public Curso getCursoByCod(int codigo) throws Exception
    {
        Fila<Curso> cursos = getCursos();

        while (!cursos.isEmpty())
        {
            Curso temp = cursos.remove();

            if (temp.getCodCurso() == codigo)
            {
                return temp;
            }
        }

        throw new Exception("Curso não encontrado.");
    }

    public void postCurso(String valor) throws Exception
    {
        this.manager.writeIntoFile(valor, true);
    }
    //endregion

    //region Usar listas encadeadas no PUT e DELETE
    public void putCurso(Curso curso) throws Exception
    {
        Fila<Curso> filaCursos = getCursos();
        Lista<Curso> cursos = new Lista<>();

        while (!filaCursos.isEmpty())
        {
            cursos.addLast(filaCursos.remove());
        }

        for (int x = 0; x < cursos.size(); x++)
        {
            Curso temp = (Curso)cursos.get(x).dado;

            if (temp.getCodCurso() == curso.getCodCurso())
            {
                cursos.add(curso, x);
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < cursos.size(); x++)
        {
            str.append(cursos.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteCurso(Curso curso) throws Exception
    {
        Fila<Curso> filaCursos = getCursos();
        Lista<Curso> cursos = new Lista<>();

        while (!filaCursos.isEmpty())
        {
            cursos.addLast(filaCursos.remove());
        }

        for (int x = 0; x < cursos.size(); x++)
        {
            Curso temp = (Curso)cursos.get(x).dado;

            if (temp.equals(curso))
            {
                cursos.remove(x);
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < cursos.size(); x++)
        {
            str.append(cursos.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion
}
