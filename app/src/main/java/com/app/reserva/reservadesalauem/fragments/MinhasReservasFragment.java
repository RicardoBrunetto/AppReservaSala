package com.app.reserva.reservadesalauem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.MinhasReservasArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MinhasReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinhasReservasFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Login login;

    private ListView lstMinhasReservasReservas;
    private ArrayAdapter<Reserva> adpReservas;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MinhasReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MinhasReservasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MinhasReservasFragment newInstance(String param1, String param2) {
        MinhasReservasFragment fragment = new MinhasReservasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = new Login();

        // receber parametros (nome do parametro, dado)
        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minhasreservas, container, false);
        lstMinhasReservasReservas = (ListView) view.findViewById(R.id.lstMinhasReservas);

        //btnMinhasReservasVoltar = (Button) view.findViewById(R.id.btnMinhasReservasVoltar);
        //btnMinhasReservasVoltar.setOnClickListener(this);

        // listar todas as reservas do usuário
        carregarMinhasReservas();
        return view;
    }

    private void carregarMinhasReservas(){
        // array adapter personalizado
        adpReservas = new MinhasReservasArrayAdapter(getActivity(),R.layout.item_minha_reserva, login);
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
    }
}
