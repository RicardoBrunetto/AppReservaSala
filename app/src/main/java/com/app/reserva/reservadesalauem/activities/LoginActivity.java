package com.app.reserva.reservadesalauem.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.app.*;

import java.io.FileOutputStream;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,Runnable{

    private EditText edtLoginEmail;
    private EditText edtLoginSenha;
    private CheckBox cbxLoginLembrar;

    private Button btnLoginCancelar;
    private Button btnLoginConfirmar;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = new Login(); // cria um objeto tipo login

        edtLoginEmail = (EditText) findViewById(R.id.edtLoginEmail);
        edtLoginSenha = (EditText) findViewById(R.id.edtLoginSenha);
        cbxLoginLembrar = (CheckBox) findViewById(R.id.cbxLoginLembrar);

        btnLoginCancelar = (Button) findViewById(R.id.btnLoginCancelar);
        btnLoginCancelar.setOnClickListener(this);

        btnLoginConfirmar = (Button) findViewById(R.id.btnLoginConfirmar);
        btnLoginConfirmar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // precisa de implements View.OnClickListener
        if(v == btnLoginCancelar){
            finish(); // fecha essa tela abre a anterior
        }
        if(v == btnLoginConfirmar){
            // verificar login, se der certo, vai para o menu principal, senão, mensagem de erro
            if(edtLoginEmail.getText().toString().replace(" ","").length() == 0){ // verifica se tem algum dado no campo email
                // o replace foi usado para eliminar espaço vazio, para o caso do usuario digitar só espaço
                MessageBox.show(this,getString(R.string.erro), getString(R.string.emptyEmail)); // mostra mensagem de erro
                return;
            }
            if(edtLoginSenha.getText().toString().replace(" ","").length() == 0){ // verifica se tem algum dado na senha
                // o replace foi usado para eliminar espaço vazio, para o caso do usuario digitar só espaço
                MessageBox.show(this,getString(R.string.erro),getString(R.string.emptySenha));
                return;
            }


            Thread t = new Thread(this); // criar nova thread para poder chamar funcionalidade relacionado à Web Service
            t.start(); // inicializar thread

            try{
                t.join(); // esperar thread terminar de executar
                if(login.getPrivilegio() != -1){ // se o usuário existir
                    if(cbxLoginLembrar.isChecked()){ // verifica se ele selecionou lembrar os dados ou não
                        this.salvarDados(); // salvar os dados no arquivo
                    }
                    // ir para a tela principal passando o login como paramentro
                    Intent it = new Intent(this,MenuPrincipalActivity.class); // criar a tela principal
                    // it.putExtra(nome da variável, dado);
                    it.putExtra(MenuPrincipalActivity.EMAIL,login.getEmail()); // passar email como parametro
                    it.putExtra(MenuPrincipalActivity.SENHA,login.getSenha()); // passar senha como paramentro
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,login.getPrivilegio()); // passar privilegio como parametro
                    startActivity(it); // inicializar a tela do menu principal
                }else{
                    AlertDialog.Builder adlg = new AlertDialog.Builder(this); // criar uma mensagem de alerta,
                    MessageBox.show(this, getString(R.string.erro), getString(R.string.notFoundUser));
                    // caracteres especiais como '~' as vezes não é reconhecido pelo android e podem ser transformados em
                    // simbolos estranhos. se precisar colocar, é recomendado inserir na classe string, para não dar erro

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void salvarDados(){
        String FILENAME = getString(R.string.arq1); // nome do arquivo onde será salvo os dados
        try{
            FileOutputStream arquivoGravar = openFileOutput(FILENAME,MODE_PRIVATE); // abre arquivo para gravar dados, apagando tudo que tem nela
            String conteudo = ""; // conteúdo a ser salvo
            conteudo += login.getEmail(); // login (email) na primeira linha
            conteudo += "\n"; // quebra de linha
            conteudo += login.getSenha(); // senha na segunda linha

            arquivoGravar.write(conteudo.getBytes()); // salvar os dados no arquivo
            arquivoGravar.close(); // fechar arquivo


        }
        catch (Exception exarq){
            exarq.printStackTrace();

        }
    }

    @Override
    public void run() {
        // funcionalidades da thread, precisa de implements Runnable
        login.setEmail(edtLoginEmail.getText().toString()); // pega texto digitado no campo email
        login.setSenha(edtLoginSenha.getText().toString()); // pega texto digitado no campo senha
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS(); // criar objeto para conexão com web service
            login.setPrivilegio(ws.confirmarLogin(login)); // receber o privilegio do usuario
            // caso não seja encontrado usuario, retorna -1
        }
        catch (Exception ex) {
            Log.e("LoginActivity", getString(R.string.erro), ex);
        }
    }
}
