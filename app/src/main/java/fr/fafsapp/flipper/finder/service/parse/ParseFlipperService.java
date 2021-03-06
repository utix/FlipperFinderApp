package fr.fafsapp.flipper.finder.service.parse;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import fr.fafsapp.flipper.finder.R;
import fr.fafsapp.flipper.finder.database.FlipperDatabaseHandler;
import fr.fafsapp.flipper.finder.fragment.FragmentActionsFlipper.FragmentActionCallback;
import fr.fafsapp.flipper.finder.metier.Commentaire;
import fr.fafsapp.flipper.finder.metier.Flipper;
import fr.fafsapp.flipper.finder.service.base.BaseCommentaireService;
import fr.fafsapp.flipper.finder.service.base.BaseFlipperService;

public class ParseFlipperService {
	
	private FragmentActionCallback mFragmentCallback;
	
    public ParseFlipperService(FragmentActionCallback fragmentCallback) {
        mFragmentCallback = fragmentCallback;
    }

	/**
	 * Retourne tous les flipper � partir du cloud
	 * 
	 * @return List<ModeleFlipper>
	 */
	public List<Flipper> getAllFlipper() {
		List<Flipper> listeFlipper = new ArrayList<Flipper>();
		/*
		List<ParseObject> listePo = new ArrayList<ParseObject>();
		ParseQuery query = new ParseQuery(
				FlipperDatabaseHandler.FLIPPER_TABLE_NAME);
		try {
			query.setLimit(2000);
			listePo = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		for (ParseObject po : listePo) {
			Flipper flipper = new Flipper(po.getLong("flipId"),
					po.getLong(FlipperDatabaseHandler.FLIPPER_MODELE),
					po.getLong(FlipperDatabaseHandler.FLIPPER_NB_CREDITS_2E),
					po.getLong(FlipperDatabaseHandler.FLIPPER_ENSEIGNE));
			listeFlipper.add(flipper);
		}
		*/
		return listeFlipper;
	}

	/**
	 * Retourne la liste des flippers � mettre � jour � partir d'une date donn�e.
	 * @param dateDerniereMaj
	 * @return
	 */
	public List<Flipper> getMajFlipperByDate(String dateDerniereMaj){
		List<Flipper> listeFlipper = new ArrayList<Flipper>();

		List<ParseObject> listePo = new ArrayList<ParseObject>();
    	ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FlipperDatabaseHandler.FLIPPER_TABLE_NAME);
    	try {
    		query.setLimit(2000);
    		query.whereGreaterThanOrEqualTo(FlipperDatabaseHandler.FLIPPER_DATMAJ, dateDerniereMaj);
    		listePo = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
    	for (ParseObject po : listePo){
    		Flipper flipper = new Flipper(po.getLong(FlipperDatabaseHandler.FLIPPER_ID),
					po.getLong(FlipperDatabaseHandler.FLIPPER_MODELE),
					po.getLong(FlipperDatabaseHandler.FLIPPER_NB_CREDITS_2E),
					po.getLong(FlipperDatabaseHandler.FLIPPER_ENSEIGNE),
					po.getLong(FlipperDatabaseHandler.FLIPPER_ACTIF),
					po.getString(FlipperDatabaseHandler.FLIPPER_DATMAJ));
    		listeFlipper.add(flipper);
    	}
		return listeFlipper;
	}

