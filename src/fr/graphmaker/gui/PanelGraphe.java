package fr.graphmaker.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import fr.graphmaker.algorithmes.AlgoBrelaz;
import fr.graphmaker.algorithmes.AlgoDjikstra;
import fr.graphmaker.algorithmes.AlgoMarquage;
import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.BoiteAOutil;
import fr.graphmaker.outils.Point;
import fr.graphmaker.outils.Sommet;

/**
 * Classe gérant le graphe et tous ses composants (arcs/arêtes et sommets).
 * C'est un JPanel à intégrer dans une fenêtre pour en obtenir une représentation graphique et pouvoir interagir avec.
 * Constitué d'un nom, d'un type d'orientation, d'un type de simplicité (boucles ou non), d'un type d'arcs et d'une liste de sommets.
 */
public class PanelGraphe extends JPanel implements GrapheConstantes{
	private static final long serialVersionUID = 4069699918532760578L;
	
	// Variables conservees dans la sauvegarde
	private String nom;
	private int type_orientation;
    private int type_simple;
    private int type_arcs;
    private ArrayList<Sommet> sommets = new ArrayList<Sommet>();
    
    // Variables ignorees dans la sauvegarde
    private boolean[] options;
    private ArrayList<Sommet> selectionnes = new ArrayList<Sommet>();
    private Sommet sommet_deplace;
    private Arc arc_deplace;
    private double x_mvt = 0;
    private double y_mvt = 0;
    private PopUpGraphe menu;
    private Shape forme_sommets = new Ellipse2D.Double();
    private BasicStroke forme_arcs = STYLE_ARC_SIMPLE_PLEIN;
    private FenetreGraphe gi;
    private int niveau_marquage = 1;
    private int max_sommet = 0;
    private int nbChromatique;
    private ArrayList<Sommet> sommetsDjikstra = new ArrayList<Sommet>();
    
    /**
     * Constructeur initialisant le graphe à vide.
     * 
     * @param nom Nom du graphe
     * @param type_orientation Orientation du graphe (orienté ou non)
     * @param type_simple Simplicité du graphe (boucles autorisées ou non)
     * @param type_arcs Type de valeurs des arcs (valué, non valué ou étiqueté)
     * 
     * @see GrapheConstantes#ORIENTE
     * @see GrapheConstantes#NON_ORIENTE
     * 
     * @see GrapheConstantes#SIMPLE
     * @see GrapheConstantes#NON_SIMPLE
     * 
     * @see GrapheConstantes#VALUE
     * @see GrapheConstantes#NON_VALUE
     * @see GrapheConstantes#ETIQUETE
     */
    public PanelGraphe(String nom, int type_orientation, int type_simple, int type_arcs) {
    	this.nom = nom;
    	this.type_orientation = type_orientation;
    	this.type_simple = type_simple;
    	this.type_arcs = type_arcs;
    	options = new boolean[NOMBRE_OPTIONS];
    	desactiverOptions();
        addMouseListener(new GrapheListener());
        addMouseMotionListener(new GrapheListener());
        menu = new PopUpGraphe(this);
        super.setBackground(Color.WHITE);
    }
    
    /**
     * Active l'option dont le code est fourni en paramètre.
     * 
     * @param codeOption Code de l'option à activer
     * @param langue Langue de l'interface utilisateur appelant la méthode
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
     * @see GrapheConstantes#CHAINE
     * @see GrapheConstantes#CYCLE
     * 
     * @see fr.graphmaker.constantes.LanguesConstantes#FR
     * @see fr.graphmaker.constantes.LanguesConstantes#CH
     * @see fr.graphmaker.constantes.LanguesConstantes#EN
     */
    public void activerOption(int codeOption, int langue){
    	if(codeOption >= 0 && codeOption < NOMBRE_OPTIONS){
    		if(codeOption!=MARQUAGE || selectionnes.size()!=1 || selectionnes.get(0)==null)
    			desactiverOptions();
    		options[codeOption] = true;
    	}
    	if(options[RESET_ARCS]){
    		resetArcs();
    		desactiverOptions();
    	}
    	if(options[BRELAZ])
			nbChromatique = AlgoBrelaz.coloration(sommets, langue);
    	if(options[CLIQUE])
    		buildClique();
    	if(options[VIDER]){
    		vider();
    		desactiverOptions();
    	}
    	if(options[CHAINE])
    		buildChaine();
    	if(options[CYCLE])
    		buildCycle();
    	if(options[MARQUAGE] && selectionnes.size()==1 && selectionnes.get(0)!=null)
    		AlgoMarquage.marquage(selectionnes.get(0), sommets, type_orientation, niveau_marquage);
    		
    	repaint();
    }
    
