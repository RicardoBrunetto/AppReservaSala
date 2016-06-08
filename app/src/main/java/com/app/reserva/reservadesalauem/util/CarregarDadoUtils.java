package com.app.reserva.reservadesalauem.util;

import android.widget.ArrayAdapter;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.dados.*;

import java.util.ArrayList;

/**
 * Created by Mamoru on 15/01/2016.
 */
public class CarregarDadoUtils implements Runnable{

    private int dado; // id para definir qual dado será solicitado ao servidor
    private ArrayList<Departamento> listaDepartamento;
    private ArrayList<Sala> listaSala;
    private ArrayList<Usuario> listaUsuario;
    private ArrayList<Disciplina> listaDisciplina;
    private ArrayList<Reserva> listaReserva;
    private ArrayList<Curso> listaCurso;

    private ArrayList<AnoLetivo> listaAnoLetivo;

    private String dataAtual;

    public CarregarDadoUtils(){
    }


    public ArrayList<Departamento> carregarDepartamento(){
        this.dado = 1;
        listaDepartamento = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaDepartamento;
    }

    public ArrayList<Sala> carregarSala(){
        this.dado = 2;
        listaSala = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaSala;
    }

    public ArrayList<Usuario> carregarUsuario(){
        this.dado = 3;
        listaUsuario = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }

    public ArrayList<Disciplina> carregarDisciplina(){
        this.dado = 4;
        listaDisciplina = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaDisciplina;
    }

    public ArrayList<Reserva> carregarReserva(){
        this.dado = 5;
        listaReserva = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaReserva;
    }

    public ArrayList<AnoLetivo> carregarAnoLetivo(){
        this.dado = 6;
        listaAnoLetivo = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaAnoLetivo;
    }

    public String carregarDataAtual(){
        this.dado = 7;
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataAtual;
    }

    public ArrayList<Curso> carregarCurso(){
        this.dado = 8;
        listaCurso = new ArrayList<>();
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listaCurso;
    }

    @Override
    public void run() {

        if(dado == 1){
            // carregar departamento
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaDepartamento = ws.carregarDepartamento();
            }
            catch (Exception ex){
            }
        }

        if(dado == 2){
            // carregar sala
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaSala = ws.carregarSala();
            }
            catch (Exception ex){
            }
        }
        if(dado == 3){
            // carregar usuario
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaUsuario = ws.carregarUsuario();
            }
            catch (Exception ex){
            }
        }
        if(dado == 4){
            // carregar disciplina
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaDisciplina = ws.carregarDisciplina();
            }
            catch (Exception ex){
            }
        }
        if(dado == 5){
            // carregar reserva
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaReserva = ws.carregarReserva();
            }
            catch (Exception ex){
            }
        }
        if(dado == 6){
            // carregar ano letivo
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaAnoLetivo = ws.carregarAnoLetivo();
            }
            catch (Exception ex){
            }
        }
        if(dado == 7){
            // carregar data atual
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                dataAtual = ws.solicitarDataAtual();
            }
            catch (Exception ex){
            }
        }
        if(dado == 8){
            // carregar curso
            try{
                AcessoAppUemWS ws = new AcessoAppUemWS();
                listaCurso = ws.carregarCurso();
            }
            catch (Exception ex){
            }
        }


    }
}
