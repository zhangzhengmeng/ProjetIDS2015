@SuppressWarnings("rawtypes")
/*
 * Création d'un objet cours avec tous les attributs nécessaire
 */
public class Cours implements java.lang.Comparable {

	private int date;
	private int hd;
	private int hf;
	private String intitule;
	private String salle;
	private String prof;
	private String option;

	public Cours(int date1, int hd1, int hf1, String intitule1, String salle1, String prof1, String option1) {
		date=date1;
		hd=hd1;
		hf=hf1;
		intitule=intitule1;
		salle=salle1;
		prof=prof1;
		option=option1;
	}
	
	public int getHd() {
		return hd;
	}

	public void setHd(int hd) {
		this.hd = hd;
	}

	public int getHf() {
		return hf;
	}

	public void setHf(int df) {
		this.hf = df;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getSalle() {
		return salle;
	}

	public void setSalle(String salle) {
		this.salle = salle;
	}

	public String getProf() {
		return prof;
	}

	public void setProf(String prof) {
		this.prof = prof;
	}
	
	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public int compareTo(Object o) {
	      int i = getDate() > ((Cours) o).getDate() ? 1 : getDate() < ((Cours) o).getDate() ? -1 : 0;
	      if(i != 0) {           
              return i;
          }
	      int y = getHd() > ((Cours) o).getHd() ? 1 : getHd() < ((Cours) o).getHd() ? -1 : 0;
	      if(y != 0) {
              return y;
          }
	      return(0);
	}
}
