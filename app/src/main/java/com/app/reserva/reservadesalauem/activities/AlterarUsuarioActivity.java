package com.app.reserva.reservadesalauem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.*;

import java.util.ArrayList;

// onclick é usado para click de botão e runnable para thread
public class AlterarUsuarioActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private Login login; // login
    private Usuario usuario; // dados do usuario que quer alterar

    private EditText edtAlterarUsuarioNome;
    private EditText edtAlterarUsuarioEmail;
    private EditText edtAlterarUsuarioTelefone;

    private Spinner spnAlterarUsuarioPermissao;
    private Spinner spnAlterarUsuarioProblemaLocomocao;

    private Button btnAlterarUsuarioCancelar;
    private Button btnAlterarUsuarioSalvar;


    ArrayAdapter<String> adpAlterarUsuarioPermissao;
    ArrayAdapter<String> adpAlterarUsuarioProblemaLocomocao;

    private int resposta; // resposta do servidor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        login = new Login();
        usuario = new Usuario();
        // passagem de parametro: login e usuario
        // as classes Login e Usuario devem estar serializáveis para poderem serem passadas de uma tela à outra
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(MenuPrincipalActivity.LOGIN)){
            login = (Login)bundle.get(MenuPrincipalActivity.LOGIN);
        }
        if(bundle.containsKey(MenuPrincipalActivity.USUARIO)){
            usuario = (Usuario)bundle.get(MenuPrincipalActivity.USUARIO);
        }

        edtAlterarUsuarioNome = (EditText) findViewById(R.id.edtAlterarUsuarioNome);
        edtAlterarUsuarioEmail = (EditText) findViewById(R.id.edtAlterarUsuarioEmail);
        edtAlterarUsuarioTelefone = (EditText) findViewById(R.id.edtAlterarUsuarioTelefone);

        spnAlterarUsuarioPermissao = (Spinner) findViewById(R.id.spnAlterarUsuarioPermissao);
        spnAlterarUsuarioProblemaLocomocao = (Spinner) findViewById(R.id.spnAlterarUsuarioProblemaLocomocao);

        btnAlterarUsuarioCancelar = (Button) findViewById(R.id.btnAlterarUsuarioCancelar);
        btnAlterarUsuarioCancelar.setOnClickListener(this);

        btnAlterarUsuarioSalvar = (Button) findViewById(R.id.btnAlterarUsuarioSalvar);
        btnAlterarUsuarioSalvar.setOnClickListener(this);
        //System.out.println("Recebeu");
        preencherUsuario(); // preencher os dados do usuario
        //System.out.println("Preencheu");

    }

    public void preencherUsuario(){
        // criar lista de permissao baseado na permissao do usuario logado
        ArrayList<String> lstPermissao;
        adpAlterarUsuarioPermissao = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        try {
            // adicionar todas as permissões na lista, mas selecionar apenas com privilégio menor para
            // o array adapter
            lstPermissao = new ArrayList<>();
            lstPermissao.add(getString(R.string.docente));
            lstPermissao.add(getString(R.string.secretario));
            lstPermissao.add(getString(R.string.admDepto));

            for (int i = 0; i < login.getPrivilegio() - 1; i++) {
                adpAlterarUsuarioPermissao.add(lstPermissao.get(i));
            }
            // verificar se o usuário a ser alterado é ele mesmo
            if(login.getEmail().equals(usuario.getEmail())){
                adpAlterarUsuarioPermissao.add(lstPermissao.get(usuario.getPermissao()-1));
            }
            spnAlterarUsuarioPermissao.setAdapter(adpAlterarUsuarioPermissao);
            spnAlterarUsuarioPermissao.setSelection(usuario.getPermissao()-1);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        adpAlterarUsuarioProblemaLocomocao = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        adpAlterarUsuarioProblemaLocomocao.add("Nao");
        adpAlterarUsuarioProblemaLocomocao.add("Sim");
        spnAlterarUsuarioProblemaLocomocao.setAdapter(adpAlterarUsuarioProblemaLocomocao);
        spnAlterarUsuarioProblemaLocomocao.setSelection(usuario.getProblema_locomocao());

        edtAlterarUsuarioNome.setText(usuario.getNome());
        edtAlterarUsuarioEmail.setText(usuario.getEmail());
        edtAlterarUsuarioTelefone.setText(usuario.getTelefone());

        // verifica se o usuário que quer alterar é ele mesmo
        // se for, não terá ermissão para alterar a propria prioridade
        // se o usuário for docente, não vai poder alterar o campo problema de
        // locomoção. Se quiser, terá de solicitar para alguém mais prioritário
        if(usuario.getEmail().equals(login.getEmail())){
            spnAlterarUsuarioPermissao.setEnabled(false);
            if(usuario.getPermissao() == 1){
                spnAlterarUsuarioProblemaLocomocao.setEnabled(false);
            }
        }
        else{
            // somente o proprio usuário pode alterar seu email
            edtAlterarUsuarioEmail.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnAlterarUsuarioCancelar){
            // se alteração for cancelada, retorna um resultado OK para a tela que o chamou, para que possa atualizar
            // os dados mostrados
            Intent it = new Intent();
            setResult(RESULT_OK,it);
            finish();
        }
        if(v == btnAlterarUsuarioSalvar){
            // verificar todos os campos e conferir se são válidos ou não
            // pegar dado do campo nome
            usuario.setNome(edtAlterarUsuarioNome.getText().toString());
            // verificar se tem algum caractere digitado, exceto espaço vazio
            if(usuario.getNome().replace(" ","").length()==0){
                // mostrar mensagem de erro (tela que criou mensagem de erro, titulo, texto)
                MessageBox.show(this,getString(R.string.erro),getString(R.string.naoValidoNome));
                return;
            }
            // verificar campo email
            usuario.setEmail(edtAlterarUsuarioEmail.getText().toString());
            if(usuario.getEmail().replace(" ","").length()==0){
                MessageBox.show(this,getString(R.string.erro),getString(R.string.naoValidoEmail));
                return;
            }
            usuario.setTelefone(edtAlterarUsuarioTelefone.getText().toString());
            if(usuario.getTelefone().replace(" ","").length()==0){
                MessageBox.show(this,getString(R.string.erro),getString(R.string.naoValidoTelefone));
                return;
            }
            usuario.setPermissao(spnAlterarUsuarioPermissao.getSelectedItemPosition()+1);
            usuario.setProblema_locomocao(spnAlterarUsuarioProblemaLocomocao.getSelectedItemPosition());

            try{ // esse try acho que era desnecessário
                Thread t = new Thread(this);
                t.start();

                try {
                    t.join();
                    if(resposta == 1){
                        // quando a alteração é realizada com sucesso, tem que enviar uma mensagem à tela que a chamou
                        // para poder atualizar a lista de usuarios
                        MessageBox.show(this, "", getString(R.string.userAlteradoSucesso));
                        Intent it = new Intent();
                        setResult(RESULT_OK,it);
                        finish();
                    }
                    if(resposta== -1){
                        // se o usuário não tinha permissão, não faz nada
                        MessageBox.show(this,getString(R.string.erro),getString(R.string.notAllowed));
                        return;
                    }
                    if (resposta== -2){
                        // se e-mail já e usado por outro usuário, dá erro
                        MessageBox.show(this,getString(R.string.erro),getString(R.string.emailInUse));
                        return;
                    }
                    if(resposta == 0){
                        MessageBox.show(this, getString(R.string.erro),getString(R.string.taskBDFail));
                    }
                }
                catch (Exception en){
                    en.printStackTrace();
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            // recebe o valor que o servidor enviou à classe AcessoAppUemWS
            resposta = ws.alteraUsuario(login,usuario);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
