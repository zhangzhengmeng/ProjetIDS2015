/*
 * Création d'un objet personne avec tous les attributs nécessaire pour une personne
 */
public class Personne {

	private String nom = "DECHAMBOUX BOYER Fabienne"; //DECHAMBOUX BOYER Fabienne    DONSEZ DIDIER    Zhangmeng
	private String type = "enseignant"; //  eleve  enseignant
	private String classe="RICM4"; //    RICM4    TIS4    GEO4    MAT4    PRI4
	private String opt="RICM4 - Op. Multimédia"; //  RICM4 - Op. Multimédia / RICM4 - Op. Système et Réseaux

	public Personne(String nom, String type, String classe, String opt) {
		this.nom=nom;
		this.type=type;
		this.classe=classe;
		this.opt=opt;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}
}



