package model.disciplina;

import model.utils.StreamableObject;

public class Disciplina extends StreamableObject {
    private int codDisciplina;
	private String nome;
	private String dia;
	private String horario;
	private double horasDia;
	private int codCurso;

    //region Getters Setters
    public int getCodDisciplina() {
        return this.codDisciplina;
    }

    public void setCodDisciplina(int codDisciplina) {
        this.codDisciplina = codDisciplina;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDia() {
        return this.dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHorario() {
        return this.horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public double getHorasDia() {
        return this.horasDia;
    }

    public void setHorasDia(double horasDia) {
        this.horasDia = horasDia;
    }

    public int getCodCurso() {
        return this.codCurso;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }
    //endregion
}
