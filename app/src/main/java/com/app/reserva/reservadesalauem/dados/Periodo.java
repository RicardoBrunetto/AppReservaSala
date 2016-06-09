package com.app.reserva.reservadesalauem.dados;

/**
 * Created by Mamoru on 02/02/2016.
 */
public class Periodo {

    private String nome;
    private int status;

    public Periodo() {
    }

    public Periodo(String nome, int status) {
        this.nome = nome;
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
