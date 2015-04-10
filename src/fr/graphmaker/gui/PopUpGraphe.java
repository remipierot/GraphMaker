package fr.graphmaker.gui;

import java.awt.BasicStroke;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.Sommet;

/**
 * Classe gérant le pop-up qui apparait sur un clic droit de la classe PanelGraphe.
 */
public class PopUpGraphe extends JPopupMenu implements GrapheConstantes, LanguesConstantes{
	private static final long serialVersionUID = -2512571037204264340L;
    private int langue = FR;
    
    private ArrayList<Sommet> sommets;
    
    private Sommet sommet_selectionne;
    private Arc arc_selectionne;
    private PanelGraphe parent;
    
    private JMenuItem changerValeur;
    private JMenuItem supprimerSommet;
    private JMenu changerFormeSommet;
    private JMenu changerFormeArc;
    private JMenuItem changerDistance;
    private JMenuItem supprimerArc;
    
    /**
     * Initialise le popup en le rattachant au graphe reçu en paramètre.
     * 
     * @param g Graphe à attacher au popup
     */
    public PopUpGraphe(PanelGraphe g){
    	parent = g;
        this.sommets = g.getSommets();
        init_popup();
    }
    
    /**
     * Modifie la langue d'affichage du popup
     * 
     * @param langue Nouvelle langue
     * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
     */
    public void changeLangue(int langue){
    	if(langue == FR || langue == CH || langue == EN){
    		this.langue = langue;
    		refresh();
    	}
    }
    
    /**
     * Vide le popup de tous ses composants.
     */
    public void clearPopUp(){
    	remove(changerDistance);
        remove(supprimerArc);
        remove(changerFormeArc);
        remove(changerValeur);
        remove(supprimerSommet);
        remove(changerFormeSommet);
    	sommet_selectionne = null;
        arc_selectionne = null;
    }

    /**
     * Créé le menu de modification de style des arcs.
     * 
     * @return Menu de modification de style des arcs
     */
    public JMenu createMenuFormesArc(){
		JMenu formesArc = new JMenu(LOC[LOC_STYLE_ARC_POPUP][langue]);
		ButtonGroup arcGroup = new ButtonGroup();
		int type_graphe = NON_ORIENTE;
		
		for(int i=0; i<NOMBRE_ARCS; i++){
			String forme_arc = new String();
			BasicStroke arcStroke = null;
			
			switch(i){
				case ARC_SIMPLE_PLEIN:
					forme_arc = LOC[LOC_ARC_SIMPLE_PLEIN][langue];
					arcStroke = STYLE_ARC_SIMPLE_PLEIN;
					break;
				case ARC_SIMPLE_POINTILLE:
					forme_arc = LOC[LOC_ARC_SIMPLE_POINTILLE][langue];
					arcStroke = STYLE_ARC_SIMPLE_POINTILLE;
					break;
				case ARC_DOUBLE_PLEIN:
					forme_arc = LOC[LOC_ARC_DOUBLE_PLEIN][langue];
					arcStroke = STYLE_ARC_DOUBLE_PLEIN;
					break;
				case ARC_DOUBLE_POINTILLE:
					forme_arc = LOC[LOC_ARC_DOUBLE_POINTILLE][langue];
					arcStroke = STYLE_ARC_DOUBLE_POINTILLE;
					break;
			}
			
			if(type_graphe == ORIENTE)
				type_graphe = ORIENTE;
			else
				type_graphe = NON_ORIENTE;
			
			JRadioButtonMenuItem formeArcButton = new JRadioButtonMenuItem(forme_arc);
			formeArcButton.addActionListener(new FormeArcListener(arcStroke, type_graphe));
			formesArc.add(formeArcButton);
			arcGroup.add(formeArcButton);
			if(parent.getStyleCode() == i || (arc_selectionne!=null && arc_selectionne.getStyleCode() == i))
				formeArcButton.setSelected(true);
		}
		
		return formesArc;
	}
    
