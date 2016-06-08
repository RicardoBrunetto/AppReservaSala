package com.app.reserva.reservadesalauem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;

public class CadastrarSalaActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private EditText edtCadastrarSalaNumero;
    private EditText edtCadastrarSalaDescricao;

    private Spinner spnCadastrarSalaClassificacao;

    private ArrayAdapter<String> adpClassificacaoSala;

    private Button btnCadastrarSalaCancelar;
    private Button btnCadastrarSalaCadastrar;

    private int resposta; // resposta do servidor
    private Sala sala;
    private Login login;
    private Usuario usuario;

    private int opcao; // cadastrar / alterar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_sala);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(MenuPrincipalActivity.LOGIN)){
            login = (Login)bundle.get(MenuPrincipalActivity.LOGIN);
        }
        opcao = 0; // 0 para cadastrar e 1 para alterar
        if (bundle.containsKey(MenuPrincipalActivity.PREENCHERSOLICITACAO)){ // verifica se solicitação é para alteração ou não
            opcao = (Integer)bundle.get(MenuPrincipalActivity.PREENCHERSOLICITACAO);
        }

        edtCadastrarSalaNumero = (EditText) findViewById(R.id.edtCadastrarSalaNumero);
        edtCadastrarSalaDescricao = (EditText) findViewById(R.id.edtCadastrarSalaDescricao);

        spnCadastrarSalaClassificacao = (Spinner) findViewById(R.id.spnCadastrarSalaClassificacao);
        adpClassificacaoSala = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1); // array adapter para a sppiner
        // inserir dados na array adapter
        adpClassificacaoSala.add(getString(R.string.laboratorio));
        adpClassificacaoSala.add(getString(R.string.projecao));

        spnCadastrarSalaClassificacao.setAdapter(adpClassificacaoSala);
        spnCadastrarSalaClassificacao.requestFocus(); // soliciatr foco nesse array adapter, senão o foco vai no primeiro edit text que
        // encontrar, aí inicializa o teclado e pode atrapalhar visão

        btnCadastrarSalaCancelar = (Button) findViewById(R.id.btnCadastrarSalaCancelar);
        btnCadastrarSalaCancelar.setOnClickListener(this);
        btnCadastrarSalaCadastrar = (Button) findViewById(R.id.btnCadastrarSalaCadastrar);
        btnCadastrarSalaCadastrar.setOnClickListener(this);

        getUsuario(login.getEmail()); // busca o usuario pelo email
        sala = new Sala(); // criar uma nova sala
        if(opcao == 1){ // preencher dados para a alteração
            if (bundle.containsKey(MenuPrincipalActivity.SALA)){
                // para passar um objeto como paramentro, é necessário serializar a classe
                sala = (Sala)bundle.get(MenuPrincipalActivity.SALA);
            }
            preencherDados(); // preenche os campos
        }

    }

    private void preencherDados(){
        edtCadastrarSalaNumero.setText(""+sala.getNumero()); // preenche o campo numero, convertendo int para String
        // a posição do sppiner começa em 0, mas a classificação da sala é 1 ou 2, então subtrai um
        spnCadastrarSalaClassificacao.setSelection(sala.getClassificacao() - 1);
        edtCadastrarSalaDescricao.setText(sala.getDescricao());
    }

    private void getUsuario(String email){ // pesquisar o usuario atraves de seu email

        ArrayList<Usuario> lstUsuario = new ArrayList<>(); // lista de usuarios
        try {
            CarregarDadoUtils cd = new CarregarDadoUtils();
            lstUsuario = cd.carregarUsuario(); // carregar todos os usuarios
            for (Usuario u : lstUsuario) {
                //System.out.println(u.getEmail());
                if (u.getEmail().equals(email)) {
                   // se email for igual, copia
                    usuario = new Usuario();
                    usuario.clonar(u);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean numeroUsado(int numero){ // verifica se tem uma sala com o mesmo numero no departamento

        ArrayList<Sala> lstS = new ArrayList<>(); // lista de salas
        try {
            CarregarDadoUtils cd = new CarregarDadoUtils();
            lstS = cd.carregarSala(); // carregar todas as salas do servidor
            for (Sala s:lstS) { // verifica todas as salas
                if(s.getId_departamento()==usuario.getId_departamento()) { // verifica o departamento da sala
                    if (s.getNumero() == numero) { //se numero for igual, já existe
                        return true;
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false; // não existe
    }

    @Override
    public void onClick(View v) {
        if(v==btnCadastrarSalaCancelar){
            finish(); // fechar tela
        }
        if(v==btnCadastrarSalaCadastrar){
            try {
                if(opcao == 0) { // cadastrar uma nova sala
                    sala = new Sala(); // criar nova sala
                    sala.setId(-1); // setar id como -1, só para não deixar vazio
                    // na hora de cadastrar no BD, é inclemental, então o valor que passa
                    // para ele é irrelevante

                    // verifica se digitou algum texto ou não (espaço em vazio é eliminado)
                    if (edtCadastrarSalaNumero.getText().toString().replace(" ", "").length() == 0) {

                        MessageBox.show(this, getString(R.string.erro), getString(R.string.inserirNumeroSala));
                        return;
                    }
                    // transforma em numero
                    sala.setNumero(Integer.parseInt(edtCadastrarSalaNumero.getText().toString()));
                    // verifica se o número já está cadastrado ou não
                    if (numeroUsado(sala.getNumero())) {
                        MessageBox.show(this, getString(R.string.erro), getString(R.string.numeroSalaInUse));
                        return;
                    }
                    // para pegar classificação, pega posição do sppiner + 1
                    sala.setClassificacao(spnCadastrarSalaClassificacao.getSelectedItemPosition() + 1);
                    // descrição é texto, pode estar vazio
                    sala.setDescricao(edtCadastrarSalaDescricao.getText().toString());
                    // status é 1 (ativo)
                    sala.setStatus(1);
                    // pega id do departamento do usuario
                    sala.setId_departamento(usuario.getId_departamento());
                    // inicializar thread
                    Thread t = new Thread(this);
                    t.start();

                    try {
                        t.join(); // espera fim da execução da thread
                        if (resposta == -1) { // não tem permissão para cadastrar
                            MessageBox.show(this, getString(R.string.erro), getString(R.string.notAllowed));
                            return;
                        }
                        if (resposta == 0) { // deu erro
                            MessageBox.show(this, getString(R.string.erro), getString(R.string.requestFailed));
                            return;
                        }
                        if (resposta == 1) { // foi bem sucedido
                            MessageBox.show(this, "", getString(R.string.salaCadastroSucesso));
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{ // alterar uma sala
                    // verificar se foi digitado algum caractere
                    if (edtCadastrarSalaNumero.getText().toString().replace(" ", "").length() == 0) {
                        MessageBox.show(this, getString(R.string.erro), getString(R.string.inserirNumeroSala));
                        return;
                    }
                    // verificar se o numero foi alterado
                    if(sala.getNumero() != Integer.parseInt(edtCadastrarSalaNumero.getText().toString())){
                        // se numero foi alterado
                        if (numeroUsado(sala.getNumero())) { // verifica se o numero já é usado
                            // se sim, retorna mensagem de erro
                            MessageBox.show(this, getString(R.string.erro), getString(R.string.numeroSalaInUse));
                            return;
                        }
                    }
                    // transformar o numero em inteiro
                    sala.setNumero(Integer.parseInt(edtCadastrarSalaNumero.getText().toString()));
                    // a classificação da sala é igual à posição da sppiner + 1
                    sala.setClassificacao(spnCadastrarSalaClassificacao.getSelectedItemPosition() + 1);
                    // descrição é String, pode ser vazio
                    sala.setDescricao(edtCadastrarSalaDescricao.getText().toString());
                    // cria e inicializa a thread para a operação com servidor
                    Thread t = new Thread(this);
                    t.start();

                    try {
                        t.join(); // espera fim da thread
                        if (resposta == -1) { // sem permissão para alterar
                            MessageBox.show(this, getString(R.string.erro), getString(R.string.notAllowed));
                            return;
                        }
                        if (resposta == 0) { // erro
                            MessageBox.show(this, getString(R.string.erro), getString(R.string.requestFailed));
                            return;
                        }
                        if (resposta == 1) { // operação realizada com sucesso
                            MessageBox.show(this, "",  getString(R.string.salaCadastroSucesso));
                            Intent it = new Intent(); // cria it para passagem de parametro
                            setResult(RESULT_OK,it); // seta valor de retorno
                            finish(); // encerra tela, devolvendo à tela que criou essa, o valor de retorno
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try{
            // todo AcesoAppUemWS deve estar em uma thread
            AcessoAppUemWS ws = new AcessoAppUemWS();
            if(opcao==0){ // cadastrar sala
                resposta = ws.cadastrarSala(login,sala);
            }
            else{ // alterar sala
                resposta = ws.alterarSala(login,sala);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
