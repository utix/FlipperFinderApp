package fr.fafsapp.flipper.finder.service.parse;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import fr.fafsapp.flipper.finder.database.FlipperDatabaseHandler;
import fr.fafsapp.flipper.finder.metier.Enseigne;

public class ParseEnseigneService {
	
	/**
	 * Retourne toutes les enseignes à partir du cloud
	 * @return int
	 */
	public List<Enseigne> getAllEnseigne(){
		List<Enseigne> listeEnseigne = new ArrayList<Enseigne>();
		/*
		List<ParseObject> listePo = new ArrayList<ParseObject>();
    	ParseQuery query = new ParseQuery(FlipperDatabaseHandler.ENSEIGNE_TABLE_NAME);
    	try {
    		query.setLimit(2000);
    		listePo = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	for (ParseObject po : listePo){
    		Enseigne enseigne = new Enseigne(
					po.getLong("ensId"),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_TYPE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_NOM),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_HORAIRE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_LATITUDE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_LONGITUDE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_ADRESSE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_CODE_POSTAL),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_VILLE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_PAYS));
    		listeEnseigne.add(enseigne);
    	}
*/
		return listeEnseigne;
	}

	/**
	 * Retourne la liste des enseignes à mettre à jour à partir d'une date donnée.
	 * @param dateDerniereMaj
	 * @return
	 */
	public List<Enseigne> getMajEnseigneByDate(String dateDerniereMaj){
		List<Enseigne> listeEnseigne = new ArrayList<Enseigne>();

		List<ParseObject> listePo = new ArrayList<ParseObject>();
    	ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ENSEIGNE2");
    	try {
    		query.setLimit(2000);
    		query.whereGreaterThanOrEqualTo(FlipperDatabaseHandler.ENSEIGNE_DATMAJ, dateDerniereMaj);
    		listePo = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
    	for (ParseObject po : listePo){
    		Enseigne enseigne = new Enseigne(
					po.getLong(FlipperDatabaseHandler.ENSEIGNE_ID),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_TYPE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_NOM),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_HORAIRE),
					po.getNumber(FlipperDatabaseHandler.ENSEIGNE_LATITUDE).toString(),
					po.getNumber(FlipperDatabaseHandler.ENSEIGNE_LONGITUDE).toString(),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_ADRESSE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_CODE_POSTAL),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_VILLE),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_PAYS),
					po.getString(FlipperDatabaseHandler.ENSEIGNE_DATMAJ));
    		listeEnseigne.add(enseigne);
    	}
		return listeEnseigne;
	}

}
