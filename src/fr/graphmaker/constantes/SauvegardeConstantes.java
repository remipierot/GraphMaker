package fr.graphmaker.constantes;

/**
 * Interface contenant les constantes utilisées par le système de sauvegarde.
 */
public interface SauvegardeConstantes {
	/**
	 * Valeur = .gm, format de sauvegarde de notre application.
	 */
	public static final String SAVE_FORMAT = ".gm";
	
	/**
	 * Valeur = .gmpp, format de sauvegarde de l'application de l'autre groupe GraphMaker.
	 */
	public static final String OTHER_SAVE_FORMAT = ".gmpp";
	
	/**
	 * Valeur = 0, ligne contenant les informations du graphe dans le fichier de sauvegarde.
	 */
	public static final int INFOS_GRAPHE = 0;
	
	/**
	 * Valeur = 1, ligne à partir de laquelle on a les informations des sommets du graphe dans le fichier de sauvegarde.
	 */
	public static final int INFOS_SOMMETS = 1;
	
	/**
	 * Valeur = 6, nombre d'informations caractérisant un graphe dans le fichier de sauvegarde.
	 */
	public static final int NOMBRE_INFOS_GRAPHE = 6;
	
	/**
	 * Valeur = 0, identifie le nom du graphe.
	 */
	public static final int NOM_GRAPHE = 0;
	
	/**
	 * Valeur = 1, identifie l'orientation du graphe.
	 */
	public static final int TYPE_ORIENTATION_GRAPHE = 1;
	
	/**
	 * Valeur = 2, identifie la simplicité du graphe (avec ou sans boucles).
	 */
	public static final int TYPE_SIMPLE_GRAPHE = 2;
	
	/**
	 * Valeur = 3, identifie le type des arcs du graphe.
	 */
	public static final int TYPE_ARCS_GRAPHE = 3;
	
	/**
	 * Valeur = 4, identifie le nombre de sommets du graphe.
	 */
	public static final int NOMBRE_SOMMETS = 4;
	
	/**
	 * Valeur = 5, identifie le nombre d'arcs du graphe.
	 */
	public static final int NOMBRE_ARCS = 5;
	
	/**
	 * Valeur = 4, nombre d'informations caractérisant un sommet dans le fichier de sauvegarde.
	 */
	public static final int NOMBRE_INFOS_SOMMET = 4;
	
	/**
	 * Valeur = 0, identifie le nom du sommet.
	 */
	public static final int VALEUR_SOMMET = 0;
	
	/**
	 * Valeur = 1, identifie l'abscisse du sommet.
	 */
	public static final int X_SOMMET = 1;
	
	/**
	 * Valeur = 2, identifie l'ordonnée du sommet.
	 */
	public static final int Y_SOMMET = 2;
	
	/**
	 * Valeur = 3, identifie la forme du sommet.
	 */
	public static final int FORME_SOMMET = 3;
	
	/**
	 * Valeur = 10, nombre d'informations caractérisant un arc dans le fichier de sauvegarde.
	 */
	public static final int NOMBRE_INFOS_ARC = 10;
	
	/**
	 * Valeur = 0, identifie l'abscisse du sommet de départ de l'arc.
	 */
	public static final int X_DEPART_ARC = 0;
	
	/**
	 * Valeur = 1, identifie l'ordonnée du sommet de départ de l'arc.
	 */
	public static final int Y_DEPART_ARC = 1;
	
	/**
	 * Valeur = 2, identifie l'abscisse du point manipulable de l'arc.
	 */
	public static final int X_BEZIER_ARC = 2;
	
	/**
	 * Valeur = 3, identifie l'ordonnée du point manipulable de l'arc.
	 */
	public static final int Y_BEZIER_ARC = 3;
	
	/**
	 * Valeur = 4, identifie l'abscisse du sommet d'arrivée de l'arc.
	 */
	public static final int X_ARRIVEE_ARC = 4;
	
	/**
	 * Valeur = 5, identifie l'ordonnée du sommet d'arrivée de l'arc.
	 */
	public static final int Y_ARRIVEE_ARC = 5;
	
	/**
	 * Valeur = 6, identifie l'abscisse de la flèche de l'arc.
	 */
	public static final int X_FLECHE_ARC = 6;
	
	/**
	 * Valeur = 7, identifie l'ordonnée de la flèche de l'arc.
	 */
	public static final int Y_FLECHE_ARC = 7;
	
	/**
	 * Valeur = 8, identifie la distance de l'arc.
	 */
	public static final int DISTANCE_ARC = 8;
	
	/**
	 * Valeur = 9, identifie le style de l'arc.
	 */
	public static final int STYLE_ARC = 9;
}
