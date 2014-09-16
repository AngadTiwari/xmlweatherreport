package com.angtwr31.xmlweatherreport;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private String xml;
		private TextView longitude,latitude,sunrise,sunset,mintemp,maxtemp,humidity,pressure,wind,weatherstatus,lastupdate;
		private Spinner selectcity;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_weather,
					container, false);
			
			longitude=(TextView)rootView.findViewById(R.id.lon);
			latitude=(TextView)rootView.findViewById(R.id.lat);
			sunrise=(TextView)rootView.findViewById(R.id.sunrise);
			sunset=(TextView)rootView.findViewById(R.id.sunset);
			mintemp=(TextView)rootView.findViewById(R.id.mintemp);
			maxtemp=(TextView)rootView.findViewById(R.id.maxtemp);
			humidity=(TextView)rootView.findViewById(R.id.humidity);
			pressure=(TextView)rootView.findViewById(R.id.pressure);
			wind=(TextView)rootView.findViewById(R.id.wind);
			weatherstatus=(TextView)rootView.findViewById(R.id.weatherstatus);
			lastupdate=(TextView)rootView.findViewById(R.id.lastupdate);
			
			selectcity=(Spinner)rootView.findViewById(R.id.selectcity);
			ArrayAdapter<String> ad=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,new String[]{"indore","jabalpur"});
			selectcity.setAdapter(ad);
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpGet request=new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=indore&mode=xml");
				HttpResponse response=client.execute(request);
				
				HttpEntity entity=response.getEntity();
				xml=EntityUtils.toString(entity);
				
				DocumentBuilder docbuilder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is=new InputSource();
				is.setCharacterStream(new StringReader(xml));
				Document doc=docbuilder.parse(is);
				
				Element city=(Element)doc.getElementsByTagName("city").item(0);
				Element temp=(Element)doc.getElementsByTagName("temperature").item(0);
				Element coordinate=(Element)city.getElementsByTagName("coord").item(0);
				Element sun=(Element)city.getElementsByTagName("sun").item(0);
				Element humidity=(Element)doc.getElementsByTagName("humidity").item(0);
				Element pressure=(Element)doc.getElementsByTagName("pressure").item(0);
				Element wind=(Element)doc.getElementsByTagName("wind").item(0);
				Element speed=(Element)wind.getElementsByTagName("speed").item(0);
				Element direction=(Element)wind.getElementsByTagName("direction").item(0);
				Element weather=(Element)doc.getElementsByTagName("weather").item(0);
				Element lastupdate=(Element)doc.getElementsByTagName("lastupdate").item(0);
				
				this.longitude.setText(coordinate.getAttribute("lon"));
				this.latitude.setText(coordinate.getAttribute("lat"));
				this.sunrise.setText(sun.getAttribute("rise"));
				this.sunset.setText(sun.getAttribute("set"));
				this.mintemp.setText(temp.getAttribute("min")+" "+temp.getAttribute("unit").substring(0, 1));
				this.maxtemp.setText(temp.getAttribute("max")+" "+temp.getAttribute("unit").substring(0, 1));
				this.humidity.setText(humidity.getAttribute("value")+" "+humidity.getAttribute("unit"));
				this.pressure.setText(pressure.getAttribute("value")+" "+pressure.getAttribute("unit"));
				this.wind.setText(speed.getAttribute("name")+" "+direction.getAttribute("name"));
				this.weatherstatus.setText(weather.getAttribute("value"));
				this.lastupdate.setText(lastupdate.getAttribute("value"));
			 	
				entity.consumeContent();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
			
			return rootView;
		}
	}
}
