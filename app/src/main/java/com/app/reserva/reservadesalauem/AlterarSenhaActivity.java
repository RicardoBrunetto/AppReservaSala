package com.app.reserva.reservadesalauem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;

public class AlterarSenhaActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private EditText edtAlterarSenhaEmail;
    private EditText edtAlterarSenhaAtual;
    private EditText edtAlterarSenhaNova;
    private EditText edtAlterarSenhaRepetir;
    private Button btnAlterarSenhaCancelar;
    private Button btnAlterarSenhaConfirmar;

    private Login login;
    private String senhaNova;
    private int resposta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        edtAlterarSenhaEmail = (EditText) findViewById(R.id.edtAlterarSenhaEmail);
        edtAlterarSenhaAtual = (EditText) findViewById(R.id.edtAlterarSenhaAtual);
        edtAlterarSenhaNova = (EditText) findViewById(R.id.edtAlterarSenhaNova);
        edtAlterarSenhaRepetir = (EditText) findViewById(R.id.edtAlterarSenhaRepetir);

        btnAlterarSenhaCancelar = (Button) findViewById(R.id.btnAlterarSenhaCancelar);
        btnAlterarSenhaCancelar.setOnClickListener(this);

        btnAlterarSenhaConfirmar = (Button) findViewById(R.id.btnAlterarSenhaConfirmar);
        btnAlterarSenhaConfirmar.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v == btnAlterarSenhaCancelar){
            finish(); // fechar tela
        }
        if(v == btnAlterarSenhaConfirmar){
            // solicitar alteração
            String email = edtAlterarSenhaEmail.getText().toString(); // pegar email digitado
            String senhaAtual = edtAlterarSenhaAtual.getText().toString(); // pegar senha digitado
            senhaNova = edtAlterarSenhaNova.getText().toString(); // pegar a nova senha
            String repetirSenha = edtAlterarSenhaRepetir.getText().toString(); // conferir a nova senha

            if(email.length() == 0){ // se campo email for vazio, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteEmail));
                return;
            }
            if(senhaAtual.length() == 0){ // se senha for vazio, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteSenhaAtual));
                return;
            }
            if(senhaNova.length() == 0){ // se a nova senha for vazio, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteSenhaNova));
                return;
            }
            if(repetirSenha.length() == 0){ // se a repeticao da nova senha for vazio, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteSenhaNova) + " outra vez");
                return;
            }
            if(!senhaNova.equals(repetirSenha)){ // se a nova senha e a senha para confirmar nova senha for diferente,
                // mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.senhaNaoConfere));
                return;
            }


            login = new Login();
            login.setEmail(email);
            login.setSenha(senhaAtual);
            Thread t = new Thread(this); // ceiar thread
            t.start(); // inicializa thread

            try {
                t.join(); // espera thread terminar de executar
                // resposta é uma variavel global que armazena o dado retornado pelo web service
                if(resposta == -1){ // se for -1, não tem permissão para alterar senha, pois a
                    // cobinação é inválida
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.notAllowed));
                    return;
                }
                if(resposta == 0){
                    // ero na conexão ou na troca de mensagem ou serividor
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.notPossiblePassChange));
                    return;
                }
                if(resposta == 1){
                    // alteração confirmada
                    MessageBox.show(this,"",getString(R.string.sucessPassChange));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void run() {
        // thread é usado para poder realizar operação envolvendo web service
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resposta = ws.alterarSenha(login,senhaNova); // envia o login e a nova senha
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
