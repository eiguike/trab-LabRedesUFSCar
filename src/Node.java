
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
  
  public Node() {
  }
    
  public void rtinit(ArrayList<Integer> distance) {
    for (Integer i = 0; i < 4; i++) {
      mindist.add(distance.get(i));
    }
  }
    
  public void rtupdate(Package pkg) {
    for (int i = 0; i < 4; i++) {
      if (i != pkg.getSourceId()) {
        // Se estiver vendo a distancia entre os dois nodes que se comunicaram, pega o menor valor de distancia
        if (mindist.get(i).equals(-1) && mindist.get(pkg.getSourceId()) > pkg.getCostTo(i)) {
          mindist.set(pkg.getSourceId(), pkg.getCostTo(i));

          // Encontra a menor distancia até outro nó qualquer
        } else if (mindist.get(i) > pkg.getCostTo(i) + mindist.get(pkg.getSourceId())) {
          mindist.set(i, pkg.getCostTo(i) + mindist.get(pkg.getSourceId()));
        }
      }
    }
  }
}
