package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "sexta")
public class FriDrive extends DayDrive {

	@Id
	@GeneratedValue
	private Long dbId;
	protected Long id;
	protected Date date;
	protected Double longitude;
	protected Double latitude;
	protected Double longitudeVertex;
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return this.date;
	}	
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getLatitude() {
		return this.latitude;
	}
	public Double getLongitude() {
		return this.longitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FriDrive) {
			FriDrive other = (FriDrive) obj;
			if (other.getId() == this.id)
				return true;
		}
		return false;
	}
}
