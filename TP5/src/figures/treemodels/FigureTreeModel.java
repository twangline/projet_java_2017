/**
 *
 */
package figures.treemodels;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import figures.Drawing;
import figures.Figure;

/**
 * Figure TreeModel dans lequel les noeuds de niveau 1 sont les figures,
 * Il n'y a pas de noeuds de niveau 2.
 * @author davidroussel
 */
public class FigureTreeModel extends AbstractFigureTreeModel
{
	/**
	 * La liste des figure dans l'arbre
	 */
	private List<Figure> figures;

	/**
	 * Constructeur de l'arbre des types de figures
	 * @param drawing le modèle de dessin
	 * @param tree le JTree utilisé pour visualiser cet arbre
	 */
	public FigureTreeModel(Drawing drawing, JTree tree) throws NullPointerException
	{
		super(drawing, tree, "Figures");
		figures = new Vector<Figure>();
		update(drawing, null); // force Tree build
	}

	/**
	 * Ajout des figures de {@link Drawing} à l'arbre (si elles n'y sont pas
	 * déjà)
	 * @param figures les figures à ajouter
	 * @pre la liste des paths des figures sélectionnées
	 * ({@link AbstractFigureTreeModel#selectedFigures}) est vide
	 */
	@Override
	protected void addFiguresFromDrawing(List<Figure> figures)
	{
		for (Iterator<Figure> fit = figures.iterator(); fit.hasNext();)
		{
			Figure figure = fit.next();

			if (figure.isSelected())
			{
				TreePath selectedPath = new TreePath(new Object[]{
					rootElement,
					figure
				});
				selectedFigures.add(selectedPath);
			}

			if (!this.figures.contains(figure))
			{
				this.figures.add(figure);

				TreePath parentPath = new TreePath(new Object[] { rootElement });
				int[] indexes = new int[] { this.figures.size() - 1 };
				Object[] nodes = new Object[] { figure };
				fireTreeNodesInserted(parentPath, indexes, nodes);
			}
		}

		printTreePathes("FigureTreeModel::addFiguresFromDrawing["
		    + selectedFigures.size() + "]",
		                selectedFigures.toArray(new TreePath[0]));
	}

	/**
	 * Retrait des figures qui ne sont plus dans {@link Drawing} de l'arbre
	 * @param figures les figures de {@link Drawing}
	 */
	@Override
	protected void removeFiguresFromDrawing(List<Figure> figures)
	{
		int figureIndex = 0;
		for (Iterator<Figure> fit = this.figures.iterator(); fit.hasNext();)
		{
			Figure figure = fit.next();
			if (!figures.contains(figure))
			{
				fit.remove();

				// Notify the listeners
				TreePath parentPath = new TreePath(new Object[] { rootElement });
				int[] childIndex = new int[] { figureIndex };
				Object[] nodes = new Object[] { figure };
				fireNodesRemoved(parentPath, childIndex, nodes);
			}
			else
			{
				figureIndex++;
			}
		}
	}

	/**
	 * Recherche d'un noeud terminal dans l'arbre
	 * @param f La figure à rechercher dans l'arbre
	 * @return le {@link TreePath} de la figure recherchée, qui sera vide
	 * si la figure recherchée ne fait pas partie de l'arbre
	 */
	@Override
	protected TreePath findLeaf(Figure f)
	{
		if (figures.contains(f))
		{
			int index = figures.indexOf(f);
			return new TreePath(new Object[]{
				rootElement,
				figures.get(index)
			});
		}
		else
		{
			return new TreePath(new Object[0]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index)
	{
		// System.out.println("getChild(" + parent + ", " + index + ")");
		if (parent == rootElement)
		{
			// return figureTypeFromIndex(index);
			if ((index >= 0) && (index < figures.size()))
			{
				return figures.get(index);
			}

			return null;
		}
		else
		{
			// Figures nodes have no children
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent)
	{
		if (parent == rootElement)
		{
			return figures.size();
		}
		else
		{
			// Figures nodes have no children
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node)
	{
		if (node == rootElement)
		{
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		if (parent == rootElement)
		{
			return figures.indexOf(child);
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(rootElement + "\n");
		for (Figure figure : figures)
		{
			sb.append("+--").append(figure.toString()).append('\n');
		}

		return sb.toString();
	}
}
