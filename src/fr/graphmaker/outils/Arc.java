package fr.graphmaker.outils;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;

import fr.graphmaker.constantes.CouleursConstantes;
import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.SauvegardeConstantes;

/**
 * Classe permettant de créer des Arcs du Graphe entre deux Sommets.
 * 		- Sommet : Le Sommet de départ de l'Arc
 * 		- Sommet : Le Sommet d'arrivée de l'Arc
 * 		- Point : Le Point manipulable de la courbe, utilisé pour modifier l'allure de la courbe
 * 		- Point : Le point correspondant à la position de la flèche si le Graphe est orienté
 * 		- Chaîne de caractères : Valuation, étiquette de l'Arc si le Graphe est valué ou étiqueté  
 * 		- Forme : Style de tracé de l'Arc
 */
public class Arc implements GrapheConstantes, SauvegardeConstantes, CouleursConstantes{
	// Variables conservees dans la sauvegarde
	private Sommet depart;
	private Point bezier;
	private Sommet arrivee;
	private Point pos_fleche;
	private String distance = NO_DISTANCE;
	private BasicStroke forme = STYLE_ARC_SIMPLE_PLEIN;
	
	// Variables ignorees dans la sauvegarde
	private Point ctrl_arrivee;
	private Point ctrl_depart;
	private boolean selectionne;
	private boolean dessine = false;
	private Shape courbe_depart, courbe_arrivee;
	
	/**
	 * Initialise l'Arc avec les paramètres reçus.
	 * Les points de contrôle de la courbe sont calculés automatiquement.
	 * Le style de dessin de base est un trait simple.
	 * L'étiquette de base est NO_DISTANCE.
	 * 
	 * @param depart Sommet de départ
	 * @param bezier Point manipulable de la courbe
	 * @param arrivee Sommet d'arrivée
	 * 
	 * @see GrapheConstantes#NO_DISTANCE
	 * @see GrapheConstantes#STYLE_ARC_SIMPLE_PLEIN
	 */
	public Arc(Sommet depart, Point bezier, Sommet arrivee){
		this.depart = depart;
		this.bezier = bezier;
		this.arrivee = arrivee;
		ctrl_depart = new Point();
		ctrl_arrivee = new Point();
		pos_fleche = new Point(bezier);
		calculerCtrl();
		refreshCourbes();
	}
	
	/**
	 * Initialise l'Arc avec les paramètres reçus.
	 * Les points de contrôle de la courbe sont calculés automatiquement.
	 * L'étiquette de base est NO_DISTANCE.
	 * 
	 * @param depart Sommet de départ
	 * @param bezier Point manipulable de la courbe
	 * @param arrivee Sommet d'arrivée
	 * @param forme Style de dessin de la courbe
	 * 
	 * @see GrapheConstantes#NO_DISTANCE
	 */
	public Arc(Sommet depart, Point bezier, Sommet arrivee, BasicStroke forme){
		this(depart, bezier, arrivee);
		this.forme = forme;
	}
	
	/**
	 * 
	 * @param depart Sommet de départ
	 * @param bezier Point manipulable de la courbe
	 * @param arrivee Sommet d'arrivée
	 * @param fleche Point correspondant a la flèche
	 * @param distance Valeur, étiquette de l'Arc
	 * @param forme Style de dessin de la courbe
	 */
	public Arc(Sommet depart, Point bezier, Sommet arrivee, Point fleche, String distance, BasicStroke forme){
		this(depart, bezier, arrivee, distance);
		this.pos_fleche = fleche;
		this.forme = forme;
	}
	
	/**
	 * Initialise l'Arc avec les paramètres reçus.
	 * Les points de contrôle de la courbe sont calculés automatiquement.
	 * Le style de dessin de base est un trait simple.
	 * 
	 * @param depart Sommet de départ
	 * @param bezier Point manipulable de la courbe
	 * @param arrivee Sommet d'arrivée
	 * @param distance Valeur, étiquette de l'Arc
	 * 
	 * @see GrapheConstantes#STYLE_ARC_SIMPLE_PLEIN
	 */
	public Arc(Sommet depart, Point bezier, Sommet arrivee, String distance){
		this(depart, bezier, arrivee);
		this.distance = distance;
	}
	
