package com.app.reserva.reservadesalauem.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.reserva.reservadesalauem.R;
import com.app.reserva.reservadesalauem.activities.MenuPrincipalActivity;
import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.Departamento;
import com.app.reserva.reservadesalauem.dados.Login;
import com.app.reserva.reservadesalauem.dados.Reserva;
import com.app.reserva.reservadesalauem.dados.Sala;
import com.app.reserva.reservadesalauem.dados.SalasDisponiveis;
import com.app.reserva.reservadesalauem.dados.Usuario;
import com.app.reserva.reservadesalauem.dados.adapters.SalasDisponiveisArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SalasDisponiveisPorDiaFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Login login;

    private Spinner spnSalasDisponiveisPorDiaDepartamento;
    private EditText edtSalasDisponiveisPorDiaDia;

    private ListView lstSalasDisponiveisPorDiaReservas;

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


    public SalasDisponiveisPorDiaFragment() {
        // Required empty public constructor
    }

    public static SalasDisponiveisPorDiaFragment newInstance(String param1, String param2) {
        SalasDisponiveisPorDiaFragment fragment = new SalasDisponiveisPorDiaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = new Login();
        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle.containsKey(MenuPrincipalActivity.EMAIL)){
            login.setEmail(bundle.getString(MenuPrincipalActivity.EMAIL));
        }
        if (bundle.containsKey(MenuPrincipalActivity.SENHA)){
            login.setSenha(bundle.getString(MenuPrincipalActivity.SENHA));
        }
        if (bundle.containsKey(MenuPrincipalActivity.PRIVILEGIO)){
            login.setPrivilegio(bundle.getInt(MenuPrincipalActivity.PRIVILEGIO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salas_disponiveis_por_dia, container, false);
        spnSalasDisponiveisPorDiaDepartamento = (Spinner) view.findViewById(R.id.spnSalasDisponiveisPorDiaDepartamento);

        // solictar foco no sppiner para o foco não ir no edit text que abre calendário
        spnSalasDisponiveisPorDiaDepartamento.requestFocus();

        edtSalasDisponiveisPorDiaDia = (EditText) view.findViewById(R.id.edtSalasDisponiveisPorDiaData);


        lstSalasDisponiveisPorDiaReservas = (ListView) view.findViewById(R.id.lstSalasDisponiveisPorDiaReservas);

        btnSalasDisponiveisPorDiaAnterior = (Button) view.findViewById(R.id.btnSalasDisponiveisDiaAnterior);
        btnSalasDisponiveisPorDiaAnterior.setOnClickListener(this);

        btnSalasDisponiveisPorDiaProximo = (Button) view.findViewById(R.id.btnSalasDisponiveisDiaProximo);
        btnSalasDisponiveisPorDiaProximo.setOnClickListener(this);

        txtSalasDisponiveisTipo = (TextView) view.findViewById(R.id.txtSalasDisponiveisTipo);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
        edtSalasDisponiveisPorDiaDia.setText(day + "/" + month + "/" + year);

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
        return view;
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
        adpDepartamento = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);

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
        adpSalasDisponiveis = new SalasDisponiveisArrayAdapter(getContext(),R.layout.item_sala_disponivel,login);
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
        if(edtSalasDisponiveisPorDiaDia.getText().toString().length()==0){
            MessageBox.show(getContext(), getString(R.string.erro), getString(R.string.dataNaoSelect));
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

        DatePickerDialog dlg = new DatePickerDialog(getContext(),new SelecionaDataListener(),ano, mes,dia);
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
