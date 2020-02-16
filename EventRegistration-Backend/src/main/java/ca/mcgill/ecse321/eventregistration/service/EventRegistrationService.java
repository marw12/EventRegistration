package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private TheatreRepository theatreRepository;
	@Autowired
	private PaypalRepository payPalRepository;
	@Autowired
	private OrganizerRepository organizerRepository;

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}
	
	@Transactional
	public List<Paypal> getPaidByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Paypal> paidByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			paidByPerson.add(r.getPaypal());
		}
		return paidByPerson;
	}
	
	/*
	 * ============================
	 * START OF THEATRE METHODS
	 * ============================
	 */	
	
	@Transactional
	public Theatre createTheatre(String name, Date theatreDate, Time startTime, Time endTime, String title) {
		
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty!";
		}
		if (theatreDate == null) {
			error = error + "Event date cannot be empty!";
		} 
		if (startTime == null) {
			error = error + "Event start time cannot be empty!";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty!";
		} else if ((endTime.before(startTime))) {
			error = error + "Event end time cannot be before event start time!";
			if (endTime != null && startTime != null && endTime.before(startTime))
			throw new IllegalArgumentException("Event end time cannot be before event start time!");
		}
		if (title == null || title.trim().length() == 0) {
			error = error + "Theatre title cannot be empty!";
		}
		if(eventRepository.findByName(name) != null) {
			error = error + "Event already exists!";
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		Theatre event = new Theatre();
		event.setName(name);
		event.setDate(theatreDate);
		event.setStartTime(startTime); //convert Time to LocalTime
		event.setEndTime(endTime);
		event.setTitle(title);
		
		theatreRepository.save(event);
		return event;
	}
	
	@Transactional
	public List<Theatre> getAllTheatres() {
		return toList(theatreRepository.findAll());
	}
	
	/*
	 * ============================
	 * END THEATRE SERVICE METHODS
	 * ============================
	 */	
	
	/*
	 * ============================
	 * START OF PAYPAL METHODS
	 * ============================
	 */	
	
	@Transactional
	public Paypal createPaypalPay(String email, int amount) {
		
		
		if (email == null || email.matches(".+@.+\\..+")==false) {
			throw new IllegalArgumentException("ID is null or has wrong format!");
		}else if (amount < 0) {
			throw new IllegalArgumentException("Payment amount cannot be negative!");
		}else if(payPalRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("Paypal has already been created!");
		}	
		
		
		Paypal payPal = new Paypal();
		payPal.setEmail(email);
		payPal.setAmount(amount);
	
		payPalRepository.save(payPal);

		return payPal;
	}
	
	@Transactional
	public Paypal getPaypal(String email) {
		if (email == null || email.trim().length() == 0) {
			throw new IllegalArgumentException("Email cannot be empty!");
		}
		Paypal id = payPalRepository.findByEmail(email);
		return id;
	}
	
	@Transactional
	public List<Paypal> getAllPayments() {
		return toList(payPalRepository.findAll());
	}
	
	@Transactional 
	public Registration pay(Registration registration, Paypal paypal) {
		String error = "";
		
		if (registration == null || paypal == null) {
			error = error + "Registration and payment cannot be null!";
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		registration.setPaypal(paypal);
		registrationRepository.save(registration);	
		
		return registration;
	}
	
	

	
	 
	/*
	 * ============================
	 * END OF PAYPAL METHODS
	 * ============================
	 */	
	
	/*
	 * ============================
	 * START OF ORGANIZER METHODS
	 * ============================
	 */	
	
	@Transactional
	public Organizer createOrganizer (String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Organizer name cannot be empty!");
		} else if (organizerRepository.existsById(name)) {
			throw new IllegalArgumentException("Organizer has already been created!");
		}
		Organizer organizer = new Organizer();
		organizer.setName(name);
		organizerRepository.save(organizer);
		return organizer;
	}
	
	@Transactional
	public List<Organizer> getAllOrganizers () {
		return toList(organizerRepository.findAll());
	}
	
	@Transactional
	public Organizer getOrganizer (String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		
		Organizer organizer = organizerRepository.findByName(name);
		
		return organizer;
	}
	
	public void organizesEvent(Organizer organizer, Event event) {
		String error = "";
		if (organizer == null) {
			error = error + "Organizer needs to be selected for organizes! ";
		} else if (organizer.getOrganizes() == null) {
		Set<Event> organizedEvents = new HashSet<Event>();
			organizedEvents.add(event);
			organizer.setOrganizes(organizedEvents);
		}
		if (event == null) {
			error = error + "Event needs to be selected for organizes!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		organizerRepository.save(organizer);

	}
		
	@Transactional
	public List<Event> getEventsAttendedByOrganizer(Organizer organizer) {
		if (organizer == null) {
			throw new IllegalArgumentException("organizer cannot be null!");
		}
		List<Event> eventsAttendedByOrganizer = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(organizer)) {
			eventsAttendedByOrganizer.add(r.getEvent());
		}
		return eventsAttendedByOrganizer;
	}
	
	@Transactional
	public List<Event> getEventsOrganizedByOrganizer(Organizer organizer) {
		if (organizer == null) {
			throw new IllegalArgumentException("organizer cannot be null!");
		}
		List<Event> eventsOrganizededByOrganizer = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(organizer)) {
			eventsOrganizededByOrganizer.add(r.getEvent());
		}
		return eventsOrganizededByOrganizer;
	}
	
	@Transactional
	public Registration getRegistrationByOrganizerAndEvent(Organizer organizer, Event event) {
		if (organizer == null || event == null) {
			throw new IllegalArgumentException("organizer or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(organizer, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForOrganizer(Organizer organizer){
		if(organizer == null) {
			throw new IllegalArgumentException("organizer cannot be null!");
		}
		return registrationRepository.findByPerson(organizer);
	}

	
	/*
	 * ============================
	 * END OF ORGANIZER METHODS
	 * ============================
	 */	


	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
