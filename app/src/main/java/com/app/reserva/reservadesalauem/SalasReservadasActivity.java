package com.app.reserva.reservadesalauem;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.*;
import com.app.reserva.reservadesalauem.dados.adapters.ListarReservaPorArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class SalasReservadasActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spnSalasReservadasDepartamento;
    private EditText edtSalasReservadasData;
    private Button btnSalasReservadasAnterior;
    private TextView txtSalasReservadasSala;
    private Button btnSalasReservadasProximo;
    private ListView lstSalasReservadasReservas;
    private Button btnSalasReservadasVoltar;

    private ArrayAdapter<Departamento> adpDepartamento;
    private ArrayList<Departamento> lstDepartamento;

    private ArrayAdapter<Sala> adpSala;
    private ArrayList<Sala> lstSala;
    private ArrayList<Sala> lstSalaDoDepartamento;
    private int posListaSalaDepartamento;

    private ArrayAdapter<Reserva> adpReserva;
    private ArrayList<Reserva> lstReserva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_reservadas);

        spnSalasReservadasDepartamento = (Spinner) findViewById(R.id.spnSalasReservadasDepartamento);

        spnSalasReservadasDepartamento.requestFocus();

        edtSalasReservadasData = (EditText) findViewById(R.id.edtSalasReservadasData);


        btnSalasReservadasAnterior = (Button) findViewById(R.id.btnSalasReservadasAnterior);
        btnSalasReservadasAnterior.setOnClickListener(this);

        txtSalasReservadasSala = (TextView) findViewById(R.id.txtSalasReservadasSala);

        btnSalasReservadasProximo = (Button) findViewById(R.id.btnSalasReservadasProximo);
        btnSalasReservadasProximo.setOnClickListener(this);

        lstSalasReservadasReservas = (ListView) findViewById(R.id.lstSalasReservadasReservas);

        btnSalasReservadasVoltar = (Button) findViewById(R.id.btnSalasReservadasVoltar);
        btnSalasReservadasVoltar.setOnClickListener(this);

        // criar evento de data com calendário para seleção
        ExibeDataListener listener = new ExibeDataListener();
        // selecionar o edit text para ser alvo do evento, como clhique no edit text
        edtSalasReservadasData.setOnClickListener(listener);
        // deixar o edit text sem poder digitar nada no campo
        edtSalasReservadasData.setKeyListener(null);

        // preenche sppiner com valores padrao
        preencherSpinner();

        CarregarDadoUtils cd = new CarregarDadoUtils();
        // pegar data atual do servidor
        String dataAtual = cd.carregarDataAtual();
        // seta a data atual no campo eit text
        edtSalasReservadasData.setText(dataAtual);
        // lsitar todas as reservas
        listar();
    }

    // não foi selecionado departamento padrão igual ao do usuario. Se for bom, tem que passar como parametro o usuario
    // e fazer igual ao de salas disponiveis
    public void preencherSpinner(){
        // classe com thread que carrega os dados do servidor
        CarregarDadoUtils cd = new CarregarDadoUtils();
        // lista de todos os departamentos
        lstDepartamento = new ArrayList<Departamento>();
        // carregar todos os departamentos
        lstDepartamento = cd.carregarDepartamento();
        // criar array adapter simples
        adpDepartamento = new ArrayAdapter<Departamento>(this,android.R.layout.simple_list_item_1);
        // adicioanr todos os departamentos no array adapter
        for(Departamento dep:lstDepartamento){
            adpDepartamento.add(dep);
        }
        // setar o array adapter no spiner
        spnSalasReservadasDepartamento.setAdapter(adpDepartamento);
        // lsita de todas as salas
        lstSala = new ArrayList<Sala>();
        // lista de todas as salas do departamento
        lstSalaDoDepartamento = new ArrayList<Sala>();
        // carregar todas as salas
        lstSala = cd.carregarSala();
        // array adapter simples para a sala
        adpSala = new ArrayAdapter<Sala>(this,android.R.layout.simple_list_item_1);
        for(Sala sala:lstSala){
            // pesquisar todas as salas do departamento e adicionar na lista e no array adapter
            if (sala.getId_departamento()==lstDepartamento.get(spnSalasReservadasDepartamento.getSelectedItemPosition()).getId()) {
                adpSala.add(sala);
                lstSalaDoDepartamento.add(sala);
            }

        }
        // adicionar a sala de pendencias (reseras pendentes)
        if(lstSalaDoDepartamento.size() > 0){
            Sala s1 = new Sala(-1,00,1,1,getString(R.string.reserva),1);
            lstSalaDoDepartamento.add(s1);
            posListaSalaDepartamento = 0;
            txtSalasReservadasSala.setText(lstSalaDoDepartamento.get(posListaSalaDepartamento).toString());
        }
        else{
            posListaSalaDepartamento = 0;
            txtSalasReservadasSala.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnSalasReservadasVoltar){
            finish(); // fechar tela, nem precisava do return
            return;
        }
        if(edtSalasReservadasData.getText().toString().length()==0){
            MessageBox.show(this, getString(R.string.erro), getString(R.string.dataNaoSelect));
            return;
        }
        if(v == btnSalasReservadasAnterior){
            // verifiar se tem salas no departamento
            if(lstSalaDoDepartamento.size() > 0){
                // mudar sala do departamento
                if(posListaSalaDepartamento != 0){
                    posListaSalaDepartamento -= 1;
                }
                else{
                    posListaSalaDepartamento = lstSalaDoDepartamento.size()-1;
                }
                // atualizar lista
                txtSalasReservadasSala.setText(lstSalaDoDepartamento.get(posListaSalaDepartamento).toString());
                if(!edtSalasReservadasData.getText().toString().equals("")){
                    listar();
                }
            }

        }
        if(v == btnSalasReservadasProximo){
            // verificar se tem sala no departamento selecionado
            if(lstSalaDoDepartamento.size() > 0){
                // atualizar indice da sala
                if(posListaSalaDepartamento < lstSalaDoDepartamento.size()-1){
                    posListaSalaDepartamento += 1;
                }
                else{
                    posListaSalaDepartamento = 0;
                }
                // atuaizar lista de reservas
                txtSalasReservadasSala.setText(lstSalaDoDepartamento.get(posListaSalaDepartamento).toString());
                if(!edtSalasReservadasData.getText().toString().equals("")){
                    listar();
                }
            }
        }

    }

    // listar todas as reservas do departamento e sala no dia selecionado
    public void listar(){
        // array adapter personalizado
        adpReserva = new ListarReservaPorArrayAdapter(this,R.layout.item_listar_reserva_por);
        // lsita de todas as reservas
        ArrayList<Reserva> lstR = new ArrayList<>();
        // lista com as reservas válidas
        lstReserva = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        // carregar todas as reservas
        lstR = cd.carregarReserva();
        for(Reserva r:lstR){
            // verificar se reserva é do mesmo departamento
            if(r.getIddepartamento()==lstDepartamento.get(spnSalasReservadasDepartamento.getSelectedItemPosition()).getId()) {
                // verificar se a sala é de reservas não confirmadas (imaginária criada para pendentes e canceladas)
                if (lstSalaDoDepartamento.get(posListaSalaDepartamento).getNumero() == 0) {
                    // se for de pendencias, pega as reervas que não foram confimadas
                    if (r.getStatus() != 2) {
                        // verifica o dia da reserva e compara com o selecionado
                        if (r.getDatareserva().equals(edtSalasReservadasData.getText().toString())) {
                            // adiciona na lista de válidos
                            lstReserva.add(r);
                        }
                    }
                }
                else {
                    // se for uma sala existente, verifica o id da sala e a data da reserva
                    // se os dois baterem, a reserva é valida
                    if (r.getIdsala() == lstSalaDoDepartamento.get(posListaSalaDepartamento).getId()) {
                        if (r.getDatareserva().equals(edtSalasReservadasData.getText().toString())) {
                            if (r.getStatus() == 2) {
                                lstReserva.add(r);
                            }
                        }
                    }
                }
            }
        }

        // depois de pegar todas as reservas válidas da sala, tem que ver os períodos, para não deixar nenhum periodo vazio
        // para cada periodo 1-7, verifica se tem uma reserva nesse período, senão, cria uma reserva extra sem preenchimento
        for(int periodo=1;periodo<7;periodo++){
            boolean existe = false;
            // verificar na lista de reserva se tem esse periodo
            for(Reserva r: lstReserva){
                if(r.getPeriodo()==periodo){
                    existe = true;
                }
            }
            // se não tem reserva nesse período
            if(!existe){
                Reserva reservaExtra = new Reserva();
                reservaExtra.setId(-1);
                reservaExtra.setStatus(100);
                reservaExtra.setIdusuario(-1);
                reservaExtra.setPeriodo(periodo);
                lstReserva.add(reservaExtra);
            }
        }
        // ordenar a lista de reservas pelo periodo
        Collections.sort(lstReserva,Reserva.ReservaPeriodoComparator);
        // adicionar ao array adapter
        for(Reserva r:lstReserva){
            adpReserva.add(r);
        }
        // adicionar o array adapter no list view
        lstSalasReservadasReservas.setAdapter(adpReserva);
    }

    // funcao para mostrar data
    private void exibeData(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this,new SelecionaDataListener(),ano, mes,dia);
        dlg.show();
    }

    // evento com calendário
    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                exibeData();
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dt = DateUtils.dateToString(year, monthOfYear, dayOfMonth);
            Date date = DateUtils.getDate(year, monthOfYear, dayOfMonth);

            // atualizar lista de reservas
            edtSalasReservadasData.setText(dt);

            listar();

        }
    }
}
