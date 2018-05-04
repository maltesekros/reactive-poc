package com.tipico.poc.reactive.dto;

public class PersonDTO {

	private String name;
	private String surname;
	private int age;
	private String houseName;
	private String street;
	private String city;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFullName() {
		return this.name + " " + this.surname;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "PersonDTO{" +
			"name='" + name + '\'' +
			", surname='" + surname + '\'' +
			", age=" + age +
			", houseName='" + houseName + '\'' +
			", street='" + street + '\'' +
			", city='" + city + '\'' +
			'}';
	}
}
