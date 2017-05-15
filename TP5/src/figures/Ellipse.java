/**
 * 
 */
package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * @author tingting
 *
 */
public class Ellipse extends Figure 
{
	/**
	 * Le compteur d'instance des ellipses.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	/**
	 * Création d'un rectangle avec les points en haut à gauche et en bas à
	 * droite
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param topLeft le point en haut à gauche
	 * @param bottomRight le point en bas à droite
	 */
	public Ellipse(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double x = topLeft.getX();
		double y = topLeft.getY();
		double w = (bottomRight.getX() - x);
		double h = (bottomRight.getY() - y);

		shape = new Ellipse2D.Double(x, y, w, h);

		// System.out.println("Rectangle created");
	}

	/**
	 * Constructeur de copie assurant une copie distincte du rectangle
	 * @param rect le rectangle à copier
	 */
	public Ellipse(Ellipse e)
	{
		super(e);
		if (e.getClass() == Ellipse.class)
		{
			Ellipse2D oldEllipse = (Ellipse2D) e.shape;
			shape = new Ellipse2D.Double(oldEllipse.getMinX(),
			                               oldEllipse.getMinY(),
			                               oldEllipse.getWidth(),
			                               oldEllipse.getHeight());
		}
		else
		{
			System.out.println("Calling Ellipse(Ellipse) from another class");
		}
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new Ellipse(this);
	}

	/**
	 * Comparaison de deux figures
	 * @param Object o l'objet à comparer
	 * @return true si obj est une figure de même type et que son contenu est
	 * identique
	 */
	@Override
	public boolean equals(Object o)
	{
		if (super.equals(o))
		{
			Ellipse e = (Ellipse) o;
			RectangularShape e1 = (RectangularShape) shape;
			RectangularShape e2 = (RectangularShape) e.shape;

			return ((e1.getX() == e2.getX()) &&
			        (e1.getY() == e2.getY()) &&
			        (e1.getWidth() == e2.getWidth()) &&
			        (e1.getHeight() == e2.getHeight()));
		}

		return false;
	}

	/**
	 * Création d'un rectangle sans points (utilisé dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 */
	protected Ellipse(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * Déplacement du point en bas à droite du rectangle à la position
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
			rect.width = newWidth;
			rect.height = newHeight;
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
		RectangularShape e = (RectangularShape) shape;

		Point2D center = new Point2D.Double(e.getCenterX(), e.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize()
	{
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		RectangularShape ellipse = (RectangularShape) shape;
		translation.translate(cx, cy);
		ellipse.setFrame(ellipse.getX() - cx,
		                   ellipse.getY() - cy,
		                   ellipse.getWidth(),
		                   ellipse.getHeight());
	}

 	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.ELLIPSE;
 	}

}
