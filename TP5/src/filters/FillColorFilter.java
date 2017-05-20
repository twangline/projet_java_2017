/**
 * 
 */
package filters;

import figures.Figure;

/**
 * @author S551L
 *
 */
public  class FillColorFilter<Paint> extends FigureFilter<Paint> {
	public FillColorFilter(Paint paint)
	{
		super(paint);
	}
	/**
	 * Test du prédicat
	 * @param f la figure à tester
	 * @return vrai si un élément de la figure f correspond à l'élément contenu
	 * dans ce prédicat (par exemple figure.getType() == element pour filtrer
	 * les types de figures)
	 * @see java.util.function.Predicate#test(java.lang.Object)
	 */
	@Override
	public boolean test(Figure f)
	{
		if(f.getFillPaint().equals(element))
		{
			return true;
		}
		else return false;
		
	}

}
