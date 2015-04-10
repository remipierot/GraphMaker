package fr.graphmaker.gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.graphmaker.algorithmes.AlgoBrelaz;
import fr.graphmaker.constantes.CouleursConstantes;
import fr.graphmaker.constantes.GrapheConstantes;
import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.outils.BoiteAOutil;
import fr.graphmaker.outils.GestionnaireFichier;
import fr.graphmaker.outils.Sommet;

/**
 * Classe gérant la matrice d'un graphe.
 * C'est un JPanel à intégrer dans une fenêtre et à rattacher à un graphe pour en obtenir une représentation graphique.
 */
public class PanelMatrice extends JPanel implements LanguesConstantes, CouleursConstantes, GrapheConstantes{
	private static final long serialVersionUID = 6240010759171878898L;
	private PanelGraphe g;
	
	/**
	 * Initialise le graphe à null.
	 */
	public PanelMatrice(){
		this(null);
	}
	
	/**
	 * Initialise le panel avec le graphe reçu en paramètre.
	 * 
	 * @param g Graphe à rattacher au panel
	 */
	public PanelMatrice(PanelGraphe g){
		super();
		this.g = g;
	}
	
	/**
	 * Renvoie une chaîne de caractères correspondant au formattage LaTeX du graphe et de ses informations.
	 * 
	 * @param fileName Chemin du fichier final ou le code LaTeX sera exporté
	 * 
	 * @return Chaîne de caractères correspondant au formattage LaTeX
	 */
	public String conversionLatex(String fileName){
		String s = "";
		if(g != null && !g.getSommets().isEmpty()){
			ArrayList<Sommet> sommets = BoiteAOutil.triCopieListe(g.getSommets());
			int[][] matrice = BoiteAOutil.matrice(sommets);
			int longueur = matrice[0].length+1, tmp_longueur = 0, ecart = ECART_BASIQUE_LATEX, nb_split = 0;
			String img = GestionnaireFichier.buildGrapheImage(g, fileName).replaceAll(".*\\\\", "");
			String img_brelaz = "";
			if(g.getTypeOrientation() == NON_ORIENTE){
				g.activerOption(BRELAZ, FR);
				img_brelaz = GestionnaireFichier.buildGrapheImage(g, fileName+"_brelaz").replaceAll(".*\\\\", "");
				g.desactiverOptions();
			}
			String nom_graphe = g.getNom().replaceAll("_", "\\\\_");
			
			s += "\\documentclass[a4paper, 12pt]{report}\n";
			s += "\\renewcommand{\\thesection}{\\arabic{section}}\n\n";
			s += "\\usepackage[utf8]{inputenc}\n";
			s += "\\usepackage[T1]{fontenc}\n";
			s += "\\usepackage[sfdefault]{quattrocento}\n";
			s += "\\usepackage[francais]{babel}\n";
			s += "\\usepackage{graphicx}\n";
			s += "\\usepackage{amsmath}\n";
			s += "\\usepackage{amssymb}\n";
			s += "\\usepackage{geometry}\n";
			s += "\\usepackage{slashbox}\n";
			s += "\\usepackage{booktabs}\n";
			s += "\\usepackage{hyperref}\n";
			s += "\\usepackage[table]{xcolor}\n";
			s += "\\hypersetup{ colorlinks=true, linkcolor=black, urlcolor=blue }\n";
			s += "\\geometry{hmargin=1.5cm,vmargin=1.5cm}\n\n";
			s += "\\definecolor{matriceInfo}{HTML}{66B0FF}\n";
			s += "\\definecolor{matriceZero}{HTML}{EFF2FB}\n";
			s += "\\definecolor{matriceUn}{HTML}{CED8F6}\n";
			s += "\\title{\\bfseries{GraphMaker : \\textit{"+nom_graphe+"}}}";
			s += "\\begin{document}\n\n";
			s += "\\maketitle\n";
			s += "\\tableofcontents\n\n";
			s += "\\chapter{Informations générales}\n";
			s += "\\begin{center}\n\n";
			s += "\\begin{tabular}{|l|l|}\n";
			s += "\\hline\n";
			s += "Orientation du graphe & ";
			if(g.getTypeOrientation() == ORIENTE)
				s += "orienté \\\\\n";
			else
				s += "non-orienté \\\\\n";
			s += "\\hline\n";
			s += "Type de valeurs des ";
			if(g.getTypeOrientation() == ORIENTE)
				s += "arcs & ";
			else
				s += "arêtes & ";
			if(g.getTypeArcs() == NON_VALUE)
				s += "sans valeurs \\\\\n";
			else if(g.getTypeArcs() == VALUE)
				s += "valeurs numériques \\\\\n";
			else
				s += "valeurs textuelles \\\\\n";
			s += "\\hline\n";
			s += "Boucles autorisées & ";
			if(g.getTypeSimple() == SIMPLE)
				s += "non \\\\\n";
			else
				s += "oui \\\\\n";
			s += "\\hline\n";
			s += "Nombre de sommets & " + g.getSommets().size() +" \\\\\n";
			s += "\\hline\n";
			s += "Nombre d'";
			if(g.getTypeOrientation() == ORIENTE)
				s += "arcs & " + g.getArcs().size() + " \\\\\n";
			else
				s += "arêtes & " + (g.getArcs().size()/2) + " \\\\\n";
			s += "\\hline\n";
			s += "Connexe & ";
			if(g.estConnexe())
				s += "oui \\\\\n";
			else
				s += "non \\\\\n";
			s += "\\hline\n";
			
			if(g.getTypeOrientation() == NON_ORIENTE){
				s += "Nombre chromatique & "+AlgoBrelaz.getNbChromatique()+"\\\\\n";
				s += "\\hline\n";
			}
			
			s += "\\end{tabular}\n\n";
			s += "\\end{center}\n\n";
			s += "\\chapter{Représentations}\n";
			s += "\\section{Représentation sagittale}\n";
			s += "\\begin{center}\n\n";
			s += "\\begin{figure}[h!]\n";
			s += "\\hbox to\\hsize{"
					+ "\\hss\\fbox{"
					+ "\\includegraphics["
					+ "trim = "+BoiteAOutil.pxToMm(g.getMinX())+"mm "
					+BoiteAOutil.pxToMm(g.getMinY())+"mm "
					+BoiteAOutil.pxToMm(g.getMinX())+"mm "
					+BoiteAOutil.pxToMm(g.getMinY())+"mm, "
					+ "clip, width=\\textwidth, height=0.65\\textheight, keepaspectratio"
					+ "]{"+ img +"}"
					+ "}\\hss}\n";
			s += "\\end{figure}\n\n";
			s += "Figure 1 - \\textit{Représentation sagittale}";
			s += "\\end{center}\n\n";
			s += "\\section{Représentation matricielle}\n";
			s += "\\subsection{Avec couleurs}\n";
			s += "\\begin{center}\n\n";
			do{
				if(tmp_longueur+ecart > longueur)
					ecart = longueur - tmp_longueur + nb_split;
				s += "\\small\n";
				s += "\\begin{tabular}{*{"+ecart+"}{>{\\columncolor{matriceZero}}c}}\n";
				s += "\\toprule\n";
				for(int i=0; i<longueur; i++){
					if(i==0)
						s += "\\rowcolor{matriceInfo} ";
			   		for(int j=tmp_longueur; j<(tmp_longueur+ecart); j++){
			   			if(j == tmp_longueur)
			   				s += "\\cellcolor{matriceInfo}";
			   			if(i==0 && j==tmp_longueur)
			   				s += "\\backslashbox{Départ}{Arrivée}";
			   			if(i==0 && j>tmp_longueur)
			   				s += "\\rotatebox{90}{"+sommets.get(j-1-nb_split).getValeur()+"\\ }";
			   			else if(i>0 && j==tmp_longueur)
			   				s += sommets.get(i-1).getValeur();
			   			else if(i>0 && j>tmp_longueur){
			   				if(matrice[i-1][j-1-nb_split] == 1)
			   					s += "\\cellcolor{matriceUn}";
			   				s += matrice[i-1][j-1-nb_split];
			   			}
			   			
			   			if(j<(tmp_longueur+ecart-1)){
		   					s += " & ";
			   			}
		   				else
		   					s += " \\\\\n";
			   		}
			   		if(i == (longueur-1))
			   			s += "\\bottomrule\n";
			   	}
				s += "\\end{tabular}\n\n";
				s += "\\normalsize\n";
				s += "\\vspace{1\\baselineskip}\n";
				s += "Figure "+(2+nb_split)+".1 - \\textit{Représentation matricielle";
				
				if((tmp_longueur+ecart) != longueur && nb_split==0)
					s += " (début)";
				else if((tmp_longueur+ecart) < longueur && nb_split > 0)
					s += " (suite)";
				else if((tmp_longueur+ecart) >= longueur && nb_split > 0)
					s += " (fin)";
				
				s += "}\n";
				
				if(longueur >= MAX_LIGNES_LATEX)
					s += "\\newpage\n";
				
				tmp_longueur += ecart;
				nb_split++;
			}while(tmp_longueur < longueur);
			
			s += "\\end{center}\n\n";
			
			tmp_longueur = 0;
			ecart = ECART_BASIQUE_LATEX;
			nb_split = 0;
			s += "\\subsection{Sans couleurs}\n";
			s += "\\begin{center}\n\n";
			do{
				if(tmp_longueur+ecart > longueur)
					ecart = longueur - tmp_longueur + nb_split;
				s += "\\small\n";
				s += "\\begin{tabular}{|*{"+ecart+"}{c|}}\n";
				s += "\\hline\n";
				for(int i=0; i<longueur; i++){
			   		for(int j=tmp_longueur; j<(tmp_longueur+ecart); j++){
			   			if(i==0 && j==tmp_longueur)
			   				s += "\\backslashbox{Départ}{Arrivée}";
			   			if(i==0 && j>tmp_longueur)
			   				s += "\\rotatebox{90}{"+sommets.get(j-1-nb_split).getValeur()+"\\ }";
			   			else if(i>0 && j==tmp_longueur)
			   				s += sommets.get(i-1).getValeur();
			   			else if(i>0 && j>tmp_longueur)
			   				s += matrice[i-1][j-1-nb_split];
			   			
			   			if(j<(tmp_longueur+ecart-1)){
		   					s += " & ";
			   			}
		   				else
		   					s += " \\\\\n";
			   		}
			   		s += "\\hline\n";
			   	}
				s += "\\end{tabular}\n\n";
				s += "\\normalsize\n";
				s += "\\vspace{1\\baselineskip}\n";
				s += "Figure "+(2+nb_split)+".2 - \\textit{Représentation matricielle";
				
				if((tmp_longueur+ecart) != longueur && nb_split==0)
					s += " (début)";
				else if((tmp_longueur+ecart) < longueur && nb_split > 0)
					s += " (suite)";
				else if((tmp_longueur+ecart) >= longueur && nb_split > 0)
					s += " (fin)";
				
				s += "}\n";
				
				if(longueur >= MAX_LIGNES_LATEX)
					s += "\\newpage\n";
				
				tmp_longueur += ecart;
				nb_split++;
			}while(tmp_longueur < longueur);
			
			s += "\\end{center}\n\n";
			if(g.getTypeOrientation() == NON_ORIENTE){
				s += "\\chapter{Algorithmes}\n";
				s += "\\section{Algorithme de coloration (Brélaz)}\n";
				s += "\\begin{center}\n\n";
				s += "\\begin{figure}[h!]\n";
				s += "\\hbox to\\hsize{"
						+ "\\hss\\fbox{"
						+ "\\includegraphics["
						+ "trim = "+BoiteAOutil.pxToMm(g.getMinX())+"mm "
						+BoiteAOutil.pxToMm(g.getMinY())+"mm "
						+BoiteAOutil.pxToMm(g.getMinX())+"mm "
						+BoiteAOutil.pxToMm(g.getMinY())+"mm, "
						+ "clip, width=\\textwidth, height=0.65\\textheight, keepaspectratio"
						+ "]{"+ img_brelaz +"}"
						+ "}\\hss}\n";
				s += "\\end{figure}\n\n";
				s += "Figure "+(2+nb_split)+" - \\textit{Graphe coloré, X(G) <= "+AlgoBrelaz.getNbChromatique()+"}";
				s += "\\end{center}\n\n";
			}
			
			s += "\\end{document}";
		}
		
		return s;
	}
	
