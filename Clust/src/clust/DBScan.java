package clust;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

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
//		DBScan dbs = new DBScan(new ManhatanDistance());
		String fileIn = "images/icon.jpg";
		String fileOut = "images/icon_out2";
		BufferedImage image = ImageIO.read(new File(fileIn));

		double eps = 5;
		int minPts = 10;
		double eps1 = 2.500, eps2 = 2.500, epsSkip = 0.01000000000000000;
		int MinPts1 = 2, MinPts2 = 2, MinPtsSkip = 1, epsDiv=100;
		int best[][] = new int[5][2];

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
				System.out.println("liczba klastrów " + nClust);

				Color reps[] = new Color[nClust];
				k=0;
				for(int i=1;i<= nClust;i++){
					if(!clust.clusts[i].isEmpty()){
						reps[k++] = dbs.findRep(clust.clusts[i]);
						int tmp = clust.clusts[i].size();
						System.out.println("wielkosc klastra: " + clust.clusts[i].size());
						int tmp2=0;
						if (tmp > best[4][0])
						{
							best[0]=best[1].clone();
							best[1]=best[2].clone();
							best[2]=best[3].clone();
							best[3]=best[4].clone();
							best[4]=new int[]{tmp,i};
						}else if (tmp > best[3][0])
						{
							best[0]=best[1].clone();
							best[1]=best[2].clone();
							best[2]=best[3].clone();
							best[3]=new int[]{tmp,i};
						}else if (tmp > best[2][0])
						{
							best[0]=best[1].clone();
							best[1]=best[2].clone();
							best[2]=new int[]{tmp,i};
						}else if (tmp > best[1][0])
						{
							best[0]=best[1].clone();
							best[1]=new int[]{tmp,i};
						}else if (tmp > best[0][0])
						{
							best[0]=new int[]{tmp,i};
						}
					}
				}
				System.out.println("Najliczniejsze: " + best[0][0] + "_"+ best[1][0]+"_"+ best[2][0]+ "_"+ best[3][0]+ "_"+ best[4][0]);
				
				
				imgSize /= k;
				int rep = 0;
				for(int i=0;i<image.getWidth();i++){
					for(int j=0;j<image.getHeight();j++){
						pix = reps[rep/(imgSize+1)].toInt();//clust.clusts[c].get(0).toInt();
						image.setRGB(i,j,pix);
						rep++;
					}
				}
				eps = eps*epsDiv;
				eps = Math.round(eps);
				eps = eps /epsDiv;
				ImageIO.write(image, "jpg", new File(fileOut+"_"+eps+"_"+minPts+"_"+nClust+".jpg"));
				
				imgSize =(image.getHeight()*image.getWidth())/5;
				rep = 0;
				for(int i=0;i<image.getWidth();i++){
					for(int j=0;j<image.getHeight();j++){
						pix = reps[best[rep/(imgSize+1)][1]].toInt();//clust.clusts[c].get(0).toInt();
						image.setRGB(i,j,pix);
						rep++;
					}
				}
				eps = eps*epsDiv;
				eps = Math.round(eps);
				eps = eps /epsDiv;
				ImageIO.write(image, "jpg", new File(fileOut+"_best_"+eps+"_"+minPts+"_"+nClust+".jpg"));
			}
		}
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
//		return crep;
	}
}
