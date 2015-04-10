package fr.graphmaker.outils;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import fr.graphmaker.constantes.LanguesConstantes;
import fr.graphmaker.constantes.SauvegardeConstantes;
import fr.graphmaker.gui.PanelGraphe;
import fr.graphmaker.gui.PanelMatrice;

/**
 * Classe gérant la sauvegarde et le chargement.
 * La gestion se fait par des methodes de classe pour ne pas avoir à instancier d'objets.
 * 
 */
public class GestionnaireFichier implements SauvegardeConstantes,LanguesConstantes{
	/**
	 * Créé une image du graphe au format PNG à l'emplacement reçu en paramètre.
	 * 
	 * @param g Graphe dont on veut une image (au format PNG)
	 * @param fileName Emplacement du fichier de sortie
	 * @return Chaîne de caractères correspondant au chemin du fichier de sortie
	 */
	public static String buildGrapheImage(PanelGraphe g, String fileName){
		BufferedImage bi = new BufferedImage(g.getMaxX()+g.getMinX(), g.getMaxY()+g.getMinY(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g.paint(g2d);
		g2d.dispose();
		try{
			ImageIO.write(bi, "png", new File(fileName+".png"));
		}catch (Exception e) { }
		
		return fileName+".png";
	}
	
	/**
	 * Exporte le graphe correspondant à la matrice reçue en paramètre dans un fichier choisi par l'utilisateur.
	 * L'exportation se fait dans un fichier LaTeX au format .tex
	 * 
	 * @param m PanelMatrice du graphe à exporter
	 * @param langue Langue de l'interface qui demande la sauvegarde du graphe
	 * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
	 */
	public static void exportationLatex(PanelMatrice m, int langue){
		FileDialog nav = new FileDialog(new Frame(), LOC[LOC_CONVERSION_LATEX][langue], FileDialog.SAVE);
		nav.setFile(m.getGraphe().getNom()+".tex");
		nav.setVisible(true);
		
		if(nav.getFile()!=null){
			String fileName = nav.getDirectory()+nav.getFile().replaceAll("\\..*", "");
	    	File f_save = new File(fileName+".tex"); 
			try {
				f_save.createNewFile();
				FileWriter w_save = new FileWriter(f_save);
				try {
					w_save.write(m.conversionLatex(fileName));
					JOptionPane.showMessageDialog(null, LOC[LOC_EXPORTATION_REUSSIE][langue]);
				} finally {
					w_save.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, LOC[LOC_EXPORTATION_ECHOUEE][langue], LOC[LOC_ERREUR][langue], JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Renvoie un objet correspondant au graphe à charger choisi par l'utilisateur.
	 * 
	 * @param langue Langue de l'interface qui va recevoir le graphe après le chargement
	 * @return PanelGraphe dans lequel est chargé le fichier choisi par l'utilisateur
	 * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
	 */
	public static PanelGraphe load(int langue){
		PanelGraphe g = null;
		FileDialog nav = new FileDialog(new Frame(), LOC[LOC_CHARGER_GRAPHE][langue], FileDialog.LOAD);
		nav.setFile("*"+SAVE_FORMAT+";*"+OTHER_SAVE_FORMAT);
		nav.setVisible(true);
		
		String fileName = nav.getDirectory()+nav.getFile();
		
		if(nav.getFile()!=null  && isValidFormat(fileName)){
			BufferedReader br = null;

			try {
				br = new BufferedReader(new FileReader(fileName));
			} catch (FileNotFoundException e) { }
			
		    try {
		        String line = "", splitter = ",";
				String[] tmp_datas;
				int num_line = 0, infos_arcs = 0;
		        try {
					while ((line = br.readLine()) != null) {
						tmp_datas = line.split(splitter);
						if(num_line == INFOS_GRAPHE){
							g = new PanelGraphe(tmp_datas[NOM_GRAPHE], 
											Integer.parseInt(tmp_datas[TYPE_ORIENTATION_GRAPHE]), 
											Integer.parseInt(tmp_datas[TYPE_SIMPLE_GRAPHE]), 
											Integer.parseInt(tmp_datas[TYPE_ARCS_GRAPHE])
											);
							infos_arcs = Integer.parseInt(tmp_datas[NOMBRE_SOMMETS])+1;
						}
						else if(num_line >= INFOS_SOMMETS && num_line < infos_arcs)
							g.addSommet(tmp_datas);
						else
							g.addArc(tmp_datas);
						
						num_line++;
					}
				} catch (Exception e) { 
					JOptionPane.showMessageDialog(null, LOC[LOC_CHARGEMENT_ECHOUE][langue], LOC[LOC_ERREUR][langue], JOptionPane.ERROR_MESSAGE); 
				}
		    } finally {
		        try {
					br.close();
				} catch (IOException e) { } 
		    }
		}
		else if(nav.getFile()!=null && !isValidFormat(fileName))
			JOptionPane.showMessageDialog(null, LOC[LOC_CHARGEMENT_ECHOUE][langue], LOC[LOC_ERREUR][langue], JOptionPane.ERROR_MESSAGE);
		return g;
	}
	
	/**
	 * Sauvegarde le graphe reçu en paramètre dans un fichier choisi par l'utilisateur.
	 * 
	 * @param g Graphe à sauvegarder
	 * @param langue Langue de l'interface qui demande la sauvegarde du graphe
	 * 
	 * @see LanguesConstantes#FR
	 * @see LanguesConstantes#CH
	 * @see LanguesConstantes#EN
	 */
	public static void save(PanelGraphe g, int langue){
		FileDialog nav = new FileDialog(new Frame(), LOC[LOC_SAUVEGARDER_GRAPHE][langue], FileDialog.SAVE);
		nav.setFile(g.getNom()+SAVE_FORMAT);
		nav.setVisible(true);
		
		if(nav.getFile()!=null){
			String fileName = nav.getDirectory()+nav.getFile().replaceAll("\\..*", "");
	    	File f_save = new File(fileName+SAVE_FORMAT); 
			try {
				f_save.createNewFile();
				FileWriter w_save = new FileWriter(f_save);
				try {
					w_save.write(""+g);
					JOptionPane.showMessageDialog(null, LOC[LOC_SAUVEGARDE_REUSSIE][langue]);
				} finally {
					w_save.close();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, LOC[LOC_SAUVEGARDE_ECHOUEE][langue], LOC[LOC_ERREUR][langue], JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Vérifie la validité du format de sauvegarde.
	 * 
	 * @param fileName Nom du fichier à enregistrer
	 * @return Validité du nom de fichier
	 * 
	 * @see SauvegardeConstantes#SAVE_FORMAT
	 * @see SauvegardeConstantes#OTHER_SAVE_FORMAT
	 */
	private static boolean isValidFormat(String fileName){
		return fileName.endsWith(SAVE_FORMAT) || fileName.endsWith(OTHER_SAVE_FORMAT);
	}
}
