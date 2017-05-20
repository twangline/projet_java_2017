package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import figures.enums.FigureType;

public class RoundedRectangle extends Figure {

	/**
	 * Le compteur d'instance des cercles.
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
	public RoundedRectangle(BasicStroke stroke, Paint edge, Paint fill, Point2D topLeft, Point2D bottomRight, int arc)
	{
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double x = topLeft.getX();
		double y = topLeft.getY();
		double w = (bottomRight.getX() - x);
		double h = (bottomRight.getY() - y);
		
		/*TODO changer */
		double minDim = (w < h ? w : h) / 2.0f;
		double actualArcSize = (arc< minDim ? arc : minDim);
		shape = new RoundRectangle2D.Double(x, y, w, h, actualArcSize,actualArcSize);
		System.out.println("Rounded Rectangle created");
	}

	public RoundedRectangle(RoundedRectangle f) 
	{
		super(f);
		if (f.getClass() == RoundedRectangle.class)
		{
			RoundRectangle2D oldRoundedRectangle = (RoundRectangle2D) f.shape;
			shape = new RoundRectangle2D.Double(
					oldRoundedRectangle.getMinX(),
					oldRoundedRectangle.getMinY(),
					oldRoundedRectangle.getWidth(),
					oldRoundedRectangle.getArcHeight(),
					oldRoundedRectangle.getArcWidth(),
					oldRoundedRectangle.getArcHeight());
		}
		else
		{
			System.out.println("Calling RoundedRectangle(RoundedRectangle) from another class");
		}
		
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone() {
		return new RoundedRectangle(this);
	}

	@Override
	public void setLastPoint(Point2D p) {
		if (shape != null)
		{
			RoundRectangle2D.Double rect = (RoundRectangle2D.Double) shape;
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
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize() {
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		RoundRectangle2D rectangle = (RoundRectangle2D) shape;
		translation.translate(cx, cy);
		rectangle.setFrame(rectangle.getX() - cx,
		                   rectangle.getY() - cy,
		                   rectangle.getWidth(),
		                   rectangle.getHeight());
	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter() {
		RoundRectangle2D rect = (RoundRectangle2D) shape;

		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}
	
	/**
	 * Mise en place de la taille de l'arc en focntion de la position
	 * d'un point par rapport au coin inférieur droit
	 * @param p le point déterminant la taille de l'arc
	 */
	
	/*TODO changer le code*/
	public void setArc(Point2D p)
	{
		RoundRectangle2D.Double rect = (RoundRectangle2D.Double)shape;

		double bottomRightX = rect.getMaxX();
		double bottomRightY = rect.getMaxY();
		double x = p.getX();
		double y = p.getY();

		if (x > bottomRightX)
		{
			if (y < bottomRightY)
			{
				rect.arcwidth = bottomRightY - y;
				rect.archeight = rect.arcwidth;
			}
			else
			{
				rect.arcwidth = 0;
				rect.archeight = 0;
			}
		}
		else
		{
			if (y > bottomRightY)
			{
				rect.arcwidth = bottomRightX - x;
				rect.archeight = rect.arcwidth;
			}
			else
			{
				rect.arcwidth = 0;
				rect.archeight = 0;
			}
		}
	}

	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
	@Override
	public FigureType getType() {
		return FigureType.ROUNDED_RECTANGLE;
	}

}