	/**
	 * Calcule les coordonnées initiales du Point manipulable de la courbe.
	 * Les coordonnées initiales correspondent au milieu du segment entre le Sommet de départ et le Sommet d'arrivée.
	 * 
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void calculerBezierInitial(int type_orientation){
		double x = 1+(depart.getX()+arrivee.getX())/2;
		double y = 1+(depart.getY()+arrivee.getY())/2;
		
		if(depart == arrivee){
			x = depart.getX()-2*(RAYON_SOMMET+BORDURE_SOMMET);
			y = depart.getY()-2*(RAYON_SOMMET+BORDURE_SOMMET);;
		}
		
		setBezierLocation(x, y, type_orientation);
		
		if(type_orientation == ORIENTE && depart!=arrivee){
			for(Arc a: arrivee.getArcsSortants()){
				if(a.arrivee == depart){
					a.bezier.setLocation(x, y);
					bezier.setLocation(x, y);
					if((depart.getX() < arrivee.getX() && depart.getY() < arrivee.getY())
							|| (depart.getX() > arrivee.getX() && depart.getY() > arrivee.getY())){
						a.changeBezier(25, -25, MVT_DEPART, type_orientation);
						changeBezier(-25, 25, MVT_ARRIVEE, type_orientation);
					}
					else{
						a.changeBezier(25, 25, MVT_DEPART, type_orientation);
						changeBezier(-25, -25, MVT_ARRIVEE, type_orientation);
					}
					a.pos_fleche.setLocation(a.bezier);
				}
			}
		}
		
		pos_fleche.setLocation(bezier);
		refreshCourbes();
		if(type_orientation == NON_ORIENTE)
			for(Arc a: arrivee.getArcsSortants())
				if(a.getArrivee() == depart){
					a.bezier = new Point(bezier);
					a.calculerCtrl();
					a.forme = forme;
					a.pos_fleche = new Point(pos_fleche);
					a.distance = distance;
					a.refreshCourbes();
				}
				
	}
	
	/**
	 * Calcule la nouvelle position de la flèche d'après les variations de coordonnées reçues en paramètre.
	 * 
	 * @param change_x Variation de l'abscisse du Point manipulable
	 * @param change_y Variation de l'ordonnée du Point manipulable
	 */
	public void calculerPosFleche(double change_x, double change_y){
		PathIterator courbe_iterator;
		Point tmp_pt = null;
		courbe_iterator = courbe_depart.getPathIterator(null, 0.0001);
		double scale_dep = depart.distance(pos_fleche)/(depart.distance(pos_fleche)+arrivee.distance(pos_fleche)), scale_arr = arrivee.distance(pos_fleche)/(depart.distance(pos_fleche)+arrivee.distance(pos_fleche));
		double tmp_scale_dep, tmp_scale_arr;
		
		scale_dep = BoiteAOutil.troncature(scale_dep, 2.0);
		scale_arr = BoiteAOutil.troncature(scale_arr, 2.0);

		double[] coords = new double[2];
        
		if(depart == arrivee)
        	pos_fleche.setLocation(pos_fleche.getX()+change_x, pos_fleche.getY()+change_y);
		else{
	        while(!courbe_iterator.isDone()) {
	        	courbe_iterator.currentSegment(coords);
	        	tmp_pt = new Point(coords[0], coords[1]);
	    		tmp_scale_dep = depart.distance(tmp_pt)/(depart.distance(tmp_pt)+arrivee.distance(tmp_pt));
	    		tmp_scale_arr = arrivee.distance(tmp_pt)/(depart.distance(tmp_pt)+arrivee.distance(tmp_pt));
	    		
	    		tmp_scale_dep = BoiteAOutil.troncature(tmp_scale_dep, 2.0);
	    		tmp_scale_arr = BoiteAOutil.troncature(tmp_scale_arr, 2.0);
	    		
	        	if(tmp_scale_dep == scale_dep && tmp_scale_arr == scale_arr && !depart.inBounds(tmp_pt.getX(), tmp_pt.getY()) && !arrivee.inBounds(tmp_pt.getX(), tmp_pt.getY()))
	        		pos_fleche = tmp_pt;
	        		
	            courbe_iterator.next();
	        }
	
	        courbe_iterator = courbe_arrivee.getPathIterator(null, 0.0001);
	        while(!courbe_iterator.isDone()) {
	        	courbe_iterator.currentSegment(coords);
	        	tmp_pt = new Point(coords[0], coords[1]);
	    		tmp_scale_dep = depart.distance(tmp_pt)/(depart.distance(tmp_pt)+arrivee.distance(tmp_pt));
	    		tmp_scale_arr = arrivee.distance(tmp_pt)/(depart.distance(tmp_pt)+arrivee.distance(tmp_pt));
	    		
	    		tmp_scale_dep = BoiteAOutil.troncature(tmp_scale_dep, 2.0);
	    		tmp_scale_arr = BoiteAOutil.troncature(tmp_scale_arr, 2.0);
	    		
	        	if(tmp_scale_dep == scale_dep && tmp_scale_arr == scale_arr && !depart.inBounds(tmp_pt.getX(), tmp_pt.getY()) && !arrivee.inBounds(tmp_pt.getX(), tmp_pt.getY()))
	        		pos_fleche = tmp_pt;
	        		
	            courbe_iterator.next();
	        }
		}   
	}
	
	/**
	 * Modifie les coordonnées du point manipulable de la courbe en lui ajoutant les coordonnnées reçues en paramètre.
	 * 
	 * @param change_x Variation à appliquer à l'abscisse du point manipulable
	 * @param change_y Variation à appliquer à l'ordonnée du point manipulable
	 * @param sommet_deplace Indique le sommet qui est en mouvement
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#MVT_DEPART
	 * @see GrapheConstantes#MVT_ARRIVEE
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void changeBezier(double change_x, double change_y, boolean sommet_deplace, int type_orientation){
		Sommet mouvement, immobile;
		
		/*
		 *  On identifie le sommet en mouvement.
		 *  Grace a ca, on peut definir l'axe de rotation autour de l'autre sommet, qui est immobile.
		 */
		if(sommet_deplace == MVT_DEPART){
			mouvement = depart;
			immobile = arrivee;
		}
		else{
			mouvement = arrivee;
			immobile = depart;
		}

