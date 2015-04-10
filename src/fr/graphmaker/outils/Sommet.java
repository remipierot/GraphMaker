package fr.graphmaker.outils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import fr.graphmaker.constantes.CouleursConstantes;
import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.SauvegardeConstantes;

/**
 * Classe permettant de créer des Sommets du Graphe.
 * 		- Chaîne de caractères : Une valeur, un nom, une étiquette qu'on donne au Sommet
 * 		- Liste d'Arc : La liste des arcs partants du Sommet (utile pour connaitre les descendants directs)
 * 		- Forme : La forme servant à dessiner le Sommet
 */
public class Sommet extends Point implements GrapheConstantes, SauvegardeConstantes, Comparable<Sommet>, CouleursConstantes{
	// Variables conservees dans la sauvegarde
	private String valeur;
	private ArrayList<Arc> arcsSortants;
	private Shape forme = new Ellipse2D.Double();
	
	// Variables ignorees dans la sauvegarde
	private boolean selectionne = false;
	private int printed_size;
	private double distance_djikstra = java.lang.Double.POSITIVE_INFINITY;
	private Sommet precedent_djikstra;
	private int tmp_couleurs_proches;
	private int nb_couleurs_proches;
	private Color couleur;
	
	/**
	 * Initialise le Sommet avec les coordonnées (x, y) et la valeur reçue en paramètre.
	 * La liste d'arcs est initialisée vide et la forme de base est une Ellipse.
	 * 
	 * @param x Abscisse du Sommet
	 * @param y Ordonnée du Sommet
	 * @param valeur Valeur, étiquette du Sommet
	 */
	public Sommet(double x, double y, String valeur){
		this(x, y, valeur, new ArrayList<Arc>(), new Ellipse2D.Double());
	}
	
	/**
	 * Initialise le Sommet avec les coordonnées (x, y), la valeur, la liste d'arcs et la forme reçues en paramètre.
	 * 
	 * @param x Abscisse du Sommet
	 * @param y Ordonnée du Sommet
	 * @param valeur Valeur, étiquette du Sommet
	 * @param arcsSortants Liste des arcs partants du Sommet
	 * @param forme Forme du Sommet lorsqu'il est dessiné
	 */
	public Sommet(double x, double y, String valeur, ArrayList<Arc> arcsSortants, Shape forme){
		super.setLocation(x, y);
		this.valeur = valeur;
		this.arcsSortants = arcsSortants;
		if(forme instanceof Ellipse2D)
			this.forme = new Ellipse2D.Double();
		else if(forme instanceof Rectangle2D)
			this.forme = new Rectangle2D.Double();
		else if(forme instanceof RoundRectangle2D)
			this.forme = new RoundRectangle2D.Double();
		else
			this.forme = new Polygon();
	}
	
	/**
	 * Initialise le Sommet avec les coordonnées (x, y), la valeur et la forme reçues en paramètre.
	 * La liste d'arcs est initialisée vide.
	 * 
	 * @param x Abscisse du Sommet
	 * @param y Ordonnée du Sommet
	 * @param valeur Valeur, étiquette du Sommet
	 * @param forme Forme du Sommet lorsqu'il est dessiné
	 */
	public Sommet(double x, double y, String valeur, Shape forme){
		this(x, y, valeur, new ArrayList<Arc>(), forme);
	}
	
	/**
	 * Ajoute l'Arc reçu en paramètre à la liste des Arcs partants du Sommet.
	 * 
	 * @param arcSortant Arc à ajouter a la liste
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void addArcSortant(Arc arcSortant, int type_orientation){
		boolean double_arc = false;
		arcsSortants.add(arcSortant);
		
		if(type_orientation == NON_ORIENTE){
			for(Arc arc: arcSortant.getArrivee().getArcsSortants())
				double_arc |= (arc.getArrivee() == this);
			if(!double_arc)
				arcSortant.getArrivee().addArcSortant(new Arc(arcSortant.getArrivee(), new Point(arcSortant.getBezier()), this, arcSortant.getDistance()), type_orientation);
		}
	}
	
	@Override
	/**
	 * Compare les distance des Sommet pour l'algorithme de Djikstra.
	 * Aucun appel direct à cette methode ne doit être fait, 
	 * elle est automatiquement appelée par la classe Djikstra à chaque comparaison dans la file de priorité utilisée.
	 * 
	 * @param s Sommet à comparer au Sommet courant
	 * @return Un entier, négatif si s a une plus grande distance que le sommet courant, nul si les distances sont égales, positif sinon. 
	 */
	public int compareTo(Sommet s) {
		return java.lang.Double.compare(distance_djikstra, s.distance_djikstra);
	}
	
