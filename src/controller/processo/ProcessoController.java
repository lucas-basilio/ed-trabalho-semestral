package controller.processo;

import model.inscricao.Inscricao;
import model.processo.Processo;
import model.processo.ProcessoModel;

public class ProcessoController
{
    private final ProcessoModel processoModel = new ProcessoModel();

    public Object[][] get() throws Exception
    {
        return this.processoModel.populateTable();
    }

    public String[] getFields()
    {
        return new Processo().properties();
    }

    public Integer[] getCodDisciplinas(boolean filter) throws Exception
    {
        return this.processoModel.getCodDisciplinas(filter);
    }

    public Processo getByCod(int cod) throws Exception
    {
        return this.processoModel.getProcessoByCod(cod);
    }

    public void post(Processo processo) throws Exception
    {
        this.processoModel.postProcesso(processo.toString());
    }

    public void put(Processo processo, int cod) throws Exception
    {
        this.processoModel.putProcesso(processo, cod);
    }

    public void delete(int cod) throws Exception
    {
        this.processoModel.deleteProcesso(cod);
    }
}
