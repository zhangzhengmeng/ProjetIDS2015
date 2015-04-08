public class AffichageVocale {
	
	private int fin;
	private String cours;
	private String salle;
	private String enseignant;
	private String horaire;
	private String vocale;
	
	public AffichageVocale(int fin, String cours, String salle, String enseignant, String horaire, String vocale) {
		this.fin = fin;
		this.cours = cours;
		this.salle = salle;
		this.enseignant = enseignant;
		this.horaire = horaire;
		this.vocale = vocale;
	}
	public int getFin() {
		return fin;
	}
	public void setFin(int fin) {
		this.fin = fin;
	}
	public String getCours() {
		return cours;
	}
	public void setCours(String cours) {
		this.cours = cours;
	}
	public String getSalle() {
		return salle;
	}
	public void setSalle(String salle) {
		this.salle = salle;
	}
	public String getEnseignant() {
		return enseignant;
	}
	public void setEnseignant(String enseignant) {
		this.enseignant = enseignant;
	}
	public String getHoraire() {
		return horaire;
	}
	public void setHoraire(String horaire) {
		this.horaire = horaire;
	}
	public String getVocale() {
		return vocale;
	}
	public void setVocale(String vocale) {
		this.vocale = vocale;
	}
}
