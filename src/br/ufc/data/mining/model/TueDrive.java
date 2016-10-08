package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "terca")
public class TueDrive extends DayDrive {

	@Id
	@GeneratedValue
	private Long dbId;
	protected Long id;
	protected Date date;
	protected Double longitude;
	protected Double latitude;
	
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return this.date;
	}
	@SuppressWarnings("deprecation")
	public void setDate(Date date) {
		this.date = date;
		int day = 0;
		if (date.getDate() == 4)
			day = 1;
		else if (date.getDate() == 5)
			day = 2;
		else if (date.getDate() == 6)
			day = 3;
		else if (date.getDate() == 7)
			day = 4;
		else if (date.getDate() == 8)
			day = 5;
		super.setWeekday(day);
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
		if (obj instanceof TueDrive) {
			TueDrive other = (TueDrive) obj;
			if (other.getId() == this.id)
				return true;
		}
		return false;
	}
}
