/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

/**
 *
 * @author Robert
 */
public class Color {
    double[] vec;
    public Color(int r, int g, int b){
        vec = new double[3];
        vec[0] = r;
        vec[1] = g;
        vec[2] = b;
    }
    
    public int toInt(){
        java.awt.Color c = new java.awt.Color((int) vec[0],(int) vec[1],(int) vec[2],255);
        int ret = 0xff000000;
        ret |= (int)vec[0] << 16;
        ret |= (int)vec[1] << 8;
        ret |= (int)vec[2];
        return c.getRGB();
    }
}
