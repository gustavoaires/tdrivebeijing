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
	 * Falta modificar o retorno do metodo dbscan para guardar os resultados do processamento
	 * Esses resultados serao as regioes
	 * As regioes podem ser um list de sets
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) {
		DayDAO dao = new DayDAO();
		String[] days = { "segunda", "terca", "quarta", "quinta", "sexta" };
		Class[] classes = { MonDrive.class, TueDrive.class, WedDrive.class, ThuDrive.class, FriDrive.class };
		// Dataset completo
		List<DayDrive> dataSet = new ArrayList<>();
		// Clusters separados por dias
		HashMap<String, List<Cluster>> dayClusters = new HashMap<String, List<Cluster>>();
		
		for (int i = 0; i < 5; i++) {
			dataSet = dao.getAllByDayAndHour(days[0], "13:00:00", "14:00:00", classes[0]);
			dayClusters.put(days[0], dbscan(dataSet, 1.2, 8));
		}
		dao.close();
	}
	
	/**
	 * 
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
				if (neighbors.size() < minPoints)
					point.setIsCore(false);
				else {
					Set<DayDrive> c = new HashSet<DayDrive>();
					Cluster cluster = new Cluster();
					cluster.points.addAll(expandCluster(point, neighbors, c, eps, 
							minPoints, dataSet, cluster.getItsId()));
					regions.add(cluster);
				}
			}
		}
		
		return regions;
	}

	/**
	 * 
	 * @param point candidato a core
	 * @param neighbors estao dentro do eps
	 * @param cluster novo cluster a ser criado
	 * @param eps raio de distancia maxima
	 * @param minPoints quantidade minima de pontos no cluster
	 * @param dataSet conjunto dos dados
	 * @param clusterId id do novo cluster criado
	 * @return novo cluster preenchido
	 */
	private static Set<DayDrive> expandCluster(DayDrive point, Set<DayDrive> neighbors, Set<DayDrive> cluster, 
			Double eps,	int minPoints, List<DayDrive> dataSet, int clusterId) {
		cluster.add(point);
		Set<DayDrive> pNeighbors= new HashSet<DayDrive>();
		// Precisa rever essa parte do algoritmo
		// É necessario adicionar mais elementos no conjunto enquanto percorre
		do {
			for (DayDrive p : neighbors) {
				if (!p.isVisited()) {
					p.setVisited(true);
					pNeighbors = regionQuery(p, eps, dataSet);
					if (pNeighbors.size() >= minPoints);
				}
				if (p.getCluster() == -1) {
					cluster.add(p);
					p.setCluster(clusterId);
				}
			}
			neighbors.addAll(pNeighbors);
		} while (false);
		
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
		return Math.sqrt(Math.pow((p1.getLongitude() - p2.getLongitude()), 2) + 
				Math.pow(p1.getLatitude() - p2.getLatitude(), 2));
	}

}
