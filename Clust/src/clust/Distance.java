package clust;

/**
 * Interfejs do wyznaczania odległości między dwoma pikselami
 */
public interface Distance {
    public double compute(Color a, Color b);
}
