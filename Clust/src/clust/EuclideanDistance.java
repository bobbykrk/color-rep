package clust;

/**
 * Odległość euklidesowa
 */
public class EuclideanDistance implements Distance {

    @Override
    public double compute(Color a, Color b) {
        double ret = 0.0;
        for(int i=0;i<a.vec.length;i++){
            ret += (a.vec[i]-b.vec[i])*(a.vec[i]-b.vec[i]);
        }
        return Math.sqrt(ret);
    }
    
}
