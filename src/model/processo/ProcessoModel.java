package model.processo;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.disciplina.DisciplinaModel;
import model.inscricao.Inscricao;
import model.inscricao.InscricaoModel;
import model.utils.FileManager;

public class ProcessoModel {
    private final FileManager manager = new FileManager("src/data/processos.csv");
    private final DisciplinaModel disciplinaModel = new DisciplinaModel();
    //private final InscricaoModel inscricaoModel = new InscricaoModel();

    public Fila<Processo> getProcessos() throws Exception
    {
        String result = this.manager.readFileToString();

        if (result.isBlank())
        {
            throw new Exception("Não há processos cadastrados");
        }

        Fila<Processo> processos = new Fila<>();
        String[] values = result.split(",");

        for (int x = 0; x < values.length; x += 3)
        {
            Processo processo = new Processo();

            processo.setCodProcesso(Integer.parseInt(values[x]));
            processo.setCodDisciplina(Integer.parseInt(values[x + 1]));
            processo.setAtivo(Boolean.parseBoolean(values[x + 2]));

            processos.insert(processo);
        }

        return processos;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Processo> fila = getProcessos();
        int size = fila.size();

        Object[][] dados = new Object[size][3];

        for (int x = 0; x < size; x++)
        {
            Processo temp = fila.remove();

            dados[x][0] = temp.getCodProcesso();
            dados[x][1] = temp.getCodDisciplina();
            dados[x][2] = temp.isAtivo();
        }

        return dados;
    }

    public Integer[] getCodDisciplinas(boolean filter) throws Exception
    {
        if (!filter)
        {
            return disciplinaModel.getCodDisciplinas(null);
        }

        Fila<Processo> processos = getProcessos();
        Fila<Integer> excludeFila = new Fila<>();

        while (!processos.isEmpty())
        {
            Processo tmp = processos.remove();


            if (tmp.isAtivo())
            {
                excludeFila.insert(tmp.getCodDisciplina());
            }
        }

        int[] exclude = new int[excludeFila.size()];

        for (int x = 0; x < exclude.length; x++)
        {
            exclude[x] = excludeFila.remove();
        }

        return disciplinaModel.getCodDisciplinas(exclude);
    }

    public Integer[] getCodProcessos(boolean ativos) throws Exception
    {
        Fila<Processo> processos = getProcessos();
        Fila<Integer> excludeFila = new Fila<>();

        //Caso precise de todos, independente do estado
        if (!ativos)
        {
            Integer[] values = new Integer[processos.size()];

            for (int x = 0; x < values.length; x++)
            {

                values[x] = processos.remove().getCodProcesso();
            }

            return values;
        }

        while (!processos.isEmpty())
        {
            Processo tmp = processos.remove();

            if (tmp.isAtivo())
            {
                excludeFila.insert(tmp.getCodDisciplina());
            }
        }

        int[] exclude = new int[excludeFila.size()];

        for (int x = 0; x < exclude.length; x++)
        {
            exclude[x] = excludeFila.remove();
        }

        return disciplinaModel.getCodDisciplinas(exclude);
    }

    public Processo getProcessoByCod(int codigo) throws Exception
    {
        Fila<Processo> processos = getProcessos();

        while (!processos.isEmpty())
        {
            Processo processo = processos.remove();

            if (processo.getCodProcesso() == codigo)
            {
                return processo;
            }
        }

        throw new Exception("Processo não encontrado.");
    }

    public Processo getProcessoByDisciplina(int codigoDisciplina) throws Exception
    {
        Fila<Processo> processos = getProcessos();

        while (!processos.isEmpty())
        {
            Processo processo = processos.remove();

            if (processo.getCodDisciplina() == codigoDisciplina)
            {
                return processo;
            }
        }

        throw new Exception("Processo não encontrado.");
    }

    public void postProcesso(String valor) throws Exception
    {
        this.manager.writeIntoFile(valor, true);
    }

    //region Usar listas encadeadas no PUT e DELETE
    public void putProcesso(Processo processo, int cod) throws Exception
    {
        Fila<Processo> filaProcessos = getProcessos();
        Lista<Processo> processos = new Lista<>();

        while (!filaProcessos.isEmpty())
        {
            processos.addLast(filaProcessos.remove());
        }

        for (int x = 0; x < processos.size(); x++)
        {
            Processo temp = (Processo) processos.get(x).dado;

            if (temp.getCodProcesso() == cod)
            {
                processos.add(processo, x);
                processos.remove(x + 1);
                break;
            }
        }

        StringBuffer str = new StringBuffer();

        for (int x = 0; x < processos.size(); x++)
        {
            str.append(processos.get(x).dado.toString().trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteProcesso(int codigo) throws Exception
    {
        Fila<Processo> filaProcessos = getProcessos();
        Lista<Processo> processos = new Lista<>();
        Inscricao inscricao =  new Inscricao();
        InscricaoModel inscricaoModel = new InscricaoModel();

        while (!filaProcessos.isEmpty())
        {
            processos.addLast(filaProcessos.remove());
        }

        for (int x = 0; x < processos.size(); x++)
        {
            Processo temp = (Processo) processos.get(x).dado;

            if (temp.getCodProcesso() == codigo)
            {
                processos.remove(x);
                try
                {
                    inscricaoModel.deleteInscricao(inscricaoModel.getInscricaoByDisciplina(temp.getCodDisciplina()).getCodInscricao());
                }
                catch (Exception ex)
                {

                }
            }
        }

        StringBuffer str = new StringBuffer();

        for (int x = 0; x < processos.size(); x++)
        {
            str.append(processos.get(x).dado.toString().trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion

    public int count()
    {
        try
        {
            var values = getProcessos();
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
            var values = getProcessos();
            Processo temp = new Processo();

            while (!values.isEmpty())
            {
                temp = values.remove();
            }

            return temp.getCodProcesso();
        }
        catch (Exception ex)
        {
            return 0;
        }
    }
}