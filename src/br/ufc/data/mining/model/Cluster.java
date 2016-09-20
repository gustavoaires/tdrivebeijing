package br.ufc.data.mining.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	private static int ID = 0;
	private int itsId;
	public List<DayDrive> points;
	
	public Cluster() {
		itsId = ID++;
		points = new ArrayList<>();
	}

	public int getItsId() {
		return itsId;
	}

	public void setItsId(int itsId) {
		this.itsId = itsId;
	}
}
