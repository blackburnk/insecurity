package com.osu.insecurity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.osu.insecurity.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Login extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	/**
	 * The incorrect email address/password Toast string
	 */
	private static final String LOGIN_INCORRECT = "Incorrect email address/password";
	
	/**
	 * The email address already taken Toast string
	 */
	private static final String REGISTER_TAKEN_EMAIL_ADDRESS = "This email address is already taken";
	
	/**
	 * The successful registration Toast string
	 */
	private static final String REGISTER_REGISTRATION_SUCCESSFUL = "Registration successful";
	
	/**
	 * The invalid registration Toast string
	 */
	private static final String REGISTER_REGISTRATION_INVALID = "Invalid registration/email";
	
	/**
	 * The successful password sent Toast string
	 */
	private static final String FORGOT_PASSWORD_EMAIL_SENT = "Password sent to email";
	
	/**
	 * Regular Expression Email Pattern
	 */
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	/**
	 * Below are the controls of the Login menu
	 * login_emailAddressEditText = email address edit text box
	 * login_passwordEditText = password edit text box
	 * login_registerText = register text view
	 * login_forgotPasswordText = forgot password text view
	 * login_loginButton = login button
	 */
	private EditText login_emailAddressEditText;
	private EditText login_passwordEditText;
	private TextView login_registerText;
	private TextView login_forgotPasswordText;
	private Button login_loginButton;
	
	/**
	 * Below are the controls for the register menu
	 * register_registerButton = register button
	 * register_backText = back text view
	 * register_emailAddressEditText = email address edit text
	 * register_confirmEmailAddressEditText = confirm email address edit text
	 * register_passwordEditText = password edit text
	 * register_confirmPasswordEditText = confirm password edit text
	 */
	private Button register_registerButton;
	private TextView register_backText;
	private EditText register_emailAddressEditText;
	private EditText register_confirmEmailAddressEditText;
	private EditText register_passwordEditText;
	private EditText register_confirmPasswordEditText;
	
	/**
	 * Below are the controls for the forgot password menu
	 * forgotPassword_emailAddressEditText = email address edit text
	 * forgotPassword_sendPassword = send password button
	 * forgotPassword_backText = back text view
	 */
	private EditText forgotPassword_emailAddressEditText;
	private Button forgotPassword_sendPassword;
	private TextView forgotPassword_backText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		login();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}
	
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	/**
	 * Sets up the login screen
	 */
	private void login()
	{
		setContentView(R.layout.login_layout);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		login_emailAddressEditText = (EditText) findViewById(R.id.main_email_address_edit_text);
		login_passwordEditText = (EditText) findViewById(R.id.main_password_edit_text);
		login_registerText = (TextView) findViewById(R.id.main_register_text);
		login_forgotPasswordText = (TextView) findViewById(R.id.main_forgot_password_text);
		login_loginButton = (Button) findViewById(R.id.main_login_button);
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		
		//sets up the interaction when the user clicks the login button
		login_loginButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(loginSuccessful()) //check to see if the user entered correct email address/password
				{
					//go to the main screen
				}
				else
				{
					Toast.makeText(getApplicationContext(), LOGIN_INCORRECT, Toast.LENGTH_SHORT).show();
				}
			}	
		});
		
		//sets up the interaction when the user clicks the register text
		login_registerText.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				register();
			}	
		});
		
		//sets up the interaction when the user clicks the forgot password text
		login_forgotPasswordText.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				forgotPassword();
			}	
		});
	}
	/**
	 * 
	 * @return true if the email address/password is correct, otherwise return false
	 */
	private boolean loginSuccessful()
	{
		boolean success = true; //TODO change to false when we establish database/server
		if(login_emailAddressEditText.getText().toString().length() != 0) //check to see if the user has entered an email address
		{
			if(login_passwordEditText.getText().toString().length() != 0 ) //check to see if the user has entered a password
			{
				//check the email address/password with the data base
			}
		}
		return success;
	}
	
	/**
	 * 
	 * @return true if the registration is successful, otherwise return false
	 */
	private boolean registrationSuccessful()
	{
		boolean success = false;
				
		if(register_emailAddressEditText.getText().toString().length() != 0) //check to see if the user has entered an email address
		{
			if(register_confirmEmailAddressEditText.getText().toString().length() != 0 ) //check to see if the user has entered a confirmed email
			{
				if(register_passwordEditText.getText().toString().length() != 0) //check to see if the user has entered a password
				{
					if(register_confirmPasswordEditText.getText().toString().length() != 0 ) //check to see if the user has entered a confirmed password
					{
						if(register_emailAddressEditText.getText().toString().equals(register_confirmEmailAddressEditText.getText().toString()))
						{
							if(register_passwordEditText.getText().toString().equals(register_confirmPasswordEditText.getText().toString()))
							{
								if(register_emailAddressEditText.getText().toString().matches(EMAIL_PATTERN))
								{
									//check database for taken email address or register 
									/*
									if(!database.contains(register_emailAddressEditText.getText().toString())
									{
										//register the email
										success = true;
									}
									else
									{
										Toast.makeText(getApplicationContext(), REGISTER_TAKEN_EMAIL_ADDRESS, Toast.LENGTH_SHORT).show();
									}
									*/
								}
								else //have not entered a valid email address
								{
									Toast.makeText(getApplicationContext(), REGISTER_REGISTRATION_INVALID, Toast.LENGTH_SHORT).show();
								}
							}
							else //passwords do not match
							{
								Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
							}
						}
						else //email addresses do not match
						{
							Toast.makeText(getApplicationContext(), "Email addresses do not match", Toast.LENGTH_SHORT).show();
						}
					}
					else //didn't enter a confirmed password
					{
						Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
					}
				}
				else //didn't enter a password
				{
					Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
				}
			}
			else //didn't enter a confirmed email
			{
				Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
			}
		}
		else //didn't enter an email
		{
			Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
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
		register_backText = (TextView) findViewById(R.id.register_back_text);
		register_emailAddressEditText = (EditText) findViewById(R.id.register_email_address_edit_text);
		register_confirmEmailAddressEditText = (EditText) findViewById(R.id.register_confirm_email_address_edit_text);;
		register_passwordEditText = (EditText) findViewById(R.id.register_password_edit_text);
		register_confirmPasswordEditText = (EditText) findViewById(R.id.register_confirm_password_edit_text);
		
		//sets up the interaction when the user clicks the register button
		register_registerButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(registrationSuccessful())
				{
					Toast.makeText(getApplicationContext(), REGISTER_REGISTRATION_SUCCESSFUL, Toast.LENGTH_SHORT).show();
				}
			}	
		});
		
		//sets up the interaction when the user clicks the back text
		register_backText.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//go back to the login menu
				login();
			}	
		});
	}
	
	/**
	 * 
	 * @return true if the password was sent, otherwise return false
	 */
	private boolean passwordSent()
	{
		boolean success = false;
		
		if(forgotPassword_emailAddressEditText.getText().toString().matches(EMAIL_PATTERN))
		{
			//send the password to that email
			success = true;
		}
		else
		{
			Toast.makeText(getApplicationContext(), REGISTER_REGISTRATION_INVALID, Toast.LENGTH_SHORT).show();
		}

		return success;
	}
	
	/**
	 * Changes the screen to the forgot password screen
	 */
	private void forgotPassword()
	{
		setContentView(R.layout.forgot_password_layout);
		
		forgotPassword_emailAddressEditText = (EditText) findViewById(R.id.forgotPassword_email_address_edit_text);
		forgotPassword_sendPassword = (Button) findViewById(R.id.forgotPassword_send_password_button);
		forgotPassword_backText = (TextView) findViewById(R.id.forgotPassword_back_text);
		
		//sets up the interaction when the user clicks the register button
		forgotPassword_sendPassword.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(passwordSent())
				{
					Toast.makeText(getApplicationContext(), FORGOT_PASSWORD_EMAIL_SENT, Toast.LENGTH_SHORT).show();
				}
			}	
		});
				
				//sets up the interaction when the user clicks the back text
		forgotPassword_backText.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//go back to the login menu
				login();
			}	
		});
	}
	
	/**
	 * Creates the options menu and the bottom of the application.
	 */
	public boolean onCreateOptionsMenu(Menu menu)
 	{
 		super.onCreateOptionsMenu(menu);
 		//add an option to the menu and sets the properties
 		menu.add( 0,1, 1, "Exit Insecurity" )
        .setShortcut('1', 'e')
        .setIcon(android.R.drawable.ic_menu_close_clear_cancel);
 		return true;
 	}
	
	/**
	 * Handles the events when the options menu is selected and when the options on the menu are selected
	 */
 	public boolean onOptionsItemSelected(MenuItem item)
 	{
 		super.onOptionsItemSelected(item);
 		switch (item.getItemId()){
 		case 1:
 			//exits the application and kills all processes
 			finish();
 			android.os.Process.killProcess(android.os.Process.myPid());
 			return true;

 		}
 		return super.onOptionsItemSelected(item);
 	}
}
