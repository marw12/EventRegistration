package ca.mcgill.ecse321.eventregistration.dto;

public class PaypalDto {
	
	private String email;
	private int amount;

	public PaypalDto() {

	}

	public PaypalDto(String email, int amount) {
		super();
		this.email = email;
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

//	private String deviceId;
//	private int amount;
//
//	public PaypalDto() {
//	}
//
//	public PaypalDto(String deviceId, int amount) {
//		this.deviceId = deviceId;
//		this.amount = amount;
//	}
//
//	public String getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}
//
//	public int getAmount() {
//		return amount;
//	}
//
//	public void setAmount(int amount) {
//		this.amount = amount;
//	}

}
