package com.app.reserva.reservadesalauem.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;

import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.dados.*;
import com.app.reserva.reservadesalauem.dados.adapters.EditarUsuarioArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;
import java.util.Collections;
import com.app.reserva.reservadesalauem.R;


public class ListaAlterarUsuarioFragment extends Fragment implements View.OnClickListener{

    private EditText edtListaAlterarUsuarioPesquisar;
    private Button btnListarUsuarioVoltar;
    private ListView lstListaAlterarUsuario;

    private ArrayAdapter<Usuario> adpUsuario;

    Login login;
    Usuario usuario;
    ArrayList<Usuario> lstUsuario;

    private FiltaDados filtaDados;

    public ListaAlterarUsuarioFragment() {
        // Required empty public constructor
    }

    public static ListaAlterarUsuarioFragment newInstance() {
        ListaAlterarUsuarioFragment fragment = new ListaAlterarUsuarioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = new Login();
        // recebe parâmetro de outra tela
        Bundle bundle = getArguments();

        // em vez de e-mail, senha e privilégio, era possível passar como parametro login
        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_alterar_usuario, container, false);
        // buscar o usuário logado no sistema pelo email, pois vai precisar saber o departamento do usuário logado
        buscarUsuario();

        btnListarUsuarioVoltar = (Button) view.findViewById(R.id.btnListaAlterarUsuarioVoltar);
        btnListarUsuarioVoltar.setOnClickListener(this); // evento para o botão (implementado dentro de onClick)

        lstListaAlterarUsuario = (ListView) view.findViewById(R.id.lstListaAlterarUsuario);

        edtListaAlterarUsuarioPesquisar = (EditText) view.findViewById(R.id.edtListaAlterarUsuarioPesquisar);

        // criar um evento de filtro de pesquisa
        filtaDados = new FiltaDados(adpUsuario);
        edtListaAlterarUsuarioPesquisar.addTextChangedListener(filtaDados);

        // mostrar todos os usuários que podem ser alterados
        listarUsuario();
        // solicitar foco na lista, senão o foco vai no primeiro edit text, aí abre o teclado e dificulta visão inicial
        lstListaAlterarUsuario.requestFocus();
        return view;
    }
    // pesquisar os dados de um usuário pelo email e copiar para a variável usuario
    // no caso, o usuário logado no sistema
    public void buscarUsuario(){
        //
        ArrayList<Usuario> lstU = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        for(Usuario u:lstU) {
            if (u.getEmail().equals(login.getEmail())) {

                usuario = new Usuario();
                usuario.clonar(u);
                return;
            }
        }
    }

    // mostra a lista de todos os usuários que podem ser alterados com a permissão atual
    public void listarUsuario(){
        lstUsuario = new ArrayList<>();
        ArrayList<Usuario> lstU = new ArrayList<>();

        // chama a função que tem thread para a busca dados no servidor
        CarregarDadoUtils cd = new CarregarDadoUtils();
        // carregar lista de todos os usuarios
        lstU = cd.carregarUsuario();
        // adicionar usuário logado na lista de usuarios que podem ser alterados
        lstUsuario.add(usuario);
        for(Usuario u:lstU){
            // para cada usuario cadastrado, verificar se é do mesmo departamento e permissão é inferior ao logado.
            // se sim, adicionar na lista de usuarios que podem ser alterados
            if (u.getId_departamento()==usuario.getId_departamento()){
                if(u.getPermissao()<usuario.getPermissao()){
                    lstUsuario.add(u);
                }
            }
        }
        // função criada na classe usuario para ordenar a lsita de usuarios em ordem alfabetica pelo nome
        Collections.sort(lstUsuario, Usuario.UsuarioNameComparator);
        // criar um array adapter personalizado, onde passa como parametro a lista de usuarios que podem ser alterados e
        // o login, para verificar prioridade, email e senha
        adpUsuario = new EditarUsuarioArrayAdapter(getContext(),R.layout.item_editar_usuario,lstUsuario,login);
        for(Usuario u:lstUsuario){
            // para cada usuario que pode ser alterado, guarda no array adapter
            adpUsuario.add(u);
        }

        try {
            // selecioanr o array adapter para ser alvo do evento de filtro de pesquisa
            filtaDados.setArrayAdapter(adpUsuario);
            // mostrar o array adapter na lista
            lstListaAlterarUsuario.setAdapter(adpUsuario);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // recebe uma resposta de uma classe que ela chamou, para realizar um evento
        // no caso, atualizar a lista
        listarUsuario();
    }

    @Override
    public void onClick(View v) {
        if(v == btnListarUsuarioVoltar){
        }
    }

    // a função de filtrar dados foi copiado do tutorial, que tem o link no dropbox
    public class FiltaDados implements TextWatcher {

        private ArrayAdapter<Usuario> arrayAdapter;

        private FiltaDados(ArrayAdapter<Usuario> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);
        }

        public void setArrayAdapter(ArrayAdapter arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
