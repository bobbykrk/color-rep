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
public interface ClustDistance {
    public double comupte(List<Color> a, List<Color> b, Distance dist);
}
