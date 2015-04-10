package fr.graphmaker.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;

import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.outils.Arc;

/**
 * Classe correspondant à la fenêtre de création et d'édition de graphes.
 */
public class CreationGraphe extends JFrame implements GrapheConstantes, LanguesConstantes{
	private static final long serialVersionUID = 4739814042624898985L;
	private int langue = FR;
	
	private JTextField nom;
	
	private JRadioButton oriente;
	private JRadioButton non_oriente;
	private JRadioButton simple;
	private JRadioButton non_simple;
	private JRadioButton value;
	private JRadioButton etiquete;
	private JRadioButton non_value;
	
	private JButton valider;
	
	private FenetreGraphe gi;
	private int mode;
	private int type_orientation = ORIENTE;
	private int type_simple = NON_SIMPLE;
	private int type_arcs = NON_VALUE;
	
	/**
	 * Constructeur de la fenêtre qui adapte le contenu selon le mode reçu en paramètre.
	 * 
	 * @param gi Fenêtre principale de gestion du graphe, utilisée pour récupérer les informations du graphe
	 * @param langue Langue de l'interface qui demande la création/édition du graphe
	 * @param mode Mode de la fenêtre, édition ou création
	 * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
	 * 
	 * @see GrapheConstantes#MODE_CREATION
	 * @see GrapheConstantes#MODE_EDITION
	 */
	public CreationGraphe(FenetreGraphe gi, int langue, int mode){
		this.langue = langue;
		this.gi = gi;
		this.mode = mode;
		
		if(mode == MODE_EDITION){
			type_orientation = gi.getGraphe().getTypeOrientation();
			type_simple = gi.getGraphe().getTypeSimple();
			type_arcs = gi.getGraphe().getTypeArcs();
		}
		
		gi.setEnabled(false);
		setTitle(LOC[LOC_CREER_GRAPHE_MENU][this.langue]);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 3*w/8, 3*h/8);
		setLocationRelativeTo(null);
		
