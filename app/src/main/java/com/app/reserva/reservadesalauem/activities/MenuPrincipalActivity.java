package com.app.reserva.reservadesalauem.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.fragments.CadastrarUsuarioFragment;
import com.app.reserva.reservadesalauem.fragments.ListaAlterarSalaFragment;
import com.app.reserva.reservadesalauem.fragments.ListaAlterarUsuarioFragment;
import com.app.reserva.reservadesalauem.fragments.ListarSalaFragment;
import com.app.reserva.reservadesalauem.fragments.MinhasReservasFragment;
import com.app.reserva.reservadesalauem.fragments.SalasDisponiveisPorDiaFragment;
import com.app.reserva.reservadesalauem.fragments.SalasReservadasFragment;
import com.app.reserva.reservadesalauem.fragments.SolicitarReservaFragment;

import java.io.File;

public class MenuPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    public FragmentManager fragmentManager; //Objeto que irá controlar os fragments na activity principal
    NavigationView navigationView; //Objeto que irá controlar as ações do NavigationDrawer

    //Fragments
    MinhasReservasFragment minhasReservasFragment;
    SalasDisponiveisPorDiaFragment salasDisponiveisPorDiaFragment;
    SolicitarReservaFragment solicitarReservaFragment;
    ListarSalaFragment listarSalaFragment;
    SalasReservadasFragment salasReservadasFragment;
    ListaAlterarSalaFragment listaAlterarSalaFragment;
    CadastrarUsuarioFragment cadastrarUsuarioFragment;
    ListaAlterarUsuarioFragment listaAlterarUsuarioFragment;

    private Login user;
    private int id_tela = 0; // qual tela está: 0=reserva, 1=Usuarios, 2=salas/departamento/ disciplina
    private int id_ultima_tela = 3; // o id da ultima tela

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        instanciarViews();

        //Até aqui é a inicialização do NavigationDrawer e do FloatButton

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
        refreshNavItems();
        mostrarFragmentPrincipal();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        //TextView txt_cargo = (TextView) findViewById(R.id.txt_cargo);
        TextView txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setText(user.getEmail());
        TextView txt_cargo = (TextView) findViewById(R.id.txt_cargo);
        txt_cargo.setText("Privilégio:" + user.getPrivilegio());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_minhasReservas && !navigationView.getMenu().findItem(R.id.nav_minhasReservas).isChecked()) {
            if (minhasReservasFragment == null){
                minhasReservasFragment = new MinhasReservasFragment();
                Bundle args = new Bundle();
                args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
                args.putString(MenuPrincipalActivity.SENHA, user.getSenha());
                args.putInt(MenuPrincipalActivity.PRIVILEGIO, user.getPrivilegio());
                minhasReservasFragment.setArguments(args);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, minhasReservasFragment).commit();
        } else if (id == R.id.nav_salasDisponiveis && !navigationView.getMenu().findItem(R.id.nav_salasDisponiveis).isChecked()) {
            if (salasDisponiveisPorDiaFragment == null){
                salasDisponiveisPorDiaFragment = new SalasDisponiveisPorDiaFragment();
                Bundle args = new Bundle();
                args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
                args.putString(MenuPrincipalActivity.SENHA, user.getSenha());
                args.putInt(MenuPrincipalActivity.PRIVILEGIO, user.getPrivilegio());
                salasDisponiveisPorDiaFragment.setArguments(args);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, salasDisponiveisPorDiaFragment).commit();
        } else if (id == R.id.nav_solicitarReserva && !navigationView.getMenu().findItem(R.id.nav_solicitarReserva).isChecked()) {
            if (solicitarReservaFragment == null) {
                solicitarReservaFragment = new SolicitarReservaFragment();
                Bundle args = new Bundle();
                args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
                args.putString(MenuPrincipalActivity.SENHA, user.getSenha());
                args.putInt(MenuPrincipalActivity.PRIVILEGIO, user.getPrivilegio());
                solicitarReservaFragment.setArguments(args);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, solicitarReservaFragment).commit();
        }else if (id == R.id.nav_cadastrarSala  && !navigationView.getMenu().findItem(R.id.nav_cadastrarSala).isChecked()) {
            if (listarSalaFragment == null) {
                listarSalaFragment = new ListarSalaFragment();
                Bundle args = new Bundle();
                Log.d("LOG1", user.getEmail());
                args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
                listarSalaFragment.setArguments(args);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, listarSalaFragment).commit();
        }else if(id == R.id.nav_salasReservadas && !navigationView.getMenu().findItem(R.id.nav_salasReservadas).isChecked()){
            if (salasReservadasFragment == null)
                salasReservadasFragment = new SalasReservadasFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, salasReservadasFragment).commit();
        }else if (id == R.id.nav_listaAlterarSala  && !navigationView.getMenu().findItem(R.id.nav_cadastrarSala).isChecked()) {
            if (listaAlterarSalaFragment == null) {
                listaAlterarSalaFragment = new ListaAlterarSalaFragment();
                Bundle args = new Bundle();
                args.putSerializable(MenuPrincipalActivity.LOGIN, user);
                listaAlterarSalaFragment.setArguments(args);
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, listaAlterarSalaFragment).commit();
        }else if (id == R.id.nav_cadastrarUsuario  && !navigationView.getMenu().findItem(R.id.nav_cadastrarUsuario).isChecked()) {
            cadastrarUsuarioFragment = new CadastrarUsuarioFragment();
            Bundle args = new Bundle();
            args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
            args.putString(MenuPrincipalActivity.SENHA, user.getSenha());
            args.putInt(MenuPrincipalActivity.PRIVILEGIO, user.getPrivilegio());
            cadastrarUsuarioFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content_frame, cadastrarUsuarioFragment).commit();
        }else if (id == R.id.nav_listaAlterarUsuario  && !navigationView.getMenu().findItem(R.id.nav_listaAlterarUsuario).isChecked()) {
            listaAlterarUsuarioFragment = new ListaAlterarUsuarioFragment();
            Bundle args = new Bundle();
            args.putString(MenuPrincipalActivity.EMAIL, user.getEmail());
            args.putString(MenuPrincipalActivity.SENHA, user.getSenha());
            args.putInt(MenuPrincipalActivity.PRIVILEGIO, user.getPrivilegio());
            listaAlterarUsuarioFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content_frame, listaAlterarUsuarioFragment).commit();
        }else if(id == R.id.nav_sair){
            File arquivoLido = getFileStreamPath(getString(R.string.arq1)); // tenta carregar arquivo com o nome = FILENAME
            if(arquivoLido.exists()){
                arquivoLido.delete();
            }
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

        //TODO: ADICIONAR UM NAV_ITEM PARA CADA FUNCIONALIDADE E VERIFICAR PRIVILÉGIOS
        //TODO: TERMINAR DE IMPLEMENTAR O MENUPRINCIPAL
    }

    /**Procedimento que instancia as views já declaradas*/
    private void instanciarViews(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /** Esta função seta o layout da activity para o
     * layout princípal, que no caso é o Minhas Reservas **/
    private void mostrarFragmentPrincipal() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MinhasReservasFragment()).commit();
        navigationView.getMenu().findItem(R.id.nav_minhasReservas).setChecked(true);
    }

    /** Esta função seta para invisible um item da nav
     *
     * @param id item a ser escondido;
     */
    private void esconderItemNav(int id) {
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(false);
    }

    /** Esta função seta para visible um item da nav
     *
     * @param id item a ser mostrado;
     */
    private void mostrarItemNav(int id) {
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(true);
    }

    /** Esta função atualiza os items do nav conforme os privilégios do usuário*/
    private void refreshNavItems() {
        mostrarItemNav(R.id.nav_minhasReservas);
    }
}
