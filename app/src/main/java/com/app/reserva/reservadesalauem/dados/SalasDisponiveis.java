package com.app.reserva.reservadesalauem.dados;

import java.io.Serializable;

/**
 * Created by Mamoru on 05/02/2016.
 */
public class SalasDisponiveis implements Serializable{

    private Login login;
    private int tiposala;
    private String data;
    private int salaslivres;
    private int periodo;

    public SalasDisponiveis(){}

    public SalasDisponiveis( Login login, int tiposala, String data, int salaslivres, int periodo) {

        this.login = login;
        this.tiposala = tiposala;
        this.data = data;
        this.salaslivres = salaslivres;
        this.periodo = periodo;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public int getTiposala() {
        return tiposala;
    }

    public void setTiposala(int tiposala) {
        this.tiposala = tiposala;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSalaslivres() {
        return salaslivres;
    }

    public void setSalaslivres(int salaslivres) {
        this.salaslivres = salaslivres;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
