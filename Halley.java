import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import flanagan.complex.ComplexPoly;
import flanagan.complex.Complex;


import javax.swing.JFrame;
import javax.swing.JComponent;

public class Halley extends JComponent
{
	
	public static void main(String[] args)
	{
		new Halley();
	}
	
	public static final int LARGEUR = 1280; // Définition de la largeur de la fenetre
	public static final int HAUTEUR = 720; // '' hauteur de la fenetre
	public static final int ITERATIONS = 500; // Définition du nombre d'itérations
	public static final float ECHELLE = 300; // Definition de l'echelle de grandeur
	
	
	public static final double epsilon = (double)0.000000001; //plus epsilon est petit, plus le calcul est précis, et le temps de calcul évolue de manière exponentielle. Tester avec de petites valeurs au début
	public static final double alpha = (double)1;
	
	//public static double [] coefficients1 = {7,-2,26,-4}; // on définit les coefficients du polynome
	
	public static double [] coefficients1 = {3,2,6,10}; // on définit les coefficients du polynome
	
	public static ComplexPoly P1 = new ComplexPoly(coefficients1);
	public static ComplexPoly P2 = P1.nthDerivative(1);
	public static ComplexPoly P3 = P1.nthDerivative(2);
	
	public static final Complex[] roots = P1.roots();
	
	public static int coloriage = 1;
	
	private BufferedImage buffer; // Image tampon
	
	public Halley()
	{
		buffer = new BufferedImage(LARGEUR, HAUTEUR, BufferedImage.TYPE_INT_RGB); // On instancie le buffer : dimensions et coloration
		
		
		
		halley();
		
		JFrame frame = new JFrame("Householder"); // Instance de la fenetre
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
	
	public void halley()
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
		
		for(; n < ITERATIONS; n++)
		{
			z = z.minus((P1.evaluate(z).times(2)).times(P2.evaluate(z)).over((P2.evaluate(z).times(P2.evaluate(z))).times(2).minus(P1.evaluate(z).times(P3.evaluate(z))))); 
			for(Complex item : roots)
			{
				if(Math.abs(z.getReal() - item.getReal()) < epsilon && Math.abs(z.getImag() - item.getImag()) < epsilon)
						{
							if(coloriage == 0)
							{
								return Color.HSBtoRGB((float)Math.sqrt((double)35*n/ITERATIONS) , 0.5f, (float)20*n/ITERATIONS);
							}
							else if(coloriage == 1)
							{
								return Color.HSBtoRGB((float)Math.sqrt((double)2*n*z.arg() + 1000000) ,0.5f, (float)40*n/ITERATIONS);
							}
							else
							{
								return Color.HSBtoRGB((float)Math.exp((double)25*n/ITERATIONS) , 0.5f, (float)15*n/ITERATIONS);
							}
								
						}
			}
		}
		return 0xFFFFFFFF;
}
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(buffer,  0,  0, null);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
	}
	
	/*
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC)
	
	*/
		
	
	// HSBtoRGB(float hue, float saturation, float brightness)
	//Converts the components of a color, as specified by the HSB model, to an equivalent set of values for the default RGB model.
}