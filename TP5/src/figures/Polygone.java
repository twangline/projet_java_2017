/**
 * 
 */
package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * @author tingting
 *
 */
public class Polygone extends Figure {

	
	/**
	 * Le compteur d'instance des Polygones.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;
	
	/**
	 * @param stroke
	 * @param edge
	 * @param fill
	 * @param p1
	 * @param p2
	 */
	public Polygone(BasicStroke stroke, Paint edge, Paint fill, Point p1, Point p2) {
		super(stroke, edge, fill);
		// Auto-generated constructor stub
		instanceNumber = ++counter;
		Polygon poly = new Polygon();
		poly.addPoint(p1.x, p1.y);
		poly.addPoint(p2.x, p2.y);
		shape = new poly;
	}
	
	/**
	 * @param stroke
	 * @param edge
	 * @param fill
	 */
	public Polygone(BasicStroke stroke, Paint edge, Paint fill) {
		super(stroke, edge, fill);
		// Auto-generated constructor stub
		shape = null;
	}

	/**
	 * @param f
	 */
	public Polygone(Polygone poly) {
		// Auto-generated constructor stub
		super(poly);
		Polygon copyPoly = (Polygon) poly.shape;
		int nPoints = copyPoly.npoints;
		int[] xpoints = new int[nPoints];
		int[] ypoints = new int[nPoints];

		for (int i = 0; i < nPoints; i++)
		{
			xpoints[i] = copyPoly.xpoints[i];
			ypoints[i] = copyPoly.ypoints[i];
		}

		shape = new java.awt.Polygon(xpoints, ypoints, nPoints);

	}

	/* (non-Javadoc)
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone() {
		// Auto-generated method stub
		return new Polygone(this);
	}

	/* (non-Javadoc)
	 * @see figures.Figure#setLastPoint(java.awt.geom.Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p) {
		// TODO Auto-generated method stub
		if (shape != null)
		{
			Polygon poly = (Polygon) shape;
			int lastnPoint = poly.npoints-1;
			if (lastnPoint >= 0)
			{
				poly.xpoints[lastnPoint] = Double.valueOf(p.getX()).intValue();
				poly.ypoints[lastnPoint] = Double.valueOf(p.getY()).intValue();
			}
		}
		else 
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	/* (non-Javadoc)
	 * @see figures.Figure#normalize()
	 */
	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		Point2D center = getCenter();
		double cx = center.getX();
		double cy = center.getY();
		 rectangle = (RectangularShape) shape;
	}

	/* (non-Javadoc)
	 * @see figures.Figure#getCenter()
	 */
	@Override
	public Point2D getCenter() {
		// TODO Auto-generated method stub
		RectangularShape rect = (RectangularShape) shape;

		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		Point2D tCenter = new Point2D.Double();
		getTransform().transform(center, tCenter);

		return tCenter;
	}

	/* (non-Javadoc)
	 * @see figures.Figure#getType()
	 */
	@Override
	public FigureType getType() {
		// TODO Auto-generated method stub
		return FigureType.POLYGON;
	}

}
