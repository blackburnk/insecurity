package com.osu.insecurity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	/**
	 * Below are the controls for the register menu
	 * register_registerButton = register button
	 * register_backButton = back text view
	 * register_emailAddressEditText = email address edit text
	 * register_confirmEmailAddressEditText = confirm email address edit text
	 * register_passwordEditText = password edit text
	 * register_confirmPasswordEditText = confirm password edit text
	 * register_securityQuestionEditText = security question edit text
	 */
	private Button register_registerButton;
	private Button register_backButton;
	private EditText register_nameEditText;
	private EditText register_emailAddressEditText;
	private EditText register_confirmEmailAddressEditText;
	private EditText register_passwordEditText;
	private EditText register_confirmPasswordEditText;
	private EditText register_securityQuestionEditText;
	
	/**
	 * The email address already taken Toast string
	 */
	private final String REGISTER_TAKEN_EMAIL_ADDRESS = "This email address is already taken";
	
	/**
	 * The successful registration Toast string
	 */
	private final String REGISTER_REGISTRATION_SUCCESSFUL = "Registration successful";
	
	/**
	 * The invalid registration Toast string
	 */
	protected final static String REGISTER_REGISTRATION_INVALID = "Invalid registration/email";
	
	/**
	 * The newly registered profile
	 */
	private Profile newPI;
	
	/**
	 * Below are the fields for the Toast's when the user is attempting to register
	 */
	private final String MUST_ENTER_NAME = "Must enter a name";
	private final String ENTER_PASSWORD = "Must enter a password";
	private final String CONFIRM_PASSWORD = "Must confirm password";
	private final String ENTER_EMAIL = "Must enter an email address";
	private final String CONFIRM_EMAIL = "Must confirm email";
	private final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
	private final String EMAILS_DO_NOT_MATCH = "Emails do not match";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		register();
	}
	/**
	 * 
	 * @return true if the registration is successful, otherwise return false
	 */
	private boolean registrationSuccessful()
	{
		boolean success = false;
		
		if(register_nameEditText.getText().toString().length() != 0)
		{
			if(register_emailAddressEditText.getText().toString().length() != 0) //check to see if the user has entered an email address
			{
				if(register_confirmEmailAddressEditText.getText().toString().length() != 0 ) //check to see if the user has entered a confirmed email
				{
					if(register_passwordEditText.getText().toString().length() != 0) //check to see if the user has entered a password
					{
						if(register_confirmPasswordEditText.getText().toString().length() != 0 ) //check to see if the user has entered a confirmed password
						{
							if(register_securityQuestionEditText.getText().toString().length() != 0) //check to see if the user has entered an answer to the security question
							{
								if(register_emailAddressEditText.getText().toString().equals(register_confirmEmailAddressEditText.getText().toString()))
								{
									if(register_passwordEditText.getText().toString().equals(register_confirmPasswordEditText.getText().toString()))
									{
										if(register_emailAddressEditText.getText().toString().matches(Login.EMAIL_PATTERN))
										{
											//check database for taken email address or register 
											Profile request = null;
											Iterator<Profile> iter = Login.database.iterator();
											String email = register_emailAddressEditText.getText().toString();
											String password = register_passwordEditText.getText().toString();
											String name = register_nameEditText.getText().toString();
											String securityAnswer = register_securityQuestionEditText.getText().toString();
											while(iter.hasNext())
											{
												Profile next = iter.next();
												if(next.getEmail().equals(email)) //check the email with the data base to check for existence
												{
													request = next;
												}
											}
											
											if(request == null)
											{
												//save information
												success = true;
												newPI = new Profile();
												newPI.setEmail(email);
												newPI.setPassword(password);
												newPI.setName(name);
												newPI.setSecurityQuestionAnswer(securityAnswer);
											}
											else
											{
												Toast.makeText(getApplicationContext(), REGISTER_TAKEN_EMAIL_ADDRESS, Toast.LENGTH_SHORT).show();
											}
											
										}
										else //have not entered a valid email address
										{
											Toast.makeText(getApplicationContext(), REGISTER_REGISTRATION_INVALID, Toast.LENGTH_SHORT).show();
										}
									}
									else //passwords do not match
									{
										Toast.makeText(getApplicationContext(), PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_SHORT).show();
									}
								}
								else //email addresses do not match
								{
									Toast.makeText(getApplicationContext(), EMAILS_DO_NOT_MATCH, Toast.LENGTH_SHORT).show();
								}
							}
							else //didn't enter an answer to security question
							{
								Toast.makeText(getApplicationContext(), EMAILS_DO_NOT_MATCH, Toast.LENGTH_SHORT).show();
							}
						}
						else //didn't enter a confirmed password
						{
							Toast.makeText(getApplicationContext(), CONFIRM_PASSWORD, Toast.LENGTH_SHORT).show();
						}
					}
					else //didn't enter a password
					{
						Toast.makeText(getApplicationContext(), ENTER_PASSWORD, Toast.LENGTH_SHORT).show();
					}
				}
				else //didn't enter a confirmed email
				{
					Toast.makeText(getApplicationContext(), CONFIRM_EMAIL, Toast.LENGTH_SHORT).show();
				}
			}
			else //didn't enter an email
			{
				Toast.makeText(getApplicationContext(), ENTER_EMAIL, Toast.LENGTH_SHORT).show();
			}
		}
		else //didn't enter a name
		{
			Toast.makeText(getApplicationContext(), MUST_ENTER_NAME, Toast.LENGTH_SHORT).show();
		}
		return success;
	}
	
	/**
	 * Changes the screen to the register registration page
	 */
	private void register()
	{
		setContentView(R.layout.register_layout);
		
		register_registerButton = (Button) findViewById(R.id.register_register_button);
		register_backButton = (Button) findViewById(R.id.register_back_button);
		register_nameEditText = (EditText) findViewById(R.id.register_name_edit_text);
		register_emailAddressEditText = (EditText) findViewById(R.id.register_email_address_edit_text);
		register_confirmEmailAddressEditText = (EditText) findViewById(R.id.register_confirm_email_address_edit_text);;
		register_passwordEditText = (EditText) findViewById(R.id.register_password_edit_text);
		register_confirmPasswordEditText = (EditText) findViewById(R.id.register_confirm_password_edit_text);
		register_securityQuestionEditText = (EditText) findViewById(R.id.register_security_question_edit_text);
		
		//sets up the interaction when the user clicks the register button
		register_registerButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(registrationSuccessful())
				{
					addDistressPassword();
				}
			}	
		});
		
		//sets up the interaction when the user clicks the back text
		register_backButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//go back to the login menu
				finish();
			}	
		});
	}
	
	/**
	 * Sets an alert dialog box to enter a distress signal/password
	 */
	/*
	 * 
	 *  add field on the register page for security question and text field to enter that
	 *  
	private void CreateAccount() {
		String username = getUserName.getText().toString();
		String password = getPassword.getText().toString();
		String confirm = getPasswordConfirm.getText().toString();
		String security = getSecurityQuestion.getText().toString();
		String codeword = getCodeword.getText().toString();
		if ((password.equals(confirm)) && (!username.equals(""))
				&& (!password.equals("")) && (!confirm.equals("")) && (!security.equals("")) && (!codeword.equals(""))) {
			this.dh = new DatabaseHelper(this);
			List<String> parameters = new ArrayList<String>();
			parameters.add(username);
			parameters.add(password);
			parameters.add(codeword);
			parameters.add(security);
			parameters.add("null");
			parameters.add("null");

			String exists = "SELECT * FROM CUSTOMER WHERE Customer_ID=?";
			String[] existParams = new String[1];
			existParams[0] = username;

			/* checking to see if they exist 
			if (!(this.dh.selectAll(exists, existParams).size() > 0)) {
				String query = "INSERT INTO CUSTOMER VALUES(?,?,?,?,?,?)";
				this.dh.insert(query, parameters);

				Toast.makeText(Account.this, "SUCCESS! Account created.",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Account.this, "Account Already Exists", Toast.LENGTH_LONG).show();
			}
			finish();
		} else if ((username.equals("")) || (password.equals(""))
				|| (confirm.equals(""))) {
			Toast.makeText(Account.this, "ERROR! Missing entry.",
					Toast.LENGTH_SHORT).show();
		} else if (!password.equals(confirm)) {
			new AlertDialog.Builder(this)
					.setTitle("ERROR!")
					.setMessage("Please make sure the passwords match")
					.setNeutralButton("Try Again",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		}
	}
	*/
	private void addDistressPassword()
	{
		 final EditText input = new EditText(this);
		 new AlertDialog.Builder(this)
	        .setView(input)
	        .setTitle("Enter Distress Password")
	        .setCancelable(false)
	        .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int which) {
                	
                	String distress = input.getText().toString();
                	if(distress.length() > 0)
                	{
                		newPI.setDistressPassword(distress);
                		Login.registeredEmail = register_emailAddressEditText.getText().toString();
    					Login.registeredPassword = register_passwordEditText.getText().toString();
    					register_emailAddressEditText.setText("");
    					register_confirmEmailAddressEditText.setText("");
    					register_passwordEditText.setText("");
    					register_confirmPasswordEditText.setText("");
    					Login.database.add(newPI);
                		Login.updateFields();
    					Toast.makeText(getApplicationContext(), REGISTER_REGISTRATION_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                		d.dismiss();
                		finish();
                	}
                }
                })
            .create().show();
	}
}
