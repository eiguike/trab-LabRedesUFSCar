
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thiago
 */
public class Node {
  
  private ArrayList<Integer> mindist;
  private Integer table[][];
  
  public Node(ArrayList<Integer> distance) {
    this.table = new Integer[4][4];
    mindist = distance;
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
          tableUpdated = true;
        }
      }
    }
    
    return tableUpdated;
  }
}
