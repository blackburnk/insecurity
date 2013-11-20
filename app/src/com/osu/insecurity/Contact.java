package com.osu.insecurity;

public class Contact {
	
	private String name;
	private String phoneNumber;
	private String email;
	
	public Contact()
	{
		name = "";
		phoneNumber = "";
		email = "";
	}


	/**
	 * sets the value of the contacts name
	 * @param contactName
	 * 			the name of the contact
	 */
	public void setName(String contactName)
	{
		name = contactName;
	}

	/**
	 * 
	 * @return
	 * 			the value of the contacts name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * sets the value of the contacts phone number
	 * @param contactPhoneNumber
	 * 			the phone number of the contact
	 */
	public void setPhoneNumber(String contactPhoneNumber)
	{
		phoneNumber = contactPhoneNumber;
	}

	/**
	 * 
	 * @return
	 * 			the value of the contacts phone number
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * sets the value of the contacts email
	 * @param contactEmail
	 * 			the email of the contact
	 */
	public void setEmail(String contactEmail)
	{
		email = contactEmail;
	}

	/**
	 * 
	 * @return
	 * 			the value of the contacts email
	 */
	public String getEmail()
	{
		return email;
	}

}
