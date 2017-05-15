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
		
		shape = new RoundRectangle2D.Double(x, y, w, h, arc, arc);
	}
	public RoundedRectangle(BasicStroke stroke, Paint edge, Paint fill) {
		super(stroke, edge, fill);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub

	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize() {
		// TODO Auto-generated method stub

	}

	/**
	 * Obtention du barycentre de la figure.
	 * @return le point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
	@Override
	public FigureType getType() {
		// TODO Auto-generated method stub
		return FigureType.ROUNDED_RECTANGLE;
	}

}
