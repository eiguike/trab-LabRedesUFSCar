
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
	private Integer table[][];
	
	// variáveis necessárias para o funcionamento do socket do servidor
	private Socket serverSocket;
	private ObjectInputStream input;
	private ServerSocket server;
	private ArrayList<Package> message;
	
	// construtor
	public Node(ArrayList<Integer> distance, ArrayList<Integer> pidConnected,Integer pid) {
		this.table = new Integer[4][4];
		mindist = distance;
		this.pid = pid;

		server();
		startMessageHandler();
	}

	// função que trata as mensagens recebidas
	public void startMessageHandler(){
		(new Thread(){
			@Override
			public void run(){	
				Package aux = null;
				Client auxClient;
				Integer i;
				do{
					aux = null;
					// acesso atômico da variável message
					synchronized (message){
						if(!message.isEmpty()){
							aux = message.remove(0);
						}else{
							try{
								message.wait();
							}catch(InterruptedException e){
								System.out.println(e);
							}
						}
					}
					if(aux != null){
						// aqui deve-se verificar se rtupdate é true
						// caso for true, deve-se enviar mensagem para
						// todos os nós conectados
						if(rtupdate(aux) == true){
							for(i=0;i<mindist.size();i++){
								// se estiver conectado, enviará
								if(mindist.get(i) != 999)
									auxClient = new Client(pid, i, mindist);
							}
						}
					}
				}while(true);
			}

		}).start();
	}
	
	public void server(){
		(new Thread(){
			private int pid;
			@Override
			public void run(){
				try{
					server = new ServerSocket(8000+this.pid);
					do{
						serverSocket = server.accept();
						try{
							input = new ObjectInputStream(serverSocket.getInputStream());
						}catch(IOException ex){
							System.out.println(ex);
						}
						synchronized(message){
							try{
								message.add((Package) input.readObject());
							} catch (ClassNotFoundException ex) {
								Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}while(true);
				}catch (IOException ex){
					System.out.println(ex);
				}
			}
		}).start();
	}
	
	public void toLayer2(){
		
	}
	
	public void rtinit() {
		for (Integer i = 0; i < 4; i++) {
			// Cria a tabela de distancias
			// Define a distancia para ou atraves do proprio node como -1
			if (mindist.get(i).equals(-1)) {
				for (Integer j = 0; j < 4; j++) {
					table[i][j] = -1;
					table[j][i] = -1;
				}
				
			// Se possuir ligacao direta com o node define a distancia minima como sendo o valor da distancia direta
			} else if (!mindist.get(i).equals(999)) {
				table[i][i] = mindist.get(i);
				
			// Se nao possuir ligacao direta com o node, define a distancia atraves de tal node como infinita (999)
			} else {
				for (Integer j = 0; j < 4; j++) {
					if (table[j][i] != -1) {
						table[j][i] = 999;
					}
				}
			}
		}
	}
	
	public boolean rtupdate(Package pkg) {
		boolean tableUpdated = false;
		for (int i = 0; i < 4; i++) {
			if (i != pkg.getSourceId() && !mindist.get(i).equals(-1)) {
				// Encontra a menor distancia até outro nó qualquer através do remetente do pacote recebido
				if (mindist.get(i) > pkg.getCostTo(i) + mindist.get(pkg.getSourceId())) {
					mindist.set(i, pkg.getCostTo(i) + mindist.get(pkg.getSourceId()));
					table[i][pkg.getSourceId()] = pkg.getCostTo(i) + mindist.get(pkg.getSourceId());
					// quando houver de fato uma mudança na tabela de distância
					// é definido uma flag para que seja feito o envio da tabela
					// para os outros nós conectados
					tableUpdated = true;
				}
			}
		}
		
		return tableUpdated;
	}
}
