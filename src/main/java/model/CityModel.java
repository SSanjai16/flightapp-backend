package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "city")
public class CityModel {
	private String cityname;

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

}
  