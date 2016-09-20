package br.ufc.data.mining.model;

import java.util.Date;

import javax.persistence.Transient;

public abstract class DayDrive {
	
	@Transient
	protected boolean iscore;
	@Transient
	protected int cluster = -1;
	@Transient
	protected final int idStudent = 363854;
	@Transient
	protected boolean visited = false;
	
	public abstract Long getId();
	public abstract void setId(Long id);
	public abstract Date getDate();
	public abstract void setDate(Date date);
	public abstract Double getLatitude();
	public abstract Double getLongitude();
	public abstract void setLatitude(Double latitude);
	public abstract void setLongitude(Double longitude);
	public abstract boolean isCore();
	public abstract void setIsCore(boolean iscore);
	public abstract int getCluster();
	public abstract void setCluster(int cluster);
	public abstract int getIdStudent();
	public abstract boolean isVisited();
	public abstract void setVisited(boolean value);
}
