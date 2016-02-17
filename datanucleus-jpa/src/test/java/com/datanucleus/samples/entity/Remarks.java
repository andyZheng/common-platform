package com.datanucleus.samples.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Remarks {

	private String name;
	
	private String content;
	
	@Override
	public String toString() {
		return "Remarks: { name : " + name + ", content : " + content + "}";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
