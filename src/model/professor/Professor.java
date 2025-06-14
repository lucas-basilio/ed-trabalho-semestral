package model.professor;

import model.utils.StreamableObject;

public class Professor extends StreamableObject {
    private String nome;
    private String cpf;
    private String area;
    private double pontos;

    //region Getters Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getPontos() {
        return pontos;
    }

    public void setPontos(double pontos) {
        this.pontos = pontos;
    }
    //endregion
}
