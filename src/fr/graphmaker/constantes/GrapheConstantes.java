package fr.graphmaker.constantes;

import java.awt.BasicStroke;

/**
 * Interface contenant les constantes utilisées par le graphe et l'interface utilisateur.
 */
public interface GrapheConstantes {
	/**
	 * Valeur = 0, lorsque le type d'orientation du graphe est orienté.
	 */
	public static final int ORIENTE = 0;
	
	/**
	 * Valeur = 1, lorsque le type d'orientation du graphe est non-orienté.
	 */
	public static final int NON_ORIENTE = 1;
	
	/**
	 * Valeur = 0, lorsque le graphe est simple (boucles interdites).
	 */
	public static final int SIMPLE = 0;
	
	/**
	 * Valeur = 1, lorsque le graphe est non-simple (boucles autorisées).
	 */
	public static final int NON_SIMPLE = 1;
	
	/**
	 * Valeur = 0, lorsque les arcs du graphe n'ont pas de valeur.
	 */
	public static final int NON_VALUE = 0;
	
	/**
	 * Valeur = 1, lorsque les arcs du graphe ont des valeurs numériques.
	 */
	public static final int VALUE = 1;
	
	/**
	 * Valeur = 2, lorsque les arcs du graphe ont des valeurs textuelles.
	 */
	public static final int ETIQUETE = 2;
	
	/**
	 * Valeur = 16, nombre d'options possibles pour le graphe.
	 */
	public static final int NOMBRE_OPTIONS = 16;
	
	/**
	 * Valeur = 0, option de création de sommets.
	 */
	public static final int CREER_SOMMET = 0;
	
	/**
	 * Valeur = 1, option de création d'arcs.
	 */
	public static final int CREER_ARC = 1;
	
	/**
	 * Valeur = 2, option de déplacement (sommets/arcs).
	 */
	public static final int DEPLACER = 2;
	
	/**
	 * Valeur = 3, option de suppression (sommets/arcs).
	 */
	public static final int SUPPRIMER = 3;
	
	/**
	 * Valeur = 4, option de sauvegarde du graphe.
	 */
	public static final int SAUVEGARDER = 4;
	
	/**
	 * Valeur = 5, option de chargement d'un graphe.
	 */
	public static final int CHARGER = 5;
	
	/**
	 * Valeur = 6, option de réinitialisation de courbe des arcs.
	 */
	public static final int RESET_ARCS = 6;
	
	/**
	 * Valeur = 7, option de déplacement de la flèche.
	 */
	public static final int DEPLACER_FLECHE = 7;
	
	/**
	 * Valeur = 8, option de déplacement du graphe total.
	 */
	public static final int DEPLACER_GRAPHE = 8;
	
	/**
	 * Valeur = 9, option de création de la clique maximale.
	 */
	public static final int CLIQUE = 9;
	
	/**
	 * Valeur = 10, option d'activation de l'algorithme de Djikstra.
	 */
    public static final int DJIKSTRA = 10;
    
    /**
	 * Valeur = 11, option d'activation de l'algorithme de Brélaz.
	 */
    public static final int BRELAZ = 11;
    
    /**
	 * Valeur = 12, option d'activation de l'algorithme du marquage.
	 */
    public static final int MARQUAGE = 12;
    
    /**
	 * Valeur = 13, option de réinitialisation du graphe.
	 */
    public static final int VIDER = 13;
    
    /**
	 * Valeur = 14, option de création de la chaîne maximale.
	 */
    public static final int CHAINE = 14;
    
    /**
	 * Valeur = 15, option de création du cycle maximal.
	 */
    public static final int CYCLE = 15;
	
    /**
	 * Valeur = false, lorsqu'on déplace le sommet d'arrivée d'un arc.
	 */
	public static final boolean MVT_ARRIVEE = false;
	
	/**
	 * Valeur = true, lorsqu'on déplace le sommet de départ d'un arc.
	 */
    public static final boolean MVT_DEPART = true;
    
    /**
	 * Valeur = false, lorsqu'on demande le calcul de la courbe du point manipulable au sommet d'arrivée pour un arc.
	 */
	public static final boolean COURBE_ARRIVEE = false;
	
	/**
	 * Valeur = true, lorsqu'on demande le calcul de la courbe du sommet de départ au point manipulable pour un arc.
	 */
    public static final boolean COURBE_DEPART = true;
	
