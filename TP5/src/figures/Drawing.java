package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Stream;

import figures.enums.FigureType;
import figures.enums.LineType;
import filters.FigureFilters;
import utils.StrokeFactory;

/**
 * Classe contenant l'ensemble des figures à dessiner (LE MODELE)
 * @author davidroussel
 */
public class Drawing extends Observable
{
	/**
	 * Liste des figures à dessiner (protected pour que les classes du même
	 * package puissent y accéder)
	 */
	protected Vector<Figure> figures;

	/**
	 * Liste triée des indices (uniques) des figures sélectionnées
	 */
	protected SortedSet<Integer> selectionIndex;

	/**
	 * Figure située sous le curseur.
	 * Déterminé par {@link #getFigureAt(Point2D)}
	 */
	private Figure selectedFigure;

	/**
	 * Le type de figure à créer (pour la prochaine figure)
	 */
	private FigureType type;

	/**
	 * La couleur de remplissage courante (pour la prochaine figure)
	 */
	private Paint fillPaint;

	/**
	 * La couleur de trait courante (pour la prochaine figure)
	 */
	private Paint edgePaint;

	/**
	 * La largeur de trait courante (pour la prochaine figure)
	 */
	private float edgeWidth;

	/**
	 * Le type de trait courant (sans trait, trait plein, trait pointillé,
	 * pour la prochaine figure)
	 */
	private LineType edgeType;

	/**
	 * Les caractétistique à appliquer au trait en fonction de {@link #type} et
	 * {@link #edgeWidth}
	 */
	private BasicStroke stroke;

	/**
	 * Etat de filtrage des figures dans le flux de figures fournit par
	 * {@link #stream()}
	 * Lorsque {@link #filtering} est true le dessin des figures est filtré
	 * par l'ensemble des filtres présents dans {@link #shapeFilters},
	 * {@link #fillColorFilter}, {@link #edgeColorFilter} et
	 * {@link #lineFilters}.
	 * Lorsque {@link #filtering} est false, toutes les figures sont fournies
	 * dans le flux des figures.
	 * @see #stream()
	 */
	private boolean filtering;

	/**
	 * Filtres à appliquer au flux des figures pour sélectionner les types
	 * de figures à afficher
	 * @see #stream()
	 */
	private FigureFilters<FigureType> shapeFilters;

	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de remplissage
	 */
	// private FillColorFilter fillColorFilter; // TODO décommenter lorsque prêt

	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de trait
	 */
	// private EdgeColorFilter edgeColorFilter; // TODO décommenter lorsque prêt

	/**
	 * Filtres à applique au flux des figures pour sélectionner les figures
	 * ayant un type particulier de lignes
	 */
	private FigureFilters<LineType> lineFilters;

	/**
	 * Le status du modèle après une opération (et
	 * typiquement avant un {@link #update()}).
	 * Prends ses valeurs en tant que combinaison (bitwise OR)
	 * des valeurs de {@link Status}
	 */
	private int status;

	/**
	 * Constructeur de modèle de dessin
	 */
	public Drawing()
	{
		figures = new Vector<Figure>();
		selectionIndex = new TreeSet<Integer>(Integer::compare);
		shapeFilters = new FigureFilters<FigureType>();

		// fillColorFilter = null; // TODO décommenter lorsque prêt
		// edgeColorFilter = null; // TODO décommenter lorsque prêt
		lineFilters = new FigureFilters<LineType>();

		fillPaint = null;
		edgePaint = null;
		edgeWidth = 1.0f;
		edgeType = LineType.SOLID;
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
		filtering = false;
		selectedFigure = null;
		status = Status.NORMAL.value;
		System.out.println("Drawing model created");
	}

	/**
	 * Nettoyage avant destruction
	 */
	@Override
	protected void finalize()
	{
		// Aide au GC
		figures.clear();
		figures = null;
		selectionIndex.clear();
		selectionIndex = null;
		fillPaint = null;
		edgePaint = null;
		edgeType = null;
		stroke = null;
		shapeFilters.clear();
		shapeFilters = null;
		// fillColorFilter = null; // TODO décommenter lorsque prêt
		// edgeColorFilter = null; // TODO décommenter lorsque prêt
		lineFilters.clear();
		lineFilters = null;
	}

	/**
	 * Mise à jour du ou des {@link Observer} qui observent ce modèle. On place
	 * le modèle dans un état "changé" puis on notifie les observateurs.
	 */
	public void update()
	{
		setChanged();
		notifyObservers(status); // pour que les observateurs soient mis à jour
		status = Status.NORMAL.value;
	}

	/**
	 * Création d'un état courant du modèle de dessin
	 * @return un état courant contenant une copie de l'état du modèle
	 */
	public State createState()
	{
		// TODO Remplacer par l'implémentation ...
		return null;
	}

	/**
	 * Mise en place d'un état particulier remplaçant l'état courant du modèle
	 * de dessin
	 * @param state l'état à mettre en place
	 * @return true si l'état courant a été correctement remplacé, false sinon
	 */
	public boolean setState(State state)
	{
		// TODO Remplacer par l'implémentation ...
		return false;
	}

