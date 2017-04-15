package figures.treemodels;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import figures.Drawing;
import figures.Drawing.Status;
import figures.Figure;

public abstract class AbstractFigureTreeModel implements TreeModel, Observer, TreeSelectionListener
{
	/**
	 * L'élément racine de l'arbre (une simple chaine de caractères)
	 */
	protected String rootElement;

	/**
	 * Le modèle de dessin.
	 * On a besoin de garder une référence vers le modèlde de dessin lorsque
	 * la liste des figures sélectionnées dans l'arbre change afin que l'on
	 * puisse le notifier des changements
	 */
	protected Drawing drawing;

	/**
	 * Le JTree utilisé pour visualiser cet arbre.
	 * On a besoin de garder une référence vers cette vue afin de
	 * spécifier (programmatiquement) quels sont les noeuds sélectionnés
	 * en fonction des figures sélectionnées.
	 * @see #selectedFigures
	 */
	protected JTree treeView;

	/**
	 * Liste des figures sélectionnées dans l'arbre
	 * (On considèrera que seule la sélection des figures a un effet, c'àd,
	 * la sélection du rootNote, ou des autre noeuds parents n'as pas d'effet)
	 */
	protected Set<TreePath> selectedFigures;

	/**
	 * La liste des listeners de ce modèle
	 */
	protected Vector<TreeModelListener> treeModelListeners;

