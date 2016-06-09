package com.app.reserva.reservadesalauem.dados;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Mamoru on 11/12/2015.
 */
public class Usuario implements Serializable{

    private int id;
    private int id_departamento;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private int permissao;
    private String id_disciplinas;
    private int problema_locomocao;
    private int status;

    public Usuario(){
        id = -1;
    }

    public Usuario(int id, int id_departamento, String nome, String email, String senha,
                   String telefone, int permissao, String id_disciplinas, int problema_locomocao, int status) {
        super();
        this.id = id;
        this.id_departamento = id_departamento;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.permissao = permissao;
        this.id_disciplinas = id_disciplinas;
        this.problema_locomocao = problema_locomocao;
        this.status = status;
    }

    public Usuario(int id, int id_departamento, String nome, String email, String senha,
                   String telefone, int permissao, int problema_locomocao, int status) {
        super();
        this.id = id;
        this.id_departamento = id_departamento;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.permissao = permissao;
        this.id_disciplinas = "";
        this.problema_locomocao = problema_locomocao;
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

    public String getEmail() {
        return email;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }

    public String getId_disciplinas() {
        return id_disciplinas;
    }

    public void setId_disciplinas(String id_disciplinas) {
        this.id_disciplinas = id_disciplinas;
    }

    public void addId_disciplinas(int id){
        if(id_disciplinas.length() != 0){
            id_disciplinas += "-";
        }
        id_disciplinas += id;
    }

    public int getProblema_locomocao() {
        return problema_locomocao;
    }

    public void setProblema_locomocao(int problema_locomocao) {
        this.problema_locomocao = problema_locomocao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void clonar(Usuario u){
        this.id = u.getId();
        this.id_departamento = u.getId_departamento();
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.senha = u.getSenha();
        this.telefone = u.getTelefone();
        this.permissao = u.getPermissao();
        this.id_disciplinas = u.getId_disciplinas();
        this.problema_locomocao = u.getProblema_locomocao();
        this.status = u.getStatus();
    }

    @Override
    public String toString() {
        return getNome();
    }

    public static Comparator<Usuario> UsuarioNameComparator = new Comparator<Usuario>() {

        public int compare(Usuario u1, Usuario u2) {
            String usuario1 = u1.getNome().toUpperCase();
            String usuario2 = u2.getNome().toUpperCase();

            //ascending order
            return usuario1.compareTo(usuario2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

}
