package com.osu.insecurity;

import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity {

	/**
	 * Below are the controls for the forgot password menu
	 * forgotPassword_emailAddressEditText = email address edit text
	 * forgotPassword_sendPassword = send password button
	 * forgotPassword_backButton = back text view
	 */
	private EditText forgotPassword_emailAddressEditText;
	private Button forgotPassword_sendPassword;
	private Button forgotPassword_backButton;
	

	/**
	 * The successful password sent Toast string
	 */
	private static final String FORGOT_PASSWORD_EMAIL_SENT = "Password sent to email";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotPassword();
	}
	
	/**
	 * Changes the screen to the forgot password screen
	 */
	private void forgotPassword()
	{
		setContentView(R.layout.forgot_password_layout);
		
		forgotPassword_emailAddressEditText = (EditText) findViewById(R.id.forgotPassword_email_address_edit_text);
		forgotPassword_sendPassword = (Button) findViewById(R.id.forgotPassword_send_password_button);
		forgotPassword_backButton = (Button) findViewById(R.id.forgotPassword_back_button);
		
		//sets up the interaction when the user clicks the register button
		forgotPassword_sendPassword.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(passwordSent())
				{
					Toast.makeText(getApplicationContext(), FORGOT_PASSWORD_EMAIL_SENT, Toast.LENGTH_SHORT).show();
					Login.forgotEmail = forgotPassword_emailAddressEditText.getText().toString();
					forgotPassword_emailAddressEditText.setText("");
					Login.updateFields();
					finish();
				}
			}	
		});
				
		//sets up the interaction when the user clicks the back text
		forgotPassword_backButton.setOnClickListener(new View.OnClickListener() 
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
	 * 
	 * @return true if the password was sent, otherwise return false
	 */
	private boolean passwordSent()
	{
		Profile pi = null;
		boolean success = false;
		if(forgotPassword_emailAddressEditText.getText().toString().matches(Login.EMAIL_PATTERN))
		{
			Iterator<Profile> iter =  Login.database.iterator();
		    while(iter.hasNext()) //iterate over the keys and values and print to the database file
		    {
		    	Profile nextPI = iter.next();
		    	if(nextPI.getEmail().equals(forgotPassword_emailAddressEditText.getText().toString()))
		    	{
		    		pi = nextPI;
		    		success = true;
		    	}
		    }
		    
		    if(pi != null && success)
			{
				//send the password to that email
		    	String email [] = new String[] {forgotPassword_emailAddressEditText.getText().toString()}; 
		    	Intent emailIntent = new Intent (Intent.ACTION_SEND);
		    	emailIntent.putExtra (Intent.EXTRA_EMAIL, email);
		    	emailIntent.putExtra (Intent.EXTRA_SUBJECT, "Password Request");
		    	emailIntent.putExtra (Intent.EXTRA_TEXT, "Hi " + pi.getName() + ", your password is " + pi.getPassword());
		    	emailIntent.setType ("message/rfc822");
		    	try {
		    	    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		    	} catch (android.content.ActivityNotFoundException ex) {
		    	    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		    	}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "There is no account associated with that email", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), Register.REGISTER_REGISTRATION_INVALID, Toast.LENGTH_SHORT).show();
		}

		return success;
	}
	
}
