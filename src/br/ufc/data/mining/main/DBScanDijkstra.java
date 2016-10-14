package br.ufc.data.mining.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.data.mining.dao.DayDAO;
import br.ufc.data.mining.dao.ResultDAO;
import br.ufc.data.mining.dao.VertexRoadDAO;
import br.ufc.data.mining.model.Cluster;
import br.ufc.data.mining.model.DayDrive;
import br.ufc.data.mining.model.FriDrive;
import br.ufc.data.mining.model.MonDrive;
import br.ufc.data.mining.model.Road;
import br.ufc.data.mining.model.ThuDrive;
import br.ufc.data.mining.model.TueDrive;
import br.ufc.data.mining.model.Vertex;
import br.ufc.data.mining.model.WedDrive;

public class DBScanDijkstra {

	private static Set<DayDrive> outliers = new HashSet<DayDrive>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VertexRoadDAO vDao = new VertexRoadDAO();
		DayDAO dao = new DayDAO();

		final String[] days = { "segunda", "terca", "quarta", "quinta", "sexta" };
		@SuppressWarnings("rawtypes")
		final Class[] classes = { MonDrive.class, TueDrive.class, WedDrive.class, ThuDrive.class, FriDrive.class };
		// Dataset completo
		List<DayDrive> dataSet = dao.getAllByDayAndHour(days[4], "13:00:00", "13:20:00", classes[4]);
		List<Vertex> nodes = vDao.getAllVertex();
		List<Road> edges = vDao.getAllRoad();

		addRoadsVertexes(edges, nodes);
		mapMatching(dataSet, nodes);

		Dijkstra algorithm = new Dijkstra(edges);

		// Clusters separados por dias
		Map<String, HashMap<Integer, Cluster>> dayClusters = new HashMap<String, HashMap<Integer, Cluster>>();
		// HashMap<String, HashMap<Integer, Cluster>>();
		Map<Integer, Cluster> c = new HashMap<Integer, Cluster>();
		Cluster cl = null;
		// // for (int i = 0; i < 5; i++) {
		// outliers.clear();
		// vertices = vDao.getAllVertex();

		dayClusters.put(days[4], dbscan(dataSet, 1., 40, algorithm));
		// }
		ResultDAO.delete();
		// for (int j = 0; j < 5; j++) {
		c = dayClusters.get(days[4]);
		// System.out.println(c.size());
		for (Map.Entry<Integer, Cluster> cl1 : c.entrySet()) {
			cl = cl1.getValue();
			System.out.println(cl.getItsId());
			System.out.println("ID=" + cl.getItsId() + ";TAM=" + cl.getPoints().size());
			ResultDAO.insert(cl.getPoints(), "result_net_dist");
		}
		// }
		ResultDAO.insert(outliers, "result_net_dist");
		//
		// dao.close();
		System.out.println(outliers.size());

	}

	private static HashMap<Integer, Cluster> dbscan(List<DayDrive> dataSet, Double eps, int minPoints,
			Dijkstra algorithm) {
		HashMap<Integer, Cluster> regions = new HashMap<Integer, Cluster>();
		Map<Vertex, Double> allDistances = null;
		Set<DayDrive> neighbors = null;

		for (DayDrive point : dataSet) {
			if (!point.isVisited()) {
				algorithm.execute(point.getVertex());

				point.setVisited(true);
				allDistances = algorithm.getDistances();

				neighbors = regionQuery(allDistances, dataSet, eps);

				if (checkNeighborhood(point, neighbors, regions))
					continue;

				if (!hasMinPoints(neighbors, minPoints)) {
					point.setCluster(-1);
					outliers.add(point);
				} else {
					Cluster cluster = new Cluster();
					System.out.println("expandCluster");
					expandCluster(point, neighbors, cluster, eps, minPoints, dataSet, algorithm);
					
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

	private static Cluster expandCluster(DayDrive point, Set<DayDrive> neighbors, Cluster cluster, Double eps,
			int minPoints, List<DayDrive> dataSet, Dijkstra algorithm) {
		cluster.add(point);
		point.setIsCore(true);
		Set<DayDrive> pNeighbors = new HashSet<DayDrive>();
		Set<DayDrive> tempNeighbors = new HashSet<DayDrive>();
		Map<Vertex, Double> allDistances = null;

		while (!neighbors.isEmpty()) {
			for (DayDrive p : neighbors) {
				if (!p.isVisited()) {
					p.setVisited(true);
					allDistances = algorithm.getDistances();
					pNeighbors = regionQuery(allDistances, dataSet, eps);
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

	private static Set<DayDrive> regionQuery(Map<Vertex, Double> allDistances, List<DayDrive> dataSet, Double eps) {
		Set<DayDrive> neighbors = new HashSet<>();
		for (Map.Entry<Vertex, Double> entry : allDistances.entrySet()) {
			for (DayDrive p : dataSet) {
				if (p.getVertex().equals(entry.getKey()) && entry.getValue() <= eps)
					neighbors.add(p);
			}
		}
		return neighbors;
	}

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

	private static boolean hasMinPoints(Set<DayDrive> neighbors, int minPoints) {
		Set<Long> ids = new HashSet<>();
		for (DayDrive n : neighbors)
			if (!ids.contains(n.getId()))
				ids.add(n.getId());
		return ids.size() >= minPoints;
	}

	private static Double euclideanDistance(DayDrive p1, Vertex p2) {
		return Math.sqrt(Math.pow((p1.getLongitude() - p2.getLongitude()), 2)
				+ Math.pow((p1.getLatitude() - p2.getLatitude()), 2));
	}

	private static void mapMatching(List<DayDrive> points, List<Vertex> vertices) {
		Double menor, atual;
		Vertex candidate = null;
		for (DayDrive p : points) {
			menor = Double.MAX_VALUE;
			atual = Double.MAX_VALUE;
			for (Vertex v : vertices) {
				atual = euclideanDistance(p, v);
				if (atual < menor) {
					menor = atual;
					candidate = v;
				}
			}
			p.setVertex(candidate);
		}
	}

	private static void addRoadsVertexes(List<Road> edges, List<Vertex> nodes) {
		for (Road edge : edges) {
			for (Vertex node : nodes) {
				if (edge.getOrigem().longValue() == node.getId().longValue())
					edge.setSource(node);
				if (edge.getDestino().longValue() == node.getId().longValue())
					edge.setDestination(node);
			}
		}
	}

}
