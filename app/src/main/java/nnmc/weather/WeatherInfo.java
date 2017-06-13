package nnmc.weather;

/**
 * Created by nnmchau on 6/13/2017.
 */

public class WeatherInfo {
    private String description;
    private Double temp;
    private Double maxTemp;
    private Double minTemp;
    private Double humidity;
    private String city;
    private String country;

    public WeatherInfo(String description, Double temp, Double maxTemp, Double minTemp, Double humidity, String city, String country) {
        this.description = description;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.city = city;
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
