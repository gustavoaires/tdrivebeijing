package br.ufc.data.mining.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

	private static int ID = 0;
	private int itsId;
	public Set<DayDrive> points;
	
	public Cluster() {
		itsId = ID++;
		points = new HashSet<DayDrive>();
	}

	public int getItsId() {
		return itsId;
	}

	public void setItsId(int itsId) {
		this.itsId = itsId;
	}
}
