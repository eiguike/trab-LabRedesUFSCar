
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
public class Node0 extends Node {
    
  public Node0() {
    rtinit0();
  }
    
  private void rtinit0() {
    ArrayList<Integer> mindist = new ArrayList<>();
    mindist.add(-1);
    mindist.add(1);
    mindist.add(3);
    mindist.add(7);
    rtinit(mindist);
  }
    
  public void rtupdate0(Package pkg) {
    rtupdate(pkg);
  }
    
}
