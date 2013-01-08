package clust;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.*;

/**
 *
 * @author Marcin
 */
public class DBScan {
    
    Distance dist;
    
    public DBScan(Distance dist){
        this.dist = dist;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        DBScan dbs = new DBScan(new EuclideanDistance());
        //DBScan dbs = new DBScan(new ManhatanDistance());
        String fileIn = "images/icon_9.jpg";
        String fileOut = "images/out_KMEANS/icon_9b/icon_9_out";
        PrintWriter myout = new PrintWriter(new FileWriter(fileOut+".txt"),true);
        BufferedImage image = ImageIO.read(new File(fileIn));
        
        double eps = 5;
        int minPts = 10;
        double eps1 = 0.500, eps2 = 20.000, epsSkip = 0.50000000000000000;
        int MinPts1 = 1, MinPts2 = 15, MinPtsSkip = 1, epsDiv=100;
        int best[][] = new int[7][2];
        
        int pix,r,g,b,k=0;
        Color[] colors = new Color[image.getHeight()*image.getWidth()];
        
        //wczytanie danych o kolorach
        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                pix = image.getRGB(i, j);
                r = (pix >> 16) & 0xFF;
                g = (pix >> 8) & 0xFF;
                b = pix & 0xFF;
                colors[k++] = new Color(r,g,b);
            }
        }
        
        for(minPts=MinPts1;minPts<=MinPts2;minPts+=MinPtsSkip)
        {
            for(eps=eps1;eps<=eps2;eps+=epsSkip)
            {
                DBScanClusters clust = new DBScanClusters(colors, dbs.dist);
                int nClust = 3, imgSize = image.getHeight()*image.getWidth();
                clust.DBSCAN(eps,minPts);
                nClust=clust.nClust;
                System.out.println("minPts= " + minPts + " eps= "+eps);
                System.out.println("liczba klastrów " + nClust);
                    
                Color reps[] = new Color[nClust+1];
                k=0;
                for(int i=1;i<= nClust;i++){
                    if(!clust.clusts[i].isEmpty()){
                        reps[k++] = dbs.findRep(clust.clusts[i]);
                        int tmp = clust.clusts[i].size();
                        //System.out.println("wielkosc klastra: " + clust.clusts[i].size());
                        boolean check = true;
                        for(int j=0;j<7;++j)
                        {
                            if (tmp > best[j][0] && check)
                            {
                                check = false;
                                for(int l=6;l>j;l--)
                                {
                                    best[l]=best[l-1].clone();
                                }
                                best[j]=new int[]{tmp,i-1};
                            }
                        }
                    }
                }
                myout.write(fileOut+"_"+eps+"_"+minPts+"_"+nClust+".bmp"+"\n");
                myout.write("eps="+eps+" minPts="+minPts+" nClust="+nClust+"\n");
                for (int i=0;i<7;i++)
                {
                    //myout.write("testing");
                    myout.write("Liczba: "+best[i][0]+"   \t Kolor: " + reps[best[i][1]]+"\n");
                }
                myout.write("\n");
                if (k==0) 
                    k=1;
                
                imgSize /= k;
                int rep = 0;
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        if(nClust==0)
                            image.setRGB(i,j,1);
                        else
                        {
                            pix = reps[rep/(imgSize+1)].toInt();
                            image.setRGB(i,j,pix);
                            rep++;
                        }
                    }
                }
                eps = eps*epsDiv;
                eps = Math.round(eps);
                eps = eps /epsDiv;
                ImageIO.write(image, "bmp", new File(fileOut+"_"+eps+"_"+minPts+"_"+nClust+".bmp"));
                
                // return 7 main colors
                imgSize =(image.getHeight()*image.getWidth())/7;
                rep = 0;
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        if(nClust==0)
                            image.setRGB(i,j,1);
                        else
                        {
                            pix = reps[best[rep/(imgSize+1)][1]].toInt();
                            image.setRGB(i,j,pix);
                            rep++;
                        }
                    }
                }
                eps = eps*epsDiv;
                eps = Math.round(eps);
                eps = eps /epsDiv;
                ImageIO.write(image, "bmp", new File(fileOut+"_"+eps+"_"+minPts+"_"+nClust+"_best7.bmp"));
                
                // return 5 main colors
                imgSize =(image.getHeight()*image.getWidth())/5;
                rep = 0;
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        if(nClust==0)
                            image.setRGB(i,j,1);
                        else
                        {
                            pix = reps[best[rep/(imgSize+1)][1]].toInt();
                            image.setRGB(i,j,pix);
                            rep++;
                        }
                    }
                }
                eps = eps*epsDiv;
                eps = Math.round(eps);
                eps = eps /epsDiv;
                ImageIO.write(image, "bmp", new File(fileOut+"_"+eps+"_"+minPts+"_"+nClust+"_best5.bmp"));
                
                // return 3 main colors
                imgSize =(image.getHeight()*image.getWidth())/3;
                rep = 0;
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        if(nClust==0)
                            image.setRGB(i,j,1);
                        else
                        {
                            pix = reps[best[rep/(imgSize+1)][1]].toInt();
                            image.setRGB(i,j,pix);
                            rep++;
                        }
                    }
                }
                eps = eps*epsDiv;
                eps = Math.round(eps);
                eps = eps /epsDiv;
                ImageIO.write(image, "bmp", new File(fileOut+"_"+eps+"_"+minPts+"_"+nClust+"_best3.bmp"));
                
                for(int j=0;j<7;j++)
                {
                    best[j][0]=0;
                    best[j][1]=0;
                }
            }
        }
        myout.close();
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