    /**
     * Ajoute l'arc correspondant à la chaîne de caractères reçue en paramètre au graphe.
     * Utilisé lors du chargement.
     * 
     * @param arc Chaîne de caractères correspondant à l'arc qu'on veut ajouter
     * @throws Exception Si la chaîne de caractères n'est pas convertible en arc, on renvoie une exception.
     */
    public void addArc(String[] arc) throws Exception{
    	Arc a = Arc.stringToArc(arc);
    	
    	if(a == null)
    		throw new Exception();
    	
    	Sommet depart = null, arrivee = null;
    	
    	for(Sommet s: sommets){
    		if(a.getDepart().equals(s))
    			depart = s;
    		if(a.getArrivee().equals(s))
    			arrivee = s;
    	}
    	
    	if(depart != null && arrivee != null && !BoiteAOutil.arcExistant(depart, arrivee))
    		depart.addArcSortant(new Arc(depart, a.getBezier(), arrivee, a.getPosFleche(), a.getDistance(), a.getForme()), type_orientation);
    }
    
    /**
     * Ajoute le sommet correspondant à la chaîne de caractères reçue en paramètre au graphe.
     * Utilisé lors du chargement.
     * 
     * @param sommet Chaîne de caractères correspondant au sommet qu'on veut ajouter
     * @throws Exception Si la chaîne de caractères n'est pas convertible en sommet, on renvoie une exception.
     */
    public void addSommet(String[] sommet) throws Exception{
    	Sommet s = Sommet.stringToSommet(sommet);
    	
    	if(s == null)
    		throw new Exception();
    	
    	max_sommet++;
    	sommets.add(s);
    }
    
    /**
     * Vérifie s'il existe un arc aux coordonnées reçues en paramètre.
     * 
     * @param x Abscisse du point à vérifier
     * @param y Ordonnée du point à vérifier
     * 
     * @return true si il existe bien un arc à l'emplacement, false sinon
     */
    public boolean arcExistant(int x, int y){
    	return (getArc(x, y) != null);
    }
    
    /**
	 * Rattache la fenêtre reçue en paramètre comme interface gérant le graphe.
	 * 
	 * @param gi Fenêtre gérant le graphe
	 */
	public void attacherParent(FenetreGraphe gi){
		this.gi = gi;
	}
    
    /**
     * Construis la chaîne maximale du graphe courant.
     * La chaîne a pour point de départ le premier sommet de la liste des sommets,
     * et pour arrivée le dernier sommets de cette liste.
     */
    public void buildChaine(){
    	Arc a = null;
    	for(int i=0; i<sommets.size()-1; i++){
    		if(!BoiteAOutil.arcExistant(sommets.get(i), sommets.get(i+1))){
    			a = new Arc(sommets.get(i), new Point(), sommets.get(i+1));
    			a.calculerBezierInitial(type_orientation);
    			if(type_arcs == VALUE)
    				a.setDistance("0", type_orientation, type_arcs);
    			sommets.get(i).addArcSortant(a, type_orientation);
    		}
    	}
    	
    	repaint();
    }
    
    /**
     * Construis la clique maximale du graphe courant.
     * A n'utiliser de préférence que sur de petits graphes, une trop grande augmentation du nombre d'objets à gérer diminuera grandement les performances.
     */
    public void buildClique(){
    	Arc a = null;
    	for(Sommet depart: sommets){
    		for(Sommet arrivee: sommets){
    			if((depart != arrivee || (depart == arrivee && type_orientation == ORIENTE)) && !BoiteAOutil.arcExistant(depart, arrivee)){
	    			a = new Arc(depart, new Point(), arrivee);
	    			a.calculerBezierInitial(type_orientation);
	    			if(type_arcs == VALUE)
	    				a.setDistance("0", type_orientation, type_arcs);
	    			depart.addArcSortant(a, type_orientation);
    			}
    		}
    	}
    	repaint();
    }
    
    /**
     * Construis le cycle maximal du graphe courant.
     * Le cycle a pour point de départ et d'arrivée le premier sommet de la liste des sommets.
     */
    public void buildCycle(){
    	buildChaine();
    	if(!BoiteAOutil.arcExistant(sommets.get(sommets.size()-1), sommets.get(0))){
			Arc a = new Arc(sommets.get(sommets.size()-1), new Point(), sommets.get(0));
			a.calculerBezierInitial(type_orientation);
			if(type_arcs == VALUE)
				a.setDistance("0", type_orientation, type_arcs);
			sommets.get(sommets.size()-1).addArcSortant(a, type_orientation);
    	}
    	repaint();
    }
    
    /**
     * Modifie la langue du graphe par celle reçue en paramètre.
     * 
     * @param langue Nouvelle langue
     * 
     * @see fr.graphmaker.constantes.LanguesConstantes#FR
     * @see fr.graphmaker.constantes.LanguesConstantes#CH
     * @see fr.graphmaker.constantes.LanguesConstantes#EN
     */
    public void changeLangue(int langue){
    	menu.changeLangue(langue);
    	repaint();
    }
    
