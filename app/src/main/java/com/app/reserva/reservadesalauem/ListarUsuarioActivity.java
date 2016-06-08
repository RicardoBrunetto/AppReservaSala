package com.app.reserva.reservadesalauem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.app.reserva.reservadesalauem.dados.Departamento;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.UsuarioArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ListarUsuarioActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtListarUsuarioBusca;
    private TextView txtListarUsuarioDepartamento;
    private Button btnListarUsuarioAnterior;
    private Button btnListarUsuarioProximo;
    private Button btnListarUsuarioVoltar;
    private ListView lstListarUsuarioDepartamento;

    private ArrayAdapter<Usuario> adpUsuario;

    private Login login;
    private ArrayList<Usuario> lstUsuario;
    private int posDepartamento;
    private ArrayList<Departamento> lstDepartamento;

    private FiltaDados filtaDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuario);

        login = new Login();
        // recebe parâmetro
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }


        edtListarUsuarioBusca = (EditText) findViewById(R.id.edtListarUsuarioBusca);
        txtListarUsuarioDepartamento = (TextView) findViewById(R.id.txtListarUsuarioDepartamento);

        btnListarUsuarioAnterior = (Button) findViewById(R.id.btnListarUsuarioAnterior);
        btnListarUsuarioAnterior.setOnClickListener(this);
        btnListarUsuarioProximo = (Button) findViewById(R.id.btnListarUsuarioProximo);
        btnListarUsuarioProximo.setOnClickListener(this);
        btnListarUsuarioVoltar = (Button) findViewById(R.id.btnListarUsuarioVoltar);
        btnListarUsuarioVoltar.setOnClickListener(this);

        lstListarUsuarioDepartamento = (ListView) findViewById(R.id.lstListarUsuarioDepartamento);

        // lista de todos os departamentos
        lstDepartamento = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstDepartamento = cd.carregarDepartamento();
        // setar posição default da lista como zero (primeiro da lsita)
        posDepartamento = 0;

        // criar evento de filtro de pesquisa no campo de texto
        filtaDados = new FiltaDados(adpUsuario);
        edtListarUsuarioBusca.addTextChangedListener(filtaDados);

        // listar todos os usuarios do departamento
        listarUsuario();
        // soliciatar foco na list view, senão foco vai no edit text, que abre o teclado e atrapalha visão
        lstListarUsuarioDepartamento.requestFocus();
    }


    // pesquisar todos alunos do departamento selecionado
    public void listarUsuario(){
        // lista de usuários do departamento selecionado
        lstUsuario = new ArrayList<>();
        // lista de todos os usuarios
        ArrayList<Usuario> lstU = new ArrayList<>();
        // criar array adapter para armazenar dados
        adpUsuario = new UsuarioArrayAdapter(this,R.layout.item_usuario);
        // solicitar lsita de usuarios do servidor
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        // atualizar nome do departamento
        txtListarUsuarioDepartamento.setText(lstDepartamento.get(posDepartamento).getNome());
        for(Usuario u:lstU){
            // para cada usuario, se ele pertencer ao mesmo departamento, adiciona na lista
            if (u.getId_departamento()==lstDepartamento.get(posDepartamento).getId()){
                lstUsuario.add(u);

            }
        }
        // ordenar a lista de usuarios pelo nome
        Collections.sort(lstUsuario,Usuario.UsuarioNameComparator);
        // adicionar os usuarios no array adapter
        for(Usuario u:lstUsuario){
            adpUsuario.add(u);
        }

        try {
            // evento de filtro de dados no array adapter
            filtaDados.setArrayAdapter(adpUsuario);
            // mostrar o array adapter no list view
            lstListarUsuarioDepartamento.setAdapter(adpUsuario);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnListarUsuarioAnterior){
            // atualizar posição na lista de departamento
            posDepartamento -= 1;
            if(posDepartamento < 0){
                posDepartamento = lstDepartamento.size()-1;
            }
            // atualizar lista de usuarios em relação ao departamento
            listarUsuario();
        }
        if(v==btnListarUsuarioProximo){
            // atualizar posição do departamento
            posDepartamento += 1;
            if(posDepartamento >= lstDepartamento.size()){
                posDepartamento = 0;
            }
            // atualizar lista de usuarios em relação ao departamento
            listarUsuario();
        }
        if(v == btnListarUsuarioVoltar){
            finish();
        }
    }

    // função copiada do tutorial com link presente no dropbox.
    public class FiltaDados implements TextWatcher{

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
