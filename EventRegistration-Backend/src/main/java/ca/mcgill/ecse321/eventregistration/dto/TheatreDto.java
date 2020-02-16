package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class TheatreDto {
	
	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	private String title;

	public TheatreDto() {
	}
	
	public TheatreDto(String name, String title) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"), title);
	}

	public TheatreDto(String name, Date date, Time startTime, Time endTime, String title) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	public String getTitle() {
		return title;
	}

}