package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.SalasDisponiveis;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.fragments.SolicitarReservaFragment;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mamoru on 05/02/2016.
 */
public class SalasDisponiveisArrayAdapter extends ArrayAdapter<SalasDisponiveis> implements View.OnClickListener{

    private int resource;
    private LayoutInflater inflater;
    private Context context;
    // login do usuario
    private Login login;
    private ArrayList<SalasDisponiveis> lstSalas;

    public SalasDisponiveisArrayAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        lstSalas = new ArrayList<>();
    }

    public SalasDisponiveisArrayAdapter(Context context, int resource, Login user){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        login =new Login();
        login = user;
        lstSalas = new ArrayList<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource,parent,false);

            viewHolder.txtItemSalaDisponivelPeriodo = (TextView) view.findViewById(R.id.txtItemSalaDisponivelPeriodo);
            viewHolder.txtItemSalaDisponivelSeparador = (TextView) view.findViewById(R.id.txtItemSalaDisponivelSeparador);
            viewHolder.txtItemSalaDisponivelQuantidade = (TextView) view.findViewById(R.id.txtItemSalaDisponivelQuantidade);
            viewHolder.btnItemSalaDisponivelReservar = (Button) view.findViewById(R.id.btnItemSalaDisponivelReservar);
            viewHolder.btnItemSalaDisponivelReservar.setOnClickListener(this);
            view.setTag(viewHolder);

            convertView = view;
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }


        SalasDisponiveis salas = getItem(position);
        // adicionei todas as salas aqui, podia ter passado a lista de salas como aprametro no
        // construtor mas quando fiz essa parte, não sabia como
        lstSalas.add(salas);

        // verificar privilégio, para ocultar botão de reservar
        if(salas.getLogin().getPrivilegio() < 1){
            viewHolder.btnItemSalaDisponivelReservar.setVisibility(View.INVISIBLE);
        }
        // mostrar dados
        viewHolder.txtItemSalaDisponivelPeriodo.setText(getPeriodo(salas.getPeriodo()));
        viewHolder.txtItemSalaDisponivelQuantidade.setText(""+salas.getSalaslivres());
        viewHolder.btnItemSalaDisponivelReservar.setEnabled(true);
        if(salas.getSalaslivres()==0){
           viewHolder.btnItemSalaDisponivelReservar.setEnabled(false);
        }
        // guardar posicao na tag
        String tag = "";
        tag = ""+position;
        viewHolder.btnItemSalaDisponivelReservar.setTag(tag);
        // fazer adapter ocupar toda a tela horizontalmente
        viewHolder.txtItemSalaDisponivelSeparador.setWidth(R.dimen.activity_horizontal_margin);
        return view;
    }

    // converter int para String
    private  String getPeriodo(int id){
        if(id == 1){
            return context.getString(R.string.primeiroPeriodo);
        }
        if(id == 2) {
            return context.getString(R.string.segundoPeriodo);
        }
        if(id == 3){
            return context.getString(R.string.terceiroPeriodo);
        }
        if(id == 4){
            return context.getString(R.string.quartoPeriodo);
        }
        if(id == 5){
            return context.getString(R.string.quintoPeriodo);
        }
        return context.getString(R.string.sextoPeriodo);
    }


    // pegar nome do usuario a partir do id
    private String getNomeUsuario(int id){
        ArrayList<Usuario> lstU = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        for(Usuario u:lstU){
            if (u.getId()== id){
                return u.getNome();
            }
        }
        return "";
    }


    // quando clica em reservar, tem que ir na tela de reserva, e preencher os dados
    @Override
    public void onClick(View v) {


        for(SalasDisponiveis s1:lstSalas){
            System.out.println(s1.getPeriodo()+"-"+s1.getSalaslivres());
        }
        System.out.println("Periodo atual : "+lstSalas.get(Integer.parseInt(""+v.getTag())).getPeriodo());

        Intent it = new Intent(context, SolicitarReservaFragment.class);
        SolicitarReservaFragment solicitarReservaFragment = new SolicitarReservaFragment();
        Bundle args = new Bundle();

        args.putString(MenuPrincipalActivity.EMAIL,login.getEmail());
        args.putString(MenuPrincipalActivity.SENHA,login.getSenha());
        args.putInt(MenuPrincipalActivity.PRIVILEGIO,login.getPrivilegio());
        solicitarReservaFragment.setArguments(args);
        context.startActivity(it);


        //TODO: Intent it = new Intent(context, SolicitarReservaActivity.class);
        // mandar preencher os dados
        args.putInt(MenuPrincipalActivity.PREENCHERSOLICITACAO,1);
        // enviar login
        args.putSerializable(MenuPrincipalActivity.LOGIN, (Serializable) lstSalas.get(Integer.parseInt(""+v.getTag())).getLogin());
        // enviar daos de preenchimento
        args.putSerializable(MenuPrincipalActivity.SALASDISPONIVEIS, (Serializable) lstSalas.get(Integer.parseInt("" + v.getTag())));
        // iniciar activity
        context.startActivity(it);
    }

    // variaveis para linkar com as da interface
    static class ViewHolder{
        TextView txtItemSalaDisponivelSeparador;
        TextView txtItemSalaDisponivelPeriodo;
        TextView txtItemSalaDisponivelQuantidade;
        Button btnItemSalaDisponivelReservar;
    }

}