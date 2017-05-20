package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import figures.enums.FigureType;

public class Star extends Figure {
	
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;

	public Star(BasicStroke stroke, Paint edge, Paint fill, Point2D p) {
		super(stroke, edge, fill);
		instanceNumber = ++counter;
		double ch=72*Math.PI/180;
	     double x1=p.getX(),
	         x2=(double)(x0-Math.sin(ch)*r),
	         x3=(double)(x0+Math.sin(ch)*r),
	         x4=(double)(x0-Math.sin(ch/2)*r),
	        x5=(double)(x0+Math.sin(ch/2)*r);
	     int y1=y0-r,
	            y2=(int)(y0-Math.cos(ch)*r),
	            y3=y2,
	            y4=(int)(y0+Math.cos(ch/2)*r),
	            y5=4;
	      int bx=(int)(x0+Math.cos(ch)*Math.tan(ch/2)*r);
	      int by=y2;   
	 
	    
	    Polygon a=new Polygon(stroke, fill, fill);
	    Polygon b=new Polygon(stroke, fill, fill);
	    a.addPoint(x2,y2);
	    a.addPoint(x5,y5);
	    a.addPoint(bx,by);
	    b.addPoint(x1,y1);
	    b.addPoint(bx,by);
	    b.addPoint(x3,y3);
	    b.addPoint(x4,y4); 
	    {
	     g.drawPolygon(a);
	     g.drawPolygon(b);
	    }

	public Star(Figure f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Figure clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLastPoint(Point2D p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Point2D getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FigureType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
