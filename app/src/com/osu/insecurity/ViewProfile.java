package com.osu.insecurity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfile extends Activity {

	/***
	 * Below are all the controls for the View Profile activity
	 */
	private TextView viewProfile_greetingText;
	private EditText viewProfile_nameEditText;
	private Button viewProfile_changePasswordButton;
	private Button viewProfile_changeDistressButton;
	private Button viewProfile_addDeleteContactButton;
	private Button viewProfile_viewSavedDataButton;
	private Button viewProfile_backButton;
	private Button viewProfile_saveButton;
	private EditText viewProfile_alertDialog_oldDistressEditText;
	private EditText viewProfile_alertDialog_newDistressEditText;
	private EditText viewProfile_alertDialog_confirmNewDistressEditText;
	private EditText viewProfile_alertDialog_oldPasswordEditText;
	private EditText viewProfile_alertDialog_newPasswordEditText;
	private EditText viewProfile_alertDialog_confirmNewPasswordEditText;
	/**
	 * Below are the Strings that hold the value of any changes to the Profile
	 */
	private String password;
	private String distress;
	
	/**
	 * Boolean that determines if a change of password, name, or distress password happened
	 */
	private boolean nameChange;
	
	/**
	 * Below are the fields for the Toast's when the user doesn't enter correct information
	 * when attempting to change their password
	 */
	private final String ENTER_OLD_PASSWORD = "Must enter old password";
	private final String ENTER_NEW_PASSWORD = "Must enter new password";
	private final String CONFIRM_NEW_PASSWORD = "Must confirm new password";
	private final String PASSWORDS_DO_NOT_MATCH = "New passwords don't match";
	private final String OLD_PASSWORD_DOESNT_MATCH = "Old password doesn't match";
	
	/**
	 * Below are the fields for the Toast's when the user doesn't enter correct information
	 * when attempting to change their distress password
	 */
	private final String ENTER_OLD_DISTRESS = "Must enter old distress";
	private final String ENTER_NEW_DISTRESS = "Must enter new distress";
	private final String CONFIRM_NEW_DISTRESS = "Must confirm new distress";
	private final String DISTRESS_PASSWORDS_DO_NOT_MATCH = "New distress don't match";
	private final String OLD_DISTRESS_DOESNT_MATCH = "Old distress doesn't match";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		viewProfile();
	}
	
	/**
	 * sets up the profile screen
	 */
	private void viewProfile()
	{
		setContentView(R.layout.profile_layout);
		viewProfile_greetingText = (TextView)findViewById(R.id.profile_greeting_text);
		viewProfile_nameEditText = (EditText)findViewById(R.id.profile_name_edit_text);
		viewProfile_changePasswordButton = (Button)findViewById(R.id.profile_change_password);
		viewProfile_changeDistressButton = (Button)findViewById(R.id.profile_change_distress);
		viewProfile_addDeleteContactButton = (Button)findViewById(R.id.profile_add_delete_contact);
		viewProfile_viewSavedDataButton = (Button)findViewById(R.id.profile_view_saved_data);
		viewProfile_backButton = (Button)findViewById(R.id.profile_back_button);
		viewProfile_saveButton = (Button)findViewById(R.id.profile_save_button);
		
		viewProfile_greetingText.setText("Hi " + Login.currentUserSignedIn.getName() + ", what would you like to make changes to?");
		viewProfile_nameEditText.setText(Login.currentUserSignedIn.getName());
		
		
		nameChange = false;
		password = "";
		distress = "";
		
		//sets up the interaction when the user change password back button
		viewProfile_changePasswordButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//inflate the change password dialog
  				inflateChangePasswordDialog();
  			}
  		});
		
		//sets up the interaction when the user clicks the change distress button
		viewProfile_changeDistressButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//inflate the change distress password dialog
  				inflateChangeDistressPasswordDialog();
  			}
  		});
		
		//sets up the interaction when the user add/delete contact back button
		viewProfile_addDeleteContactButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//go to the add/delete contact page
  				startActivity(new Intent ("com.osu.insecurity.CONTACTS"));
  			}
  		});
		
		//sets up the interaction when the user add/delete contact back button
		viewProfile_viewSavedDataButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//go to the add/delete contact page
  				startActivity(new Intent ("com.osu.insecurity.DATA"));
  			}
  		});
		
		//sets up the interaction when the user save back button
		viewProfile_saveButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//inflate dialog to confirm changes
  				inflateConfirmSaveDialog();
  			}
  		});
		
		//sets up the interaction when the user back back button
		viewProfile_backButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//see if the user changed their name
  				if(!viewProfile_nameEditText.getText().toString().equals(Login.currentUserSignedIn.getName()))
  				{
  					nameChange = true;
  				}
  				
  				if(!(nameChange || password.length() > 0 || distress.length() > 0))//if there are no changes then continue
  				{
  					finish();
  				}
  				else
  				{
  					//inflate confirm deletion of changes
  					inflateSaveChangesDialog();
  				}
  			}
  		});
	}
	
	/**
	 * Inflates the confirm save dialog
	 */
	private void inflateConfirmSaveDialog()
	{
		final FrameLayout fl = new FrameLayout(this);
		 new AlertDialog.Builder(this)
	        .setView(fl)
	        .setTitle("Confirm Changes?")
	        .setCancelable(false)
	        .setPositiveButton("Save", new DialogInterface.OnClickListener(){
               @Override
               public void onClick(DialogInterface d, int which) {
            	   updateProfile();
               }
               })
           .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
               @Override

               public void onClick(DialogInterface d, int which) {
                       d.dismiss();
               }
	        }).create().show();
	}
	
	/**
	 * inflates the save changes dialog
	 */
	private void inflateSaveChangesDialog()
	{
		final FrameLayout fl = new FrameLayout(this);
		 new AlertDialog.Builder(this)
	        .setView(fl)
	        .setTitle("Are you sure you want to quit without saving?")
	        .setCancelable(false)
	        .setPositiveButton("Yes save my changes", new DialogInterface.OnClickListener(){
              @Override
              public void onClick(DialogInterface d, int which) {
            	  updateProfile(); //update the profile
            	  finish();
              }
              })
          .setNegativeButton("Just quit", new DialogInterface.OnClickListener(){
              @Override

              public void onClick(DialogInterface d, int which) {
                      finish();
              }
	        }).create().show();
	}
	
	/**
	 * inflates change password dialog
	 */
	private void inflateChangePasswordDialog()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.alert_dialog_change_password_layout, null);
		viewProfile_alertDialog_oldPasswordEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_password_old_password_edit_text);
		viewProfile_alertDialog_newPasswordEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_password_new_password_edit_text);
		viewProfile_alertDialog_confirmNewPasswordEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_password_confirm_new_password_edit_text);
		new AlertDialog.Builder(this)
	        .setView(layout)
	        .setTitle("Change Password?")
	        .setCancelable(false)
	        .setPositiveButton("Change", new DialogInterface.OnClickListener(){
             @Override
             public void onClick(DialogInterface d, int which) {
    	 		if(viewProfile_alertDialog_oldPasswordEditText.getText().toString().length() > 0) //check to see if the user entered their old password
    	 		{
    	 			if(viewProfile_alertDialog_newPasswordEditText.getText().toString().length() > 0) //check to see if the user entered a new password
    	 			{
    	 				if(viewProfile_alertDialog_confirmNewPasswordEditText.getText().toString().length() > 0) //check to see if the user entered a new password
        	 			{
    	 					if(viewProfile_alertDialog_confirmNewPasswordEditText.getText().toString().equals(viewProfile_alertDialog_newPasswordEditText.getText().toString())) //check to see if the passwords match
            	 			{
            	 				if(Login.currentUserSignedIn.getPassword().equals(viewProfile_alertDialog_oldPasswordEditText.getText().toString())) //check to see if the old passwords match
	            	 			{
            	 					password = viewProfile_alertDialog_confirmNewPasswordEditText.getText().toString();
	            	 			}
	            	 			else
	            	 			{
	            	 				Toast.makeText(getApplicationContext(), OLD_PASSWORD_DOESNT_MATCH, Toast.LENGTH_SHORT).show();
	            	 			}
            	 			}
    	 					else
            	 			{
            	 				Toast.makeText(getApplicationContext(), PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_SHORT).show();
            	 			}
        	 			}
    	 				else
        	 			{
        	 				Toast.makeText(getApplicationContext(), CONFIRM_NEW_PASSWORD, Toast.LENGTH_SHORT).show();
        	 			}
    	 			}
    	 			else
    	 			{
    	 				Toast.makeText(getApplicationContext(), ENTER_NEW_PASSWORD, Toast.LENGTH_SHORT).show();
    	 			}
    	 		}
    	 		else
	 			{
	 				Toast.makeText(getApplicationContext(), ENTER_OLD_PASSWORD, Toast.LENGTH_SHORT).show();
	 			}
           	  	d.dismiss();
             }
             })
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
             @Override

             public void onClick(DialogInterface d, int which) {
            	 	d.dismiss();
             }
	        }).create().show();
	}
	
	/**
	 * inflates change distress password dialog
	 */
	private void inflateChangeDistressPasswordDialog()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.alert_dialog_change_distress_layout, null);
		viewProfile_alertDialog_oldDistressEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_distress_old_distress_edit_text);
		viewProfile_alertDialog_newDistressEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_distress_new_distress_edit_text);
		viewProfile_alertDialog_confirmNewDistressEditText = (EditText) layout.findViewById(R.id.alert_dialog_change_distress_confirm_new_distress_edit_text);
		new AlertDialog.Builder(this)
	        .setView(layout)
	        .setTitle("Change Distress Password?")
	        .setCancelable(false)
	        .setPositiveButton("Change", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int which) {
            	if(viewProfile_alertDialog_oldDistressEditText.getText().toString().length() > 0) //check to see if the user entered their old distress
    	 		{
    	 			if(viewProfile_alertDialog_newDistressEditText.getText().toString().length() > 0) //check to see if the user entered a new distress
    	 			{
    	 				if(viewProfile_alertDialog_confirmNewDistressEditText.getText().toString().length() > 0) //check to see if the user confirmed a new distress
        	 			{
    	 					if(viewProfile_alertDialog_confirmNewDistressEditText.getText().toString().equals(viewProfile_alertDialog_newPasswordEditText.getText().toString())) //check to see if the distress match
            	 			{
            	 				if(Login.currentUserSignedIn.getDistressPassword().equals(viewProfile_alertDialog_oldDistressEditText.getText().toString())) //check to see if the old passwords match
	            	 			{
            	 					distress = viewProfile_alertDialog_confirmNewDistressEditText.getText().toString();
	            	 			}
	            	 			else
	            	 			{
	            	 				Toast.makeText(getApplicationContext(), OLD_DISTRESS_DOESNT_MATCH, Toast.LENGTH_SHORT).show();
	            	 			}
            	 			}
    	 					else
            	 			{
            	 				Toast.makeText(getApplicationContext(), DISTRESS_PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_SHORT).show();
            	 			}
        	 			}
    	 				else
        	 			{
        	 				Toast.makeText(getApplicationContext(), CONFIRM_NEW_DISTRESS, Toast.LENGTH_SHORT).show();
        	 			}
    	 			}
    	 			else
    	 			{
    	 				Toast.makeText(getApplicationContext(), ENTER_NEW_DISTRESS, Toast.LENGTH_SHORT).show();
    	 			}
    	 		}
    	 		else
	 			{
	 				Toast.makeText(getApplicationContext(), ENTER_OLD_DISTRESS, Toast.LENGTH_SHORT).show();
	 			}
           	  	d.dismiss();
            }
            })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override

            public void onClick(DialogInterface d, int which) {
           	 	d.dismiss();
            }
	        }).create().show();
	}
	
	/**
	 * Updates the Profile
	 */
	private void updateProfile()
	{
		Login.database.remove(Login.currentUserSignedIn);
		Login.currentUserSignedIn.setName(viewProfile_nameEditText.getText().toString());
		viewProfile_greetingText.setText("Hi " + Login.currentUserSignedIn.getName() + ", what would you like to make changes to?");
		if(!password.equals(""))
		{
			Login.currentUserSignedIn.setPassword(password);
		}
		if(!distress.equals(""))
		{
			Login.currentUserSignedIn.setDistressPassword(distress);
		}
		Login.database.add(Login.currentUserSignedIn);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	//see if the user changed their name
			if(!viewProfile_nameEditText.getText().toString().equals(Login.currentUserSignedIn.getName()))
			{
				nameChange = true;
			}
			
			if(!(nameChange || password.length() > 0 || distress.length() > 0))//if there are no changes then continue
			{
				finish();
			}
			else
			{
				//inflate confirm deletion of changes
				inflateSaveChangesDialog();
			}
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