	/**
	 * Mise en place d'un nouveau type de figure à générer
	 * @param type le nouveau type de figure
	 */
	public void setFigureType(FigureType type)
	{
		this.type = type;
	}

	/**
	 * Accesseur de la couleur de remplissage courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getFillpaint()
	{
		return fillPaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de remplissage
	 * @param fillPaint la nouvelle couleur de remplissage
	 */
	public void setFillPaint(Paint fillPaint)
	{
		this.fillPaint = fillPaint;
	}

	/**
	 * Accesseur de la couleur de trait courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getEdgePaint()
	{
		return edgePaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de trait
	 * @param edgePaint la nouvelle couleur de trait
	 */
	public void setEdgePaint(Paint edgePaint)
	{
		this.edgePaint = edgePaint;
	}

	/**
	 * Accesseur du trait courant des figures
	 * @return le trait courant des figures
	 */
	public BasicStroke getStroke()
	{
		return stroke;
	}

	/**
	 * Mise en place d'un nouvelle épaisseur de trait
	 * @param width la nouvelle épaisseur de trait
	 */
	public void setEdgeWidth(float width)
	{
		edgeWidth = width;
		/*
		 * TODO Il faut regénérer le stroke
		 */
	}

	/**
	 * Mise en place d'un nouvel état de ligne pointillée
	 * @param type le nouveau type de ligne
	 */
	public void setEdgeType(LineType type)
	{
		edgeType = type;
		/*
		 * TODO Il faut regénérer le stroke
		 */
	}

	/**
	 * Accesseur en lecture du {@link #status}
	 * @return le status courant
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * Vérifie si un status particulier fait partie
	 * du status courant
	 * @param status le status recherché
	 * @return true si le status recherché fait partie
	 * du status courant.
	 */
	public boolean hasStatus(Status status)
	{
		return (this.status & status.value) != 0;
	}

	/**
	 * Mutateur du {@link #status}
	 * @param status la nouvelle valeur du {@link #status}
	 */
	public void setStatus(Status status)
	{
		this.status = status.value;
	}

	/**
	 * Ajout d'un bit de flag de status
	 * @param status le status à combiner au status courant
	 */
	public void addStatus(Status status)
	{
		this.status = (this.status | status.value);
	}

	/**
	 * Initialisation d'une figure de type {@link #type} au point p et ajout de
	 * cette figure à la liste des {@link #figures}
	 * @param p le point où initialiser la figure
	 * @return la nouvelle figure créée à x et y avec les paramètres courants
	 */
	public Figure initiateFigure(Point2D p)
	{
		/*
		 * TODO Maintenant que l'on s'apprête effectivement à créer une figure on
		 * ajoute les Paints et le Stroke aux factories
		 */

		/*
		 * TODO Obtention de la figure correspondant au type de figure choisi grâce à
		 * type.getFigure(...)
		 */
		Figure newFigure = null; // TODO remplacer par type.getType(...)

		/*
		 * TODO Ajout de la figure à #figures
		 */

		/* TODO Notification des observers */

		return newFigure;
	}

	/**
	 * Obtention de la dernière figure (implicitement celle qui est en cours de
	 * dessin)
	 * @return la dernière figure du dessin
	 */
	public Figure getLastFigure()
	{
		// TODO Remplacer par l'implémentation ...
		return null;
	}

	/**
	 * Obtention de la dernière figure contenant le point p.
	 * @param p le point sous lequel on cherche une figure
	 * @return une référence vers la dernière figure contenant le point p ou à
	 * défaut null.
	 */
	public Figure getFigureAt(Point2D p)
	{
		selectedFigure = null;

		/*
		 * TODO Recherche dans le flux des figures de la DERNIERE figure
		 * contenant le point p.
		 */

		return selectedFigure;
	}

	/**
	 * Retrait de la dernière figure (sera déclencé par une action undo)
	 * @post le modèle de dessin a été mis à jour
	 */
	public void removeLastFigure()
	{
		// TODO Compléter ...
	}

	/**
	 * Effacement de toutes les figures (sera déclenché par une action clear)
	 * @post le modèle de dessin a été mis à jour
	 */
	public void clear()
	{
		// TODO Compléter ...
	}

	/**
	 * Accesseur de l'état de filtrage
	 * @return l'état courant de filtrage
	 */
	public boolean getFiltering()
	{
		return filtering;
	}

	/**
	 * Changement d'état du filtrage
	 * @param filtering le nouveau statut de filtrage
	 * @post le modèle de dessin a été mis à jour
	 */
	public void setFiltering(boolean filtering)
	{
		// TODO ... filtering ...
	}

