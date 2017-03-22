package com.app.reserva.reservadesalauem.dados;

/**
 * Created by rickh on 19/03/2017.
 */

public class ReservaLogin{
    Reserva res;
    Login login;

    public ReservaLogin(Reserva res, Login login) {
        this.res = res;
        this.login = login;
    }

    public Reserva getRes() {
        return res;
    }

    public Login getLogin() {
        return login;
    }
}