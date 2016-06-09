package com.app.reserva.reservadesalauem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.dados.Departamento;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.SalaArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ListarSalaActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnListarSalaAnterior;
    private Button btnListarSalaProximo;
    private Button btnListarSalaVoltar;

    private TextView txtListarSalaDepartamento;

    private ListView lstListarSala;

    private Login login;
    private Usuario usuario;
    private ArrayList<Sala> lstSalaAtivo;
    private ArrayAdapter<Sala> adpSala;

    private int posDepartamento;
    private ArrayList<Departamento> lstDepartamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_sala);

        // recebe parametro
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(MenuPrincipalActivity.LOGIN)){
            login = (Login)bundle.get(MenuPrincipalActivity.LOGIN);
        }

        // pesquisar dados do usuario logado pelo email
        getUsuario(login.getEmail());

        btnListarSalaAnterior = (Button) findViewById(R.id.btnListarSalaAnterior);
        btnListarSalaAnterior.setOnClickListener(this);
        btnListarSalaProximo = (Button) findViewById(R.id.btnListarSalaProximo);
        btnListarSalaProximo.setOnClickListener(this);
        btnListarSalaVoltar = (Button) findViewById(R.id.btnListarSalaVoltar);
        btnListarSalaVoltar.setOnClickListener(this);

        txtListarSalaDepartamento = (TextView) findViewById(R.id.txtListarSalaDepartamento);
        lstListarSala = (ListView) findViewById(R.id.lstListarSala);

        // lista de todos os departamentos
        lstDepartamento = new ArrayList<>();

        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstDepartamento = cd.carregarDepartamento();
        // setar a posição atual da lista de departamentos como 0 (primeiro e default)
        posDepartamento = 0;

        // listar todas as salas do departamento selecionado
        listarSalas();

        // selecionar o foco como uma lista, senão o foco vai em um edit text, que abre o teclado e atrapalha a visão
        lstListarSala.requestFocus();


    }

    // listar todas as salas de um departamento
    public void listarSalas(){
        // lista de todas as salas ativas (válidas)
        lstSalaAtivo = new ArrayList<>();
        // lista de todas as salas cadastradas
        ArrayList<Sala> lstS = new ArrayList<>();
        // array adapter que será usado na lista de visualização
        adpSala = new SalaArrayAdapter(this,R.layout.item_sala);
        // carregar dados do servidor
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstS = cd.carregarSala();
        // mostrar o nome do departamento
        txtListarSalaDepartamento.setText(lstDepartamento.get(posDepartamento).getNome());
        for(Sala s:lstS){
            // se a sala pertencer ao departamento selecionado, adicionar na lista
            if (s.getId_departamento()==lstDepartamento.get(posDepartamento).getId()){
                lstSalaAtivo.add(s);

            }
        }
        // ordenar a lista de salas pelo numero da sala
        Collections.sort(lstSalaAtivo, Sala.SalaNumeroComparator);
        // adicionar todas as salas válidas no array adapter
        for(Sala s:lstSalaAtivo){
            adpSala.add(s);
        }
        // mostrar os dados do array adapter na lista
        lstListarSala.setAdapter(adpSala);

    }

    // tem várias funções iguais a essa com nome dferente em várias classe
    // dá para juntar todas e padronizar, por exemplo, criar uma classe na pasta util
    // só para funções muito repetitivas
    private void getUsuario(String email){
        Usuario user1 = new Usuario();
        ArrayList<Usuario> lstUsuario = new ArrayList<>();
        try {
            CarregarDadoUtils cd = new CarregarDadoUtils();
            lstUsuario = cd.carregarUsuario();
            for (Usuario u : lstUsuario) {
                //System.out.println(u.getEmail());
                if (u.getEmail().equals(email)) {
                    user1.clonar(u);
                    usuario = new Usuario();
                    usuario.clonar(u);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnListarSalaVoltar){
            finish();
        }
        if(v == btnListarSalaAnterior){
            // atualizar posição na lista de departamento
            posDepartamento -= 1;
            if(posDepartamento < 0){
                posDepartamento = lstDepartamento.size()-1;
            }
            // atualizar lista de salas em relação ao departamento
            listarSalas();

        }
        if(v == btnListarSalaProximo){
            // atualizar posição do departamento
            posDepartamento += 1;
            if(posDepartamento >= lstDepartamento.size()){
                posDepartamento = 0;
            }
            // atualizar salas em relação ao departamento
            listarSalas();
        }

    }
}
