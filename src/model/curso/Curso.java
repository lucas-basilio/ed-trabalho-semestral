package model.curso;

import model.utils.StreamableObject;

public class Curso extends StreamableObject {
    private int codCurso;
    private String nome;
    private String area;

    //region Getters Setters
    public int getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    //endregion
}