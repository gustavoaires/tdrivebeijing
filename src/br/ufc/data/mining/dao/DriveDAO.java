package br.ufc.data.mining.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufc.data.mining.model.Drive;

public class DriveDAO {
	private EntityManager manager;
	private EntityManagerFactory factory;
	
	public DriveDAO() {
		factory = Persistence.createEntityManagerFactory("drive");
		manager = factory.createEntityManager();
	}
	
	public void insert(Drive drive) {
		manager.getTransaction().begin();
		manager.persist(drive);
		manager.getTransaction().commit();
	}
	
	public void close() {
		manager.close();
	}
}
