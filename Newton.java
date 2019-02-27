import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import flanagan.complex.ComplexPoly;
import flanagan.complex.Complex;




import javax.swing.JFrame;
import javax.swing.JComponent;

public class Newton extends JComponent
{
	
	public static void main(String[] args)
	{
		new Newton();
	}
	
	public static final int LARGEUR = 1280; // Définition de la largeur de la fenetre
	public static final int HAUTEUR = 720; // '' hauteur de la fenetre
	public static final int ITERATIONS = 500; // Définition du nombre d'itérations
	public static final float ECHELLE = 300; // Definition de l'echelle de grandeur
	
	public static final double epsilon = (double)0.1; //plus epsilon est petit, plus le calcul est précis, et le temps de calcul évolue de manière exponentielle. Tester avec de petites valeurs au début
	public static final double alpha = (double)1;
	
	public static double [] coefficients1 = {2,-2,0,1}; // on définit les coefficients du polynome
	
	//public static PolynomialFunction P1 = new PolynomialFunction(coefficients1); test de la lib d'apache polynomiale
	//public static PolynomialFunction P2 = new PolynomialFunction(coefficients2); // on construit les polynomes
	
	public static ComplexPoly P1 = new ComplexPoly(coefficients1);
	public static ComplexPoly P2 = P1.nthDerivative(1);
	
	public static final Complex[] roots = P1.roots();
	
	private BufferedImage buffer; // Image tampon
	
	public Newton()
	{
		buffer = new BufferedImage(LARGEUR, HAUTEUR, BufferedImage.TYPE_INT_RGB); // On instancie le buffer : dimensions et coloration
		
		newton();
		
		JFrame frame = new JFrame("Newton"); // Instance de la fenetre
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pour fermer la fenetre on pourra cliquer sur la croix rouge
		frame.setResizable(true); // On pourra resizer la fenetre
		frame.getContentPane().add(this); // ??
		frame.pack(); // Permet d'adapter la taille de la fenetre suivant les cas de figures
		frame.setVisible(true); // Pour que la fenetre soit visible

		//P2 = P2.multiply(P1); test de la lib polynomiale d'apache
		//System.out.println("le degré est :" + P2.degree());
	}

	@Override 
	public void addNotify()
	{
		setPreferredSize(new Dimension(LARGEUR,HAUTEUR)); // Pas trop compris ce paramètre mais c'est pour faire lien entre les différents layers du panel (container etc avec awt)
	}
	
	public void newton()
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
			//z = P1.value(z)/P1.DerivativeStructure(z);
			//z = ((z.pow(3).times(2)).plus(1)).over((z.pow(2)).times(3));
			z = z.minus((P1.evaluate(z)).times(alpha).over(P2.evaluate(z)));
			for(Complex item : roots)
			{
			if(Math.abs(z.getReal() - item.getReal()) < epsilon && Math.abs(z.getImag() - item.getImag()) < epsilon)
			{
				if(item.getImag()==0)
				{
					return Color.HSBtoRGB((float)Math.sqrt((double)30*n/ITERATIONS) , 0.5f, 1); // ce point converge vers la racine réelle
					//return 0xFF0000FF;
				}
				else if(item.getImag()<0)
				{
					return Color.HSBtoRGB((float)Math.sqrt((double)30*n/ITERATIONS) , 0.5f, 1); // ce point converge vers la 1ere racine complexe
					//return 0xFFFF0000;
				}
				else if(item.getImag()>0)
				{
					return Color.HSBtoRGB((float)Math.sqrt((double)30*n/ITERATIONS) , 0.5f, 1); // ce point converge vers la racine complexe conjuguée
					//return 0xFF00FF00;
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
	
	
	// HSBtoRGB(float hue, float saturation, float brightness)
	//Converts the components of a color, as specified by the HSB model, to an equivalent set of values for the default RGB model.
}

