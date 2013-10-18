package com.osu.insecurity;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
	 * Below are the controls for the main menu
	 * main_onOrOffCheckBox = on or off check box
	 * main_contactsButton = contacts button
	 */
	private CheckBox insecurity_onOrOffCheckBox;
	private Button insecurity_profileButton;
	private GoogleMap mMap;
	
	private Bundle savedInstanceState;
	
	/**
	 * The alarm sound to be played
	 */
	private MediaPlayer alarm;
	
	/**
	 * The recorder and the player
	 */
    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;
    
    private String mFileName = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;		
		insecurity();
	}
	 
	/**
	 * Set up the main screen
	 */
	private void insecurity()
	{
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.insecurity_layout);
		
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
        
		insecurity_onOrOffCheckBox = (CheckBox)findViewById(R.id.insecurity_on_off_checkBox);
		insecurity_profileButton = (Button)findViewById(R.id.insecurity_profile_button);
		insecurity_onOrOffCheckBox.setChecked(true);
		
		//sets up the interaction when the user clicks the on or off check box
		insecurity_onOrOffCheckBox.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(insecurity_onOrOffCheckBox.isChecked())
				{
					insecurity_onOrOffCheckBox.setText("Turn Off");
					//main_onOrOffCheckBox.setChecked(false);
				}
				else
				{
					insecurity_onOrOffCheckBox.setText("Turn On");
					//main_onOrOffCheckBox.setChecked(true);
				}
			}
		});
		
		//sets up the interaction when the user clicks the contacts button
		insecurity_profileButton.setOnClickListener(new View.OnClickListener() 
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				//go to the contacts screen
				startActivity(new Intent ("com.osu.insecurity.PROFILE"));
  			}
  		});
		
		alarm = MediaPlayer.create(this, R.raw.alarm);	
	}
	
	@Override
	public void onMapClick(LatLng position) {
		
		mMap.addMarker(new MarkerOptions()
							.position(position).
							icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
	}
	
	/**
	 * Starts recording data from the mic
	 */
	private void startRecording() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        	 Toast.makeText(getApplicationContext(), "Cant record", Toast.LENGTH_SHORT ).show();
        }

        mRecorder.start();
    }
	
	/**
	 * Stops recording the data from the mic
	 */
	 private void stopRecording() 
	 {
		 if(mRecorder != null)
		 {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
		 }
	 }
	 
	 /**
	  * Used if you want to listen to the recorded data
	  * Starts playing the recorded data from the mic
	  */
	 private void startPlaying() 
	 {
	        mPlayer = new MediaPlayer();
	        try {
	            mPlayer.setDataSource(mFileName);
	            mPlayer.prepare();
	            mPlayer.start();
	        } catch (IOException e) {
	        	 Toast.makeText(getApplicationContext(), "Cant play recorded sound", Toast.LENGTH_SHORT ).show();
	        }
	 }

	 /**
	  * Used if you want to listen to the recorded data
	  * stops playing the recorded data from the mic
	  */
	 private void stopPlaying() {
		if(mPlayer != null)
		{
	        mPlayer.release();
	        mPlayer = null;
		}
	 }
	 
	 /**
	  * Sets of the alarm
	  */
	 private void setOffAlarm()
		{
			alarm.start();
			alarm.setLooping(true);
			final EditText input = new EditText(this);
			 new AlertDialog.Builder(this)
		        .setView(input)
		        .setMessage("Please enter your user identification code")
		        .setCancelable(false)
		        .setPositiveButton("Enter", new DialogInterface.OnClickListener(){
	                @Override
	                public void onClick(DialogInterface d, int which) {
	                	String id = input.getText().toString();
	                	if(id.length() > 0)
	                	{
	                		if(id.equals("0"))
	                		{
	                			alarm.setLooping(false);
	                			alarm.stop();
	                			alarm.release();
	                			alarm = null;
	                			alarm = MediaPlayer.create(Insecurity.this, R.raw.alarm);
	                		}
	                	}
	                }
	                })
	            .create().show();
		}
	 
	 @Override
	 protected void onPause() {
		 if(mPlayer != null)
		 {
			 mPlayer.release();
			 mPlayer = null;
		 }
		 if(mRecorder != null)
		 {
			 mRecorder.release();
			 mRecorder = null;
		 }
		 
	     super.onPause();
	 }
	 
	 @Override
	 protected void onStop() {
		 if(mPlayer != null)
		 {
			 mPlayer.release();
			 mPlayer = null;
		 }
		 if(mRecorder != null)
		 {
			 mRecorder.release();
			 mRecorder = null;
		 }
	     super.onPause();
	 }
	 
	 @Override
     protected void onDestroy() {
		 if(mPlayer != null)
		 {
			 mPlayer.release();
			 mPlayer = null;
		 }
		 if(mRecorder != null)
		 {
			 mRecorder.release();
			 mRecorder = null;
		 }
	     super.onPause();
	 }
}
