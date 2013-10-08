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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Insecurity extends android.support.v4.app.FragmentActivity 
implements OnMapClickListener{
	
	/**
	 * The database of Contacts
	 */
	private Set<Contact> contacts;
	
	/**
	 * The linear layout view of the Contacts
	 */
	private LinearLayout contactsLinearLayout;
	
	/**
	 * Below are the controls for the contacts menu
	 * contacts_backButton = back button
	 * contacts_addContactButton = add contact button
	 * contacts_deleteContactButton = delete contact button
	 */
	private Button contacts_backButton;
	private Button contacts_addContactButton;
	private Button contacts_deleteContactButton;
	
	/**
	 * Below are the controls for the main menu
	 * main_onOrOffCheckBox = on or off check box
	 * main_contactsButton = contacts button
	 */
	private CheckBox main_onOrOffCheckBox;
	private Button main_contactsButton;
	private GoogleMap mMap;
	
	private Bundle savedInstanceState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.savedInstanceState = savedInstanceState;
		
		try 
		{
			loadContacts();
		} 
		catch (IOException e) 
		{
			System.exit(0);
		}
		
		main();
	}
	 
	/**
	 * Set up the main screen
	 */
	private void main()
	{
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.main_layout);
		
		// Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setOnMapClickListener(this);
        
        //set up the location manager to get Latituded and Longitude position
        LocationManager locationManager;
        String svcName= Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        //set the necessary criteria for the location
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
        
        
        Location l = locationManager.getLastKnownLocation(provider);
        
        if(l != null) //check to see if the location was found
        {
	        LatLng coordinate = new LatLng(l.getLatitude(), l.getLongitude());
	        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,
	    			17));
        }
        else
        {
        	LatLng coordinate = new LatLng(40.0023, -83.0158); //position of dreese labs
	        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,
	    			17));
        }
        
		main_onOrOffCheckBox = (CheckBox)findViewById(R.id.main_on_off_checkBox);
		main_contactsButton = (Button)findViewById(R.id.main_contacts_button);
		main_onOrOffCheckBox.setChecked(true);
		
		//sets up the interaction when the user clicks the on or off check box
		main_onOrOffCheckBox.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(main_onOrOffCheckBox.isChecked())
				{
					main_onOrOffCheckBox.setText("Turn Off");
					//main_onOrOffCheckBox.setChecked(false);
				}
				else
				{
					main_onOrOffCheckBox.setText("Turn On");
					//main_onOrOffCheckBox.setChecked(true);
				}
			}
		});
		
		//sets up the interaction when the user clicks the contacts button
		main_contactsButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				contacts();
	  			}
	  		});
		
		
	}
	
	/**
	 * set up the contacts screen
	 */
	private void contacts()
	{
		setContentView(R.layout.contacts_layout);
		contactsLinearLayout = (LinearLayout)findViewById(R.id.contacts_linear_layout);
		contacts_backButton = (Button)findViewById(R.id.contacts_back_button);
		contacts_addContactButton = (Button)findViewById(R.id.contacts_add_contact_button);
		contacts_deleteContactButton = (Button)findViewById(R.id.contacts_delete_contact_button);
		
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
	    	contactsLinearLayout.addView(newContactView);
	    	id++;
	    }
	    
	  //sets up the interaction when the user clicks the back button
	    contacts_backButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				main();
	  			}
	  		});
	    
	  //sets up the interaction when the user clicks the add contact button
	    contacts_addContactButton.setOnClickListener(new View.OnClickListener() 
	  		{
	  			@Override
	  			public void onClick(View v) 
	  			{
	  				addContact();
	  			}
	  		});
	    
	  //sets up the interaction when the user clicks the delete contact button
	    contacts_deleteContactButton.setOnClickListener(new View.OnClickListener() 
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
		final EditText input = new EditText(this);
		 new AlertDialog.Builder(this)
	        .setView(input)
	        .setTitle("Add Contact")
	        .setMessage("Contact (First Last)/Phone Number(xxx-xxx-xxxx)/Email with each seperated by a space")
	        .setCancelable(false)
	        .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int which) {
                	Contact contact = new Contact();
                	String text  = input.getText().toString();
                	String fields [] = text.split(" ");
                	String name = "", phoneNumber = "", email = "";
                	if(fields.length != 4)
                	{
                		Toast.makeText(getApplicationContext(), "Error, contact was not added", Toast.LENGTH_SHORT).show();
                	}
                	else
                	{
                		name = fields[0] + " " + fields[1];
            			phoneNumber = fields[2];
            			email = fields[3];
            			if(name.length() > 0)
                    	{
                    		contact.setName(name);
                    		contact.setPhoneNumber(phoneNumber);
                    		contact.setEmail(email);
                    		contacts.add(contact);
                    	}
                    	else
                    	{
                    		Toast.makeText(getApplicationContext(), "Error, contact was not added", Toast.LENGTH_SHORT).show();
                    	}
                   	}
                	d.dismiss();
                	contacts();
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
    			    d.dismiss();
    			    contacts();
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
			if(c == ',')
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
			    	out.write(contact.getName() + "," + contact.getPhoneNumber() + "," + contact.getEmail() + ";\n");
			    }
			    out.close();
			}
		} 
		catch (IOException e) 
		{
		    
		}
	}
	
	@Override
	public void onMapClick(LatLng position) {
		
		mMap.addMarker(new MarkerOptions()
							.position(position).
							icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
		
	}
	
	/**
	 * private class Contact is a contact that holds the values of a contact
	 */
	private class Contact 
	{
		private String name;
		private String phoneNumber;
		private String email;
		
		/**
		 * Public constructor to build a new Contact
		 */
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
	
}
