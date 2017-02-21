package com.app.reserva.reservadesalauem.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.dados.Departamento;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.SalaArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ListarSalaFragment extends Fragment implements View.OnClickListener{

    private Button btnListarSalaAnterior;
    private Button btnListarSalaProximo;
    private Button btnListarSalaVoltar;

    private TextView txtListarSalaDepartamento;

    private ListView lstListarSala;

    private String usermail;
    private Usuario usuario;
    private ArrayList<Sala> lstSalaAtivo;
    private ArrayAdapter<Sala> adpSala;

    private int posDepartamento;
    private ArrayList<Departamento> lstDepartamento;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListarSalaFragment() {
        // Required empty public constructor
    }

    public static ListarSalaFragment newInstance(String param1, String param2) {
        ListarSalaFragment fragment = new ListarSalaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // recebe parametro
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            Log.d("LOG", "yes");
            usermail = bundle.getString(MenuPrincipalActivity.EMAIL);
            Log.d("Log", usermail);
        }

        // pesquisar dados do usuario logado pelo email
        getUsuario(usermail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_sala, container, false);
        btnListarSalaAnterior = (Button) view.findViewById(R.id.btnListarSalaAnterior);
        btnListarSalaAnterior.setOnClickListener(this);
        btnListarSalaProximo = (Button) view.findViewById(R.id.btnListarSalaProximo);
        btnListarSalaProximo.setOnClickListener(this);
        btnListarSalaVoltar = (Button) view.findViewById(R.id.btnListarSalaVoltar);
        btnListarSalaVoltar.setOnClickListener(this);

        txtListarSalaDepartamento = (TextView) view.findViewById(R.id.txtListarSalaDepartamento);
        lstListarSala = (ListView) view.findViewById(R.id.lstListarSala);

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

        return view;
    }


    // listar todas as salas de um departamento
    public void listarSalas(){
        // lista de todas as salas ativas (válidas)
        lstSalaAtivo = new ArrayList<>();
        // lista de todas as salas cadastradas
        ArrayList<Sala> lstS = new ArrayList<>();
        // array adapter que será usado na lista de visualização
        adpSala = new SalaArrayAdapter(getContext(),R.layout.item_sala);
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