	/**
	 * Renvoie le graphe rattaché au panel.
	 * 
	 * @return Graphe attaché au panel
	 */
	public PanelGraphe getGraphe(){
		return g;
	}
	
	/**
	 * Met à jour l'affichage du panel.
	 */
	public void refresh(){
		removeAll();
		if(g!=null && !g.getSommets().isEmpty()){
			ArrayList<Sommet> sommets = BoiteAOutil.triCopieListe(g.getSommets());
			int[][] matrice = BoiteAOutil.matrice(sommets);
			int longueur = g.getSommets().size()+3;
			JLabel tmp;
			
		   	setBackground(COL_CENTRE);
		   	setLayout(new GridLayout(longueur, longueur, 1, 1));
		   	setFont(FONT_MONOSPACE_12);
		   	setAlignmentY(CENTER_ALIGNMENT);
		   	
		   	if(g.getSommets().size() != 0){
			   	for(int i=0; i<longueur; i++){
			   		for(int j=0; j<longueur; j++){
			   			if(i==0 || j==0 || (i==1 && j==1) || i==longueur-1 || j==longueur-1)
			   				tmp = new JLabel("", JLabel.CENTER);
			   			else if(i==1 && j>1){
			   				tmp = new JLabel(sommets.get(j-2).getValeur(), JLabel.CENTER);
			   				tmp.setBackground(COL_MATRICE_INFO);
			   			}
			   			else if(j==1 && i>1){
			   				tmp = new JLabel(sommets.get(i-2).getValeur(), JLabel.CENTER);
			   				tmp.setBackground(COL_MATRICE_INFO);
			   			}
			   			else{
			   				tmp = new JLabel(""+matrice[i-2][j-2], JLabel.CENTER);
			   				tmp.setBackground(COL_MATRICE_UN);
			   				if(matrice[i-2][j-2] == 0){
			   					tmp.setEnabled(false);
			   					tmp.setBackground(COL_MATRICE_ZERO);
			   				}
			   				tmp.setToolTipText("<html>"+sommets.get(i-2).getValeur()+"&rarr;"+sommets.get(j-2).getValeur()+"</html>");
			   			}
			   			
			   			if(!tmp.getText().equals(""))
			   				tmp.setOpaque(true);
			   			
			   			add(tmp);
			   		}
			   	}
		   	}
		}
	}
	
	/**
	 * Modifie le graphe rattaché au panel.
	 * 
	 * @param g Nouveau graphe
	 */
	public void setGraphe(PanelGraphe g){
		this.g = g;
	}
	
}
