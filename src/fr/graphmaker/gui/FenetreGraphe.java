package fr.graphmaker.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.graphmaker.constantes.CouleursConstantes;
import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.ImagesConstantes;
import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.outils.Arc;
import fr.graphmaker.outils.GestionnaireFichier;
import fr.graphmaker.outils.Sommet;

/**
 * Fenêtre principale gérant le graphe, ses options ainsi que toutes ses représentations.
 * Contient un ensemble de boutons et un panneau a onglet pour les deux représentations possibles du graphe.
 */
public class FenetreGraphe extends JFrame implements GrapheConstantes, LanguesConstantes, ImagesConstantes, CouleursConstantes{
	private static final long serialVersionUID = -3920397881628182839L;
	private PanelGraphe g;
   	private int langue = FR;
   	
   	private ArrayList<JButton> btns = new ArrayList<JButton>();
   	private JButton deplacer = new JButton(IMG_DEPLACER);
   	private JButton creerSommet = new JButton(IMG_CREER_SOMMET);
   	private JButton creerArc = new JButton(IMG_CREER_ARC);
   	private JButton supprimer = new JButton(IMG_EFFACER);
   	private JButton reset = new JButton(IMG_RESET_ARCS);
   	private JButton deplacerFleche = new JButton(IMG_DEPLACER_FLECHE);
   	private JButton deplacerGraphe = new JButton(IMG_DEPLACER_GRAPHE);
   	private JButton clique = new JButton(IMG_CLIQUE);
   	private JButton vider = new JButton(IMG_VIDER);
	private JButton chaine = new JButton(IMG_CHAINE);
	private JButton cycle = new JButton(IMG_CYCLE);
	
	private JMenu options_graphe;
	private JMenu formes_sommets;
	private JMenu formes_arcs;
	private JMenu langue_menu;
	private JMenu algo_menu;
	private ButtonGroup sommetGroup = new ButtonGroup();
   	private ButtonGroup arcGroup = new ButtonGroup();
   	private ButtonGroup langueGroup = new ButtonGroup();
   	private ButtonGroup algoGroup = new ButtonGroup();
   	private ArrayList<JMenuItem> btns_options = new ArrayList<JMenuItem>();
   	private ArrayList<JMenuItem> btns_sommets = new ArrayList<JMenuItem>();
   	private ArrayList<JMenuItem> btns_arcs = new ArrayList<JMenuItem>();
   	private ArrayList<JMenuItem> btns_langue = new ArrayList<JMenuItem>();
   	private ArrayList<JMenuItem> btns_algo = new ArrayList<JMenuItem>();
   	
   	private JPanel pInfos;
   	private JLabel lOrientation = new JLabel();
   	private JLabel lValuation = new JLabel();
   	private JLabel lBoucles = new JLabel();
   	private JLabel lSommets = new JLabel();
   	private JLabel lArcs = new JLabel();
   	private JLabel lConnexite = new JLabel();
   	
   	private JPanel pSud = new JPanel();
	
   	private JLabel lMarquage = new JLabel();
	private JLabel numMarquage = new JLabel();
	private JButton bMoins = new JButton(IMG_MOINS);
	private JButton bPlus = new JButton(IMG_PLUS);
	
	private JPanel tabBrelaz = new JPanel();
   	
   	private JLabel lPopupDjikstra = new JLabel();
   	private JLabel lPopupDeuxDjikstra = new JLabel();
   	private JPanel tabDjikstra = new JPanel();
   	private JPanel popupDjikstra = new JPanel();
	
	private JTabbedPane pRepresentations = new JTabbedPane();
	private JPanel pSagittale = new JPanel(new BorderLayout());
	private PanelMatrice pMatricielle = new PanelMatrice();
	
	/**
   	 * Initialise la fenêtre et ses composants.
   	 */
	public FenetreGraphe(){
		super("GraphMaker");
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(w/8, h/8, 3*w/4, 3*h/4);
		setJMenuBar(createMenu());
		add(creerPanelNord(), "North");
		pRepresentations.addChangeListener(new TabbedListener());
		pRepresentations.addTab(LOC[LOC_SAGITTALE][langue], IMG_SAGITTALE, pSagittale);
		pRepresentations.addTab(LOC[LOC_MATRICIELLE][langue], IMG_MATRICIELLE, pMatricielle);
		add(pRepresentations, "Center");
		add(creerPanelSud(), "South");
		update_fonts(FONT_MONOSPACE_12);
		refresh();
		setVisible(true);
	}
	
