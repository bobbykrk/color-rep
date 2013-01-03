/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author czarek
 */
public class KMeansClust {
   
    private final Distance dist;
    private Color[] points; 
    Random rand = new Random(System.currentTimeMillis());
    
     public KMeansClust(Distance dist){
        this.dist = dist;  
    }
   
     private  void getPoints( BufferedImage image)
     {
        int pix,r,g,b,k=0;
        Color[] colors = new Color[image.getHeight()*image.getWidth()];
        int width = image.getWidth();
        int height = image.getHeight();
        int test=0;
        for(int i=0;i<width ;i++){
            for(int j=0;j<height ;j++){
                pix = image.getRGB(i, j);
                r = (pix >> 16) & 0xFF;
                g = (pix >> 8) & 0xFF;
                b = pix & 0xFF;
                colors[k++] = new Color(r,g,b);
                if(r>0 ||  b>0 || g>0)
                {
                    test++;
                }
            }      
        }
            this.points = colors;
     }
     
     private Color[]  kmeans( final int k, int maxNumberOfIterations)
     {           
         Color [] centers  = new Color[k];    
         Set<Color> colors = new HashSet<Color>();
         
         ///@todo zrob z tego opcje
//         for(int i = 0; i< k; i++)
//         {
//             centers[i] = new Color(Math.abs( rand.nextInt() % 256), Math.abs( rand.nextInt() % 256) , Math.abs( rand.nextInt() % 256));
//          }   
         
         for(int i = 0; i< k; i++)
         {      
             int n = Math.abs( rand.nextInt() % (points.length));
             centers[i] = points[n];
          }
         
         while(true)
         {
             maxNumberOfIterations--;
            Color[][] clusters = new Color [k][];

            for(int i = 0; i< k; i++)            
            {
              clusters[i] = new Color[points.length];
            }

            for(int j = 0 ; j< points.length ; j++)
            {
                double smallestDist = Double.MAX_VALUE;
                int idx = 0;
                 for(int i = 0; i< k; i++)
                 {
                     double distance = dist.compute(points[j], centers[i]);
                       if (distance < smallestDist)
                       {
                           smallestDist = distance;
                           idx = i;
                       }
                 }
                 clusters[idx][j] = points[j];
            }

            int numberOfConZeroDists = 0;
                    
            for(int i = 0; i< k; i++)
            {
                 Color newCenter = calculateCenter(clusters[i], k);
                 //@todo sprawdzać wszystkie 3 klastry
                 double d = dist.compute(newCenter, centers[i]);
                 System.out.println("new center vs old: " + d);
                 if ( 0 == dist.compute(newCenter, centers[i]))
                {
                    numberOfConZeroDists++;
                }
                 else
                 {
                     numberOfConZeroDists = 0;
                 }
                 
 

                 if (numberOfConZeroDists == k || maxNumberOfIterations <= 0)
                 {
                    
                     return centers;
                 }
                 
               centers[i] = newCenter;
               System.out.println("new center: " + newCenter);
                 
            }
        }     
     }
     
    private Color calculateCenter(Color[] points, int k)
    {
        long r = 0;
        long g = 0;
        long b = 0;
        int lenght = 0;
        for(Color p : points)
        {
            if(p != null)
            {
                lenght++;
                if(p.vec[0] > 0 || p.vec[1] > 0  || p.vec[2] > 0  )
                {
                    r += p.vec[0];           
                    g += p.vec[1];
                    b += p.vec[2];                 
                }
            }
        }
         System.out.println("lenght: " + lenght);
         if(lenght > 0)
         {
             return new Color((int)r/lenght, (int)g/lenght, (int)b/lenght);
         }
         //return new Color(Math.abs( rand.nextInt() % 256), Math.abs( rand.nextInt() % 256) , Math.abs( rand.nextInt() % 256));
        return this.points[Math.abs( rand.nextInt() % (points.length))];
    }
    
        public static void main(String[] args) throws IOException {
        HClust hc = new HClust(new EuclideanDistance());
        BufferedImage image = ImageIO.read(new File("./images/ref1.jpg"));
         
        KMeansClust clusterer = new KMeansClust(new EuclideanDistance());
        int k =3, pix =0;
        clusterer.getPoints(image);
        Color[] result = clusterer.kmeans(k, 20);
        int n=0;
        int width = image.getWidth();
        int height = image.getHeight();
        
        for (int m = 0; m< k; m++)
        {
            for(int i= (width / k) * m ; i<(width / k) * (m +1);i++){
               for(int j=0;j<height ;j++){

                  // pix = reps[rep/(imgSize+1)].toInt();//clust.clusts[c].get(0).toInt();
                   pix = result[m].toInt();
                   image.setRGB(i,j,pix);
                   n++;
               }
            }
        }
        
        ImageIO.write(image, "jpg", new File("./images/out/icon_KMEANS.png"));

    }
}
