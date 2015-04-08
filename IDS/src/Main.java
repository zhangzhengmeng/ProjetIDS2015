import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main { //classe pricipale avec la fonction main
	
	public static String code = null; //code lu par le lecteur NFC
	public static Boolean fini = false; //pour éviter les plantages du lecteur NFC
	public static Boolean trouve = false; //pour le cas où un prof cherche sa salle
	
	//fonction qui met un emploi du temps (celui de la classe "classe") dans une structure (ici une liste de cours)
	//on obtient en sortie une liste des cours triées dans l'ordre des cours de la semaine
	@SuppressWarnings("unchecked")
	public static ArrayList<Cours> Edt(String classe) throws IOException
	{
		File f = new File(classe+".ics");
		String line="";
		int a,b,c,d;
		int coco=0;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
		
		int date1 = 0;
		int hd1 = 0;
		int hf1 = 0;
		String intitule1 = "";
		String salle1 = "";
		String prof1 = "";
		String option1 = "";
		
		ArrayList<Cours> al = new ArrayList<Cours>();
	    line = br.readLine();
	    
	    /*********ATTENTION FAIRE +1 HEURE***********/
	    
        while (line != null)
        {
        	if(line.indexOf("DTSTART:")!=-1)
        	{
        		date1=Integer.parseInt(line.substring(9,16)); //date1
        		System.out.println(date1);
        		hd1=Integer.parseInt(line.substring(17,21))+200; //hd1 (ATTENTION : si heure d'été -> +200 et si heure d'hiver +100)
        		System.out.println(hd1+200);
        	}
        	if(line.indexOf("DTEND:")!=-1)
        	{
        		hf1=Integer.parseInt(line.substring(15,19))+200; //hf1  (ATTENTION : si heure d'été -> +200 et si heure d'hiver +100)
        		System.out.println(hf1+200);
        	}
        	if(line.indexOf("SUMMARY:")!=-1)
        	{
        		intitule1=line.substring(8,line.length());//intitule1
        		System.out.println(intitule1);
        	}
        	if(line.indexOf("LOCATION:")!=-1)
        	{
        		salle1=line.substring(9,line.length()).replace("\\,"," ou alors en salle");//salle1
        		salle1=salle1.replace(" ? ",", inconnue,");
        	}
        	if(line.indexOf("DESCRIPTION:")!=-1)
        	{
        		int i=1;
        		int merde=0;
        		while((line.charAt(i-1)!='\\' || line.charAt(i)!='n') && (line.charAt(i-1)!='n' || line.charAt(i)!='('))
        		{
        			i++;
        		}
        		a=i+1;
        		i++;
        		while((line.charAt(i-1)!='\\' || line.charAt(i)!='n') && (line.charAt(i-1)!='n' || line.charAt(i)!='('))
        		{
        			i++;
        		}
        		b=i-1;
        		c=i+1;
        		i++;
        		while((line.charAt(i-1)!='n' || line.charAt(i)!='(') && i<line.length()-1)
        		{
        			i++;
        			merde=1;
        		}
        		d=i-1;
        		
        		option1=line.substring(a,b);
        		System.out.println(option1);
        		if(merde==1)
        		{
            		prof1=line.substring(c,d);
            		prof1=prof1.replace("\\n"," ou peut être ");
            		prof1=prof1.replace("\\","");
        		}
        		else //pas de prof
        		{
        			prof1="un, anonyme";
        		}
        		coco=1;
        	}
        	
        	if(coco==1)
    		{
    			al.add(new Cours(date1, hd1, hf1, intitule1, salle1, prof1, option1));
    			coco=0;
    		}
        	
    		line = br.readLine();
        }
        
        System.out.println("********************************************");
        
        Collections.sort(al); //tri
        
        //affichage de l'emploi du temps
        for(int i = 0; i < al.size(); i++)
        {
        	System.out.println(((Cours) al.get(i)).getIntitule());
        }
        
        br.close();
        
		return al;
	}
	
	//fonction qui permet d'interroger l'emploi du temps à partir de certaines informations (liste de cours, classe, nom, option, statut, fenetre) et de construire l'affichage
	//ainsi que les phrases qui seront prononcées par la synthèse vocale
	public static void Interrogation(ArrayList<Cours> al, String classe, String nom, String opt, String type, AffichageVocale aff) throws NumberFormatException, IOException
	{
        /**récupération de la date et de l'heure du PC**/
        Date now = new Date();
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now);
        //System.out.println(date);
        
        int today = Integer.parseInt(date.substring(6,8)+date.substring(3,5)+date.substring(0,2));
        int hour = Integer.parseInt(date.substring(9,11)+date.substring(12,14));
        System.out.println(today);
        System.out.println(hour);
        //System.out.println(" 8. " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now));
		
		System.out.println("*****************"+nom);
		
		/**Variables utiles au programme**/
        int k=0;
        int suite=0;
        String dd="";
        String ff="";
        int max=al.size();
        
        /*****************Variables de tests*******************/
        /*int today=0;
        int hour=0;
		BufferedReader in = new BufferedReader(new FileReader("date.txt"));
		String str = null;
	    String[] mots = null;
	    
		 while((str = in.readLine()) != null){
			mots = str.split(" ");
			today=Integer.parseInt(mots[0]);
			hour=Integer.parseInt(mots[1]);
		 }
		 
		 in.close();*/
	    /******************************************************/
        
        while(((Cours) al.get(k)).getDate()<today && k<max-1)
        {
        	k++;
        }
        
        if(type.compareTo("eleve")==0)
        {
            while(suite==0 && k<max-1 && ((Cours) al.get(k)).getDate()==today)
            {
            	if(hour<=((Cours) al.get(k)).getHd() && ((((Cours) al.get(k)).getOption()).compareTo(classe)==0 || (((Cours) al.get(k)).getOption()).compareTo(opt)==0 ))
            	{
            		suite=1; //avance
            	}
            	else if(((Cours) al.get(k)).getHd()<=hour && hour<=((Cours) al.get(k)).getHf() && ((((Cours) al.get(k)).getOption()).compareTo(classe)==0 || (((Cours) al.get(k)).getOption()).compareTo(opt)==0)) //
            	{
            		suite=2; //retard
            	}
            	else
            	{
            		k++;
            	}
            }
        }
        else //enseignant
        {
            while(suite==0 && k<max-1 && ((Cours) al.get(k)).getDate()==today)
            { System.out.println("ttttttttttttttttttttttttt"+((Cours) al.get(k)).getProf());
            	if(hour<=((Cours) al.get(k)).getHd() && (((Cours) al.get(k)).getProf().toLowerCase().indexOf(nom.toLowerCase())!=-1))
            	{ System.out.println("*****************"+((Cours) al.get(k)).getProf());
            		suite=1; //avance
            	}
            	else if(((Cours) al.get(k)).getHd()<=hour && hour<=((Cours) al.get(k)).getHf() && (((Cours) al.get(k)).getProf().toLowerCase().indexOf(nom.toLowerCase())!=-1)) //
            	{
            		suite=2; //retard
            	}
            	else
            	{
            		k++;
            	}
            }
        }

        if(k==max || ((Cours) al.get(k)).getDate()!=today)
        {
        	System.out.print("Vous n'avez plus de cours aujourd'hui! Bonne soirée!");
        	aff.setVocale("Bonjour "+nom+"! Vous n'avez plus de cours aujourd'hui! A bientôt!");
        	aff.setFin(1);
        }
        else
        {
            dd=String.valueOf(String.valueOf(((Cours) al.get(k)).getHd()));
            ff=String.valueOf(String.valueOf(((Cours) al.get(k)).getHf()));
            
            if(type.compareTo("eleve")==0)
            {
                System.out.println("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle "+(((Cours) al.get(k)).getSalle())+" avec "+(((Cours) al.get(k)).getProf())+".");
                aff.setVocale("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle"+(((Cours) al.get(k)).getSalle())+" avec "+(((Cours) al.get(k)).getProf())+".");
                aff.setCours((((Cours) al.get(k)).getIntitule()));
                aff.setSalle((((Cours) al.get(k)).getSalle()));
                aff.setEnseignant((((Cours) al.get(k)).getProf()));
            }
            else //enseignant
            {
                System.out.println("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle "+(((Cours) al.get(k)).getSalle())+" avec les "+(((Cours) al.get(k)).getOption())+".");
                aff.setVocale("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle"+(((Cours) al.get(k)).getSalle())+" avec les "+(((Cours) al.get(k)).getOption())+".");
                aff.setCours((((Cours) al.get(k)).getIntitule()));
                aff.setSalle((((Cours) al.get(k)).getSalle()));
                aff.setEnseignant((((Cours) al.get(k)).getOption()));
            }
            
            
            System.out.print("Le cours se déroulera de ");
            aff.setVocale(aff.getVocale()+" Le cours se déroulera de ");
            
            if(dd.length()==3)
            {
            	System.out.print(dd.subSequence(0,1));
            	aff.setVocale(aff.getVocale()+dd.subSequence(0,1));
            	aff.setHoraire(""+dd.subSequence(0,1));
            }
            else //4
            {
            	System.out.print(dd.subSequence(0,2));
            	aff.setVocale(aff.getVocale()+dd.subSequence(0,2));
            	aff.setHoraire(""+dd.subSequence(0,2));
            }
            
            System.out.print("h");
            aff.setVocale(aff.getVocale()+"h");
            aff.setHoraire(aff.getHoraire()+"h");
            
            if(dd.length()==3)
            {
            	System.out.print(dd.subSequence(1,3));
            	aff.setVocale(aff.getVocale()+dd.subSequence(1,3));
            	aff.setHoraire(aff.getHoraire()+dd.subSequence(1,3));
            }
            else //4
            {
            	System.out.print(dd.subSequence(2,4));
            	aff.setVocale(aff.getVocale()+dd.subSequence(2,4));
            	aff.setHoraire(aff.getHoraire()+dd.subSequence(2,4));
            }
            
            System.out.print(" à ");
            aff.setVocale(aff.getVocale()+" à ");
            aff.setHoraire(aff.getHoraire()+" à ");
            
            if(ff.length()==3)
            {
            	System.out.print(ff.subSequence(0,1));
            	aff.setVocale(aff.getVocale()+ff.subSequence(0,1));
            	aff.setHoraire(aff.getHoraire()+ff.subSequence(0,1));
            }
            else //4
            {
            	System.out.print(ff.subSequence(0,2));
            	aff.setVocale(aff.getVocale()+ff.subSequence(0,2));
            	aff.setHoraire(aff.getHoraire()+ff.subSequence(0,2));
            }
            
            System.out.print("h");
            aff.setVocale(aff.getVocale()+"h");
            aff.setHoraire(aff.getHoraire()+"h");
            
            if(ff.length()==3)
            {
            	System.out.print(ff.subSequence(1,3));
            	aff.setVocale(aff.getVocale()+ff.subSequence(1,3));
            	aff.setHoraire(aff.getHoraire()+ff.subSequence(1,3));
            }
            else //4
            {
            	System.out.print(ff.subSequence(2,4));
            	aff.setVocale(aff.getVocale()+ff.subSequence(2,4));
            	aff.setHoraire(aff.getHoraire()+ff.subSequence(2,4));
            }
            
            System.out.println(". Je vous souhaite une excellente journée!");
            aff.setVocale(aff.getVocale()+". Je vous souhaite une excellente journée!");
            aff.setFin(0);
            trouve=true;
        }
        aff.setVocale(aff.getVocale().replace("S.",""));
        aff.setVocale(aff.getVocale().replace("Salle",""));
        aff.setVocale(aff.getVocale().replace(" (IM²AG)",""));
        aff.setVocale(aff.getVocale().replace("POLYTECH",""));
        aff.setVocale(aff.getVocale().replace("00",""));
        aff.setVocale(aff.getVocale().replace("S8 ",""));
        aff.setSalle(aff.getSalle().replace(",",""));
        aff.setSalle(aff.getSalle().replace("ou alors en salle","/"));
        aff.setSalle(aff.getSalle().replace("Salle",""));
        aff.setSalle(aff.getSalle().replace("POLYTECH",""));
        aff.setEnseignant(aff.getEnseignant().replace("un, ",""));
	}
	
	//fonction qui envoie du texte sur le serveur de Voxygen, qui récupère l'enregistrement et qui le joue
	public static void Speak(String vocale) throws Exception
	{
		BufferedReader prop = new BufferedReader(new FileReader("speak.txt"));
	    String langue;
	    String serveur;
	    
	    langue = prop.readLine();
	    serveur = prop.readLine();
	    prop.close();
	    
		//On saisit un texte
		String texte = vocale;
		
		//On affiche la saisie
		System.out.println(langue);
		System.out.println(texte);
		
		//On écrit dans le fichier ce qui va être traduit
		PrintWriter fichier = new PrintWriter(new FileWriter(serveur+"texte.txt"));
		fichier.println(langue);
		fichier.print(texte);
		fichier.close();
		
    	//On exécute la requête HTTP
        URL oracle = new URL("http://localhost/PHP-Voxygen-master/");
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

        //On affiche le résultat
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    	
        //Le fichier son porte le nom ...
        String file = serveur+"texte2.txt";
        BufferedReader buff = new BufferedReader(new FileReader(file));
        String line;
        line = buff.readLine();
        buff.close();
        System.out.println(line);
        
        //On lit le fichier son
        Sound application = new Sound(serveur+"cache/"+line);
        application.play();
	}
	
	//fonction qui permet de télécharger automatiquement les emplois du temps de chaque filière
	public static void Download() throws IOException
    {
		ArrayList<String> al_url = new ArrayList<String>();
		ArrayList<String> al_classe = new ArrayList<String>();
    	
    	@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new FileReader("adweb.txt"));
	    String str = null;
	    String[] mots = null;
	    
	    while((str = in.readLine()) != null){
			mots = str.split(" ");
			al_classe.add(mots[0]);
			al_url.add(mots[1]);
		}
	    
    	for(int i=0;i<6;i++)
    	{
        	//On exécute la requête HTTP
            URL oracle = new URL(al_url.get(i));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(oracle.openStream()));
    		PrintWriter fichier2 = new PrintWriter(new FileWriter(al_classe.get(i)+".ics"));
            //On affiche le résultat
            String inputLine;
            while ((inputLine = in2.readLine()) != null)
            {
        		fichier2.println(inputLine);
            }
            in2.close();
    		fichier2.close();
    	}
    }
	
	//Recherche de la personne correspondante au numéro (num étu par ex) lu par le lecteur et qui retourne une personne avec toutes les informations utiles là concernant
	public static Personne NFC_Recherche() throws IOException, InterruptedException
    {
		while(fini==false)
		{
		    TestThread t = new TestThread("A");
		    t.start();
		    t.join();
		}
		
		BufferedReader in = new BufferedReader(new FileReader("nfc.txt"));
		String str = null;
	    String[] mots = null;
	    Personne p = null;
	    Boolean fin = false;
	    System.out.println(code);
	    code=code.substring(2,10);
	    System.out.println(code);
	    
		 while((str = in.readLine()) != null && fin==false){
			mots = str.split("/");
			if(mots[0].compareTo(code)==0)
			{
				if(mots[1].compareTo("eleve")==0) //eleve
				{
					p = new Personne(mots[2],mots[1],mots[4],mots[5]); fin=true;
					System.out.println(mots[2]+" "+mots[1]+" "+mots[4]+" "+mots[5]);
				}
				else //enseignant
				{
					p = new Personne(mots[2],mots[1],"",""); fin=true;
					System.out.println(mots[2]+" "+mots[1]);
				}
			}
		 }
		 
		 in.close();
		 fini=false;
		 System.out.println("Personne -> "+p.getNom()+" "+p.getType()+" "+p.getClasse()+" "+p.getOpt());
		 
		 return(p);
    }
	
	//Fonction qui met les emplois du temps de chaque filère dans une structure (une liste de ListeCours)
	public static ArrayList<ListeCours> TraitementEmploiDuTemps() throws IOException, InterruptedException
    {
		ArrayList<ListeCours> al = new ArrayList<ListeCours>();
		ArrayList<Cours> al_temp = new ArrayList<Cours>();
		BufferedReader in = new BufferedReader(new FileReader("adweb.txt"));
		String str = null;
	    String[] mots = null;
	    
		 while((str = in.readLine()) != null){
			mots = str.split(" ");
			al_temp=Edt(mots[0]);
			ListeCours alc = new ListeCours(mots[0],al_temp);
			al.add(alc);
		 }
		 
		 in.close();
		 
		 return al;
    }
	
	//fonction principale
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		//String classe -> RICM4    TIS4    GEO4    MAT4    PRI4
        //String nom -> DECHAMBOUX BOYER Fabienne    DONSEZ DIDIER    Zhangmeng
        //String type -> eleve  enseignant
        //String opt -> RICM4 - Op. Multimédia / RICM4 - Op. Système et Réseaux / TIS4 - GrA / GEO 4 - GrA  /  3I4 - A  / MAT4 - 1 / PRI4-GR-A
		
		Download(); //téléchargement automatique des emplois du temps
		Date datePre = new Date();
		
		Fenetre f = new Fenetre();
		f.setVisible(true);//On la rend visible
		f.setTitle("IDS"); //On donne un titre à l'application
		f.setSize(200,100); //On donne une taille à notre fenêtre
		f.setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
		f.setResizable(true); //On permet le redimensionnement
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
		
		ArrayList<ListeCours> al_classe;
		al_classe=TraitementEmploiDuTemps(); //on met les emplois du temps dans une structure
		
		while(true) //boucle du programme principal (attente nfc + traitement de la demande + affichage + vocale)
		{
			int i=0;
			AffichageVocale aff = new AffichageVocale(0,"","","","","");
			
			Personne p = NFC_Recherche(); //on récupère une personne à partir du tag nfc
			
			/****************téléchargement des emplois du temps automatiquement****************/
			Date now = new Date();
	        String today = now.toString();
	        today=today.substring(0,3);
	        long diff = now.getTime( ) - datePre.getTime( );
	        diff=(diff/(1000*60*60*24));
	        
	        switch(today)
	        {
	        case "Mon" : if(diff>0){Download(); datePre=now;}
	        	break;
	        case "Tue" : if(diff>1){Download(); datePre=now;}
	        	break;
	        case "Wed" : if(diff>2){Download(); datePre=now;}
	        	break;
	        case "Thu" : if(diff>3){Download(); datePre=now;}
	        	break;
	        case "Fri" : if(diff>4){Download(); datePre=now;}
	        	break;
	        case "Sat" : if(diff>5){Download(); datePre=now;}
	        	break;
	        case "Sun" : if(diff>6){Download(); datePre=now;}
	        	break;
	        }
			
	        /****************interrogation****************/
			if(p.getType().compareTo("eleve")==0) //on cherche quel est son prochain cours de la journée en fonction de l'heure qu'il est
			{
				while((al_classe.get(i).getClasse().compareTo(p.getClasse())!=0) && i < al_classe.size())
				{
					i++;
				}
				
				Interrogation(al_classe.get(i).getAl(),p.getClasse(),p.getNom(),p.getOpt(),p.getType(),aff);
			}
			else //enseignant
			{
				while(i < al_classe.size() && trouve==false)
				{
					Interrogation(al_classe.get(i).getAl(),p.getClasse(),p.getNom(),p.getOpt(),p.getType(),aff);
					i++;
				}
			}
			trouve=false; //on remet à false pour le prochain coup où on en aura besoin
			
			/*************affichage fenetre******************/		
			Fenetre fenetre = new Fenetre(aff,p.getType());
			fenetre.setVisible(true);//On la rend visible
			/*************affichage fenetre******************/
			
			//Partie vocale
			System.out.println(aff.getVocale());
			Speak(aff.getVocale());
			
			fenetre.dispose(); //fermeture fenetre
		}
	}
}