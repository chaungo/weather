package nnmc.ourtrips;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by nnmchau on 6/16/2017.
 */

public class InformationFragment extends Fragment {
    static final String API_KEY = "1b401aa71ffcb2682f311c8b5766643c";
    public static final int TIMEOUT = 3000;
    static final String DEGREE_SIGN = "\u2103";
    public static final String NAME = "name";
    public static final String SYS = "sys";
    public static final String COUNTRY = "country";
    public static final String WEATHER = "weather";
    public static final String DESCRIPTION = "description";
    public static final String MAIN = "main";
    public static final String TEMP = "temp";
    public static final String TEMP_MAX = "temp_max";
    public static final String TEMP_MIN = "temp_min";
    public static final String HUMIDITY = "humidity";
    public static final String DATA_TIME = "dt";
    public static final String ICON = "icon";


    Task<Location> mLastLocation;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    TextView textViewDescription;
    TextView textViewTemp;
    TextView textViewMaxTemp;
    TextView textViewMinTemp;
    TextView textViewHumidity;
    TextView textViewCity;


    Location mLocation;

    public InformationFragment() {
    }

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);
        textViewDescription = (TextView) rootView.findViewById(R.id.descriptionInfo);
        textViewTemp = (TextView) rootView.findViewById(R.id.tempInfo);
        textViewMaxTemp = (TextView) rootView.findViewById(R.id.maxTempInfo);
        textViewMinTemp = (TextView) rootView.findViewById(R.id.minTempInfo);
        textViewHumidity = (TextView) rootView.findViewById(R.id.humidityInfo);
        textViewCity = (TextView) rootView.findViewById(R.id.cityInfo);


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
        new GetCurrentWeatherInfoByLocation().execute(mLocation);
    }


    private class GetCurrentWeatherInfoByLocation extends AsyncTask<Location, Object, WeatherInfo> {

        @Override
        protected WeatherInfo doInBackground(Location... params) {
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            try {
                String wurl = "http://api.openweathermap.org/data/2.5/weather?lat=";
                URL url = new URL(wurl + params[0].getLatitude() + "&lon=" + params[0].getLongitude() + "&appid=" + API_KEY + "&units=metric&lang=vi");
                //System.out.println(url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setReadTimeout(TIMEOUT);
                connection.connect();

                inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

                String kq = scanner.hasNext() ? scanner.next() : "";

                JSONObject jsonObject = new JSONObject(kq);
                String cityName = jsonObject.getString(NAME);
                String country = jsonObject.getJSONObject(SYS).getString(COUNTRY);

                String description = jsonObject.getJSONArray(WEATHER).getJSONObject(0).getString(DESCRIPTION);
                String icon = jsonObject.getJSONArray(WEATHER).getJSONObject(0).getString(ICON);

                JSONObject main = jsonObject.getJSONObject(MAIN);
                Double temp = main.getDouble(TEMP);
                Double temp_max = main.getDouble(TEMP_MAX);
                Double temp_min = main.getDouble(TEMP_MIN);
                Double humidity = main.getDouble(HUMIDITY);

                return new WeatherInfo(description, temp, temp_max, temp_min, humidity, cityName, country, icon);
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
                textViewDescription.setText(weatherInfo.getDescription());
                textViewTemp.setText(weatherInfo.getTemp() + DEGREE_SIGN);
                textViewMaxTemp.setText(weatherInfo.getMaxTemp() + DEGREE_SIGN);
                textViewMinTemp.setText(weatherInfo.getMinTemp() + DEGREE_SIGN);
                textViewHumidity.setText(weatherInfo.getHumidity() + "%");
                textViewCity.setText(weatherInfo.getCity() + ", " + weatherInfo.getCountry());
            }

        }
    }

    private class GetWeather3hForecastByLocation extends AsyncTask<Location, Object, JSONObject> {

        @Override
        protected JSONObject doInBackground(Location... params) {
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            try {
                String wurl = "http://api.openweathermap.org/data/2.5/forecast?lat=";
                URL url = new URL(wurl + params[0].getLatitude() + "&lon=" + params[0].getLongitude() + "&appid=" + API_KEY + "&units=metric&lang=vi&cnt=8");
                //System.out.println(url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setReadTimeout(TIMEOUT);
                connection.connect();

                inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

                String kq = scanner.hasNext() ? scanner.next() : "";

                JSONObject rawJsonObject = new JSONObject(kq);
                JSONArray list = rawJsonObject.getJSONArray("list");
                JSONObject city = rawJsonObject.getJSONObject("city");
                String cityName = city.getString(NAME);
                String country = city.getString(COUNTRY);

                JSONObject jsonObject;
                String description = null;
                JSONObject main;
                Double temp_max = null;
                Double temp_min = null;
                Double humidity = null;
                Long datatime = 0L;
                String icon;

                ArrayList<WeatherInfo> weatherList = new ArrayList<>();

                JSONObject res = new JSONObject();
                JSONObject locationInfo = new JSONObject();
                JSONArray weatherInfoList = new JSONArray();

                for (int i = 0; i < list.length(); i++) {
                    jsonObject = list.getJSONObject(i);
                    datatime = jsonObject.getLong(DATA_TIME) * 1000;
                    description = jsonObject.getJSONArray(WEATHER).getJSONObject(0).getString(DESCRIPTION);
                    icon = jsonObject.getJSONArray(WEATHER).getJSONObject(0).getString(ICON);
                    main = jsonObject.getJSONObject(MAIN);
                    temp_max = main.getDouble(TEMP_MAX);
                    temp_min = main.getDouble(TEMP_MIN);
                    humidity = main.getDouble(HUMIDITY);
                    weatherList.add(new WeatherInfo(description, temp_max, temp_min, humidity, cityName, country, datatime, icon));
                }

                res.put("loc", locationInfo);
                res.put("weatherinfo", weatherInfoList);

                return res;

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
        protected void onPostExecute(JSONObject weatherInfo) {
            super.onPostExecute(weatherInfo);
            if (weatherInfo != null) {

            }

        }
    }
}
