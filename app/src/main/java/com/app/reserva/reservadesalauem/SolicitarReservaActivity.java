package com.app.reserva.reservadesalauem;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.app.reserva.reservadesalauem.app.MessageBox;
import com.app.reserva.reservadesalauem.dados.*;
import com.app.reserva.reservadesalauem.dados.adapters.PeriodoArrayAdapter;
import com.app.reserva.reservadesalauem.util.CarregarDadoUtils;
import com.app.reserva.reservadesalauem.util.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SolicitarReservaActivity extends AppCompatActivity implements View.OnClickListener,Runnable{

    private Login login; // login do usuário
    private Usuario usuario; // dados do usuário logado
    private Date atual = new Date(); // data atual
    private int semana; // verificar qual semana é (atual (n) =0, seguinte (n+1) =1, n+2 ou mais = 2)

    // usado para quando for solicitar uma reserva a partir de salas disponíveis
    private SalasDisponiveis salasDisponiveis;

    private Spinner spnSolicitarReservaDocente;
    private Spinner spnSolicitarReservaTipoAula;
    private Spinner spnSolicitarReservaDisciplina;
    private Spinner spnSolicitarReservaTipoSala;
    private Spinner spnSolicitarReservaPeriodo;
    private Spinner spnSolicitarReservaTipo;

    private ArrayList<Usuario> lstUsuario;
    private ArrayList<Usuario> lstUsuarioValido;

    private ArrayList<Periodo> lstPeriodos;

    private ArrayList<Disciplina> lstDisciplina;

    private ArrayList<Reserva> lstReserva;

    private ArrayAdapter<Usuario> adpUsuario;
    private ArrayAdapter<String> adpTipoAula;
    private ArrayAdapter<Disciplina> adpDisciplina;
    private ArrayAdapter<String> adpTipoSala;
    private ArrayAdapter<String> adpPeriodo;
    private ArrayAdapter<String> adpTipo;


    private Button btnSolicitarReservaCancelar;
    private Button btnSolicitarReservaSolicitar;


    private EditText edtSolicitarReservaData;
    private EditText edtSolicitarReservaObservacao;

    private TextView txtSoliciatarReservaTipo;

    // resposta do servidor
    private int resultado;

    private Reserva reserva;

    // verificar se veio do menu principal (0) ou de sals disponiveis (1), se opção é 1, tem que
    // preencher os dados
    private int opcao;

    // usado para atualizar periodo em relação
    private int reselecionaPeriodo;

    private AnoLetivo anoLetivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_reserva);


        edtSolicitarReservaData = (EditText) findViewById(R.id.edtSolicitarReservaData);

        btnSolicitarReservaCancelar = (Button) findViewById(R.id.btnSolicitarReservaCancelar);
        btnSolicitarReservaCancelar.setOnClickListener(this);

        btnSolicitarReservaSolicitar = (Button) findViewById(R.id.btnSolicitarReservaSolicitar);
        btnSolicitarReservaSolicitar.setOnClickListener(this);

        spnSolicitarReservaDocente = (Spinner) findViewById(R.id.spnSolicitarReservaDocente);
        spnSolicitarReservaTipoAula = (Spinner) findViewById(R.id.spnSolicitarReservaTipoAula);
        spnSolicitarReservaDisciplina = (Spinner) findViewById(R.id.spnSolicitarReservaDisciplina);
        spnSolicitarReservaTipoSala = (Spinner) findViewById(R.id.spnSolicitarReservaTipoSala);
        spnSolicitarReservaPeriodo = (Spinner) findViewById(R.id.spnSolicitarReservaPeriodo);
        spnSolicitarReservaTipo = (Spinner) findViewById(R.id.spnSolicitarReservaTipo);

        // solicitar foco em spinner para não focr o edit text e abrir tela de seleção de calendario
        spnSolicitarReservaDisciplina.requestFocus();

        txtSoliciatarReservaTipo = (TextView) findViewById(R.id.txtSolicitarReservaTipo);

        // lista de todos os usuarios
        lstUsuario = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        // carregar todos os usuarios
        lstUsuario = cd.carregarUsuario();
        // lista de usuarios válidos (no caso de ser docente, é so ele). se quem está logado tiver
        // permissão maior que docente, a lista de todos os docentes
        lstUsuarioValido = new ArrayList<>();
        // lista de todas as reservas existentes
        lstReserva = new ArrayList<>();
        lstReserva = cd.carregarReserva();

        login = new Login();

        reselecionaPeriodo = 0;

        // carregar data atual do servidor
        String dataAtual = cd.carregarDataAtual();
        // usado para converter string em data
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
        df.setLenient (false);
        java.util.Date dAtual = new Date();
        // converter para java.Data
        try{
            dAtual = df.parse(dataAtual);
        }
        catch(Exception ex){}

        // atual é uma data do tipo sql
        atual = new Date();
        // tem que pegar o time de java.util e converter para sql
        atual.setTime(dAtual.getTime());

        // verifica quem chamou essa tela
        opcao = 0;
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(MenuPrincipalActivity.PREENCHERSOLICITACAO)){
            opcao = (Integer)bundle.get(MenuPrincipalActivity.PREENCHERSOLICITACAO);
        }
        // se foi a tela de salas livres, tem que pegar o dado da sala e periodo
        if(opcao==1){
            if (bundle.containsKey(MenuPrincipalActivity.LOGIN)){
                login = (Login)bundle.get(MenuPrincipalActivity.LOGIN);
            }
            salasDisponiveis = new SalasDisponiveis();
            if (bundle.containsKey(MenuPrincipalActivity.SALASDISPONIVEIS)){
                salasDisponiveis = (SalasDisponiveis)bundle.get(MenuPrincipalActivity.SALASDISPONIVEIS);
            }
        }
        // se foi diretamente do menu principal, só precisa pegar o login
        else{
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
        // se for docente, só pode solicitar reserva em seu próprio nome
        if(login.getPrivilegio() == 1){
            lstUsuarioValido.add(getUsuario(login.getEmail()));
        }
        // se não foi docente, pode reservar para qualquer docente
        else{
            for(Usuario u:lstUsuario){
                if(u.getPermissao() == 1){
                    lstUsuarioValido.add(u);
                }
            }
        }
        // lista de todas as disciplinas
        lstDisciplina = new ArrayList<Disciplina>();
        lstDisciplina = cd.carregarDisciplina();

        // evento que exibe calendário para seleção de data
        ExibeDataListener listener = new ExibeDataListener();
        edtSolicitarReservaData.setOnClickListener(listener);
        edtSolicitarReservaData.setKeyListener(null);

        // preencher todos os dados, deixar como padrão se vier da tela principal, senão preencher seus devidos campos
        preencheDados();

        // evento para quando seleciona o sppiner docente, para alterar docente
        spnSolicitarReservaDocente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // atualiza usuario
                usuario = lstUsuarioValido.get(spnSolicitarReservaDocente.getSelectedItemPosition());
                // atualiza a lista de disciplinas válidas para o novo usuario
                atualizaDisciplina();
                // se for reserva de laboratório para aula teórica, reseta data
                if (spnSolicitarReservaTipoSala.getSelectedItemId() == 0) {
                    if (spnSolicitarReservaTipoAula.getSelectedItemId() == 1) {
                        edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                        semana = 0;
                    }
                }
                // caso mude o tipo de aula, pode acabar mudando para uma disciplina de outro curso (com limite 2 dias).
                // para evitar que este possa ser solicitado em dia inválido, resstaura a data.
                Disciplina dicp1 = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());
                Curso curso = getCurso(dicp1.getId_curso());
                if (curso.getTipo() == 2) {
                    edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                }
                // atualizar os periodos disponiveis
                atualizaPeriodo();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // evento para quando muda tipo de aula (pratica, teorica, mestrado ...)
        spnSolicitarReservaTipoAula.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // atualiza a lsita de disciplinas
                atualizaDisciplina();
                // se for aula teórica em laboratório, reseta a data

                if(spnSolicitarReservaTipoSala.getSelectedItemId()==0){
                    if(spnSolicitarReservaTipoAula.getSelectedItemId()==1){
                        edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                        semana = 0;
                    }
                }

                // caso mude o tipo de aula, pode acabar mudando para uma disciplina de outro curso (com limite 2 dias).
                // para evitar que este possa ser solicitado em dia inválido, resstaura a data.
                Disciplina dicp1 = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());
                Curso curso = getCurso(dicp1.getId_curso());
                if(curso.getTipo()==2){
                    edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                }
                semana = 0;
                atualizaPeriodo();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // evento para quando muda disciplina
        spnSolicitarReservaDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // caso mude disciplina, pode acabar mudando para uma disciplina de outro curso (com limite 2 dias).
                // para evitar que este possa ser solicitado em dia inválido, resstaura a data.
                Disciplina dicp1 = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());
                Curso curso = getCurso(dicp1.getId_curso());
                if(curso.getTipo()==2){
                    edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                }
                semana = 0;
                atualizaPeriodo();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // evento para spinner tipo de sala
        spnSolicitarReservaTipoSala.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // se for selecionado laboratório para aula teórica, reseta data
                if(spnSolicitarReservaTipoSala.getSelectedItemId()==0){
                    if(spnSolicitarReservaTipoAula.getSelectedItemId()==1){
                        edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
                        semana = 0;
                    }
                }
                // atualiza o período
                atualizaPeriodo();
                // por algum motivo, quando preenchia o periodo antes, quando vier da tela de salas disponiveis, resetava o
                // indice do perído para zero. Para arrumar isso, tive que colocar isso nesse ultimo evento de spinner
                // antes do de período. Reseleciona periodo é para reselecionar posição do periodo
                if(reselecionaPeriodo == 0){
                    // só precisa ser executado uma vez
                    reselecionaPeriodo = 1;
                    // só se vier da tela de salas disponiveis
                    if(opcao == 1){
                        spnSolicitarReservaPeriodo.setSelection(salasDisponiveis.getPeriodo());
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // evento para seleção de período da sppiner
        spnSolicitarReservaPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // verifica se alguma data foi selecionada, se não mostra mensagem de erro
                if(edtSolicitarReservaData.getText().toString().length()==0){
                    // periodo na posição 0 é vazio, como padrão
                    if(spnSolicitarReservaPeriodo.getSelectedItemId() == 0){
                        return;
                    }
                    spnSolicitarReservaPeriodo.setSelection(0);
                    MessageBox.show(adapterView.getContext(),"",getString(R.string.selecionarData));
                }
                // se tiver data selecionada, verifica se tem sala livre no periodo, se não mostra mensagem de erro
                else{
                    if(lstPeriodos.get(spnSolicitarReservaPeriodo.getSelectedItemPosition()).getStatus()==1){
                        spnSolicitarReservaPeriodo.setSelection(0);
                        MessageBox.show(adapterView.getContext(),"",getString(R.string.periodoInUse));
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        // se vier da tela de salas disponiveis tem que preencher os dados
        if(opcao==1){
            // se tiver solicitado uma sala de projeção, seleciona tipo de aula como teórica, senão, como padrão
            // vai na pratica
            if(salasDisponiveis.getTiposala()==2){
                spnSolicitarReservaTipoAula.setSelection(1);
            }
            // atualizar as disciplinas em relação ao tipo de aula e docente
            atualizaDisciplina();
            // seleciona tipo de sala. como arivael, lab = 1 e proj = 2, mas no spinner é posição 0 e 1
            spnSolicitarReservaTipoSala.setSelection(salasDisponiveis.getTiposala() - 1);
            // preencher a data para a data que foi passado como parametro
            edtSolicitarReservaData.setText(salasDisponiveis.getData());
            // atualizar lista de periodo
            atualizaPeriodo();
            // selecionar o periodo que recebeu como paramentro
            spnSolicitarReservaPeriodo.setSelection(salasDisponiveis.getPeriodo());

        }
        // se veio do menu principal, tem que setar a data como padrão para o atual
        else{
            edtSolicitarReservaData.setText(DateUtils.dateToString(atual));
            atualizaPeriodo();
        }
    }


    // procurar o usuário pelo email
    private Usuario getUsuario(String email){
        Usuario user1 = new Usuario();
        for (Usuario u:lstUsuario){
            //System.out.println(u.getEmail());
            if(u.getEmail().equals(email)){
                user1.clonar(u);
                usuario = new Usuario();
                usuario.clonar(u);
            }
        }
        return user1;
    }

    // procurar curso pelo id
    private Curso getCurso(int id){
        Curso curso = new Curso();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        for(Curso c:cd.carregarCurso()){
            if(c.getId()==id){
                curso.clonar(c);
                return curso;
            }
        }
        return curso;
    }

    // remover caracter " , caso ele esteja na String, por algum erro, tinha disciplina com esse caractere, não lembro se
    // tinha arrumado ou não
    private String arrumaDisciplina(String d){
        String discs = "";
        for(int i=0;i<d.length();i++){
            if(d.charAt(i) != '"'){
                discs += d.charAt(i);
            }
        }
        return discs;
    }

    // verificar disciplinas válidas, no caso pegar todas as disciplinas de um tipo(teórica, pratica) do usuario
    // depois tem que adicionar disciplinas do mestrado
    private ArrayList<Disciplina> getDisciplinasValidas(){
        ArrayList<Disciplina> dv = new ArrayList<>();

        //Disciplina  disciplina = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());


        // definir a data limite limites entre o primeiro e segundo semestre, o limite é dado pelo inicio do segundo
        java.util.Date datalimite = new Date();

        // definir a data em que será solicitado a reserva, pego do edit text
        java.util.Date dataReserva = new Date();

        // usado apra converter string para java.util.date
        DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
        dformat.setLenient(false);

        // variavel que diz se está no primeiro ou segundo semestre. como padrao, começa no primeiro
        int semestre = 1;
        // pega a data de inicio do segundo semestre
        try{
            datalimite = dformat.parse(anoLetivo.getInicios());
            dataReserva = dformat.parse(edtSolicitarReservaData.getText().toString());
        }
        catch (Exception e12){
        }
        // se a data atual for maior que o inicio do segundo semestre, está nbo segundo semestre
        if(dataReserva.getTime()>=datalimite.getTime()){
            semestre = 2;
        }


        // para cada disciplina existente, verificar se é uma disciplina do docente ou não
        for (Disciplina d:lstDisciplina){
            // dividir a string de disciplinas, pois cada disciplina é separado por um '-'
            for(String dic:usuario.getId_disciplinas().split("-")){
                // arrumar formato da disciplina, de ele vier com caractere '"' por erro
                if(arrumaDisciplina(dic).equals("" + d.getId())) {
                    // verificar se disciplina é do primeiro ou segundo semestre ou anual, se for do mesmo semestre ou
                    // anual, a disciplina é valida

                    if ((d.getPeriodo() == semestre) || (d.getPeriodo() == 0)) {

                        // verifica classificação da disciplina e vê se é igual ao selecionado na sppiner
                        // pratica
                        if (d.getClassificacao() == 1 && spnSolicitarReservaTipoAula.getSelectedItemId() == 0) {
                            Disciplina d1 = new Disciplina();
                            d1.clonar(d);
                            dv.add(d1);
                        }
                        // teorica
                        if (d.getClassificacao() == 2 && spnSolicitarReservaTipoAula.getSelectedItemId() == 1) {
                            Disciplina d1 = new Disciplina();
                            d1.clonar(d);
                            dv.add(d1);
                        }
                        // mestrado
                        if (d.getClassificacao() == 3 && spnSolicitarReservaTipoAula.getSelectedItemId() == 2) {
                            Disciplina d1 = new Disciplina();
                            d1.clonar(d);
                            dv.add(d1);
                        }
                    }
                }
            }
        }
        return dv;
    }

    // preenche os campos e as sppiners com valor padrão
    private void preencheDados(){
        // apenas a spinner periodo tem array adapter personalizado (com cores e borda)
        adpUsuario = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        adpDisciplina = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        adpTipoAula = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        adpTipoSala = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        adpTipo = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        // possíveis tipos de aula
        adpTipoAula.add(getString(R.string.praticaStr));
        adpTipoAula.add(getString(R.string.teoricaStr));
        adpTipoAula.add(getString(R.string.mestradoStr));
        adpTipoAula.add(getString(R.string.especializacaoStr));
        adpTipoAula.add(getString(R.string.defesaStr));
        adpTipoAula.add(getString(R.string.minicursoStr));

        // lista de períodos
        List<String> lstP = new ArrayList<>();
        // o lstPeriodo é personalizado, com o nome e Status, para ser passado como parametro para o array adapter
        lstPeriodos = new ArrayList<>();
        lstP.add("");
        lstPeriodos.add(new Periodo("",0));
        
        Resources res = this.getResources();
        TypedArray listaPeriodos = res.obtainTypedArray(R.array.listaPeriodos);
        for(int i = 0; i<6; i++){
            String numeroPeriodo = String.valueOf(i+1) + listaPeriodos.getString(i);
            lstPeriodos.add(new Periodo(numeroPeriodo,1));
            lstP.add(numeroPeriodo + getString(R.string.rightright));
        }
        adpPeriodo = new PeriodoArrayAdapter(this,R.layout.item_periodo,lstP,lstPeriodos);

        adpTipoSala.add(getString(R.string.laboratorio));
        adpTipoSala.add(getString(R.string.projecao));

        // tipo de reserva como padrão é eventual
        adpTipo.add(getString(R.string.eventualStr));
        // se for docente, desabilita o tipo de reserva e o oculta
        if(login.getPrivilegio() == 1){
            adpUsuario.add(getUsuario(login.getEmail()));
            spnSolicitarReservaTipo.setVisibility(View.INVISIBLE);
            spnSolicitarReservaTipo.setEnabled(false);
            txtSoliciatarReservaTipo.setVisibility(View.INVISIBLE);
            txtSoliciatarReservaTipo.setEnabled(false);
        }
        // senão, adiciona o fixo (não foi implementado as restrições para fixo)
        else{
            // spinner de usuario fica com todos os docentes
            for(Usuario u:lstUsuario){
                if(u.getPermissao() == 1){
                    adpUsuario.add(u);
                }
            }
            usuario = adpUsuario.getItem(0);
            adpTipo.add(getString(R.string.fixoStr));
       }
        spnSolicitarReservaDocente.setAdapter(adpUsuario);
        spnSolicitarReservaTipoAula.setAdapter(adpTipoAula);
        spnSolicitarReservaTipo.setAdapter(adpTipo);
        spnSolicitarReservaPeriodo.setAdapter(adpPeriodo);
        spnSolicitarReservaTipoSala.setAdapter(adpTipoSala);

        // adicionar as disciplinas válidas no sppiner
        for(Disciplina dv:this.getDisciplinasValidas()){
            adpDisciplina.add(dv);
        }

        spnSolicitarReservaDisciplina.setAdapter(adpDisciplina);

        //  verificar limite em realação às disciplinas semestrais (inicio e fim do primeiro e segundo semestre)
        CarregarDadoUtils cd = new CarregarDadoUtils();
        ArrayList<AnoLetivo> lstAL = cd.carregarAnoLetivo();
        for(AnoLetivo al: lstAL){
            if(al.getIddepartamento()==usuario.getId_departamento()){
                anoLetivo = new AnoLetivo();
                anoLetivo.clonarAnoLetivo(al);
            }
        }

    }

    // atualizar o sppiner de disciplinas válidas
    private void atualizaDisciplina(){
        adpDisciplina = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        for(Disciplina dv:this.getDisciplinasValidas()){
            adpDisciplina.add(dv);
        }
        spnSolicitarReservaDisciplina.setAdapter(adpDisciplina);
    }

    // verifica quantas salasdo tipo laboratorio ou projecao estão livres naquele periodo e dia
    private int salasLivres(int periodo){
        // seta salas livres como 0
        int livres = 0;
        // carrega todas as salas
        ArrayList<Sala> lstSalaTotal = new ArrayList<>();
        CarregarDadoUtils cd = new CarregarDadoUtils();
        lstSalaTotal = cd.carregarSala();
        // lista sala tipo é lista de salas do tipo selecionado na spinner (lab ou proj)
        ArrayList<Sala> lstSalaTipo = new ArrayList<>();
        // verifica todos os lab do departamento
        if(spnSolicitarReservaTipoSala.getSelectedItemId()==0){
            for(Sala s1:lstSalaTotal){
                if(s1.getId_departamento()==usuario.getId_departamento()){
                    if(s1.getClassificacao()==1){
                        lstSalaTipo.add(s1);
                    }
                }
            }
        }
        // verifica todos os proj do departamento
        else{
            for(Sala s1:lstSalaTotal){
                if(s1.getId_departamento()==usuario.getId_departamento()){
                    if(s1.getClassificacao()==2){
                        lstSalaTipo.add(s1);
                    }
                }
            }
        }
        // salas livres é a quantidade de salas existentes - a quantidade de salas usadas
        livres = lstSalaTipo.size()- salasUsadas(periodo);
        return livres;
    }

    // verifica quantas salas de um tipo estão reservadas naquele dia e periodo
    private int salasUsadas(int periodo){
        // padrão usado = 0
        int usado = 0;
        try{
            // carregar todas as salas
            CarregarDadoUtils cd = new CarregarDadoUtils();
            ArrayList<Reserva> lstR = cd.carregarReserva();
            for(Reserva r:lstR){
                // verificar se status é 2 (confirmado)
                if(r.getStatus() == 2){
                    // verificar a data
                    if(r.getDatareserva().equals(edtSolicitarReservaData.getText().toString())){
                        // verificar periodo
                        if(r.getPeriodo()==periodo){
                            // verificar tipo de sala
                            if(r.getTiposala() == spnSolicitarReservaTipoSala.getSelectedItemId()+1){
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

    // atualizar a spinner de periodos
    private void atualizaPeriodo(){
        // se data não for selecionada, mostra todos os periodos como usado
        if(edtSolicitarReservaData.getText().toString().equals("")){
            List<String> lstP = new ArrayList<>();
            lstPeriodos = new ArrayList<>();
            lstP.add("");
            lstPeriodos.add(new Periodo("",0));

            Resources res = this.getResources();
            TypedArray listaPeriodos = res.obtainTypedArray(R.array.listaPeriodos);
            for(int i = 0; i<6; i++){
                String numeroPeriodo = String.valueOf(i+1) + listaPeriodos.getString(i);
                lstPeriodos.add(new Periodo(numeroPeriodo,1));
                lstP.add(numeroPeriodo + getString(R.string.rightright));
            }

            adpPeriodo = new PeriodoArrayAdapter(this,R.layout.item_periodo,lstP,lstPeriodos);
            spnSolicitarReservaPeriodo.setAdapter(adpPeriodo);
            return;
        }
        // lista de periodos
        List<String> lstP = new ArrayList<>();
        lstPeriodos = new ArrayList<>();

        // lista de ststus de reserva
        int[] lstStatus = new int[6];
        // setar o status inicial como 2 = válido
        for(int i=0;i<6;i++){
            // status livre
            lstStatus[i] = 2;
        }
        // se semana for n+2, todos os periodos estarão como pendentes
        if(semana==2){
            for(int i=0;i<6;i++){
                // status pendente
                lstStatus[i] = 3;
            }
        }

        try{
            // carregar todas as reservas
            CarregarDadoUtils cd = new CarregarDadoUtils();
            ArrayList<Reserva> lstR = cd.carregarReserva();
            // para cada reserva, verificar a data, periodo e tipo de sala
            for(Reserva r:lstR){
                // se a reserva estiver confirmada
                if(r.getStatus()==2){
                    // verificar data
                    if(r.getDatareserva().equals(edtSolicitarReservaData.getText().toString())){
                        // verificar departamento
                        if(usuario.getId_departamento()==r.getIddepartamento()){
                            // se o usuario logado tiver uma reserva confirmada nesse período, não pode mais solicitar
                            // no periodo atual, não importa o tipo de sala
                            if(r.getIdusuario()==usuario.getId()) {
                                // status inválido
                                lstStatus[r.getPeriodo()-1] = 1;

                            }
                            // se não for o usuario, verifica se tem sala livre, se não, desativa periodo
                            else{
                                // verificar se tem sala livre
                                if(salasLivres(r.getPeriodo())==0){
                                    // status = invalido
                                    lstStatus[r.getPeriodo()-1] = 1;
                                }

                            }
                        }
                    }
                }
                // se a reserva estiver pendente
                if(r.getStatus()==1){
                    if(r.getDatareserva().equals(edtSolicitarReservaData.getText().toString())){
                        if(usuario.getId_departamento()==r.getIddepartamento()){
                            // se for na semana n+2, pode solicitar uma reserva para cada tipo de sala no mesmo periodo, sendo que
                            // o sistema prioriza os labaoratórios
                            if(r.getTiposala()==spnSolicitarReservaTipoSala.getSelectedItemId()+1){
                                // se o usuario tiver uma resera pendente, não pode solicitar outra reserva
                                if(r.getIdusuario()==usuario.getId()) {
                                    // status inválido
                                    lstStatus[r.getPeriodo()-1] = 1;

                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){
        }

        // lista de periodo com texto e status para passar para o array adapter

        lstP.add("");
        lstPeriodos.add(new Periodo("",0));

        Resources res = this.getResources();
        TypedArray listaPeriodos = res.obtainTypedArray(R.array.listaPeriodos);


        for(int i = 0; i<6; i++){
            String numeroPeriodo = String.valueOf(i+1) + listaPeriodos.getString(i);
            // adiciona o numero do periodo e o status dele
            lstPeriodos.add(new Periodo(numeroPeriodo,lstStatus[i]));
            // adiciona o numero do periodo
            lstP.add(numeroPeriodo + getString(R.string.rightright));
        }


        adpPeriodo = new PeriodoArrayAdapter(this,R.layout.item_periodo,lstP,lstPeriodos);
        spnSolicitarReservaPeriodo.setAdapter(adpPeriodo);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSolicitarReservaCancelar){
            finish();
        }
        if(v == btnSolicitarReservaSolicitar){
            // criar uma nova reserva
            reserva = new Reserva();
            // se tipo de aula for < 3 é teórica ou prática ou mestrado, então tem que selecionar uma disciplina
            // para o tipo de aula
            if(spnSolicitarReservaTipoAula.getSelectedItemId() < 3){
                if(getDisciplinasValidas().size() == 0){
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.selecionarDisc));
                    return;
                }
                else{
                    reserva.setIddisciplina(getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition()).getId());
                }
            }
            // se não precisar de disciplina, seta id = -1
            else{
                reserva.setIddisciplina(-1);
            }
            // verifica se tem data selecionada
            if(edtSolicitarReservaData.getText().toString().length() == 0){
                MessageBox.show(this, getString(R.string.erro), getString(R.string.selecionarData));
                return;
            }
            // salva dta de reserva
            reserva.setDatareserva(edtSolicitarReservaData.getText().toString());
            // verifica data atual do servidor
            CarregarDadoUtils cd = new CarregarDadoUtils();
            reserva.setDataefetuacao(cd.carregarDataAtual());
            // verifica se foi selecionado algum periodo, 1-6
            if(spnSolicitarReservaPeriodo.getSelectedItemId() == 0){
                MessageBox.show(this,getString(R.string.erro),getString(R.string.selecionarPeriodo));
                return;
            }
            reserva.setPeriodo((int)spnSolicitarReservaPeriodo.getSelectedItemId());
            // seta observação como vazio

            // tipo de reserva para docente é sempre eventual
            if(login.getPrivilegio() == 1){
                reserva.setTipo(1);
            }
            // se for secretário ou superior, pode ser eventual ou fixo
            else{
                reserva.setTipo((int)spnSolicitarReservaTipo.getSelectedItemId()+1);
            }
            // id da sala e id não serão vistos agora, portanto vai como -1, status = 1 é pendente
            reserva.setIdsala(-1);
            reserva.setId(-1);
            reserva.setStatus(1);
            // pegar id do departamento
            reserva.setIddepartamento(usuario.getId_departamento());
            // pegar tipo de aula
            reserva.setTipoaula((int) spnSolicitarReservaTipoAula.getSelectedItemId() + 1);
            // pegar id do usuario
            reserva.setIdusuario(usuario.getId());
            // proximo id vai -1, ou seja, não tem
            reserva.setProximoid(-1);
            // pega tipo de aula
            reserva.setTiposala((int) spnSolicitarReservaTipoSala.getSelectedItemId() + 1);
            // preenchimento completo, chamar a thread e enviar ao servidor
            Thread t = new Thread(this);
            t.start();

            try {
                // esperar thread terminar de rodar
                t.join();
                // combinação login e senha errado
                if(resultado == -1){
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.notAllowed));
                    return;
                }
                // erro na solicitação
                if(resultado == 0){
                    MessageBox.show(this,getString(R.string.erro),getString(R.string.requestFailed));
                    return;
                }
                // deu certo
                if(resultado == 1){
                    MessageBox.show(this,"",getString(R.string.sucessRequestReserva));
                    atualizaPeriodo();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void exibeData(){
        CarregarDadoUtils cd = new CarregarDadoUtils();
        // pegar data atual do servidor
        String dataAtual = cd.carregarDataAtual();
        // usado para converter string em data
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
        df.setLenient (false);
        java.util.Date dAtual = new Date();
        // converter para java.Data
        try{
            dAtual = df.parse(dataAtual);
        }
        catch(Exception ex){}

        // atual é uma data do tipo sql
        atual = new Date();
        // tem que pegar o time de java.util e converter para sql
        atual.setTime(dAtual.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(atual);
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this,new SelecionaDataListener(),ano, mes,dia);
        dlg.show();
    }

    @Override
    public void run() {
        try {
            AcessoAppUemWS ws = new AcessoAppUemWS();
            resultado = ws.solicitarReserva(login,reserva);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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

            // transfornma dat em string no formato dd/MM/yyyy usando função da classe DateUtils
            String dt = DateUtils.dateToString(year, monthOfYear, dayOfMonth);
            Date date = DateUtils.getDate(year, monthOfYear, dayOfMonth);

            //System.out.println(""+date.getTime());
            //System.out.println(""+atual.getTime());

            // verifia se data selecionado é maior ou igual ao atual, senão não é permitido reservar, pois já passou
            if(date.getTime() >= atual.getTime()){
                // se tipo de aula for mestrado ou maior, atualiaz periodo e não tem limite em relação a data
                if(spnSolicitarReservaTipoAula.getSelectedItemId() > 1){
                    edtSolicitarReservaData.setText(dt);
                    atualizaPeriodo();
                }
                // se for aula teórica ou prática, tem limite
                else{
                    // dateMax é a data limite (normalmente n+2)
                    Date dateMax;
                    // limite = nova data
                    dateMax = new Date();
                    dateMax.setTime(atual.getTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(atual);
                    // day é o dia entre 1-31
                    int day = cal.get(Calendar.DAY_OF_WEEK);

                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(date);
                    // diaSemana é o dia da semana 1-7 (domingo=1, seg=2, ..., sabado=7)
                    int diaSemana = c2.get(Calendar.DAY_OF_WEEK);

                    // se for docente, não pode reservar em domingo ou sabado, mostra erro
                    if(login.getPrivilegio() == 1){
                        if(diaSemana == 1){
                            MessageBox.show(SolicitarReservaActivity.this, getString(R.string.erro), getString(R.string.notAllowedRequestOn) + getString(R.string.domingo));
                            atualizaPeriodo();
                            return;
                        }
                        if(diaSemana == 7){
                            MessageBox.show(SolicitarReservaActivity.this, getString(R.string.erro), getString(R.string.notAllowedRequestOn) + getString(R.string.sabado));
                            atualizaPeriodo();
                            return;
                        }
                    }
                    // se for aula prática, o limite é até fim da semana n + 2, portanto soma 3 semanas e subtrai dia da semana
                    // atual que já passou. se for segunda, subtrai um dia util e assim vai
                    if(spnSolicitarReservaTipoAula.getSelectedItemId() == 0){
                        dateMax.setDate(atual.getDate()+21-day);
                    }
                    else{
                        // se for aula teórica em lab, o prazo é até semana n + 1
                        if(spnSolicitarReservaTipoSala.getSelectedItemId()==0){
                            dateMax.setDate(atual.getDate()+14-day);
                        }
                        // teórica em proj = n + 2
                        else{
                            dateMax.setDate(atual.getDate()+21-day);
                        }
                    }
                    // verificar a prioridade do curso, caso tenha disciplina
                    if(getDisciplinasValidas().size() != 0) {
                        Disciplina dicp1 = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());

                        // se for do departamento, limite permanesce mesmo, senão
                        // só pode reservar no dia atual e seguinte
                        Curso curso = getCurso(dicp1.getId_curso());
                        if(curso.getTipo()==2){
                            dateMax.setDate(atual.getDate()+2);
                        }

                    }

                    // se data estiver no limite
                    if(date.getTime() < dateMax.getTime()){
                        // verificar se tem disciplina, se tem, tem que ver se é do primeiro semestre=1,
                        // segundo=2 ou anbual=0. Se for do primeiro ou segundo, tem que ver se está no limite do semestre
                        // pela variavel do anoletivo

                        // primeiro verifica se tem uma disciplina válida
                        if(getDisciplinasValidas().size() != 0){
                            // se tem, pegar a disciplina
                            Disciplina  disciplina = getDisciplinasValidas().get(spnSolicitarReservaDisciplina.getSelectedItemPosition());

                            // definir os limites inicio e fim de cada semestre ou anual
                            java.util.Date dinic = new Date();
                            java.util.Date dfim = new Date();


                            DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
                            dformat.setLenient(false);

                            // se for do primeiro semestre
                            if(disciplina.getPeriodo()==1){
                                try{
                                    dinic = dformat.parse(anoLetivo.getIniciop());
                                    dfim = dformat.parse(anoLetivo.getFimp());
                                }
                                catch (Exception e12){
                                }

                            }
                            // se for do segundo semestre
                            if(disciplina.getPeriodo()==2){
                                try{
                                    dinic = dformat.parse(anoLetivo.getInicios());
                                    dfim = dformat.parse(anoLetivo.getFims());
                                }
                                catch (Exception e12){
                                }
                            }
                            // se for anual
                            if(disciplina.getPeriodo()==0){
                                try{
                                    dinic = dformat.parse(anoLetivo.getIniciop());
                                    dfim = dformat.parse(anoLetivo.getFims());
                                }
                                catch (Exception e12){
                                }
                            }

                            // aumentar um dia apra o fim (pode reservar para o ultimo dia do periodo)
                            dfim.setDate(dfim.getDate()+1);
                            /*
                            System.out.println(dinic.getTime());
                            System.out.println(dfim.getTime());
                            System.out.println(date.getTime());
                            */
                            if(date.getTime() < dinic.getTime()){
                                MessageBox.show(SolicitarReservaActivity.this,"Erro","Disciplina inválida nesse semestre inválido");
                                atualizaPeriodo();
                                return;
                            }
                            if(date.getTime() > dfim.getTime()){
                                MessageBox.show(SolicitarReservaActivity.this,"Erro","Disciplina inválida nesse semestre inválido");
                                atualizaPeriodo();
                                return;
                            }

                        }
                        //


                        // seta a data
                        edtSolicitarReservaData.setText(dt);
                        // dateProx é para verificar se semana é n + 2 ou não, se for n+2, semana = 2
                        Date dateProx;
                        dateProx = new Date();
                        dateProx.setTime(atual.getTime());
                        dateProx.setDate(atual.getDate()+14-day);
                        if(date.getTime() < dateProx.getTime()){
                            semana = 1;
                        }
                        else{
                            semana = 2;
                        }
                        atualizaPeriodo();
                    }
                    atualizaPeriodo();
                }
            }
        }
    }
}
