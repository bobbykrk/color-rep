/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clust;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public KMeansClust(Distance dist) {
        this.dist = dist;
    }

    ///wczytaj tablice z wartosciami pikseli
    private void getPoints(BufferedImage image) {
        int pix, r, g, b, k = 0;
        Color[] colors = new Color[image.getHeight() * image.getWidth()];
        int width = image.getWidth();
        int height = image.getHeight();
        int test = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pix = image.getRGB(i, j);
                r = (pix >> 16) & 0xFF;
                g = (pix >> 8) & 0xFF;
                b = pix & 0xFF;
                colors[k++] = new Color(r, g, b);
                if (r > 0 || b > 0 || g > 0) {
                    test++;
                }
            }
        }
        this.points = colors;
    }

    //główny algorytm
    private Color[] kmeans(final int k, int maxNumberOfIterations) {
        Color[] centers = new Color[k];
        int numberOfIterations = 0;

        ///wylosuj poczatkowe wartości grup
        for (int i = 0; i < k; i++) {
            int n = Math.abs(rand.nextInt() % (points.length));
            centers[i] = points[n];
        }

        while (true) {
            maxNumberOfIterations--;
            numberOfIterations ++;
           
            Color[][] clusters = new Color[k][];

            for (int i = 0; i < k; i++) {
                clusters[i] = new Color[points.length];
            }

            ///zapisz piksele do najbliższych grup
            for (int j = 0; j < points.length; j++) {
                double smallestDist = Double.MAX_VALUE;
                int idx = 0;
                for (int i = 0; i < k; i++) {
                    double distance = dist.compute(points[j], centers[i]);
                    if (distance < smallestDist) {
                        smallestDist = distance;
                        idx = i;
                    }
                }
                clusters[idx][j] = points[j];
            }

            int numberOfConZeroDists = 0;

            ///oblicz nowe wartości średnie
            for (int i = 0; i < k; i++) {
                Color newCenter = calculateCenter(clusters[i]);

                double d = dist.compute(newCenter, centers[i]);
               // System.out.println("new center vs old: " + d);
               
                ///sprawdź czy wartość średnia grupy uległa zmianie
                if (0 == dist.compute(newCenter, centers[i])) {
               // if(newCenter.equals(centers[i])) {
                    numberOfConZeroDists++;
                } else {
                    numberOfConZeroDists = 0;
                }

                ///zakończ, jeżeli wartości średnie wszystkich grup nie zmieniły się
                if (numberOfConZeroDists == k /*|| maxNumberOfIterations <= 0*/) {
                    System.out.println("numberOfIterations: " + numberOfIterations);
                  //  System.out.println("new center: " + newCenter + "   i: " + i);
                    return centers;
                }
               // System.out.println("numberOfIterations: " + numberOfIterations);
                centers[i] = newCenter;
                
               // System.out.println("new center: " + newCenter + "   i: " + i);
            }
        }
    }

    ///oblicza nową wartość średnią grupy
    private Color calculateCenter(Color[] points) {
        long r = 0;
        long g = 0;
        long b = 0;
        int lenght = 0;

        for (Color p : points) {
            if (p != null) {
                lenght++;
                if(p.vec[0] > 0 || p.vec[1] > 0  || p.vec[2] > 0  )
                {
                r += p.vec[0];
                g += p.vec[1];
                b += p.vec[2];         
                }
            }
        }
        ///wypisz licznosc grupy
        //System.out.println("lenght: " + lenght);
        if (lenght > 0) {
            return new Color((int) r / lenght, (int) g / lenght, (int) b / lenght);
        }
        return this.points[Math.abs(rand.nextInt() % (points.length))];
    }
    
    
    ///funkcja pomocznicza wykonująca algorytm k-means dla n plików, 
    ///@param
    private static void doStuff(int numberOfGroups, int n, Distance dist ) throws IOException{
        int k = numberOfGroups;
        String filenameIn = "./images/icon_"+n+".jpg";
        //String filenameIn = "./images/big"+n+".jpg";
        BufferedImage image = ImageIO.read(new File(filenameIn));
        KMeansClust clusterer;
        clusterer = new KMeansClust(dist);
        int pix = 0;
        clusterer.getPoints(image);
        Color[] result = clusterer.kmeans(k, 200);

        int width = image.getWidth();
        int height = image.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int m;
                //m = (int)((double)(i*j*k))/((double)(width*height));
                //m = (i*j*k) / (width*height);
                m = (i * k) / width;
                //  System.out.println(" i: " + i + " j: " + j + " k: " + k +  " width*height " + (width*height) + " m " + m);
                pix = result[m].toInt();
                image.setRGB(i, j, pix);

            }
        }
        String filenameOut = "./images/out_czarek/icon_"+n+"__" + k + "_groups_" + dist.getClass().getSimpleName() +"_KMEANS.bmp";
        //String filenameOut = "./images/out_czarek/big"+n+"__" + k + "_groups_" + dist.getClass().getSimpleName() +"_KMEANS.bmp";
        ImageIO.write(image, "bmp", new File(filenameOut));
    }
    

    public static void main(String[] args) throws IOException {
        int numberOfFiles = 9;
//         int numberOfFiles = 3;
        int[] numberOfGroups = {3, 5, 7};
        for (int n = 1; n <= numberOfFiles; n++) {
            for (int r = 0; r < 3; r++) {
                doStuff(numberOfGroups[r], n, new EuclideanDistance() );
            }
        }
  
        //numberOfFiles = 3;
         for (int n = 1; n <= numberOfFiles; n++) {
            doStuff(5, n, new ManhatanDistance() );
        }
        
    }
}
