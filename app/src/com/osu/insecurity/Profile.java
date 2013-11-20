package com.osu.insecurity;

/**
 * Public class that holds the profile information of the user
 * 
 *
 */
public class Profile {
	
	private String email;
	private String name;
	private String password;
	private String distressPassword;
	private String securityQuestionAnswer;
	
	/**
	 * Public constructor for a users Profile Information
	 */
	public Profile()
	{
		email = "";
		name = "";
		password = "";
		distressPassword = "";
		securityQuestionAnswer = "";
	}
	
	/**
	 * Public constructor for a users Profile Information
	 * @param name
	 * 				name of user
	 * @param email
	 * 				email of user
	 * @param password
	 * 				password of user
	 * @param disressPassword
	 * 				distress password of user
	 * @param securityQuestionAnswer
	 * 				security question answer of the user
	 */
	public Profile(String name, String email, String password, String distressPassword, String securityQuestionAnswer)
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.distressPassword = distressPassword;
		this.securityQuestionAnswer = securityQuestionAnswer;
	}
	
	/**
	 * Sets the name of the user
	 * @param name
	 * 			name of the user
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the email of the user
	 * @param email
	 * 			email of the user
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * Sets the password of the user
	 * @param password
	 * 			password of the user
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	/**
	 * Sets the distress password of the user
	 * @param distress password
	 * 			distress password of the user
	 */
	public void setDistressPassword(String distressPassword)
	{
		this.distressPassword = distressPassword;
	}
	
	/**
	 * Sets the security question answer of the user
	 * @param security question
	 * 			security question answer of the user
	 */
	public void setSecurityQuestionAnswer(String securityQuestion)
	{
		this.securityQuestionAnswer = securityQuestion;
	}
	
	/**
	 * Gets the name of the user
	 * @return
	 * 			the name of the user
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the email of the user
	 * @return
	 * 			the email of the user
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Gets the password of the user
	 * @return
	 * 			the password of the user
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Gets the distress password of the user
	 * @return
	 * 			the distress password of the user
	 */
	public String getDistressPassword()
	{
		return distressPassword;
	}
	
	/**
	 * Gets the security question answer of the user
	 * @return 
	 * 			the security question answer of the user
	 */
	public String getSecurityQuestionAnswer()
	{
		return securityQuestionAnswer;
	}
}
