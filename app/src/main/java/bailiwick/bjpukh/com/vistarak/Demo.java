package bailiwick.bjpukh.com.vistarak;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import bailiwick.bjpukh.com.vistarak.LocationService.MyLocation;


public class Demo extends Activity {

    TextView txt_lan, txt_lat;
    Button btnsubmit;
    public static Location loc;
    MyLocation.LocationResult locationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demoloc);
        txt_lan = (TextView) findViewById(R.id.txt_lan);
        txt_lat = (TextView) findViewById(R.id.txt_long);
        btnsubmit = (Button) findViewById(R.id.button1);


        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(final Location location) {
                loc = location;
                System.out.println("Latitude: " + loc.getLatitude());
                System.out.println("Longitude: " + loc.getLongitude());
                txt_lan.setText("" + loc.getLatitude());
                txt_lat.setText("" + loc.getLongitude());

            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(Demo.this, locationResult);
    }
}
        /*	btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				show_location();
			}

			private void show_location() {
				// TODO Auto-generated method stub
				locationResult = new LocationResult() {
					@Override
					public void gotLocation(final Location location) {
						loc = location;
						System.out.println("Latitude: " + loc.getLatitude());
						System.out.println("Longitude: " + loc.getLongitude());
						txt_lan.setText("" + loc.getLatitude());
						txt_lat.setText("" + loc.getLatitude());

					}
				};
			}
		});

		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(MainActivity.this, locationResult);
	}*/



