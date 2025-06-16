package model.inscricao;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.disciplina.Disciplina;
import model.disciplina.DisciplinaModel;
import model.processo.Processo;
import model.processo.ProcessoModel;
import model.professor.ProfessorModel;
import model.utils.FileManager;

public class InscricaoModel {
    private final FileManager manager = new FileManager("src/data/inscricoes.csv");
    private final ProcessoModel processoModel = new ProcessoModel();
    private final ProfessorModel professorModel = new ProfessorModel();
    private final DisciplinaModel disciplinaModel = new DisciplinaModel();

    public Fila<Inscricao> getInscricoes() throws Exception
    {
        String result = this.manager.readFileToString();
        Fila<Inscricao> inscricoes = new Fila<>();

        if (result.isBlank())
        {
            throw new Exception("Não há inscrições cadastradas");
        }

        String[] values = result.split(",");

        for (int x = 0; x < values.length; x += 4)
        {
            Inscricao inscricao = new Inscricao();

            inscricao.setCodInscricao(Integer.parseInt(values[x]));
            inscricao.setCodProcesso(Integer.parseInt(values[x + 1]));
            inscricao.setCpfProfessor(values[x + 2]);
            inscricao.setCodDisciplina(Integer.parseInt(values[x + 3]));

            inscricoes.insert(inscricao);
        }

        return inscricoes;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Inscricao> fila = getInscricoes();
        int size = fila.size();

        Object[][] dados = new Object[size][4];

        for (int x = 0; x < size; x++)
        {
            Inscricao temp = fila.remove();

            dados[x][0] = temp.getCodInscricao();
            dados[x][1] = temp.getCodProcesso();
            dados[x][2] = temp.getCpfProfessor();
            dados[x][3] = temp.getCodDisciplina();
        }

        return dados;
    }

    public Object[][] dadosDisciplina() throws Exception
    {
        var processos = processoModel.getProcessos();
        int size = processos.size();

        Object[][] result = new Object[size][2];

        for (int x = 0; x < size; x++)
        {
            Processo temp = processos.remove();

            result[x][0] = temp.getCodProcesso();
            result[x][1] = temp.getCodDisciplina();
        }

        return result;
    }

    public String[] getCpfProfessores() throws Exception
    {
        return professorModel.getCpfProfessores();
    }

    public Integer[] getCodProcessos() throws Exception
    {
        return processoModel.getCodProcessos(false);
    }

    public String[] getCodDisciplinas() throws Exception
    {
        Integer[] codDisciplinas = disciplinaModel.getCodDisciplinas(null);
        String[] response = new String[codDisciplinas.length];

        for (int x = 0; x < codDisciplinas.length; x++)
        {
            response[x] = codDisciplinas.toString();
        }

        return response;
    }

    public String[] filterCpfByInscricao(int cod) throws Exception
    {
        Fila<Inscricao> inscricoes = getInscricoes();
        Fila<String> cpfsInscritos = new Fila<>();
        String[] todosCpfs = professorModel.getCpfProfessores();

        while (!inscricoes.isEmpty())
        {
            Inscricao temp = inscricoes.remove();

            if (temp.getCodProcesso() == cod)
            {
                cpfsInscritos.insert(temp.getCpfProfessor());
            }
        }

        Fila<String> cpfsDisponiveis = new Fila<>();

        for (String cpf : todosCpfs) {
            boolean contains = false;

            Fila<String> tempFila = new Fila<>();

            while (!cpfsInscritos.isEmpty())
            {
                String inscrito = cpfsInscritos.remove();

                if (inscrito.equals(cpf)) {
                    contains = true;
                }

                tempFila.insert(inscrito);
            }

            cpfsInscritos = tempFila;

            if (!contains) {
                cpfsDisponiveis.insert(cpf);
            }
        }

        String[] resultado = new String[cpfsDisponiveis.size()];

        for (int i = 0; i < resultado.length; i++)
        {
            resultado[i] = cpfsDisponiveis.remove();
        }

        return resultado;
    }

    public Inscricao getInscricaoByCod(int codigo) throws Exception
    {
        Fila<Inscricao> inscricoes = getInscricoes();

        while (!inscricoes.isEmpty())
        {
            Inscricao inscricao = inscricoes.remove();

            if (inscricao.getCodProcesso() == codigo)
            {
                return inscricao;
            }
        }

        throw new Exception("Inscrição não encontrada.");
    }

    public Inscricao getInscricaoByDisciplina(int codigoDisciplina) throws Exception
    {
        Fila<Inscricao> inscricoes = getInscricoes();

        while (!inscricoes.isEmpty())
        {
            Inscricao inscricao = inscricoes.remove();

            if (inscricao.getCodDisciplina() == codigoDisciplina)
            {
                return inscricao;
            }
        }

        throw new Exception("Inscrição não encontrada.");
    }

    public void postInscricao(String valor) throws Exception
    {
        this.manager.writeIntoFile(valor, true);
    }

    //region Usar listas encadeadas no PUT e DELETE
    public void putInscricao(Inscricao inscricao) throws Exception
    {
        Fila<Inscricao> filaInscricao = getInscricoes();
        Lista<Inscricao> inscricoes = new Lista<>();

        while (!filaInscricao.isEmpty())
        {
            inscricoes.addLast(filaInscricao.remove());
        }

        for (int x = 0; x < inscricoes.size(); x++)
        {
            Inscricao temp = (Inscricao) inscricoes.get(x).dado;

            if (temp.getCodInscricao() == inscricao.getCodInscricao())
            {
                inscricoes.add(inscricao, x);
                inscricoes.remove(x + 1);
                break;
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < inscricoes.size(); x++)
        {
            str.append(inscricoes.get(x).dado.toString().trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteInscricao(int cod) throws Exception
    {
        Fila<Inscricao> filaInscricao = getInscricoes();
        Lista<Inscricao> inscricoes = new Lista<>();

        while (!filaInscricao.isEmpty())
        {
            inscricoes.addLast(filaInscricao.remove());
        }

        for (int x = 0; x < inscricoes.size(); x++)
        {
            Inscricao temp = (Inscricao) inscricoes.get(x).dado;

            if (temp.getCodInscricao() == cod)
            {
                inscricoes.remove(x);
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < inscricoes.size(); x++)
        {
            str.append(inscricoes.get(x).dado.toString().trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion

    public int count()
    {
        try
        {
            var values = getInscricoes();
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
            var values = getInscricoes();
            Inscricao temp = new Inscricao();

            while (!values.isEmpty())
            {
                temp = values.remove();
            }

            return temp.getCodInscricao();
        }
        catch (Exception ex)
        {
            return 0;
        }
    }
}
