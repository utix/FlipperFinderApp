package fr.fafsapp.flipper.finder.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import fr.fafsapp.flipper.finder.fragment.FragmentActionsFlipper.FragmentActionCallback;
import fr.fafsapp.flipper.finder.metier.Commentaire;
import fr.fafsapp.flipper.finder.metier.Flipper;
import fr.fafsapp.flipper.finder.service.base.BaseFlipperService;
import fr.fafsapp.flipper.finder.service.parse.ParseFlipperService;

public class FlipperService {

	private FragmentActionCallback mFragmentCallback;

	public FlipperService(FragmentActionCallback fragmentCallback) {
        mFragmentCallback = fragmentCallback;
    }
	
	public boolean remplaceToutFlipper(Context pContext){
		boolean retour = true;
		BaseFlipperService baseFlipperService = new BaseFlipperService();
		ParseFlipperService parseFlipperService = new ParseFlipperService(null);
		List<Flipper> nvlleListe = parseFlipperService.getAllFlipper();
		retour = baseFlipperService.majListeFlipper(nvlleListe, pContext);
		return retour;
	}

	
	public boolean valideFlipper(Context pContext, Flipper flipper){
		ParseFlipperService parseFlipperService = new ParseFlipperService(mFragmentCallback);
		final String dateToSave = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).format(new Date());
		parseFlipperService.updateDateFlipper(pContext, flipper, dateToSave);
		
		return true;
	}
	
	public boolean remplaceFlipper(Context pContext, Flipper flipper, long idNouveauModele, String commentaire, String pseudo){
		Date dateDuJour = new Date();

		String dateMaj = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).format(dateDuJour);
	
		flipper.setDateMaj(dateMaj);
		flipper.setActif(0);

		Flipper nouveauFlipper = new Flipper(dateDuJour.getTime(), idNouveauModele, 0, flipper.getIdEnseigne(), 1,
				dateMaj);
		Commentaire commentaireToAdd = null;
		if (commentaire != null && commentaire.length() > 0){
			commentaireToAdd = new Commentaire(dateDuJour.getTime(), dateDuJour.getTime(), commentaire, dateMaj,
					pseudo, 1);
		}
				
		ParseFlipperService parseFlipperService = new ParseFlipperService(mFragmentCallback);
		
		parseFlipperService.remplaceModeleFlipper(pContext, flipper, nouveauFlipper, commentaireToAdd);
		
		return true;
	}
	
}
