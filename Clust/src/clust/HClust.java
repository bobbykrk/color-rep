/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Robert
 */
public class HClust {

    Distance dist;
    
    public HClust(Distance dist){
        this.dist = dist;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        HClust hc = new HClust(new EuclideanDistance());
        BufferedImage image = ImageIO.read(new File("images/icon.jpg"));
        int pix,r,g,b,k=0;
        Color[] colors = new Color[image.getHeight()*image.getWidth()];
        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                pix = image.getRGB(i, j);
                r = (pix >> 16) & 0xFF;
                g = (pix >> 8) & 0xFF;
                b = pix & 0xFF;
                colors[k++] = new Color(r,g,b);
            }
        }
        Clusters clust = new Clusters(colors, hc.dist, new CompleteDistance());
        int nClust = 3, imgSize = image.getHeight()*image.getWidth();
        clust.compute(nClust);
        Color reps[] = new Color[nClust];
        k=0;
        for(int i=0;i< clust.clusts.length;i++){
            if(!clust.clusts[i].isEmpty()){
                reps[k++] = hc.findRep(clust.clusts[i]);
            }
        }
        imgSize /= k;
        int c=0, rep = 0;
        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                while(clust.clusts[c].isEmpty()){
                    c++;
                }
                pix = reps[rep/(imgSize+1)].toInt();//clust.clusts[c].get(0).toInt();
                clust.clusts[c].remove(0);
                image.setRGB(i,j,pix);
                rep++;
            }
        }
        ImageIO.write(image, "jpg", new File("images/icon_p.jpg"));
    }
    
    public Color findRep(List<Color> colors){
        double r=0.0,g=0.0,b=0.0,dst = Double.POSITIVE_INFINITY,tmp;
        double s = (double)colors.size();
        Color ret = new Color(0,0,0);
        for(Color c : colors){
            r += c.vec[0];
            g += c.vec[1];
            b += c.vec[2];
        }
        r /= s;
        g /= s;
        b /= s;
        Color crep = new Color((int)r,(int)g,(int)b);
        for(Color c : colors){
            tmp = dist.compute(c, crep);
            if(tmp < dst){dst = tmp; ret = c;}
        }
        return ret;
    }
}
