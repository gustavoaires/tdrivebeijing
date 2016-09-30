package br.ufc.data.mining.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufc.data.mining.dao.DayDAO;
import br.ufc.data.mining.model.Cluster;
import br.ufc.data.mining.model.DayDrive;
import br.ufc.data.mining.model.FriDrive;
import br.ufc.data.mining.model.MonDrive;
import br.ufc.data.mining.model.ThuDrive;
import br.ufc.data.mining.model.TueDrive;
import br.ufc.data.mining.model.WedDrive;

public class DBScan {

	/*
	 * Falta modificar o retorno do metodo dbscan para guardar os resultados do
	 * processamento Esses resultados serao as regioes As regioes podem ser um
	 * list de sets
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {
		DayDAO dao = new DayDAO();
		final String[] days = { "segunda", "terca", "quarta", "quinta", "sexta" };
		final Class[] classes = { MonDrive.class, TueDrive.class, WedDrive.class, ThuDrive.class, FriDrive.class };
		// Dataset completo
		List<DayDrive> dataSet = new ArrayList<>();
		// Clusters separados por dias
		HashMap<String, List<Cluster>> dayClusters = new HashMap<String, List<Cluster>>();

//		for (int i = 0; i < 5; i++) {
		dataSet = dao.getAllByDayAndHour(days[4], "13:00:00", "13:20:00", classes[4]);
		dayClusters.put(days[4], dbscan(dataSet, 0.250, 3500));
		
		List<Cluster> c = dayClusters.get(days[4]);
		
		for (Cluster cl : c) {
			for (DayDrive dd : cl.getPoints()) {
				System.out.println(dd.getCluster() + ";" + dd.getId() + ";" +
						dd.getLongitude() + ";" + dd.getLatitude() + ";" +
						dd.isCore() + ";");
			}
		}
		for (Cluster cl : c)
			System.out.println("ID=" + cl.getItsId() + ";TAM=" + cl.getPoints().size());
		System.out.println(c.size());
//		}
		dao.close();
	}

	/**
	 * @param dataSet
	 * @param eps
	 * @param minPoints
	 * @return List of Clusters  
	 */
	private static List<Cluster> dbscan(List<DayDrive> dataSet, Double eps, int minPoints) {
		List<Cluster> regions = new ArrayList<Cluster>();
		
		for (DayDrive point : dataSet) {
			if (!point.isVisited()) {
				point.setVisited(true);
				Set<DayDrive> neighbors = regionQuery(point, eps, dataSet);
				if (!hasMinPoints(neighbors, minPoints))
					point.setCluster(-1);
				else {
					Cluster cluster = new Cluster();
					expandCluster(point, neighbors, cluster, eps, minPoints, dataSet);
					regions.add(cluster);
					point.setIsCore(true);
				}
			}
		}
		
		return regions;
	}

	/**
	 * @param point candidato a core
	 * @param neighbors estao dentro do eps
	 * @param cluster novo cluster a ser criado
	 * @param eps raio de distancia maxima
	 * @param minPoints quantidade minima de pontos no cluster
	 * @param dataSet conjunto dos dados
	 * @param clusterId id do novo cluster criado
	 * @return novo cluster preenchido
	 */
	private static Cluster expandCluster(DayDrive point, Set<DayDrive> neighbors, Cluster cluster, Double eps,
			int minPoints, List<DayDrive> dataSet) {
		cluster.add(point);
		point.setIsCore(true);
		Set<DayDrive> pNeighbors = new HashSet<DayDrive>();
		Set<DayDrive> tempNeighbors = new HashSet<DayDrive>();

		while (!neighbors.isEmpty()) {
			for (DayDrive p : neighbors) {
				if (!p.isVisited()) {
					p.setVisited(true);
					pNeighbors = regionQuery(p, eps, dataSet);
					if (hasMinPoints(pNeighbors, minPoints)) {
						tempNeighbors.addAll(pNeighbors);
						p.setIsCore(true);
					}
				}
				if (p.getCluster() == 0)
					cluster.add(p);
			}
			neighbors.clear();
			for (DayDrive d: tempNeighbors)
				if (!cluster.getPoints().contains(d))
					neighbors.add(d);
			tempNeighbors.clear();
		}

		return cluster;
	}

	private static Set<DayDrive> regionQuery(DayDrive point, Double eps, List<DayDrive> dataSet) {
		Set<DayDrive> neighbors = new HashSet<DayDrive>();
		neighbors.add(point);

		for (DayDrive other : dataSet) {
			if (euclideanDistance(point, other) <= eps)
				neighbors.add(other);
		}

		return neighbors;
	}

	private static Double euclideanDistance(DayDrive p1, DayDrive p2) {
		return Math.sqrt(Math.pow((p1.getLongitude() - p2.getLongitude()), 2)
				+ Math.pow(p1.getLatitude() - p2.getLatitude(), 2));
	}
	
	private static boolean hasMinPoints(Set<DayDrive> neighbors, int minPoints) {
		Set<Long> ids = new HashSet<>();
		for (DayDrive n : neighbors)
			if (!ids.contains(n.getId())) 
				ids.add(n.getId());
		System.out.println(ids.size());
		return ids.size() >= minPoints;
	}
}
