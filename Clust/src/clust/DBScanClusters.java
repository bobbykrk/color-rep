package clust;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcin
 */
public class DBScanClusters {
    int counter=0;
    int nClust;
    Color[] colors;
    public List<Color>[] clusts;
    boolean[] visited;
    Distance dist;
    
    @SuppressWarnings("unchecked")
    public DBScanClusters(Color[] colors, Distance dist){
    	clusts = (List<Color>[]) new ArrayList[colors.length];
    	clusts[0] = new ArrayList(); //SZUM
        visited = new boolean[colors.length];
        this.colors = new Color[colors.length];
        System.arraycopy( colors, 0, this.colors, 0, colors.length );
        counter=colors.length;
        for(int i=0;i<colors.length;i++)
            visited[i] = false;
        this.nClust = 0;
        this.dist = dist;
    }
    
    public void DBSCAN (double eps, int MinPts)
    {
    	for(int i=0;i<colors.length;i++){
    		if (!visited[i])
    		{
    			visited[i]=true;
    			counter--;
    			System.out.println("nieodwiedzonych: " + counter + "   NOWY KLASTER");
    			List<Integer> NeighborPts = (List<Integer>) regionQuery(i, eps);
    			if (NeighborPts.size() < MinPts){
    				//mark as NOISE
    				clusts[0].add(colors[i]);
    			}else
    			{
    				//make new cluster
    				nClust++;
    	            clusts[nClust] = new ArrayList();
    				expandCluster(i,NeighborPts,nClust,eps,MinPts);
    			}
    		}
    	}
    }

    private void expandCluster (int P, List<Integer> NeighborPts, int nClust, double eps, int MinPts)
    {
        clusts[nClust].add((Color)colors[P]);
        for(int i=0;i<NeighborPts.size();i++){
        	if (!visited[NeighborPts.get(i)])
        	{
        		visited[NeighborPts.get(i)]=true;
    			counter--;
    			System.out.println("nieodwiedzonych: " + counter);
        		//List<Integer> NeighborPtsTmp = (List<Integer>) regionQuery(i, eps);
    			List<Integer> NeighborPtsTmp = (List<Integer>) regionQuery(NeighborPts.get(i), eps);
        		if (NeighborPtsTmp.size() >= MinPts){
        			for(int j=0 ; j< NeighborPtsTmp.size();j++)
        				if (!NeighborPts.contains(NeighborPtsTmp.get(j)))
        					NeighborPts.add(NeighborPtsTmp.get(j));
        			NeighborPtsTmp.clear();
        		}
        	}
        	if (!checkInClusters(NeighborPts.get(i))){
        		clusts[nClust].add((Color)colors[NeighborPts.get(i)]);
        	}
        }	
    }
    
    private boolean checkInClusters (int P)
    {
    	boolean ans=false;
    	for(int i=0;i<nClust;i++){
    		ans = clusts[i].contains(P);
    		if (ans)
    			return true;
    	}
    	return false;
    }
    		
    private List<Integer> regionQuery(int P, double eps)
    {
    	List<Integer> neighbours = new ArrayList<Integer>();
    	double tmp;
    	for(int i=0;i<colors.length;i++){
            if(P!=i){
            	tmp = dist.compute(colors[P], colors[i]);
            	if(tmp <= eps)
            		neighbours.add(i);
            }
        }
    	return neighbours;
    }
}
