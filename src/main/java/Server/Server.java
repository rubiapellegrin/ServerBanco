/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Estados;
import util.Status;
import util.Mensagem;

/**
 *
 * @author rubia
 */
public class Server {

    private ServerSocket serverSocket;
    public void criaServerSocket(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
    }

    public Socket esperaConexao() throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }

    public void fechaSocket(Socket socket) throws IOException {
        socket.close();

    }

    private void tratarConexao(Socket socket) throws IOException, ClassNotFoundException {
        float saldo = (float) 5.4;
        String operacao = null;
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            while (!"SAIR".equals(operacao)){
            System.out.println("Tratando..");
            Mensagem m,reply = null;
            
            String conta;
            Double valor;

            m = (Mensagem) input.readObject();

            operacao = (String) m.getOperacao();

            reply = new Mensagem(operacao + "REPLY");

            switch (operacao) {
                case "CONSULTA":
                    conta = (String) m.getParam("Conta");

                    if (conta == null) {
                        reply.setStatus(Status.PARAMERROR);
                    } else if (conta.equals("rubia")) {
                        reply.setParam("mensagem", "\nSaldo atual da conta  " + conta
                                + " : " + saldo);
                    } else {
                        reply.setParam("mensagem", "Conta não encontrada! ");
                    }
                    break;
                case "DEPOSITO":
                    conta = (String) m.getParam("Conta");
                    valor = (Double) m.getParam("Valor");

                    if (conta == null) {
                        reply.setStatus(Status.PARAMERROR);
                    } else if (conta.equals("rubia")) {
                        float saldoAnterior = saldo;          
                        saldo += valor;
                        reply.setParam("mensagem", "\nSaldo anterior:  " + saldoAnterior
                                + "\nSaldo atual : " + saldo);
                    } else {
                        reply.setParam("mensagem", "Conta não encontrada! ");
                    }
                    break;
                case "SAQUE":
                    conta = (String) m.getParam("Conta");
                    valor = (Double) m.getParam("Valor");

                    if (conta == null) {
                        reply.setStatus(Status.PARAMERROR);
                    } else if (conta.equals("rubia")) {
                        float saldoAnterior = saldo;          
                        saldo -= valor;
                        reply.setParam("mensagem", "\nSaldo anterior:  " + saldoAnterior
                                + "\nSaldo atual : " + saldo);
                    } else {
                        reply.setParam("mensagem", "Conta não encontrada! ");
                    }
                    break;
                case "tchau":
                    output.writeObject(reply);
                    output.flush();

                    input.close();
                    output.close();
                    break;
                }
            output.writeObject(reply);
            }
            
            output.flush();

            input.close();
            output.close();
        } catch (IOException ex) {
            System.out.println("Problema no tratamento da conexao com o cliente.. " + socket.getInetAddress());
            System.out.println("Erro: " + ex.getMessage());
        } finally {
            fechaSocket(socket);
        }
    }

    public static void main(String[] args) {

        try {

            Server server = new Server();
            System.out.println("Aguardando conexao..");
            server.criaServerSocket(5555);
            while (true) {
                Socket socket = server.esperaConexao();
                System.out.println("Cliente conectado!");
                server.tratarConexao(socket);
                System.out.println("Cliente finalizado!\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro no cast: " + ex.getMessage());
        }

    }
}