		init();
		setVisible(true);
	}
	
	/**
	 * Créé le panel central de la fenêtre contenant tous les boutons radios pour le choix du type du graphe.
	 * 
	 * @return Panel central
	 */
	private JPanel createCenterPanel(){
		JPanel pCenter = new JPanel(new GridLayout(3, 1, 0, 0));
		JPanel pDirection = new JPanel(new GridLayout(1, 2, 0, 0));
		JPanel pLoops = new JPanel(new GridLayout(1, 2, 0, 0));
		JPanel pWeight = new JPanel(new GridLayout(1, 3, 0, 0));
		
		TitledBorder title = BorderFactory.createTitledBorder(LOC[LOC_TYPE_GRAPHE][langue]);
		TitledBorder titleDirection = BorderFactory.createTitledBorder(LOC[LOC_ORIENTATION][langue]);
		TitledBorder titleLoops = BorderFactory.createTitledBorder(LOC[LOC_SIMPLICITE][langue]);
		TitledBorder titleWeight = BorderFactory.createTitledBorder(LOC[LOC_VALUATION][langue]);
		
		ButtonGroup gDirection = new ButtonGroup();
		ButtonGroup gLoops = new ButtonGroup();
		ButtonGroup gWeight = new ButtonGroup();
		
		oriente = new JRadioButton(LOC[LOC_ORIENTE][langue]);
		non_oriente = new JRadioButton(LOC[LOC_NON_ORIENTE][langue]);
		simple = new JRadioButton(LOC[LOC_SIMPLE][langue]);
		non_simple = new JRadioButton(LOC[LOC_NON_SIMPLE][langue]);
		value = new JRadioButton(LOC[LOC_VALUE][langue]);
		etiquete = new JRadioButton(LOC[LOC_ETIQUETE][langue]);
		non_value = new JRadioButton(LOC[LOC_NON_VALUE][langue]);
		
		if(mode == MODE_CREATION){
			oriente.setSelected(true);
			non_simple.setSelected(true);
			non_value.setSelected(true);
		}
		else{
			nom.setText(gi.getGraphe().getNom());
			
			if(gi.getGraphe().getTypeOrientation() == ORIENTE)
				oriente.setSelected(true);
			else{
				non_oriente.setSelected(true);
				oriente.setEnabled(false);
				non_simple.setEnabled(false);
			}
			
			if(gi.getGraphe().getTypeSimple() == SIMPLE){
				simple.setSelected(true);
				non_simple.setEnabled(false);
			}
			else
				non_simple.setSelected(true);
			
			if(gi.getGraphe().getTypeArcs() == VALUE)
				value.setSelected(true);
			else if(gi.getGraphe().getTypeArcs() == NON_VALUE)
				non_value.setSelected(true);
			else
				etiquete.setSelected(true);
		}
		
		oriente.addActionListener(new DirectionListener(ORIENTE));
		non_oriente.addActionListener(new DirectionListener(NON_ORIENTE));
		simple.addActionListener(new LoopListener(SIMPLE));
		non_simple.addActionListener(new LoopListener(NON_SIMPLE));
		value.addActionListener(new WeightListener(VALUE));
		etiquete.addActionListener(new WeightListener(ETIQUETE));
		non_value.addActionListener(new WeightListener(NON_VALUE));
		
		pDirection.add(oriente);
		pDirection.add(non_oriente);
		gDirection.add(oriente);
		gDirection.add(non_oriente);
		pDirection.setBorder(titleDirection);
		pLoops.add(non_simple);
		pLoops.add(simple);
		gLoops.add(non_simple);
		gLoops.add(simple);
		pLoops.setBorder(titleLoops);
		pWeight.add(non_value);
		pWeight.add(value);
		pWeight.add(etiquete);
		gWeight.add(non_value);
		gWeight.add(value);
		gWeight.add(etiquete);
		pWeight.setBorder(titleWeight);
		
		pCenter.add(pDirection);
		pCenter.add(pLoops);
		pCenter.add(pWeight);
		
		pCenter.setBorder(title);
		
		return pCenter;
	}
	
	/**
	 * Créé le panel nord de la fenêtre contenant le champ texte pour le choix du nom du graphe.
	 * 
	 * @return Panel nord
	 */
	private JPanel createNorthPanel(){
		JPanel pNorth = new JPanel(new BorderLayout());
		
		TitledBorder title = BorderFactory.createTitledBorder(LOC[LOC_NOM_GRAPHE][langue]);
		
		nom = new JTextField();
		nom.setEditable(true);
		nom.addKeyListener(new NameListener());
		
		pNorth.add(nom);
		pNorth.setBorder(title);
		
		return pNorth;
	}
	
	/**
	 * Créé le panel sud de la fenêtre contenant le bouton de validation.
	 * 
	 * @return Panel sud
	 */
	private JPanel createSouthPanel(){
		JPanel pSouth = new JPanel();
		
		valider = new JButton(LOC[LOC_VALIDER][langue]);
		
		if(mode == MODE_CREATION)
			valider.setEnabled(false);
		
		valider.addActionListener(new ValidListener());
		pSouth.add(valider);
		
		return pSouth;
	}
	
	/**
	 * Initialise les composants de la fenêtre.
	 */
	private void init(){
		Container c = getContentPane();
		
		JPanel pNorth = createNorthPanel();
	    JPanel pCenter = createCenterPanel();
	    JPanel pSouth = createSouthPanel();
	    
	    c.add(pNorth, "North");
	    c.add(pCenter, "Center");
	    c.add(pSouth, "South");
	    addWindowListener(new CloseListener());
	}

	/**
	 * Listener de la fenêtre pour modifier son action à la fermeture.
	 */
	class CloseListener extends WindowAdapter{
		@Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	        gi.setEnabled(true);
	    }
	}
	
	/**
	 * Listener des boutons radios de l'orientation du graphe.
	 */
	class DirectionListener implements ActionListener{
		int direction;
		
		public DirectionListener(int direction){
			this.direction = direction;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			type_orientation = direction;
			if(type_orientation == ORIENTE)
				non_simple.setEnabled(true);
			else{
				type_simple = SIMPLE;
				simple.setSelected(true);
				non_simple.setEnabled(false);
			}
		}
	}
	
	/**
	 * Listener des boutons radios pour les boucles du graphe.
	 */
	class LoopListener implements ActionListener{
		int loop;
		
		public LoopListener(int loop){
			this.loop = loop;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			type_simple = loop;
		}
	}
	
	/**
	 * Listener du champ texte dans le panel nord.
	 */
	class NameListener extends KeyAdapter{
	
		public boolean isAlphabetic(char c){
			boolean criterion1 = (c >= 'A');
			boolean criterion2 = (c <= 'Z');
			boolean criterion3 = (c >= 'a');
			boolean criterion4 = (c <= 'z');
			
			return (criterion1 && criterion2) || (criterion3 && criterion4);
		}
		
		public boolean isAuthorized(KeyEvent e){
			boolean criterion1 = (e.getKeyChar() == '_');
			boolean criterion2 = (e.getKeyChar() == '-');
			boolean criterion3 = (e.getKeyCode() == KeyEvent.VK_BACK_SPACE);
			boolean criterion4 = (e.getKeyCode() == KeyEvent.VK_RIGHT);
			boolean criterion5 = (e.getKeyCode() == KeyEvent.VK_LEFT);
			boolean criterion6 = (e.getKeyCode() == KeyEvent.VK_DELETE);
			
			return criterion1 || criterion2 || criterion3 || criterion4 
					|| criterion5 || criterion6 || isAlphabetic(e.getKeyChar()) || isNumeric(e.getKeyChar());
		}
		
		public boolean isNumeric(char c){
			boolean criterion1 = (c >= '0');
			boolean criterion2 = (c <= '9');
			
			return (criterion1 && criterion2);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			char c = e.getKeyChar();
			String cString = String.valueOf(c);
			
			if(!isAuthorized(e)){
				String newChar = nom.getText().replace(cString, "");
				nom.setText(newChar);
			}
			
			if(nom.getText().length() > TAILLE_MAX_NOM){
				try {
					nom.setText(nom.getText(0, TAILLE_MAX_NOM));
				} 
				catch (BadLocationException e1) { }
			}
			
			if(nom.getText().length() == 0){
				valider.setEnabled(false);
			}
			else{
				valider.setEnabled(true);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			keyPressed(e);
		}
		
	}
	
	/**
	 * Listener du bouton valider.
	 */
	class ValidListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(mode == MODE_CREATION){
				gi.setGraphe(new PanelGraphe(nom.getText(), type_orientation, type_simple, type_arcs));
			}
			else{
				if(type_arcs == VALUE && gi.getGraphe().getTypeArcs()!=VALUE)
					for(Arc a: gi.getGraphe().getArcs())
						a.setDistance("0", type_orientation, type_arcs);
				gi.getGraphe().setNom(nom.getText());
				gi.getGraphe().setTypeOrientation(type_orientation);
				gi.getGraphe().setTypeSimple(type_simple);
				gi.getGraphe().setTypeArcs(type_arcs);
			}
			
			gi.getGraphe().attacherParent(gi);
			gi.setEnabled(true);
			gi.getGraphe().desactiverOptions();
			gi.refresh();
			dispose();
		}
		
	}
	
	/**
	 * Listener des boutons radios pour les valeurs des arcs du graphe.
	 */
	class WeightListener implements ActionListener{
		int weight;
		
		public WeightListener(int weight){
			this.weight = weight;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			type_arcs = weight;
		}
	}
}
