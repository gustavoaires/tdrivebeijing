package br.ufc.data.mining.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "vertices")
public class Vertex {

	@Id
	private Long id;
	private Double longitude;
	private Double latitude;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
