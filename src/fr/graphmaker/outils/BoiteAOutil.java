package fr.graphmaker.outils;

import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Classe contenant des fonctions générales utiles pour le reste du programme (calculs du moteur graphique par exemple).
 */
public class BoiteAOutil {

	/**
	 * Renvoie l'angle sur le cercle dont le point de départ est le centre.
	 * Par exemple, pour simuler le cercle trigonométrique, si on a :
	 * 		depart (0, 0)
	 * 		arrivee (0, 1)
	 * Alors la fonction renverra un angle de 90° (pi/2).
	 * 
	 * @param depart Point servant de centre au cercle
	 * @param arrivee Point situé sur le cercle
	 * @return Angle entre le point (arrivee.x, 0) et arrivee.
	 */
	public static double angle_sur_cercle(Point2D depart, Point2D arrivee){
		double distance_x = arrivee.getX() - depart.getX();
		double distance_y = arrivee.getY() - depart.getY();
        
		return Math.atan2(distance_y, distance_x);
	}
	
	/**
	 * Renvoie l'angle de la rotation permettant le passage du segment old au segment new.
	 * Les paramètres old_depart et new_depart permettent d'ignorer une éventuelle translation dans les coordonnées.
	 * Par exemple, pour simuler le cercle trigonométrique, si on a :
	 * 		old_depart (0, 0)
	 * 		old_arrivee (0, 1)
	 * 		new_depart (0, 0)
	 * 		new_arrivee (-1, 0)
	 * Alors la fonction renverra un angle de 90° (pi/2) puisqu'il faut une rotation de 90° pour passer de old a new.
	 * 
	 * @param old_depart Ancien centre du cercle
	 * @param old_arrivee Point avant la rotation
	 * @param new_depart Nouveau centre du cercle
	 * @param new_arrivee Point après la rotation
	 * @return Angle de la rotation pour passer de old à new
	 */
	public static double angle_transformation(Point2D old_depart, Point2D old_arrivee, Point2D new_depart, Point2D new_arrivee){
		return angle_sur_cercle(new_depart, new_arrivee) - angle_sur_cercle(old_depart, old_arrivee);
	}

	/**
	 * Ajoute une mise a l'échelle à la transformation reçue en paramètre.
	 * 
	 * @param a Transformation affine à laquelle ajouter une échelle
	 * @param echelle Echelle à appliquer à la transformation
	 */
	public static void appliquer_echelle(AffineTransform a, double echelle){
		a.concatenate(AffineTransform.getScaleInstance(echelle, echelle));
	}
	
	/**
	 * Ajoute un angle de rotation à la transformation reçue en paramètre.
	 * 
	 * @param a Transformation affine à laquelle ajouter un angle de rotation
	 * @param angle Angle à appliquer à la transformation
	 */
	public static void appliquer_rotation(AffineTransform a, double angle){
		a.concatenate(AffineTransform.getRotateInstance(angle));
	}
	
	/**
	 * Ajoute un angle de rotation calculé à partir des points reçus en paramètre
	 * 
	 * @param a Transformation affine à laquelle ajouter un angle de rotation
	 * @param depart Point servant de centre au cercle
	 * @param arrivee Point situé sur le cercle
	 */
	public static void appliquer_rotation(AffineTransform a, Point2D depart, Point2D arrivee){
		appliquer_rotation(a, angle_sur_cercle(depart, arrivee));
	}
	
	/**
	 * Ajoute une translation à partir des coordonnées reçues en paramètre.
	 * 
	 * @param a Transformation affine à laquelle ajouter une translation
	 * @param x Abscisse de la translation
	 * @param y Ordonnée de la translation
	 */
	public static void appliquer_translation(AffineTransform a, double x, double y){
		appliquer_translation(a, new Point(x, y));
	}
	
	/**
	 * Ajoute une translation à partir du point reçu en paramètre.
	 * 
	 * @param a Transformation affine à laquelle ajouter une translation
	 * @param p Point de la translation
	 */
	public static void appliquer_translation(AffineTransform a, Point2D p){
		a.concatenate(AffineTransform.getTranslateInstance(p.getX(), p.getY()));
	}
	
	/**
	 * Vérifie s'il existe un arc entre les deux sommets reçus en paramètre.
	 * 
	 * @param depart Sommet de départ
	 * @param arrivee Sommet d'arrivée
	 * 
	 * @return true si il y a un arc entre les deux sommets, false sinon
	 */
	public static boolean arcExistant(Sommet depart, Sommet arrivee){
    	boolean existe = false;
	
    	for(Arc a: depart.getArcsSortants()){
    		if(a.getArrivee() == arrivee){
    			existe = true;
    			break;
    		}
    	}
    	
    	return existe;
    }
	
