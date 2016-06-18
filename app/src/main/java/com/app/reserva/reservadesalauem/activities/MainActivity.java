package com.app.reserva.reservadesalauem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private Button btnMainLogin;
    private Button btnMainEntrar;
    private TextView btnMainEsqueceuSenha;
    private Login login;
    private TextView btnVerificaSistema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMainLogin = (Button) findViewById(R.id.btnMainLogin);
        btnMainLogin.setOnClickListener(this);

        btnMainEntrar = (Button) findViewById(R.id.btnMainEntrar);
        btnMainEntrar.setOnClickListener(this);

        btnMainEsqueceuSenha = (TextView) findViewById(R.id.btnMainEsqueceuSenha);
        btnMainEsqueceuSenha.setOnClickListener(this);

        btnVerificaSistema = (TextView) findViewById(R.id.btnVerificaSistema);
        btnVerificaSistema.setOnClickListener(this);

        String FILENAME = getString(R.string.arq1); // arquivo onde será salvo o login (email) e a senha
        try{
            File arquivoLido = getFileStreamPath(FILENAME); // tenta carregar arquivo com o nome = FILENAME
            if(arquivoLido.exists()){ // verifica se arquivo existe
                FileInputStream arquivoLer = openFileInput(FILENAME); // abre arquio para leitura, não sei bem como funciona. foi pesquisado no google
                InputStreamReader inputStream = new InputStreamReader(arquivoLer);
                BufferedReader br = new BufferedReader(inputStream); // converte para um bufferedReader
                String linha;

                //String conteudoLido = "";
                ArrayList<String> lista_linhas= new ArrayList<>(); // ArrayList de String onde armazena o email e a senha nas posições 0 e 1

                while ( (linha = br.readLine()) != null ) { // verifica se tem dado na linha do arquivo
                    lista_linhas.add(linha); // adiciona o dado na lista
                    //conteudoLido += linha;
                    //conteudoLido += "\n";
                }

                //System.out.println("Arquivo: " + conteudoLido);

                arquivoLer.close(); // fechar o arquivo, senão fica como lixo ocupando espaço
                inputStream.close(); //

                if(lista_linhas.size() > 1){ // se tiver um login e uma senha escrito (minimo de 2 dados)
                    login = new Login(); // cria um novo objeto do tipo Login
                    login.setEmail(lista_linhas.get(0)); // adiciona o email e a senha
                    login.setSenha(lista_linhas.get(1));
                    Thread t = new Thread(this); // para chamar uma funcionalidade relacionado à conexão com o servidor,
                    // é nescessário executá-lo dentro de uma thread, para isso,
                    // cria uma thread dessa mesma classe (precisa chamar implements runnable para poder transformar a classe em uma thread)
                    t.start();  // inicia a thread

                    try {
                        t.join(); // espera a thread terminar de rodar, senão não dá tempo dela atualizar os dados.

                        if(login.getPrivilegio() != -1){ // se o usuário for válido, já vai na tela principal, passando o login como parametro
                            Intent it = new Intent(this,MenuPrincipalActivity.class);
                            // it.putExtra(nome da variável, dado); usada para passagem de paramentro
                            it.putExtra(MenuPrincipalActivity.EMAIL,login.getEmail());
                            it.putExtra(MenuPrincipalActivity.SENHA,login.getSenha());
                            it.putExtra(MenuPrincipalActivity.PRIVILEGIO,login.getPrivilegio());
                            startActivity(it);
                        }
                    } catch (InterruptedException e) {
                        // se não for possível encontrar o usuário, não faz nada
                        e.printStackTrace(); // mostra todo o caminho até o erro
                    }
                }
            }


        }
        catch (Exception exarq){
            // tratamento de excessão para erro em leitura de arquivos
            exarq.printStackTrace();

        }


    }

    @Override
    public void onClick(View v) {
        // operação ao clicar um botão
        if(v == btnMainLogin){ // se o botão clicado for de login, vai para a tela de login, sem precisar passar parametro
            Intent it = new Intent(this,LoginActivity.class);
            startActivity(it);

        }
        if(v == btnMainEntrar){
            // se for de entrar, vai na tela principal com privilegio -1 (sem privilegio)
            Intent it = new Intent(this,MenuPrincipalActivity.class);
            // it.putExtra(nome da variável, dado); usado para passagem de parameetro
            it.putExtra(MenuPrincipalActivity.EMAIL,"1");
            it.putExtra(MenuPrincipalActivity.SENHA,"1");
            it.putExtra(MenuPrincipalActivity.PRIVILEGIO,-1);
            startActivity(it);

        }
        if(v == btnMainEsqueceuSenha){ // se o botão for de esqueceu senha, vai na tela;
            //Intent it = new Intent(this,ListarReservaPorDiaActivity.class);
            //TODO: IMPLEMENTAR OS FRAGMENTS RESTANTES
            //Intent it = new Intent(this,RecuperarSenhaActivity.class); // criar classe
            //startActivity(it); // inicializar tela
        }
        if(v == btnVerificaSistema){
            //Intent it = new Intent(this,VerificaServidorActivity.class);
            //startActivity(it);
        }
    }

    @Override
    public void run() { // funcionalidade que a thread executa
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS(); // cria um objeto da classe que faz toda operação com o web service
            login.setPrivilegio(ws.confirmarLogin(login)); // chama a função de confirmar login, que retorna o privilégio,
            // se não for encontrado, retorna -1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