    /**
     * Vérifie que le point de coordonnées (x,y) est bien contenu dans les limites du graphe.
     * Utilisé avant de modifier les coordonnées d'un sommet.
     * 
     * @param x Abscisse du point à vérifier
     * @param y Ordonnée du point à vérifier
     * @param s Sommet dont on prévoit de modifier les coordonnées
     * 
     * @return true si le point reste dans les limites du graphe, false sinon
     */
    public boolean contains(int x, int y, Sommet s){
		int min_x, min_y, max_x, max_y, largeur, hauteur, size, x_mod, y_mod;
		size = y_mod = RAYON_SOMMET+BORDURE_SOMMET;
		x_mod = size + s.getPrintedSize()/2;
		min_x = x - x_mod;
		min_y = y - y_mod;
		max_x = x + x_mod;
		max_y = y + y_mod;
		largeur = getSize().width;
		hauteur = getSize().height;
		
		return (min_x > 0 && max_x < largeur && min_y > 0 && max_y < hauteur);
	}
    
    /**
     * Désactive toutes les options du graphe et dé-sélectionne tous les sommets.
     */
	public void desactiverOptions(){
    	for(int i=0; i<NOMBRE_OPTIONS; i++)
    		options[i] = false;
    	deselectionner();
    	repaint();
    }
    
    /**
	 * Dé-sélectionne tous les sommets du graphe et vide la liste de sommets selectionnés.
	 */
	public void deselectionner(){
    	for(Sommet s: sommets)
    		s.setSelectionne(false, type_orientation);
    	selectionnes.clear();
    }
    
    /**
	 * Vérifie que le graphe est connexe ou non.
	 * 
	 * @return true si le graphe est connexe, false sinon
	 */
	public boolean estConnexe(){
		if(type_orientation == ORIENTE)
			return AlgoMarquage.connexite(sommets, type_orientation);
		else
			return ((getArcs().size()/2) >= sommets.size());
	}
    
    /**
     * Renvoie le code de l'algorithme actuellement actif sur le graphe.
     * 
     * @return Code de l'algorithme actif, -1 si aucun algorithme d'actif
     * 
     * @see GrapheConstantes#ALGO_DJIKSTRA
     * @see GrapheConstantes#ALGO_BRELAZ
     * @see GrapheConstantes#ALGO_MARQUAGE
     */
    public int getAlgoCode(){
    	if(options[DJIKSTRA])
			return ALGO_DJIKSTRA;
		else if(options[BRELAZ])
			return ALGO_BRELAZ;
		else if(options[MARQUAGE])
			return ALGO_MARQUAGE;
		else
			return -1;
    }
    
    /**
     * Renvoie l'arc aux coordonnées (x,y) si il y en a un.
     * 
     * @param x Abscisse à vérifier
     * @param y Ordonnée à vérifier
     * 
     * @return Arc existant aux coordonnées (x,y) si il y en a un, null sinon
     */
    public Arc getArc(int x, int y){
    	Arc selectionne = null;
    	
    	for(Sommet s: sommets){
    		for(Arc a: s.getArcsSortants())
    		if(a.contains(x, y, SELECT_ARC)){
    			selectionne = a;
    			break;
    		}
    	}
    	
    	return selectionne;
    }
    
    /**
     * Renvoie la liste de tous les arcs du graphe.
     * 
     * @return Liste des arcs du graphe
     */
    public ArrayList<Arc> getArcs(){
    	ArrayList<Arc> arcs = new ArrayList<Arc>();
    	
    	for(Sommet s: sommets)
    		for(Arc a: s.getArcsSortants())
    			arcs.add(a);
    	
    	return arcs;
    }
    
    /**
     * Renvoie le code de la forme de base des sommets actuellement associée au graphe.
     * 
     * @return Code de la forme de base des sommets
     * 
     * @see GrapheConstantes#ELLIPSE
     * @see GrapheConstantes#RECTANGLE
     * @see GrapheConstantes#ROUND_RECTANGLE
     * @see GrapheConstantes#LOSANGE
     */
    public int getFormeCode(){
    	if(forme_sommets instanceof Ellipse2D)
			return ELLIPSE;
		else if(forme_sommets instanceof Rectangle2D)
			return RECTANGLE;
		else if(forme_sommets instanceof RoundRectangle2D)
			return ROUND_RECTANGLE;
		else
			return LOSANGE;
    }
    
    /**
     * Renvoie la fenêtre qui sert d'interface associée au graphe.
     * 
     * @return Fenêtre associée au graphe
     */
    public FenetreGraphe getInterface(){
    	return gi;
    }
    
