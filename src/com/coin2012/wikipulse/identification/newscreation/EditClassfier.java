package com.coin2012.wikipulse.identification.newscreation;



import com.coin2012.wikipulse.models.WikiEdit;

public class EditClassfier  extends  AbstractClassifier{

	@Override
	public boolean classifyWikiEdit(WikiEdit wikiEdit) {
		return isEditNewsWorthy(wikiEdit) && isEditorANewsCreator(wikiEdit) ? true: false;
	}
	
}
