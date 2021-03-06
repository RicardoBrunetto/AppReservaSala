package com.app.reserva.reservadesalauem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.EditarSalaArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;
import java.util.Collections;
import com.app.reserva.reservadesalauem.R;


public class ListaAlterarSalaFragment extends Fragment implements View.OnClickListener{

    private Button btnListaAlterarSalaVoltar;
    private ListView lstListaAlterarSala;

    private Login login;
    private Usuario usuario;

    private ArrayList<Sala> lstSala;
    private ArrayAdapter<Sala> adpSala;

    public ListaAlterarSalaFragment() {
        // Required empty public constructor
    }

    public static ListaAlterarSalaFragment newInstance() {
        ListaAlterarSalaFragment fragment = new ListaAlterarSalaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments(); // usado para passagem de parametro
        if (bundle.containsKey(MenuPrincipalActivity.LOGIN)){
            login = (Login)bundle.get(MenuPrincipalActivity.LOGIN); // passei o login inteiro, dependendo do local do código
            // foi passado cada parametro separadamente
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_alterar_sala, container, false);
        lstListaAlterarSala = (ListView) view.findViewById(R.id.lstListaAlterarSala);

        btnListaAlterarSalaVoltar = (Button) view.findViewById(R.id.btnListaAlterarSalaVoltar);
        btnListaAlterarSalaVoltar.setOnClickListener(this); // evento de clique do botão

        buscarUsuario(); // buscar o usuario para poder usar o departamento do usuario
        listarSala(); // listar as salas com o mesmo departamento que o usuario
        return view;
    }

    public void buscarUsuario(){
        ArrayList<Usuario> lstU = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        for(Usuario u:lstU) { // para cada usuario u pertencente à lista com todos os usuarios
            if (u.getEmail().equals(login.getEmail())) { // verificar se email é igual
                usuario = new Usuario(); // criar novo usuario
                usuario.clonar(u); // copiar todos os dados
                return;
            }
        }
    }

    public void listarSala(){
        lstSala = new ArrayList<>(); // lista de salas do mesmo departamento
        ArrayList<Sala> lstS = new ArrayList<>(); // lista de todas as salas

        CarregarDadoUtils cd = new CarregarDadoUtils(); // funcionalidade que carrega dados do servidor
        lstS = cd.carregarSala(); // carregar todas as salas do servidor

        for(Sala s:lstS){
            if (s.getId_departamento()==usuario.getId_departamento()){
                // verifica apenas as salas do mesmo departamento que o usuario
                lstSala.add(s);
            }
        }
        Collections.sort(lstSala, Sala.SalaNumeroComparator); //ordenar as salas pelo numero (funcao está na classe Sala)
        // criar um array adapter personalizado e passar parametro para o array adapter, mno caso, a lista de salas
        // para saber qual sala deve ser alterado e o login, para verificar permissão
        adpSala = new EditarSalaArrayAdapter(getContext(),R.layout.item_editar_sala,lstSala,login);
        for(Sala s:lstSala){
            adpSala.add(s); // adicionar as salas no array adapter
        }
        lstListaAlterarSala.setAdapter(adpSala); // usar o array adapter na listview
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // evento em que recebe uma resposta de outra interface e realiza uma operação, tipo uma trigger
        // no caso, é uma trigger para o botão dentro do array adapter para alteração
        listarSala();
    }*/

    @Override
    public void onClick(View v) {
        if(v==btnListaAlterarSalaVoltar){
        }
    }

}