	/**
	 * Constructeur de l'arbre des figures
	 * @param drawing le modèle de dessin
	 * @param tree le {@link JTree} utilisé pour visualiser cet arbre
	 * @param rootName le nom du noeud racine
	 */
	public AbstractFigureTreeModel(Drawing drawing, JTree tree, String rootName)
	    throws NullPointerException
	{
		this.drawing = drawing;
		treeView = tree;
		treeView.addTreeSelectionListener(this);
		rootElement = new String(rootName);
		selectedFigures = new HashSet<TreePath>();
		treeModelListeners = new Vector<TreeModelListener>();
		if (drawing != null)
		{
			drawing.addObserver(this);
		}
		else
		{
			throw new NullPointerException("FigureTypeTreeModel(null drawing)");
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		drawing.deleteObserver(this);
		rootElement = null;
		drawing = null;
		treeView.removeTreeSelectionListener(this);
		treeView = null;
		selectedFigures.clear();
		selectedFigures = null;
		treeModelListeners.clear();
		treeModelListeners = null;
		super.finalize();
	}

	/**
	 * Mise à jour par l'observable (en l'occurrence un {@link Drawing})
	 * @param observable le {@link Drawing}
	 * @param data les données à transmettre (non utilisé ici)
	 * @see Observer#update(Observable, Object)
	 */
	@Override
	public void update(Observable observable, Object data)
	{
		if (observable instanceof Drawing)
		{
			synchronized (observable)
			{
				drawing = (Drawing) observable;
				Stream<Figure> stream = drawing.stream();

				// Rebuild a vector from the stream
				Vector<Figure> figures = stream.sequential()
				    .collect(Collectors.toCollection(Vector::new));

				if (drawing.hasStatus(Status.REORDERED))
				{
					fireTreeStructureChanged(new TreePath(new Object[]{rootElement}));
				}

				// Clears currently selected figures
				selectedFigures.clear();

				/*
				 * Ajout des figures présentes dans #drawing et
				 * de leur TreePath dans #selectedFigures si elles sont
				 * sélectionnées
				 */
				addFiguresFromDrawing(figures);

				/*
				 * Mise à jour des figures déjà sélectionnées dans le #treeView
				 */
				updateSelectedPath();

				/*
				 * Remove figures that are no longer in drawings
				 */
				removeFiguresFromDrawing(figures);
				// System.out.println("AbstractFigureTreeModel updated : \n" + this);
			}
		}
		else
		{
			System.err.println("Observable is not an instance of Drawing");
		}
	}

	/**
	 * Ajout des figures de {@link Drawing} à l'arbre (si elles n'y sont pas
	 * déjà) et si les figures sont sélectionnées ajout des paths de sélection
	 * dans {@link #selectedFigures}
	 * @param figures les figures à ajouter
	 * @see Figure#isSelected()
	 * @pre la liste des paths des figures sélectionnées
	 * ({@link #selectedFigures}) est vide
	 */
	protected abstract void addFiguresFromDrawing(List<Figure> figures);

	/**
	 * Retrait des figures qui ne sont plus dans {@link Drawing} de l'arbre
	 * @param figures les figures de {@link Drawing}
	 */
	protected abstract void removeFiguresFromDrawing(List<Figure> figures);

	/**
	 * Mise à jour des noeuds sélectionnés dans le {@link #treeView} d'après
	 * les paths répertoriés dans {@link #selectedFigures}
	 */
	protected void updateSelectedPath()
	{
		System.out.println("AbstractFigureTreeModel::updateSelectedPath");
		if (treeView != null)
		{
			TreeSelectionModel tsm = treeView.getSelectionModel();
			if (tsm != null)
			{
				TreePath[] treePathes = selectedFigures.toArray(new TreePath[0]);
				printTreePathes("TreeSelectionModel::updateSelectedPathes", treePathes);
				tsm.setSelectionPaths(treePathes);
			}
			else
			{
				System.err.println("AbstractFigureTreeModel::updateSelectedPath : null Selection Model");
			}
		}
		else
		{
			System.err.println("AbstractFigureTreeModel::updateSelectedPath : null TreeView");
		}
	}

	/**
	 * Méthode à utiliser lorsque la structure de l'arbre change.
	 * Tous les éléments situés en dessous de path sont mis à jour
	 * @param path le chemin en dessous duquel l'arbre a changé
	 */
	protected void fireTreeStructureChanged(TreePath path)
	{
		if (treeModelListeners.size() > 0)
		{
			/*
			 * Used to create an event when the node structure has changed in
			 * some way, identifying the path to the root of the modified
			 * subtree as a TreePath object.
			 */
			TreeModelEvent e = new TreeModelEvent(this, path);
			for (TreeModelListener tml : treeModelListeners)
			{
				System.out.println("FireTreeStructureChanged(" + e + " to " + tml);
				tml.treeStructureChanged(e);
			}
		}
	}

	/**
	 * Méthode à utliser lorsqu'un ou plusieurs noeuds sont ajoutés à
	 * l'arbre
	 * @param path the path to the parent of inserted node(s)
	 * @param newchildIndices an array of the indices of the new inserted nodes
	 * @param newNodes an array of the new inserted nodes (Optional)
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(TreeModelEvent)
	 */
	protected void fireTreeNodesInserted(TreePath path,
	                                     int[] newchildIndices,
	                                     Object[] newNodes)
	{
		if (treeModelListeners.size() > 0)
		{
			TreeModelEvent e =
			    new TreeModelEvent(this, path, newchildIndices, newNodes);
			for (TreeModelListener tml : treeModelListeners)
			{
				tml.treeNodesInserted(e);
			}
		}
	}

	/**
	 * Méthode à utiliser lorsqu'un ou plusieurs noeuds sont retirés de l'arbre
	 * @param path the path to the former parent of deleted node
	 * @param oldChildIndices an array of indices (in ascending order) where
	 * the removed nodes used to be
	 * @note if a subtree is removed from the tree, this method may only be
	 * invoked once for the root of the removed subtree, not once for
	 * each individual set of siblings removed.
	 */
	protected void fireNodesRemoved(TreePath path,
	                                int[] oldChildIndices,
	                                Object[] oldNodes)
	{
		if (treeModelListeners.size() > 0)
		{
			TreeModelEvent e =
			    new TreeModelEvent(this, path, oldChildIndices, oldNodes);
			for (TreeModelListener tml : treeModelListeners)
			{
				tml.treeNodesRemoved(e);
			}
		}
	}

	/**
	 * Méthode à utiliser lorsqu'un ou plusieurs noeuds sont changés (par
	 * exemple s'il sont sélectionnés programmatiquement)
	 * @param treePathes l'ensemble des {@link TreePath} des noeuds changés
	 */
	protected void fireNodesChanged(TreePath[] treePathes)
	{
		for (int i = 0; i < treePathes.length; i++)
		{
			for (TreeModelListener tml : treeModelListeners)
			{
				tml.treeNodesChanged(new TreeModelEvent(this, treePathes[i]));
			}
		}
	}

	/**
	 * Recherche d'un noeud terminal dans l'arbre
	 * @param f La figure à rechercher dans l'arbre
	 * @return le {@link TreePath} de la figure recherchée, qui sera vide
	 * si la figure recherchée ne fait pas partie de l'arbre
	 */
	protected abstract TreePath findLeaf(Figure f);

	@Override
	public Object getRoot()
	{
		return rootElement;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		System.out
		    .println("*** valueForPathChanged : " + path + " --> " + newValue);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		if ((l != null) && !treeModelListeners.contains(l))
		{
			treeModelListeners.add(l);
		}
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		if (treeModelListeners.contains(l))
		{
			treeModelListeners.remove(l);
		}
	}

	/**
	 * Callback déclenché lorsqu'un noeud est sélectionné dans le {@link #treeView}
	 * @param e l'évènement de sélection dans le {@link JTree}
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		JTree tree = (JTree) e.getSource();
		int count = tree.getSelectionCount();
		TreePath[] paths = tree.getSelectionPaths();

		printTreePathes("TreeSelectionListener::valueChanged", paths);

		drawing.clearSelection();

		for (int i = 0; i < count; i++)
		{
			// System.out.println("Selection [" + i + "] = " + paths[i]);
			Object[] objPath =paths[i].getPath();
			int pathSize = paths[i].getPathCount();
			Object node = objPath[pathSize - 1];
			if (node instanceof Figure)
			{
				Figure figure = (Figure) node;
				figure.setSelected(true);
			}
		}

		drawing.updateSelection();
	}

	public void printTreePathes(String message, TreePath[] pathes)
	{
		System.out.print(message + " = ");
		if (pathes != null)
		{
			for (int i = 0; i < pathes.length; i++)
			{
				System.out.print(pathes[i] + ", ");
			}
		}
		System.out.println();
	}
}
