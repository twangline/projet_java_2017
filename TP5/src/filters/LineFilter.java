package filters;


import figures.Figure;

public class LineFilter<LineType> extends FigureFilter<LineType> {
	public LineFilter(LineType type)
	{
		super(type);
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
		if(f.getLineType().equals(element))//??stroke??
		{
			return true;
		}
		else return false;
		
	}
	
}
