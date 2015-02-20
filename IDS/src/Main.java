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

	static String vocale="";
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Cours> Edt(String classe) throws IOException
	{
		File f = new File(classe+".ics"); //    ADECal_TIS4.ics    ADECal_GEO4.ics
	    //FileReader fr = new FileReader (f);
	    //BufferedReader br = new BufferedReader (fr);
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
		/*ArrayList<Cours> al_tab[] = new ArrayList[5]; // Tu crées un tableau d'ArrayList normaux
		for(int i = 0 ; i < 5 ; i++) // Tu crées en boucle des ArrayList<Noeud>
		    al_tab[i] = new ArrayList<Cours>();*/
		
	    line = br.readLine(); //System.out.println(line.charAt(0)); System.out.println(line.charAt(14)); System.out.println(line.length());
	    
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
        		//salle1=line.substring(9,line.length()).replace("POLYTECH","POLYTEK");
        		//System.out.println(salle1);
        	}
        	if(line.indexOf("DESCRIPTION:")!=-1) //A REVOIR !!!!! ............................................................................. + synthèse vocale
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
        		} //System.out.println(line.charAt(i-1)+"-"+line.charAt(i));
        		d=i-1;
        		
        		//System.out.println(a+"-"+b+"-"+c+"-"+d);
        		
        		option1=line.substring(a,b);
        		System.out.println(option1);
        		if(merde==1)
        		{
            		prof1=line.substring(c,d);
            		prof1=prof1.replace("\\n"," ou peut être ");
            		prof1=prof1.replace("\\","");
            		//System.out.println(prof1);
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
        	
            //System.out.println(line);
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
        //fr.close();
		return al;
	}
	
	public static void Interrogation(ArrayList<Cours> al, String classe, String nom, String opt, String type)
	{
        //récupération de la date et de l'heure du PC !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        /*Date now = new Date();
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now);
        //System.out.println(date);
        
        int today = Integer.parseInt(date.substring(6,8)+date.substring(3,5)+date.substring(0,2));
        int hour = Integer.parseInt(date.substring(9,11)+date.substring(12,14));
        System.out.println(today);
        System.out.println(hour);
        //System.out.println(" 8. " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now));*/
		
        int k=0;
        int today=150224; //150224
        int hour=800; //1400
        int suite=0;
        String dd="";
        String ff="";
        int max=al.size();
        
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
            		//System.out.println(((Cours) al.get(k)).getOption());
            	}
            	else if(((Cours) al.get(k)).getHd()<=hour && hour<=((Cours) al.get(k)).getHf() && ((((Cours) al.get(k)).getOption()).compareTo(classe)==0 || (((Cours) al.get(k)).getOption()).compareTo(opt)==0)) //
            	{
            		suite=2; //retard
            		//System.out.println(((Cours) al.get(k)).getIntitule());
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
            		//System.out.println(((Cours) al.get(k)).getOption());
            	}
            	else if(((Cours) al.get(k)).getHd()<=hour && hour<=((Cours) al.get(k)).getHf() && (((Cours) al.get(k)).getProf().toLowerCase().indexOf(nom.toLowerCase())!=-1)) //
            	{
            		suite=2; //retard
            		//System.out.println(((Cours) al.get(k)).getIntitule());
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
        	vocale="Bonjour "+nom+"! Vous n'avez plus de cours aujourd'hui! A bientôt!";
        }
        else
        {
            dd=String.valueOf(String.valueOf(((Cours) al.get(k)).getHd()));
            ff=String.valueOf(String.valueOf(((Cours) al.get(k)).getHf()));
            
            if(type.compareTo("eleve")==0)
            {
                System.out.println("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle "+(((Cours) al.get(k)).getSalle())+" avec "+(((Cours) al.get(k)).getProf())+".");
                vocale="Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle"+(((Cours) al.get(k)).getSalle())+" avec "+(((Cours) al.get(k)).getProf())+".";
            }
            else //enseignant
            {
                System.out.println("Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle "+(((Cours) al.get(k)).getSalle())+" avec les "+(((Cours) al.get(k)).getOption())+".");
                vocale="Bonjour "+nom+", vous avez cours de "+(((Cours) al.get(k)).getIntitule())+" en salle"+(((Cours) al.get(k)).getSalle())+" avec les "+(((Cours) al.get(k)).getOption())+".";
            }
            
            
            System.out.print("Le cours se déroulera de ");
            vocale=vocale+" Le cours se déroulera de ";
            
            if(dd.length()==3)
            {
            	System.out.print(dd.subSequence(0,1));
            	vocale=vocale+dd.subSequence(0,1);
            }
            else //4
            {
            	System.out.print(dd.subSequence(0,2));
            	vocale=vocale+dd.subSequence(0,2);
            }
            
            System.out.print("h");
            vocale=vocale+"h";
            
            if(dd.length()==3)
            {
            	System.out.print(dd.subSequence(1,3));
            	vocale=vocale+dd.subSequence(1,3);
            }
            else //4
            {
            	System.out.print(dd.subSequence(2,4));
            	vocale=vocale+dd.subSequence(2,4);
            }
            
            System.out.print(" à ");
            vocale=vocale+" à ";
            
            if(ff.length()==3)
            {
            	System.out.print(ff.subSequence(0,1));
            	vocale=vocale+ff.subSequence(0,1);
            }
            else //4
            {
            	System.out.print(ff.subSequence(0,2));
            	vocale=vocale+ff.subSequence(0,2);
            }
            
            System.out.print("h");
            vocale=vocale+"h";
            
            if(ff.length()==3)
            {
            	System.out.print(ff.subSequence(1,3));
            	vocale=vocale+ff.subSequence(1,3);
            }
            else //4
            {
            	System.out.print(ff.subSequence(2,4));
            	vocale=vocale+ff.subSequence(2,4);
            }
            
            System.out.println(". Je vous souhaite une excellente journée!");
            vocale=vocale+". Je vous souhaite une excellente journée!";
        }
        vocale=vocale.replace("S.","");
        vocale=vocale.replace("Salle","");
        vocale=vocale.replace(" (IM²AG)","");
        vocale=vocale.replace("POLYTECH","");
        vocale=vocale.replace("00","");
        vocale=vocale.replace("S8 ","");
	}
	
	public static void Speak(String vocale) throws Exception
	{
		//On saisit une voix et un texte
		String langue = "Yeti";
		String texte = vocale;
		
		//On affiche la saisie
		System.out.println(langue);
		System.out.println(texte);
		
		//On écrit dans le fichier ce qui va être traduit
		PrintWriter fichier = new PrintWriter(new FileWriter("C:/wamp/www/PHP-Voxygen-master/texte.txt"));
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
        String file = "C:/wamp/www/PHP-Voxygen-master/texte2.txt";
        BufferedReader buff = new BufferedReader(new FileReader(file));
        String line;
        line = buff.readLine();
        buff.close();
        System.out.println(line);
        
        //On lit le fichier son
        Sound application = new Sound("C:/wamp/www/PHP-Voxygen-master/cache/"+line);
        application.play();
	}
	
    public static void Download() throws IOException
    {
    	String tab_url[] = {"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=839,1133,1132,857&projectId=3&calType=ical&nbWeeks=1",
    						"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=698,1128,693&projectId=3&calType=ical&nbWeeks=1",
    						"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=86,1094,1093,1092,1091&projectId=3&calType=ical&nbWeeks=1",
    						"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=1139,1138,1137,1049,1010,1009,1007,1006,1005&projectId=3&calType=ical&nbWeeks=1",
    						"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=827,90,1072,1071,1068,24,9,8,990,906,867,2036,2037,2034,2035,2038,196&projectId=3&calType=ical&nbWeeks=1",
    						"http://ade6-ujf.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=920,917&projectId=3&calType=ical&nbWeeks=1"};
    	String tab_classe[] = {"RICM4", "TIS4", "PRI4" , "GEO4", "MAT4", "3I4"};
    	
    	for(int i=0;i<6;i++)
    	{
        	//On exécute la requête HTTP
            URL oracle = new URL(tab_url[i]);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
    		PrintWriter fichier = new PrintWriter(new FileWriter(tab_classe[i]+".ics"));
            //On affiche le résultat
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                //System.out.println(inputLine);
        		fichier.println(inputLine);
            }
            in.close();
    		fichier.close();
    	}
    }
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ArrayList<Cours> al_RICM4;
		String classe="RICM4"; //    RICM4    TIS4    GEO4    MAT4    PRI4
        String nom = "Zhangmeng"; //DECHAMBOUX BOYER Fabienne    DONSEZ DIDIER    Zhangmeng
        String type = "eleve"; //  eleve  enseignant
        String opt="RICM4 - Op. Multimédia"; //  RICM4 - Op. Multimédia / RICM4 - Op. Système et Réseaux / TIS4 - GrA / GEO 4 - GrA  /  3I4 - A  MAT4 - 1 / PRI4-GR-A
		
        //Download(); //téléchargement des emplois du temps de chaque filière
		al_RICM4=Edt(classe);
		Interrogation(al_RICM4,classe,nom,opt,type);
		
		//Partie vocale
		System.out.println(vocale);
		Speak(vocale);
	}
}