	/**
	 * Créé la barre de menu contenant les options de graphe, de changement de forme, de langue, et les algorithmes.
	 * 
	 * @return Barre de menu
	 */
	public JMenuBar createMenu(){
		options_graphe = new JMenu();
		formes_sommets = new JMenu();
	   	formes_arcs = new JMenu();
	   	langue_menu = new JMenu();
	   	algo_menu = new JMenu();
	   	
		JMenuBar menuBar = new JMenuBar();

		for(int i=0; i<NOMBRE_GRAPHES; i++){
			JMenuItem grapheButton = new JMenuItem();
			grapheButton.addActionListener(new ButtonGrapheListener(i, this));
			options_graphe.add(grapheButton);
			btns_options.add(grapheButton);
		}
		
		for(int i=0; i<NOMBRE_FORMES; i++){
			Shape sommetShape = null;
			
			switch(i){
				case ELLIPSE:
					sommetShape = new Ellipse2D.Double();
					break;
				case RECTANGLE:
					sommetShape = new Rectangle2D.Double();
					break;
				case ROUND_RECTANGLE:
					sommetShape = new RoundRectangle2D.Double();
					break;
				case LOSANGE:
					sommetShape = new Polygon();
					break;
			}
			
			JRadioButtonMenuItem formeSommetButton = new JRadioButtonMenuItem();
			formeSommetButton.addActionListener(new FormeSommetListener(sommetShape));
			formes_sommets.add(formeSommetButton);
			btns_sommets.add(formeSommetButton);
			sommetGroup.add(formeSommetButton);
		}
		
		
		for(int i=0; i<NOMBRE_ARCS; i++){
			BasicStroke arcStroke = null;
			
			switch(i){
				case ARC_SIMPLE_PLEIN:
					arcStroke = STYLE_ARC_SIMPLE_PLEIN;
					break;
				case ARC_SIMPLE_POINTILLE:
					arcStroke = STYLE_ARC_SIMPLE_POINTILLE;
					break;
				case ARC_DOUBLE_PLEIN:
					arcStroke = STYLE_ARC_DOUBLE_PLEIN;
					break;
				case ARC_DOUBLE_POINTILLE:
					arcStroke = STYLE_ARC_DOUBLE_POINTILLE;
					break;
			}
			
			JRadioButtonMenuItem formeArcButton = new JRadioButtonMenuItem();
			formeArcButton.addActionListener(new FormeArcListener(arcStroke));
			formes_arcs.add(formeArcButton);
			btns_arcs.add(formeArcButton);
			arcGroup.add(formeArcButton);
		}
		
		for(int i=0; i<NOMBRE_LANGUES; i++){
			JRadioButtonMenuItem langueButton = new JRadioButtonMenuItem();
			langueButton.addActionListener(new LangueListener(i));
			this.langue_menu.add(langueButton);
			btns_langue.add(langueButton);
			langueGroup.add(langueButton);
		}
		
		for(int i=0; i<NOMBRE_ALGO; i++){
			JRadioButtonMenuItem algoButton = new JRadioButtonMenuItem();
			algoButton.addActionListener(new ButtonMenuListener(i+10));
			algo_menu.add(algoButton);
			btns_algo.add(algoButton);
			algoGroup.add(algoButton);
		}
		
		refresh_menu();
		
		menuBar.add(options_graphe);
		menuBar.add(formes_sommets);
		menuBar.add(formes_arcs);
		menuBar.add(algo_menu);
		menuBar.add(langue_menu);
		
		return menuBar;
	}
	
	/**
	 * Créé le panel nord contenant les boutons et le panel d'informations.
	 * 
	 * @return Panel nord
	 */
	public JPanel creerPanelNord(){
		JPanel pNord = new JPanel(new GridLayout(1,2,0,0));
		JPanel pButton = new JPanel(new GridLayout(1,11,0,0));
		pInfos = new JPanel(new GridLayout(3,2,0,0));
		
		btns.add(creerSommet);
		btns.add(creerArc);
		btns.add(deplacer);
		btns.add(supprimer);
		btns.add(reset);
		btns.add(deplacerFleche);
		btns.add(deplacerGraphe);
		btns.add(clique);
		btns.add(chaine);
		btns.add(cycle);
		btns.add(vider);
		
		creerSommet.addActionListener(new ButtonMenuListener(CREER_SOMMET, creerSommet));
		creerArc.addActionListener(new ButtonMenuListener(CREER_ARC, creerArc));
		deplacer.addActionListener(new ButtonMenuListener(DEPLACER, deplacer));
		supprimer.addActionListener(new ButtonMenuListener(SUPPRIMER, supprimer));
		reset.addActionListener(new ButtonMenuListener(RESET_ARCS, reset));
		deplacerFleche.addActionListener(new ButtonMenuListener(DEPLACER_FLECHE, deplacerFleche));
		deplacerGraphe.addActionListener(new ButtonMenuListener(DEPLACER_GRAPHE, deplacerGraphe));
		clique.addActionListener(new ButtonMenuListener(CLIQUE, clique));
		vider.addActionListener(new ButtonMenuListener(VIDER, vider));
		chaine.addActionListener(new ButtonMenuListener(CHAINE, chaine));
		cycle.addActionListener(new ButtonMenuListener(CYCLE, cycle));
		
		creerSommet.addMouseListener(new ButtonIconeListener(creerSommet));
		creerArc.addMouseListener(new ButtonIconeListener(creerArc));
		deplacer.addMouseListener(new ButtonIconeListener(deplacer));
		supprimer.addMouseListener(new ButtonIconeListener(supprimer));
		reset.addMouseListener(new ButtonIconeListener(reset));
		deplacerFleche.addMouseListener(new ButtonIconeListener(deplacerFleche));
		deplacerGraphe.addMouseListener(new ButtonIconeListener(deplacerGraphe));
		clique.addMouseListener(new ButtonIconeListener(clique));
		vider.addMouseListener(new ButtonIconeListener(vider));
		chaine.addMouseListener(new ButtonIconeListener(chaine));
		cycle.addMouseListener(new ButtonIconeListener(cycle));
		
		modifierBouton(creerSommet);
		modifierBouton(creerArc);
		modifierBouton(deplacer);
		modifierBouton(supprimer);
		modifierBouton(reset);
		modifierBouton(clique);
		modifierBouton(deplacerFleche);
		modifierBouton(deplacerGraphe);
		modifierBouton(vider);
		modifierBouton(chaine);
		modifierBouton(cycle);
		
		pButton.add(creerSommet);
		pButton.add(creerArc);
		pButton.add(deplacer);
		pButton.add(deplacerFleche);
		pButton.add(deplacerGraphe);
		pButton.add(reset);
		pButton.add(chaine);
		pButton.add(cycle);
		pButton.add(clique);
		pButton.add(supprimer);
		pButton.add(vider);
		
		pInfos.add(lOrientation);
		pInfos.add(lSommets);
		pInfos.add(lValuation);
		pInfos.add(lArcs);
		pInfos.add(lBoucles);
		pInfos.add(lConnexite);
		
		pNord.add(pButton);
		pNord.add(pInfos);

		return pNord;
	}
	