	/**
	 * Détruit le Sommet et tous les Arcs qui y sont rattachés.
	 * 
	 * @param sommets Liste de Sommet dont on veut supprimer le Sommet courant
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void detruireSommet(ArrayList<Sommet> sommets, int type_orientation){
		ArrayList<Arc> arcs = new ArrayList<Arc>();
			
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getArrivee() == this && a.getDepart() != this)
					arcs.add(a);

		for(Arc a: arcs)
			a.detruireArc(type_orientation);
		
		sommets.remove(this);
	}
	
	/**
	 * Dessine le Sommet avec l'objet Graphics2D fourni en paramètre.
	 * 
	 * @param g2d Objet graphique servant au dessin du Sommet
	 * @param mode Mode dans lequel est l'interface utilisateur (Djikstra, ou Déplacer par exemple)
	 */
	public void draw(Graphics2D g2d, int mode){
		printed_size = g2d.getFontMetrics().stringWidth(valeur)+2;
		
		if(isSelectionne() && mode == DJIKSTRA){
			g2d.setPaint(COL_DJIKSTRA);
			g2d.fill(buildForme(SELECTION));
		}
		else if(isSelectionne() && mode == MARQUAGE){
			g2d.setPaint(COL_MARQUAGE);
			g2d.fill(buildForme(SELECTION));
		}
		else if(isSelectionne()){
			g2d.setPaint(COL_SELECT);
			g2d.fill(buildForme(SELECTION));
		}
		g2d.setPaint(COL_BASE);
		g2d.fill(buildForme(BORDURE));
		g2d.setPaint(COL_CENTRE);

		if(mode == BRELAZ){
			g2d.setPaint(couleur);
		}

		g2d.fill(buildForme(CENTRE));
		
	    printValeur(g2d);
	}

	@Override
	/**
	 * Vérifie que les coordonnées entre le Sommet courant et le paramètre sont identiques.
	 * 
	 * @param o Objet à comparer au Sommet courant
	 * @return Renvoie true si l'objet est un Sommet aux coordonnées identiques à celles du Sommet courant, false sinon
	 */
	public boolean equals(Object o){
		return super.equals(o) && (o instanceof Sommet);
	}

	/**
	 * Renvoie la liste des Arcs partants du Sommet.
	 * 
	 * @return Liste des Arcs partants du Sommet
	 */
	public ArrayList<Arc> getArcsSortants() {
		return arcsSortants;
	}
	
	/**
	 * Renvoie la couleur du Sommet dans le cadre de l'algorithme de Brélaz.
	 * 
	 * @return Couleur pour colorer le Sommet pour l'algorithme de Brélaz
	 */
	public Color getCouleur(){
		return couleur;
	}
	
	/**
	 * Renvoie la distance du Sommet dans le cadre de l'algorithme de Djikstra.
	 * 
	 * @return Distance pour l'algorithme de Djikstra
	 */
	public double getDistanceDjikstra(){
		return distance_djikstra;
	}
	
	/**
	 * Renvoie la forme dessinée du Sommet.
	 * 
	 * @return Forme dessinée
	 */
	public Shape getForme(){
		return forme;
	}
	
	/**
	 * Renvoie la constante de la forme dessinée du Sommet.
	 * 
	 * @return Constante de la forme dessinée
	 * 
	 * @see GrapheConstantes#ELLIPSE
	 * @see GrapheConstantes#RECTANGLE
	 * @see GrapheConstantes#ROUND_RECTANGLE
	 * @see GrapheConstantes#LOSANGE
	 */
	public int getFormeCode(){
		if(forme instanceof Ellipse2D)
			return ELLIPSE;
		else if(forme instanceof Rectangle2D)
			return RECTANGLE;
		else if(forme instanceof RoundRectangle2D)
			return ROUND_RECTANGLE;
		else
			return LOSANGE;
	}
	
