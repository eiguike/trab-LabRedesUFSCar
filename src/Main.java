
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thiago
 */
public class Main {
  
  public static void main(String[] args) {
    Node node0, node1, node2, node3;
  
    ArrayList<Integer> distance0 = new ArrayList<>();
    distance0.add(-1);
    distance0.add(1);
    distance0.add(3);
    distance0.add(7);
  
    ArrayList<Integer> distance1 = new ArrayList<>();
    distance1.add(1);
    distance1.add(-1);
    distance1.add(1);
    distance1.add(999);
  
    ArrayList<Integer> distance2 = new ArrayList<>();
    distance2.add(3);
    distance2.add(1);
    distance2.add(-1);
    distance2.add(2);
  
    ArrayList<Integer> distance3 = new ArrayList<>();
    distance3.add(7);
    distance3.add(999);
    distance3.add(2);
    distance3.add(-1);
    
    System.out.println("Criando nodes");
    node0 = new Node(0, distance0);
    node1 = new Node(1, distance1);
    node2 = new Node(2, distance2);
    node3 = new Node(3, distance3);
    
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    node0.sendMessages();
    node1.sendMessages();
    node2.sendMessages();
    node3.sendMessages();
    
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    node0.printTable();
    node1.printTable();
    node2.printTable();
    node3.printTable();
  }
}
