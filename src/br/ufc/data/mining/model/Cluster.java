package br.ufc.data.mining.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

	private static int ID = 1;
	private int itsId;
	private Set<DayDrive> points;
	
	public Cluster() {
		itsId = ID++;
		points = new HashSet<DayDrive>();
	}

	public int getItsId() {
		return itsId;
	}
	
	public void add(DayDrive point) {
		point.setCluster(this.itsId);
		points.add(point);
	}
	
	public Set<DayDrive> getPoints() {
		return points;
	}
}
