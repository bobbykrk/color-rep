/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
public class Clusters {
    
    int nClust;
    Color[] colors;
    List<Color>[] clusts;
    Distance dist;
    ClustDistance cdist;
    
    public Clusters(Color[] colors, Distance dist, ClustDistance cdist){
        clusts = (List<Color>[]) new ArrayList[colors.length];
        for(int i=0;i<colors.length;i++){
            clusts[i] = new ArrayList();
            clusts[i].add(colors[i]);
        }
        this.nClust = colors.length;
        this.dist = dist;
        this.cdist = cdist;
    }
    
    
    public void connect(){
        int a = -1,b = -1;
        double dst = Double.POSITIVE_INFINITY,tmp;
        for(int i=0;i<clusts.length;i++){
            if(!clusts[i].isEmpty()){
                for(int j=i+1;j<clusts.length;j++){
                    if(!clusts[j].isEmpty()){
                        tmp = cdist.comupte(clusts[i], clusts[j], dist);
                        if(tmp < dst){
                            a = i; b = j;
                            dst = tmp;
                        }
                    }
                }
            }
        }
        if(a>=0 && b >=0){
            if(clusts[a].size() < clusts[b].size()){
                clusts[b].addAll(clusts[a]);
                clusts[a].clear();
            }
            else{
                clusts[a].addAll(clusts[b]);
                clusts[b].clear();
            }
            nClust--;
        }   
    }
    
    public void compute(int n){
        while(nClust>n){
            connect();
            System.out.println("liczba grup: " + nClust);
        }
    }
    
}
