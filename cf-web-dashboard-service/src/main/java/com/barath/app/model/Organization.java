package com.barath.app.model;

import java.io.Serializable;
import java.util.List;

public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private List<String> spaces;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<String> spaces) {
		this.spaces = spaces;
	}

	public Organization() {
		super();

	}

}
