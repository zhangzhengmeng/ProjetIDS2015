import java.util.Date;
import java.util.GregorianCalendar;


public class TestDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Date now = new Date();
        String date = now.toString();
        date=date.substring(0,3);//+date.substring(11,13)+date.substring(14,16)+date.substring(17,19);
        if(date.compareTo("Wed")==0)
        {
        	System.out.println("["+date+"]");
        }*/
        //System.out.println("["+date+"]");
        
        Date date1 = new GregorianCalendar(2015,03,7,1,31).getTime( );
        System.out.println("["+date1+"]");
        //Date du jour 
        Date today = new Date( );

        //Calcul de différence
        long diff = today.getTime( ) - date1.getTime( );

        System.out.println("Différence en nombre de jour entre "+date1+ " et " + today + " est " + (diff / (1000*60*60*24)) + " jours.");
        
        
	}

}