	/**
	 * Créé le panel Sud contenant les panel d'informations des différents algorithmes lorsqu'ils sont selectionnés.
	 * 
	 * @return Panel sud
	 */
	public JPanel creerPanelSud(){
		JPanel pbSud = new JPanel(new BorderLayout());
		pSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		btns.add(bPlus);
		btns.add(bMoins);
		bPlus.addMouseListener(new ButtonIconeListener(bPlus));
		bMoins.addMouseListener(new ButtonIconeListener(bMoins));
		
		modifierBouton(bPlus);
		modifierBouton(bMoins);
		
		numMarquage.setText("1");
		
		bPlus.addActionListener(new BoutonMarquageListener(Integer.parseInt(this.getNumMarquage())));
		bMoins.addActionListener(new BoutonMarquageListener(Integer.parseInt(this.getNumMarquage())));
		
		pSud.add(lMarquage);
		pSud.add(bMoins);
		pSud.add(numMarquage);
		pSud.add(bPlus);
		
		pSud.add(tabBrelaz);
		
		pSud.add(lPopupDjikstra);
		pSud.add(lPopupDeuxDjikstra);
		pSud.add(tabDjikstra);
		
		pbSud.add(pSud, BorderLayout.NORTH);
		pbSud.add(popupDjikstra, BorderLayout.SOUTH);
		
		return pbSud;
	}
	
	/**
	 * Renvoie le graphe actuellement attaché à la fenêtre.
	 * 
	 * @return Graphe attaché à la fenêtre
	 */
	public PanelGraphe getGraphe(){
		return g;
	}
	
	/**
	 * Renvoie le niveau de marquage actuellement affiché dans le panel sud pour l'algorithme du marquage.
	 * 
	 * @return Niveau de marquage actuellement affiché
	 */
	public String getNumMarquage(){
		return numMarquage.getText();
	}
	
	/**
	 * Modifie le bouton reçu en paramètre pour lui donner l'aspect voulu par notre design.
	 * 
	 * @param btn JButton à modifier
	 */
	public void modifierBouton(JButton btn){
		btn.setFont(FONT_MONOSPACE_10); 
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setFocusPainted(false);
	}
	
	/**
	 * Met à jour tous les composants de la fenêtre.
	 */
	public void refresh(){
		refresh_menu();
		refresh_buttons();
		refresh_infos();
	   	refresh_tabs();
	}
	
	/**
	 * Met à jour le panel contenant les boutons.
	 */
	public void refresh_buttons(){		
		if(g!=null){			
			creerSommet.setEnabled(true);
			supprimer.setEnabled(true);
			deplacerGraphe.setEnabled(true);
			clique.setEnabled(true);
			deplacer.setEnabled(true);
			creerArc.setEnabled(true);
			reset.setEnabled(true);
			chaine.setEnabled(true);
			cycle.setEnabled(true);
		   	
		   	if(g.getSommets().size() == 0){
		   		supprimer.setEnabled(false);
				deplacerGraphe.setEnabled(false);
				clique.setEnabled(false);
				deplacer.setEnabled(false);
				creerArc.setEnabled(false);
				reset.setEnabled(false);
				chaine.setEnabled(false);
				cycle.setEnabled(false);
		   	}
		   	
		   	if(g.getSommets().size() != 0)
				vider.setEnabled(true);
		   	else
		   		vider.setEnabled(false);
			
			if(g.getSommets().size()!=0)
				deplacerFleche.setEnabled(true);
			else
				deplacerFleche.setEnabled(false);
			
			creerSommet.setToolTipText(LOC[LOC_CREER_SOMMET][langue]);
			supprimer.setToolTipText(LOC[LOC_EFFACER][langue]);
			deplacerFleche.setToolTipText(LOC[LOC_DEPLACER_FLECHE][langue]);
			deplacerGraphe.setToolTipText(LOC[LOC_DEPLACER_GRAPHE][langue]);
			clique.setToolTipText(LOC[LOC_CLIQUE][langue]);
			vider.setToolTipText(LOC[LOC_VIDER][langue]);
			chaine.setToolTipText(LOC[LOC_CHAINE][langue]);
			cycle.setToolTipText(LOC[LOC_CYCLE][langue]);
			
			if(g.getTypeOrientation() == ORIENTE){
				deplacer.setToolTipText(LOC[LOC_DEPLACER_S_ARCS][langue]);
				creerArc.setToolTipText(LOC[LOC_CREER_ARC][langue]);
				reset.setToolTipText(LOC[LOC_REINITIALISER_ORIENTE][langue]);
				creerArc.setIcon(IMG_CREER_ARC);
			}
			else{
				deplacer.setToolTipText(LOC[LOC_DEPLACER_S_ARETES][langue]);
				creerArc.setToolTipText(LOC[LOC_CREER_ARETE][langue]);
				reset.setToolTipText(LOC[LOC_REINITIALISER_NON_ORIENTE][langue]);
				creerArc.setIcon(IMG_CREER_ARETE);
			}
		}
		else{
			creerSommet.setEnabled(false);
			supprimer.setEnabled(false);
			deplacerFleche.setEnabled(false);
			deplacerGraphe.setEnabled(false);
			clique.setEnabled(false);
			deplacer.setEnabled(false);
			creerArc.setEnabled(false);
			reset.setEnabled(false);
			vider.setEnabled(false);
			chaine.setEnabled(false);
			cycle.setEnabled(false);
		}
		for(JButton btn: btns)
			btn.repaint();
	}
	
