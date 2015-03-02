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

public class Main {
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
        		hd1=Integer.parseInt(line.substring(17,21))+100; //hd1
        		System.out.println(hd1+100);
        	}
        	if(line.indexOf("DTEND:")!=-1)
        	{
        		hf1=Integer.parseInt(line.substring(15,19))+100; //hf1
        		System.out.println(hf1+100);
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
	
	public static void Interrogation(ArrayList<Cours> al, String classe, String nom, String opt, String type, AffichageVocale aff)
	{
        /**récupération de la date et de l'heure du PC**/
        /*Date now = new Date();
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now);
        //System.out.println(date);
        
        int today = Integer.parseInt(date.substring(6,8)+date.substring(3,5)+date.substring(0,2));
        int hour = Integer.parseInt(date.substring(9,11)+date.substring(12,14));
        System.out.println(today);
        System.out.println(hour);
        //System.out.println(" 8. " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now));*/
		
		/**Variables utiles au programme**/
        int k=0;
        int suite=0;
        String dd="";
        String ff="";
        int max=al.size();
        
        /**Variables de tests**/
        int today=150302; //150224
        int hour=800; //1400
        
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
            {
            	if(hour<=((Cours) al.get(k)).getHd() && (((Cours) al.get(k)).getProf().toLowerCase().indexOf(nom.toLowerCase())!=-1))
            	{
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
            	aff.setHoraire("de "+dd.subSequence(0,1));
            }
            else //4
            {
            	System.out.print(dd.subSequence(0,2));
            	aff.setVocale(aff.getVocale()+dd.subSequence(0,2));
            	aff.setHoraire("de "+dd.subSequence(0,2));
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
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Cours> al_RICM4;
		String classe="RICM4"; //    RICM4    TIS4    GEO4    MAT4    PRI4
        String nom = "Zhangmeng"; //DECHAMBOUX BOYER Fabienne    DONSEZ DIDIER    Zhangmeng
        String type = "eleve"; //  eleve  enseignant
        String opt="RICM4 - Op. Multimédia"; //  RICM4 - Op. Multimédia / RICM4 - Op. Système et Réseaux / TIS4 - GrA / GEO 4 - GrA  /  3I4 - A  MAT4 - 1 / PRI4-GR-A
    	
        AffichageVocale aff = new AffichageVocale(0,"","","","","");
        
        //Download(); //téléchargement des emplois du temps de chaque filière
		al_RICM4=Edt(classe);
		Interrogation(al_RICM4,classe,nom,opt,type,aff);
		
		
		/*************affichage fenetre******************/		
		Fenetre fenetre = new Fenetre(aff,type);
		fenetre.setVisible(true);//On la rend visible
		//Thread.sleep(3000);
		//fenetre.dispose();
		/*************affichage fenetre******************/
		
		//Partie vocale
		System.out.println(aff.getVocale());
		Speak(aff.getVocale());
		
		fenetre.dispose(); //fermeture fenetre
	}
}