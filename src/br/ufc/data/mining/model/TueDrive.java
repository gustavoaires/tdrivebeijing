package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "terca")
public class TueDrive extends DayDrive {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		if (obj instanceof TueDrive) {
			TueDrive other = (TueDrive) obj;
			if (other.getId() == this.id)
				return true;
		}
		return false;
	}
}
