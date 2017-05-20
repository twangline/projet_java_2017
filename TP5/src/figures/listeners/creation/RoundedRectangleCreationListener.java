package figures.listeners.creation;

import figures.Drawing;
import figures.Figure;
import figures.RoundedRectangle;
import history.HistoryManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import figures.listeners.AbstractFigureListener;

import javax.swing.JLabel;

/**
 * Listener (incomplet) des évènements souris pour créer une figure. Chaque
 * figure (Cercle, Ellipse, Rectangle, etc) est graphiquement construite par une
 * suite de pressed/drag/release ou de clicks qui peut être différente pour
 * chaque type de figure. Aussi les classes filles devront implémenter leur
 * propre xxxCreationListener assurant la gestion de la création d'une nouvelle
 * figure.
 */

public class RoundedRectangleCreationListener extends AbstractCreationListener {

	
	/**
	 * Constructeur d'un listener à deux étapes: pressed->drag->release pour
	 * toutes les figures à caractère rectangulaire (Rectangle, Ellipse, evt
	 * Cercle)
	 * @param model le modèle de dessin à modifier par ce creationListener
	 * @param history le gestionnaire d'historique pour les Undo/Redo
	 * @param tipLabel le label dans lequel afficher les conseils utilisateur
	 */
	
	public RoundedRectangleCreationListener(Drawing model,
											HistoryManager<Figure> history,
											JLabel infoLabel) {
		super(model,history ,infoLabel, 3);

		tips[0] = new String("Cliquez et maintenez le bouton gauche pour commencer le rectangle arrondi");
		tips[1] = new String("Relâchez, et déplacer la souris pour régler l'arrondis");
		tips[2] = new String("Faites un clic gauche pour terminer l'arrondi du rectangle");

		updateTip();

		System.out.println("RoundedRectangleCreationListener created");
	
	}

	/**
	 * Terminaison de la figure roundedRectangle si le bouton appuyé es la droite
	 *
	 * @param e l'évènement souris associé
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 2))
		{
			endAction(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// rien

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// rien

	}

	/**
	 * Création d'une nouvelle figure rectangulaire de taille 0 au point de
	 * l'évènement souris, si le bouton appuyé est le bouton gauche.
	 *
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#startAction(MouseEvent)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 0))
		{
			startAction(e);
		}
	}

	/**
	 * Terminaison de la partie rectangle si le bouton appuyé
	 * était le bouton gauche
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#endAction(MouseEvent)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 1))
		{
			nextStep();
		}
	}

	/**
	 * Déplacement du point en bas à droite de la figure rectangulaire, si
	 * l'on se trouve à l'étape 1 (après initalisation de la figure) et que
	 * le bouton enfoncé est bien le bouton gauche.
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (currentStep == 1)
		{
			// AbstractFigure figure = drawingModel.getLastFigure();
			if (currentFigure != null)
			{
				currentFigure.setLastPoint(e.getPoint());
			}
			else
			{
				System.err.println(getClass().getSimpleName() + "::mouseDragged : null figure");
			}

			drawingModel.update();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (currentStep == 2)
		{
			RoundedRectangle rect = (RoundedRectangle) currentFigure;
			rect.setArc(e.getPoint());

			drawingModel.update();
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// rien

	}

}
