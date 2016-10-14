package br.ufc.data.mining.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "road")
public class Road {
	@Id
	private Long id;
	private Long origem;
	private Long destino;
	private Double peso;
	@Transient
	private Vertex source;
	@Transient
	private Vertex destination;
	
	public Vertex getSource() {
		return source;
	}
	public void setSource(Vertex source) {
		this.source = source;
	}
	public Vertex getDestination() {
		return destination;
	}
	public void setDestination(Vertex destination) {
		this.destination = destination;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrigem() {
		return origem;
	}
	public void setOrigem(Long origem) {
		this.origem = origem;
	}
	public Long getDestino() {
		return destino;
	}
	public void setDestino(Long destino) {
		this.destino = destino;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Road) {
				Road other = (Road) obj;
				if (this.origem == other.origem 
						&& this.destino == other.destino
						&& this.id == other.id)
					return true;
			}
		}
		return false;
	}
}
