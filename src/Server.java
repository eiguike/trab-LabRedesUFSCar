import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class Server extends Thread{
  // Tabela de distancias
  private int table[][];

  // Informações para o ServerSocket
  private Thread t;
  private ServerSocket server = null;
  private Socket client = null;
  private ObjectInputStream input = null;
  private Integer pid;
  private Package pkg;

  // Filas de mensagens recebidas e das que serão
  // enviadas
  private ArrayList<Package> queueMsgRcvd;
  private ArrayList<Package> queueMsgSnt;


  // Número de Nós ativos
  private Integer processNum;
  
  // Node relativo ao server
  private Node node;

  // Construtor do Servidor
  Server(Integer pid, Node node){
    this.pid = pid;
    this.node = node;
  }

  public void start(){
    if(t == null){
      t = new Thread(this, "node" + pid);
      t.start();
    }
  }

  public void run(){
    try{
      server = new ServerSocket(8000 + pid); // não esquecer de somar com a porta
      
      node.rtinit();

      // loop que trata todas as mensagens recebidas
      do{
        // Servidor esta pronto para receber mensagens
        client = server.accept();

        // Ativa o fluxo (pipe) da conexão
        try{
          input = new ObjectInputStream(client.getInputStream());
        }catch(IOException ex){
          System.out.println(ex);
        }

        // Lê a stream e transforma em objeto
        synchronized(queueMsgRcvd){
          try{
            queueMsgRcvd.add((Package) input.readObject());
          }catch (IOException ex){
            System.out.println(ex);
          }catch (ClassNotFoundException ex){
            System.out.println(ex);
          }
          queueMsgRcvd.notify();
        }
      }while(true);
    }catch (IOException ex){
      System.out.println(ex);
    }
  }
}
