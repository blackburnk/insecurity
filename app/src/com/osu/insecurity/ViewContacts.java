package com.osu.insecurity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContacts extends Activity {
	
	/**
	 * Below are the controls for the contacts menu
	 * viewContacts_backButton = back button
	 * viewContacts_addContactButton = add contact button
	 * viewContacts_deleteContactButton = delete contact button
	 * viewContacts_nameEditText = alert dialog edit text for name
	 * viewContacts_phoneNumberEditText = alert dialog edit text for phone number
	 * viewContacts_emailAddressEditTexts = alert dialog edit text for email
	 */
	private Button viewContacts_backButton;
	private Button viewContacts_addContactButton;
	private Button viewContacts_deleteContactButton;
	private EditText viewContacts_alertDialog_nameEditText;
	private EditText viewContacts_alertDialog_phoneNumberEditText;
	private EditText viewContacts_alertDialog_emailAddressEditText;
	/**
	 * The linear layout view of the Contacts
	 */
	private LinearLayout viewContactsLinearLayout;
	
	/**
	 * The database of Contacts
	 */
	private Set<Contact> contacts;
	
	/**
	 * Below are the fields for the Toast's when the user enters incorrect information when 
	 * attempting to add a contact
	 */
	private final String MUST_ENTER_A_PHONE_NUMBER_OR_EMAIL_ADDRESS = "Must enter a phone number or an email address";
	private final String MUST_ENTER_A_NAME = "Must enter a phone number or an email address";
	
	/**
	 * Below is the field for the Toast when the user enters incorrect information when
	 * attempting to delete a contact
	 */
	private final String NO_CONTACT_FOUND = "No contact found with that associated name, please check spelling";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		try 
		{
			loadContacts();
		} 
		catch (IOException e) 
		{
			System.exit(0);
		}
		viewContacts();
	}
	
	/**
	 * set up the contacts screen
	 */
	private void viewContacts()
	{
		setContentView(R.layout.contacts_layout);
		viewContactsLinearLayout = (LinearLayout)findViewById(R.id.contacts_linear_layout);
		viewContacts_backButton = (Button)findViewById(R.id.contacts_back_button);
		viewContacts_addContactButton = (Button)findViewById(R.id.contacts_add_contact_button);
		viewContacts_deleteContactButton = (Button)findViewById(R.id.contacts_delete_contact_button);
		
		Iterator<Contact> iter =  contacts.iterator();
		int id = 0;
	    while(iter.hasNext()) //iterate over the keys and values and print to the text view
	    {
	    	Contact contact = iter.next();
	    	TextView newContactView = new TextView(this);
	    	newContactView.setText(contact.getName());
	    	newContactView.setId(id);
	    	newContactView.setTextColor(Color.WHITE);
	    	newContactView.setLayoutParams(new LayoutParams(
	                LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT));
	    	viewContactsLinearLayout.addView(newContactView);
	    	id++;
	    }
	    
	  //sets up the interaction when the user clicks the back button
	    viewContacts_backButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				saveContacs();
	  				finish();
	  			}
	  		});
	    
	  //sets up the interaction when the user clicks the add contact button
	    viewContacts_addContactButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				addContact();
	  			}
	  		});
	    
	  //sets up the interaction when the user clicks the delete contact button
	    viewContacts_deleteContactButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				deleteContact();
	  			}
	  		});
	    
	  
	}
	
	/**
	 * Sets an alert dialog box to add a contact
	 */
	private void addContact()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.alert_dialog_add_contact_layout, null);		viewContacts_alertDialog_nameEditText = (EditText)findViewById(R.id.alert_dialog_add_contact_name_edit_text);
		viewContacts_alertDialog_phoneNumberEditText = (EditText)findViewById(R.id.alert_dialog_add_contact_phone_number_edit_text);
		viewContacts_alertDialog_emailAddressEditText = (EditText)findViewById(R.id.alert_dialog_add_contact_email_edit_text);
		new AlertDialog.Builder(this)
	        .setView(layout)
	        .setTitle("Add Contact")
	        .setCancelable(true)
	        .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int which) {
                	Contact contact = new Contact();
                	String name = "", phoneNumber = "", email = "";
                	if(viewContacts_alertDialog_nameEditText.getText().toString().length() > 0) //check to ensure there is a name
                	{
                		if(viewContacts_alertDialog_phoneNumberEditText.getText().toString().length() > 0) //check to see if there is a phone number
                		{
                			if(viewContacts_alertDialog_emailAddressEditText.getText().toString().length() > 0) //check to see if there is an email
                    		{
                    			name = viewContacts_alertDialog_nameEditText.getText().toString();
                    			phoneNumber = viewContacts_alertDialog_phoneNumberEditText.getText().toString();
                    			email = viewContacts_alertDialog_emailAddressEditText.getText().toString();
                    			
                    			contact.setEmail(email);
                    			contact.setName(name);
                    			contact.setPhoneNumber(phoneNumber);
                    			contacts.add(contact);
                    		}
                			else //no email just phone number and name
                			{
                				name = viewContacts_alertDialog_nameEditText.getText().toString();
                    			phoneNumber = viewContacts_alertDialog_phoneNumberEditText.getText().toString();
                    			contacts.add(contact);
                			}
                		}
                		else if(viewContacts_alertDialog_emailAddressEditText.getText().toString().length() > 0) //check to see if there is an email
                		{
                			if(viewContacts_alertDialog_phoneNumberEditText.getText().toString().length() > 0) //check to see if there is a phone number
                    		{
                    			name = viewContacts_alertDialog_nameEditText.getText().toString();
                    			phoneNumber = viewContacts_alertDialog_phoneNumberEditText.getText().toString();
                    			email = viewContacts_alertDialog_emailAddressEditText.getText().toString();
                    			contacts.add(contact);
                    		}
                			else //no phone number just email and name
                			{
                				name = viewContacts_alertDialog_nameEditText.getText().toString();
                    			email = viewContacts_alertDialog_emailAddressEditText.getText().toString();
                    			contacts.add(contact);
                			}
                		}
                		else //user must enter a phone number or email
                		{
                    		Toast.makeText(getApplicationContext(), MUST_ENTER_A_PHONE_NUMBER_OR_EMAIL_ADDRESS, Toast.LENGTH_SHORT).show();                		
                		}
                	}
                	else //user must enter a name
                	{
                		Toast.makeText(getApplicationContext(), MUST_ENTER_A_NAME, Toast.LENGTH_SHORT).show();                		
                	}
                	d.dismiss();
                	viewContacts();
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
	 * Sets an alert dialog box to delete a contact
	 */
	private void deleteContact()
	{
		 final EditText input = new EditText(this);
		 new AlertDialog.Builder(this)
	        .setView(input)
	        .setTitle("Delete Contact")
	        .setMessage("Enter the contacts name exactly how it appears")
	        .setCancelable(false)
	        .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int which) {
                	Iterator<Contact> iter =  contacts.iterator();
                	Contact contactToDelete = new Contact();
    			    while(iter.hasNext()) //iterate over the contacts
    			    {
    			    	Contact contact = iter.next();
    			    	
    			    	String name = input.getText().toString();
    			    	if(contact.getName().equals(name))
    			    	{
    			    		contactToDelete = contact;
    			    	}
    			    }
    			   
    			    if(contactToDelete.getName().length() > 0)
    			    {
    			    	contacts.remove(contactToDelete);
    			    }
    			    else
    			    {
    			    	Toast.makeText(getApplicationContext(), NO_CONTACT_FOUND,  Toast.LENGTH_SHORT).show();
    			    }
    			    d.dismiss();
    			    viewContacts();
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
	 * Loads the data from contacts.txt in the assets folder
	 * Loads the contact
	 */
	private void loadContacts() throws IOException
	{
		contacts = new HashSet<Contact>();
		AssetManager assets = getResources().getAssets();
		DataInputStream in = new DataInputStream(assets.open("contacts.txt"));// opens file  sent into loadData into the data input stream
		BufferedReader br = new BufferedReader(new InputStreamReader(in));// creates buffered reader br 
		String name = "";// initializes name
		String phoneNumber = "";//initializes phone number
		String email = ""; //initializes email
		boolean onName = true;
		boolean onPhoneNumber = false;
		boolean onEmail = false;
		while(br.ready())
		{
			char c = (char) br.read();// sets c as the character read from the buffered reader
			if(c != '\n' && c != '\r' && c != '\t') //skip escape character tokens
			{
				if(c == ':')
				{
					if(onName)
					{
						onName = false;
						onPhoneNumber = true;
					}
					else if(onPhoneNumber)
					{
						onPhoneNumber = false;
						onEmail = true;
					}
				}
				else if(c == ';') //check to see if end of line
				{
					Contact contact = new Contact();
					contact.setEmail(email);
					contact.setName(name);
					contact.setPhoneNumber(phoneNumber);
					contacts.add(contact);
					name = "";
					email = "";
					phoneNumber = "";
					onName = true;
					onEmail = false;
					onPhoneNumber = false;
				}
				else
				{
					if(onName) //check to see if we are still getting characters from the contacts name
					{
						name = name + c;
					}
					else if(onPhoneNumber) //check to see if we are still getting characters from the contacts phone number
					{
						phoneNumber = phoneNumber + c;
					}
					else if(onEmail) //check to see if we are still getting characters from the contacts email
					{
						email = email + c;
					}
				}
			}
		}
		br.close();//close buffered reader
		in.close();// close input stream
	}
	
	
	/**
	 * Saves the contacts data base
	 */
	private void saveContacs()
	{
		try 
		{
			File output= new File("contacts.txt");
			if (output.canWrite()) 
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(output, false));
			    Iterator<Contact> iter =  contacts.iterator();
			    while(iter.hasNext()) //iterate over the contacts and print to the contacts database file
			    {
			    	Contact contact = iter.next();
			    	out.write(contact.getName() + ":" + contact.getPhoneNumber() + ":" + contact.getEmail() + ";\n");
			    }
			    out.close();
			}
		} 
		catch (IOException e) 
		{
		    
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	//if the user hits the back button on the keypad, make sure we save the contacts
	        saveContacs();
	        finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
