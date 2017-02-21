package com.app.reserva.reservadesalauem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;

public class RecuperarSenhaActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private EditText edtRecuperarEmail;
    private Button btnRecuperarCancelar;
    private Button btnRecuperarConfirmar;

    private String email;
    private int resposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        edtRecuperarEmail = (EditText) findViewById(R.id.edtRecuperarEmail);

        btnRecuperarCancelar = (Button) findViewById(R.id.btnRecuperarCancelar);
        btnRecuperarCancelar.setOnClickListener(this);

        btnRecuperarConfirmar = (Button) findViewById(R.id.btnRecuperarConfirmar);
        btnRecuperarConfirmar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnRecuperarCancelar){
            finish(); // fechar tela
        }
        if(v == btnRecuperarConfirmar){
            // resposta é uma variável global que vai receber o valor retornado pelo web service
            resposta = -1;
            email = edtRecuperarEmail.getText().toString(); // pega email digitado
            if(email.replace(" ","").length()==0){
                // se não tiver nada digitado, ou somente espcçao, mostra mensagem de erro
                MessageBox.show(this,getString(R.string.erro),getString(R.string.digiteEmail));
                return;
            }
            Thread t = new Thread(this); // criar thread para poder realizar operação com o web service
            t.start(); // executa thread

            try {
                t.join(); // espera thread terminar execução
                if(resposta == 1){
                    // se resposta for 1, a operação foi bem sucedida e foi enviado a senha para o email cadastrado
                    MessageBox.show(this, "", getString(R.string.sucessMailSent));
                    finish(); // fecha tela
                }
                if(resposta== -1){
                    // erro
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.failRecuperarUser));
                    return;
                }

            }
            catch (Exception ex){

                ex.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        // thread é usado para operação com web service
        try{
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resposta = ws.recuperarSenha(email);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
