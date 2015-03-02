import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * Gestion de la fen�tre d'affichage du prochain cours sur l'emploi du temps
 */
public class Fenetre extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Fenetre(AffichageVocale aff, String type){
		super();
		
		build(aff,type);//On initialise notre fen�tre
	}
	
	private void build(AffichageVocale aff, String type){
		setTitle("Fen�tre qui affiche du texte"); //On donne un titre � l'application
		setSize(640,480); //On donne une taille � notre fen�tre
		setLocationRelativeTo(null); //On centre la fen�tre sur l'�cran
		setResizable(true); //On permet le redimensionnement
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit � l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane(aff,type));
	}
	
	private JPanel buildContentPane(AffichageVocale aff, String type){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel label1 = null;
		
		if(aff.getFin()==0)
		{
			if(type=="eleve")
			{
				label1 = new JLabel("<html>Cours : "+ aff.getCours() + "<br>Salle : " + aff.getSalle() + "<br>Enseignant : " + aff.getEnseignant() + "<br>Horaire : " + aff.getHoraire() + "</html>");
			}
			else
			{
				label1 = new JLabel("<html>Cours : "+ aff.getCours() + "<br>Salle : " + aff.getSalle() + "<br>Classe : " + aff.getEnseignant() + "<br>Horaire : " + aff.getHoraire() + "</html>");
			}
			
		}
		else //fin == 1
		{
			label1 = new JLabel("<html>Plus de cours aujourd'hui! A bient�t!</html>");
		}

		//JLabel label2 = new JLabel("Bonjour Zhangmeng, vous avez cours de (CAR) Applications r�parties en salle 052 avec DECHAMBOUX BOYER Fabienne.\n");
		//JLabel label3 = new JLabel("Le cours se d�roulera de 8h � 9h30.\n");
		//JLabel label4 = new JLabel("Je vous souhaite une excellente journ�e!\n");
		
		JLabel image = new JLabel(new ImageIcon("yeti3.jpg"));
		
		Font font = new Font("Arial",Font.BOLD,18);
		label1.setFont(font);
		
		//Bonjour Zhangmeng, vous avez cours de (CAR) Applications r�parties en salle 052 avec DECHAMBOUX BOYER Fabienne.
		//Le cours se d�roulera de 8h � 9h30.
		//Je vous souhaite une excellente journ�e!
		
		panel.add(label1,BorderLayout.PAGE_START);
		panel.add(image,BorderLayout.PAGE_END);
		panel.setBackground(Color.white);
		
		return panel;
	}
}