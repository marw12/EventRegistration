package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Paypal{
   private String email;

public void setEmail(String value) {
    this.email = value;
}
public String getEmail() {
    return this.email;
}
private int amount;

public void setAmount(int value) {
    this.amount = value;
}
public int getAmount() {
    return this.amount;
}

private int deviceID;

public void setDeviceID(int deviceID) {
    this.deviceID = deviceID;
}
@Id
@GeneratedValue
public int getDeviceID() {
    return this.deviceID;
}
}