        /*
         * On doit deplacer bezier comme si le sommet immobile etait l'origine du repere avant de le transformer.
         * On cree donc un nouveau point en soustrayant les coordonnes du sommet immobile a bezier :
         * 		nouveau_point = bezier - immobile
         * On applique ensuite les transformations affines creees dans getBezierRotation a ce point, puis on stocke le resultat dans bezier.
         */
        if(depart != arrivee){
        	getBezierTransformation(change_x, change_y, mouvement, immobile).transform(new Point(bezier.getX()-immobile.getX(), bezier.getY()-immobile.getY()), bezier);
        	getBezierTransformation(change_x, change_y, mouvement, immobile).transform(new Point(pos_fleche.getX()-immobile.getX(), pos_fleche.getY()-immobile.getY()), pos_fleche);
        }
        else{
        	setBezierLocation(bezier.getX()+change_x, bezier.getY()+change_y, type_orientation);
        }
        calculerCtrl();
        refreshCourbes();
	}
	
	/**
	 * Vérifie que le point de coordonnées (x,y) appartient à la courbe de l'Arc.
	 * 
	 * @param x Abscisse à vérifier
	 * @param y Ordonnée à vérifier
	 * @param mode Mode dans lequel on se place (Déplacement de l'arc ou sélection)
	 * @return true si la courbe contient le point, false sinon
	 * 
	 * @see GrapheConstantes#SELECT_ARC
	 * @see GrapheConstantes#MOVE_ARC
	 */
	public boolean contains(double x, double y, int mode){
		return (findPoint(x, y, PT_COURBE_TOTALE, mode) != null);
	}
	
	/**
	 * Utilisé pour désorienter un Arc.
	 * Créé son jumeau s'il n'existe pas déjà pour pouvoir utiliser les algorithmes sur la version non-orienteée du graphe.
	 */
	public void desorienter(){
		boolean deja_double = false;
		if(depart != arrivee){
			for(Arc a: arrivee.getArcsSortants()){
				deja_double |= (a.getArrivee() == depart);
				if(a.getArrivee() == depart){
					a.bezier = new Point(bezier);
					a.calculerCtrl();
					a.forme = forme;
					a.pos_fleche = new Point(pos_fleche);
					a.distance = distance;
				}
			}
			if(!deja_double){
				Arc a = new Arc(arrivee, new Point(bezier), depart, pos_fleche, distance, forme);
				a.refreshCourbes();
				arrivee.addArcSortant(a, NON_ORIENTE);
			}
		}
	}
	
	/**
	 * Détruit l'Arc en le supprimant de la liste des Arcs de ses sommets.
	 * 
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void detruireArc(int type_orientation){
		depart.removeArcSortant(this);
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants()){
				if(a.arrivee == depart){
					arrivee.removeArcSortant(a);
					break;
				}
			}
		}
	}
	
	/**
	 * Dessine l'Arc sur l'objet Graphics2D fourni en paramètre.
	 * 
	 * @param g2d Objet graphique servant au dessin de l'Arc
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * @param mode Mode dans lequel est l'interface utilisateur (Djikstra, ou Déplacer par exemple)
	 * @param type_simple Simplicité du Graphe concerné, si il accepte (NON_SIMPLE) ou non les boucles (SIMPLE)
	 * @param type_arc Type d'Arc dans le Graphe concerné, VALUE, NON_VALUE ou ETIQUETE
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 * 
	 * @see GrapheConstantes#CREER_SOMMET
	 * @see GrapheConstantes#CREER_ARC
	 * @see GrapheConstantes#DEPLACER
	 * @see GrapheConstantes#SUPPRIMER
	 * @see GrapheConstantes#SAUVEGARDER
	 * @see GrapheConstantes#CHARGER
	 * @see GrapheConstantes#RESET_ARCS
	 * @see GrapheConstantes#DEPLACER_FLECHE
	 * @see GrapheConstantes#DEPLACER_GRAPHE
	 * @see GrapheConstantes#CLIQUE
     * @see GrapheConstantes#DJIKSTRA
     * @see GrapheConstantes#BRELAZ
     * @see GrapheConstantes#MARQUAGE
     * @see GrapheConstantes#VIDER
     * 
     * @see GrapheConstantes#SIMPLE
     * @see GrapheConstantes#NON_SIMPLE
     * 
     * @see GrapheConstantes#NON_VALUE
     * @see GrapheConstantes#VALUE
     * @see GrapheConstantes#ETIQUETE
	 */
	public void draw(Graphics2D g2d, int type_orientation, int mode, int type_simple, int type_arc){
		dessine = true;
		
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants())
				if(a.arrivee == depart)
					a.dessine = true;
		}
		
		/* Dessine les droites et points vers les points d'influence de la courbe de bezier
		Line2D dd = new Line2D.Double();
		g2d.setStroke(STYLE_ARC_SIMPLE_POINTILLE);
		dd.setLine(depart, ctrl_depart);
		g2d.draw(dd);
		dd.setLine(bezier, ctrl_depart);
		g2d.draw(dd);
		dd.setLine(bezier, ctrl_arrivee);
		g2d.draw(dd);
		dd.setLine(arrivee, ctrl_arrivee);
		g2d.draw(dd);
		g2d.fillOval((int)ctrl_depart.getX()-RAYON_SOMMET/4, (int)ctrl_depart.getY()-RAYON_SOMMET/4, RAYON_SOMMET/2, RAYON_SOMMET/2);
		g2d.fillOval((int)ctrl_arrivee.getX()-RAYON_SOMMET/4, (int)ctrl_arrivee.getY()-RAYON_SOMMET/4, RAYON_SOMMET/2, RAYON_SOMMET/2);
		*/
		
		if(type_simple == NON_SIMPLE || (type_simple == SIMPLE && depart != arrivee)){
			if(selectionne && (mode == DJIKSTRA || mode == MARQUAGE)){
				if(mode == DJIKSTRA)
					g2d.setColor(COL_DJIKSTRA);
				else if(mode == MARQUAGE)
					g2d.setColor(COL_MARQUAGE);
				
				g2d.setStroke(new BasicStroke(4.0f));
				g2d.draw(courbe_depart);
		    	g2d.draw(courbe_arrivee);
		    	if(type_orientation == ORIENTE && buildFleche(CENTRE)!=null)
		    		g2d.fill(buildFleche(SELECTION));
			}
			g2d.setColor(COL_BASE);
			g2d.setStroke(forme);
			g2d.draw(courbe_depart);
	    	g2d.draw(courbe_arrivee);
	    	
	    	if(forme.equals(STYLE_ARC_DOUBLE_PLEIN) || forme.equals(STYLE_ARC_DOUBLE_POINTILLE)){
	    		if(selectionne && (mode == DJIKSTRA || mode == MARQUAGE)){
	    			if(mode == DJIKSTRA)
						g2d.setColor(COL_DJIKSTRA);
					else if(mode == MARQUAGE)
						g2d.setColor(COL_MARQUAGE);
	    		}
	    		else
	    			g2d.setColor(COL_CENTRE);
				g2d.setStroke(new BasicStroke(1.5f));
				g2d.draw(courbe_depart);
		    	g2d.draw(courbe_arrivee);
			}
	    	
			g2d.setColor(COL_BASE);
	    	if(type_orientation == ORIENTE && buildFleche(CENTRE)!=null)
	    		g2d.fill(buildFleche(CENTRE));
	        
	    	if(type_arc == VALUE || (type_arc == ETIQUETE && !distance.equals(NO_DISTANCE)))
	    		printDistance(g2d);
		}
	}
	
	/**
	 * Renvoie le point de la courbe correspondant aux coordonnées reçues en paramètre.
	 * Une marge d'erreur calculée selon le mode de l'interface utilisateur est incluse dans la sélection du point correspondant.
	 * 
	 * @param x Abscisse du Point a renvoyer
	 * @param y Ordonnee du Point a renvoyer
	 * @param mode Mode dans lequel on se place (Deplacement de l'arc ou sélection), détermine la marge d'erreur
	 * @return Le point de la courbe correspondant aux coordonnées si il existe, null sinon
	 * 
	 * @see GrapheConstantes#SELECT_ARC
	 * @see GrapheConstantes#MOVE_ARC
	 */
	public Point findPoint(double x, double y, int mode){
		return findPoint(x, y, PT_COURBE_TOTALE, mode);
	}
	
	/**
	 * Renvoie le Sommet d'arrivée.
	 * 
	 * @return Sommet d'arrivee
	 */
	public Sommet getArrivee() {
		return arrivee;
	}
	
	/**
	 * Renvoie le Point manipulable de la courbe de l'Arc.
	 * 
	 * @return Point manipulable de la courbe
	 */
	public Point getBezier() {
		return bezier;
	}
	
	/**
	 * Renvoie le Sommet de départ.
	 * 
	 * @return Sommet de depart
	 */
	public Sommet getDepart() {
		return depart;
	}

	/**
	 * Renvoie la valeur, l'étiquette de l'Arc.
	 * 
	 * @return Valeur, étiquette de l'Arc
	 */
	public String getDistance(){
		return distance;
	}

	/**
	 * Renvoie le style de tracé de l'Arc.
	 * 
	 * @return Style de trace
	 */
	public BasicStroke getForme(){
		return forme;
	}
	
	/**
	 * Renvoie le Point correspondant a la position de la flèche.
	 * 
	 * @return Position de la fleche
	 */
	public Point getPosFleche(){
		return pos_fleche;
	}
	
	/**
	 * Renvoie la constante du style de tracé de l'Arc.
	 * 
	 * @return Constante du style de tracé
	 * 
	 * @see GrapheConstantes#ARC_SIMPLE_PLEIN
	 * @see GrapheConstantes#ARC_SIMPLE_POINTILLE
	 * @see GrapheConstantes#ARC_DOUBLE_PLEIN
	 * @see GrapheConstantes#ARC_DOUBLE_POINTILLE
	 */
    public int getStyleCode(){
    	if(forme.equals(STYLE_ARC_SIMPLE_PLEIN))
    		return ARC_SIMPLE_PLEIN;
    	else if(forme.equals(STYLE_ARC_SIMPLE_POINTILLE))
    		return ARC_SIMPLE_POINTILLE;
    	else if(forme.equals(STYLE_ARC_DOUBLE_PLEIN))
    		return ARC_DOUBLE_PLEIN;
    	else
    		return ARC_DOUBLE_POINTILLE;
    }
	
	/**
	 * Vérifie si l'Arc a déjà été dessiné ou non.
	 * 
	 * @return true si l'Arc est dessiné, false sinon
	 */
	public boolean isDessine(){
		return dessine;
	}
	
	/**
	 * Vérifie si l'Arc est sélectionné ou non.
	 * 
	 * @return true si l'Arc est sélectionné, false sinon
	 */
	public boolean isSelectionne(){
		return selectionne;
	}
	
	/**
	 * Modifie la position du Point manipulable de la courbe.
	 * 
	 * @param x Nouvelle abscisse du Point manipulable
	 * @param y Nouvelle ordonnée du Point manipulable
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setBezierLocation(double x, double y, int type_orientation){
		double change_x = x-bezier.getX(), change_y = y-bezier.getY();
		bezier.setLocation(x, y);
		calculerCtrl();
		refreshCourbes();
		calculerPosFleche(change_x, change_y);
		
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants()){
				if(a.arrivee == depart){
					a.bezier.setLocation(x, y);
					a.calculerCtrl();
					a.refreshCourbes();
					a.pos_fleche.setLocation(pos_fleche);
					break;
				}
			}
		}
	}
	
	/**
	 * Modifie la valeur du booléen dessine.
	 * Si il vaut true, l'arc ne sera plus dessiné, s'il vaut false, l'arc sera dessiné à l'appel de repaint
	 * 
	 * @param dessin Nouvelle valeur du boolean dessine
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setDessine(boolean dessin, int type_orientation){
		dessine = dessin;
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants()){
				if(a.arrivee == depart){
					a.dessine = dessin;
					break;
				}
			}
		}
	}
	
	/**
	 * Modifie la valuation, l'étiquette de l'Arc.
	 * 
	 * @param distance Nouvelle valeur/étiquette
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * @param type_arc Type d'Arc dans le Graphe concerné, VALUE, NON_VALUE ou ETIQUETE
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 * 
	 * @see GrapheConstantes#NON_VALUE
	 * @see GrapheConstantes#VALUE
	 * @see GrapheConstantes#ETIQUETE
	 */
	public void setDistance(String distance, int type_orientation, int type_arc){
		if(type_arc != NON_VALUE){
			this.distance = distance;
			if(type_orientation == NON_ORIENTE){
				for(Arc a: arrivee.getArcsSortants())
					if(a.arrivee ==depart)
						a.distance = distance;
			}
		}
	}
	
	/**
	 * Modifie la forme de l'Arc.
	 * 
	 * @param forme Nouvelle forme
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setForme(BasicStroke forme, int type_orientation){
		this.forme = forme;
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants())
				if(a.arrivee == depart)
					a.forme = forme;
		}
	}
	
	/**
	 * Modifie la position de la flèche en fonction des coordonnées reçues.
	 * 
	 * @param x Nouvelle abscisse (avec une marge d'erreur)
	 * @param y Nouvelle ordonnée (avec une marge d'erreur)
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * @param mode Mode dans lequel on se place (Déplacement de l'arc ou sélection), détermine la marge d'erreur
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 * 
	 * @see GrapheConstantes#SELECT_ARC
	 * @see GrapheConstantes#MOVE_ARC
	 */
	public void setPosFleche(double x, double y, int type_orientation, int mode){
		if(contains(x, y, mode) && depart!=arrivee){
			pos_fleche = findPoint(x, y, mode);
			if(type_orientation == NON_ORIENTE){
				for(Arc a: arrivee.getArcsSortants()){
					if(a.arrivee == depart){
						a.pos_fleche = new Point(pos_fleche);
						break;
					}
				}
			}
		}
	}
		
	/**
	 * Change la valeur de selectionne. Un Arc sélectionné sera dessiné différemment (algorithme de Djikstra par exemple).
	 * 
	 * @param select Nouvelle valeur de selectionne
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public void setSelectionne(boolean select, int type_orientation){
		selectionne = select;
		if(type_orientation == NON_ORIENTE){
			for(Arc a: arrivee.getArcsSortants()){
				if(a.arrivee == depart){
					a.selectionne = select;
					break;
				}
			}
		}
	}
	
    /**
	 * Renvoie une chaîne de caractères au format "depart_x,depart_y,bezier_x,bezier_y,arrivee_x, arrivee_y,fleche_x,fleche_y,distance,style" représentant l'Arc.
	 * 
	 * @return Chaîne représentant l'Arc
	 */
	@Override
	public String toString(){
		return depart.getX()+","+depart.getY()+","+bezier+","+arrivee.getX()+","+arrivee.getY()+","+pos_fleche+","+distance+","+getStyleCode();
	}
	
	/**
	 * Construis la courbe correspondant à l'arc.
	 * 
	 * @param sens Détermine la partie de la courbe à dessiner (de Depart vers Bezier ou de Bezier vers Arrivee)
	 * @return Courbe construite sous la forme d'un objet QuadCurve2D
	 * 
	 * @see GrapheConstantes#COURBE_DEPART
	 * @see GrapheConstantes#COURBE_ARRIVEE
	 */
	private Shape buildCourbe(boolean sens) {
		Shape courbe = new QuadCurve2D.Double();
		
		if(sens == COURBE_DEPART)
			((QuadCurve2D) courbe).setCurve(depart.getX(), depart.getY(), ctrl_depart.getX(), ctrl_depart.getY(), bezier.getX(), bezier.getY());
		else if(sens == COURBE_ARRIVEE)
			((QuadCurve2D) courbe).setCurve(bezier.getX(), bezier.getY(), ctrl_arrivee.getX(), ctrl_arrivee.getY(), arrivee.getX(), arrivee.getY());
		
		if(depart == arrivee && COURBE_DEPART){
			courbe = new Ellipse2D.Double();
			((Ellipse2D) courbe).setFrameFromCenter(bezier.distance(arrivee)/2, 0, bezier.distance(arrivee), 20);
			courbe = getBezierRotation().createTransformedShape(courbe);
		}
		
		return courbe;
	}
	
	/**
	 * Construis la flèche de l'arc en mode orienté.
	 * 
	 * @param partie_forme Permet de dessiner le contour de la flèche quand ce paramètre vaut SELECTION.
	 * @return Flèche construite sous la forme d'un objet Polygon
	 * 
	 * @see GrapheConstantes#SELECTION
	 */
	private Shape buildFleche(int partie_forme){
		Polygon fleche;
        int toAdd = 0;
        
        if(partie_forme == SELECTION)
        	toAdd = 4;
        
        AffineTransform t = new AffineTransform();
        if(buildPartiesFleche()[0] != null && buildPartiesFleche()[1] != null){
			BoiteAOutil.appliquer_translation(t, pos_fleche);
			
			if(depart == arrivee)
				BoiteAOutil.appliquer_rotation(t, ctrl_depart, ctrl_arrivee);
			else
				BoiteAOutil.appliquer_rotation(t, buildPartiesFleche()[0], buildPartiesFleche()[1]);
        
	        fleche = new Polygon(new int[] {(TAILLE_FLECHE/2)+toAdd, -(TAILLE_FLECHE/2)-toAdd/2, -(TAILLE_FLECHE/2)-toAdd/2}, 
	        						new int[] {0, -TAILLE_FLECHE-toAdd, TAILLE_FLECHE+toAdd}, 3);
	        
	        return t.createTransformedShape(fleche);
        }
        else
        	return null;
	}
	
	/**
	 * Renvoie les points de départ et de fin de la flèche sur la courbe de l'arc.
	 * 
	 * @return Tableau de Point correspondant au point de départ et de fin de la flèche sur l'arc.
	 */
	private Point[] buildPartiesFleche(){
		PathIterator courbe_iterator;
		double[] coords = new double[2];
		Point[] return_points = new Point[2];
		Point tmp_pt = null, start_pt = null, end_pt = null;
		boolean test_taille_start, test_taille_end;
		double tmp_dist = 0;
		
		courbe_iterator = courbe_depart.getPathIterator(null, 0.0001);
		
        while(!courbe_iterator.isDone()) {
        	courbe_iterator.currentSegment(coords);
        	tmp_pt = new Point(coords[0], coords[1]);
        	tmp_dist =  BoiteAOutil.troncature(tmp_pt.distance(pos_fleche));

        	test_taille_start = (tmp_dist >= (TAILLE_FLECHE/2)-3 && tmp_dist <= (TAILLE_FLECHE/2)+3);
        	if(start_pt != null)
        		test_taille_end = (BoiteAOutil.troncature(tmp_pt.distance(start_pt)) >= TAILLE_FLECHE-3 && BoiteAOutil.troncature(tmp_pt.distance(start_pt)) <= TAILLE_FLECHE+3);
        	else
        		test_taille_end = false;
        	
        	if(test_taille_start && start_pt == null)
        		start_pt = new Point(tmp_pt);
        	else if(start_pt != null && (test_taille_start || test_taille_end))
        		end_pt = new Point(tmp_pt);
        	
            courbe_iterator.next();
        }
        
        if(start_pt == null || end_pt == null){
	        courbe_iterator = courbe_arrivee.getPathIterator(null, 0.0001);
	        while(!courbe_iterator.isDone()) {
	        	courbe_iterator.currentSegment(coords);
	        	tmp_pt = new Point(coords[0], coords[1]);
	        	tmp_dist =  BoiteAOutil.troncature(tmp_pt.distance(pos_fleche));
	        	
	        	test_taille_start = (tmp_dist >= (TAILLE_FLECHE/2)-3 && tmp_dist <= (TAILLE_FLECHE/2)+3);
	        	if(start_pt != null)
	        		test_taille_end = (BoiteAOutil.troncature(tmp_pt.distance(start_pt)) >= TAILLE_FLECHE-3 && BoiteAOutil.troncature(tmp_pt.distance(start_pt)) <= TAILLE_FLECHE+3);
	        	else
	        		test_taille_end = false;
	        	
	        	if(test_taille_start && start_pt == null)
	        		start_pt = new Point(tmp_pt);
	        	else if(start_pt != null && (test_taille_start || test_taille_end))
	        		end_pt = new Point(tmp_pt);
	        	
	            courbe_iterator.next();
	        }
        }
        
        return_points[0] = start_pt;
        return_points[1] = end_pt;
        
        return return_points;
	}

	/**
	 * Met à jour les points de contrôle de la courbe en fonction des coordonnées du point manipulable.
	 */
	private void calculerCtrl(){
		double x_dep, x_bez, y_dep, y_bez, x_arr, y_arr, 
				ctrl_x_dep, ctrl_y_dep, ctrl_x_arr, ctrl_y_arr, 
				offset_x, offset_y, scale_ctrl_dep, scale_ctrl_arr;
		
		scale_ctrl_dep = 2*depart.distance(bezier)/(depart.distance(bezier)+bezier.distance(arrivee));
		scale_ctrl_arr = 2*bezier.distance(arrivee)/(depart.distance(bezier)+bezier.distance(arrivee));
		x_dep = depart.getX();
    	y_dep = depart.getY();
    	x_bez = bezier.getX();
    	y_bez = bezier.getY();
    	x_arr = arrivee.getX();
    	y_arr = arrivee.getY();
    	
    	offset_x = (x_arr - x_dep) / 5.0;
    	offset_y = (y_arr - y_dep) / 5.0;
    	ctrl_x_dep = x_bez - scale_ctrl_dep*offset_x;
    	ctrl_y_dep = y_bez - scale_ctrl_dep*offset_y;
    	ctrl_x_arr = x_bez + scale_ctrl_arr*offset_x;
    	ctrl_y_arr = y_bez + scale_ctrl_arr*offset_y;
    	ctrl_depart.setLocation(ctrl_x_dep, ctrl_y_dep);
		ctrl_arrivee.setLocation(ctrl_x_arr, ctrl_y_arr);
    	
    	if(depart == arrivee){
    		ctrl_x_dep = ctrl_x_arr = bezier.distance(arrivee);
    		ctrl_y_dep = -3*RAYON_SOMMET;
    		ctrl_y_arr = 3*RAYON_SOMMET;
    		getBezierRotation().transform(new Point(ctrl_x_dep, ctrl_y_dep), ctrl_depart);
    		getBezierRotation().transform(new Point(ctrl_x_arr, ctrl_y_arr), ctrl_arrivee);
    	}
	}
	
	/**
	 * Renvoie le point de la courbe le plus proche des coordonnées reçues en paramètre.
	 * 
	 * @param x Abscisse du point demandé
	 * @param y Ordonnée du point demandé
	 * @param courbe Partie de la courbe voulue
	 * @param mode Mode dans lequel on se place (Déplacement de l'arc ou sélection), détermine la marge d'erreur
	 * @return Le point de la courbe correspondant aux coordonnées si il existe, null sinon
	 * 
	 * @see GrapheConstantes#PT_COURBE_DEPART
	 * @see GrapheConstantes#PT_COURBE_ARRIVEE
	 * @see GrapheConstantes#PT_COURBE_TOTALE
	 * 
	 * @see GrapheConstantes#SELECT_ARC
	 * @see GrapheConstantes#MOVE_ARC
	 */
	private Point findPoint(double x, double y, int courbe, int mode){
		boolean trouve = false;
		PathIterator courbe_iterator;
		double tmp_dist = Double.POSITIVE_INFINITY;
		Point tmp_pt = null, init_pt = new Point(x, y), return_point = null;
		if(courbe == PT_COURBE_DEPART || courbe == PT_COURBE_TOTALE)
			courbe_iterator = courbe_depart.getPathIterator(null, 0.0001);
		else
			courbe_iterator = courbe_arrivee.getPathIterator(null, 0.0001);
		
		double[] coords = new double[2];
		int range = (RAYON_SOMMET+BORDURE_SOMMET);
		
		if(mode == MOVE_ARC)
			range = Integer.MAX_VALUE;
        
        while(!courbe_iterator.isDone()) {
        	courbe_iterator.currentSegment(coords);
        	if(coords[0]-range <= x && x <= coords[0]+range && coords[1]-range <= y && y <= coords[1]+range){
        		trouve = true;
        		tmp_pt = new Point(coords[0], coords[1]);
    		}
        	
        	if(trouve && tmp_dist > tmp_pt.distance(init_pt)){
        		tmp_dist = tmp_pt.distance(init_pt);
        		return_point = new Point(tmp_pt);
        	}
        	else if(trouve && tmp_dist < tmp_pt.distance(init_pt))
        		break;
        		
            courbe_iterator.next();
        }
        
        if((!trouve || mode == MOVE_ARC) && courbe == PT_COURBE_TOTALE){
	        courbe_iterator = courbe_arrivee.getPathIterator(null, 0.0001);
	        while(!courbe_iterator.isDone()) {
	        	courbe_iterator.currentSegment(coords);
	        	if(coords[0]-range <= x && x <= coords[0]+range && coords[1]-range <= y && y <= coords[1]+range){
	        		trouve = true;
	        		tmp_pt = new Point(coords[0], coords[1]);
	    		}
	        	
	        	if(trouve && tmp_dist > tmp_pt.distance(init_pt)){
	        		tmp_dist = tmp_pt.distance(init_pt);
	        		return_point = new Point(tmp_pt);
	        	}
	        	
	            courbe_iterator.next();
	        }
        }

        return return_point;
	}
	
	/**
	 * Renvoie la transformation affine correspondant à la rotation dont l'angle est celui entre 
	 * le point manipulable(sur le tracé du cercle) et le sommet arrivee (centre du cercle).
	 * 
	 * @return Rotation nécessaire pour obtenir le point manipulable, avec le sommet d'arrivée pour centre
	 */
	private AffineTransform getBezierRotation(){
		return getBezierTransformation(0, 0, arrivee, arrivee);
	}
	
	/**
	 * Renvoie la transformation affine correspondant à la transformation à appliquer
	 * au point manipulable pour conserver l'allure de la courbe après le déplacement du Sommet depart ou du Sommet arrivee.
	 * 
	 * @param change_x Variation d'abscisse du Sommet en mouvement
	 * @param change_y Variation d'ordonnée du Sommet en mouvement
	 * @param mouvement Sommet en mouvement
	 * @param immobile Sommet immobile
	 * @return Transformation à appliquer au point manipulable
	 */
	private AffineTransform getBezierTransformation(double change_x, double change_y, Sommet mouvement, Sommet immobile){
		double angle, echelle;
		Point old_mouvement, new_mouvement ;

		// Calcul des anciennes et nouvelles coordonnées du Sommet en mouvement
		old_mouvement = new Point(mouvement.getX()-change_x, mouvement.getY()-change_y);
		new_mouvement = new Point(mouvement.getX(), mouvement.getY());
	
		/*
		 *  Calcul de l'angle de rotation et du rapport d'echelle.
		 *  Si l'Arc relie deux Sommets differents, l'angle de rotation que subit le point est tel que :
		 *  	angle_rotation = angle_nouvelle_position - angle_ancienne_position
		 *  Sinon :
		 *  	angle_rotation = angle(immobile, bezier)
		 *  Le rapport d'echelle est tel que :
		 *  	echelle = nouvelle_distance / ancienne_distance
		 */
		angle = BoiteAOutil.angle_transformation(immobile, old_mouvement, immobile, new_mouvement);
		if(mouvement == immobile) 
			angle = BoiteAOutil.angle_sur_cercle(immobile, bezier);
		
		echelle = mouvement.distance(immobile) / immobile.distance(mouvement.getX()-change_x, mouvement.getY()-change_y);
		
		/*
		 *  Creation des transformations affines necessaires.
		 *  On se place sur le sommet immobile comme origine a notre repere.
		 *  On effectue une rotation correspondant a notre angle.
		 *  On applique le rapport d'echelle calcule si l'arc relie deux Sommets differents.
		 */
		AffineTransform t = new AffineTransform();
		BoiteAOutil.appliquer_translation(t, immobile);
		BoiteAOutil.appliquer_rotation(t, angle);
        if(mouvement != immobile)
        	BoiteAOutil.appliquer_echelle(t, echelle);
        
        return t;
	}
	
	/**
	 * Affiche la valeur de l'arc sur la courbe.
	 * 
	 * @param g2d Objet Graphics2D utilisé pour le dessin de la valeur
	 */
	private void printDistance(Graphics2D g2d){
        AffineTransform t = new AffineTransform();
		BoiteAOutil.appliquer_translation(t, pos_fleche);
		
        AffineTransform old = g2d.getTransform();
        g2d.transform(t);
        g2d.drawString(distance, (float)-((float)distance.length()/2)*((float)g2d.getFont().getSize()/2), ((float)-TAILLE_FLECHE-3));
        g2d.setTransform(old);
	}
	
	/**
	 * Met a jour le tracé des courbes en les recalculant.
	 */
	private void refreshCourbes(){
		courbe_depart = buildCourbe(COURBE_DEPART);
		courbe_arrivee = buildCourbe(COURBE_ARRIVEE);
	}
	
	/**
	 * Transforme une chaîne de caractères en un Arc.
	 * Utilisé lors du chargement.
	 * 
	 * @param s Chaîne de caractères à transformer
	 * @return Arc construit à partir de la chaîne
	 */
	public static Arc stringToArc(String[] s){
		Arc arc = null;
		double x_dep, y_dep, x_bez, y_bez, x_arr, y_arr, x_fleche, y_fleche;
		String distance;
		int style_id;
		BasicStroke style;
		
		if(s.length == NOMBRE_INFOS_ARC){
			try{
				x_dep = Double.parseDouble(s[X_DEPART_ARC]);
				y_dep = Double.parseDouble(s[Y_DEPART_ARC]);
				x_bez = Double.parseDouble(s[X_BEZIER_ARC]);
				y_bez = Double.parseDouble(s[Y_BEZIER_ARC]);
				x_arr = Double.parseDouble(s[X_ARRIVEE_ARC]);
				y_arr = Double.parseDouble(s[Y_ARRIVEE_ARC]);
				x_fleche = Double.parseDouble(s[X_FLECHE_ARC]);
				y_fleche = Double.parseDouble(s[Y_FLECHE_ARC]);
				distance = s[DISTANCE_ARC];
				style_id = Integer.parseInt(s[STYLE_ARC]);
				
				
				if(style_id == ARC_SIMPLE_POINTILLE)
					style = STYLE_ARC_SIMPLE_POINTILLE;
				else if(style_id == ARC_DOUBLE_PLEIN)
					style = STYLE_ARC_DOUBLE_PLEIN;
				else if(style_id == ARC_DOUBLE_POINTILLE)
					style = STYLE_ARC_DOUBLE_POINTILLE;
				else
					style = STYLE_ARC_SIMPLE_PLEIN;
				
				if(x_dep>=0 && y_dep>=0 && x_bez>=0 && y_bez>=0 && x_arr>=0 && y_arr>=0 && x_fleche>=0 && y_fleche>=0)
					arc = new Arc(new Sommet(x_dep, y_dep, ""), 
									new Point(x_bez, y_bez), 
									new Sommet(x_arr, y_arr, ""), 
									new Point(x_fleche, y_fleche), 
									distance, style);
			}
			catch(NumberFormatException e){ }
		}
		
		return arc;
	}

}
