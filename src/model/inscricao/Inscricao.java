package model.inscricao;

import model.utils.StreamableObject;

public class Inscricao extends StreamableObject
{
    private int codProcesso;
    private String cpfProfessor;
    private int codDisciplina;
    private boolean ativo;

    public Inscricao()
    {
        this.ativo = true;
    }

    //region Getters Setters

    public int getCodProcesso()
    {
        return this.codProcesso;
    }

    public void setCodProcesso(int codProcesso)
    {
        this.codProcesso = codProcesso;
    }

    public String getCpfProfessor()
    {
        return this.cpfProfessor;
    }

    public void setCpfProfessor(String cpfProfessor)
    {
        this.cpfProfessor = cpfProfessor;
    }

    public int getCodDisciplina()
    {
        return this.codDisciplina;
    }

    public void setCodDisciplina(int codDisciplina)
    {
        this.codDisciplina = codDisciplina;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    //endregion
}
