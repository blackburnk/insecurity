package com.osu.insecurity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

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
  				//go to the contacts screen
				startActivity(new Intent ("com.osu.insecurity.CONTACTS"));
  			}
  		});
		
		
	}
	
		@Override
	public void onMapClick(LatLng position) {
		
		mMap.addMarker(new MarkerOptions()
							.position(position).
							icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
	}
	
}
