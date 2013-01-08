package clust;

import java.util.ArrayList;
import java.util.List;

/**
 * Zajmuje się wykonaniem grupowania DBScan
 */
public class DBScanClusters {
    int counter=0;
    int nClust;
    Color[] colors;
    public List<Color>[] clusts;
    boolean[] visited;
    Distance dist;
    
    /**
     * @param colors Tablica kolorów obrazka
     * @param dist Metryka odległości miedzy pikselami
     */
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
    
    /**
     * Główna funkcja algorytmu DBSCAN
     * @param eps maksymalna odległość pomiędzy punktami gęstościowo osiągalnymi
     * @param MinPts minimalna liczba punktów w sąsiedztwie wymagana do stworzenia klastra
     */
    public void DBSCAN (double eps, int MinPts)
    {
        for(int i=0;i<colors.length;i++){
            if (!visited[i])
            {
                visited[i]=true;
                counter--;
                //System.out.println("nieodwiedzonych: " + counter + "   NOWY KLASTER");
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
    
    /**
     * Funkcja pomocnicza algorytmu DBSCAN analizująca nowostworzony klaster
     * @param P indeks analizowanego pixela
     * @param NeighborPts lista pixeli sąsiadujących z analizowanym klastrem
     * @param nClust indeks analizowanego klastra
     * @param eps maksymalna odległość pomiędzy punktami gęstościowo osiągalnymi
     * @param MinPts minimalna liczba punktów w sąsiedztwie wymagana do stworzenia klastra
     */
    private void expandCluster (int P, List<Integer> NeighborPts, int nClust, double eps, int MinPts)
    {
        clusts[nClust].add((Color)colors[P]);
        for(int i=0;i<NeighborPts.size();i++){
            if (!visited[NeighborPts.get(i)])
            {
                visited[NeighborPts.get(i)]=true;
                counter--;
                //System.out.println("nieodwiedzonych: " + counter);
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
    
    /**
     * Funkcja pomocnicza dla analizującej klaster sprawdzająca czy na pixel znajduje się już w jakimś klastrze
     * @param P indeks spradzanego pixela
     * @return zwraca true jeżeli już jest w jakimś klastrze
     */
    private boolean checkInClusters (int P)
    {
        boolean ans;
        for(int i=0;i<nClust;i++){
            ans = clusts[i].contains(P);
            if (ans)
                return true;
        }
        return false;
    }
    
    /**
     * @param P indeks analizowanego pixela
     * @param eps promień sąsiedztwa
     * @return lista sąsiadów danego pixela w zadanym promieniu eps
     */
    private List<Integer> regionQuery(int P, double eps)
    {
        List<Integer> neighbours = new ArrayList<>();
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
