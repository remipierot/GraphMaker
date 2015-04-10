package fr.graphmaker.constantes;

import java.awt.Font;

/**
 * Interface regroupant les constantes de localisation pour le français, chinois et anglais.
 * Chaque texte utilisé dans l'application existe en trois version, une pour chaque langue.
 */
public interface LanguesConstantes {
	/**
	 * Valeur = 3, nombre de langues disponibles.
	 */
	public static final int NOMBRE_LANGUES = 3;
	
	/**
	 * Valeur = 0, identifie la langue française.
	 */
	public static final int FR = 0;
	
	/**
	 * Valeur = 1, identifie la langue chinoise.
	 */
	public static final int CH = 1;
	
	/**
	 * Valeur = 2, identifie la langue anglaise.
	 */
	public static final int EN = 2;
	
	/**
	 * Police "monospace" d'épaisseur 10.
	 */
	public static final Font FONT_MONOSPACE_10 = new Font("monospace", Font.PLAIN, 10);
	
	/**
	 * Police "monospace" d'épaisseur 12.
	 */
	public static final Font FONT_MONOSPACE_12 = new Font("monospace", Font.PLAIN, 12);
	
	/**
	 * Nombre de textes utilisés dans l'application.
	 */
	public static final int NOMBRE_TEXTES = 91;
	
	public static final int LOC_MENU_GRAPHE = 0;
	public static final int LOC_CREER_GRAPHE = 1;
	public static final int LOC_CHARGER_GRAPHE = 2;
	public static final int LOC_SAUVEGARDER_GRAPHE = 3;
	public static final int LOC_FERMER_GRAPHE = 4;
	public static final int LOC_EDITER_GRAPHE = 5;
	public static final int LOC_DESORIENTER_GRAPHE = 6;
	public static final int LOC_SIMPLIFIER_GRAPHE = 7;
	public static final int LOC_MENU_SOMMETS = 8;
	public static final int LOC_ELLIPSE = 9;
	public static final int LOC_RECTANGLE = 10;
	public static final int LOC_ROUND_RECTANGLE = 11;
	public static final int LOC_LOSANGE = 12;
	public static final int LOC_MENU_ARCS = 13;
	public static final int LOC_MENU_ARETES = 14;
	public static final int LOC_ARC_SIMPLE_PLEIN = 15;
	public static final int LOC_ARC_SIMPLE_POINTILLE = 16;
	public static final int LOC_ARC_DOUBLE_PLEIN = 17;
	public static final int LOC_ARC_DOUBLE_POINTILLE = 18;
	public static final int LOC_VALEUR_SOMMET_POPUP = 19;
	public static final int LOC_SUPPRIMER_SOMMET_POPUP = 20;
	public static final int LOC_FORME_SOMMET_POPUP = 21;
	public static final int LOC_VALEUR_ARC_POPUP = 22;
	public static final int LOC_TEXTE_ARC_POPUP = 23;
	public static final int LOC_SUPPRIMER_ARC_POPUP = 24;
	public static final int LOC_STYLE_ARC_POPUP = 25;
	public static final int LOC_MENU_LANGUE = 26;
	public static final int LOC_FRANCAIS = 27;
	public static final int LOC_CHINOIS = 28;
	public static final int LOC_ANGLAIS= 29;
	public static final int LOC_SAUVEGARDE_REUSSIE = 30;
	public static final int LOC_SAUVEGARDE_ECHOUEE = 31;
	public static final int LOC_CREER_GRAPHE_MENU = 32;
	public static final int LOC_NOM_GRAPHE = 33;
	public static final int LOC_TYPE_GRAPHE = 34;
	public static final int LOC_ORIENTATION = 35;
	public static final int LOC_SIMPLICITE = 36;
	public static final int LOC_VALUATION = 37;
	public static final int LOC_ORIENTE = 38;
	public static final int LOC_NON_ORIENTE = 39;
	public static final int LOC_SIMPLE = 40;
	public static final int LOC_NON_SIMPLE = 41;
	public static final int LOC_VALUE = 42;
	public static final int LOC_ETIQUETE = 43;
	public static final int LOC_NON_VALUE = 44;
	public static final int LOC_VALIDER = 45;
	public static final int LOC_CHROMATIQUE = 46;
	public static final int LOC_DEPLACER_S_ARCS = 47;
	public static final int LOC_DEPLACER_S_ARETES = 48;
	public static final int LOC_CREER_SOMMET = 49;
	public static final int LOC_CREER_ARC = 50;
	public static final int LOC_CREER_ARETE = 51;
	public static final int LOC_EFFACER = 52;
	public static final int LOC_REINITIALISER_ORIENTE = 53;
	public static final int LOC_REINITIALISER_NON_ORIENTE = 54;
	public static final int LOC_CLIQUE = 55;
	public static final int LOC_DEPLACER_FLECHE = 56;
	public static final int LOC_DEPLACER_GRAPHE = 57;
	public static final int LOC_ALGORITHMES = 58;
	public static final int LOC_ALGO_DJIKSTRA = 59;
	public static final int LOC_ALGO_BRELAZ = 60;
	public static final int LOC_ALGO_MARQUAGE = 61;
	public static final int LOC_ERREUR_VALUE = 62;
	public static final int LOC_ERREUR = 63;
	public static final int LOC_INFOS_GRAPHE = 64;
	public static final int LOC_INFOS_ORIENTATION = 65;
	public static final int LOC_INFOS_VALUATION = 66;
	public static final int LOC_INFOS_BOUCLES = 67;
	public static final int LOC_INFOS_SOMMETS = 68;
	public static final int LOC_INFOS_ARCS = 69;
	public static final int LOC_INFOS_ARETES = 70;
	public static final int LOC_INFOS_CONNEXE = 71;
	public static final int LOC_INFOS_OUI = 72;
	public static final int LOC_INFOS_NON = 73;
	public static final int LOC_VIDER = 74;
	public static final int LOC_CHAINE = 75;
	public static final int LOC_CYCLE = 76;
	public static final int LOC_SAGITTALE = 77;
	public static final int LOC_MATRICIELLE = 78;
	public static final int LOC_SET_MARQUAGE = 79;
    public static final int LOC_CONVERSION_LATEX = 80;
    public static final int LOC_CHARGEMENT_ECHOUE = 81;
    public static final int LOC_EXPORTATION_REUSSIE = 82;
    public static final int LOC_EXPORTATION_ECHOUEE = 83;
    public static final int LOC_CONFIRM_DESORIENTER = 84;
    public static final int LOC_CONFIRM_SIMPLIFIER = 85;
    public static final int LOC_CONFIRM_VIDER = 86;
    public static final int LOC_CONFIRM_FERMER = 87;
    public static final int LOC_POPUP_DJIKSTRA = 88;
    public static final int LOC_POPUP_DEUX_DJIKSTRA = 89;
    public static final int LOC_DEMANDE_SELECTION_DJIKSTRA = 90;
	