	/**
	 * Met à jour le panel d'informations.
	 */
	public void refresh_infos(){
		TitledBorder b = BorderFactory.createTitledBorder(LOC[LOC_INFOS_GRAPHE][langue]);
		b.setTitleFont(FONT_MONOSPACE_12);
		pInfos.setBorder(b);
		
		if(g!=null){
			pInfos.setEnabled(true);
			lOrientation.setEnabled(true);
		   	lValuation.setEnabled(true);
		   	lBoucles.setEnabled(true);
		   	lSommets.setEnabled(true);
		   	lArcs.setEnabled(true);
		   	lConnexite.setEnabled(true);
		   	
		   	if(g.getTypeOrientation() == NON_ORIENTE)
				lOrientation.setText(LOC[LOC_INFOS_ORIENTATION][langue]+LOC[LOC_NON_ORIENTE][langue]);
			else
				lOrientation.setText(LOC[LOC_INFOS_ORIENTATION][langue]+LOC[LOC_ORIENTE][langue]);
			
			if(g.getTypeArcs() == NON_VALUE)
				lValuation.setText(LOC[LOC_INFOS_VALUATION][langue]+LOC[LOC_NON_VALUE][langue]);
			else if(g.getTypeArcs() == VALUE)
				lValuation.setText(LOC[LOC_INFOS_VALUATION][langue]+LOC[LOC_VALUE][langue]);
			else
				lValuation.setText(LOC[LOC_INFOS_VALUATION][langue]+LOC[LOC_ETIQUETE][langue]);
			
			if(g.getTypeSimple() == SIMPLE)
				lBoucles.setText(LOC[LOC_INFOS_BOUCLES][langue]+LOC[LOC_INFOS_NON][langue]);
			else
				lBoucles.setText(LOC[LOC_INFOS_BOUCLES][langue]+LOC[LOC_INFOS_OUI][langue]);
			
		   	lSommets.setText(LOC[LOC_INFOS_SOMMETS][langue]+g.getSommets().size());
		   	
		   	if(g.getTypeOrientation() == NON_ORIENTE)
		   		lArcs.setText(LOC[LOC_INFOS_ARETES][langue]+g.getArcs().size()/2);
		   	else
		   		lArcs.setText(LOC[LOC_INFOS_ARCS][langue]+g.getArcs().size());
		   	
		   	if(g.estConnexe())
		   		lConnexite.setText(LOC[LOC_INFOS_CONNEXE][langue]+LOC[LOC_INFOS_OUI][langue]);
		   	else
		   		lConnexite.setText(LOC[LOC_INFOS_CONNEXE][langue]+LOC[LOC_INFOS_NON][langue]);
		}
		else{
			pInfos.setEnabled(false);
			lOrientation.setEnabled(false);
		   	lValuation.setEnabled(false);
		   	lBoucles.setEnabled(false);
		   	lSommets.setEnabled(false);
		   	lArcs.setEnabled(false);
		   	lConnexite.setEnabled(false);
		   	
		   	lOrientation.setText(LOC[LOC_INFOS_ORIENTATION][langue]);
		   	lValuation.setText(LOC[LOC_INFOS_VALUATION][langue]);
		   	lBoucles.setText(LOC[LOC_INFOS_BOUCLES][langue]);
		   	lSommets.setText(LOC[LOC_INFOS_SOMMETS][langue]);
		   	lArcs.setText(LOC[LOC_INFOS_ARCS][langue]);
		   	lConnexite.setText(LOC[LOC_INFOS_CONNEXE][langue]);
		}
		
		pInfos.repaint();
	}
	
