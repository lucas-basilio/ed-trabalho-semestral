package model.processo;

import model.utils.StreamableObject;

public class Processo extends StreamableObject {
    private int codProcesso;
    private int codDisciplina;
    private boolean ativo;

    public int getCodProcesso() {
        return this.codProcesso;
    }

    public void setCodProcesso(int codProcesso) {
        this.codProcesso = codProcesso;
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getCodDisciplina() {
        return this.codDisciplina;
    }

    public void setCodDisciplina(int codDisciplina) {
        this.codDisciplina = codDisciplina;
    }
}