	/**
	 * Renvoie le nombre de Sommets colorés liés au Sommet courant dans le cadre de l'algorithme de Brélaz.
	 * 
	 * @return Nombre de Sommets colorés liés au Sommet courant
	 */
	public int getNbCouleursProches(){
		return nb_couleurs_proches;
	}
	
	/**
	 * Renvoie le Sommet calculé comme prédécésseur optimal direct au Sommet courant par l'algorithme de Djikstra.
	 * 
	 * @return Prédécésseur optimal au Sommet courant pour l'algorithme de Djikstra
	 */
	public Sommet getPrecedentDjikstra(){
		return precedent_djikstra;
	}
	
	/**
	 * Renvoie la taille de la valeur du Sommet une fois qu'il est dessiné.
	 * 
	 * @return Taille de la valeur une fois dessinée
	 */
	public int getPrintedSize(){
		return printed_size;
	}
	
	/**
	 * Renvoie le rayon du cercle représentant le Sommet dessiné.
	 * 
	 * @param partie_forme Partie du Sommet (zone de sélection, bordure ou centre)
	 * @return Valeur du rayon du Sommet
	 * 
	 * @see GrapheConstantes#SELECTION
	 * @see GrapheConstantes#BORDURE
	 * @see GrapheConstantes#CENTRE
	 */
	public double getRayon(int partie_forme){
		int toAdd = 0;
		double rayon = 0;

		if(partie_forme == SELECTION)
			toAdd = BORDURE_SOMMET*2;
		else if(partie_forme == BORDURE)
			toAdd = BORDURE_SOMMET;
		else if(partie_forme == CENTRE)
			toAdd = 0;
		
		rayon = (printed_size/2) + RAYON_SOMMET + toAdd;
		if(forme instanceof Polygon)
			rayon = 1.3*RAYON_SOMMET+(printed_size/2)+toAdd;
		
		return rayon;
	}
	
	/**
	 * Renvoie le nombre présumé de Sommets colorés liés au Sommet courant dans le cadre de l'algorithme de Brélaz.
	 * 
	 * @return Nombre présumé de Sommets colorés liés
	 */
	public int getTmpCouleursProches(){
		return tmp_couleurs_proches;
	}
	
	/**
	 * Renvoie la valeur du Sommet.
	 * 
	 * @return Valeur, étiquette du Sommet
	 */
	public String getValeur(){
		return valeur;
	}
	
	/**
	 * Vérifie si les coordonnées reçues en paramètre appartiennent au Sommet dessiné.
	 * 
	 * @param x Abscisse à vérifier
	 * @param y Ordonnee à vérifier
	 * @return true si le Sommet contient bien les coordonnées reçues, false sinon
	 */
	public boolean inBounds(double x, double y){
		double x_min, y_min, x_max, y_max;
		
		x_min = getX() - ((printed_size/2)+RAYON_SOMMET+BORDURE_SOMMET);
		y_min = getY() - (RAYON_SOMMET+BORDURE_SOMMET);
		x_max = getX() + ((printed_size/2)+RAYON_SOMMET+BORDURE_SOMMET);
		y_max = getY() + (RAYON_SOMMET+BORDURE_SOMMET);
		
		return (x_min <= x && x <= x_max && y_min <= y && y <= y_max);
	}
	
	/**
	 * Renvoie la valeur de la sélection.
	 * 
	 * @return true si le Sommet est sélectionné, false sinon
	 */
	public boolean isSelectionne(){
		return selectionne;
	}
	
	/**
	 * Supprime l'Arc reçu en paramètre de la liste des Arcs partants du Sommet.
	 * 
	 * @param arcSortant Arc à supprimer de la liste.
	 */
	public void removeArcSortant(Arc arcSortant){
		arcsSortants.remove(arcSortant);
	}
	
