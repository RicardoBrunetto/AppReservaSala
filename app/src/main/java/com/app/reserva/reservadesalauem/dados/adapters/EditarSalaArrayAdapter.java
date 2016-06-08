package com.app.reserva.reservadesalauem.dados.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.AcessoAppUemWS;
import com.app.reserva.reservadesalauem.CadastrarSalaActivity;
import com.app.reserva.reservadesalauem.ListaAlterarSalaActivity;
import com.app.reserva.reservadesalauem.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mamoru on 19/02/2016.
 */
public class EditarSalaArrayAdapter extends ArrayAdapter<Sala> implements View.OnClickListener, Runnable {

    private int resource;
    private LayoutInflater inflater;
    private Context context;

    // lista de salas passadas por parametro no construtor
    private ArrayList<Sala> lstSala;
    // o login, para ser usado na hora da solicitação com o servidor
    private Login login;
    // a sala que será alterada
    private Sala sala1;
    // resposta do servidor
    private int resposta;

    public EditarSalaArrayAdapter(Context context, int resource, ArrayList<Sala> lstS, Login log1) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
        // o que muda é aqui, onde recebe a lista de sals e o login como parametro
        lstSala = lstS;
        login = log1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtItemEditarSalaSeparador = (TextView) view.findViewById(R.id.txtItemEditarSalaSeparador);
            viewHolder.txtItemEditarSalaNumero = (TextView) view.findViewById(R.id.txtItemEditarSalaNumero);
            viewHolder.txtItemEditarSalaClassificacao = (TextView) view.findViewById(R.id.txtItemEditarSalaClassificacao);
            viewHolder.btnItemEditarSalaEditar = (Button) view.findViewById(R.id.btnItemEditarSalaEditar);
            viewHolder.btnItemEditarSalaRemover = (Button) view.findViewById(R.id.btnItemEditarSalaRemover);

            view.setTag(viewHolder);

            convertView = view;
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        // pegar item da posição selecionada
        final Sala sala = getItem(position);

        // tag é mais para inserir texto em cada item
        // no caso, estou usando tag para guardar a posição do item na lista
        String tag = "";
        tag = ""+position;
        
        viewHolder.btnItemEditarSalaRemover.setTag(tag);
        viewHolder.btnItemEditarSalaEditar.setTag(tag);

        // setar o separador para ocupar toda a tela (horizontal)
        viewHolder.txtItemEditarSalaSeparador.setWidth(R.dimen.activity_horizontal_margin);
        // setar os dados
        viewHolder.txtItemEditarSalaNumero.setText(context.getString(R.string.sala) + sala.getNumero());
        viewHolder.txtItemEditarSalaClassificacao.setText(getClassificacao(sala.getClassificacao()));

        // criar evento de clique para o botão remover uma sala de uma linha da lista
        viewHolder.btnItemEditarSalaRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("Remover : "+lstUsuario.get(Integer.parseInt(""+v.getTag())).getNome());
                sala1 = new Sala();
                // pega sala da lista usando o tag para ver sua posição
                sala1 = lstSala.get(Integer.parseInt("" + v.getTag()));
                try {
                    // cria thread para a solicitação de remoção
                    Thread t = new Thread(EditarSalaArrayAdapter.this);
                    t.start();

                    try {
                        t.join();
                        if (resposta == 1) {
                            MessageBox.show(context, "", context.getString(R.string.salaRemovidaSucesso));

                            // aqui converte o context para a classe que tem esse array adapter e chama a
                            // função que atualiza a lista
                            ((ListaAlterarSalaActivity)context).listarSala();
                            return;
                        }
                        if (resposta == -1) {
                            MessageBox.show(context, context.getString(R.string.erro), context.getString(R.string.notAllowed));
                            return;
                        }
                        if (resposta == 0) {
                            MessageBox.show(context, context.getString(R.string.erro), context.getString(R.string.taskBDFail));
                            return;
                        }
                    } catch (Exception en) {
                    }
                } catch (Exception ex) {
                }

            }
        });

        // evento de click do botão alterar uma sala
        viewHolder.btnItemEditarSalaEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("Alterar : " + lstUsuario.get(Integer.parseInt("" + v.getTag())).getNome());

                // passagem de parametro para a classe cadastrar sala (usado também para alterar)
                Intent it = new Intent(context, CadastrarSalaActivity.class);
                // parametro que diz que é de alterar, não cadastrar
                it.putExtra(MenuPrincipalActivity.PREENCHERSOLICITACAO,1);
                it.putExtra(MenuPrincipalActivity.LOGIN, (Serializable) login);
                // pega sala que está na mesma linha do botão selecionado
                it.putExtra(MenuPrincipalActivity.SALA, (Serializable) lstSala.get(Integer.parseInt("" + v.getTag())));
                // iniciar a activity como se a classe que tem o array adapter tivesse chamado, aí usa for result, que espera um
                // resultado da classe criada. Se fizer isso, tem que colocar onActivityResult na classe que tem o ArrayAdapter.
                ((Activity) context).startActivityForResult(it, 3);
            }
        });

        return view;
    }

    // pega classificacao da sala e converte para texto
    public String getClassificacao(int id) {
        if (id == 1) {
            return context.getString(R.string.laboratorio);
        }
        return context.getString(R.string.projecao);
    }


    // função não usada
    @Override
    public void onClick(View v) {
        MessageBox.show(context, "", "" + v.getTag());
        //Toast.makeText(context, (String) v.getTag(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void run() {
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resposta = ws.removeSala(login, sala1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    static class ViewHolder {
        // nome das variaveis
        TextView txtItemEditarSalaSeparador;
        TextView txtItemEditarSalaNumero;
        TextView txtItemEditarSalaClassificacao;
        Button btnItemEditarSalaEditar;
        Button btnItemEditarSalaRemover;

    }
}