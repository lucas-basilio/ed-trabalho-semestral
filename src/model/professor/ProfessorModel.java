package model.professor;

import model.dataStructures.Fila;
import model.dataStructures.Lista;
import model.inscricao.Inscricao;
import model.inscricao.InscricaoModel;
import model.utils.FileManager;

public class ProfessorModel {

    private final FileManager manager = new FileManager("src/data/professores.csv");

    private Fila<Professor> getProfessores() throws Exception
    {
        String[] values = this.manager.readFileToString().split(",");
        Fila<Professor> professores = new Fila<>();

        for (int x = 0; x < values.length; x += 4)
        {
            Professor professor = new Professor();

            professor.setNome(values[x].trim());
            professor.setCpf(values[x + 1].trim());
            professor.setArea(values[x + 2].trim());
            professor.setPontos(Double.parseDouble(values[x + 3].trim()));

            professores.insert(professor);
        }

        return professores;
    }

    public Object[][] populateTable() throws Exception
    {
        Fila<Professor> fila = getProfessores();
        int size = fila.size();

        Object[][] dados = new Object[size][4];

        for (int x = 0; x < size; x++)
        {
            Professor temp = fila.remove();

            dados[x][0] = temp.getNome();
            dados[x][1] = temp.getCpf();
            dados[x][2] = temp.getArea();
            dados[x][3] = temp.getPontos();
        }

        return dados;
    }

    public String[] getFields()
    {
        return new Professor().properties();
    }

    //Busca professor por CPF
    public Professor getProfessorByCPF(String cpf) throws Exception
    {
        Fila<Professor> professores = getProfessores();

        while (!professores.isEmpty())
        {
            Professor temp = professores.remove();

            if (temp.getCpf().equals(cpf))
            {
                return temp;
            }
        }

        throw new Exception("Professor não encontrado.");
    }

    public Lista<Professor> getProfessorByDisciplina(int codigo) throws Exception
    {
        Fila<Professor> filaProfessores = getProfessores();
        Lista<Professor> professores = new Lista<>();
        Inscricao inscricao = new InscricaoModel().getInscricaoByCod(codigo);

        while (!filaProfessores.isEmpty())
        {
            Professor temp = filaProfessores.remove();

            if (inscricao.getCpfProfessor().equals(temp.getCpf()))
            {
                professores.addLast(temp);
            }
        }

        return professores;
    }

    public void postProfessor(String valor) throws Exception
    {
        this.manager.writeIntoFile(valor, true);
    }

    //region Usar listas encadeadas no PUT e DELETE
    public void putProfessor(Professor professor) throws Exception
    {
        Fila<Professor> filaProfessores = getProfessores();
        Lista<Professor> professores = new Lista<>();

        while (!filaProfessores.isEmpty())
        {
            professores.addLast(filaProfessores.remove());
        }

        for (int x = 0; x < professores.size(); x++)
        {
            Professor temp = (Professor)professores.get(x).dado;

            if (temp.getCpf().equals(professor.getCpf()))
            {
                professores.add(professor, x);
                professores.remove(x + 1);
                break;
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < professores.size(); x++)
        {
            str.append(professores.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }

    public void deleteProfessor(Professor professor) throws Exception
    {
        Fila<Professor> filaProfessores = getProfessores();
        Lista<Professor> professores = new Lista<>();

        while (!filaProfessores.isEmpty())
        {
            professores.addLast(filaProfessores.remove());
        }

        for (int x = 0; x < professores.size(); x++)
        {
            Professor temp = (Professor)professores.get(x).dado;

            if (temp.getCpf().equals(professor.getCpf()))
            {
                professores.remove(x);
                break;
            }
        }

        StringBuffer str = new StringBuffer();

        //Transformar em string para o csv
        for (int x = 0; x < professores.size(); x++)
        {
            str.append(professores.get(x).dado.toString().replaceAll("(L>)|([|])|\\n", ",").trim());
        }

        this.manager.writeIntoFile(str.toString(), false);
    }
    //endregion
}
