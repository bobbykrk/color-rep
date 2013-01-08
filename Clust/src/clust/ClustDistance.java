package clust;

import java.util.List;

/**
 * Intefejs dla  klas odległości międzyklastrowych
 */
public interface ClustDistance {
    public double comupte(List<Color> a, List<Color> b, Distance dist);
}
