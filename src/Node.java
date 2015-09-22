
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiago
 */
public class Node {
	
	// para identificar qual processo que é
	private Integer pid;
	
	private ArrayList<Integer> mindist;
  private ArrayList<Boolean> connected;
	private Integer table[][];
	
	// variáveis necessárias para o funcionamento do socket do servidor
	private Socket serverSocket;
	private ObjectInputStream input;
	private ServerSocket server;
	private ArrayList<Package> message;
	
	// construtor
	public Node(Integer pid, ArrayList<Integer> distance) {
    this.pid = pid;
    this.table = new Integer[4][4];
    
    message = new ArrayList<Package>();
    
    connected = new ArrayList<>();
    
    mindist = distance;
      
    server();
    startMessageHandler();
    rtinit();
	}
  
  public void rtinit() {
		for (Integer i = 0; i < 4; i++) {
			// Cria a tabela de distancias
			// Define a distancia para ou atraves do proprio node como -1
			if (i.equals(pid)) {
				for (Integer j = 0; j < 4; j++) {
					table[i][j] = -1;
					table[j][i] = -1;
				}
        connected.add(Boolean.FALSE);
				
			// Se possuir ligacao direta com o node define a distancia minima como sendo o valor da distancia direta
			} else if (!mindist.get(i).equals(999)) {
				table[i][i] = mindist.get(i);
        connected.add(Boolean.TRUE);
				
			// Se nao possuir ligacao direta com o node, define a distancia atraves de tal node como infinita (999)
			} else {
				for (Integer j = 0; j < 4; j++) {
            if (table[j][i] == null) {
              table[j][i] = 999;
            }
        }
        connected.add(Boolean.FALSE);
			}
		}
	}
	
	public boolean rtupdate(Package pkg) {
		boolean mindistUpdated = false;
		for (int i = 0; i < 4; i++) {
			if (i != pkg.getSourceId() && !mindist.get(i).equals(-1)) {
        if (table[i][pkg.getSourceId()] == null || table[i][pkg.getSourceId()] > pkg.getCostTo(i) + table[pkg.getSourceId()][pkg.getSourceId()]) {
					table[i][pkg.getSourceId()] = pkg.getCostTo(i) + mindist.get(pkg.getSourceId());
          if (mindist.get(i) > pkg.getCostTo(i) + table[pkg.getSourceId()][pkg.getSourceId()]) {
            mindist.set(i, pkg.getCostTo(i) + table[pkg.getSourceId()][pkg.getSourceId()]);
            mindistUpdated = true;
          }
				}
			}
		}
		
		return mindistUpdated;
	}
	
	public void server() {
		(new Thread() {
			@Override
			public void run() {
				try {
					server = new ServerSocket(8000+pid);
					do {

						serverSocket = server.accept();
            try {
							input = new ObjectInputStream(serverSocket.getInputStream());
						} catch(IOException ex){
							System.out.println(ex);
						}
						synchronized(message) {
							try{
								message.add((Package) input.readObject());
							} catch (ClassNotFoundException ex) {
								Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
							}
              message.notify();
						}
					} while(true);
				} catch (IOException ex) {
					System.out.println(ex);
				}
			}
		}).start();
	}
  
  // função que trata as mensagens recebidas
	public void startMessageHandler(){
		(new Thread(){
			@Override
			public void run() {	
				Package aux;
				do {
					aux = null;
					// acesso atômico da variável message
					synchronized (message){
						if(!message.isEmpty()) {
							aux = message.remove(0);
              System.out.println("node" + pid + ": mensagem de node" + aux.getSourceId() + " recebida");
						} else {
							try {
                System.out.println("node" + pid + ": waiting");
								message.wait();
							} catch(InterruptedException e) {
								System.out.println(e);
							}
						}
					}
					if(aux != null){
						// aqui deve-se verificar se rtupdate é true
						// caso for true, deve-se enviar mensagem para
						// todos os nós conectados
						if(rtupdate(aux) == true){
							sendMessages();
						}
					}
				} while(true);
			}

		}).start();
	}
  
  public void sendMessages() {
    Client auxClient;
    
    for(Integer i = 0; i < mindist.size(); i++) {
			// se estiver conectado, enviará
			if(connected.get(i).equals(Boolean.TRUE)) {
        System.out.println("Mandando mensagem de node" + pid + " para node" + i);
				auxClient = new Client(pid, i, mindist);
        auxClient.start();
      }
		}
  }
  
  public void printTable() {
    System.out.println("Tabela do Node" + pid);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        System.out.print(table[i][j] + "  ");
      }
      System.out.println();
    }
  }
	
	public void toLayer2(){
		
	}
	
}
