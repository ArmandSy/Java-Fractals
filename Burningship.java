import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import flanagan.complex.ComplexPoly;
import flanagan.complex.Complex;


import javax.swing.JFrame;
import javax.swing.JComponent;

public class Burningship extends JComponent
{
	
	public static void main(String[] args)
	{
		new Burningship();
	}
	
	public static final int LARGEUR = 1280; // Définition de la largeur de la fenetre
	public static final int HAUTEUR = 720; // '' hauteur de la fenetre
	public static final int ITERATIONS = 10000; // Définition du nombre d'itérations
	public static final float ECHELLE = 300; // Definition de l'echelle de grandeur
	
	public static final double epsilon = (double)0.1; //plus epsilon est petit, plus le calcul est précis, et le temps de calcul évolue de manière exponentielle. Tester avec de petites valeurs au début
	
	public static final Complex c = new Complex(1,1);
	
	private BufferedImage buffer; // Image tampon
	
	public Burningship()
	{
		buffer = new BufferedImage(LARGEUR, HAUTEUR, BufferedImage.TYPE_INT_RGB); // On instancie le buffer : dimensions et coloration
		
		burningship();
		
		JFrame frame = new JFrame("Burningship"); // Instance de la fenetre
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pour fermer la fenetre on pourra cliquer sur la croix rouge
		frame.setResizable(true); // On pourra resizer la fenetre
		frame.getContentPane().add(this); // ??
		frame.pack(); // Permet d'adapter la taille de la fenetre suivant les cas de figures
		frame.setVisible(true); // Pour que la fenetre soit visible


	}

	@Override 
	public void addNotify()
	{
		setPreferredSize(new Dimension(LARGEUR,HAUTEUR)); // Pas trop compris ce paramètre mais c'est pour faire lien entre les différents layers du panel (container etc avec awt)
	}
	
	public void burningship()
	{
		for (int x = 0 ; x < LARGEUR ; x++)
			for (int y = 0; y < HAUTEUR ; y++)
			{
				int color = calculCouleur((x - LARGEUR/2f)/ECHELLE, (y - HAUTEUR/2f)/ECHELLE);
				buffer.setRGB(x, y, color);
			}
	}
	
	public int calculCouleur(double zx, double zy)
	{
		int n = 0;
		
		Complex z = new Complex(zx,zy);
		double x = zx;
		double y = zy;
		
		while(n < ITERATIONS && z.getReal()*z.getReal() + z.getImag()*z.getImag() < 4)
		{
			double xtemp = z.getReal()*z.getReal() - z.getImag()*z.getImag() + x;
			zy = Math.abs(2*z.getReal()*z.getImag() + y);
			zx = Math.abs(xtemp);
			z = new Complex(zx,zy);
			n++;
		}
		if(n==ITERATIONS)
		{
			return 0x00000000;
		}
		return Color.HSBtoRGB((float)Math.sqrt((double)60*n/ITERATIONS) , 0.5f, 1);
}	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(buffer,  0,  0, null);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	}
	
	
	// HSBtoRGB(float hue, float saturation, float brightness)
	//Converts the components of a color, as specified by the HSB model, to an equivalent set of values for the default RGB model.
}
