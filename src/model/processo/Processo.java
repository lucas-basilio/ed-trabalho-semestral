package model.processo;

import model.utils.StreamableObject;

public class Processo extends StreamableObject {
    private int codProcesso;

    public int getCodProcesso() {
        return codProcesso;
    }

    public void setCodProcesso(int codProcesso) {
        this.codProcesso = codProcesso;
    }
}
