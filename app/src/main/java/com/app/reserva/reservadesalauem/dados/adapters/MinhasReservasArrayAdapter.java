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

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mamoru on 05/02/2016.
 */
public class MinhasReservasArrayAdapter extends ArrayAdapter<Reserva> {

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public MinhasReservasArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtItemMinhasReservasData = (TextView) view.findViewById(R.id.txtItemMinhasReservasData);
            viewHolder.txtItemMinhasReservasPeriodo = (TextView) view.findViewById(R.id.txtItemMinhasReservasPeriodo);
            viewHolder.txtItemMinhasReservasSemana = (TextView) view.findViewById(R.id.txtItemMinhasReservasSemana);
            viewHolder.txtItemMinhasReservasSeparador = (TextView) view.findViewById(R.id.txtItemMinhasReservasSeparador);
            viewHolder.txtItemMinhasReservasSala = (TextView) view.findViewById(R.id.txtItemMinhasReservasSala);
            viewHolder.txtItemMinhasReservasTipo = (TextView) view.findViewById(R.id.txtItemMinhasReservasTipo);
            viewHolder.txtItemMinhasReservasStatus = (TextView) view.findViewById(R.id.txtItemMinhasReservasStatus);
            view.setTag(viewHolder);

            convertView = view;
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        // pegar item de cada linha
        Reserva reserva = getItem(position);
        // setar os textos
        viewHolder.txtItemMinhasReservasData.setText(reserva.getDatareserva());
        viewHolder.txtItemMinhasReservasPeriodo.setText(getPeriodo(reserva.getPeriodo()));
        viewHolder.txtItemMinhasReservasSemana.setText(getSemana(reserva.getDatareserva()));
        // escrever numero da sala só se tiver numero
        if(reserva.getIdsala() != -1){
            viewHolder.txtItemMinhasReservasSala.setText(context.getString(R.string.sala) +reserva.getIdsala());
        }
        else{
            viewHolder.txtItemMinhasReservasSala.setText(context.getString(R.string.sala));
        }
        viewHolder.txtItemMinhasReservasTipo.setText(getTipoSala(reserva.getTiposala()));
        viewHolder.txtItemMinhasReservasStatus.setText(getStatus(reserva.getStatus()));
        // separador usado para o adapter ocupar toda a linha
        viewHolder.txtItemMinhasReservasSeparador.setWidth(R.dimen.activity_horizontal_margin);

        return view;
    }

    // transformar o periodo de integer para string
    private String getPeriodo(int id) {
        if (id == 1) {
            return context.getString(R.string.primeiroPeriodo);
        }
        if (id == 2) {
            return context.getString(R.string.segundoPeriodo);
        }
        if (id == 3) {
            return context.getString(R.string.terceiroPeriodo);
        }
        if (id == 4) {
            return context.getString(R.string.quartoPeriodo);
        }
        if (id == 5) {
            return context.getString(R.string.quintoPeriodo);
        }
        return context.getString(R.string.sextoPeriodo);
    }

    // transformar o status de integer para string
    private String getStatus(int id) {
        if (id == -2) {
            return context.getString(R.string.rejeitado);
        }
        if (id == -1) {
            return context.getString(R.string.cancelado);
        }
        if (id == 0) {
            return context.getString(R.string.finalizado);
        }
        if (id == 1) {
            return context.getString(R.string.pendente);
        }
        if (id == 2) {
            return context.getString(R.string.confirmado);
        }
        return context.getString(R.string.cancelado);
    }

    // verificar o dia da seman a partir da data da reserva
    private String getSemana(String data){
        try {
            // usado para converter String para date, definindo em que formato ele está
            DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
            df.setLenient (false);
            java.util.Date date = null;
            // conversão string -> java.util.date
            date = df.parse(data);
            // pegar calendário
            Calendar cal = Calendar.getInstance();
            // setar a data
            cal.setTime(date);
            //pegar o dia da semana
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            // para cada dia, converter em string
            switch (dia){
                case 1:
                    return context.getString(R.string.domingo);
                case 2:
                    return context.getString(R.string.segunda_feira);
                case 3:
                    return context.getString(R.string.terca_feira);
                case 4:
                    return context.getString(R.string.quarta_feira);
                case 5:
                    return context.getString(R.string.quinta_feira);
                case 6:
                    return context.getString(R.string.sexta_feira);
                case 7:
                    return context.getString(R.string.sabado);
                default:
                    return "";
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    // converter int para string do tipo de sala
    private String getTipoSala(int id){
        if(id == 1){
            return context.getString(R.string.laboratorio);
        }
        return context.getString(R.string.projecao);
    }

    // pegar nome do usuario a partir do id
    private String getNomeUsuario(int id) {
        ArrayList<Usuario> lstU = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        for (Usuario u : lstU) {
            if (u.getId() == id) {
                return u.getNome();
            }
        }
        return "";
    }

    // variaveis existentes na interface, para linkar depois
    static class ViewHolder {
        TextView txtItemMinhasReservasData;
        TextView txtItemMinhasReservasPeriodo;
        TextView txtItemMinhasReservasSemana;
        TextView txtItemMinhasReservasSala;
        TextView txtItemMinhasReservasTipo;
        TextView txtItemMinhasReservasStatus;
        TextView txtItemMinhasReservasSeparador;
    }
}