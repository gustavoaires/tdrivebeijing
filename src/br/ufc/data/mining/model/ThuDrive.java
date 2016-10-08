package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "quinta")
public class ThuDrive extends DayDrive {

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
	public boolean isCore() {
		return this.iscore;
	}
	public void setIsCore(boolean iscore) {
		this.iscore = iscore;
	}
	public int getCluster() {
		return this.cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	public int getIdStudent() {
		return this.idStudent;
	}
	public boolean isVisited() {
		return this.visited;
	}
	public void setVisited(boolean value) {
		this.visited = value;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ThuDrive) {
			ThuDrive other = (ThuDrive) obj;
			if (other.getId() == this.id)
				return true;
		}
		return false;
	}
}