    /**
     * Valeur = 18, rayon de base d'un sommet une fois dessiné.
     */
	public static final int RAYON_SOMMET = 18;
	
	/**
	 * Valeur = 2, épaisseur de la bordure d'un sommet une fois dessiné.
	 */
    public static final int BORDURE_SOMMET = 2;
    
    /**
     * Valeur = 7, épaisseur de la flèche des arcs d'un graphe orienté.
     */
    public static final int TAILLE_FLECHE = 7;
    
    /**
     * Valeur = 0, utilisé pour dessiner la partie sélectionnée des sommets dans leur méthode draw.
     */
    public static final int SELECTION = 0;
    
    /**
     * Valeur = 1, utilisé pour dessiner la partie bordure des sommets dans leur méthode draw.
     */
	public static final int BORDURE = 1;
	
	/**
     * Valeur = 2, utilisé pour dessiner la partie centrale des sommets dans leur méthode draw.
     */
    public static final int CENTRE = 2;
    
    /**
     * Valeur = NaN, distance de base des arcs.
     */
    public static final String NO_DISTANCE = ""+Double.NaN; 
    
    /**
     * Valeur = 0, identifie le menu de changement de nom d'un sommet dans le popup.
     */
    public static final int CHANGER_VALEUR_POPUP = 0;

    /**
     * Valeur = 1, identifie le menu de suppression d'un sommet dans le popup.
     */
    public static final int SUPPRIMER_SOMMET_POPUP = 1;

    /**
     * Valeur = 2, identifie le menu de changement de forme d'un sommet dans le popup.
     */
    public static final int CHANGER_FORME_POPUP = 2;

    /**
     * Valeur = 3, identifie le menu de changement de distance d'un arc dans le popup.
     */
    public static final int CHANGER_DISTANCE_POPUP = 3;

    /**
     * Valeur = 4, identifie le menu de suppression d'un arc dans le popup.
     */
    public static final int SUPPRIMER_ARC_POPUP = 4;

    /**
     * Valeur = 5, identifie le menu de changement de style d'un arc dans le popup.
     */
    public static final int CHANGER_TYPE_POPUP = 5;
    
    /**
     * Valeur = 4, nombre de formes de sommet disponibles.
     */
    public static final int NOMBRE_FORMES = 4;
    
    /**
     * Valeur = 0, identifie les ellipses.
     */
    public static final int ELLIPSE = 0;
    
    /**
     * Valeur = 1, identifie les rectangles.
     */
    public static final int RECTANGLE = 1;
    
    /**
     * Valeur = 2, identifie les rectangles arrondis.
     */
    public static final int ROUND_RECTANGLE = 2;
    
    /**
     * Valeur = 3, identifie les losanges.
     */
    public static final int LOSANGE = 3;
    
    /**
     * Valeur = 4, nombre de styles d'arcs disponibles.
     */
    public static final int NOMBRE_ARCS = 4;
    
    /**
     * Valeur = 0, identifie un arc simple plein.
     */
    public static final int ARC_SIMPLE_PLEIN = 0;
    
    /**
     * Valeur = 1, identifie un arc simple à pointillés.
     */
    public static final int ARC_SIMPLE_POINTILLE = 1;
    
    /**
     * Valeur = 2, identifie un arc double plein.
     */
    public static final int ARC_DOUBLE_PLEIN = 2;
    
    /**
     * Valeur = 3, identifie un arc double à pointillés.
     */
    public static final int ARC_DOUBLE_POINTILLE = 3;
    
    /**
     * Ligne utilisée pour tracer les arcs simples pleins.
     */
    public static final BasicStroke STYLE_ARC_SIMPLE_PLEIN = new BasicStroke(1.5f);
    
    /**
     * Ligne utilisée pour tracer les arcs simples à pointillés.
     */
    public static final BasicStroke STYLE_ARC_SIMPLE_POINTILLE = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
    
    /**
     * Ligne utilisée pour tracer les arcs doubles pleins.
     */
    public static final BasicStroke STYLE_ARC_DOUBLE_PLEIN = new BasicStroke(4.0f);
    
    /**
     * Ligne utilisée pour tracer les arcs doubles à pointillés.
     */
    public static final BasicStroke STYLE_ARC_DOUBLE_POINTILLE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
    
