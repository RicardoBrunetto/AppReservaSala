package com.app.reserva.reservadesalauem.dados;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Mamoru on 11/12/2015.
 */
public class Sala implements Serializable{
    private int id;
    private int numero;
    private int id_departamento;
    private int classificacao;
    private String descricao;
    private int status;

    public Sala() {
        id = -1;
    }

    public Sala(int id, int numero, int id_departamento, int classificacao, String descricao,
                int status) {
        super();
        this.id = id;
        this.numero = numero;
        this.id_departamento = id_departamento;
        this.classificacao = classificacao;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getId_departamento() {
        return id_departamento;
    }

    public void setId_departamento(int id_departamento) {
        this.id_departamento = id_departamento;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
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

    @Override
    public String toString() {
        return "Sala: "+this.getNumero();
    }

    public static Comparator<Sala> SalaNumeroComparator = new Comparator<Sala>() {

        public int compare(Sala s1, Sala s2) {
            /*
            String sala1 = ""+s1.getNumero();
            String sala2 = ""+s2.getNumero();


            if(sala1.length() < 6){
                for(int i=0;i<6-sala1.length();i++){
                    sala1 = "0" + sala1;
                }
            }
            if(sala2.length() < 6){
                for(int i=0;i<6-sala2.length();i++){
                    sala2 = "0" + sala2;
                }
            }
            System.out.println(sala1+"-"+sala2);
            //ascending order
            System.out.println(sala1.compareTo(sala2));
            return sala1.compareTo(sala2);
            */
            int sala1 = s1.getNumero();
            int sala2 = s2.getNumero();
            if(sala1<=sala2){
                return -2;
            }
            return 1;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}