	/**
	 * Met à jour la barre de menu.
	 */
	public void refresh_menu(){
		options_graphe.setText(LOC[LOC_MENU_GRAPHE][langue]);
		formes_sommets.setText(LOC[LOC_MENU_SOMMETS][langue]);
	   	formes_arcs.setText(LOC[LOC_MENU_ARCS][langue]);
	   	langue_menu.setText(LOC[LOC_MENU_LANGUE][langue]);
	   	algo_menu.setText(LOC[LOC_ALGORITHMES][langue]);
		
		btns_options.get(NOUVEAU_GRAPHE).setText(LOC[LOC_CREER_GRAPHE][langue]);
		btns_options.get(CHARGER_GRAPHE).setText(LOC[LOC_CHARGER_GRAPHE][langue]);
		btns_options.get(SAUVEGARDER_GRAPHE).setText(LOC[LOC_SAUVEGARDER_GRAPHE][langue]);
		btns_options.get(CONVERSION_LATEX).setText(LOC[LOC_CONVERSION_LATEX][langue]);
		btns_options.get(FERMER_GRAPHE).setText(LOC[LOC_FERMER_GRAPHE][langue]);
		btns_options.get(EDITER_GRAPHE).setText(LOC[LOC_EDITER_GRAPHE][langue]);
		btns_options.get(DESORIENTER_GRAPHE).setText(LOC[LOC_DESORIENTER_GRAPHE][langue]);
		btns_options.get(SIMPLIFIER_GRAPHE).setText(LOC[LOC_SIMPLIFIER_GRAPHE][langue]);
		
		btns_langue.get(FR).setText(LOC[LOC_FRANCAIS][langue]);
		btns_langue.get(CH).setText(LOC[LOC_CHINOIS][langue]);
		btns_langue.get(EN).setText(LOC[LOC_ANGLAIS][langue]);
		
		btns_langue.get(langue).setSelected(true);
		
		if(g == null){
			btns_options.get(SAUVEGARDER_GRAPHE).setEnabled(false);
			btns_options.get(CONVERSION_LATEX).setEnabled(false);
			btns_options.get(FERMER_GRAPHE).setEnabled(false);
			btns_options.get(EDITER_GRAPHE).setEnabled(false);
			btns_options.get(DESORIENTER_GRAPHE).setEnabled(false);
			btns_options.get(SIMPLIFIER_GRAPHE).setEnabled(false);
			
			formes_sommets.setEnabled(false);
			formes_arcs.setEnabled(false);
			algo_menu.setEnabled(false);
		}
		else{
			btns_options.get(NOUVEAU_GRAPHE).setEnabled(true);
			btns_options.get(CHARGER_GRAPHE).setEnabled(true);
			btns_options.get(SAUVEGARDER_GRAPHE).setEnabled(true);
			btns_options.get(CONVERSION_LATEX).setEnabled(true);
			btns_options.get(FERMER_GRAPHE).setEnabled(true);
			btns_options.get(EDITER_GRAPHE).setEnabled(true);
			
			if(g.getTypeOrientation() == NON_ORIENTE){
				btns_options.get(DESORIENTER_GRAPHE).setEnabled(false);
				btns_algo.get(ALGO_BRELAZ).setEnabled(true);
			}
			else{
				btns_options.get(DESORIENTER_GRAPHE).setEnabled(true);
				btns_algo.get(ALGO_BRELAZ).setEnabled(false);
			}
			
			if(g.getTypeSimple() == SIMPLE || g.getTypeOrientation() == NON_ORIENTE)
				btns_options.get(SIMPLIFIER_GRAPHE).setEnabled(false);
			else
				btns_options.get(SIMPLIFIER_GRAPHE).setEnabled(true);
			
			if(g.getTypeArcs() != VALUE)
				btns_algo.get(ALGO_DJIKSTRA).setEnabled(false);
			else
				btns_algo.get(ALGO_DJIKSTRA).setEnabled(true);
			
			btns_sommets.get(ELLIPSE).setText(LOC[LOC_ELLIPSE][langue]);
			btns_sommets.get(RECTANGLE).setText(LOC[LOC_RECTANGLE][langue]);
			btns_sommets.get(ROUND_RECTANGLE).setText(LOC[LOC_ROUND_RECTANGLE][langue]);
			btns_sommets.get(LOSANGE).setText(LOC[LOC_LOSANGE][langue]);
			btns_sommets.get(g.getFormeCode()).setSelected(true);
			
			btns_arcs.get(ARC_SIMPLE_PLEIN).setText(LOC[LOC_ARC_SIMPLE_PLEIN][langue]);
			btns_arcs.get(ARC_SIMPLE_POINTILLE).setText(LOC[LOC_ARC_SIMPLE_POINTILLE][langue]);
			btns_arcs.get(ARC_DOUBLE_PLEIN).setText(LOC[LOC_ARC_DOUBLE_PLEIN][langue]);
			btns_arcs.get(ARC_DOUBLE_POINTILLE).setText(LOC[LOC_ARC_DOUBLE_POINTILLE][langue]);
			btns_arcs.get(g.getStyleCode()).setSelected(true);
			
			btns_algo.get(ALGO_DJIKSTRA).setText(LOC[LOC_ALGO_DJIKSTRA][langue]);
			btns_algo.get(ALGO_BRELAZ).setText(LOC[LOC_ALGO_BRELAZ][langue]);
			btns_algo.get(ALGO_MARQUAGE).setText(LOC[LOC_ALGO_MARQUAGE][langue]);
			
			if(g.getAlgoCode() != -1)
				btns_algo.get(g.getAlgoCode()).setSelected(true);
			else
				algoGroup.clearSelection();
			
			formes_sommets.setEnabled(true);
			formes_arcs.setEnabled(true);
			algo_menu.setEnabled(true);
		}
		if(getJMenuBar()!=null)
			getJMenuBar().repaint();
	}
	
