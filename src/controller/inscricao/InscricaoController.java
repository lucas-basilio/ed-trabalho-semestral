package controller.inscricao;

import model.inscricao.Inscricao;
import model.inscricao.InscricaoModel;

public class InscricaoController {

    private final InscricaoModel inscricaoModel = new InscricaoModel();

    public Object[][] get() throws Exception
    {
        return this.inscricaoModel.populateTable();
    }

    public String[] getFields()
    {
        return new Inscricao().properties();
    }

    public String[] getCpfProfessores() throws Exception
    {
        return inscricaoModel.getCpfProfessores();
    }

    public Integer[] getCodProcessos() throws Exception
    {
        return inscricaoModel.getCodProcessos();
    }

    public String[] getCodDisciplinas() throws Exception
    {
        return inscricaoModel.getCodDisciplinas();
    }

    public Object[][] dadosDisciplina() throws Exception
    {
        return inscricaoModel.dadosDisciplina();
    }

    public String[] filterCpfByInscricao(int cod) throws Exception
    {
        return inscricaoModel.filterCpfByInscricao(cod);
    }

    public Inscricao getByCodigo(int cod) throws Exception
    {
        return this.inscricaoModel.getInscricaoByCod(cod);
    }

    public void post(Inscricao Inscricao) throws Exception
    {
        this.inscricaoModel.postInscricao(Inscricao.toString());
    }

    public void put(Inscricao Inscricao) throws Exception
    {
        this.inscricaoModel.putInscricao(Inscricao);
    }

    public void delete(int cod) throws Exception
    {
        this.inscricaoModel.deleteInscricao(cod);
    }

    public int count()
    {
        return this.inscricaoModel.count();
    }

    public int lastId()
    {
        return this.inscricaoModel.lastId();
    }
}
