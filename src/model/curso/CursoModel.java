package model.curso;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.disciplina.Disciplina;
import model.inscricao.Inscricao;
import model.professor.Professor;
import model.utils.FileManager;

public class CursoModel {

    private final FileManager manager = new FileManager("src/data/cursos.csv");

    //region Usar filas no GET e POST
    public Fila<Curso> getCursos() throws Exception
    {
        Fila<Curso> cursos = new Fila<>();
        String result = this.manager.readFileToString();

        if (result.isBlank())
        {
            throw new Exception("Não há cursos cadastrados");
        }

        String[] values = result.split(",");

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

    public Integer[] getCodCursos() throws Exception
    {
        Fila<Curso> cursos = getCursos();
        int size = cursos.size();
        Integer[] values = new Integer[size];

        for (int x = 0; x < size; x++)
        {
            Curso temp = cursos.remove();
            values[x] = temp.getCodCurso();
        }

        return values;
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
                cursos.remove(x + 1);
                break;
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

    public void deleteCurso(int cod) throws Exception
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

            if (temp.getCodCurso() == cod)
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

    public int count()
    {
        try
        {
            var values = getCursos();
            return values.size();
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    public int lastId()
    {
        try
        {
            var values = getCursos();
            Curso temp = new Curso();

            while (!values.isEmpty())
            {
                temp = values.remove();
            }

            return temp.getCodCurso();
        }
        catch (Exception ex)
        {
            return 0;
        }
    }
}
