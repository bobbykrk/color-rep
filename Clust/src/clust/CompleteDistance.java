package clust;

import java.util.List;

/**
 * Klasa wyznaczająca całkowitą odległość między klasami
 * (odległość pomiędzy dowama najbardziej odległymi pikselami między grupami)
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
