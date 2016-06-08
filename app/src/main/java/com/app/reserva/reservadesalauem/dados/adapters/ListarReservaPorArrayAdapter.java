package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Mamoru on 14/12/2015.
 */
public class ListarReservaPorArrayAdapter extends ArrayAdapter<Reserva>{

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ListarReservaPorArrayAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource,parent,false);

            viewHolder.txtItemListaPorPeriodo = (TextView) view.findViewById(R.id.txtItemListarPorPeriodo);
            viewHolder.txtItemListaPorDocente = (TextView) view.findViewById(R.id.txtItemListaPorDocente);
            viewHolder.txtItemListaPorStatus = (TextView) view.findViewById(R.id.txtItemListaPorStatus);
            viewHolder.txtItemListaPorSeparator1 = (TextView) view.findViewById(R.id.txtItemListarPorSeparator1);
            view.setTag(viewHolder);

            convertView = view;
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        Reserva reserva = getItem(position);
        // pegar item de uma linha e setar os dados
        viewHolder.txtItemListaPorDocente.setText(getNomeUsuario(reserva.getIdusuario()));
        viewHolder.txtItemListaPorPeriodo.setText(getPeriodo(reserva.getPeriodo()));
        viewHolder.txtItemListaPorStatus.setText(getStatus(reserva.getStatus()));
        // separador, para ocupar toda a parte horizontal da tela
        viewHolder.txtItemListaPorSeparator1.setWidth(R.dimen.activity_horizontal_margin);
        return view;
    }

    // converter periodo para texto
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

    // converter status para texto
    private String getStatus(int id){
        if(id == -2){
            return context.getString(R.string.rejeitado);
        }
        if(id == -1){
            return context.getString(R.string.cancelado);
        }
        if(id == 0){
            return context.getString(R.string.finalizado);
        }
        if(id == 1){
            return context.getString(R.string.pendente);
        }
        if(id == 2){
            return context.getString(R.string.confirmado);
        }
        return context.getString(R.string.livreStr);
    }

    // buscar nome do usuario pelo id
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

    // variaveis que tem na interface
    static class ViewHolder{
        TextView txtItemListaPorPeriodo;
        TextView txtItemListaPorDocente;
        TextView txtItemListaPorStatus;
        TextView txtItemListaPorSeparator1;
    }

}
