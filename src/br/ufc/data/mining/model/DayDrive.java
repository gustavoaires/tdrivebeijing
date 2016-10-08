package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Transient;

public abstract class DayDrive {

	@Transient
	protected boolean iscore = false;
	@Transient
	protected int cluster = 0;
	@Transient
	protected final int idStudent = 363854;
	@Transient
	protected boolean visited = false;
	@Transient
	protected int weekday = 0;

	public abstract Long getId();

	public abstract void setId(Long id);

	public abstract Date getDate();

	public abstract void setDate(Date date);

	public abstract Double getLatitude();

	public abstract Double getLongitude();

	public abstract void setLatitude(Double latitude);

	public abstract void setLongitude(Double longitude);

	public int getWeekDay() {

		return this.weekday;
	}

	public void setWeekday(int weekDay) {
		this.weekday = weekDay;
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
}
