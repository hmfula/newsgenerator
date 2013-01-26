package com.coin2012.wikipulse.identification.newscreation;



import com.coin2012.wikipulse.models.WikiEdit;

public class WikiEditClassfier  extends  AbstractClassifier{

	@Override
	public boolean classify(Object classifiable) {
		WikiEdit wikiEdit;
		if(classifiable instanceof WikiEdit){
			wikiEdit = (WikiEdit)classifiable;
		}
		else{
			throw new RuntimeException("Unrecogined object input into the classifier's classfify method.");
		}
		return isEditNewsWorthy(wikiEdit) && isEditorANewsCreator(wikiEdit) ? true: false;
	}
	
}
