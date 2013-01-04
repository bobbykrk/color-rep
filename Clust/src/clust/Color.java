/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

/**
 *
 * @author Robert
 */
public class Color implements Comparable<Color> {
    double[] vec;
    public Color(int r, int g, int b){
        vec = new double[3];
        vec[0] = r;
        vec[1] = g;
        vec[2] = b;
    }
    
    public Color(){
        vec = new double[3];
        vec[0] = -1.0;
        vec[1] = -1.0;
        vec[2] = -1.0;
    }
    
    public int toInt(){
        java.awt.Color c = new java.awt.Color((int) vec[0],(int) vec[1],(int) vec[2],255);
        int ret = 0xff000000;
        ret |= (int)vec[0] << 16;
        ret |= (int)vec[1] << 8;
        ret |= (int)vec[2];
        return c.getRGB();
    }

    @Override
    public int compareTo(Color o) {
        if(this.vec[0]  ==  o.vec[0] && this.vec[1]  ==  o.vec[1] && this.vec[2]  ==  o.vec[2] )
        {
            return 1;
        }
        return 0;
    }
    
    @Override
    public String toString()
    {
        return "r: " + vec[0] + " g: " + vec[1] + " b: " + vec[2];
    }
    
}
