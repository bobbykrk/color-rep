/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

import java.util.List;

/**
 *
 * @author Robert
 */
public class CompleteDistance implements ClustDistance {

    @Override
    public double comupte(List<Color> a, List<Color> b, Distance dist) {
        double dst = 0.0,tmp;
        for(Color ca : a){
            for(Color cb : b){
                tmp = dist.compute(ca, cb);
                if(dst < tmp){dst = tmp;}
            }
        }
        return dst;
    }
    
}
