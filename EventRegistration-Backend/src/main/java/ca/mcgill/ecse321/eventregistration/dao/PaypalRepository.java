package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Paypal;

public interface PaypalRepository extends CrudRepository<Paypal, Integer>{
	public Paypal findByDeviceID(int deviceId);
	public Paypal findByEmail(String email);
	boolean existsByEmail(String email);
}
