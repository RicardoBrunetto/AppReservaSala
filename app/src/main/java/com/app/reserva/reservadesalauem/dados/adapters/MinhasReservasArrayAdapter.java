package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.ReservaLogin;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.fragments.SolicitarReservaFragment;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mamoru on 05/02/2016.
 */
public class MinhasReservasArrayAdapter extends ArrayAdapter<Reserva>{

    private Login login;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public MinhasReservasArrayAdapter(Context context, int resource, Login login) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        this.login   = login;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        final int pos = position;
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtItemMinhasReservasData = (TextView) view.findViewById(R.id.txtItemMinhasReservasData);
            viewHolder.txtItemMinhasReservasSala = (TextView) view.findViewById(R.id.txtItemSalaNumero);
            viewHolder.txtItemMinhasReservasTipo = (TextView) view.findViewById(R.id.txtItemMinhasReservasTipo);
            viewHolder.txtItemMinhasReservasStatus = (TextView) view.findViewById(R.id.txtItemMinhasReservasStatus);
            viewHolder.txtItemSalaNumero = (TextView) view.findViewById(R.id.txtItemSalaNumero);
            viewHolder.btnCancelar = (Button) view.findViewById(R.id.btn_cancelarReserva);
            viewHolder.img_sts = (ImageView) view.findViewById(R.id.img_sts);
            view.setTag(viewHolder);

            convertView = view;
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        viewHolder.btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Reserva reserva = getItem(pos);
                new CancelarReserva(context).execute(new ReservaLogin(reserva, login));
            }
        });


        // pegar item de cada linha
        Reserva reserva = getItem(position);
        // setar os textos
        viewHolder.txtItemMinhasReservasData.setText(getSemana(reserva.getDatareserva()) + ", " + getShortData(reserva.getDatareserva()) + " às " + getPeriodo(reserva.getPeriodo()).replace("(", "").replace(")", ""));
        // escrever numero da sala só se tiver numero
        System.out.println(reserva.getIdsala());
        if(reserva.getIdsala() != -1){
            viewHolder.txtItemMinhasReservasSala.setText(reserva.getIdsala());
            viewHolder.txtItemSalaNumero.setText(reserva.getIdsala());
        }
        viewHolder.txtItemMinhasReservasTipo.setText(getTipoSala(reserva.getTiposala()));
        int status = reserva.getStatus();
        viewHolder.txtItemMinhasReservasStatus.setText(getStatus(status));
        if(status < 0){
            viewHolder.img_sts.setBackgroundColor(view.getResources().getColor(R.color.vermelho));
        }else if(status == 1){
            viewHolder.img_sts.setBackgroundColor(view.getResources().getColor(R.color.amarelo));
        }else if(status == 2){
            viewHolder.img_sts.setBackgroundColor(view.getResources().getColor(R.color.verde));
        }else{
            viewHolder.img_sts.setBackgroundColor(view.getResources().getColor(R.color.colorGray));
        }

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
            Date date = null;
            Log.d("LogReserva", data);
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

    //retorna a data numa versão mais curta
    private String getShortData(String data){
        try {
            DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
            Date date = null;
            Log.d("LogReserva", "Short:" + data);
            date = df.parse(data);
            df = new SimpleDateFormat("dd/MM");
            return df.format(date);
        }catch (ParseException e) {
            e.printStackTrace();
            return data;
        }
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

    @Override
    public void add(Reserva object) {
        super.add(object);
    }

    // variaveis existentes na interface, para linkar depois
    static class ViewHolder {
        TextView txtItemMinhasReservasData;
        TextView txtItemMinhasReservasSala;
        TextView txtItemMinhasReservasTipo;
        TextView txtItemMinhasReservasStatus, txtItemSalaNumero;
        Button btnCancelar;
        ImageView img_sts;
    }

    private class CancelarReserva extends AsyncTask<ReservaLogin, Integer, Integer> {

        Context ctx;

        public CancelarReserva(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(Integer res) {
            if (res == -1) {
                MessageBox.show(getContext(), ctx.getString(R.string.erro), ctx.getString(R.string.notAllowed));
                return;
            }
            // erro na solicitação
            if (res == -2) {
                MessageBox.show(getContext(), ctx.getString(R.string.erro), ctx.getString(R.string.requestFailed));
                return;
            }
            // deu certo
            if (res == 1) {
                MessageBox.show(getContext(), "", ctx.getString(R.string.sucessRemoveReserva));
            }
        }

        @Override
        protected Integer doInBackground(ReservaLogin[] params) {
            Reserva reserva = params[0].getRes();
            Login login = params[0].getLogin();
            int resultado = 0;

            try {
                AcessoAppUemWS ws = new AcessoAppUemWS();
                Log.d("LogReserva", "O removerReserva será chamado");
                resultado = ws.removerReserva(login, reserva);
            } catch (Exception ex) {
                Log.d("LogReserva", "Erro aqui");
                ex.printStackTrace();
            }

            return resultado;
        }
    }
}