	/**
	 * Réinitialise la position de la courbe de tous les Arcs partants du Sommet.
	 * Cela se traduit par un retour à des droites rectilignes pour chaque Arc plutôt que des courbes.
	 * 
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void resetArcs(int type_orientation){
		for(Arc a: arcsSortants)
			a.calculerBezierInitial(type_orientation);
	}
	
	/**
	 * Réinitialise l'attribut dessine des Arcs pour pouvoir les redessiner à nouveau plus tard.
	 * Si un Arc a son paramètre dessine sur true, il ne sera pas redessiné, alors que s'il est sur false on le redessinera.
	 * 
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void resetDessinArcs(int type_orientation){
		for(Arc a: arcsSortants)
			a.setDessine(false, type_orientation);
	}
	
	/**
	 * Réinitialise les attributs du Sommet courant utilisés pour l'algorithme de Djikstra.
	 * La distance est remise à l'infini et le prédécésseur est remis à null.
	 */
	public void resetDjikstra(){
		distance_djikstra = java.lang.Double.POSITIVE_INFINITY;
		precedent_djikstra = null;
	}
	
	/**
	 * Modifie la couleur du Sommet pour l'algorithme de Brélaz
	 * 
	 * @param couleur Nouvelle couleur pour l'algorithme de Brélaz
	 */
	public void setCouleur(Color couleur){
		this.couleur = couleur;
	}
	
	/**
	 * Modifie la distance du Sommet pour l'algorithme de Djikstra.
	 * 
	 * @param distance_djikstra Nouvelle distance pour l'algorithme de Djikstra
	 */
	public void setDistanceDjikstra(double distance_djikstra){
		this.distance_djikstra = distance_djikstra;
	}
	
	/**
	 * Modifie la forme du Sommet dessiné.
	 * 
	 * @param forme Nouvelle forme du Sommet
	 */
	public void setForme(Shape forme){
		this.forme = forme;
	}
	
	/**
	 * Modifie les coordonnées du Sommet courant et de tous ses Arcs sortants pour qu'ils suivent le mouvement.
	 * 
	 * @param x Nouvelle abscisse
	 * @param y Nouvelle ordonnée
	 * @param sommets Liste des sommets dans laquelle se trouve le Sommet courant, utilisée pour la modification de tous les arcs liés au Sommet
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setLocation(double x, double y, ArrayList<Sommet> sommets, int type_orientation){
		double change_x = x-getX();
		double change_y = y-getY();
		
		setLocation(x, y);
		
		for(Arc a: arcsSortants)
			a.changeBezier(change_x, change_y, MVT_DEPART, type_orientation);
		
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getArrivee() == this && a.getDepart() != this)
						a.changeBezier(change_x, change_y, MVT_ARRIVEE, type_orientation);
	}
	
	/**
	 * Modifie le nombre de couleurs proches du sommet.
	 * 
	 * @param nb_couleur_proche Nouveau nombre de couleurs proches
	 */
	public void setNbCouleurProche(int nb_couleur_proche){
		this.nb_couleurs_proches = nb_couleur_proche;
	}
	
	/**
	 * Remplace le Sommet prédécésseur dans Djikstra par celui reçu en paramètre.
	 * 
	 * @param precedent_djikstra Nouveau prédécésseur pour Djikstra
	 */
	public void setPrecedentDjikstra(Sommet precedent_djikstra){
		this.precedent_djikstra = precedent_djikstra;
	}
	
	/**
	 * Modifie l'état de sélection du Sommet courant.
	 * 
	 * @param select Nouvelle valeur de la sélection
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setSelectionne(boolean select, int type_orientation){
		selectionne = select;
		if(!select)
			for(Arc a: arcsSortants)
				a.setSelectionne(select, type_orientation);
	}
	
	/**
	 * Modifie le nombre présumé de Sommets colores liés au Sommet courant pour l'algorithme de Brélaz.
	 * 
	 * @param couleur_brelaz Nouveau nombre présumé de Sommets colores liés
	 */
	public void setTmpCouleursProches(int couleur_brelaz){
		this.tmp_couleurs_proches = couleur_brelaz;
	}
	
	/**
	 * Modifie la valeur du Sommet par celle reçue en paramètre.
	 * 
	 * @param valeur Nouvelle valeur, étiquette du Sommet
	 */
	public void setValeur(String valeur){
		this.valeur = valeur;
	}
	
