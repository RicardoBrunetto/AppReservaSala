package com.app.reserva.reservadesalauem.dados.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Usuario;

import java.util.ArrayList;


/**
 * Created by Mamoru on 29/01/2016.
 */
public class UsuarioArrayAdapter extends ArrayAdapter<Usuario> implements View.OnClickListener{

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    //private ArrayList<String> lstInfo;

    public UsuarioArrayAdapter(Context context, int resource){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        //lstInfo = new ArrayList<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource,parent,false);

            viewHolder.txtItemUsuarioSeparador = (TextView) view.findViewById(R.id.txtItemUsuarioSeparador);
            viewHolder.txtItemUsuarioNome = (TextView) view.findViewById(R.id.txtItemUsuarioNome);
            viewHolder.txtItemUsuarioCargo = (TextView) view.findViewById(R.id.txtItemUsuarioCargo);
            viewHolder.btnItemUsuarioInfo = (Button) view.findViewById(R.id.btnItemUsuarioInfo);

            view.setTag(viewHolder);

            convertView = view;
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        Usuario usuario = getItem(position);

        // verificar permissão para ver que usuario é
        if (usuario.getPermissao()==1){
            viewHolder.txtItemUsuarioCargo.setText(context.getString(R.string.docente));
        }
        if (usuario.getPermissao()==2){
            viewHolder.txtItemUsuarioCargo.setText(context.getString(R.string.secretario));
        }
        if (usuario.getPermissao()==3){
            viewHolder.txtItemUsuarioCargo.setText(context.getString(R.string.admDepto));
        }
        if (usuario.getPermissao()==4){
            viewHolder.txtItemUsuarioCargo.setText(context.getString(R.string.admSistema));
        }
        // fazer ocupar toda a linha horizontalmente
        viewHolder.txtItemUsuarioSeparador.setWidth(R.dimen.activity_horizontal_margin);
        viewHolder.btnItemUsuarioInfo = (Button) view.findViewById(R.id.btnItemUsuarioInfo);
        viewHolder.btnItemUsuarioInfo.setOnClickListener(this);
        // guardar os dados que quer mostrar na tag
        String tag = "";
        tag += context.getString(R.string.nomeStr)+usuario.getNome();
        tag += "\n";
        tag += context.getString(R.string.mailStr)+usuario.getEmail();
        tag += "\n";
        tag += context.getString(R.string.telefoneStr)+usuario.getTelefone();
        // setar tag na linha
        viewHolder.btnItemUsuarioInfo.setTag(tag);
        //viewHolder.txtCor = contato
        viewHolder.txtItemUsuarioNome.setText(usuario.getNome());
        return view;
    }

    // mostrar informação da tag
    @Override
    public void onClick(View v) {
        MessageBox.show(context,"",""+v.getTag());
        //Toast.makeText(context, (String) v.getTag(), Toast.LENGTH_SHORT).show();
    }


    static class ViewHolder{
        TextView txtItemUsuarioSeparador;
        TextView txtItemUsuarioNome;
        TextView txtItemUsuarioCargo;
        Button btnItemUsuarioInfo;

    }
}
