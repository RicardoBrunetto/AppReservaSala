package com.app.reserva.reservadesalauem.dados;

//import java.sql.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class Reserva {

    private int id;
    private int iddepartamento;
    private int idusuario;
    private int tipoaula;
    private int iddisciplina;
    private int tipo;
    private String dataefetuacao;
    private int proximoid;
    private String datareserva;
    private int periodo;
    private int tiposala; //// solicitação para tipoaula de sala (projecao, laboratorio)
    private int idsala;
    private int status;

    public Reserva(){
        this.id = -1;
        this.iddisciplina = -1;
        this.idsala = -1;
        this.tiposala = -1;
    }

    public Reserva(int id, int iddepartamento, int idusuario, int tipoaula, int iddisciplina,
                   int tipo, String dataefetuacao, int proximoid, String datareserva, int periodo, int tiposala, int idsala,
                   int status) {
        super();
        this.id = id;
        this.iddepartamento = iddepartamento;
        this.idusuario = idusuario;
        this.tipoaula = tipoaula;
        this.iddisciplina = iddisciplina;
        this.tipo = tipo;
        this.dataefetuacao = dataefetuacao;
        this.proximoid = proximoid;
        this.datareserva = datareserva;
        this.periodo = periodo;
        this.tiposala = tiposala;
        this.idsala = idsala;
        this.status = status;
    }

    public void print(){
        //System.out.print("Reserva: ID: " + id + " | ID_Depto: " + iddepartamento + " | ID_User: " + idusuario + " | TipoAula: " + tipoaula + " | ID_Disc: " + iddisciplina +
        //" | Tipo: " + tipo + " | Data_Ef: " + dataefetuacao + " | Proximo_Id: " + " | Data_Reserva: " +  datareserva + " | Periodo: " + periodo + " | TipoSala:" +  tiposala + " | ID_Sala: " + idsala +
        //" | Status" + status + "\n");
        System.out.print("ID_Sala: " + idsala + "\n");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(int iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getTipoaula() {
        return tipoaula;
    }

    public void setTipoaula(int tipoaula) {
        this.tipoaula = tipoaula;
    }

    public int getIddisciplina() {
        return iddisciplina;
    }

    public void setIddisciplina(int iddisciplina) {
        this.iddisciplina = iddisciplina;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDataefetuacao() {
        return dataefetuacao;
    }

    public void setDataefetuacao(String dataefetuacao) {
        this.dataefetuacao = dataefetuacao;
    }

    public int getProximoid() {
        return proximoid;
    }

    public void setProximoid(int proximoid) {
        this.proximoid = proximoid;
    }

    public String getDatareserva() {
        return datareserva;
    }

    public void setDatareserva(String datareserva) {
        this.datareserva = datareserva;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getTiposala() {
        return tiposala;
    }

    public void setTiposala(int tiposala) {
        this.tiposala = tiposala;
    }

    public int getIdsala() {
        return idsala;
    }

    public void setIdsala(int idsala) {
        this.idsala = idsala;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Comparator<Reserva> ReservaPeriodoComparator = new Comparator<Reserva>() {

        public int compare(Reserva r1, Reserva r2) {

            int p1 = r1.getPeriodo();
            int p2 = r2.getPeriodo();
            if(p1<=p2){
                return -2;
            }
            return 1;

        }};

    public static Comparator<Reserva> ReservaDataComparator = new Comparator<Reserva>() {

        public int compare(Reserva r1, Reserva r2) {

            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            dt.setLenient(false);
            try{
                Long d1 = dt.parse(r1.getDatareserva()).getTime();
                Long d2 = dt.parse(r2.getDatareserva()).getTime();
                if(d1<d2){
                    return -2;
                }
                return 1;
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return 1;

        }};

}
