package fr.graphmaker.algorithmes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.Sommet;

/**
 * Classe utilisée pour réaliser l'algorithme de Djikstra (Chemin le plus court) sur une liste de sommets.
 *
 */
public class AlgoDjikstra{
	
	/**
	 * Exécute l'algorithme de Djikstra sur la liste de sommets reçue en paramètre.
	 * On part du point de départ et on cherche le chemin le plus court jusqu'à l'arrivée.
	 * 
	 * @param depart Sommet de départ de l'algorithme
	 * @param arrivee Sommet d'arrivée de l'algorithme
	 * @param sommets Liste de sommets contenant les sommets de départ et d'arrivée, nécessaire pour l'initialisation.
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @return Chemin le plus court (sous forme d'une liste de sommets) allant de depart à arrivee si il existe, null sinon.
	 *
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	public static ArrayList<Sommet> djikstra(Sommet depart, Sommet arrivee, ArrayList<Sommet> sommets, int type_orientation){
		ArrayList<Sommet> chemin = new ArrayList<Sommet>();
		if(depart != arrivee){
			AlgoDjikstra.init(sommets, type_orientation);
			AlgoDjikstra.construireDjikstra(depart, arrivee, chemin, type_orientation);
		}
		
		if(chemin.size() == 1)
			chemin = null;
		
		return chemin;
	}
	
	/**
	 * Uniquement dans un graphe valué (métrique).
	 * Construit le chemin le plus court à partir du Sommet depart vers le Sommet arrivee.
	 * Ce chemin est enregistré dans la liste chemin.
	 * Cette fonction ne doit être appelée que lorsque tous les sommets du graphe ont été initialisés du point de vue de Djisktra. 
	 * 
	 * @param depart Sommet de départ de l'algorithme
	 * @param arrivee Sommet de fin de l'algorithme
	 * @param chemin Liste de sommets correspondant au chemin construit par l'algorithme
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	private static void construireDjikstra(Sommet depart, Sommet arrivee, 
			ArrayList<Sommet> chemin, int type_orientation){
		
		/*
		 * On commence par mettre la distance du sommet depart à 0,
		 * puis on lance notre algorithme.
		 * La file de priorité va classer les sommets qu'elle reçoit
		 * selon leur distance de djikstra respectives.
		 */
		depart.setDistanceDjikstra(0.0);
		PriorityQueue<Sommet> djikstra = new PriorityQueue<Sommet>();
		djikstra.add(depart);
		
		/*
		 * L'algorithme va tourner tant que la file de priorité n'est pas vide.
		 * Ici chaque sommet membre du chemin va retenir son prédécesseur
		 * ce qui permettra d'idientifier la chaine du graphe correspondant
		 * au chemin le plus court entre depart et arrivee.
		 */
		while (!djikstra.isEmpty()) {
			/*
			 * On récupère le sommet ayant la plus grande distance de djikstra
			 * dans la file.
			 */
			Sommet sommet_courant = djikstra.poll();
			
			/*
			 * On va parcourir tous les arcs partants
			 * du sommet_courant pour trouver celui ayant la plus
			 * petite valeur associée.
			 */
			for (Arc arc_courant : sommet_courant.getArcsSortants()){
				/*
				 * On considère le sommet à l'autre bout de l'arc comme le
				 * potentiel sommet_proche du sommet_courant.
				 */
				Sommet sommet_proche = arc_courant.getArrivee();
				double distance_arc = Double.parseDouble(arc_courant.getDistance());
				double distance_totale = sommet_courant.getDistanceDjikstra() + distance_arc;
				
				/*
				 * Si la distance de djikstra totale 
				 * (celle du sommet_courant + celle de l'arc) est plus petite
				 * que la distance de djikstra du sommet_proche,
				 * on met à jour ce dernier.
				 * Sa distance de djikstra devient la distance_totale,
				 * son prédécesseur dans Djikstra devient le sommet_courant,
				 * et on le reclasse dans la file de priorité.
				 */
				if (distance_totale < sommet_proche.getDistanceDjikstra()) {
					djikstra.remove(sommet_proche);
					sommet_proche.setDistanceDjikstra(distance_totale);
					sommet_proche.setPrecedentDjikstra(sommet_courant);
					djikstra.add(sommet_proche);
				}
			}
		}
		
		/*
		 * Puisque chaque sommet membre du chemin le plus court
		 * connait son prédécesseur dans la chaine correspondante,
		 * on remonte la chaine à partir du sommet arrivee jusqu'au sommet depart
		 * pour construire la liste correspondant au chemin.
		 */
		for (Sommet sommet_courant = arrivee; sommet_courant != null; 
				sommet_courant = sommet_courant.getPrecedentDjikstra()){
			chemin.add(sommet_courant);
			sommet_courant.setSelectionne(true, type_orientation);
			if(sommet_courant.getPrecedentDjikstra() != null)
				for(Arc a: sommet_courant.getPrecedentDjikstra().getArcsSortants())
					if(a.getArrivee() == sommet_courant)
						a.setSelectionne(true, type_orientation);
		}
		
		/*
		 *  Enfin on renverse la liste chemin pour qu'elle soit classée
		 *  de depart à arrivee et pas l'inverse.
		 */
		Collections.reverse(chemin);
	}
	
	/**
	 * Initialise la liste de sommets pour la préparer à l'algorithme.
	 * Chaque sommet du graphe est considéré à une distance infinie du sommet depart et aucun n'a pour l'instant de sommet proche.
	 * 
	 * @param sommets Liste de sommets à initialiser
	 * @param type_orientation Type du Graphe concerné (Orienté ou non)
	 * 
	 * @see GrapheConstantes#ORIENTE
	 * @see GrapheConstantes#NON_ORIENTE
	 */
	private static void init(ArrayList<Sommet> sommets, int type_orientation){
		for(Sommet s: sommets){
			s.setSelectionne(false, type_orientation);
			s.resetDessinArcs(type_orientation);
			s.resetDjikstra();
		}
	}
}
