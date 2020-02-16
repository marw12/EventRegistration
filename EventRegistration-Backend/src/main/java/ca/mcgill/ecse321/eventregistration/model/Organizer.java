package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Organizer extends Person{
		

	private Set<Event> organizedEvent;
	
	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Event> getOrganizes() {
	   return this.organizedEvent;
	}

	public void setOrganizes(Set<Event> organizedEvent) {
	   this.organizedEvent = organizedEvent;
	}


}
