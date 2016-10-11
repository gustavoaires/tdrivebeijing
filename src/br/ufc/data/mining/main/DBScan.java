package br.ufc.data.mining.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufc.data.mining.dao.DayDAO;
import br.ufc.data.mining.dao.ResultDAO;
import br.ufc.data.mining.dao.VertexDAO;
import br.ufc.data.mining.model.Cluster;
import br.ufc.data.mining.model.DayDrive;
import br.ufc.data.mining.model.FriDrive;
import br.ufc.data.mining.model.MonDrive;
import br.ufc.data.mining.model.ThuDrive;
import br.ufc.data.mining.model.TueDrive;
import br.ufc.data.mining.model.Vertex;
import br.ufc.data.mining.model.WedDrive;

public class DBScan {

	private static Set<DayDrive> outliers = new HashSet<DayDrive>();

	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {
		DayDAO dao = new DayDAO();
		VertexDAO vDao = new VertexDAO();
		final String[] days = { "segunda", "terca", "quarta", "quinta", "sexta" };
		final Class[] classes = { MonDrive.class, TueDrive.class, WedDrive.class, ThuDrive.class, FriDrive.class };
		// Dataset completo
		List<DayDrive> dataSet = null;
//		List<Vertex> vertices = null;
		// Clusters separados por dias
		HashMap<String, HashMap<Integer, Cluster>> dayClusters = new HashMap<String, HashMap<Integer, Cluster>>();
		HashMap<Integer, Cluster> c = new HashMap<Integer, Cluster>();
		Cluster cl = null;
		// for (int i = 0; i < 5; i++) {
		outliers.clear();
//		vertices = vDao.getAllVertex();
		dataSet = dao.getAllByDayAndHour(days[0], "13:00:00", "14:00:00", classes[0]);
//		mapMatching(dataSet, vertices);
		dayClusters.put(days[0], dbscan(dataSet, 0.003, 40));
		// }
		ResultDAO.delete();
		// for (int j = 0; j < 5; j++) {
		c = dayClusters.get(days[0]);
		System.out.println(c.size());
		for (int i = 1; i <= c.size(); i++) {
			cl = c.get(i);
			System.out.println("i: " + i);
			System.out.println(cl.getItsId());
			System.out.println("ID=" + cl.getItsId() + ";TAM=" + cl.getPoints().size());
			ResultDAO.insert(cl.getPoints());
		}
		// }
		ResultDAO.insert(outliers);

		dao.close();
		System.out.println(outliers.size());

	}

	/**
	 * @param dataSet
	 * @param eps
	 * @param minPoints
	 * @return List of Clusters
	 */

	private static boolean checkNeighborhood(DayDrive d, Set<DayDrive> neighbors, HashMap<Integer, Cluster> clusters) {

		for (DayDrive pt : neighbors) {
			if (pt.getCluster() > 0 && pt.isCore()) {
				int id = pt.getCluster();
				Cluster c = clusters.get(id);
				d.setCluster(pt.getCluster());
				c.add(d);
				return true;
			}
		}
		return false;
	}

	private static HashMap<Integer, Cluster> dbscan(List<DayDrive> dataSet, Double eps, int minPoints) {
		HashMap<Integer, Cluster> regions = new HashMap<Integer, Cluster>();
		Set<DayDrive> neighbors = null;
		System.out.println("Tamanho dataset " + dataSet.size());
		for (DayDrive point : dataSet) {
			if (!point.isVisited()) {

				point.setVisited(true);
				neighbors = regionQuery(point, eps, dataSet);

				if (checkNeighborhood(point, neighbors, regions))
					continue;

				if (!hasMinPoints(neighbors, minPoints)) {
					point.setCluster(-1);
					outliers.add(point);
				} else {

					Cluster cluster = new Cluster();
					expandCluster(point, neighbors, cluster, eps, minPoints, dataSet);
					if (cluster.getPoints().size() >= minPoints) {
						regions.put(cluster.getItsId(), cluster);
						point.setIsCore(true);
					} else {
						for (DayDrive p : cluster.getPoints()) {
							p.setCluster(-1);
							outliers.add(p);
						}
					}
				}
			}

			neighbors.clear();
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
			for (DayDrive d : tempNeighbors)
				if (!cluster.getPoints().contains(d))
					neighbors.add(d);
			tempNeighbors.clear();
		}

		return cluster;
	}

	private static Set<DayDrive> regionQuery(DayDrive point, Double eps, List<DayDrive> dataSet) {
		Set<DayDrive> neighbors = new HashSet<DayDrive>();
		neighbors.add(point);

		for (DayDrive other : dataSet)
			if (euclideanDistance(point, other) <= eps)
				neighbors.add(other);

		return neighbors;
	}

	private static Double euclideanDistance(DayDrive p1, DayDrive p2) {
		return Math.sqrt(Math.pow((p1.getLongitude() - p2.getLongitude()), 2)
				+ Math.pow((p1.getLatitude() - p2.getLatitude()), 2));
	}

	private static Double euclideanDistance(DayDrive p1, Vertex p2) {
		return Math.sqrt(Math.pow((p1.getLongitude() - p2.getLongitude()), 2)
				+ Math.pow((p1.getLatitude() - p2.getLatitude()), 2));
	}
	
	private static boolean hasMinPoints(Set<DayDrive> neighbors, int minPoints) {
		Set<Long> ids = new HashSet<>();
		for (DayDrive n : neighbors)
			if (!ids.contains(n.getId()))
				ids.add(n.getId());
		return ids.size() >= minPoints;
	}
	
	private static void mapMatching(List<DayDrive> points, List<Vertex> vertices) {
		Double menor, atual;
		Vertex candidate = null;
		for (DayDrive p: points) {
			menor = 10000000d; 
			atual = 0.;
			for (Vertex v: vertices) {
				atual = euclideanDistance(p, v);
				if (atual < menor) {
					menor = atual;
					candidate = v;
				}
			}
			p.setLatitudeVertex(candidate.getLatitude());
			p.setLongitudeVertex(candidate.getLongitude());
		}
	}
}