	/**
	 * Ajout d'un filtre pour filtrer les types de figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de figures, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public boolean addShapeFilter(ShapeFilter filter)
//	{
//		// TODO ... shapeFilters ...
//		return false;
//	}

	/**
	 * Retrait d'un filtre filtrant les types de figures
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de figure et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public boolean removeShapeFilter(ShapeFilter filter)
//	{
//		// TODO ... shapeFilters ...
//		return false;
//	}

	/**
	 * Mise en place du filtre de couleur de remplissage
	 * @param filter le filtre de couleur de remplissage à appliquer
	 * @post le {@link #fillColorFilter} est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public void setFillColorFilter(FillColorFilter filter)
//	{
//		// TODO ... fillColorFilter ...
//	}

	/**
	 * Mise en place du filtre de couleur de trait
	 * @param filter le filtre de couleur de trait à appliquer
	 * @post le #edgeColorFilter est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public void setEdgeColorFilter(EdgeColorFilter filter)
//	{
//		// TODO ... edgeColorFilter ...
//	}

	/**
	 * Ajout d'un filtre pour filtrer les types de ligne des figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de lignes, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public boolean addLineFilter(LineFilter filter)
//	{
//		// TODO ... lineFilters ...
//		return false;
//	}

	/**
	 * Retrait d'un filtre filtrant les types de lignes
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de lignes et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
//	public boolean removeLineFilter(LineFilter filter)
//	{
//		// TODO ... lineFilters ...
//		return false;
//	}

	/**
	 * Remise à l'état non sélectionné de toutes les figures
	 */
	public void clearSelection()
	{
		// TODO Compléter ...
	}

	/**
	 * Mise à jour des indices des figures sélectionnées dans {@link #selectionIndex}
	 * d'après l'interrogation de l'ensembles des figures (après filtrage).
	 * @note {@link #status} doit être modifié avant l'appel d'
	 * {@link #updateSelection()}
	 */
	public void updateSelection()
	{
		// TODO Compléter ...
	}

	/**
	 * Indique s'il existe des figures sélectionnées
	 * @return true s'il y a des figures sélectionnées
	 */
	public boolean hasSelection()
	{
		// TODO Remplacer par l'implémentation
		return false;
	}

	/**
	 * Destruction des figures sélectionnées.
	 * Et incidemment nettoyage de {@link #selectionIndex}
	 */
	public void deleteSelected()
	{
		// TODO Compléter ...
	}

	/**
	 * Applique un style particulier aux figure sélectionnées
	 * @param fill la couleur de remplissage à applique aux figures sélectionnées
	 * @param edge la couleur de trait à appliquer aux figures sélectionnées
	 * @param stroke le style de trait à appliquer aux figures sélectionnées
	 */
	public void applyStyleToSelected(Paint fill, Paint edge, BasicStroke stroke)
	{
		// TODO Compléter ...
	}

	/**
	 * Déplacement des figures sélectionnées en haut de la liste des figures.
	 * En conservant l'ordre des figures sélectionnées
	 */
	public void moveSelectedUp()
	{
		// TODO Compléter ...



		// Mise à jour des index des figures sélectionnées & notif observers
		status = Status.REORDERED.value;
		updateSelection();
	}

	/**
	 * Accès aux figures dans un stream afin que l'on puisse y appliquer
	 * de filtres
	 * @return le flux des figures éventuellement filtrés par les différents
	 * filtres
	 */
	public Stream<Figure> stream()
	{
		Stream<Figure> figuresStream = figures.stream();
		if (filtering)
		{
			// TODO Compléter avec ...
			// if (filters.size() > 0)
			// {
			// figuresStream = figuresStream.filter(filters);
			// }
		}

		return figuresStream;
	}

	/**
	 * Enum contenant différents flags indiquant l'état dans lequel se trouve
	 * le modèle de dessin après une opération.
	 * Par exemple, une figure peut avoir été ajoutée, ou enlevée, les figures
	 * réordonnées etc, etc.
	 */
	public enum Status
	{
		/**
		 * Aucun status particulier
		 */
		NORMAL(0),
		/**
		 * Une ou plusieurs figures ont été ajoutées
		 */
		ADDED(1),
		/**
		 * Une ou plusieurs figures ont été enlevées
		 */
		REMOVED(2),
		/**
		 * Les figures ont été réordonnées
		 */
		REORDERED(4);

		/**
		 * Valeur interne (puissance de 2 pour
		 * pouvoir l'utiliser dans un bitwise OR)
		 */
		public final int value;

		/**
		 * Constructeur valué
		 * @param value la valeur de l'enum
		 */
		Status(int value)
		{
			this.value = value;
		}

		/**
		 * Vérifie si une valeur d'enum particulière
		 * fait partie de flags
		 * @param flags une combinaison (bitwise OR)
		 * de valeurs d'enum
		 * @return
		 */
		public boolean isIn(int flags)
		{
			return (value & flags) != 0;
		}
	}

	/**
	 * Classe permettant de sauvegarder l'état courant du modèle de dessin.
	 * (Quitte à déplacer des attributs de {@link Drawing} dans cette classe
	 * si besoin).
	 * @note On considèrera que l'état courant concerne uniquement les
	 * figures présentes dans {@link Drawing#figures}
	 */
	public class State
	{
		// TODO Compléter ...
	}
}
