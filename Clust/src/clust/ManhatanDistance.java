package clust;

/**
 *
 * @author czarek
 */
public class ManhatanDistance implements Distance{
    
    @Override
    public double compute(Color a, Color b) {
        double ret = 0.0;
        for(int i=0;i<a.vec.length;i++){
            ret += Math.abs(a.vec[i]-b.vec[i]);
        }
        return ret;
    }
}