	/**
	 * Met à jour le panel contenant les représentations.
	 */
	public void refresh_tabs(){
		lMarquage.getParent().setVisible(false);
		lMarquage.setVisible(false);
		bPlus.setVisible(false);
		numMarquage.setVisible(false);
		bMoins.setVisible(false);
		
		tabBrelaz.getParent().setVisible(false);
		tabBrelaz.setVisible(false);
		tabBrelaz.removeAll();
		
		lPopupDjikstra.getParent().setVisible(false);
		lPopupDjikstra.setVisible(false);
		lPopupDeuxDjikstra.setVisible(false);
		tabDjikstra.setVisible(false);
		tabDjikstra.removeAll();
		popupDjikstra.setVisible(false);
		popupDjikstra.removeAll();
		
		pRepresentations.setTitleAt(SAGITTALE, LOC[LOC_SAGITTALE][langue]);
	   	pRepresentations.setTitleAt(MATRICIELLE, LOC[LOC_MATRICIELLE][langue]);
	   	
	   	if(g != null){
	   		if(pMatricielle.isShowing()){
			   	pMatricielle.setGraphe(g);
			   	pMatricielle.refresh();
	   		}
		   	if(g.getAlgoCode() == ALGO_MARQUAGE){
	   			lMarquage.setText(LOC[LOC_SET_MARQUAGE][langue]);
	   			lMarquage.getParent().setVisible(true);
		   		lMarquage.setEnabled(true);
			   	numMarquage.setEnabled(true);
			   	bPlus.setEnabled(false);
				bMoins.setEnabled(false);
				lMarquage.setVisible(true);
		   		numMarquage.setVisible(true);
		   		bPlus.setVisible(true);
				bMoins.setVisible(true);
			   	if(g.getSelectionnes().size() > 0){
				   	if(g.isMarquageComplet(SUCCESSEURS))
						bPlus.setContentAreaFilled(false);
				   	else
				   		bPlus.setEnabled(true);
					if(g.isMarquageComplet(PREDECESSEURS))
						bMoins.setContentAreaFilled(false);
					else
						bMoins.setEnabled(true);
			   	}
		   	}
		   	if(g.getAlgoCode() == ALGO_BRELAZ){
		   		tabBrelaz.getParent().setVisible(true);
		   		tabBrelaz.setVisible(true);
		   		tabBrelaz.setLayout(new GridLayout(2,1,10,10));
		   		tabBrelaz.add(new JLabel(LOC[LOC_CHROMATIQUE][langue] + g.getNbChromatique(), JLabel.CENTER));
		   		tabBrelaz.add(new JLabel("X(G) ≤ " + g.getNbChromatique(), JLabel.CENTER));
		   	}
		   	if(g.getAlgoCode() == ALGO_DJIKSTRA){
		   		lPopupDjikstra.setText(LOC[LOC_POPUP_DJIKSTRA][langue]);
	   			lPopupDjikstra.getParent().setVisible(true);
		   		lPopupDjikstra.setVisible(true);
		   		
		   		if(g.getSelectionnes().size() == 1){
		   			lPopupDeuxDjikstra.setText(LOC[LOC_POPUP_DEUX_DJIKSTRA][langue]);
		   			lPopupDjikstra.setVisible(false);
		   			lPopupDeuxDjikstra.setVisible(true);
		   		}
		   		
		   		if(g.getSelectionnes().size() == 2){
			   		lPopupDjikstra.setVisible(false);
		   			lPopupDeuxDjikstra.setVisible(false);
		   			tabDjikstra.setVisible(true);
		   			popupDjikstra.setVisible(true);
		   			
		   			tabDjikstra.setLayout(new GridLayout(2,g.getSommetsDjikstra().size()+1,10,10));
		   			tabDjikstra.setFont(FONT_MONOSPACE_12);
		   			tabDjikstra.add(new JLabel("Som(G)", JLabel.CENTER));
		   			for(Sommet s : g.getSommetsDjikstra())
		   				tabDjikstra.add(new JLabel(s.getValeur(), JLabel.CENTER));
		   			tabDjikstra.add(new JLabel("d_min", JLabel.CENTER));
		   			tabDjikstra.add(new JLabel("0", JLabel.CENTER));
   					int tmp=0;
   					int distance=0;
		   			for(Sommet s : g.getSommetsDjikstra())
		   				if(!s.equals(g.getSelectionnes().get(1)))
			   				for(Arc a: s.getArcsSortants())
				   				if(a.isSelectionne() && a.getArrivee().getPrecedentDjikstra() == s){
			   						distance = Integer.parseInt(a.getDistance());
			   						distance += tmp;
			   						tabDjikstra.add(new JLabel(Integer.toString(distance), JLabel.CENTER));
				   					tmp = distance;
				   				}
		   			popupDjikstra.setLayout(new GridLayout(1,1,10,10));
		   			popupDjikstra.add(new JLabel(LOC[LOC_DEMANDE_SELECTION_DJIKSTRA][langue], JLabel.CENTER));
		   		}
		   	}
	   	}
	   	else{
	   		lMarquage.getParent().setVisible(false);
	   		lMarquage.setEnabled(false);
		   	numMarquage.setEnabled(false);
		   	bMoins.setEnabled(false);
		   	bPlus.setEnabled(false);
   		}
	   	
	   	pRepresentations.repaint();
	   	lMarquage.repaint();
	   	numMarquage.repaint();
	   	bMoins.repaint();
	   	bPlus.repaint();
	   	
	   	tabBrelaz.repaint();
	   	
	   	tabDjikstra.repaint();
	}
	
