package com.app.reserva.reservadesalauem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.MinhasReservasArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MinhasReservasActivity extends AppCompatActivity implements View.OnClickListener{

    private Login login;

    private Button btnMinhasReservasVoltar;
    private ListView lstMinhasReservasReservas;

    private ArrayAdapter<Reserva> adpReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        login = new Login();

        // receber parametros (nome do aprametro, dado)
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

        lstMinhasReservasReservas = (ListView) findViewById(R.id.lstMinhasReservasReservas);

        btnMinhasReservasVoltar = (Button) findViewById(R.id.btnMinhasReservasVoltar);
        btnMinhasReservasVoltar.setOnClickListener(this);

        // listar todas as reservas do usuário
        carregarMinhasReservas();
    }

    private void carregarMinhasReservas(){
        // array adapter personalizado
        adpReservas = new MinhasReservasArrayAdapter(this,R.layout.item_minhas_reservas);
        // todas as reservas do usuario
        ArrayList<Reserva> lstRes = new ArrayList<>();
        try{
            // pegar o id do usuario logado, pois é isso que é cadastrado nas reservas
            getIdUsuario();
            // pegar lista com todas as reservas
            CarregarDadoUtils cd = new CarregarDadoUtils();
            ArrayList<Reserva> lstR = cd.carregarReserva();

            // usado apra converter string para data
            DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
            df.setLenient (false);

            // pegar data atual do servidor
            String dataAtual = cd.carregarDataAtual();
            // converter para Data
            java.util.Date atual = df.parse(dataAtual);

            for(Reserva r:lstR){
                // para cada reserva existente, converter a data da reserva de String para java.util.Date

                java.util.Date date = null;
                date = df.parse(r.getDatareserva());
                //date = df.parse("05/04/2016");

                System.out.println(date.getTime()+":"+atual.getTime());
                // transformar as duas datas em valores positivos, pois por algum motivo, o atual aparece como negativo
                if(Math.abs(atual.getTime()) <= Math.abs(date.getTime())){
                    //System.out.println("atual é menor ou igual :"+(date.getTime()-atual.getTime()));
                    // se data atual for menor ou igual que data da reserva, no caso, se a reserva for para o dia atual ou
                    // posterior, verifica quem reservou. Se foi o usuario, adiciona a reserva na lista
                    if(r.getIdusuario() == login.getId()) {
                        lstRes.add(r);
                    }
                }
            }
            // ordenar as reservas por periodo
            Collections.sort(lstRes, Reserva.ReservaPeriodoComparator);
            // ordenar as reservas por data
            Collections.sort(lstRes, Reserva.ReservaDataComparator);
            // adicionar todas as reservas válidas no array adapter
            for(Reserva r:lstRes){
                adpReservas.add(r);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        lstMinhasReservasReservas.setAdapter(adpReservas);
    }

    // pesquisar o id do usuario pelo email
    private int getIdUsuario(){
        try{
            // carregar todos os usuarios
            CarregarDadoUtils cd = new CarregarDadoUtils();
            ArrayList<Usuario> lstU = cd.carregarUsuario();
            // pesquisar um com email igual (não é permitido email repetido no sitema)
            for(Usuario u:lstU){
                if(u.getEmail().equals(login.getEmail())){
                    login.setId(u.getId());
                    return login.getId();
                }
            }

        }
        catch(Exception ex){
        }
        return login.getId();
    }

    @Override
    public void onClick(View v) {
        if(v == btnMinhasReservasVoltar){
            finish();
        }
    }
}