    /**
     * Valeur = 8, nombre d'options possibles dans sous-menu Graphe de la fenêtre principale.
     */
    public static final int NOMBRE_GRAPHES = 8;
    
    /**
     * Valeur = 0, option de création d'un nouveau graphe.
     */
    public static final int NOUVEAU_GRAPHE = 0;
    
    /**
     * Valeur = 1, option de chargement d'un nouveau graphe.
     */
    public static final int CHARGER_GRAPHE = 1;
    
    /**
     * Valeur = 2, option d'édition du graphe courant.
     */
    public static final int EDITER_GRAPHE = 2;
    
    /**
     * Valeur = 3, option de sauvegarde du graphe courant.
     */
    public static final int SAUVEGARDER_GRAPHE = 3;
    
    /**
     * Valeur = 4, option de conversion du graphe en format LaTeX.
     */
    public static final int CONVERSION_LATEX = 4;
    
    /**
     * Valeur = 5, option de désorientation du graphe courant.
     */
    public static final int DESORIENTER_GRAPHE = 5;
    
    /**
     * Valeur = 6, option de simplification du graphe courant.
     */
    public static final int SIMPLIFIER_GRAPHE = 6;
    
    /**
     * Valeur = 7, option de fermeture du graphe courant.
     */
    public static final int FERMER_GRAPHE = 7;
    
    /**
     * Valeur = 3, nombre d'algorithmes disponibles.
     */
    public static final int NOMBRE_ALGO = 3;
    
    /**
     * Valeur = 0, identifie l'algorithme de Djikstra.
     */
    public static final int ALGO_DJIKSTRA = 0;
    
    /**
     * Valeur = 1, identifie l'algorithme de Brélaz.
     */
    public static final int ALGO_BRELAZ = 1;
    
    /**
     * Valeur = 2, identifie l'algorithme du marquage.
     */
    public static final int ALGO_MARQUAGE = 2;
    
    /**
     * Valeur = 0, utilisé quand on demande à trouver un point sur la courbe de l'arc allant du sommet de départ au point manipulable.
     */
    public static final int PT_COURBE_DEPART = 0;
    
    /**
     * Valeur = 1, utilisé quand on demande à trouver un point sur la courbe de l'arc allant du point manipulable au sommet d'arrivée.
     */
    public static final int PT_COURBE_ARRIVEE = 1;
    
    /**
     * Valeur = 2, utilisé quand on demande à trouver un point sur la courbe de l'arc.
     */
    public static final int PT_COURBE_TOTALE = 2;
    
    /**
     * Valeur = 0, utilisé quand on demande à trouver un point de l'arc en mode sélection (peu de marge).
     */
    public static final int SELECT_ARC = 0;
    
    /**
     * Valeur = 1, utilisé quand on demande à trouver un point sur la courbe d'un arc déjà sélectionné (marge infinie).
     */
    public static final int MOVE_ARC = 1;  
    
    /**
     * Valeur = 35, taille maximale du nom du graphe.
     */
    public static final int TAILLE_MAX_NOM = 35;
    
    /**
     * Valeur = 0, identifie le mode création de la fenêtre de création de graphe.
     */
    public static final int MODE_CREATION = 0;
    
    /**
     * Valeur = 1, identifie le mode édition de la fenêtre de création de graphe.
     */
    public static final int MODE_EDITION = 1;
    
    /**
     * Valeur = 0, identifie l'onglet de la représentation sagittale dans la fenêtre principale.
     */
    public static final int SAGITTALE = 0;
    
    /**
     * Valeur = 1, identifie l'onglet de la représentation matricielle dans la fenêtre principale.
     */
    public static final int MATRICIELLE = 1;
    
    /**
     * Valeur = 1, identifie la recherche des successeurs dans la vérification de la complétion du marquage.
     */
    public static final int SUCCESSEURS = 1;
    
    /**
     * Valeur = -1, identifie la recherche des prédécésseurs dans la vérification de la complétion du marquage.
     */
    public static final int PREDECESSEURS = -1;
    
    /**
     * Valeur = 20, nombre de colonnes maximal de base pour la matrice du graphe lors de l'exportation en LaTeX.
     */
    public static final int ECART_BASIQUE_LATEX = 20;
    
    /**
     * Valeur = 36, maximum de lignes sur une page pour la matrice du graphe lors de l'exportation en LaTeX.
     */
    public static final int MAX_LIGNES_LATEX = 36;
}
