package com.coin2012.wikipulse.models;

/**
 * Represents a Wikipedia Editor. For simplicity all variables are treated as
 * strings- can be changed if needed.
 * 
 * @since 30-12-2012
 */
public class Editor {
	String userid;
	String user;
	String editcount;

	public Editor() {

	}

	public Editor(String userid, String user) {
		this.userid = userid;
		this.user = user;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String name) {
		this.user = name;
	}

	public String getEditcount() {
		return editcount;
	}

	public void setEditcount(String editcount) {
		this.editcount = editcount;
	}
}
