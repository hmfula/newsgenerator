package com.coin2012.wikipulse.extraction;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Wikipedia Editor. For simplicity all variables are treated as strings- can be changed if needed.
 * 
 * @author mfula
 * @since 30-12-2012
 */
public class Editor implements Serializable {
	private static final long serialVersionUID = -6414500746332633332L;
	String userid;
	String name;
	String editcount;
	String registration;
	String title;
	List<String> groups;


	
	public Editor() {
	
	}

	public Editor(String userid, String name, String editcount,
			String registration, List<String> groups) {
		setUserid(userid);
		setName(name);
		setEditcount(editcount);
		setTitle(title);
		setGroups(groups);
		setRegistration(registration);
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
