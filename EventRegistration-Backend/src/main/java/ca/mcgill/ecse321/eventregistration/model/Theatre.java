package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;

@Entity
public class Theatre extends Event {
	private String title;
	
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}	
}