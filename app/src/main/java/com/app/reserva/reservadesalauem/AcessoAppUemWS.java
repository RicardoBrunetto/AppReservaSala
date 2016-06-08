package com.app.reserva.reservadesalauem;


import android.content.Context;
import android.widget.ArrayAdapter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.app.reserva.reservadesalauem.dados.*;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Mamoru on 12/01/2016.
 */
public class AcessoAppUemWS {

    // para conexão com web service, precisa do URL dele
    private static final String URL = "http://din.uem.br/appsmoveis/services/AcessoAppUemWS?wsdl";
    // namespace do web service
    private static final String NAMESPACE="http://acesso.uemWS.appreserva.com.br";

    // nome das funções que tem no web service
    private static final String CADASTRARUSUARIO = "cadastrarUsuario";
    private static final String ALTERARUSUARIO = "alterarUsuario";
    private static final String REMOVEUSUARIO = "removeUsuario";
    private static final String CARREGARUSUARIO = "carregarUsuario";
    private static final String RECUPERARSENHA = "recuperarSenha";

    private static final String CONFIRMARLOGIN = "confirmarLogin";
    private static final String ALTERARSENHA = "alterarSenha";

    private static final String CARREGARDEPARTAMENTO = "carregarDepartamento";

    private static final String CARREGARSALA = "carregarSala";
    private static final String CADASTRARSALA = "cadastrarSala";
    private static final String ALTERARSALA = "alterarSala";
    private static final String REMOVESALA = "removeSala";

    private static final String CARREGARDISCIPLINA = "carregarDisciplina";
    private static final String CARREGARRESERVA = "carregarReserva";
    private static final String CARREGARANOLETIVO = "carregarAnoLetivo";

    private static final String SOLICITARRESERVA = "solicitarReserva";

    private static final String SOLICITARDATAATUAL = "solicitarDataAtual";

    private static final String CARREGARCURSO = "carregarCurso";

    ///
    //// As funções que tem tem nessa classe tem nome igual às funções que tem no Web Service e com os mesmos parametros
    /// thows Exception é para tratamento de excessao

    //NÃO SERÁ POSSÍVEL UTILIZAR AS STRINGS DO ARQUIVO strings.xml POR QUESTÕES DE CONTEXT

    // / tem 2 estruturas principais que estão sendo usadas para solicitação com o servidor
    // 1 - é a parte onde envia o login e um dado, para alteração ou cadastro de dado, ver função alterar senha para saber
    // seu funcionamento
    // 2 - são requisições que solicitam algum dado, por exemplo, carregarDepartamento, ver essa função

