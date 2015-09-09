
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
public class Package {
    private Integer sourceid;
    private Integer destid;
    private ArrayList<Integer> mincost;
    
    public Package(Integer sourceid, Integer destid, ArrayList<Integer> minscost) {
        this.sourceid = sourceid;
        this.destid = destid;
        this.mincost = minscost;
    }
    
    public Integer getSourceId() {
        return sourceid;
    }
    
    public Integer getDest() {
        return destid;
    }
    
    public ArrayList<Integer> getMinCostArray() {
        return mincost;
    }
    
    public Integer getCostTo(Integer i) {
        return mincost.get(i);
    }
}
