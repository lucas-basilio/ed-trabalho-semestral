package model.inscricao;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.disciplina.Disciplina;
import model.professor.Professor;
import model.utils.FileManager;

public class InscricaoModel {
    private final FileManager manager = new FileManager("src/data/inscricoes.csv");

    private Fila<Inscricao> getInscricoes() throws Exception
    {
        String[] values = this.manager.readFileToString().split(",");
        Fila<Inscricao> inscricoes = new Fila<>();

        for (int x = 0; x < values.length; x += 3)
        {
            Inscricao inscricao = new Inscricao();

            inscricao.setCodProcesso(Integer.parseInt(values[x]));
            inscricao.setCpfProfessor(values[x + 1]);
            inscricao.setCodDisciplina(Integer.parseInt(values[x + 2]));

            inscricoes.insert(inscricao);
        }

        return inscricoes;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Inscricao> fila = getInscricoes();
        int size = fila.size();

        Object[][] dados = new Object[size][3];

        for (int x = 0; x < size; x++)
        {
            Inscricao temp = fila.remove();

            dados[x][0] = temp.getCodProcesso();
            dados[x][1] = temp.getCpfProfessor();
            dados[x][2] = temp.getCodDisciplina();
        }

        return dados;
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
            Disciplina temp = (Disciplina) inscricoes.get(x).dado;

            if (temp.getCodDisciplina() == inscricao.getCodDisciplina())
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
            str.append(inscricoes.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteInscricao(Inscricao inscricao) throws Exception
    {
        Fila<Inscricao> filaInscricao = getInscricoes();
        Lista<Inscricao> inscricoes = new Lista<>();

        while (!filaInscricao.isEmpty())
        {
            inscricoes.addLast(filaInscricao.remove());
        }

        for (int x = 0; x < inscricoes.size(); x++)
        {
            Disciplina temp = (Disciplina) inscricoes.get(x).dado;

            if (temp.getCodDisciplina() == inscricao.getCodDisciplina())
            {
                inscricoes.remove(x);
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < inscricoes.size(); x++)
        {
            str.append(inscricoes.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion
}