    /**
     * Créé le menu de modification de forme des sommets.
     * 
     * @return Menu de modification de forme des sommets
     */
    public JMenu createMenuFormesSommet(){
		JMenu formesSommet = new JMenu(LOC[LOC_FORME_SOMMET_POPUP][langue]);
		ButtonGroup sommetGroup = new ButtonGroup();
		
		for(int i=0; i<NOMBRE_FORMES; i++){
			String forme = new String();
			Shape sommetShape = null;
			
			switch(i){
				case ELLIPSE:
					forme = LOC[LOC_ELLIPSE][langue];
					sommetShape = new Ellipse2D.Double();
					break;
				case RECTANGLE:
					forme = LOC[LOC_RECTANGLE][langue];
					sommetShape = new Rectangle2D.Double();
					break;
				case ROUND_RECTANGLE:
					forme = LOC[LOC_ROUND_RECTANGLE][langue];
					sommetShape = new RoundRectangle2D.Double();
					break;
				case LOSANGE:
					forme = LOC[LOC_LOSANGE][langue];
					sommetShape = new Polygon();
					break;
			}
			
			JRadioButtonMenuItem formeButton = new JRadioButtonMenuItem(forme);
			formeButton.addActionListener(new FormeSommetListener(sommetShape));
			formesSommet.add(formeButton);
			sommetGroup.add(formeButton);
			if(parent.getFormeCode() == i || (sommet_selectionne!=null && sommet_selectionne.getFormeCode() == i))
				formeButton.setSelected(true);
		}
		
		return formesSommet;
	}
    
    /**
     * Modifie l'arc rattaché au popup.
     * Appelé quand on effectue un clic droit sur un arc dans la classe PanelGraphe.
     * 
     * @param a Arc à rattacher au popup
     */
    public void setArc(Arc a){
    	clearPopUp();
    	arc_selectionne = a;
    	init_popup();
        if(a != null){
        	remove(changerValeur);
	        remove(supprimerSommet);
	        remove(changerFormeSommet);
	        
	        if(parent.getTypeArcs() != NON_VALUE)
	        	add(changerDistance);
	        
	        add(supprimerArc);
	        add(changerFormeArc);
        }
    }
    
    /**
     * Modifie le sommet rattaché au popup.
     * Appelé quand on effectue un clic droit sur un sommet dans la classe PanelGraphe.
     * 
     * @param s Sommet à rattacher au popup
     */
    public void setSommet(Sommet s){
    	clearPopUp();
    	sommet_selectionne = s;
    	init_popup();
        if(s != null){
        	remove(changerDistance);
	        remove(supprimerArc);
	        remove(changerFormeArc);
	        add(changerValeur);
	        add(supprimerSommet);
	        add(changerFormeSommet);
        }
    }
    
    /**
     * Initialise l'affichage du popup selon le graphe rattaché.
     */
    private void init_popup(){
    	changerValeur = new JMenuItem(LOC[LOC_VALEUR_SOMMET_POPUP][langue]);
        supprimerSommet = new JMenuItem(LOC[LOC_SUPPRIMER_SOMMET_POPUP][langue]);
        changerFormeSommet = createMenuFormesSommet();
        changerFormeArc = createMenuFormesArc();
        
        if(parent.getTypeArcs() == VALUE)
        	changerDistance = new JMenuItem(LOC[LOC_VALEUR_ARC_POPUP][langue]);
        else
        	changerDistance = new JMenuItem(LOC[LOC_TEXTE_ARC_POPUP][langue]);
        
        supprimerArc = new JMenuItem(LOC[LOC_SUPPRIMER_ARC_POPUP][langue]);
        changerValeur.addActionListener(new JMenuItemList(CHANGER_VALEUR_POPUP));
        supprimerSommet.addActionListener(new JMenuItemList(SUPPRIMER_SOMMET_POPUP));
        changerFormeSommet.addActionListener(new JMenuItemList(CHANGER_FORME_POPUP));
        changerDistance.addActionListener(new JMenuItemList(CHANGER_DISTANCE_POPUP));
        supprimerArc.addActionListener(new JMenuItemList(SUPPRIMER_ARC_POPUP));
    }
	
	/**
     * Met à jour l'affichage du popup.
     */
    private void refresh(){
    	clearPopUp();
    	init_popup();
    }

