package ca.mcgill.ecse321.eventregistration.dto;

public class PayDto {

	private RegistrationDto registration;
	private PaypalDto paypal;

	public PayDto() {
	}

	public PayDto(RegistrationDto registration, PaypalDto paypal) {
		this.registration = registration;
		this.paypal = paypal;
	}
	
	public RegistrationDto getRegistration() {
		return registration;
	}
	
	public void setRegistration (RegistrationDto registration) {
		this.registration = registration;
	}

	public PaypalDto getPaypal() {
		return paypal;
	}
	
	public void setPaypal (PaypalDto paypal) {
		this.paypal = paypal;
	}

}
