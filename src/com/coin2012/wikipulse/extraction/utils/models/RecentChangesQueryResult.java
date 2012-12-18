package com.coin2012.wikipulse.extraction.utils.models;

import java.util.ArrayList;
import java.util.List;


public class RecentChangesQueryResult {
	
	private String rcstart = null;
	private List<Change> changes = new ArrayList<Change>();
	

	public List<Change> getChanges() {
		return changes;
	}
	
	public String getRcstart(){
		return rcstart;
	}

	public void setRcstart(String rcstart) {
		this.rcstart = rcstart;
	}

}
