package br.ufc.data.mining.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufc.data.mining.model.DayDrive;

public class DayDAO {
	private EntityManager manager;
	private EntityManagerFactory factory;
	
	public DayDAO() {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DayDrive> getAllByDayAndHour(String day, String begin, String end, Class c) {
		String[] dateBeginEnd = createDate(day, begin, end);
		System.out.println(day);
		List<DayDrive> result = (List<DayDrive>)(List<?>) 
				manager.createQuery("select x from " + day + " as x "
				+ "where date between '"+ dateBeginEnd[0] +"' and '" + dateBeginEnd[1] + "'",
				c).getResultList();
		return result;
	}
	
	private String[] createDate(String table, String begin, String end) {
		String[] dateBeginEnd = new String[2];
		switch (table) {
			case "segunda":
				dateBeginEnd[0] = "2008-02-04 " + begin;
				dateBeginEnd[1] = "2008-02-04 " + end;
				break;
			case "terca":
				dateBeginEnd[0] = "2008-02-05 " + begin;
				dateBeginEnd[1] = "2008-02-05 " + end;
				break;
			case "quarta":
				dateBeginEnd[0] = "2008-02-06 " + begin;
				dateBeginEnd[1] = "2008-02-06 " + end;
				break;
			case "quinta":
				dateBeginEnd[0] = "2008-02-07 " + begin;
				dateBeginEnd[1] = "2008-02-07 " + end;
				break;
			case "sexta":
				dateBeginEnd[0] = "2008-02-08 " + begin;
				dateBeginEnd[1] = "2008-02-08 " + end;
				break;
			default:
				break;
		}
		return dateBeginEnd;
	}
}
