package com.app.reserva.reservadesalauem;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Departamento;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.SalasDisponiveis;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.ListarReservaPorArrayAdapter;
import com.app.reserva.reservadesalauem.dados.adapters.SalasDisponiveisArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SalasDisponiveisPorDiaActivity extends AppCompatActivity implements View.OnClickListener
{

    private Login login;

    private Spinner spnSalasDisponiveisPorDiaDepartamento;
    private EditText edtSalasDisponiveisPorDiaDia;

    private ListView lstSalasDisponiveisPorDiaReservas;

    private Button btnSalasDisponiveisPorDiaVoltar;
    private Button btnSalasDisponiveisPorDiaAnterior;
    private Button btnSalasDisponiveisPorDiaProximo;

    private TextView txtSalasDisponiveisTipo;

    private ArrayAdapter<Departamento> adpDepartamento;
    private ArrayList<Departamento> lstDepartamento;

    private ArrayList<Sala> lstSala;
    private ArrayList<Sala> lstSalaProj;
    private ArrayList<Sala> lstSalaLab;


    private ArrayAdapter<Reserva> adpReserva;
    private ArrayList<Reserva> lstReserva;

    private int tipoSala; // lab = 1; proj = 2

    private Usuario usuario;

    private ArrayList<SalasDisponiveis> lstSalasDisponiveis;
    private ArrayAdapter<SalasDisponiveis> adpSalasDisponiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_disponiveis_por_dia);
        login = new Login();

        // receber parametros
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }



        spnSalasDisponiveisPorDiaDepartamento = (Spinner) findViewById(R.id.spnSalasDisponiveisPorDiaDepartamento);

        // solictar foco no sppiner para o foco não ir no edit text que abre calendário
        spnSalasDisponiveisPorDiaDepartamento.requestFocus();

        edtSalasDisponiveisPorDiaDia = (EditText) findViewById(R.id.edtSalasDisponiveisPorDiaData);


        lstSalasDisponiveisPorDiaReservas = (ListView) findViewById(R.id.lstSalasDisponiveisPorDiaReservas);

        btnSalasDisponiveisPorDiaVoltar = (Button) findViewById(R.id.btnSalasDisponiveisPorDiaVoltar);
        btnSalasDisponiveisPorDiaVoltar.setOnClickListener(this);

        btnSalasDisponiveisPorDiaAnterior = (Button) findViewById(R.id.btnSalasDisponiveisDiaAnterior);
        btnSalasDisponiveisPorDiaAnterior.setOnClickListener(this);

        btnSalasDisponiveisPorDiaProximo = (Button) findViewById(R.id.btnSalasDisponiveisDiaProximo);
        btnSalasDisponiveisPorDiaProximo.setOnClickListener(this);

        txtSalasDisponiveisTipo = (TextView) findViewById(R.id.txtSalasDisponiveisTipo);

        // selecioanr tipo de sala padrão como laboratório
        tipoSala = 1;
        // escrever que é laboratório
        txtSalasDisponiveisTipo.setText(getString(R.string.laboratorio) + " " + getString(R.string.livreStr));

        buscarUsuario();
        // preencher todas as sppiners com valor padrão
        preencherSpinner();

        // criar evento que exibe calendário para seleção de data
        ExibeDataListener listener = new ExibeDataListener();

        edtSalasDisponiveisPorDiaDia.setOnClickListener(listener);
        //edtListarPorDiaData.setOnFocusChangeListener(listener);

        edtSalasDisponiveisPorDiaDia.setKeyListener(null);

        CarregarDadoUtils cd = new CarregarDadoUtils();
        // pegar data atual do servidor
        String dataAtual = cd.carregarDataAtual();
        // setar data default como o atual
        edtSalasDisponiveisPorDiaDia.setText(dataAtual);
        // preencher lista de salas
        preencherLista();
        //listar();
    }

    // buscar dados do usuario, o departamento é usado para saber o padrao
    public void buscarUsuario(){
        usuario = new Usuario();

        ArrayList<Usuario> lstU = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstU = cd.carregarUsuario();
        for(Usuario u:lstU) {
            if (u.getEmail().equals(login.getEmail())) {
                usuario.clonar(u);
                return;
            }
        }
    }

    // preenche as sppiners com valor padão. O departamento é o primeiro, caso entre sem login,
    // senão, é o departamento do usuário.
    public void preencherSpinner(){

        CarregarDadoUtils cd = new CarregarDadoUtils();
        // lista de todos os departamentos
        lstDepartamento = new ArrayList<>();
        lstDepartamento = cd.carregarDepartamento();
        // array adapter de todos os departamentos
        adpDepartamento = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        // verificar posicao do departamento do usuario
        int posdep = 0;
        // verifiar se encontrou o departamento do usuario
        int encontrou = 0;
        for(Departamento dep:lstDepartamento){
            adpDepartamento.add(dep);
            // se for um usuario logado, procura o departamento dele
            if(login.getPrivilegio() != -1) {
                // se não encontrou, vai avançando
                if (encontrou == 0) {
                    if (dep.getId() == usuario.getId_departamento()) {
                        encontrou = 1;
                    }
                    else{
                        posdep += 1;
                    }
                }
            }
        }

        // setar o sppiner do departamento
        spnSalasDisponiveisPorDiaDepartamento.setAdapter(adpDepartamento);
        // selecionar posição do departamento padrão
        spnSalasDisponiveisPorDiaDepartamento.setSelection(posdep);
        // lista de todas as salas
        lstSala = new ArrayList<Sala>();
        // lista de todas as salas de projecao do departamento selecionado
        lstSalaProj = new ArrayList<>();
        // lista de todos os laboratorios do departamento selecionado
        lstSalaLab = new ArrayList<>();
        // carregar todas as salas
        lstSala = cd.carregarSala();
        // carregar todas as salas do departamento selecionado, separando por sala de projeção ou laboratório
        for(Sala sala:lstSala){
            // verificar departamento da sala
            if (sala.getId_departamento()==lstDepartamento.get(spnSalasDisponiveisPorDiaDepartamento.getSelectedItemPosition()).getId()) {
                // verificar se é sala de projeção ou laboratório
                if(sala.getClassificacao() == 1){
                    lstSalaLab.add(sala);
                }
                else{
                    lstSalaProj.add(sala);}
            }
        }

    }

    // preencher a lista de salas
    public void preencherLista(){
        // lista de salas disponíveis
        lstSalasDisponiveis = new ArrayList<>();
        // array adapter de salas que serão mostradas na tela
        adpSalasDisponiveis = new SalasDisponiveisArrayAdapter(this,R.layout.item_sala_disponivel,login);
        // para cada periodo verificar quantas salas estão sendo usados, de cada tipo (proj e lab)
        for(int i=0;i<6;i++){ // para cada periodo
            // novo tipo de dados, salas disponiveis
            SalasDisponiveis sd = new SalasDisponiveis();
            sd.setData(edtSalasDisponiveisPorDiaDia.getText().toString());
            sd.setLogin(login);
            sd.setPeriodo(i + 1);
            // inserir quantidade de salas livres, calculado pelo total de sals de um tipo - quantidade de reservas para
            // esse periodo nesse tipo de sala, que estáo confirmados
            if(tipoSala==1){
                sd.setSalaslivres(lstSalaLab.size()-salasUsadas(i+1));
            }
            else{
                sd.setSalaslivres(lstSalaProj.size()-salasUsadas(i+1));
            }
            sd.setTiposala(tipoSala);
            lstSalasDisponiveis.add(sd);
            adpSalasDisponiveis.add(sd);
        }
        lstSalasDisponiveisPorDiaReservas.setAdapter(adpSalasDisponiveis);
    }

    // verificar quantas salas de um determinado periodo e dia estão reservadas
    private int salasUsadas(int periodo){
        int usado = 0;
        try{
            CarregarDadoUtils cd = new CarregarDadoUtils();
            ArrayList<Reserva> lstR = cd.carregarReserva();
            for(Reserva r:lstR){
                // verificar se reserva está confirmado
                if(r.getStatus() == 2){
                    // comparar data
                    if(r.getDatareserva().equals(edtSalasDisponiveisPorDiaDia.getText().toString())){
                        // comparar periodo
                        if(r.getPeriodo()==periodo){
                            // comparar tipo de sala
                            if(r.getTiposala() == tipoSala){
                                usado += 1;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
        }
        return usado;
    }


    @Override
    public void onClick(View v) {
        if(v == btnSalasDisponiveisPorDiaVoltar){
            finish();
            return;
        }
        if(edtSalasDisponiveisPorDiaDia.getText().toString().length()==0){
            MessageBox.show(this, getString(R.string.erro), getString(R.string.dataNaoSelect));
            return;
        }
        if(v == btnSalasDisponiveisPorDiaAnterior){
            if(this.tipoSala == 1){
                tipoSala = 2;
                txtSalasDisponiveisTipo.setText(getString(R.string.salaStr) + " " + getString(R.string.livreStr));
            }
            else{
                tipoSala = 1;
                txtSalasDisponiveisTipo.setText(getString(R.string.laboratorio) + " " + getString(R.string.livreStr));
            }
            preencherLista();
        }
        if(v == btnSalasDisponiveisPorDiaProximo){
            if(this.tipoSala == 1){
                tipoSala = 2;
                txtSalasDisponiveisTipo.setText(getString(R.string.salaStr) + " " + getString(R.string.livreStr));
            }
            else{
                tipoSala = 1;
                txtSalasDisponiveisTipo.setText(getString(R.string.laboratorio) + " " + getString(R.string.livreStr));
            }
            preencherLista();
        }
    }

    // função copiada do tutorial
    private void exibeData(){

        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this,new SelecionaDataListener(),ano, mes,dia);
        dlg.show();
    }

    // função copiada do tutorial, evento
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
            edtSalasDisponiveisPorDiaDia.setText(dt);
            // quando uma data é selecionada, recria a lista de salas
            preencherLista();
        }
    }
}
