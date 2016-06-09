package com.app.reserva.reservadesalauem.dados;

/**
 * Created by Mamoru on 02/03/2016.
 */
public class AnoLetivo {

    private int id;
    private int iddepartamento;
    private String iniciop; // inicio primeiro semestre
    private String fimp; // fim primeiro semestre
    private String inicios; // inico seguno
    private String fims;// fim segundo
    private int status;

    public AnoLetivo() {
    }

    public AnoLetivo(int id, int iddepartamento, String iniciop, String fimp, String inicios, String fims, int status) {
        this.id = id;
        this.iddepartamento = iddepartamento;
        this.iniciop = iniciop;
        this.fimp = fimp;
        this.inicios = inicios;
        this.fims = fims;
        this.status = status;
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

    public String getIniciop() {
        return iniciop;
    }

    public void setIniciop(String iniciop) {
        this.iniciop = iniciop;
    }

    public String getFimp() {
        return fimp;
    }

    public void setFimp(String fimp) {
        this.fimp = fimp;
    }

    public String getInicios() {
        return inicios;
    }

    public void setInicios(String inicios) {
        this.inicios = inicios;
    }

    public String getFims() {
        return fims;
    }

    public void setFims(String fims) {
        this.fims = fims;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void clonarAnoLetivo(AnoLetivo anoLetivo) {
        this.id = anoLetivo.getId();
        this.iddepartamento = anoLetivo.getIddepartamento();
        this.iniciop = anoLetivo.getIniciop();
        this.fimp = anoLetivo.getFimp();
        this.inicios = anoLetivo.getInicios();
        this.fims = anoLetivo.getFims();
        this.status = anoLetivo.getStatus();
    }
}
