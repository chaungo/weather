package nnmc.weather;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nnmchau on 6/16/2017.
 */

public class WeatherFragment extends Fragment {

    Task<Location> mLastLocation;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    TextView description;
    TextView temp;
    TextView maxTemp;
    TextView minTemp;
    TextView humidity;
    TextView city;


    Location mLocation;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);
        description = (TextView) rootView.findViewById(R.id.descriptionInfo);
        temp = (TextView) rootView.findViewById(R.id.tempInfo);
        maxTemp = (TextView) rootView.findViewById(R.id.maxTempInfo);
        minTemp = (TextView) rootView.findViewById(R.id.minTempInfo);
        humidity = (TextView) rootView.findViewById(R.id.humidityInfo);
        city = (TextView) rootView.findViewById(R.id.cityInfo);


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            getLocation();
        }
        return rootView;
    }

    private Location getLocation() {
        final Location[] resultLocation = {null};
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation();
            mLastLocation.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLocation = location;
                        doingJob();
                    }
                }
            });
        }
        return resultLocation[0];
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
            }
        }
    }

    public void doingJob() {
        new GetWeatherInfoByLocation().execute(mLocation);
    }


    private class GetWeatherInfoByLocation extends AsyncTask<Location, Object, WeatherInfo> {

        @Override
        protected WeatherInfo doInBackground(Location... params) {
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            try {
                String apiKey = "1b401aa71ffcb2682f311c8b5766643c";
                String wurl = "http://api.openweathermap.org/data/2.5/weather?lat=";
                URL url = new URL(wurl + params[0].getLatitude() + "&lon=" + params[0].getLongitude() + "&appid=" + apiKey + "&units=metric&lang=vi");
                //System.out.println(url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setReadTimeout(3000);
                connection.connect();

                inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

                String kq = scanner.hasNext() ? scanner.next() : "";

                JSONObject jsonObject = new JSONObject(kq);
                String cityName = jsonObject.getString("name");
                String country = jsonObject.getJSONObject("sys").getString("country");

                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                JSONObject main = jsonObject.getJSONObject("main");
                Double temp = main.getDouble("temp");
                Double temp_max = main.getDouble("temp_max");
                Double temp_min = main.getDouble("temp_min");
                Double humidity = main.getDouble("humidity");

                System.out.println(cityName);
                System.out.println(country);
                System.out.println(description);
                System.out.println(temp);
                System.out.println(humidity);


                return new WeatherInfo(description, temp, temp_max, temp_min, humidity, cityName, country);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherInfo weatherInfo) {
            super.onPostExecute(weatherInfo);
            if (weatherInfo != null) {
                description.setText(weatherInfo.getDescription());
                String DEGREE_SIGN = "\u2103";
                temp.setText(weatherInfo.getTemp() + DEGREE_SIGN);
                maxTemp.setText(weatherInfo.getMaxTemp() + DEGREE_SIGN);
                minTemp.setText(weatherInfo.getMinTemp() + DEGREE_SIGN);
                humidity.setText(weatherInfo.getHumidity() + "%");
                city.setText(weatherInfo.getCity() + ", " + weatherInfo.getCountry());
            }

        }
    }
}