	/**
	 * Renvoie l'arrondi de la valeur reçue en paramètre.
	 * 
	 * @param valeur Valeur à arrondir
	 * @return Valeur arrondie
	 */
	public static double arrondi(double valeur){
		return arrondi(valeur, 0);
	}
	
	/**
	 * Renvoie l'arrondi de la valeur reçue en paramètre avec un nombre de chiffres après la virgule précisé en paramètre.
	 * 
	 * @param valeur Valeur à arrondir
	 * @param digits_apres_virgule Nombre de chiffres après la virgule à conserver
	 * @return Valeur arrondie
	 */
	public static double arrondi(double valeur, double digits_apres_virgule){
		if(digits_apres_virgule > 0){
			double truncTool = Math.pow(10.0, digits_apres_virgule);
			valeur *= truncTool;
			valeur = Math.round(valeur);
			valeur /= truncTool;
		}
		else
			valeur = Math.round(valeur);
		
		return valeur;
	}
	
	/**
	 * Renvoie la matrice de la liste reçues en paramètre.
	 * 
	 * @param sommets Liste de sommets dont on veut la matrice
	 * 
	 * @return Tableau d'entiers à deux dimensions correspondant à la matrice de la liste
	 */
	public static int[][] matrice(ArrayList<Sommet> sommets){
		int[][] matrice = new int[sommets.size()][sommets.size()];
		
		for(Sommet depart: sommets)
			for(Sommet arrivee: sommets)
				if(arcExistant(depart, arrivee))
					matrice[sommets.indexOf(depart)][sommets.indexOf(arrivee)] = 1;
		
		return matrice;
	}
	
	/**
	 * Convertit le nombre de pixels reçus en nombre de millimètres.
	 * 
	 * @param nb_pixels Nombre de pixels à convertir
	 * @return Nombre de millimètres équivalent
	 */
	public static double pxToMm(int nb_pixels){
		return (25.4*(double)nb_pixels)/(double)Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	/**
	 * Renvoie une copie triée de la liste de sommets reçue en paramètre.
	 * Le tri se fait d'abord par ordre numérique puis par ordre alphabétique.
	 * 
	 * @param aTrier Liste de sommets à trier
	 * @return Liste triée
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Sommet> triCopieListe(ArrayList<Sommet> aTrier){
		ArrayList<Sommet> sommets = (ArrayList<Sommet>)aTrier.clone();
		triListe(sommets);
		return sommets;
	}
	
	/**
	 * Trie la liste de sommets reçue en paramètre.
	 * Le tri se fait d'abord par ordre numérique puis par ordre alphabétique.
	 * 
	 * @param aTrier Liste de sommets à trier
	 */
	public static void triListe(ArrayList<Sommet> aTrier){
		Collections.sort(aTrier, new Comparator<Sommet>() {
            @Override
            public int compare(Sommet s_1, Sommet s_2) {
            	int test = 0;
            	try{
            		test = (((Integer)(Integer.parseInt(s_1.getValeur()))).compareTo(((Integer)(Integer.parseInt(s_2.getValeur())))));
            	} catch (Exception e) {
            		test = s_1.getValeur().compareTo(s_2.getValeur());
            	}
                return test;
            }
        });
	}
	
	/**
	 * Renvoie la troncature de la valeur reçue en paramètre.
	 * 
	 * @param valeur Valeur à tronquer
	 * @return Valeur tronquée
	 */
	public static double troncature(double valeur){
		return troncature(valeur, 0);
	}
	
	/**
	 * Renvoie la troncature de la valeur reçue en paramètre avec un nombre de chiffres après la virgule précisé en paramètre.
	 * 
	 * @param valeur Valeur à tronquer
	 * @param digits_apres_virgule Nombre de chiffres après la virgule à conserver
	 * @return Valeur tronquée
	 */
	public static double troncature(double valeur, double digits_apres_virgule){
		if(digits_apres_virgule > 0){
			double truncTool = Math.pow(10.0, digits_apres_virgule);
			valeur *= truncTool;
			valeur = (double)((int) valeur);
			valeur /= truncTool;
		}
		else
			valeur = (double)((int) valeur);
		
		return valeur;
	}
}
