package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Sala;

import java.util.ArrayList;

/**
 * Created by Mamoru on 19/02/2016.
 */
public class SalaArrayAdapter extends ArrayAdapter<Sala> implements View.OnClickListener {

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    ArrayList<String> lstInfo;

    public SalaArrayAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        lstInfo = new ArrayList<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtItemSalaNumero = (TextView) view.findViewById(R.id.txtItemSalaNumero);
            viewHolder.txtItemSalaClassificacao = (TextView) view.findViewById(R.id.txtItemSalaClassificacao);
            viewHolder.btnItemSalaInfo = (Button) view.findViewById(R.id.btnItemSalaInfo);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        Sala sala = getItem(position);

        // ecento de click no botão info
        viewHolder.btnItemSalaInfo.setOnClickListener(this);
        // armazenar os dados que quer mostrar no tag de cada linha
        String tag = "";
        tag += context.getString(R.string.sala) + sala.getNumero();
        tag += "\n";
        tag += context.getString(R.string.descricao) + sala.getDescricao();
        viewHolder.btnItemSalaInfo.setTag(tag);
        viewHolder.txtItemSalaNumero.setText(context.getString(R.string.sala) + sala.getNumero());
        viewHolder.txtItemSalaClassificacao.setText(getClassificacao(sala.getClassificacao()));

        return view;
    }

    // transformar inteiro para string
    public String getClassificacao(int id){
        if(id == 1){
            return context.getString(R.string.laboratorio);
        }
        return context.getString(R.string.projecao);
    }

    @Override
    public void onClick(View v) {
        // mostrar mensagem guardada no tag da linha selecionada
        MessageBox.show(context, "", "" + v.getTag());
        //Toast.makeText(context, (String) v.getTag(), Toast.LENGTH_SHORT).show();
    }

    // variaveis para linkar com a interface
    static class ViewHolder {
        TextView txtItemSalaNumero;
        TextView txtItemSalaClassificacao;
        Button btnItemSalaInfo;

    }
}