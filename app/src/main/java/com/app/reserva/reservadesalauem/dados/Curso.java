package com.app.reserva.reservadesalauem.dados;

/**
 * Created by Mamoru on 12/01/2016.
 */
public class Curso {

    private int id;
    private int id_departamento;
    private String nome;
    private int tipo; // graduacao prioritario=1, graduacao prioritario 2 = 2, mestrado=3, outros cursos=4
    private String descricao;
    private int status;

    public Curso() {
        id = -1;
    }

    public Curso(int id, int id_departamento, String nome, int tipo, String descricao, int status) {
        super();
        this.id = id;
        this.id_departamento = id_departamento;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void clonar(Curso c){
        this.id = c.getId();
        this.id_departamento = c.getId_departamento();
        this.nome = c.getNome();
        this.descricao = c.getDescricao();
        this.tipo = c.getTipo();
        this.status = c.getStatus();
    }
}
