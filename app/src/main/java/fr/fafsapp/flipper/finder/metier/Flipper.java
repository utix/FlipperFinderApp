package fr.fafsapp.flipper.finder.metier;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Flipper implements Serializable {

	private static final long serialVersionUID = 2088862582774334050L;

	@SerializedName("FLIP_ID")
	private long id;

	@SerializedName("FLIP_MODELE")
	private long idModele;

	@SerializedName("FLIP_NB_CREDITS_2E")
	private Long nbCreditsDeuxEruros;

	@SerializedName("FLIP_ENSEIGNE")
	private long idEnseigne;

	@SerializedName("FLIP_ACTIF")
	private boolean actif;

	@SerializedName("FLIP_DATMAJ")
	private String dateMaj;

	private ModeleFlipper modele;
	private Enseigne enseigne;

	public Flipper() {
	}

	public Flipper(long id, long idModele, long nbCreditsDeuxEruros,
			long idEnseigne, long actif, String dateMaj) {
		this.id = id;
		this.idModele = idModele;
		this.nbCreditsDeuxEruros = nbCreditsDeuxEruros;
		this.idEnseigne = idEnseigne;
		if (actif == 1)
			this.actif = true;
		else
			this.actif = false;
		this.dateMaj = dateMaj;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ModeleFlipper getModele() {
		return modele;
	}

	public void setModele(ModeleFlipper modele) {
		this.modele = modele;
		if (modele != null) {
			this.idModele = modele.getId();
		}
	}

	public Long getNbCreditsDeuxEruros() {
		return nbCreditsDeuxEruros;
	}

	public void setNbCreditsDeuxEruros(Long nbCreditsDeuxEruros) {
		this.nbCreditsDeuxEruros = nbCreditsDeuxEruros;
	}

	public Enseigne getEnseigne() {
		return enseigne;
	}

	public void setEnseigne(Enseigne enseigne) {
		this.enseigne = enseigne;
		if (enseigne != null) {
			this.idEnseigne = enseigne.getId();
		}
	}

	public long getIdModele() {
		return idModele;
	}

	public void setIdModele(long idModele) {
		this.idModele = idModele;
	}

	public long getIdEnseigne() {
		return idEnseigne;
	}

	public void setIdEnseigne(long idEnseigne) {
		this.idEnseigne = idEnseigne;
	}

	public String getDateMaj() {
		return this.dateMaj;
	}

	public void setDateMaj(String dateMaj) {
		this.dateMaj = dateMaj;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(long actif) {
		if (actif == 1)
			this.actif = true;
		else
			this.actif = false;
	}

}