    // função para confirmar login, ela retorna o privilégio, se encontrar, senão, retorna -1
    public int confirmarLogin(Login login) throws Exception{

        // criar um soap object para encapsular os dados
        SoapObject soapConfirmarLogin = new SoapObject(NAMESPACE,CONFIRMARLOGIN);

        //para enviar um objeto, tem que encapsular dentro de soap
        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha",login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());

        // adicionar o soap user no soap que vai enviar
        soapConfirmarLogin.addSoapObject(user);

        // essa parte de baixo é tudo igual
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapConfirmarLogin);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);

        try{
            // envia o envelope com o soap object
            httpTrans.call("urn" + CONFIRMARLOGIN, envelope);
            // recebe resposta simples
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            // converte para inteiro e reorna
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return -1;
        }
    }

    // envair um login e uma nova senha para alteração
    public int alterarSenha(Login login, String senha) throws Exception{
        // criar soap que vai enviar
        SoapObject soapAlterarSenha = new SoapObject(NAMESPACE,ALTERARSENHA);

        // criar soap do objeto login
        SoapObject user = new SoapObject(NAMESPACE,"login");
        // adicionar dados no soap user (login)
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha",login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());

        // adicionar o soap usuario no soap que vai enviar
        soapAlterarSenha.addSoapObject(user);
        // como senha é uma variavel simples, adiciona diretamente, passando nome do parametro e dado
        soapAlterarSenha.addProperty(NAMESPACE, "senha", senha);

        // encapsula no envole
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapAlterarSenha);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            // envia ao servidor
            httpTrans.call("urn" + ALTERARSENHA, envelope);
            // recebe resposta simples
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            // converte para inteiro e retorna
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    // função que solicita recuperação se senha
    public int recuperarSenha(String email) throws Exception{
        // soap principal que será enviada
        SoapObject soapResuperarSenha = new SoapObject(NAMESPACE,RECUPERARSENHA);

        // como é variavel simples, adiciona diretamente no soap
        // se tiver que adicionar outro objeto, tipo, usuario, ou departamento, tem que criar
        // um novo soap object e adicionar dados nele
        soapResuperarSenha.addProperty(NAMESPACE,"email", email);

        // essa parte de baixo é só copiar
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapResuperarSenha);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            // envia ao servidor
            httpTrans.call("urn" + RECUPERARSENHA, envelope);
            // recebe resposta
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            //converte para inteiro e retorna
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return -1;
        }
    }

    // receber todos os departamentos do servidor
    public ArrayList<Departamento> carregarDepartamento() throws Exception{
        // criar lista vazia de departamento
        ArrayList<Departamento> listaDepartamento;
        listaDepartamento = new ArrayList<>();

        // cria objeto soap
        SoapObject soapcarregarDepartamento = new SoapObject(NAMESPACE,CARREGARDEPARTAMENTO);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarDepartamento);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            // envia solicitação
            httpTrans.call("urn" + CARREGARDEPARTAMENTO, envelope);
            // quando recebe um unico departamento
            try{
                //  converte para objeto
                SoapObject item = (SoapObject) envelope.getResponse();
                // cria departamento
                Departamento departamento = new Departamento();
                // converte todos os dados
                departamento.setId(Integer.parseInt(item.getProperty("id").toString()));
                departamento.setNome(item.getProperty("nome").toString());
                departamento.setDescricao(item.getProperty("descricao").toString());
                departamento.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                // adiciona na lista
                listaDepartamento.add(departamento);
            }
            //
            catch (Exception ex){
                //quando recebe não só um, mas uma lista como resposta
                try{
                    // recebe vetor
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    // cada item do vetor é um departamento, então tem que criar um novo departamento e converter cada
                    // dado de uma posicao do vetor no dado que quer
                    for (SoapObject item:resposta){
                        Departamento departamento = new Departamento();

                        departamento.setId(Integer.parseInt(item.getProperty("id").toString()));
                        departamento.setNome(item.getProperty("nome").toString());
                        departamento.setDescricao(item.getProperty("descricao").toString());
                        departamento.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                        // adiciona na lista
                        listaDepartamento.add(departamento);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaDepartamento;
    }

    // solicitar a lista de sals para o servidor
    public ArrayList<Sala> carregarSala() throws Exception{
        // lista de salas vazias
        ArrayList<Sala> listaSala;
        listaSala = new ArrayList<>();

        // criar soap object
        SoapObject soapcarregarSala = new SoapObject(NAMESPACE,CARREGARSALA);
        // encapsular em um envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarSala);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            // enviar solicitação ao servidor
            httpTrans.call("urn" + CARREGARSALA, envelope);
            // receber um único dado como resposta
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                // criar nova sala e converter o dado recebido
                Sala sala = new Sala();

                sala.setId(Integer.parseInt(item.getProperty("id").toString()));
                sala.setNumero(Integer.parseInt(item.getProperty("numero").toString()));
                sala.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                sala.setClassificacao(Integer.parseInt(item.getProperty("classificacao").toString()));
                sala.setDescricao(item.getProperty("descricao").toString());
                sala.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaSala.add(sala);
            }
            catch (Exception ex){
                // recebe um vetor de onjetos, para cada objeto, converter para sala e adicionar na lista
                try{
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject item:resposta){
                        Sala sala = new Sala();

                        sala.setId(Integer.parseInt(item.getProperty("id").toString()));
                        sala.setNumero(Integer.parseInt(item.getProperty("numero").toString()));
                        sala.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                        sala.setClassificacao(Integer.parseInt(item.getProperty("classificacao").toString()));
                        sala.setDescricao(item.getProperty("descricao").toString());
                        sala.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                        listaSala.add(sala);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaSala;
    }

    public int cadastrarSala(Login login, Sala sala) throws Exception{
        SoapObject soapCadastrarSala = new SoapObject(NAMESPACE,CADASTRARSALA);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapCadastrarSala.addSoapObject(user);

        SoapObject soapSala = new SoapObject(NAMESPACE,"sala");
        soapSala.addProperty("id",sala.getId());
        soapSala.addProperty("numero",sala.getNumero());
        soapSala.addProperty("id_departamento",sala.getId_departamento());
        soapSala.addProperty("classificacao",sala.getClassificacao());
        soapSala.addProperty("descricao",sala.getDescricao());
        soapSala.addProperty("status",sala.getStatus());

        soapCadastrarSala.addSoapObject(soapSala);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapCadastrarSala);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CADASTRARSALA, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public int alterarSala(Login login, Sala sala) throws Exception{
        SoapObject soapAlterarSala = new SoapObject(NAMESPACE,ALTERARSALA);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapAlterarSala.addSoapObject(user);

        SoapObject soapSala = new SoapObject(NAMESPACE,"sala");
        soapSala.addProperty("id",sala.getId());
        soapSala.addProperty("numero",sala.getNumero());
        soapSala.addProperty("id_departamento",sala.getId_departamento());
        soapSala.addProperty("classificacao",sala.getClassificacao());
        soapSala.addProperty("descricao",sala.getDescricao());
        soapSala.addProperty("status",sala.getStatus());

        soapAlterarSala.addSoapObject(soapSala);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapAlterarSala);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + ALTERARSALA, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public int removeSala(Login login, Sala sala) throws Exception{
        SoapObject soapRemoveSala = new SoapObject(NAMESPACE,REMOVESALA);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapRemoveSala.addSoapObject(user);

        SoapObject soapSala = new SoapObject(NAMESPACE,"sala");
        soapSala.addProperty("id",sala.getId());
        soapSala.addProperty("numero",sala.getNumero());
        soapSala.addProperty("id_departamento",sala.getId_departamento());
        soapSala.addProperty("classificacao",sala.getClassificacao());
        soapSala.addProperty("descricao",sala.getDescricao());
        soapSala.addProperty("status",sala.getStatus());

        soapRemoveSala.addSoapObject(soapSala);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapRemoveSala);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + REMOVESALA, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public ArrayList<Usuario> carregarUsuario() throws Exception{
        ArrayList<Usuario> listaUsuario;
        listaUsuario = new ArrayList<>();

        SoapObject soapcarregarUsuario = new SoapObject(NAMESPACE,CARREGARUSUARIO);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarUsuario);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CARREGARUSUARIO, envelope);
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                Usuario user = new Usuario();

                user.setId(Integer.parseInt(item.getProperty("id").toString()));
                user.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                user.setNome(item.getProperty("nome").toString());
                user.setEmail(item.getProperty("email").toString());
                user.setSenha(item.getProperty("senha").toString());
                user.setTelefone(item.getProperty("telefone").toString());
                user.setPermissao(Integer.parseInt(item.getProperty("permissao").toString()));
                user.setId_disciplinas(item.getProperty("id_disciplinas").toString());
                user.setProblema_locomocao(Integer.parseInt(item.getProperty("problema_locomocao").toString()));
                user.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaUsuario.add(user);
            }
            catch (Exception ex){
                try{
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject item:resposta){
                        Usuario user = new Usuario();
                        user.setId(Integer.parseInt(item.getProperty("id").toString()));
                        user.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                        user.setNome(item.getProperty("nome").toString());
                        user.setEmail(item.getProperty("email").toString());
                        user.setSenha(item.getProperty("senha").toString());
                        user.setTelefone(item.getProperty("telefone").toString());
                        user.setPermissao(Integer.parseInt(item.getProperty("permissao").toString()));
                        user.setId_disciplinas(item.getProperty("id_disciplinas").toString());
                        //System.out.println(item.getProperty("id_disciplinas").toString());
                        user.setProblema_locomocao(Integer.parseInt(item.getProperty("problema_locomocao").toString()));
                        user.setStatus(Integer.parseInt(item.getProperty("status").toString()));

                        listaUsuario.add(user);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaUsuario;
    }

    public int cadastrarUsuario(Login login, Usuario usuario) throws Exception{
        SoapObject soapCadastrarUsuario = new SoapObject(NAMESPACE,CADASTRARUSUARIO);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapCadastrarUsuario.addSoapObject(user);

        SoapObject user2 = new SoapObject(NAMESPACE,"usuario");
        user2.addProperty("id",usuario.getId());
        user2.addProperty("nome",usuario.getNome());
        user2.addProperty("email", usuario.getEmail());
        user2.addProperty("senha",usuario.getSenha());
        user2.addProperty("telefone",usuario.getTelefone());
        user2.addProperty("id_departamento",usuario.getId_departamento());
        user2.addProperty("id_disciplinas",usuario.getId_disciplinas());
        user2.addProperty("permissao", usuario.getPermissao());
        user2.addProperty("problema_locomocao",usuario.getProblema_locomocao());
        user2.addProperty("status",usuario.getStatus());
        soapCadastrarUsuario.addSoapObject(user2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapCadastrarUsuario);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CADASTRARUSUARIO, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public int alteraUsuario(Login login, Usuario usuario) throws Exception{
        SoapObject soapAlteraUsuario = new SoapObject(NAMESPACE,ALTERARUSUARIO);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapAlteraUsuario.addSoapObject(user);

        SoapObject user2 = new SoapObject(NAMESPACE,"usuario");
        user2.addProperty("id",usuario.getId());
        user2.addProperty("nome",usuario.getNome());
        user2.addProperty("email", usuario.getEmail());
        user2.addProperty("senha",usuario.getSenha());
        user2.addProperty("telefone",usuario.getTelefone());
        user2.addProperty("id_departamento",usuario.getId_departamento());
        user2.addProperty("id_disciplinas",usuario.getId_disciplinas());
        user2.addProperty("permissao", usuario.getPermissao());
        user2.addProperty("problema_locomocao",usuario.getProblema_locomocao());
        user2.addProperty("status",usuario.getStatus());
        soapAlteraUsuario.addSoapObject(user2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapAlteraUsuario);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + ALTERARUSUARIO, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public int removeUsuario(Login login, Usuario usuario) throws Exception{
        SoapObject soapRemoveUsuario = new SoapObject(NAMESPACE, REMOVEUSUARIO);

        SoapObject user = new SoapObject(NAMESPACE,"login");
        user.addProperty("id",login.getId());
        user.addProperty("email", login.getEmail());
        user.addProperty("senha", login.getSenha());
        user.addProperty("privilegio", login.getPrivilegio());
        soapRemoveUsuario.addSoapObject(user);

        SoapObject user2 = new SoapObject(NAMESPACE,"usuario");
        user2.addProperty("id",usuario.getId());
        user2.addProperty("nome",usuario.getNome());
        user2.addProperty("email", usuario.getEmail());
        user2.addProperty("senha",usuario.getSenha());
        user2.addProperty("telefone",usuario.getTelefone());
        user2.addProperty("id_departamento",usuario.getId_departamento());
        user2.addProperty("id_disciplinas",usuario.getId_disciplinas());
        user2.addProperty("permissao", usuario.getPermissao());
        user2.addProperty("problema_locomocao",usuario.getProblema_locomocao());
        user2.addProperty("status",usuario.getStatus());
        soapRemoveUsuario.addSoapObject(user2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapRemoveUsuario);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + REMOVEUSUARIO, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }

    public ArrayList<Disciplina> carregarDisciplina() throws Exception{
        ArrayList<Disciplina> listaDisciplina;
        listaDisciplina = new ArrayList<>();

        SoapObject soapcarregarDisciplina = new SoapObject(NAMESPACE,CARREGARDISCIPLINA);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarDisciplina);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CARREGARDISCIPLINA, envelope);
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                Disciplina disc = new Disciplina();

                disc.setId(Integer.parseInt(item.getProperty("id").toString()));
                disc.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                disc.setId_curso(Integer.parseInt(item.getProperty("id_curso").toString()));
                disc.setCodigo(Integer.parseInt(item.getProperty("codigo").toString()));
                disc.setPeriodo(Integer.parseInt(item.getProperty("periodo").toString()));
                disc.setTurma(Integer.parseInt(item.getProperty("turma").toString()));
                disc.setNome(item.getProperty("nome").toString());
                disc.setClassificacao(Integer.parseInt(item.getProperty("classificacao").toString()));
                disc.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaDisciplina.add(disc);
            }
            catch (Exception ex){
                try{
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject item:resposta){
                        Disciplina disc = new Disciplina();

                        disc.setId(Integer.parseInt(item.getProperty("id").toString()));
                        disc.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                        disc.setId_curso(Integer.parseInt(item.getProperty("id_curso").toString()));
                        disc.setCodigo(Integer.parseInt(item.getProperty("codigo").toString()));
                        disc.setPeriodo(Integer.parseInt(item.getProperty("periodo").toString()));
                        disc.setTurma(Integer.parseInt(item.getProperty("turma").toString()));
                        disc.setNome(item.getProperty("nome").toString());
                        disc.setClassificacao(Integer.parseInt(item.getProperty("classificacao").toString()));
                        disc.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                        listaDisciplina.add(disc);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaDisciplina;
    }

    public ArrayList<Reserva> carregarReserva() throws Exception{
        ArrayList<Reserva> listaReserva;
        listaReserva = new ArrayList<>();

        SoapObject soapcarregarReserva = new SoapObject(NAMESPACE,CARREGARRESERVA);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarReserva);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CARREGARRESERVA, envelope);
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                Reserva reserva = new Reserva();
                reserva.setId(Integer.parseInt(item.getProperty("id").toString()));
                reserva.setIddepartamento(Integer.parseInt(item.getProperty("iddepartamento").toString()));
                reserva.setIdusuario(Integer.parseInt(item.getProperty("idusuario").toString()));
                reserva.setTipoaula(Integer.parseInt(item.getProperty("tipoaula").toString()));
                reserva.setIddisciplina(Integer.parseInt(item.getProperty("iddisciplina").toString()));
                reserva.setTipo(Integer.parseInt(item.getProperty("tipo").toString()));
                reserva.setDataefetuacao(item.getProperty("dataefetuacao").toString());
                reserva.setProximoid(Integer.parseInt(item.getProperty("proximoid").toString()));
                reserva.setDatareserva(item.getProperty("datareserva").toString());
                reserva.setPeriodo(Integer.parseInt(item.getProperty("periodo").toString()));
                reserva.setTiposala(Integer.parseInt(item.getProperty("tiposala").toString()));
                reserva.setIdsala(Integer.parseInt(item.getProperty("idsala").toString()));
                reserva.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaReserva.add(reserva);
            }
            catch (Exception ex){
                try{
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();
                    for (SoapObject item:resposta){
                        Reserva reserva = new Reserva();

                        reserva.setId(Integer.parseInt(item.getProperty("id").toString()));
                        reserva.setIddepartamento(Integer.parseInt(item.getProperty("iddepartamento").toString()));
                        reserva.setIdusuario(Integer.parseInt(item.getProperty("idusuario").toString()));
                        reserva.setTipoaula(Integer.parseInt(item.getProperty("tipoaula").toString()));
                        reserva.setIddisciplina(Integer.parseInt(item.getProperty("iddisciplina").toString()));
                        reserva.setTipo(Integer.parseInt(item.getProperty("tipo").toString()));
                        reserva.setDataefetuacao(item.getProperty("dataefetuacao").toString());
                        reserva.setProximoid(Integer.parseInt(item.getProperty("proximoid").toString()));
                        reserva.setDatareserva(item.getProperty("datareserva").toString());
                        reserva.setPeriodo(Integer.parseInt(item.getProperty("periodo").toString()));
                        reserva.setTiposala(Integer.parseInt(item.getProperty("tiposala").toString()));
                        reserva.setIdsala(Integer.parseInt(item.getProperty("idsala").toString()));
                        reserva.setStatus(Integer.parseInt(item.getProperty("status").toString()));

                        listaReserva.add(reserva);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaReserva;
    }


    public int solicitarReserva(Login login, Reserva reserva) throws Exception{
        SoapObject soapSolicitarReserva = new SoapObject(NAMESPACE,SOLICITARRESERVA);

        SoapObject soapUser = new SoapObject(NAMESPACE,"login");
        soapUser.addProperty("id",login.getId());
        soapUser.addProperty("email", login.getEmail());
        soapUser.addProperty("senha", login.getSenha());
        soapUser.addProperty("privilegio", login.getPrivilegio());
        soapSolicitarReserva.addSoapObject(soapUser);

        SoapObject soapReserva = new SoapObject(NAMESPACE,"reserva");
        soapReserva.addProperty("id",reserva.getId());
        soapReserva.addProperty("iddepartamento",reserva.getIddepartamento());
        soapReserva.addProperty("idusuario",reserva.getIdusuario());
        soapReserva.addProperty("tipoaula",reserva.getTipoaula());
        soapReserva.addProperty("iddisciplina",reserva.getIddisciplina());
        soapReserva.addProperty("tipo",reserva.getTipo());
        soapReserva.addProperty("dataefetuacao",reserva.getDataefetuacao());
        soapReserva.addProperty("proximoid",reserva.getProximoid());
        soapReserva.addProperty("datareserva",reserva.getDatareserva());
        soapReserva.addProperty("periodo",reserva.getPeriodo());
        soapReserva.addProperty("tiposala",reserva.getTiposala());
        soapReserva.addProperty("idsala",reserva.getIdsala());
        soapReserva.addProperty("status",reserva.getStatus());
        soapSolicitarReserva.addSoapObject(soapReserva);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapSolicitarReserva);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + SOLICITARRESERVA, envelope);
            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(resposta.toString());
        }
        catch (Exception ex){
            return 0;
        }
    }


    public ArrayList<AnoLetivo> carregarAnoLetivo() throws Exception{
        ArrayList<AnoLetivo> listaA = new ArrayList<>();

        SoapObject soapcarregarSemestre = new SoapObject(NAMESPACE,CARREGARANOLETIVO);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarSemestre);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CARREGARANOLETIVO, envelope);
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                AnoLetivo al = new AnoLetivo();

                al.setId(Integer.parseInt(item.getProperty("id").toString()));
                al.setIddepartamento(Integer.parseInt(item.getProperty("iddepartamento").toString()));
                al.setIniciop(item.getProperty("iniciop").toString());
                al.setFimp(item.getProperty("fimp").toString());
                al.setInicios(item.getProperty("inicios").toString());
                al.setFims(item.getProperty("fims").toString());
                al.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaA.add(al);
            }
            catch (Exception ex){
                try{
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject item:resposta){
                        AnoLetivo al = new AnoLetivo();

                        al.setId(Integer.parseInt(item.getProperty("id").toString()));
                        al.setIddepartamento(Integer.parseInt(item.getProperty("iddepartamento").toString()));
                        al.setIniciop(item.getProperty("iniciop").toString());
                        al.setFimp(item.getProperty("fimp").toString());
                        al.setInicios(item.getProperty("inicios").toString());
                        al.setFims(item.getProperty("fims").toString());
                        al.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                        listaA.add(al);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaA;
    }

    // receber todos os departamentos do servidor
    public String solicitarDataAtual() throws Exception{
        // criar lista vazia de departamento
        String dataAtual = "";
        // cria objeto soap
        SoapObject soapcarregarDataAtual = new SoapObject(NAMESPACE,SOLICITARDATAATUAL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarDataAtual);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            // envia solicitação
            httpTrans.call("urn" + SOLICITARDATAATUAL, envelope);
            // quando recebe um unico departamento
            try{
                //  converte para objeto
                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
                dataAtual = resposta.toString();
            }
            //
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return dataAtual;
    }

    public ArrayList<Curso> carregarCurso() throws Exception{
        ArrayList<Curso> listaCurso;
        listaCurso = new ArrayList<>();

        SoapObject soapcarregarCurso = new SoapObject(NAMESPACE,CARREGARCURSO);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapcarregarCurso);
        envelope.implicitTypes = true;
        HttpTransportSE httpTrans = new HttpTransportSE(URL);
        try{
            httpTrans.call("urn" + CARREGARCURSO, envelope);
            try{
                SoapObject item = (SoapObject) envelope.getResponse();
                Curso curso = new Curso();

                curso.setId(Integer.parseInt(item.getProperty("id").toString()));
                curso.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                curso.setNome(item.getProperty("nome").toString());
                curso.setTipo(Integer.parseInt(item.getProperty("tipo").toString()));
                curso.setDescricao(item.getProperty("descricao").toString());
                curso.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                listaCurso.add(curso);
            }
            catch (Exception ex) {
                try {
                    Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject item:resposta){
                        Curso curso = new Curso();

                        curso.setId(Integer.parseInt(item.getProperty("id").toString()));
                        curso.setId_departamento(Integer.parseInt(item.getProperty("id_departamento").toString()));
                        curso.setNome(item.getProperty("nome").toString());
                        curso.setTipo(Integer.parseInt(item.getProperty("tipo").toString()));
                        curso.setDescricao(item.getProperty("descricao").toString());
                        curso.setStatus(Integer.parseInt(item.getProperty("status").toString()));
                        listaCurso.add(curso);
                    }
                }
                catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return listaCurso;
    }
}
