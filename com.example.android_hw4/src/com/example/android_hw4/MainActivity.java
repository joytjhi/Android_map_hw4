package com.example.android_hw4;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

	Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
	List<Address> address;
	Marker TP = null;
	Marker[] markers = new Marker[] {null, null, null, null, null, null, null};
	String[] wonders = new String[] {"Chichen Itza, Mexico", "Christ Redeemer, Brazil", "Great Wall of China, China", "Machu Picchu, Peru", "Petra, Jordan", "The Roman Colosseum, Italy", "The Taj Mahal, India"};
	int[] wonderIDs = new int[] {R.drawable.chichenitza, R.drawable.christredeemer, R.drawable.greatwall, R.drawable.machupicchu, R.drawable.petra, R.drawable.colosseum, R.drawable.tajmahal};
	String actualaddr;
	double lat=0.0;
	double lng=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button button4 = (Button) findViewById(R.id.button4);
		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText text = (EditText) findViewById(R.id.main_input);
				showAddress(text.getText().toString());
			}
		});
		button2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				showSevenWonders();
			}
		});
		button3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTrafficMap(true);
			}
		});
		button4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTrafficMap(false);
			}
		});
    
    }
    
   public void showAddress(String addr){
	   GoogleMap googleMap;
	   
	   
	      if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                    .permitAll().build();
	 
	            StrictMode.setThreadPolicy(policy);
		StringBuilder stringBuild = new StringBuilder();
		
		// get rid of sevenwonders
		if (markers[0] != null) {
			for (int i=0; i<7; i++) {
				markers[i].remove();
			}
		}
		
		String addre;
		String encodedAddr;
		try {
			encodedAddr = URLEncoder.encode(addr,"UTF-8");
			addre = "https://maps.googleapis.com/maps/api/geocode/json?address="+encodedAddr+"&key=AIzaSyCED_Y2_Wt3qVdDPtiqMHVUCG9xyViE_xI";
			HttpPost httpGet = new HttpPost(addre);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response ;
			response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuild.append((char) b);
	        }
		
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	   JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuild.toString());

	       lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	       lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");
	     actualaddr = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
	        
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
		   googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		final LatLng TutorialsPoint = new LatLng(lat , lng);
		
		if(TP != null){
			TP.remove();
		}
		
		 TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title(actualaddr + "\n" +lat+","+lng));
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(TutorialsPoint, 10);
		googleMap.animateCamera(cameraUpdate);
		googleMap.setMyLocationEnabled(true);

	      }
   	}
   
   public void showSevenWonders(){
	   GoogleMap googleMap;
	   
	   
	   if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                    .permitAll().build();
	 
	            StrictMode.setThreadPolicy(policy);
		
	            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			//get rid of show address result
			if(TP != null){
				TP.remove();
			}
			
			CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(0);
			googleMap.animateCamera(cameraUpdate);
			googleMap.setMyLocationEnabled(true);
		
			for (int i=0; i<7; i++) {
				String addre;
				String encodedAddr;
				StringBuilder stringBuild = new StringBuilder();
				try {
					encodedAddr = URLEncoder.encode(wonders[i],"UTF-8");
					addre = "https://maps.googleapis.com/maps/api/geocode/json?address="+encodedAddr+"&key=AIzaSyCED_Y2_Wt3qVdDPtiqMHVUCG9xyViE_xI";
					HttpPost httpGet = new HttpPost(addre);
					HttpClient client = new DefaultHttpClient();
					HttpResponse response ;
					response = client.execute(httpGet);
			        HttpEntity entity = response.getEntity();
			        InputStream stream = entity.getContent();
			        int b;
			        while ((b = stream.read()) != -1) {
			            stringBuild.append((char) b);
			        }
				
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			   JSONObject jsonObject = new JSONObject();
			    try {
			        jsonObject = new JSONObject(stringBuild.toString());

			       lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			            .getJSONObject("geometry").getJSONObject("location")
			            .getDouble("lng");

			       lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			            .getJSONObject("geometry").getJSONObject("location")
			            .getDouble("lat");
			     actualaddr = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getString("formatted_address");
			        
			    } catch (JSONException e) {
			        e.printStackTrace();
			    }
				final LatLng TutorialsPoint = new LatLng(lat , lng);
				
				 markers[i] = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title(actualaddr).icon(BitmapDescriptorFactory.fromResource(wonderIDs[i])));
			}

	   }
   	}
   
   public void showTrafficMap(boolean condition) {
	   GoogleMap googleMap;
	   
	   
	   if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                    .permitAll().build();
	 
	            StrictMode.setThreadPolicy(policy);
		
	            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	            googleMap.setTrafficEnabled(condition);
	            googleMap.setMyLocationEnabled(true);
   
	   }
   }
}
