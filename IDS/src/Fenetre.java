import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * Gestion de la fenêtre d'affichage du prochain cours sur l'emploi du temps
 */
public class Fenetre extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Fenetre(AffichageVocale aff, String type){
		super();
		
		build(aff,type);//On initialise notre fenêtre
	}
	
	private void build(AffichageVocale aff, String type){
		setTitle("Fenêtre qui affiche du texte"); //On donne un titre à l'application
		setSize(640,480); //On donne une taille à notre fenêtre
		setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
		setResizable(true); //On permet le redimensionnement
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
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
			label1 = new JLabel("<html>Plus de cours aujourd'hui! A bientôt!</html>");
		}

		//JLabel label2 = new JLabel("Bonjour Zhangmeng, vous avez cours de (CAR) Applications réparties en salle 052 avec DECHAMBOUX BOYER Fabienne.\n");
		//JLabel label3 = new JLabel("Le cours se déroulera de 8h à 9h30.\n");
		//JLabel label4 = new JLabel("Je vous souhaite une excellente journée!\n");
		
		JLabel image = new JLabel(new ImageIcon("yeti3.jpg"));
		
		Font font = new Font("Arial",Font.BOLD,18);
		label1.setFont(font);
		
		//Bonjour Zhangmeng, vous avez cours de (CAR) Applications réparties en salle 052 avec DECHAMBOUX BOYER Fabienne.
		//Le cours se déroulera de 8h à 9h30.
		//Je vous souhaite une excellente journée!
		
		panel.add(label1,BorderLayout.PAGE_START);
		panel.add(image,BorderLayout.PAGE_END);
		panel.setBackground(Color.white);
		
		return panel;
	}
}