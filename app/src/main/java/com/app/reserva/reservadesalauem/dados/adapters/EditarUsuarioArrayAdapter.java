package com.app.reserva.reservadesalauem.dados.adapters;

import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mamoru on 17/02/2016.
 */
public class EditarUsuarioArrayAdapter extends ArrayAdapter<Usuario> implements View.OnClickListener, Runnable{

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    // lista de todos os usuarios que podem ser alterados
    private ArrayList<Usuario> lstUsuario;
    // login
    private Login login;
    // usuario a ser alterado
    private Usuario user;
    // resposta dos servidor
    private int resposta;
    
    public EditarUsuarioArrayAdapter(Context context, int resource,ArrayList<Usuario> lstU,Login log1){
        super(context,resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        // receber como aprametro a lista de usuarios e login.
        lstUsuario = new ArrayList<>();
        lstUsuario = lstU;
        login = new Login();
        login = log1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource,parent,false);

            viewHolder.txtItemEditarUsuarioSeparador = (TextView) view.findViewById(R.id.txtItemEditarUsuarioSeparador);
            viewHolder.txtItemEditarUsuarioNome = (TextView) view.findViewById(R.id.txtItemEditarUsuarioNome);
            viewHolder.txtItemEditarUsuarioEspaco = (TextView) view.findViewById(R.id.txtItemEditarUsuarioEspaco);
            viewHolder.txtItemEditarUsuarioCargo = (TextView) view.findViewById(R.id.txtItemEditarUsuarioCargo);
            viewHolder.btnItemEditarUsuarioRemover = (Button) view.findViewById(R.id.btnItemEditarUsuarioRemover);
            viewHolder.btnItemEditarUsuarioEditar = (Button) view.findViewById(R.id.btnItemEditarUsuarioEditar);

            view.setTag(viewHolder);

            convertView = view;
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        // pegar item da posicao
        final Usuario usuario = getItem(position);

        // setar o texto com cargo a partir da permissão
        if (usuario.getPermissao()==1){
            viewHolder.txtItemEditarUsuarioCargo.setText(context.getString(R.string.docente));
        }
        if (usuario.getPermissao()==2){
            viewHolder.txtItemEditarUsuarioCargo.setText(context.getString(R.string.secretario));
        }
        if (usuario.getPermissao()==3){
            viewHolder.txtItemEditarUsuarioCargo.setText(context.getString(R.string.admDepto));
        }
        if (usuario.getPermissao()==4){
            viewHolder.txtItemEditarUsuarioSeparador.setText(context.getString(R.string.admSistema));
        }
        // setar o separador para preencher toda a tela
        viewHolder.txtItemEditarUsuarioNome.setWidth(R.dimen.activity_horizontal_margin);
        // guardar a posicao do item na tag
        String tag = "";
        tag = ""+position;
        // setar tag
        viewHolder.btnItemEditarUsuarioRemover.setTag(tag);
        viewHolder.btnItemEditarUsuarioEditar.setTag(tag);

        // ativar o botão remoção. se o usuario for ele mesmo, desativar, não pode auto remover a sí mesmo
        viewHolder.btnItemEditarUsuarioRemover.setEnabled(true);
        if(login.getEmail().equals(usuario.getEmail())){
            viewHolder.btnItemEditarUsuarioRemover.setEnabled(false);
        }

    // evento para quando clica no botão remover
        viewHolder.btnItemEditarUsuarioRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("Remover : "+lstUsuario.get(Integer.parseInt(""+v.getTag())).getNome());
                // pega usuario da lista usando a posição guardada no tag
                user = new Usuario();
                user = lstUsuario.get(Integer.parseInt("" + v.getTag()));
                // criar thread para remoção
                try{
                    Thread t = new Thread(EditarUsuarioArrayAdapter.this);
                    t.start();

                    try {
                        t.join();
                        if(resposta == 1){
                            MessageBox.show(context,"",context.getString(R.string.userRemovidoSucesso));
                            //TODO: se foi removido com sucesso, chamar função da classe que tem o array adapter para atualizar a lista
                          //  ((ListaAlterarUsuarioActivity)context).listarUsuario();
                            return;
                        }
                        if(resposta== -1){
                            MessageBox.show(context,context.getString(R.string.erro),context.getString(R.string.notAllowed));
                            return;
                        }
                        if(resposta == 0){
                            MessageBox.show(context, context.getString(R.string.erro),context.getString(R.string.taskBDFail));
                            return;
                        }
                    }
                    catch (Exception en){
                    }
                }
                catch(Exception ex){
                }

            }
        });

        // evento para botão alterar
        viewHolder.btnItemEditarUsuarioEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("Alterar : " + lstUsuario.get(Integer.parseInt("" + v.getTag())).getNome());
                //TODO: Intent it = new Intent(context, AlterarUsuarioActivity.class);
          //      it.putExtra(MenuPrincipalActivity.LOGIN, (Serializable) login);
                // passar o usuario usando a tag para saber posicao na lista (linha que tem esse botão)
          //      it.putExtra(MenuPrincipalActivity.USUARIO, (Serializable) lstUsuario.get(Integer.parseInt("" + v.getTag())));
                // chamar a classe que irá alterar, solicitando uma resposta. Quando usa esse context, é como se quem estiver
                // chamando a classe não é o adapter, mas sim a classe que tem o adapter, portanto a resposta irá para a classe
                // que tem o adapter
          //      ((Activity)context).startActivityForResult(it, 0);
            }
        });



        viewHolder.txtItemEditarUsuarioNome.setText(usuario.getNome());
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void run() {
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resposta = ws.removeUsuario(login,user);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // variaveis que tem na interface do adapter
    static class ViewHolder{
        TextView txtItemEditarUsuarioSeparador;
        TextView txtItemEditarUsuarioNome;
        TextView txtItemEditarUsuarioCargo;
        TextView txtItemEditarUsuarioEspaco;
        Button btnItemEditarUsuarioEditar;
        Button btnItemEditarUsuarioRemover;

    }
}
