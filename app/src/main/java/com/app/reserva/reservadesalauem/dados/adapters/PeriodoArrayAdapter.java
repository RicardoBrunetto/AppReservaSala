package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.dados.Periodo;

import java.util.List;

/**
 * Created by Mamoru on 28/01/2016.
 */
public class PeriodoArrayAdapter extends ArrayAdapter<String>{

    // lista de periodo
    private List<Periodo> lstPeriodo;

    private List<String> objects;
    private Context context;

    public PeriodoArrayAdapter(Context context, int resourceId,
                              List<String> objects, List<Periodo> lstP) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.lstPeriodo = lstP;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.item_periodo, parent, false);
        TextView label=(TextView)row.findViewById(R.id.txtItemPeriodo);
        label.setText(objects.get(position));

        // mudar cor do texto para preto (pasta R.color)
        label.setTextColor(context.getResources().getColor(R.color.colorBlack));
        // fazer ocupar o maximo de espaço possível (tela horizontal inteira)
        label.setWidth(R.dimen.activity_horizontal_margin);
        //label.setSingleLine();
        // para cada periodo, verificar o staus e colorir de acordo
        if (lstPeriodo.get(position).getStatus() == 0) { // sem selecao
            //label.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        if (lstPeriodo.get(position).getStatus() == 1) { // usado, por isso, inválido
            label.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
        if (lstPeriodo.get(position).getStatus() == 2) { // válido
            label.setBackgroundColor(context.getResources().getColor(R.color.colorLimeGreen));
        }
        if (lstPeriodo.get(position).getStatus() == 3) { // pendente
            label.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
        }
        return row;
    }

}

