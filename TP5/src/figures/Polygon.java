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
 * Classe de Polygon pour les {@link Figure}
 * @author tingting
 *
 */
public class Polygon extends Figure {

	
	/**
	 * Le compteur d'instance des Polygones.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;
	
	/**
	 * @param stroke le type de trait
	 * @param edge la couleur du trait
	 * @param fill la couleur de remplissage
	 * @param p1
	 * @param p2
	 */
	public Polygon(BasicStroke stroke, Paint edge, Paint fill, Point p1, Point p2) {
		super(stroke, edge, fill);
		// Auto-generated constructor stub
		instanceNumber = ++counter;
		java.awt.Polygon poly = new java.awt.Polygon();
		poly.addPoint(p1.x, p1.y);
		poly.addPoint(p2.x, p2.y);
		shape = poly;
	}
	
	/**
	 * @param stroke
	 * @param edge
	 * @param fill
	 */
	public Polygon(BasicStroke stroke, Paint edge, Paint fill) {
		super(stroke, edge, fill);
		// Auto-generated constructor stub
		shape = null;
	}

	/**
	 * Constructeur de copie assurant une copie distincte du rectangle
	 * @param poly le polygone à copier
	 */
	public Polygon(Polygon poly) {
		// Auto-generated constructor stub
		super(poly);
		java.awt.Polygon copyPoly = (java.awt.Polygon) poly.shape;
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
		return new Polygon(this);
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
		// TODO
		if (super.equals(o))
		{
			Polygon poly = (Polygon) o;
			
			java.awt.Polygon p1 = (java.awt.Polygon) shape;
			java.awt.Polygon p2 = (java.awt.Polygon) poly.shape;
			
			int nPoints1 = p1.npoints;
			int nPoints2 = p2.npoints;
			
			if (nPoints1 == nPoints2)
			{
				for (int i = 0; i < nPoints; i++)
				{
					p1.xpoints[i] == p2.xpoints[i];
					p1.xpoints[i] == p2.ypoints[i];
				}
			}

			
			return ((p1.getScaleX() == p2.getScaleX()) &&
			        (p1.getScaleY() == p2.getScaleY()) &&
			        (p1.getWidth() == p2.getWidth()) &&
			        (p1.getHeight() == p2.getHeight()));
		}

		return false;
	}
	

	/**
	 * Déplacement du point en bas à droite du rectangle à la position
	 * du point p
	 *
	 * @param p la nouvelle position du dernier point
	 * @see figures.Figure#setLastPoint(Point2D)
	 */
	@Override
	public void setLastPoint(Point2D p) {
		//  Auto-generated method stub
		if (shape != null)
		{
			java.awt.Polygon poly = (java.awt.Polygon) shape;
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

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
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

	/**
 	 * Accesseur du type de figure selon {@link FigureType}
 	 * @return le type de figure
 	 */
	@Override
	public FigureType getType() 
	{
		return FigureType.POLYGON;
	}

}
