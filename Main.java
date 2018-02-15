package mdb;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JComponent;

public class Main extends JComponent
{
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public static final int LARGEUR = 640; // Définition de la largeur de la fenetre
	public static final int HAUTEUR = 480; // '' hauteur de la fenetre
	public static final int ITERATIONS = 100; // Définition du nombre d'itérations
	public static final float ECHELLE = 250; // Definition de l'echelle de grandeur
	
	private BufferedImage buffer; // Image tampon
	
	public Main()
	{
		buffer = new BufferedImage(LARGEUR, HAUTEUR, BufferedImage.TYPE_INT_RGB); // On instancie le buffer : dimensions et coloration
		
		mandelbrot();
		
		JFrame frame = new JFrame("Mandelbrot"); // Instance de la fenetre
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
	
	public void mandelbrot()
	{
		for (int x = 0 ; x < LARGEUR ; x++)
			for (int y = 0; y < HAUTEUR ; y++)
			{
				int color = calculCouleur((x - LARGEUR/2f)/ECHELLE, (y - HAUTEUR/2f)/ECHELLE);
				buffer.setRGB(x, y, color);
			}
	}
	
	public int calculCouleur(float x, float y)
	{
		float constantereelle = x;
		float constanteimaginaire = y;
		
		int n = 0;
		
		for(; n < ITERATIONS; n++)
		{
			//On applique la formule générale pour générer l'ensemble de Mandelbrot (z(n+1) = z(n)**2 + constante complexe
			float nreelle = x*x - y*y + constantereelle;
			float nimaginaire = 2 * x * y + constanteimaginaire;
			x = nreelle;
			y = nimaginaire;
			
			if (x*x + y*y > 4) break;
		}
		if(n == ITERATIONS) return 0x00000000; // blanc => hors de l'ensemble
		return Color.HSBtoRGB((float)n/ITERATIONS , 0.5f, 1); // noir => dans l'ensemble
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(buffer,  0,  0, null);
	}
	
	
	// HSBtoRGB(float hue, float saturation, float brightness)
	//Converts the components of a color, as specified by the HSB model, to an equivalent set of values for the default RGB model.
}