	/**
	 * Listener du menu de changement de style des arcs.
	 */
	class FormeArcListener implements ActionListener{
		private BasicStroke bs;
		private int type_orientation;
		
		public FormeArcListener(BasicStroke bs, int type_orientation){
			this.bs = bs;
			this.type_orientation = type_orientation;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			arc_selectionne.setForme(bs, type_orientation);
			parent.repaint();
			clearPopUp();
		}
	}
	
	/**
	 * Listener du menu de changement de forme des sommets.
	 */
	class FormeSommetListener implements ActionListener{
		private Shape s;
		
		public FormeSommetListener(Shape s){
			this.s = s;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sommet_selectionne.setForme(s);
			parent.repaint();
			clearPopUp();
		}
	}
	
	/**
	 * Listener des boutons du menu principal du popup.
	 */
	class JMenuItemList implements ActionListener{
	    int codeItem;
	
	    public JMenuItemList(int codeItem){
	        this.codeItem = codeItem;
	    }
	
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
	        switch(codeItem){
	            case CHANGER_VALEUR_POPUP :
	        		String s_sommet = (String)JOptionPane.showInputDialog(
			                    getInvoker(), LOC[LOC_VALEUR_SOMMET_POPUP][langue]+" :\n",
			                    null, JOptionPane.PLAIN_MESSAGE,
			                    null, null,
			                    sommet_selectionne.getValeur());
	
				    if ((s_sommet == null) || (s_sommet.length() == 0)) {
				        sommet_selectionne.setValeur(sommet_selectionne.getValeur());
				    }else{
				    	sommet_selectionne.setValeur(s_sommet);
				    }
				    parent.repaint();
	                break;
	            case SUPPRIMER_SOMMET_POPUP : 
	            	sommet_selectionne.detruireSommet(sommets, parent.getTypeOrientation());
	            	parent.repaint();
	                break;
	            case CHANGER_DISTANCE_POPUP :
	            	String distance = arc_selectionne.getDistance();
	            	if(distance.equals(NO_DISTANCE))
	            		distance = "";
	                String s_arc = (String)JOptionPane.showInputDialog(
	                            getInvoker(), LOC[LOC_VALEUR_ARC_POPUP][langue]+" :\n",
	                            null, JOptionPane.PLAIN_MESSAGE,
	                            null, null,
	                            distance);
	                
	                if(parent.getTypeArcs() == VALUE){
	                	 try{
	                		if ((s_arc == null) || (s_arc.length() == 0))
	 					       arc_selectionne.setDistance(arc_selectionne.getDistance(), parent.getTypeOrientation(), parent.getTypeArcs());
	                		else{
		                		Double.parseDouble(s_arc);
		 					    arc_selectionne.setDistance(s_arc, parent.getTypeOrientation(), parent.getTypeArcs());
	                		}
	        
	                 	}catch(NumberFormatException e){
	                 		JOptionPane.showMessageDialog(null, LOC[LOC_ERREUR_VALUE][langue], LOC[LOC_ERREUR][langue], JOptionPane.ERROR_MESSAGE);
	                 	}
	                }
	                else if(parent.getTypeArcs() == ETIQUETE){
	                	if ((s_arc == null) || (s_arc.length() == 0))
					        arc_selectionne.setDistance(arc_selectionne.getDistance(), parent.getTypeOrientation(), parent.getTypeArcs());
					    else
					    	arc_selectionne.setDistance(s_arc, parent.getTypeOrientation(), parent.getTypeArcs());
	                }
				    parent.repaint();
	                break;
	            case SUPPRIMER_ARC_POPUP : 
	            	arc_selectionne.detruireArc(parent.getTypeOrientation());
	            	parent.repaint();
	                break;
	        }
	        if(parent.getAlgoCode() == ALGO_DJIKSTRA && parent.getSelectionnes().size() >= 1){
        		parent.desactiverOptions();
        		parent.activerOption(DJIKSTRA, langue);
	        }
	        parent.getInterface().refresh();
	        clearPopUp();
	    }
	}
}