package br.ufc.data.mining.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufc.data.mining.model.Vertex;

public class VertexDAO {

	private EntityManager manager;
	private EntityManagerFactory factory;
	
	public VertexDAO() {
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
	
	public List<Vertex> getAllVertex() {
		return manager.createQuery("select v from vertex as v", Vertex.class).getResultList();
	}
}
