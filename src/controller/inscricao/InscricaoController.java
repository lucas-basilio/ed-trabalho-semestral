package controller.inscricao;

import model.inscricao.Inscricao;
import model.inscricao.InscricaoModel;
import model.dataStructures.Fila;

public class InscricaoController {

    private final InscricaoModel inscricaoModel = new InscricaoModel();

    public Object[][] get() throws Exception
    {
        return this.inscricaoModel.populateTable();
    }

    public Inscricao getByCpf(int cod) throws Exception
    {
        return this.inscricaoModel.getInscricaoByCod(cod);
    }

    public void post(Inscricao Inscricao) throws Exception
    {
        this.inscricaoModel.postInscricao(Inscricao.toCSV());
    }

    public void put(Inscricao Inscricao) throws Exception
    {
        this.inscricaoModel.putInscricao(Inscricao);
    }

    public void delete(Inscricao Inscricao) throws Exception
    {
        this.inscricaoModel.deleteInscricao(Inscricao);
    }
}
