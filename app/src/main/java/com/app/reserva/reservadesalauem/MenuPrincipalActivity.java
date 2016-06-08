package com.app.reserva.reservadesalauem;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity implements View.OnClickListener{

    // todos esses public final static string são nomes para as variáveis que serão passadas por
    // parâmetro por qualquer classe. Foi definido aqui para ficar como padrão e não problema com
    // erro de digitação.
    public final static String EMAIL = "EMAIL";
    public final static String SENHA = "SENHA";
    public final static String PRIVILEGIO = "PRIVILEGIO";
    public final static String LOGIN = "LOGIN";
    public final static String SALASDISPONIVEIS = "SALASDISPONIVEIS";
    public final static String PREENCHERSOLICITACAO = "PREENCHERSOLICITACAO";
    public static final String USUARIO = "USUARIO";
    public static final String SALA = "SALA";

    private Login user;
    private int id_tela = 0; // qual tela está: 0=reserva, 1=Usuarios, 2=salas/departamento/ disciplina
    private int id_ultima_tela = 3; // o id da ultima tela
    private Usuario usuario; // daod sdo usuario

    private Button btnMenuPrincipalAnterior; // botão tela anterior
    private Button btnMenuPrincipalProximo; // botão proxima tela
    private TextView txtMenuPrincipalTela; // texto para explicar qual tela é
    private Button btnMenuPrincipalOp1;
    private Button btnMenuPrincipalOp2;
    private Button btnMenuPrincipalOp3;
    private Button btnMenuPrincipalOp4;
    private Button btnMenuPrincipalOp5;
    private Button btnMenuPrincipalOp6;
    private Button btnMenuPrincipalOp7;
    private Button btnMenuPrincipalOp8;
    private Button btnMenuPrincipalOp9;


    //New Design
    GridView gridMenuMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        user = new Login(); // cria um novo login

        Bundle bundle = getIntent().getExtras(); // é usado para receber parâmetro

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){ // se paramentro tiver chave ou nome email it.setExtra(chave, dado)
            user.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){ // verifica se recebeu senha
            user.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            user.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }

        btnMenuPrincipalAnterior = (Button) findViewById(R.id.btnMenuPrincipalAnterior);
        btnMenuPrincipalAnterior.setOnClickListener(this);

        btnMenuPrincipalProximo = (Button) findViewById(R.id.btnMenuPrincipalProximo);
        btnMenuPrincipalProximo.setOnClickListener(this);

        txtMenuPrincipalTela = (TextView) findViewById(R.id.txtMenuPrincipalTela);

        btnMenuPrincipalOp1 = (Button) findViewById(R.id.btnMenuPrincipalOp1);
        btnMenuPrincipalOp1.setOnClickListener(this);

        btnMenuPrincipalOp2 = (Button) findViewById(R.id.btnMenuPrincipalOp2);
        btnMenuPrincipalOp2.setOnClickListener(this);

        btnMenuPrincipalOp3 = (Button) findViewById(R.id.btnMenuPrincipalOp3);
        btnMenuPrincipalOp3.setOnClickListener(this);

        btnMenuPrincipalOp4 = (Button) findViewById(R.id.btnMenuPrincipalOp4);
        btnMenuPrincipalOp4.setOnClickListener(this);

        btnMenuPrincipalOp5 = (Button) findViewById(R.id.btnMenuPrincipalOp5);
        btnMenuPrincipalOp5.setOnClickListener(this);

        btnMenuPrincipalOp6 = (Button) findViewById(R.id.btnMenuPrincipalOp6);
        btnMenuPrincipalOp6.setOnClickListener(this);

        btnMenuPrincipalOp7 = (Button) findViewById(R.id.btnMenuPrincipalOp7);
        btnMenuPrincipalOp7.setOnClickListener(this);

        btnMenuPrincipalOp8 = (Button) findViewById(R.id.btnMenuPrincipalOp8);
        btnMenuPrincipalOp8.setOnClickListener(this);

        btnMenuPrincipalOp9 = (Button) findViewById(R.id.btnMenuPrincipalOp9);
        btnMenuPrincipalOp9.setOnClickListener(this);

        ArrayList<ItemMain> itens = new ArrayList<>();

        itens.add(new ItemMain("Pesquisar Salas", R.drawable.shape_circle));
        itens.add(new ItemMain("Salas Livres", R.drawable.shape_circle));
        itens.add(new ItemMain("Minhas Reservas", R.drawable.shape_circle));
        itens.add(new ItemMain("Funcionalidade X", R.drawable.shape_circle));

        gridMenuMain = (GridView) findViewById(R.id.gridMenuMain);
        ItemMainAdapter adapter = new ItemMainAdapter(this, itens);
        gridMenuMain.setAdapter(adapter);

        //this.carregarTela(); // carregar os dados da tela atual
    }

    private void carregarTela(){
        // verifica o id da tela e o privilegio do usuario para mostrar as funcionalidades disponíveis
        if(id_tela == 0) { // id = 0 é a tela de reservas

            txtMenuPrincipalTela.setText(getString(R.string.reservasStr)); // entre os botões anterior e proximo, escreve o nome da tela

            // deixar os botões proximo e anterior visíveis ao usuario e ativos (desativa se usuario tiver privilegio < 2)
            btnMenuPrincipalAnterior.setVisibility(View.VISIBLE);
            btnMenuPrincipalAnterior.setEnabled(true);
            btnMenuPrincipalProximo.setVisibility(View.VISIBLE);
            btnMenuPrincipalProximo.setEnabled(true);

            btnMenuPrincipalOp1.setText(getString(R.string.solicitarReserva));
            btnMenuPrincipalOp2.setText("Minhas\n"+getString(R.string.reservasStr));
            btnMenuPrincipalOp3.setText(getString(R.string.salasLivres));
            btnMenuPrincipalOp4.setText(getString(R.string.salasReservadas));
            btnMenuPrincipalOp5.setText(getString(R.string.meusDados));
            btnMenuPrincipalOp1.setVisibility(View.VISIBLE);
            btnMenuPrincipalOp1.setEnabled(true);
            btnMenuPrincipalOp2.setVisibility(View.VISIBLE);
            btnMenuPrincipalOp2.setEnabled(true);
            btnMenuPrincipalOp3.setVisibility(View.VISIBLE);
            btnMenuPrincipalOp3.setEnabled(true);

            // desativar todas as outras telas
            btnMenuPrincipalOp4.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp4.setEnabled(false);
            btnMenuPrincipalOp5.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp5.setEnabled(false);
            btnMenuPrincipalOp6.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp6.setEnabled(false);
            btnMenuPrincipalOp7.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp7.setEnabled(false);
            btnMenuPrincipalOp8.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp8.setEnabled(false);
            btnMenuPrincipalOp9.setVisibility(View.INVISIBLE);
            btnMenuPrincipalOp9.setEnabled(false);

            if (user.getPrivilegio() == -1) { // se usuario não tiver login, mostra apenas 2 funcionalidades
                // e desativa todas as demais.
                btnMenuPrincipalOp1.setText(getString(R.string.salasLivres));
                btnMenuPrincipalOp2.setText(getString(R.string.salasReservadas));
                btnMenuPrincipalAnterior.setVisibility(View.INVISIBLE);
                btnMenuPrincipalAnterior.setEnabled(false);
                btnMenuPrincipalProximo.setVisibility(View.INVISIBLE);
                btnMenuPrincipalProximo.setEnabled(false);
                btnMenuPrincipalOp3.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp3.setEnabled(false);

            }
            if (user.getPrivilegio() == 1) { // docente
                btnMenuPrincipalAnterior.setVisibility(View.INVISIBLE);
                btnMenuPrincipalAnterior.setEnabled(false);
                btnMenuPrincipalProximo.setVisibility(View.INVISIBLE);
                btnMenuPrincipalProximo.setEnabled(false);
                btnMenuPrincipalOp4.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp4.setEnabled(true);
                btnMenuPrincipalOp5.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp5.setEnabled(true);

            }
            if(user.getPrivilegio() > 1){
                btnMenuPrincipalOp2.setText(getString(R.string.salasLivres));
                btnMenuPrincipalOp3.setText(getString(R.string.salasReservadas));

            // se usuário for secretário ou mais privilegiado, pode acessar as outras telas

                btnMenuPrincipalAnterior.setText(getString(R.string.leftleft));
                btnMenuPrincipalProximo.setText(getString(R.string.rightright));
            }

        }
        if(id_tela == 1){ // tela de controle de usuarios
            if(user.getPrivilegio() > 1) { // só é permitido para secretarios ou com permissão superior
                txtMenuPrincipalTela.setText(getString(R.string.usuariosStr));

                btnMenuPrincipalAnterior.setVisibility(View.VISIBLE);
                btnMenuPrincipalAnterior.setEnabled(true);
                btnMenuPrincipalProximo.setVisibility(View.VISIBLE);
                btnMenuPrincipalProximo.setEnabled(true);

                btnMenuPrincipalOp1.setText(getString(R.string.cadastrarUsuario));
                btnMenuPrincipalOp2.setText(getString(R.string.listarUsuario));
                btnMenuPrincipalOp3.setText(getString(R.string.alterarUsuario));
                btnMenuPrincipalOp4.setText(getString(R.string.vincularDisc) + "\n" + getString(R.string.aoUser));
                //btnMenuPrincipalOp5.setText("Cancelar Reserva");

                btnMenuPrincipalOp1.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp1.setEnabled(true);
                btnMenuPrincipalOp2.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp2.setEnabled(true);
                btnMenuPrincipalOp3.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp3.setEnabled(true);
                btnMenuPrincipalOp4.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp4.setEnabled(false);
                btnMenuPrincipalOp5.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp5.setEnabled(false);
                btnMenuPrincipalOp6.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp6.setEnabled(false);
                btnMenuPrincipalOp7.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp7.setEnabled(false);
                btnMenuPrincipalOp8.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp8.setEnabled(false);
                btnMenuPrincipalOp9.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp9.setEnabled(false);
            }
            else{
                btnMenuPrincipalAnterior.setVisibility(View.INVISIBLE);
                btnMenuPrincipalAnterior.setEnabled(false);
                btnMenuPrincipalProximo.setVisibility(View.INVISIBLE);
                btnMenuPrincipalProximo.setEnabled(false);
            }

        }
        if(id_tela==2){ // tela de controle de salas
            if(user.getPrivilegio()>1){ // só é permitido para usuarios com permissão secretio ou superior
                txtMenuPrincipalTela.setText(getString(R.string.salasStr));

                btnMenuPrincipalAnterior.setVisibility(View.VISIBLE);
                btnMenuPrincipalAnterior.setEnabled(true);
                btnMenuPrincipalProximo.setVisibility(View.VISIBLE);
                btnMenuPrincipalProximo.setEnabled(true);

                btnMenuPrincipalOp1.setText(getString(R.string.cadastrarSalas));
                btnMenuPrincipalOp2.setText(getString(R.string.listarSalas));
                btnMenuPrincipalOp3.setText(getString(R.string.alterarSala));
                //btnMenuPrincipalOp4.setText("Remover Sala");

                btnMenuPrincipalOp1.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp1.setEnabled(true);
                btnMenuPrincipalOp2.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp2.setEnabled(true);
                btnMenuPrincipalOp3.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp3.setEnabled(true);
                btnMenuPrincipalOp4.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp4.setEnabled(false);
                btnMenuPrincipalOp5.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp5.setEnabled(false);
                btnMenuPrincipalOp6.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp6.setEnabled(false);
                btnMenuPrincipalOp7.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp7.setEnabled(false);
                btnMenuPrincipalOp8.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp8.setEnabled(false);
                btnMenuPrincipalOp9.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp9.setEnabled(false);
            }
            else{
                btnMenuPrincipalAnterior.setVisibility(View.INVISIBLE);
                btnMenuPrincipalAnterior.setEnabled(false);
                btnMenuPrincipalProximo.setVisibility(View.INVISIBLE);
                btnMenuPrincipalProximo.setEnabled(false);
            }
        }
        if(id_tela==3){ // controle de disciplna
            // ainda não foi implementado, de preferencia, automatizar depois
            if(user.getPrivilegio()>1){
                txtMenuPrincipalTela.setText(getString(R.string.disciplinasStr));

                btnMenuPrincipalAnterior.setVisibility(View.VISIBLE);
                btnMenuPrincipalAnterior.setEnabled(true);
                btnMenuPrincipalProximo.setVisibility(View.VISIBLE);
                btnMenuPrincipalProximo.setEnabled(true);

                btnMenuPrincipalOp1.setText(getString(R.string.cadastrarDisc));
                btnMenuPrincipalOp2.setText(getString(R.string.listarDisc));
                btnMenuPrincipalOp3.setText(getString(R.string.alterarDisc));
                btnMenuPrincipalOp4.setText(getString(R.string.vincularDisc));
                btnMenuPrincipalOp5.setText(getString(R.string.desvincularDisc));

                btnMenuPrincipalOp1.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp1.setEnabled(true);
                btnMenuPrincipalOp2.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp2.setEnabled(true);
                btnMenuPrincipalOp3.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp3.setEnabled(true);
                btnMenuPrincipalOp4.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp4.setEnabled(true);
                btnMenuPrincipalOp5.setVisibility(View.VISIBLE);
                btnMenuPrincipalOp5.setEnabled(true);
                btnMenuPrincipalOp6.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp6.setEnabled(false);
                btnMenuPrincipalOp7.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp7.setEnabled(false);
                btnMenuPrincipalOp8.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp8.setEnabled(false);
                btnMenuPrincipalOp9.setVisibility(View.INVISIBLE);
                btnMenuPrincipalOp9.setEnabled(false);
            }
            else{
                btnMenuPrincipalAnterior.setVisibility(View.INVISIBLE);
                btnMenuPrincipalAnterior.setEnabled(false);
                btnMenuPrincipalProximo.setVisibility(View.INVISIBLE);
                btnMenuPrincipalProximo.setEnabled(false);
            }
        }
    }

    private Usuario getUsuario(String email){ // pesquisar o usuário pelo e-mail
        Usuario user1 = new Usuario(); // criar novo usuario
        ArrayList<Usuario> lstUsuario = new ArrayList<>();
        try{
            // CarregarDadosUtil é uma classe criada apenas para pegar dados do web service, para não precisar criar
            // thread em toda classe toda vez que precisa pesquisar um dado
            CarregarDadoUtils cd = new CarregarDadoUtils();
            lstUsuario = cd.carregarUsuario();
            for (Usuario u:lstUsuario){ // para cada usuario u na lista de usuario
                if(u.getEmail().equals(email)){ // verifica email. Para String não dá para usar == na hora de comparar
                    user1.clonar(u); // copiar todos os dados
                    usuario = new Usuario();
                    usuario.clonar(u);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return user1;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // o menu de 3 pontos na parte superior direito da tela
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_menu_principal, menu);

        if (user.getPrivilegio() != -1){
            menu.getItem(0).setVisible(true);
        }
        else{ // se tiver entrado sem login, não mostra
            menu.getItem(0).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ao ser clicado em um item do menu de 3 pontos no canto superior direito da tela
        if(item.getItemId() == R.id.mni_acao1){ // se for clicado na primeira opção do menu
            // tem que encerrar seção, apagar daods do arquivo que contém o email e senha
            String FILENAME = getString(R.string.arq1);
            try{
                FileOutputStream arquivoGravar = openFileOutput(FILENAME,MODE_PRIVATE);
                String conteudo = "";
                arquivoGravar.write(conteudo.getBytes());
                arquivoGravar.close();
            }
            catch (Exception exarq){
                exarq.printStackTrace();
            }
            finish(); // fechar tela
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // se botão clicado for anterior ou proximo
        //alterar id da tela
        // recarregar os dados da tela
        if (v == btnMenuPrincipalAnterior) {
            id_tela -= 1;

            if(id_tela<0){
                id_tela = id_ultima_tela;
            }
            carregarTela();
        }
        if (v == btnMenuPrincipalProximo) {
            id_tela += 1;
            if(id_tela > id_ultima_tela){
                id_tela = 0;
            }
            carregarTela();
        }
        // se o id da tela for de reserva, tem que ver prioridade do uduario para definir qual operação
        // será execuada
        if(id_tela == 0) {

            if(user.getPrivilegio() ==-1){ // usuario sem login na tela de reserva
                if (v == btnMenuPrincipalOp1) { // verificar salas disponiveis
                    Intent it = new Intent(this,SalasDisponiveisPorDiaActivity.class);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp2) {// verificar salas reservadas
                    Intent it = new Intent(this, SalasReservadasActivity.class);
                    startActivity(it);
                }

            }
            if(user.getPrivilegio() == 1){ // docente na tela de reserva
                if (v == btnMenuPrincipalOp1) { // solicitar reserva
                    Intent it = new Intent(this,SolicitarReservaActivity.class);
                    it.putExtra(MenuPrincipalActivity.PREENCHERSOLICITACAO,0);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp2) { // listar suas reservas
                    Intent it = new Intent(this,MinhasReservasActivity.class);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp3) { // listar salas disponiveis por dia
                    Intent it = new Intent(this,SalasDisponiveisPorDiaActivity.class);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp4) { // listar salas reservadas
                    Intent it = new Intent(this, SalasReservadasActivity.class);
                    startActivity(it);
                }
                if(v == btnMenuPrincipalOp5){ // alterar os proprios dados
                    Intent it = new Intent(this,AlterarUsuarioActivity.class);
                    it.putExtra(MenuPrincipalActivity.LOGIN,user);
                    it.putExtra(MenuPrincipalActivity.USUARIO,getUsuario(user.getEmail()));
                    startActivity(it);
                }
            }
            if(user.getPrivilegio() > 1){ // secretário ou superior na tela de reserva
                if (v == btnMenuPrincipalOp1) { // solicitar reserva em nome de algum docente
                    Intent it = new Intent(this,SolicitarReservaActivity.class);
                    it.putExtra(MenuPrincipalActivity.PREENCHERSOLICITACAO,0);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp2) { // visualizar salas disponíveis
                    Intent it = new Intent(this,SalasDisponiveisPorDiaActivity.class);
                    it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                    it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                    it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                    startActivity(it);
                }
                if (v == btnMenuPrincipalOp3) { // visualisar sals reservadas
                    Intent it = new Intent(this, SalasReservadasActivity.class);
                    startActivity(it);
                }

            }
        }
        if(id_tela == 1){ // tela de controle de usuario somente para secretário ou superior

            if(v == btnMenuPrincipalOp1){ // cadstrar usuario com permissao inferior
                Intent it = new Intent(this,CadastrarUsuarioActivity.class);
                it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                startActivity(it);
            }
            if(v == btnMenuPrincipalOp2){ // listar usuarios
                Intent it = new Intent(this,ListarUsuarioActivity.class);
                it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                startActivityForResult(it,1);
            }
            if(v == btnMenuPrincipalOp3){ // alterar dado de algum usuario com permissão inferior ou a si mesmo
                Intent it = new Intent(this,ListaAlterarUsuarioActivity.class);
                it.putExtra(MenuPrincipalActivity.EMAIL, user.getEmail());
                it.putExtra(MenuPrincipalActivity.SENHA,user.getSenha());
                it.putExtra(MenuPrincipalActivity.PRIVILEGIO,user.getPrivilegio());
                startActivity(it);
            }
        }
        if(id_tela==2){ // tela de controle de salas para secretário ou superior
            if(v == btnMenuPrincipalOp1){ // cadastrar sala
                Intent it = new Intent(this,CadastrarSalaActivity.class);
                it.putExtra(MenuPrincipalActivity.PREENCHERSOLICITACAO,0);
                it.putExtra(MenuPrincipalActivity.LOGIN,user);
                startActivity(it);
            }
            if(v == btnMenuPrincipalOp2){ // listar sala
                Intent it = new Intent(this,ListarSalaActivity.class);
                it.putExtra(MenuPrincipalActivity.LOGIN,user);
                startActivity(it);
            }
            if(v == btnMenuPrincipalOp3){ // listar sala para poder selecionar alguma para alteração
                Intent it = new Intent(this,ListaAlterarSalaActivity.class);
                it.putExtra(MenuPrincipalActivity.LOGIN,user);
                startActivity(it);
            }

        }
        if(id_tela==3){ // disciplinas, não implementado, pode ser alterado para o que for necessário

        }
    }

    private class ItemMain{
        String description;
        int imagem;

        public ItemMain(String description, int imagem) {
            this.description = description;
            this.imagem = imagem;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getImagem() {
            return imagem;
        }

        public void setImagem(int imagem) {
            this.imagem = imagem;
        }
    }

    protected class ItemMainAdapter extends BaseAdapter {
        Context ctx;
        List<ItemMain> itens;

        public ItemMainAdapter(Context ctx, List<ItemMain> itens) {
            this.ctx = ctx;
            this.itens = itens;
        }

        @Override
        public int getCount() {
            return itens.size();
        }

        @Override
        public Object getItem(int position) {
            return itens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemMain itemAtual = itens.get(position);

            ViewHolder holder = null;
            if (convertView == null) {
                Log.d("LogDesign", "Criando Item");
                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_principal, null);
                holder = new ViewHolder();
                holder.txtDesc   = (TextView) convertView.findViewById(R.id.txtDesc);
                holder.imgBtn = (ImageView) convertView.findViewById(R.id.imgBtn);
                convertView.setTag(holder);
            } else {
                Log.d("LogDesign", "Reciclando item");
                holder = (ViewHolder)convertView.getTag();
            }

            Log.d("LogDesign", "Preenchendo info");
            holder.txtDesc.setText(itemAtual.getDescription());
            holder.imgBtn.setImageResource(itemAtual.getImagem());

            return convertView;
        }

        protected class ViewHolder{
            TextView txtDesc;
            ImageView imgBtn;
        }
    }
}
