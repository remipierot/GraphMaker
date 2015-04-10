package fr.graphmaker.outils;

import java.awt.geom.Point2D;

/**
 * Classe permettant de construire des points à deux dimensions.
 * 		- Réel : une coordonnée pour l'abscisse
 * 		- Réel : une coordonnée pour l'ordonnée
 */
public class Point extends Point2D{
	private double x;
	private double y;
	
	/**
	 * Initialise le point avec le couple de coordonnées (0, 0).
	 */
	public Point(){
		this(0, 0);
	}
	
	/**
	 * Initialise le point avec le couple de coordonnées (x, y).
	 * 
	 * @param x Abscisse du point
	 * @param y Ordonnée du point
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialise le point avec le couple de coordonnées (p.x, p.y).
	 * 
	 * @param p Point dont on copie les coordonnées
	 */
	public Point(Point p){
		this(p.x, p.y);
	}
	
	/**
	 * Vérifie que les coordonnées entre le Point courant et le paramètre sont identiques.
	 * 
	 * @param o Objet à comparer au Point courant
	 * @return Renvoie true si l'objet est un Point aux coordonnées identiques à celles du Point courant, false sinon
	 */
	@Override
	public boolean equals(Object o){
		Point tmp = new Point();
		
		if(o instanceof Point)
			tmp = (Point)o;
		
		return (tmp.getX() == getX() && tmp.getY() == getY());
	}
	
	/**
	 * Renvoie la valeur de l'abscisse du Point.
	 * 
	 * @return Abscisse du Point
	 */
	@Override
	public double getX(){
		return x;
	}
	
	/**
	 * Renvoie la valeur de l'ordonnée du Point.
	 * 
	 * @return Ordonnée du Point
	 */
	@Override
	public double getY(){
		return y;
	}
	
	/**
	 * Modifie les coordonnées du Point.
	 * 
	 * @param x Nouvelle abscisse du Point
	 * @param y Nouvelle ordonnée du Point
	 */
	@Override
	public void setLocation(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Renvoie une chaîne de caractères au format "x,y" représentant le Point.
	 * 
	 * @return Chaîne représentant le Point
	 */
	@Override
	public String toString(){
		return x+","+y;
	}
}
