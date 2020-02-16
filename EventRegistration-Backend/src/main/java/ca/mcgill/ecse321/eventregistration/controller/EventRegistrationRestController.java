package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController { 

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	
	//http://localhost:8080/events/theatre/testevent?date=2013-10-23&startTime=00:00&endTime=23:59&title=kewl
	@PostMapping(value = { "/events/theatre/{name}", "/events/theatre/{name}/" })
	public TheatreDto createTheatre(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, @RequestParam String title)
			throws IllegalArgumentException {
		// @formatter:on
		Theatre theatre = service.createTheatre(name, date, Time.valueOf(startTime), Time.valueOf(endTime), title);
		return convertToDto(theatre);
	}
	
	//http://localhost:8080/paypal/marwan.khan12@gmail.com/?amount=69
	@PostMapping(value = { "/paypal/{deviceId}", "/paypal/{deviceId}/" })
	public PaypalDto createPaypal(@PathVariable("deviceId") String deviceId, @RequestParam int amount) throws IllegalArgumentException {
		Paypal paypal = service.createPaypalPay(deviceId, amount);
		return convertToDto(paypal);
	}
	
//	@PostMapping(value = { "/registration/pay", "/registration/pay/" })
//	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
//			@RequestParam(name = "event") EventDto eDto, @RequestParam(name = "email", required = false) String email, @RequestParam(name = "amount") int amount 
//			) throws IllegalArgumentException {
//		// @formatter:on
//
//		// Both the person and the event are identified by their names
//		Person p = service.getPerson(pDto.getName());
//		Event e = service.getEvent(eDto.getName());
//
//		Registration r = service.getRegistrationByPersonAndEvent(p, e);
//		if (r == null) {
//			throw new IllegalArgumentException("the person has not yet registered to the event");
//		}
//		
//		RegistrationDto rDto;
//		if(email != null && !email.isEmpty()) {
//			Paypal paypal = service.createPaypalPay(email, amount);
//			r = service.pay(r, paypal);
//			rDto = convertToDto(r);
//		}else {
//			throw new IllegalArgumentException("one payment type should have been specified");
//		}
//
//		return rDto;
//	}
	
	//http://localhost:8080/organizers/Marwan
	@PostMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto createOrganizer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Organizer organizer = service.createOrganizer(name);
		return convertToDto(organizer);
	}
	
	//http://localhost:8080/assign/?organizer=Marwan&event=testevent
	@PostMapping(value = { "/assign", "/assign/" })
	public RegistrationDto registerOrganizerForEvent(@RequestParam(name = "organizer") OrganizerDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the Organizer and the event are identified by their names
		Organizer p = service.getOrganizer(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	
	@GetMapping(value = { "/events/theatre", "/events/theatre/" })
	public List<TheatreDto> getAllTheatres() {
		List<TheatreDto> theatreDtos = new ArrayList<>();
		for (Theatre theatre : service.getAllTheatres()) {
			theatreDtos.add(convertToDto(theatre));
		}
		return theatreDtos;
	}
	
	@GetMapping(value = { "/paypal/{email}", "/paypal/{email}/" })
	public PaypalDto getPaypalById(@PathVariable("email") String email) throws IllegalArgumentException {
		return convertToDto(service.getPaypal(email));
	}
	
	@GetMapping(value = { "/paypal", "/paypal/" })
	public List<PaypalDto> getAllPayments() {
		List<PaypalDto> paypals = new ArrayList<>();
		for (Paypal paypal : service.getAllPayments()) {
			paypals.add(convertToDto(paypal));
		}
		return paypals;
	}
	
	@GetMapping(value = { "/organizers/{name}", "/organizers/{name}/" })
	public OrganizerDto getOrganizerByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getOrganizer(name));
	}
	
	//http://localhost:8080/assignations/?organizer=Marwan&event=testevent
	@GetMapping(value = { "/assignations", "/assignations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "organizer") OrganizerDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Organizer p = service.getOrganizer(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByOrganizerAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}
	
	//http://localhost:8080/assignations/organizer/Marwan
	@GetMapping(value = { "/assignations/organizer/{name}", "/assignations/organizer/{name}/" })
	public List<RegistrationDto> getRegistrationsForOrganizer(@PathVariable("name") OrganizerDto pDto)
			throws IllegalArgumentException {
		// Both the organizer and the event are identified by their names
		Organizer p = service.getOrganizer(pDto.getName());
		return createRegistrationDtosForOrganizer(p);
	}
	
	@GetMapping(value = { "/organizers", "/organizers/" })
	public List<OrganizerDto> getAllOrganizers() {
		List<OrganizerDto> organizers = new ArrayList<>();
		for (Organizer organizer : service.getAllOrganizers()) {
			organizers.add(convertToDto(organizer));
		}
		return organizers;
	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
//		rDto.setPaypal(convertToDto(r.getPaypal()));
		return rDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}
	

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}
	
	private TheatreDto convertToDto(Theatre e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Theatre!");
		}
		TheatreDto eventDto = new TheatreDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getTitle());
		return eventDto;
	}
	
//	private PaypalDto convertToDto(Paypal a) {
//		if (a == null) {
//			throw new IllegalArgumentException("There is no such Payment!");
//		}
//		PaypalDto paypalDto = new PaypalDto(a.getDeviceID(), a.getAmount());
//		return paypalDto;
//	}
	
	private PaypalDto convertToDto(Paypal paypal) {
		if (paypal == null) {
			throw new IllegalArgumentException("There is no such Payment!");
		}
		PaypalDto paypalDto = new PaypalDto(paypal.getEmail(), paypal.getAmount());
		return paypalDto;
	}
	
	private OrganizerDto convertToDto(Organizer p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Organizer!");
		}
		OrganizerDto organizerDto = new OrganizerDto(p.getName());
		organizerDto.setEventsAttended(createAttendedEventDtosForOrganizer(p));
		organizerDto.setEventsOrganized(createOrganizedEventDtosForOrganizer(p));
		return organizerDto;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
	
//	private List<PaypalDto> createPaypalPaidDtosForPerson(Person p) {
//		List<Paypal> paidForPerson = service.getPaidByPerson(p);
//		List<PaypalDto> payments = new ArrayList<>();
//		for (Paypal payment : paidForPerson) {
//			payments.add(convertToDto(payment));
//		}
//		return payments;
//	}
	
	private List<EventDto> createAttendedEventDtosForOrganizer(Organizer p) {
		List<Event> eventsForOrganizer = service.getEventsAttendedByOrganizer(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForOrganizer) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
	private List<EventDto> createOrganizedEventDtosForOrganizer(Organizer p) {
		List<Event> eventsForOrganizer = service.getEventsOrganizedByOrganizer(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForOrganizer) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
	private List<RegistrationDto> createRegistrationDtosForOrganizer(Organizer p) {
		List<Registration> registrationsForOrganizer = service.getRegistrationsForOrganizer(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForOrganizer) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}

	
	
}
