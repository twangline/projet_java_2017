package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

public class Cercle extends Figure {

	private static int counter = 0;

	/**
	 * Cr¨¦ation d'un rectangle avec les points en haut ¨¤ gauche et en bas ¨¤
	 * droite
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param topLeft le point en haut ¨¤ gauche
	 * @param bottomRight le point en bas ¨¤ droite
	 */
	public Cercle(BasicStroke stroke, Paint edge, Paint fill, Point2D coeur,
			double r)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double w = 2*r;
		double h = 2*r;
		double x = (coeur.getX() - r);
		double y = (coeur.getY() - r);

		shape = new Ellipse2D.Double(x, y, w, h);
	
		// System.out.println("Rectangle created");
	}

	/**
	 * Constructeur de copie assurant une copie distincte du rectangle
	 * @param rect le rectangle ¨¤ copier
	 */
	public Cercle(Cercle rect)
	{
		super(rect);
		if (rect.getClass() == Cercle.class)
		{
			Ellipse2D oldCercle = (Ellipse2D) rect.shape;
			shape = new Ellipse2D.Double(oldCercle.getMinX(),
											oldCercle.getMinY(),
											oldCercle.getWidth(),
											oldCercle.getHeight());
		}
		else
		{
			System.out.println("Calling Cercle(Cercle) from another class");
		}
	}

	/**
	 * Cr¨¦ation d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new Cercle(this);
	}

	/**
	 * Comparaison de deux figures
	 * @param Object o l'objet ¨¤ comparer
	 * @return true si obj est une figure de m¨ºme type et que son contenu est
	 * identique
	 */
	@Override
	public boolean equals(Object o)
	{
		if (super.equals(o))
			
		{
			Cercle r = (Cercle) o;
			RectangularShape r1 = (RectangularShape) shape;
			RectangularShape r2 = (RectangularShape) r.shape;

			return ((r1.getX() == r2.getX()) &&
			        (r1.getY() == r2.getY()) &&
			        (r1.getWidth() == r2.getWidth()) &&
			        (r1.getHeight() == r2.getHeight()));
		}

		return false;
	}

	/**
	 * Cr¨¦ation d'un rectangle sans points (utilis¨¦ dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 */
	protected Cercle(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * D¨¦placement du point en bas ¨¤ droite du rectangle ¨¤ la position
	 * du point p
	 *
	 * @param p la nouvelle position du dernier point
	 * @see figures.Figure#setLastPoint(Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p)
	{
		if (shape != null)
		{
			Ellipse2D.Double rect = (Ellipse2D.Double) shape;
			double newWidth = p.getX() - rect.x;
			double newHeight = p.getY() - rect.y;
			if(newHeight>newWidth)
			{
				rect.width = newWidth;
				rect.height = newWidth;
			}
			else
			{
				rect.width = newHeight;
				rect.height = newHeight;	
			}
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		Ellipse2D rect = (Ellipse2D.Double) shape;
		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	/**
	 * Normalise une figure de mani¨¨re ¨¤ exprimer tous ses points par rapport
	 * ¨¤ son centre, puis transf¨¨re la position r¨¦elle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize()
	{
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		Ellipse2D.Double cercle = (Ellipse2D.Double) shape;
		
		translation.translate(cx, cy);
		cercle.setFrame(cercle.getX() - cx,
		                   cercle.getY() - cy,
		                   cercle.getWidth(),
		                   cercle.getHeight());
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.CIRCLE;
 	}
}
