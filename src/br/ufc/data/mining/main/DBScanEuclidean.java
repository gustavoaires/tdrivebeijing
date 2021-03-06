package br.ufc.data.mining.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.data.mining.dao.DayDAO;
import br.ufc.data.mining.dao.ResultDAO;
import br.ufc.data.mining.model.Cluster;
import br.ufc.data.mining.model.DayDrive;
import br.ufc.data.mining.model.FriDrive;
import br.ufc.data.mining.model.MonDrive;
import br.ufc.data.mining.model.ThuDrive;
import br.ufc.data.mining.model.TueDrive;
import br.ufc.data.mining.model.WedDrive;

public class DBScanEuclidean {

	private static Set<DayDrive> outliers = new HashSet<DayDrive>();

	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {
		DayDAO dao = new DayDAO();

		final String[] days = { "segunda", "terca", "quarta", "quinta", "sexta" };
		final Class[] classes = { MonDrive.class, TueDrive.class, WedDrive.class, ThuDrive.class, FriDrive.class };
		// Dataset completo
		List<DayDrive> dataSet = dao.getAllByDayAndHour(days[4], "13:00:00", "14:00:00", classes[4]);

		// Clusters separados por dias
		Map<String, HashMap<Integer, Cluster>> dayClusters = new HashMap<String, HashMap<Integer, Cluster>>();
		// HashMap<String, HashMap<Integer, Cluster>>();
		Map<Integer, Cluster> c = new HashMap<Integer, Cluster>();
		Cluster cl = null;
		// for (int i = 0; i < 5; i++) {
		// outliers.clear();
		// vertices = vDao.getAllVertex();

		dayClusters.put(days[4], dbscan(dataSet, 0.004, 10));
		// }
		ResultDAO.delete();
		// for (int j = 0; j < 5; j++) {
		c = dayClusters.get(days[4]);
		// System.out.println(c.size());
		for (Map.Entry<Integer, Cluster> cl1 : c.entrySet()) {
			cl = cl1.getValue();
			System.out.println(cl.getItsId());
			System.out.println("ID=" + cl.getItsId() + ";TAM=" + cl.getPoints().size());
			ResultDAO.insert(cl.getPoints(), "result_fri");
		}
		// }
		System.out.println(outliers.size());
		ResultDAO.insert(outliers, "result_fri");
		//
		// dao.close();
	}

	private static HashMap<Integer, Cluster> dbscan(List<DayDrive> dataSet, Double eps, int minPoints) {
		HashMap<Integer, Cluster> regions = new HashMap<Integer, Cluster>();
		Set<DayDrive> neighbors = null;
		System.out.println("Tamanho dataset " + dataSet.size());
		for (DayDrive point : dataSet) {
			if (!point.isVisited()) {

				point.setVisited(true);
				neighbors = regionQuery(point, eps, dataSet);

				if (!hasMinPoints(neighbors, minPoints)) {
					point.setCluster(-1);
					outliers.add(point);
				} else {
					Cluster cluster = new Cluster();
					expandCluster(point, neighbors, cluster, eps, minPoints, dataSet);
					regions.put(cluster.getItsId(), cluster);
					point.setIsCore(true);
				}
			}
			neighbors.clear();
		}
		return regions;
	}

	private static Cluster expandCluster(DayDrive point, Set<DayDrive> neighbors, Cluster cluster, Double eps,
			int minPoints, List<DayDrive> dataSet) {
		cluster.add(point);
		point.setIsCore(true);
		Set<DayDrive> pNeighbors = new HashSet<DayDrive>();
		Set<DayDrive> tempNeighbors = new HashSet<DayDrive>();
		Set<DayDrive> visited = new HashSet<DayDrive>();

		while (!neighbors.isEmpty()) {
			visited.addAll(neighbors);
			for (DayDrive p : neighbors) {
				if (!p.isVisited()) {
					p.setVisited(true);
					visited.add(p);
					pNeighbors = regionQuery(p, eps, dataSet);
					if (hasMinPoints(pNeighbors, minPoints)) {
						tempNeighbors.addAll(pNeighbors);
						p.setIsCore(true);
					}
				}
				if (p.getCluster() == 0)
					cluster.add(p);
			}
			neighbors.addAll(tempNeighbors);
			for (DayDrive d : visited)
				if (neighbors.contains(d))
					neighbors.remove(d);
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

	private static boolean hasMinPoints(Set<DayDrive> neighbors, int minPoints) {
		Set<Long> ids = new HashSet<>();
		for (DayDrive n : neighbors)
			if (!ids.contains(n.getId()))
				ids.add(n.getId());
		return ids.size() >= minPoints;
	}
}