	/**
	 * Supprime l'arc boucle du sommet s'il en a un.
	 */
	public void simplifier(){
		Arc toDelete = null;
		
		for(Arc a: arcsSortants)
			if(a.getArrivee() == this)
				toDelete = a;
		
		if(toDelete != null)
			arcsSortants.remove(toDelete);
	}
	
	/**
	 * Renvoie une chaîne de caractères au format "valeur,x,y,forme" représentant le Sommet.
	 * 
	 * @return Chaîne représentant le Sommet
	 */
	@Override
	public String toString(){
		return valeur + ","+super.toString()+","+getFormeCode();
	}
	
	/**
	 * Construit la forme du Sommet dessiné selon les paramètres reçus.
	 * 
	 * @param partie_forme Partie du Sommet (zone de sélection, bordure ou centre)
	 * @return Forme construite correspondant au Sommet courant
	 * 
	 * @see GrapheConstantes#SELECTION
	 * @see GrapheConstantes#BORDURE
	 * @see GrapheConstantes#CENTRE
	 */
	private Shape buildForme(int partie_forme){
		double rayon = getRayon(partie_forme);
		int toAdd = 0;
		
		if(partie_forme == SELECTION)
			toAdd = BORDURE_SOMMET*2;
		else if(partie_forme == BORDURE)
			toAdd = BORDURE_SOMMET;
		else if(partie_forme == CENTRE)
			toAdd = 0;
		
		if(forme instanceof Polygon){
			int[] xpoints = {(int)getX(), (int)(getX() + rayon), (int)getX(), (int)(getX() - rayon)};
			int[] ypoints = {(int)(getY() - 1.5*RAYON_SOMMET - toAdd), (int)getY(), (int)(getY() + 1.5*RAYON_SOMMET + toAdd), (int)getY()};
			int npoints = 4;
			((Polygon) forme).npoints = npoints;
			((Polygon) forme).xpoints = xpoints;
			((Polygon) forme).ypoints = ypoints;
		}
		else
			((RectangularShape) forme).setFrameFromCenter(getX(), getY(), getX() + rayon, getY() + RAYON_SOMMET + toAdd);
		
		if(forme instanceof RoundRectangle2D){
			((RoundRectangle2D) forme).setRoundRect(((RoundRectangle2D) forme).getMinX(), ((RoundRectangle2D) forme).getMinY(), ((RoundRectangle2D) forme).getWidth(), ((RoundRectangle2D) forme).getHeight(), 20, 20);
		}

		return forme;
	}
	
	/**
	 * Dessine la valeur du Sommet avec l'objet Graphics2D reçu en paramètre.
	 * 
	 * @param g2d Objet graphique utilisé pour dessiner la valeur du Sommet
	 */
	private void printValeur(Graphics2D g2d){
		FontMetrics fm = g2d.getFontMetrics();
		
		int x = (int)((int)getRayon(CENTRE)*2 - fm.stringWidth(valeur)) / 2;
		g2d.setPaint(COL_BASE);
		g2d.drawString(valeur, (int)(getX()-getRayon(CENTRE)+x), (int)getY()+4);
	}

	/**
	 * Transforme une chaîne de caractères en un Sommet.
	 * Utilisé lors du chargement.
	 * 
	 * @param s Chaîne de caractères à transformer
	 * @return Sommet construit à partir de la chaîne
	 */
	public static Sommet stringToSommet(String[] s){
		Sommet sommet = null;
		String valeur;
		double x, y;
		int forme_id;
		Shape forme;
		
		if(s.length == NOMBRE_INFOS_SOMMET){
			try{
				valeur = s[VALEUR_SOMMET];
				x = java.lang.Double.parseDouble(s[X_SOMMET]);
				y = java.lang.Double.parseDouble(s[Y_SOMMET]);
				forme_id = Integer.parseInt(s[FORME_SOMMET]);
				
				
				if(forme_id == RECTANGLE)
					forme = new Rectangle2D.Double();
				else if(forme_id == ROUND_RECTANGLE)
					forme = new RoundRectangle2D.Double();
				else if(forme_id == LOSANGE)
					forme = new Polygon();
				else
					forme = new Ellipse2D.Double();
				
				if(x >= 0 && y >= 0)
					sommet = new Sommet(x, y, valeur, forme);
			}
			catch(NumberFormatException e){ }
		}
		
		return sommet;
	}
	
}
