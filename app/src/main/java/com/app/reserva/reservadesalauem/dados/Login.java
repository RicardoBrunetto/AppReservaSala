package com.app.reserva.reservadesalauem.dados;

import java.io.Serializable;

/**
 * Created by Mamoru on 15/12/2015.
 */
public class Login implements Serializable{

    private int id;
    private String email;
    private String senha;
    private int privilegio;

    public Login() {
        this.id = -1;
        this.privilegio = -1;
    }

    public Login(String email, String senha) {
        this.id = -1;
        this.email = email;
        this.senha = senha;
        this.privilegio = -1;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(int privilegio) {
        this.privilegio = privilegio;
    }
}
