package fr.graphmaker.algorithmes;

import java.util.ArrayList;

import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.Sommet;

/**
 * Classe utilisée pour réaliser l'algorithme du marquage (Descendants et ascendants) sur une liste de sommets.
 *
 */
public class AlgoMarquage implements GrapheConstantes{
	
	/**
	 * Vérifie si la liste de sommets reçue constitue un graphe connexe ou non.
	 * 
	 * @param sommets Liste de sommets dont on veut vérifier la connexité
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @return true si le graphe formé par la liste de sommets est connexe, false sinon
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public static boolean connexite(ArrayList<Sommet> sommets, int type_orientation){
		boolean connexe = true;
		
		if(sommets.size() > 0){
			AlgoMarquage.init(sommets, type_orientation);
			AlgoMarquage.construireMarquage(sommets.get(0), sommets, type_orientation, sommets.size());
			AlgoMarquage.construireMarquage(sommets.get(0), sommets, type_orientation, -sommets.size());
			for(Sommet s: sommets){
				connexe &= s.isSelectionne();
				s.setSelectionne(false, type_orientation);
			}
		}
		
		if(sommets.size() == 1 && type_orientation == ORIENTE)
			if(sommets.get(0).getArcsSortants().size() == 0)
				connexe = false;
		
		return (connexe && sommets.size()>0);
	}
	
	/**
	 * Exécute l'algorithme de marquage sur la liste de sommets reçue en paramètre.
	 * On part du point de départ et on cherche la liste des ascendants/descendants sur le nombre de niveaux demandés.
	 * Si le nombre de niveaux est négatif, on cherche les ascendants.
	 * Sinon, on cherche les descendants.
	 * 
	 * @param depart Sommet de départ de l'algorithme
	 * @param sommets Liste de sommets contenant le sommet de départ
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * @param nb_niveaux Nombre de niveaux sur lequel on cherche les descendants/ascendants
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public static void marquage(Sommet depart, ArrayList<Sommet> sommets, int type_orientation, int nb_niveaux){
		AlgoMarquage.init(sommets, type_orientation);
		AlgoMarquage.construireMarquage(depart, sommets, type_orientation, nb_niveaux);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Construit le marquage à partir du Sommet depart sur un nombre nb_niveaux de niveaux.
	 * Si nb_niveaux < 0, on cherche les prédécesseurs, sinon on cherche les successeurs.
	 * 
	 * @param depart Sommet de départ de l'algorithme
	 * @param sommets Liste de sommets à laquelle appartient le sommet de départ
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * @param nb_niveaux Nombre de niveaux sur lequel on cherche les descendants/ascendants
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	private static void construireMarquage(Sommet depart, ArrayList<Sommet> sommets, int type_orientation, int nb_niveaux){
		/*
		 *  On utilise une liste pour parcourir le marquage en cours, 
		 *  l'autre pour construire le prochain marquage.
		 *  On initialise notre nombre de niveaux local pour 
		 *  qu'il soit toujours positif (valeur absolue)
		 *  On commence l'algorithme avec le sommet sélectionné reçu en paramètre.
		 */
		ArrayList<Sommet> marquage = new ArrayList<Sommet>();
		ArrayList<Sommet> next_marquage = new ArrayList<Sommet>();
		int tmp_niveaux = Math.abs(nb_niveaux);
		marquage.add(depart);
		
		/*
		 * Tant que le nombre de niveaux local n'est pas inférieur à 0
		 * et que la liste de marquage n'est pas vide, on fait tourner l'algorithme. 
		 */
		while(tmp_niveaux >= 0 && !marquage.isEmpty()){
			/*
			 * Pour chacun des sommets de la liste de marquage
			 * on cherche les successeurs/prédécesseurs directs selon le signe de nb_niveaux.
			 */
			for(Sommet sommet_courant: marquage){
				// On cherche les successeurs.
				if(nb_niveaux >= 0){
					/*
					 * On marque chacun des arcs sortants et
					 * on ajoute les sommets d'arrivée de ces arcs 
					 * à la liste du prochain marquage,
					 * si elle ne les contient pas déjà et qu'on a 
					 * un nombre de niveaux local supérieur à 0.
					 */
					for(Arc a: sommet_courant.getArcsSortants())
						if(tmp_niveaux > 0){
							if(!next_marquage.contains(a.getArrivee()))
								next_marquage.add(a.getArrivee());
							a.setSelectionne(true, type_orientation);
						}
				}
				// On cherche les prédécesseurs.
				else{
					/*
					 * Même procédure qu'au-dessus, sauf que comme on cherche les
					 * prédécesseurs il faut regarder les arcs du graphe ayant pour arrivée
					 * notre sommet_courant.
					 */
					for(Sommet s: sommets)
						for(Arc a: s.getArcsSortants())
							if(a.getArrivee() == sommet_courant)
								if(tmp_niveaux > 0){
									if(!next_marquage.contains(s))
										next_marquage.add(s);
									a.setSelectionne(true, type_orientation);
								}
				}
				// On marque le sommet_courant avant de passer au suivant de la liste.
				sommet_courant.setSelectionne(true, type_orientation);
			}
			/*
			 * On copie la liste du prochain marquage dans la liste du marquage actuel.
			 * On vide la liste du prochain marquage.
			 * On décrémente le nombre de niveaux local.
			 */
			marquage = (ArrayList<Sommet>)next_marquage.clone();
			next_marquage.clear();
			tmp_niveaux--;
		}
	}
	
	/**
	 * Initialise la liste de sommets pour la préparer à l'algorithme.
	 * Chaque sommet est dé-sélectionné.
	 * 
	 * @param sommets Liste de sommets à préparer
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	private static void init(ArrayList<Sommet> sommets, int type_orientation){
		for(Sommet s : sommets)
			s.setSelectionne(false, type_orientation);
	}

}
