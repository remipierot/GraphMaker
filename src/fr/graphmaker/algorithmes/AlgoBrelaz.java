package fr.graphmaker.algorithmes;

import java.awt.Color;
import java.util.ArrayList;

import fr.graphmaker.constantes.CouleursConstantes;
import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.Sommet;

/**
 * Classe utilisée pour réaliser l'algorithme de Brélaz (Coloration) sur une liste de sommets.
 *
 */
public class AlgoBrelaz implements CouleursConstantes, LanguesConstantes{
	private static int nb_chromatique = 0;
	
	/**
	 * Exécute l'algorithme de Brélaz sur la liste de sommets reçue en paramètre.
	 * 
	 * @param sommets Liste de sommets à colorer
	 * @param langue Langue de l'interface utilisateur appelant la méthode
	 * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
	 */
	public static int coloration(ArrayList<Sommet> sommets, int langue){
		AlgoBrelaz.construireBrelaz(sommets);
        return nb_chromatique;
	}
	
	/**
	 * Renvoie le nombre chromatique.
	 * 
	 * @return Nombre chromatique
	 */
	public static int getNbChromatique(){
		return nb_chromatique;
	}
	
	/**
	 * Uniquement sur un graphe non-orienté.
	 * Colore les sommets de la liste sommets selon l'algorithme de Brélaz.
	 * 
	 * @param sommets Liste de sommets à colorer
	 */
	private static void construireBrelaz(ArrayList<Sommet> sommets){
		/*
		 *  La liste index est utilisée pour calculer 
		 *  le nombre chromatique à la fin de la fonction.
		 */
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		/*
		 * On commence par initialiser la liste sommets.
		 * On met le nombre de sommets voisins réellement colorés à 0 
		 * pour chaque sommet de la liste.
		 * Et on met le nombre temporaire de sommets voisins
		 * colorés possible égal au degré du sommet (nombre d'arcs liés).
		 */
		AlgoBrelaz.init(sommets);
		
		/*
		 * On fait tourner l'algorithme sur chacun des sommets.
		 */
		for(int i=0; i<sommets.size(); i++){
			/*
			 * On commence par sélectionner le sommet ayant le plus grand nombre temporaire 
			 * de sommets voisins colorés.
			 */
			Sommet sommet_courant = sommets.get(0);
			for(Sommet s : sommets)
				if(s.getTmpCouleursProches() > sommet_courant.getTmpCouleursProches())
					sommet_courant = s;
			
			/*
			 * Une fois le sommet sélectionné,
			 * son nombre temporaire est "désactivé" (mis à -1)
			 * et on change sa couleur par la première disponible pour lui.
			 * Pour information, nos couleurs sont rangées dans un tableau, 
			 * et la première couleur disponible pour un sommet est la première
			 * du tableau qui ne corresponde pas à la couleur d'un des sommets voisins.
			 */
			sommet_courant.setTmpCouleursProches(-1);
			sommet_courant.setCouleur(getPremiereCouleurDisponible(sommet_courant));
			
			/*
			 * On met ensuite à jour le nombre de couleurs proches de chacun des sommets voisins.
			 */
			for(Arc a : sommet_courant.getArcsSortants()){
				a.getArrivee().setNbCouleurProche(a.getArrivee().getNbCouleursProches()+1);
				if(a.getArrivee().getTmpCouleursProches() != -1)
					a.getArrivee().setTmpCouleursProches(a.getArrivee().getNbCouleursProches());
			}
		}
		
		/*
		 * On enregistre la liste des couleurs utilisées dans le graphe
		 * puis on en stock la taille dans le nombre chromatique.
		 */
		for(Sommet s: sommets)
			if(!index.contains(COULEURS.indexOf(s.getCouleur())))
				index.add(COULEURS.indexOf(s.getCouleur()));
		
		nb_chromatique = index.size();
	}
	
	/**
	 * Renvoie la premiere couleur disponible pour le sommet reçu en paramètre.
	 * 
	 * @param s Sommet dont on veut la couleur possible
	 * @return Couleur disponible pour le sommet reçu en paramètre
	 */
	private static Color getPremiereCouleurDisponible(Sommet s){
		Color couleur_courante = COULEURS.get(0);
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(Arc a: s.getArcsSortants()){
			if(!index.contains(COULEURS.indexOf(a.getArrivee().getCouleur())))
				index.add(COULEURS.indexOf(a.getArrivee().getCouleur()));
		}
		
		for(Color c: COULEURS){
			if(!index.contains(COULEURS.indexOf(c))){
				couleur_courante = c;
				break;
			}
		}
		
		return couleur_courante;
	}
	
	/**
	 * Initialise la liste de sommets reçue en paramètres.
	 * Nombre de sommets voisins réellement colorés mis à 0 pour chaque sommet de la liste.
	 * Nombre temporaire de sommets voisins colorés possible égal au degré du sommet (nombre d'arcs liés).
	 * 
	 * @param sommets Liste de sommets à initialiser
	 */
	private static void init(ArrayList<Sommet> sommets){
		nb_chromatique = 0;
		for(Sommet s : sommets){
			s.setTmpCouleursProches(s.getArcsSortants().size());
			s.setNbCouleurProche(0);
			s.setCouleur(COL_CENTRE);
		}
	}
}
