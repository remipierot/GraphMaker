package fr.graphmaker.constantes;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Interface contenant les couleurs utilisées par l'interface.
 */
public interface CouleursConstantes {
	/**
	 * Couleur du contour des sommets et du tracé des arcs de base
	 */
	public static final Color COL_BASE = new Color(36,36,36);
	
	/**
	 * Couleur du contour des sommets sélectionnés en mode création d'arc
	 */
	public static final Color COL_SELECT = new Color(30,149,76);

	/**
	 * Couleur du centre des sommets
	 */
	public static final Color COL_CENTRE = Color.WHITE;

	/**
	 * Couleur de contour des sommets et des arcs pour l'algorithme de djikstra
	 */
	public static final Color COL_DJIKSTRA = new Color(157,135,255);

	/**
	 * Couleur de contour des sommets et des arcs pour l'algorithme du marquage
	 */
	public static final Color COL_MARQUAGE = new Color(255,120,120);

	/**
	 * Couleur des cases correspondant au nom d'un sommet dans la matrice du graphe
	 */
	public static final Color COL_MATRICE_INFO = new Color(0x66B0FF);

	/**
	 * Couleur des cases comportant un zéro dans la matrice du graphe
	 */
	public static final Color COL_MATRICE_ZERO = new Color(0xEFF2FB);

	/**
	 * Couleur des cases comportant un un dans la matrice du graphe
	 */
	public static final Color COL_MATRICE_UN = new Color(0xCED8F6);

	/**
	 * Couleur d'un bouton lorsqu'on le survole
	 */
	public static final Color COL_BOUTON = new Color(0xDADADA);

	/**
	 * Couleur d'un bouton une fois qu'il a été cliqué
	 */
	public static final Color COL_BOUTON_SELECT = new Color(0xB6B6B6);
	
	/**
	 * Tableau de couleurs utilisé pour l'algorithme de Brélaz (coloration)
	 */
	public static final ArrayList<Color> COULEURS = new ArrayList<Color>() {
		private static final long serialVersionUID = -324883053780514578L;
		
		{
			add(new Color(0xFF5353));
			add(new Color(0x72FE95));
			add(new Color(0x62A9FF));
			add(new Color(0xFFF06A));
			add(new Color(0xFF9C42));
			add(new Color(0xA095EE));
			add(new Color(0x92FEF9));
			add(new Color(0xC87C5B));
			add(new Color(0x74BAAC));
			add(new Color(0x95FF4F));
			add(new Color(0xFF9A9A));
			add(new Color(0xD79AFF));
			add(new Color(0x9AD9FF));
			add(new Color(0x98DFA1));
			add(new Color(0xFFF29A));
			add(new Color(0xFFCA9A));
			add(new Color(0xD1FF9A));
			add(new Color(0xD7FFFF));
			add(new Color(0xC3C6FF));
			add(new Color(0xFF9AD2));
		}
	};
}
