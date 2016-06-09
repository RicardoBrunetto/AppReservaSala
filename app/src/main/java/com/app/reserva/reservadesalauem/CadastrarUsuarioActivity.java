package com.app.reserva.reservadesalauem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.util.ArrayList;

public class CadastrarUsuarioActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private Login login;

    private EditText edtCadastrarUsuarioNome;
    private EditText edtCadastrarUsuarioEmail;
    private EditText edtCadastrarUsuarioTelefone;
    private EditText edtCadastrarUsuarioSenha;
    private EditText edtCadastrarUsuarioRepetirSenha;
    private Spinner spnCadastrarUsuarioProblemaLocomocao;
    private Spinner spnCadastrarUsuarioPermissao;
    private Button btnCadastrarUsuarioCancelar;
    private Button btnCadastrarUsuarioCadastrar;

    private ArrayAdapter<String> adpPermissao;
    private ArrayAdapter<String> adpProblemaLocomocao;

    private ArrayList<String> lstPermissao;

    private int resposta;
    private Usuario user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        login = new Login(); // criar um novo login

        Bundle bundle = getIntent().getExtras(); // usado para passagem de parametro

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){ // verifica se existe parametro com esse nome
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){ // verifica se existe parametro com esse nome
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){ // verifica se existe parametro com esse nome
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }


        edtCadastrarUsuarioNome = (EditText) findViewById(R.id.edtCadastrarUsuarioNome);
        edtCadastrarUsuarioEmail = (EditText) findViewById(R.id.edtCadastrarusuarioEmail);
        edtCadastrarUsuarioTelefone = (EditText) findViewById(R.id.edtCadastrarUsuarioTelefone);
        edtCadastrarUsuarioSenha = (EditText) findViewById(R.id.edtCadastrarUsuarioSenha);
        edtCadastrarUsuarioRepetirSenha = (EditText) findViewById(R.id.edtCadastrarUsuarioRepetirSenha);

        btnCadastrarUsuarioCancelar = (Button) findViewById(R.id.btnCadastrarUsuarioCancelar);
        btnCadastrarUsuarioCancelar.setOnClickListener(this);
        btnCadastrarUsuarioCadastrar = (Button) findViewById(R.id.btnCadastrarUsuarioCadastrar);
        btnCadastrarUsuarioCadastrar.setOnClickListener(this);

        spnCadastrarUsuarioPermissao = (Spinner) findViewById(R.id.spnCadastrarUsuarioPermissao);
        spnCadastrarUsuarioProblemaLocomocao = (Spinner) findViewById(R.id.spnCadastrarUsuarioProblemaLocomocao);

        try { // verificar permissões menores ou iguais a ele para poder ser usado
            lstPermissao = new ArrayList<String>(); // cria lista de permissões possíveis
            lstPermissao.add(getString(R.string.docente));
            lstPermissao.add(getString(R.string.secretario));
            lstPermissao.add(getString(R.string.admDepto));
            adpPermissao = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1); // cria um array adapter
            // para as permissões
            for (int i = 0; i < login.getPrivilegio() - 1; i++) {
            // adiciona somente aqueles com permissão inferior ao usuario
                adpPermissao.add(lstPermissao.get(i));
            }
            spnCadastrarUsuarioPermissao.setAdapter(adpPermissao); // setar o array adapter no sppiner
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        // criar array adapter para problema de locomoção
        adpProblemaLocomocao = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        adpProblemaLocomocao.add(getString(R.string.nao)); // inserir as strings sim e não
        adpProblemaLocomocao.add(getString(R.string.sim));
        // setar o array adapter na sppiner
        spnCadastrarUsuarioProblemaLocomocao.setAdapter(adpProblemaLocomocao);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCadastrarUsuarioCancelar){
            finish(); // fechar tela
        }
        if(v == btnCadastrarUsuarioCadastrar){ // cadastrar usuario

            ArrayList<Usuario> lstUsuario = new ArrayList<>(); // lista de todos os usuarios
            try{
                CarregarDadoUtils cd = new CarregarDadoUtils(); // carregar dados do servidor
                lstUsuario = cd.carregarUsuario(); // carregar todos os usuarios do servidor
            }
            catch(Exception ex){
            }
            user1 = new Usuario(); // criar novo usuario
            Usuario userAtual = new Usuario(); // usuario que está logado
            String nome = edtCadastrarUsuarioNome.getText().toString(); // pegar nome do campo
            if(nome.replace(" ","").length() == 0){
                // se campo nome estiver vazio ou somente com espaço em branco, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.naoValidoNome));
                return;
            }
            // pega dado do campo email
            String email = edtCadastrarUsuarioEmail.getText().toString();
            if(email.replace(" ","").length() == 0){
                // verifica se email está vazio ou não
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteEmail));
                return;
            }
            //
            for(Usuario u:lstUsuario){
                if(u.getEmail().equals(email)){
                    // verifica se email está cadastrado no BD ou é válido
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.emailInUse));
                    return;
                }
                if(u.getEmail().equals(login.getEmail())){
                    // carregar o usuario atual
                    userAtual.clonar(u);
                }
            }
            // pega dado do campo telefone
            String telefone = edtCadastrarUsuarioTelefone.getText().toString();
            if(telefone.replace(" ","").length() == 0){
                // verifica se campo esta vazio ou não
                MessageBox.show(this,getString(R.string.erro),getString(R.string.naoValidoTelefone));
                return;
            }
            // pega dado do campo senha
            String senha = edtCadastrarUsuarioSenha.getText().toString();
            if(senha.replace(" ","").length() == 0){
                // verifica se senha esta vazio ou não
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteSenhaAtual));
                return;
            }
            // pega dado do campo repetir senha
            String repetirsenha = edtCadastrarUsuarioRepetirSenha.getText().toString();
            if(repetirsenha.replace(" ","").length() == 0){
                // verifica se campo está vazio ou não
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteSenhaAtual) + " novamente");
                return;
            }
            // verifica se a senha digitada e repetida são iguais
            if(!senha.equals(repetirsenha)){
                MessageBox.show(this,getString(R.string.erro), getString(R.string.senhaNaoConfere));
                return;
            }
            // pega permissão do sppiner, vai pegar 0 para docente, 1 para secretário, ...
            int permissao = spnCadastrarUsuarioPermissao.getSelectedItemPosition();
            //System.out.println(permissao);
            // tem que inclementar permissão em 1, pois é usado permissão 1,2,3,...
            permissao = permissao + 1;
            //System.out.println(permissao);
            // pega problema de locomoção da sppiner
            int problemalocomocao = spnCadastrarUsuarioProblemaLocomocao.getSelectedItemPosition();

            // pega id do departamento do usuario que está logado
            int iddepartamento = userAtual.getId_departamento();

            // seta id como -1 somente para não deixá-lo vazio, o valor do id é irrelevante na hora de cadastrar
            user1.setId(-1);
            // seta o email
            user1.setEmail(email);
            // seta o id d departamento
            user1.setId_departamento(iddepartamento);
            // como usuario é novo, está sem disciplinas
            user1.setId_disciplinas("");
            // seta telefone
            user1.setTelefone(telefone);
            //seta a senha
            user1.setSenha(senha);
            // onome
            user1.setNome(nome);
            // status = 1 = ativo
            user1.setStatus(1);
            // permissão pego do sppiner
            user1.setPermissao(permissao);
            // problwema de locomoção pego do sppiner
            user1.setProblema_locomocao(problemalocomocao);
            try{ // try desnecessário
                Thread t = new Thread(this);
                t.start();

                try {
                    t.join();
                    if(resposta == 1){ // cadastrado com sucesso
                        MessageBox.show(this,"",getString(R.string.userCadastroSucesso));
                        finish();
                    }
                    if(resposta== -1){ // não tem permissão para cadastrar, seja por não estar cadastrado no sistema
                        // ou permissão inferior
                        MessageBox.show(this,getString(R.string.erro),getString(R.string.notAllowed));
                        return;
                    }
                    if (resposta== -2){ // email já usado por outro usuario, conferencia do email pelo servidor,
                        // só por garantia
                        MessageBox.show(this,getString(R.string.erro),getString(R.string.emailInUse));
                        return;
                    }
                    if(resposta == 0){ // erro no BD ou comunicação
                        MessageBox.show(this, getString(R.string.erro),getString(R.string.taskBDFail));
                        return;
                    }
                }
                catch (Exception en){
                }
            }
            catch(Exception ex){
            }

        }
    }

    @Override
    public void run() {
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resposta = ws.cadastrarUsuario(login,user1); // enivia o login do usuario e os dados do novo usuario
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
