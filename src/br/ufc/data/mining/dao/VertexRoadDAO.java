package br.ufc.data.mining.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufc.data.mining.model.Road;
import br.ufc.data.mining.model.Vertex;

public class VertexRoadDAO {

	private EntityManager manager;
	private EntityManagerFactory factory;
	
	public VertexRoadDAO() {
		factory = Persistence.createEntityManagerFactory("drive");
		manager = factory.createEntityManager();
		begin();
	}

	public void begin() {
		manager.getTransaction().begin();
	}
	
	public void close() {
		manager.close();
	}
	
	public List<Road> getAllRoad() {
		return manager.createQuery("select r from road as r", Road.class).getResultList();
	}
	
	public List<Vertex> getAllVertex() {
		return manager.createQuery("select v from vertex as v", Vertex.class).getResultList();
	}
}
