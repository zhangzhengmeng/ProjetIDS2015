import java.util.ArrayList;
/*
 * Création d'un objet liste_cours avec une classe et la liste de cours qui lui est associée
 */
public class ListeCours {
	private String classe;
	private ArrayList<Cours> al;

	public ListeCours(String classe, ArrayList<Cours> al) {
		this.classe=classe;
		this.al=al;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public ArrayList<Cours> getAl() {
		return al;
	}

	public void setAl(ArrayList<Cours> al) {
		this.al = al;
	}
}