	/**
	 * Met à jour le graphe de la fenêtre.
	 * 
	 * @param g Nouveau graphe à attacher à la fenêtre
	 */
	public void setGraphe(PanelGraphe g){
		if(this.g != null)
			pSagittale.remove(this.g);
		this.g = g;
		if(g != null){
			g.changeLangue(langue);
			g.setFont(FONT_MONOSPACE_12);
			pSagittale.add(g, "Center");
		}
		refresh();
	}
	
	/**
	 * Met à jour le niveau de marquage affiché dans le panel sud pour l'algorithme du marquage.
	 * 
	 * @param numMarquage Nouvelle valeur du niveau de marquage
	 */
	public void setNumMarquage(String numMarquage){
		this.numMarquage.setText(numMarquage);
	}
	
	/**
	 * Annule le clic de tous les boutons de la fenêtre.
	 * Autrement dit, ils reviennent à leur état de base (non survolé, non cliqué).
	 */
	public void unclickAll(){
		for(JButton btn: btns){
			btn.setContentAreaFilled(false);
			btn.setBackground(COL_BOUTON);
			for(MouseListener ml: btn.getMouseListeners())
				if(ml instanceof ButtonIconeListener)
					if(((ButtonIconeListener)ml).cliqued)
						((ButtonIconeListener)ml).cliqued = false;
		}
	}
	
	/**
	 * Met à jour la police de tous les composants de la fenêtre avec la police reçue en paramètre.
	 * 
	 * @param f Police à appliquer à tous les composants de la fenêtre
	 */
	private void update_fonts(Font f){
		if(lOrientation!= null)
			lOrientation.setFont(f);
		if(lValuation!= null)
			lValuation.setFont(f);
		if(lBoucles!= null)
	   		lBoucles.setFont(f);
		if(lSommets!= null)
		   	lSommets.setFont(f);
		if(lArcs!= null)
		   	lArcs.setFont(f);
		if(lConnexite!= null)
		   	lConnexite.setFont(f);
		if(lMarquage!= null)
		   	lMarquage.setFont(f);
		if(numMarquage!= null)
		   	numMarquage.setFont(f);
		if(lPopupDjikstra!= null)
			lPopupDjikstra.setFont(f);
		if(lPopupDeuxDjikstra!= null)
			lPopupDeuxDjikstra.setFont(f);
		if(pRepresentations!= null)
		   	pRepresentations.setFont(f);
		if(options_graphe!= null){
		   	options_graphe.setFont(f);
		   	for(JMenuItem item: btns_options)
				item.setFont(f);
		}
		if(formes_sommets!= null){
			formes_sommets.setFont(f);
			for(JMenuItem item: btns_sommets)
				item.setFont(f);
		}
		if(formes_arcs!= null){
		   	formes_arcs.setFont(f);
		   	for(JMenuItem item: btns_arcs)
				item.setFont(f);
		}
		if(langue_menu!= null){
		   	langue_menu.setFont(f);
		   	for(JMenuItem item: btns_langue)
				item.setFont(f);
		}
		if(algo_menu!= null){
		   	algo_menu.setFont(f);
		   	for(JMenuItem item: btns_algo)
				item.setFont(f);
		}
		if(lPopupDjikstra!= null)
			lPopupDjikstra.setFont(f);
		if(lPopupDeuxDjikstra!= null)
			lPopupDeuxDjikstra.setFont(f);
		if(tabDjikstra!= null)
			tabDjikstra.setFont(f);
		if(popupDjikstra!= null)
			popupDjikstra.setFont(f);
	}
	
	/**
	 * Listener des boutons de l'algorithme du marquage.
	 * Gère l'augmentation et la diminution des niveaux de marquage.
	 */
	class BoutonMarquageListener implements ActionListener{
		private int niveau;
		