    /**
	 * Renvoie l'abscisse maximale parmis les sommets et arcs du graphe.
	 * 
	 * @return Abscisse maximale
	 */
	public int getMaxX(){
		double max_x = Double.NEGATIVE_INFINITY;
		
		for(Sommet s: sommets)
			if(s.getX()+s.getRayon(SELECTION) > max_x)
				max_x = s.getX()+s.getRayon(SELECTION);
		
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getBezier().getX()+RAYON_SOMMET > max_x)
					max_x = a.getBezier().getX()+RAYON_SOMMET;
		
		return (int)max_x;
	}
    
    /**
	 * Renvoie l'ordonnée maximale parmis les sommets et arcs du graphe.
	 * 
	 * @return Ordonnée maximale
	 */
	public int getMaxY(){
		double max_y = Double.NEGATIVE_INFINITY, coeff;
		
		for(Sommet s: sommets){
			if(s.getFormeCode() == LOSANGE)
				coeff = 1.5;
			else
				coeff = 1;
			
			if(s.getY()+(BORDURE_SOMMET*2)+(coeff*RAYON_SOMMET) > max_y)
				max_y = s.getY()+(BORDURE_SOMMET*2)+(coeff*RAYON_SOMMET);
		}
		
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getBezier().getY()+RAYON_SOMMET > max_y)
					max_y = a.getBezier().getY()+RAYON_SOMMET;
		
		return (int)max_y;
	}
    
    /**
	 * Renvoie l'abscisse minimale parmis les sommets et arcs du graphe.
	 * 
	 * @return Abscisse minimale
	 */
	public int getMinX(){
		double min_x = Double.POSITIVE_INFINITY;
		
		for(Sommet s: sommets)
			if(s.getX()-s.getRayon(SELECTION) < min_x)
				min_x = s.getX()-s.getRayon(SELECTION);
		
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getBezier().getX()-RAYON_SOMMET < min_x)
					min_x = a.getBezier().getX()-RAYON_SOMMET;
		
		if(min_x < 0)
			min_x = 0;
		
		return (int)min_x;
	}
    
    /**
	 * Renvoie l'ordonnée minimale parmis les sommets et arcs du graphe.
	 * 
	 * @return Ordonnée minimale
	 */
	public int getMinY(){
		double min_y = Double.POSITIVE_INFINITY, coeff;
		
		for(Sommet s: sommets){
			if(s.getFormeCode() == LOSANGE)
				coeff = 1.5;
			else
				coeff = 1;
			
			if(s.getY()-(BORDURE_SOMMET*2)-(coeff*RAYON_SOMMET) < min_y)
				min_y = s.getY()-(BORDURE_SOMMET*2)-(coeff*RAYON_SOMMET);
		}
		
		for(Sommet s: sommets)
			for(Arc a: s.getArcsSortants())
				if(a.getBezier().getY()-RAYON_SOMMET < min_y)
					min_y = a.getBezier().getY()-RAYON_SOMMET;
		
		if(min_y < 0)
			min_y = 0;
		
		return (int)min_y;
	}
    
    /**
	 * Renvoie le code de l'option actuellement active sur le graphe.
	 * 
	 * @return Code de l'option active, -1 si aucune option n'est active
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
     * @see GrapheConstantes#CHAINE
     * @see GrapheConstantes#CYCLE
	 */
	public int getMode(){
		int mode = -1;
		for(int i=0; i<NOMBRE_OPTIONS; i++){
			if(options[i]){
				mode = i;
				break;
			}
		}
		return mode;
	}
    
    /**
     * Renvoie le nombre chromatique de l'algorithme de Brélaz.
     *
     * @return Nombre chromatique
     */
    public int getNbChromatique(){
    	return nbChromatique;
    }
    
    /**
     * Renvoie le niveau de l'algorithme du marquage.
     * 
     * @return Niveau de marquage
     */
    public int getNiveauMarquage(){
    	return niveau_marquage;
    }
    
    /**
     * Renvoie le nom du graphe.
     * 
     * @return Nom du graphe
     */
    public String getNom(){
    	return nom;
    }
    
    /**
	 * Renvoie la liste des sommets selectionnés.
	 * 
	 * @return Liste des sommets selectionnés
	 */
	public ArrayList<Sommet> getSelectionnes(){
		return selectionnes;
	}
    
    /**
     * Renvoie le sommet aux coordonnées (x,y) si il y en a un.
     * 
     * @param x Abscisse à vérifier
     * @param y Ordonnée à vérifier
     * 
     * @return Sommet existant aux coordonnées (x,y) si il y en a un, null sinon
     */
    public Sommet getSommet(double x, double y){
    	Sommet selectionne = null;
    	
    	for(Sommet s: sommets){
    		if(s.inBounds(x, y)){
    			selectionne = s;
    			break;
    		}
    	}
    	
    	return selectionne;
    }
    
    /**
     * Renvoie la liste de tous les sommets du graphe.
     * 
     * @return Liste des sommets du graphe
     */
    public ArrayList<Sommet> getSommets(){
    	return sommets;
    }
    
    /**
     * Renvoie le chemin de l'algorithme de Djikstra.
     *
     * @return Liste de sommets correspondant au chemin de Djikstra
     */
    public ArrayList<Sommet> getSommetsDjikstra(){
    	return sommetsDjikstra;
    }
    
    /**
     * Renvoie le code du style de base des arcs actuellement associé au graphe.
     * 
     * @return Code du style de base des arcs
     * 
     * @see GrapheConstantes#ARC_SIMPLE_PLEIN
     * @see GrapheConstantes#ARC_SIMPLE_POINTILLE
     * @see GrapheConstantes#ARC_DOUBLE_PLEIN
     * @see GrapheConstantes#ARC_DOUBLE_POINTILLE
     */
    public int getStyleCode(){
    	if(forme_arcs == STYLE_ARC_SIMPLE_PLEIN)
    		return ARC_SIMPLE_PLEIN;
    	else if(forme_arcs == STYLE_ARC_SIMPLE_POINTILLE)
    		return ARC_SIMPLE_POINTILLE;
    	else if(forme_arcs == STYLE_ARC_DOUBLE_PLEIN)
    		return ARC_DOUBLE_PLEIN;
    	else
    		return ARC_DOUBLE_POINTILLE;
    }
    
    /**
     * Renvoie le type d'arcs du graphe.
     * 
     * @return Type de valeur des arcs
     * 
     * @see GrapheConstantes#VALUE
     * @see GrapheConstantes#NON_VALUE
     * @see GrapheConstantes#ETIQUETE
     */
    public int getTypeArcs(){
    	return type_arcs;
    }
    
    /**
     * Renvoie le type d'orientation du graphe.
     * 
     * @return Orientation du graphe
     * 
     * @see GrapheConstantes#ORIENTE
     * @see GrapheConstantes#NON_ORIENTE
     */
    public int getTypeOrientation(){
    	return type_orientation;
    }
    
    /**
     * Renvoie le type de simplicité du graphe (autorisation des boucles ou non).
     * 
     * @return Simplicité du graphe
     * 
     * @see GrapheConstantes#SIMPLE
     * @see GrapheConstantes#NON_SIMPLE
     */
    public int getTypeSimple(){
    	return type_simple;
    }
	
	@SuppressWarnings("unchecked")
	/**
	 * Vérifie que le marquage est complet selon le niveau courant et la direction fournie.
	 * 
	 * @param direction 1 ou -1 selon qu'on veuille vérifier les successeurs ou les prédécésseurs
	 * @return true si le marquage est complet (impossible d'aller plus loin dans la même direction), false sinon
	 * 
	 * @see GrapheConstantes#PREDECESSEURS
	 * @see GrapheConstantes#SUCCESSEURS
	 */
	public boolean isMarquageComplet(int direction){
		int compteur = 0;
		if(options[MARQUAGE] && sommets.size() > 0 && selectionnes.size() > 0){
			ArrayList<Sommet> copie_marquage_complet = (ArrayList<Sommet>)sommets.clone();
			Sommet start = null;
			
			AlgoMarquage.marquage(selectionnes.get(0), sommets, type_orientation, niveau_marquage);
			for(Sommet s: sommets)
				if(s.isSelectionne())
					for(Arc a: s.getArcsSortants())
						if(a.isSelectionne())
							compteur++;
			
			for(Sommet s: copie_marquage_complet)
				if(s.equals(selectionnes.get(0)))
					start = s;
			AlgoMarquage.marquage(start, copie_marquage_complet, type_orientation, direction*copie_marquage_complet.size());
			if((niveau_marquage>=0 && direction==1) || (niveau_marquage<=0 && direction==-1))
				for(Sommet s: copie_marquage_complet)
					if(s.isSelectionne())
						for(Arc a: s.getArcsSortants())
							if(a.isSelectionne())
								compteur--;

			AlgoMarquage.marquage(selectionnes.get(0), sommets, type_orientation, niveau_marquage);
		}
		
		return compteur == 0 || (niveau_marquage == 0 && sommets.size() == 1);
	}
	
	/**
	 * Renvoie la matrice correspondant au graphe.
	 * 
	 * @return Matrice du graphe
	 */
	public int[][] matrice(){
		return BoiteAOutil.matrice(sommets);
	}
	
	/**
     * Réinitialise la courbe de tous les arcs du graphe.
     * Chaque point manipulable est remis à sa position initiale.
     */
    public void resetArcs(){
    	for(Sommet s: sommets)
    		s.resetArcs(type_orientation);
    }
	
	/**
     * Fait tourner l'algorithme de djikstra selon les paramètres reçus.
     * 
     * @param depart Sommet de départ de l'algorithme
     * @param arrivee Sommet d'arrivée de l'algorithme
     * 
     * @return Liste de sommets correspondant au chemin tracé par l'algorithme
     */
    public ArrayList<Sommet> runDjikstra(Sommet depart, Sommet arrivee){
    	return AlgoDjikstra.djikstra(depart, arrivee, sommets, type_orientation);
    }
	
	/**
     * Modifie le style de base des arcs qui seront créés par la suite.
     * 
     * @param s Nouveau style d'arc
     * 
     * @see GrapheConstantes#STYLE_ARC_SIMPLE_PLEIN
     * @see GrapheConstantes#STYLE_ARC_SIMPLE_POINTILLE
     * @see GrapheConstantes#STYLE_ARC_DOUBLE_PLEIN
     * @see GrapheConstantes#STYLE_ARC_DOUBLE_POINTILLE
     */
    public void setFormeArcs(BasicStroke s){
    	forme_arcs = s;
    }

    /**
     * Modifie la forme de base des sommets qui seront créés par la suite.
     * 
     * @param s Nouvelle forme de sommet
     */
    public void setFormeSommets(Shape s){
    	forme_sommets = s;
    }
 
    /**
     * Modifie le niveau de l'algorithme du marquage.
     * 
     * @param niveau Nouveau niveau de marquage
     */
    public void setNiveauMarquage(int niveau){
    	niveau_marquage = niveau;
    }

    /**
     * Modifie le nom du graphe.
     * 
     * @param nom Nouveau nom
     */
    public void setNom(String nom){
    	this.nom = nom;
    }
    
    /**
     * Modifie la liste des sommets du chemin formé par Djikstra.
     */
    public void setSommetsDjikstra(ArrayList<Sommet> chemin){
    	sommetsDjikstra = chemin;
    }
	
	/**
     * Modifie le type d'arcs du graphe.
     * 
     * @param type_arcs Nouveau type d'arcs
     * 
     * @see GrapheConstantes#VALUE
     * @see GrapheConstantes#NON_VALUE
     * @see GrapheConstantes#ETIQUETE
     */
    public void setTypeArcs(int type_arcs){
    	this.type_arcs = type_arcs;
    	repaint();
    }
	
	/**
     * Modifie l'orientation du graphe.
     * 
     * @param type_orientation Nouvelle orientation
     * 
     * @see GrapheConstantes#ORIENTE
     * @see GrapheConstantes#NON_ORIENTE
     */
    public void setTypeOrientation(int type_orientation){
    	this.type_orientation = type_orientation;
    	
    	if(type_orientation == NON_ORIENTE){
    		setTypeSimple(SIMPLE);
	    	for(Sommet s: sommets)
	    		for(Arc a: s.getArcsSortants())
	    			a.desorienter();
    	}
    	
    	repaint();
    }
	
	/**
     * Modifie la simplicité (boucles autorisées ou non) du graphe.
     * 
     * @param type_simple Nouvelle simplicité
     * 
     * @see GrapheConstantes#SIMPLE
     * @see GrapheConstantes#NON_SIMPLE
     */
    public void setTypeSimple(int type_simple){
    	this.type_simple = type_simple;
    	
    	if(type_simple == SIMPLE)
	    	for(Sommet s: sommets)
	    		s.simplifier();
    	
    	repaint();
    }
	
	/**
     * Vérifie s'il existe un sommet aux coordonnées reçues en paramètre.
     * 
     * @param x Abscisse du point à vérifier
     * @param y Ordonnée du point à vérifier
     * 
     * @return true si il existe bien un sommet à l'emplacement, false sinon
     */
	public boolean sommetExistant(double x, double y){
    	return (getSommet(x, y) != null);
    }
	
	
	
	/**
     * Vérifie s'il existe un sommet aux coordonnées du point reçu en paramètre.
     * 
     * @param p Point à vérifier
     * 
     * @return true si il existe bien un sommet à l'emplacement, false sinon
     */
	public boolean sommetExistant(Point p){
    	return (getSommet(p.getX(), p.getY()) != null);
    }
	
	/**
	 * Renvoie une chaîne de caractères représentant le graphe.
	 * Cette chaîne est utilisée pour sauvegarder le graphe.
	 * 
	 * @return Chaîne représentant le Graphe
	 */
	@Override
	public String toString(){
		String s = nom +","+ type_orientation+","+type_simple+","+type_arcs+","+sommets.size()+","+getArcs().size()+"\n";
		
		for(Sommet sommmet: sommets)
			s += sommmet+"\n";
		for(Arc arc: getArcs())
			s += arc+"\n";
		
		return s;
	}
	
	/**
	 * Vide le graphe de tous ses composants (sommets et arcs).
	 */
	public void vider(){
		max_sommet = 0;
    	sommets.clear();
    	repaint();
    }
	
	@Override
    /**
	 * Redéfinition de paintComponent pour customiser le dessin du graphe.
	 * 
	 * @param g Objet graphics utilisé pour le dessin du composant
	 */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension size = getSize();
        Insets insets = getInsets();
        Graphics2D g2d = (Graphics2D) g.create(insets.left, insets.top, size.width - (insets.left + insets.right), size.height - (insets.top + insets.bottom));
        try {
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
        	resetDessinArcs();
            for(Sommet s: sommets)
            	for(Arc a: s.getArcsSortants())
            		if(!a.isDessine())
            			a.draw(g2d, type_orientation, getMode(), type_simple, type_arcs);
   
            for(Sommet s: sommets){
            	if(s != sommet_deplace)
            		s.draw(g2d, getMode());
            	else
            		sommet_deplace = s;
            }
	            
            if(sommet_deplace != null)
            	sommet_deplace.draw(g2d, getMode());     
        } finally {
            g2d.dispose();
        }
    }
	
	/**
     * Déplace le graphe entier (sommets et arcs) selon les paramètres reçus.
     * 
     * @param change_x Modification d'abscisse
     * @param change_y Modification d'ordonnée
     */
    private void deplacerGraphe(double change_x, double change_y){
		for(Sommet s: sommets)
			s.setLocation(s.getX()+change_x, s.getY()+change_y, sommets, type_orientation);
	}
	
	/**
	 * Vérifie que le graphe sera toujours dans les limites de la fenêtre si on le déplace des variations reçues en paramètre.
	 * 
	 * @param change_x Variation des abscisses
	 * @param change_y Variation des ordonnées
	 * 
	 * @return true si le graphe reste dans les limites, false sinon
	 */
	private boolean grapheInBounds(double change_x, double change_y){
		for(Sommet s: sommets){
			if(!contains((int)(s.getX()+change_x), (int)(s.getY()+change_y), s))
				return false;
			for(Arc a: s.getArcsSortants())
				if(!contains((int)(a.getBezier().getX()+change_x), (int)(a.getBezier().getY()+change_y), s))
					return false;
		}
		
		return true;
	}
	
	/**
	 * Réinitialise l'attribut dessine de tous les arcs du graphe.
	 */
	private void resetDessinArcs(){
		for(Sommet s: sommets)
			s.resetDessinArcs(type_orientation);
	}
	
	/**
	 * Listener du graphe.
	 * Gère toutes les interactions entre la souris et le graphe.
	 */
	class GrapheListener extends MouseAdapter{
		@Override
		public void mouseDragged(MouseEvent e) {
			int nouvel_x = e.getX(), nouvel_y = e.getY();
			
			if(options[DEPLACER]){
	    		if(sommet_deplace != null && contains(nouvel_x, nouvel_y, sommet_deplace)){
	    			x_mvt = Math.abs(nouvel_x - sommet_deplace.getX());
	    			y_mvt = Math.abs(nouvel_y - sommet_deplace.getY());
	    			if(x_mvt >= 4 || y_mvt >= 4){
	    				sommet_deplace.setLocation(nouvel_x, nouvel_y, sommets, type_orientation);
	    				x_mvt = 0;
	    				y_mvt = 0;
	    				repaint();
	    			}
	    		}
	    		else if(arc_deplace != null && contains(nouvel_x, nouvel_y)){
	    			x_mvt = Math.abs(nouvel_x - arc_deplace.getBezier().getX());
	    			y_mvt = Math.abs(nouvel_y - arc_deplace.getBezier().getY());
	    			if((x_mvt >= 4 || y_mvt >= 4) && !sommetExistant(nouvel_x, nouvel_y)){
	    				arc_deplace.setBezierLocation(nouvel_x, nouvel_y, type_orientation);
	    				x_mvt = 0;
	    				y_mvt = 0;
	    				repaint();
	    			}
	    		}
			}
			else if(options[DEPLACER_FLECHE]){
				if(arc_deplace != null && arc_deplace.contains(nouvel_x, nouvel_y, MOVE_ARC)){
					if(!sommetExistant(arc_deplace.findPoint(nouvel_x, nouvel_y, MOVE_ARC))){
						arc_deplace.setPosFleche(nouvel_x, nouvel_y, type_orientation, MOVE_ARC);
						repaint();
					}
				}
			}
			else if(options[DEPLACER_GRAPHE]){
				double change_x, change_y;
				change_x = nouvel_x - x_mvt;
				change_y = nouvel_y - y_mvt;
				if((Math.abs(change_x) >= 4 || Math.abs(change_y) >= 4) && grapheInBounds(change_x, change_y)){
    				deplacerGraphe(change_x, change_y);
    				x_mvt = nouvel_x;
    				y_mvt = nouvel_y;
    				repaint();
    			}
			}
		}

		@Override
		public void mousePressed(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON1){
	    		if(options[CREER_SOMMET] && !sommetExistant(e.getX(), e.getY()) && !arcExistant(e.getX(), e.getY())){
	    			sommets.add(new Sommet(e.getX(), e.getY(), ""+(max_sommet+1), forme_sommets));
	    			max_sommet++;
					gi.refresh_infos();
					gi.refresh_buttons();
					repaint();
	    		}
	    		else if(options[CREER_ARC] && sommetExistant(e.getX(), e.getY())){
	    			Sommet selectionne = getSommet(e.getX(), e.getY());
	    			selectionne.setSelectionne(true, type_orientation);
	    			selectionnes.add(selectionne);
	    			if(selectionnes.size() == 2 && !BoiteAOutil.arcExistant(selectionnes.get(0), selectionnes.get(1))){
	    				Sommet depart = selectionnes.get(0);
	    				Sommet arrivee = selectionnes.get(1);
	    				if(depart!=arrivee || (depart==arrivee && type_orientation == ORIENTE)){
		    				Arc a = new Arc(depart, new Point(), arrivee, forme_arcs);
		    				a.calculerBezierInitial(type_orientation);
		    				if(type_arcs == VALUE)
		    					a.setDistance("0", type_orientation, type_arcs);
		    				depart.addArcSortant(a, type_orientation);
		    				gi.refresh_infos();
							gi.refresh_buttons();
	    				}
	    			}
	    			
	    			if(selectionnes.size() == 2)
	    				deselectionner();
	    			repaint();
	    		}
	    		else if(options[CREER_ARC] && !sommetExistant(e.getX(), e.getY())){
	    			deselectionner();
	    			gi.refresh();
	    		}
	    		else if(options[DEPLACER] && sommet_deplace == null && arc_deplace == null){
	    			if(sommetExistant(e.getX(), e.getY()))
	    				sommet_deplace = getSommet(e.getX(), e.getY());
	    			else if(arcExistant(e.getX(), e.getY()))
	    				arc_deplace = getArc(e.getX(), e.getY());
	    		}
	    		else if(options[SUPPRIMER]){
	    			if(sommetExistant(e.getX(), e.getY()) )
	    				getSommet(e.getX(), e.getY()).detruireSommet(sommets, type_orientation);
	    			else if(arcExistant(e.getX(), e.getY()))
	    				getArc(e.getX(), e.getY()).detruireArc(type_orientation);
	    			gi.refresh_infos();
					gi.refresh_buttons();
					repaint();
	    		}
	    		else if(options[DJIKSTRA] && sommetExistant(e.getX(), e.getY())){
	    			Sommet selectionne = getSommet(e.getX(), e.getY());
	    			selectionne.setSelectionne(true, type_orientation);
	    			if(!selectionnes.contains(selectionne) || (selectionnes.size() == 2 && selectionnes.get(0) != selectionnes.get(1)))
	    				selectionnes.add(selectionne);
	    			if(selectionnes.size() == 2){
	    				Sommet depart = selectionnes.get(0);
	    				Sommet arrivee = selectionnes.get(1);
	    				if(runDjikstra(depart, arrivee) == null){
	    					Sommet tmp = selectionnes.get(selectionnes.size()-1);
		    				deselectionner();
		    				tmp.setSelectionne(true, type_orientation);
		    				selectionnes.add(tmp);
	    				}
                        sommetsDjikstra = runDjikstra(depart,arrivee);
	    			}
	    			else if(selectionnes.size() > 2){
	    				Sommet tmp = selectionnes.get(selectionnes.size()-1);
	    				deselectionner();
	    				tmp.setSelectionne(true, type_orientation);
	    				selectionnes.add(tmp);
	    			}
                    gi.refresh_tabs();
	    			repaint();
	    		}
	    		else if(options[DJIKSTRA] && !sommetExistant(e.getX(), e.getY())){
	    			deselectionner();
	    			gi.refresh();
	    		}
	    		else if(options[DEPLACER_FLECHE] && arc_deplace == null){
	    			arc_deplace = getArc(e.getX(), e.getY());
	    		}
	    		else if(options[DEPLACER_GRAPHE]){
	    			x_mvt = e.getX();
	    			y_mvt = e.getY();
	    		}
	    		else if(options[MARQUAGE] && sommetExistant(e.getX(), e.getY())){
	    			Sommet selectionne = getSommet(e.getX(), e.getY());
	    			if(!selectionnes.contains(selectionne) && selectionnes.size() == 0)
	    				selectionnes.add(selectionne);
	    			else
	    				selectionnes.set(0, selectionne);
	    			
	    			gi.refresh_tabs();
	    			AlgoMarquage.marquage(selectionne, sommets, type_orientation, niveau_marquage);
	    			repaint();
	    		}
			}
			else if(e.getButton() == MouseEvent.BUTTON3){
				if(sommetExistant(e.getX(), e.getY()))
					menu.setSommet(getSommet(e.getX(), e.getY()));
				else if(arcExistant(e.getX(), e.getY()))
					menu.setArc(getArc(e.getX(), e.getY()));
				else
					menu.clearPopUp();

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e){			
			sommet_deplace = null;
			arc_deplace = null;
		}
	}
}
