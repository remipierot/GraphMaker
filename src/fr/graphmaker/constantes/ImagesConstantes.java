package fr.graphmaker.constantes;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Interface contenant les images utilisées pour les boutons de la fenêtre principale.
 */
public interface ImagesConstantes {
	/**
	 * Image du bouton deplacer.
	 */
	public static final ImageIcon IMG_DEPLACER = new ImageIcon(new ImageIcon("res/img/deplacer.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton creerSommet.
	 */
	public static final ImageIcon IMG_CREER_SOMMET = new ImageIcon(new ImageIcon("res/img/POINT.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton creerArc (si graphe non-orienté).
	 */
	public static final ImageIcon IMG_CREER_ARC = new ImageIcon(new ImageIcon("res/img/ARC.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton creerArc (si graphe orienté).
	 */
	public static final ImageIcon IMG_CREER_ARETE = new ImageIcon(new ImageIcon("res/img/arete.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton supprimer.
	 */
	public static final ImageIcon IMG_EFFACER = new ImageIcon(new ImageIcon("res/img/EFFACER.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton reset.
	 */
	public static final ImageIcon IMG_RESET_ARCS = new ImageIcon(new ImageIcon("res/img/RESET.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton deplacerFleche.
	 */
	public static final ImageIcon IMG_DEPLACER_FLECHE = new ImageIcon(new ImageIcon("res/img/FLECHE.png").getImage().getScaledInstance(18, 18, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton deplacerGraphe.
	 */
	public static final ImageIcon IMG_DEPLACER_GRAPHE = new ImageIcon(new ImageIcon("res/img/deplacer_graph.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton clique.
	 */
	public static final ImageIcon IMG_CLIQUE = new ImageIcon(new ImageIcon("res/img/CLIQUE.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton vider.
	 */
	public static final ImageIcon IMG_VIDER = new ImageIcon(new ImageIcon("res/img/VIDER.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton chaine.
	 */
	public static final ImageIcon IMG_CHAINE = new ImageIcon(new ImageIcon("res/img/CHAINE.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton cycle.
	 */
	public static final ImageIcon IMG_CYCLE = new ImageIcon(new ImageIcon("res/img/CYCLE.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
	
	/**
	 * Image de l'onglet de la représentation matricielle.
	 */
	public static final ImageIcon IMG_MATRICIELLE = new ImageIcon(new ImageIcon("res/img/MATRICE.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
	
	/**
	 * Image de l'onglet de la représentation sagittale.
	 */
	public static final ImageIcon IMG_SAGITTALE = new ImageIcon(new ImageIcon("res/img/SAGITTALE.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton "+" pour le niveau du marquage.
	 */
	public static final ImageIcon IMG_PLUS = new ImageIcon(new ImageIcon("res/img/PLUS.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
	
	/**
	 * Image du bouton "-" pour le niveau du marquage.
	 */
	public static final ImageIcon IMG_MOINS = new ImageIcon(new ImageIcon("res/img/MOINS.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
}
