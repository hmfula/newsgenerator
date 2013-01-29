package com.coin2012.wikipulse.models;

import java.util.List;

/**
 * Represents a Wikipedia Editor. For simplicity all variables are treated as
 * strings- can be changed if needed.
 * 
 * @since 30-12-2012
 */
public class Editor{
	String userid;
	String user;
	String editcount;
	String registration;
	String title;
	List<String> groups;

	// TODO clean up?
	public Editor() {
	}

	public Editor(String userid, String user, String editcount, String registration, List<String> groups) {
		setUserid(userid);
		setUser(user);
		setEditcount(editcount);
		setTitle(title);
		setGroups(groups);
		setRegistration(registration);
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

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

}
