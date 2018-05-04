package com.tipico.poc.reactive.model;

public class Address {

	private int id;
	private int personId;
	private String houseName;
	private String streetName;
	private String city;

	public Address(int id, int personId, String houseName, String streetName,
		String city) {
		this.id = id;
		this.personId = personId;
		this.houseName = houseName;
		this.streetName = streetName;
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	@Override
	public String toString() {
		return "Address{" +
			"id=" + id +
			", personId=" + personId +
			", houseName='" + houseName + '\'' +
			", streetName='" + streetName + '\'' +
			", city='" + city + '\'' +
			'}';
	}
}