	public boolean updateDateFlipper(final Context pContext, final Flipper flipper, final String dateToSave){
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FlipperDatabaseHandler.FLIPPER_TABLE_NAME);
		query.whereEqualTo(FlipperDatabaseHandler.FLIPPER_ID, flipper.getId());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects != null && objects.size() > 0){
					objects.get(0).put(FlipperDatabaseHandler.FLIPPER_DATMAJ, dateToSave);
					objects.get(0).saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null){
								BaseFlipperService baseFlipperService = new BaseFlipperService();
								flipper.setDateMaj(dateToSave);
								baseFlipperService.majFlipper(flipper, pContext);
								Toast toast = Toast.makeText(pContext, pContext.getResources().getString(R.string.toastValidationCloudOK), Toast.LENGTH_SHORT);
								toast.show();
								if (mFragmentCallback != null){
									mFragmentCallback.onTaskDone();
								}
							}else{
							Toast toast = Toast.makeText(pContext, pContext.getResources().getString(R.string.toastValidationCloudKO), Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				});
			}
		}
		});
		return true;
	}
	
	public boolean remplaceModeleFlipper(final Context pContext, final Flipper ancienflipper, final Flipper nouveauFlipper, final Commentaire commentaire){
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(FlipperDatabaseHandler.FLIPPER_TABLE_NAME);
		query.whereEqualTo(FlipperDatabaseHandler.FLIPPER_ID, ancienflipper.getId());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects != null && objects.size() > 0){
					// On v�rifie d'abord que le flipper n'�tait pas d�j� d�sactiv�, pour ne pas cr�er de doublons
					if (objects.get(0).getInt(FlipperDatabaseHandler.FLIPPER_ACTIF) == 0){
						new AlertDialog.Builder(pContext).setTitle("Envoi impossible!").setMessage("Le mod�le a d�j� �t� modifi� par un autre utilisateur. Mettez � jour votre base de flipper pour voir les derni�res modifications.").setNeutralButton("Fermer", null).setIcon(R.drawable.ic_delete).show();
					}else{
						
						// On met � jour l'ancien flipper avec l'�tat et la date de m�j
						objects.get(0).put(FlipperDatabaseHandler.FLIPPER_DATMAJ, ancienflipper.getDateMaj());
						objects.get(0).put(FlipperDatabaseHandler.FLIPPER_ACTIF, 0);
	
						// On cr�� l'objet du nouveau flipper
						ParseObject parseNouveauFlipper = new ParseObject(FlipperDatabaseHandler.FLIPPER_TABLE_NAME);
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_ACTIF, 1);
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_DATMAJ, nouveauFlipper.getDateMaj());
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_ENSEIGNE, nouveauFlipper.getIdEnseigne());
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_ID, nouveauFlipper.getId());
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_MODELE, nouveauFlipper.getIdModele());
						parseNouveauFlipper.put(FlipperDatabaseHandler.FLIPPER_NB_CREDITS_2E, "");
						
						// On met le tout dans une liste
						List<ParseObject> listParseToSave = new ArrayList<ParseObject>();
						listParseToSave.add(parseNouveauFlipper);
						listParseToSave.add(objects.get(0));
						
						// On met �ventuellement le nouveau commentaire
						if (commentaire != null){
							ParseObject parseNouveauCommentaire = new ParseObject(FlipperDatabaseHandler.COMMENTAIRE_TABLE_NAME);
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_ACTIF, 1);
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_DATE, commentaire.getDate());
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_FLIPPER_ID, commentaire.getFlipperId());
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_ID, commentaire.getId());
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_PSEUDO, commentaire.getPseudo());
							parseNouveauCommentaire.put(FlipperDatabaseHandler.COMM_TEXTE, commentaire.getTexte());
							
							listParseToSave.add(parseNouveauCommentaire);
						}
						
						
						// Et on balance in da cloud!
						ParseObject.saveAllInBackground(listParseToSave, new SaveCallback(){
							@Override
							public void done(ParseException e) {
								if (e == null){
									
									// Ca s'est bien pass�, on sauvegarde les flippers
									List<Flipper> listBaseToSave = new ArrayList<Flipper>();
									listBaseToSave.add(nouveauFlipper);
									listBaseToSave.add(ancienflipper);
									
									BaseFlipperService baseFlipperService = new BaseFlipperService();
									baseFlipperService.majListeFlipper(listBaseToSave, pContext);
									
									// Et �ventuellement le commentaire
									if(commentaire != null){
										BaseCommentaireService baseCommentaireService = new BaseCommentaireService();
										baseCommentaireService.addCommentaire(commentaire, pContext);
									}
									Toast toast = Toast.makeText(pContext, pContext.getResources().getString(R.string.popupChangementModeleOK), Toast.LENGTH_SHORT);
									toast.show();
									if (mFragmentCallback != null){
										mFragmentCallback.onTaskDone();
									}
								}else{
									Toast toast = Toast.makeText(pContext, pContext.getResources().getString(R.string.popupChangementModeleKO), Toast.LENGTH_SHORT);
									toast.show();
								}
							}
						});
					}
				}
			}
		});
		
		return true;
	}
}
