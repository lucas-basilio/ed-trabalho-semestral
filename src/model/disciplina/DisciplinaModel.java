package model.disciplina;

import model.curso.Curso;
import model.curso.CursoModel;
import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.inscricao.Inscricao;
import model.inscricao.InscricaoModel;
import model.processo.ProcessoModel;
import model.utils.FileManager;

public class DisciplinaModel{
    private final FileManager manager = new FileManager("src/data/disciplinas.csv");
    private final CursoModel cursoModel = new CursoModel();

    public Fila<Disciplina> getDisciplinas() throws Exception
    {
        Fila<Disciplina> disciplinas = new Fila<>();
        String result = this.manager.readFileToString();

        if (result.isBlank())
        {
            throw new Exception("Não há disciplinas cadastradas");
        }

        String[] values = result.split(",");

        for (int x = 0; x < values.length; x += 6)
        {
            Disciplina disciplina = new Disciplina();

            disciplina.setCodDisciplina(Integer.parseInt(values[x]));
            disciplina.setNome(values[x + 1]);
            disciplina.setDia(values[x + 2]);
            disciplina.setHorario(values[x + 3]);
            disciplina.setHorasDia(Double.parseDouble(values[x + 4]));
            disciplina.setCodCurso(Integer.parseInt(values[x + 5]));

            disciplinas.insert(disciplina);
        }

        return disciplinas;
    }

    public String[] getFields()
    {
        return new Disciplina().properties();
    }

    public Integer[] getCodCursos() throws Exception
    {
        return cursoModel.getCodCursos();
    }

    public Integer[] getCodDisciplinas(int[] exclude) throws Exception
    {
        Fila<Disciplina> disciplinas = getDisciplinas();
        Fila<Integer> filteredItems = new Fila<>();
        int size = disciplinas.size();

        for (int x = 0; x < size; x++)
        {
            boolean contains = false;
            Disciplina temp = disciplinas.remove();

            //Deixar o exclude opcional
            if (exclude != null)
            {
                for (int y = 0; y < exclude.length; y++)
                {
                    if (exclude[y] == temp.getCodDisciplina())
                    {
                        contains = true;
                    }
                }
            }

            if (!contains)
            {
                filteredItems.insert(temp.getCodDisciplina());
            }
        }

        //Tudo isso pq não pode usar primitivo
        if (filteredItems.isEmpty())
        {
            throw new Exception("Não há disciplinas disponíveis pois todas possuem processo ativo");
        }

        Integer[] values = new Integer[filteredItems.size()];

        for (int x = 0; x < values.length; x++)
        {
            values[x] = filteredItems.remove();
        }

        return values;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Disciplina> fila = getDisciplinas();
        int size = fila.size();

        Object[][] dados = new Object[size][6];

        for (int x = 0; x < size; x++)
        {
            Disciplina temp = fila.remove();

            dados[x][0] = temp.getCodDisciplina();
            dados[x][1] = temp.getNome();
            dados[x][2] = temp.getDia();
            dados[x][3] = temp.getHorario();
            dados[x][4] = temp.getHorasDia();
            dados[x][5] = temp.getCodCurso();
        }

        return dados;
    }

    public Disciplina getDisciplinaByCod(int codigo) throws Exception
    {
        Fila<Disciplina> disciplinas = getDisciplinas();

        while (!disciplinas.isEmpty())
        {
            Disciplina temp = disciplinas.remove();

            if (temp.getCodDisciplina() == codigo)
            {
                return temp;
            }
        }

        throw new Exception("Disciplina não encontrada.");
    }

    public void postDisciplina(String valor) throws Exception
    {
        this.manager.writeIntoFile(valor, true);
    }

    //region Usar listas encadeadas no PUT e DELETE
    public void putDisciplina(Disciplina disciplina) throws Exception
    {
        Fila<Disciplina> filaDisciplina = getDisciplinas();
        Lista<Disciplina> disciplinas = new Lista<>();

        while (!filaDisciplina.isEmpty())
        {
            disciplinas.addLast(filaDisciplina.remove());
        }

        for (int x = 0; x < disciplinas.size(); x++)
        {
            Disciplina temp = (Disciplina)disciplinas.get(x).dado;

            if (temp.getCodDisciplina() == disciplina.getCodDisciplina())
            {
                disciplinas.add(disciplina, x);
                disciplinas.remove(x + 1);
                break;
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < disciplinas.size(); x++)
        {
            str.append(disciplinas.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteDisciplina (int cod) throws Exception
    {
        InscricaoModel inscricaoModel = new InscricaoModel();

        Fila<Disciplina> filaDisciplina = getDisciplinas();
        Lista<Disciplina> disciplinas = new Lista<>();

        while (!filaDisciplina.isEmpty())
        {
            disciplinas.addLast(filaDisciplina.remove());
        }

        for (int x = 0; x < disciplinas.size(); x++)
        {
            Disciplina temp = (Disciplina)disciplinas.get(x).dado;
            ProcessoModel processoModel = new ProcessoModel();

            if (temp.getCodDisciplina() == cod)
            {
                //Remove as inscrições associadas àquela disciplina
                try
                {
                   processoModel.deleteProcesso(processoModel.getProcessoByDisciplina(cod).getCodProcesso());
                }
                catch (Exception ex)
                {

                }
                disciplinas.remove(x);
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < disciplinas.size(); x++)
        {
            str.append(disciplinas.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion

    public int count()
    {
        try
        {
            var values = getDisciplinas();
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
            var values = getDisciplinas();
            Disciplina temp = new Disciplina();

            while (!values.isEmpty())
            {
                temp = values.remove();
            }

            return temp.getCodDisciplina();
        }
        catch (Exception ex)
        {
            return 0;
        }
    }
}