    /**
     * Tableau contenant tous les textes.
     * Les lignes correspondent au texte et les colonnes à la langue.
     * Par exemple, pour accéder au texte "Graphe" de la barre de menu en francais, 
     * il faut utiliser LOC[LOC_MENU_GRAPHE][FR]. Si on le veut en chinois, ce sera LOC[LOC_MENU_GRAPHE][CH].
     */
	public static final String[][] LOC = {
		{"Graphe", "图论", "Graph"},
		{"Créer", "新建", "New"},
		{"Charger", "打开", "Load"},
		{"Sauvegarder", "保存", "Save"},
		{"Fermer", "关闭", "Close"},
		{"Editer", "修改", "Edit"},
		{"Désorienter", "转为无向图", "Undirect the graph"},
		{"Simplifier (supprimer les boucles)", "简化(除掉自环)", "Simplify (disable loops)"},
		{"Forme des Sommets", "顶点形状", "Nodes shape"},
		{"Ellipse", "椭圆", "Ellipse"},
		{"Rectangle", "矩形", "Rectangle"},
		{"Rectangle arrondi", "圆角矩形", "Round rectangle"},
		{"Losange", "菱形", "Diamond"},
		{"Style des arcs", "连接样式", "Edges style"},
		{"Style des arêtes", "连接样式", "Edges style"},
		{"Trait simple plein", "实线", "Simple full edge"},
		{"Trait simple à pointillés", "虚线", "Simple dashed edge"},
		{"Trait double plein", "双实线", "Double full edge"},
		{"Trait double à pointillés", "双虚线", "Double dashed edge"},
		{"Changer la valeur", "更改距离", "Change value"},
		{"Supprimer", "删除", "Delete"},
		{"Changer la forme", "修改形状", "Change shape"},
		{"Changer la valeur", "更改距离", "Change value"},
		{"Changer le texte", "编辑文字", "Change text"},
		{"Supprimer", "删除", "Delete"},
		{"Changer le tracé", "更改连接样式", "Change style"},
		{"Langue", "语言", "Language"},
		{"Français", "法文", "French"},
		{"Chinois", "中文", "Chinese"},
		{"Anglais", "英文", "English"},
		{"Sauvegarde réussie", "保存成功", "Save successful"},
		{"Erreur : impossible de sauvegarder", "错误 : 无法保存", "Error : unable to save"},
		{"Créer un graphe", "新图", "New graph"},
		{"Nom du graphe", "图名", "Graph name"},
		{"Type du graphe", "图的种类", "Graph type"},
		{"Orientation des arcs/arêtes", "有向图/无向图", "Edges direction"},
		{"Boucles", "自环", "Loops"},
		{"Valuation des arcs/arêtes", "连接之间的距离", "Edges weight"},
		{"Orienté", "有向图", "Directed"},
		{"Non-orienté", "无向图", "Undirected"},
		{"Sans boucles (Simple)", "无自环 (简单) ", "Without loops (Simple)"},
		{"Avec boucles", "自环", "With loops"},
		{"Valué (Valeur numérique)", "数字距离", "Weighted (Numeric value)"},
		{"Etiqueté (Valeur textuelle)", "文字距离", "Labelled (Text value)"},
		{"Basique (Sans valeur)", "无距离", "Basic (Without value)"},
		{"Valider", "确定", "Confirm"},
		{"Le nombre chromatique est majoré par : ", "点色数的上限为 :", "The chromatic number can't be higher than : "},
		{"Déplacer (sommets et arcs)", "移动 (顶点和链接)", "Move (Nodes and edges)"},
		{"Déplacer (sommets et arêtes)", "移动 (顶点和链接)", "Move (Nodes and edges)"},
		{"Créer Sommet", "顶点", "New node"},
		{"Créer Arc", "连接(弧)", "New edge"},
		{"Créer Arête", "连接", "New edge"},
		{"Effacer", "删除", "Erase"},
		{"Réinitialiser (courbes des arcs)", "重置连接线", "Reset (edges curves)"},
		{"Réinitialiser (courbes des arêtes)", "重置连接线", "Reset (edges curves)"},
		{"Clique maximale", "极大团", "Maximal clique"},
		{"Déplacer Flèche", "移动箭头", "Move arrows"},
		{"Déplacer Graphe", "移动图", "Move graph"},
		{"Algorithmes", "算法", "Algorithms"},
		{"Djikstra (chemin le plus court)", "迪克斯特拉 (最短路径)", "Djikstra (shortest path)"},
		{"Brélaz (coloration)", "Brelaz 算法 (着色)", "Brelaz (coloring)"},
		{"Marquage (successeurs/prédécesseurs)", "Marking (descendants/ancestors)", "Marking (descendants/ancestors)"}, 
		{"Impossible d'affecter un texte à un arc valué.", "无法更改为文字", "You cannot put a non-numeric value into a weighted edge."},
		{"Erreur", "错误", "Error"},
		{" Informations du graphe ", " 图的信息 ", " Graph informations"},
		{"  Orientation : ", "  有向图/无向图 : ", "  Direction : "},
		{"  Valuation : ", "  连接之间的距离 : ", "  Valuation : "},
		{"  Boucles : ", "  自环 : ", "  Loops : "},
		{"Nombre de sommets : ", "顶点数量 : ", "Number of nodes : "},
		{"Nombre d'arcs : ", "连接数量 : ", "Number of edges : "},
		{"Nombre d'arêtes : ", "连接数量 : ", "Number of edges : "},
		{"Connexe : ", "连通性 : ", "Connected : "},
		{"Oui", "是", "Yes"},
		{"Non", "否", "No"},
		{"Vider le graphe",	"一键删除", "Clear the graph"},
		{"Chaîne maximale", "最长路径", "Maximal path"},
		{"Cycle maximal", "最长回路", "Maximal cycle"},
		{" Représentation sagittale", " 图像", " Graph"},
		{" Représentation matricielle", " 矩阵", " Matrix"},
		{"Niveau du marquage : ", "Marking level : ", "Marking level : "},
		{"Exportation en LaTeX", "转为LaTeX", "Export to LaTeX"},
        {"Erreur : échec du chargement.\nLe fichier demandé est invalide (inexistant ou au mauvais format).", "错误 : 加载失败.\n 文件不存在或格式错误.\n", "Error : fail loading.\nThe file is not valid (either it does not exist or it has a wrong format)."},
        {"Exportation réussie", "转换成功", "Export successful"},
        {"Erreur : échec de l'exportation", "错误 : 转换失败", "Error : unable to export"},
        {"Ceci rendra le graphe non-orienté et il sera impossible de revenir en arrière.\nÊtes-vous sûr de vouloir désorienter le graphe ?", "一旦转为无向图，就无法撤销.\n您是否确定转为无向图?", 	"The graph will become undirected and it will be irreversible.\nAre you sure you want to undirect the graph ?"},
        {"Ceci empêchera toute création de boucle et il sera impossible de revenir en arrière.\nÊtes-vous sûr de vouloir simplifier le graphe ?", "一旦禁止了自环，就无法撤销.\n您是否确定要简化(除掉自环)?", "This will prevent you from adding loops to the graph and it will be irreversible.\nAre you sure you want to simplify the graph ?"},
        {"Ceci supprimera tous les éléments du graphe.\nÊtes-vous sûr de vouloir vider le graphe ?", "内容将会全部删除.\n您是否确定全部清除?", "This will destroy every element (nodes and edges) of the graph.\nAre you sure you want to clear the graph ?"},
        {"Ceci fermera le graphe courant, tout changement non sauvegardé sera perdu.\nÊtes-vous sûr de vouloir fermer le graphe ?", "一旦关闭, 就无法恢复没有备份的内容.\n您是否确定关闭此图?", "This will close the current graph without saving it.\nAre you sure you want to close the graph ?"},
        {"Sélectionnez deux sommets afin d'afficher le chemin le plus court reliant les deux.", "请选择最短路径的两个顶点", "Choose two nodes to show the shortest path between them."},
        {"Encore un sommet à sélectionner.", "请再选择一个顶点", "One more node to choose."},
        {"Sélectionnez deux autres sommets si vous souhaitez relancer l'algorithme.", "如果想再运行算法 请选择其他两个顶点", "Choose another pair of nodes if you want to re-run the algorithm."}
	};
	
}
