package com.app.reserva.reservadesalauem.dados;

/**
 * Created by Mamoru on 11/12/2015.
 */
public class Disciplina {
    private int id;
    private int id_departamento;
    private int id_curso;
    private int codigo;
    private int periodo; //S1=1, S2=2, A=0
    private int turma;
    private String nome;
    private int classificacao; // teórica, pratica;
    private int status;

    public Disciplina() {
        // TODO Auto-generated constructor stub
    }

    public Disciplina(int id, int id_departamento, int id_curso, int codigo, int periodo, int turma, String nome,
                      int classificacao, int status) {
        super();
        this.id = id;
        this.id_departamento = id_departamento;
        this.id_curso = id_curso;
        this.codigo = codigo;
        this.periodo = periodo;
        this.turma = turma;
        this.nome = nome;
        this.classificacao = classificacao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_departamento() {
        return id_departamento;
    }

    public void setId_departamento(int id_departamento) {
        this.id_departamento = id_departamento;
    }

    public int getId_curso() {
        return id_curso;
    }

    public void setId_curso(int id_curso) {
        this.id_curso = id_curso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getTurma() {
        return turma;
    }

    public void setTurma(int turma) {
        this.turma = turma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void clonar(Disciplina d) {
        this.id = d.getId();
        this.id_curso = d.getId_curso();
        this.codigo = d.getCodigo();
        this.periodo = d.getPeriodo();
        this.turma = d.getTurma();
        this.nome = d.getNome();
        this.classificacao = d.getClassificacao();
        this.status = d.getStatus();
    }

    @Override
    public String toString() {
        return ""+this.getCodigo()+"-"+this.getTurma();
    }
}
