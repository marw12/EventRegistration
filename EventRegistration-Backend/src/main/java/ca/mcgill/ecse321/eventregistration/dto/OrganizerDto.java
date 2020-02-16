package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class OrganizerDto {

	private String name;
	private List<EventDto> eventsAttended;
	private List<EventDto> eventsOrganized;

	public OrganizerDto() {
	}

	@SuppressWarnings("unchecked")
	public OrganizerDto(String name) {
		this(name, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	public OrganizerDto(String name, List<EventDto> events, List<EventDto> organizedEvents) {
		this.name = name;
		this.eventsAttended = events;
		this.eventsOrganized = organizedEvents;
	}

	public String getName() {
		return name;
	}

	public List<EventDto> getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(List<EventDto> events) {
		this.eventsAttended = events;
	}
	
	public List<EventDto> getEventsOrganized() {
		return eventsOrganized;
	}

	public void setEventsOrganized(List<EventDto> organizedEvents) {
		this.eventsOrganized = organizedEvents;
	}
}