		public BoutonMarquageListener(int niveau){
			this.niveau = niveau;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == bPlus){
				niveau = (Integer.parseInt(getNumMarquage())+1);
			}
			else if(e.getSource() == bMoins){
				niveau = (Integer.parseInt(getNumMarquage())-1);
			}
			g.setNiveauMarquage(niveau);
			setNumMarquage(Integer.toString(niveau));
			g.activerOption(MARQUAGE, langue);
			refresh_tabs();
		}
	}
	
	/**
	 * Listener des boutons du sous-menu graphe.
	 */
	class ButtonGrapheListener implements ActionListener{
   		int codeBouton;
   		FenetreGraphe gi;
		
		public ButtonGrapheListener(int codeBouton, FenetreGraphe g){
			this.codeBouton = codeBouton;
			this.gi = g;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			unclickAll();
			switch(codeBouton){
				case NOUVEAU_GRAPHE:
					new CreationGraphe(gi, langue, MODE_CREATION);
					break;
				case CHARGER_GRAPHE:
					PanelGraphe old = g;
					setGraphe(GestionnaireFichier.load(langue));
					if(g != null)
						g.attacherParent(gi);
					else
						setGraphe(old);
					break;
				case SAUVEGARDER_GRAPHE:
					GestionnaireFichier.save(g, langue);
					break;
				case CONVERSION_LATEX:
					pMatricielle.setGraphe(g);
					GestionnaireFichier.exportationLatex(pMatricielle, langue);
					break;
				case FERMER_GRAPHE:
					if(JOptionPane.showConfirmDialog(null,
							LOC[LOC_CONFIRM_FERMER][langue], 
							LOC[LOC_FERMER_GRAPHE][langue], 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE) 
							== JOptionPane.YES_OPTION)
						setGraphe(null);
					break;
				case EDITER_GRAPHE:
					new CreationGraphe(gi, langue, MODE_EDITION);
					break;
				case DESORIENTER_GRAPHE:
					if(JOptionPane.showConfirmDialog(null,
							LOC[LOC_CONFIRM_DESORIENTER][langue], 
							LOC[LOC_DESORIENTER_GRAPHE][langue], 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE) 
							== JOptionPane.YES_OPTION){
						g.setTypeOrientation(NON_ORIENTE);
						g.desactiverOptions();
						refresh();
					}
					break;
				case SIMPLIFIER_GRAPHE:
					if(JOptionPane.showConfirmDialog(null,
							LOC[LOC_CONFIRM_SIMPLIFIER][langue], 
							LOC[LOC_SIMPLIFIER_GRAPHE][langue], 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE) 
							== JOptionPane.YES_OPTION){
						g.setTypeSimple(SIMPLE);
						g.desactiverOptions();
						refresh();
					}
					break;
			}
		}
   	}
	
	/**
	 * Listener des boutons de la fenêtre.
	 * Gère le changement de couleur de ces derniers.
	 */
	class ButtonIconeListener extends MouseAdapter {
		private JButton bouton;
		private boolean cliqued = false;

		public ButtonIconeListener(JButton bouton){
			this.bouton = bouton;
		}
		
		public void mouseClicked(MouseEvent e){
			if(bouton.isEnabled()){
				for(JButton b: btns)
					for(MouseListener ml: b.getMouseListeners())
						if(ml instanceof ButtonIconeListener)
							if(((ButtonIconeListener)ml).cliqued)
								((ButtonIconeListener)ml).cliqued = false;
				cliqued = true;
			}
		}
		
		public void mouseEntered(MouseEvent e) {
			if(bouton.isEnabled() && (!cliqued || bouton==bMoins || bouton==bPlus)){
				bouton.setContentAreaFilled(true);
				bouton.setBackground(COL_BOUTON);
				for(JButton btn: btns)
					btn.repaint();
			}
		}
		
		public void mouseExited(MouseEvent e) {
			if(bouton.isEnabled()){
				if(!cliqued || bouton==bMoins || bouton==bPlus)
					bouton.setContentAreaFilled(false);
				for(JButton btn: btns)
					btn.repaint();
			}
		}
	}
	
	/**
	 * Listener des boutons de la fenêtre.
	 * Gère les actions de ces derniers et leurs effets sur le graphe.
	 */
	class ButtonMenuListener implements ActionListener{
		private int codeBouton;
		private JButton btn;
		
		public ButtonMenuListener(int codeBouton){
			this.codeBouton = codeBouton;
			btn = null;
		}
		
		public ButtonMenuListener(int codeBouton, JButton btn){
			this.codeBouton = codeBouton;
			this.btn = btn;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {			
			int safe_check = JOptionPane.YES_OPTION;
			
			if(codeBouton == VIDER)
				safe_check = JOptionPane.showConfirmDialog(null,
								LOC[LOC_CONFIRM_VIDER][langue], 
								LOC[LOC_VIDER][langue], 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.WARNING_MESSAGE);
			
			if(safe_check == JOptionPane.YES_OPTION){
				if(btn != null){
					unclickAll();
					btn.setContentAreaFilled(true);
					btn.setBackground(COL_BOUTON_SELECT);
					if(btn==reset || btn==clique || btn==vider || 
							btn==chaine || btn==cycle)
						unclickAll();
				}
				
				g.activerOption(codeBouton, langue);
				if(codeBouton == DJIKSTRA || codeBouton == BRELAZ || codeBouton == MARQUAGE)
					unclickAll();
				if(codeBouton == MARQUAGE){
					g.setNiveauMarquage(0);
					numMarquage.setText("0");
				}
				refresh();
			}
		}
	}
	
	/**
	 * Listener des boutons du sous-menu Style des arcs/arêtes.
	 */
	class FormeArcListener implements ActionListener{
		private BasicStroke s;
		
		public FormeArcListener(BasicStroke s){
			this.s = s;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			g.setFormeArcs(s);
			refresh();
		}
	}
	
	/**
	 * Listener des boutons du sous-menu Forme des sommets.
	 */
	class FormeSommetListener implements ActionListener{
		private Shape s;
		
		public FormeSommetListener(Shape s){
			this.s = s;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			g.setFormeSommets(s);
			refresh();
		}
	}
	
	/**
	 * Listener des boutons du sous-menu Langue.
	 */
	class LangueListener implements ActionListener{
		private int langue_list;
		
		public LangueListener(int langue){
			langue_list = langue;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			langue = langue_list;
			
			if(g != null)
				g.changeLangue(langue_list);
			
			refresh();
		}
	}
	
	/**
	 * Listener du panneau a onglets.
	 * Gère le changement d'onglet.
	 */
	class TabbedListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent arg0) {
			if(pMatricielle.isShowing()){
			   	pMatricielle.setGraphe(g);
			   	pMatricielle.refresh();
		   	}
		}
		
	}
}
