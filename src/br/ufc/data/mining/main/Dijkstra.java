package br.ufc.data.mining.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.data.mining.model.Road;
import br.ufc.data.mining.model.Vertex;

public class Dijkstra {
	private final List<Road> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Double> distances;

	public Dijkstra(List<Road> edges) {
		this.edges = new ArrayList<Road>(edges);
	}

	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distances = new HashMap<Vertex, Double>();
		distances.put(source, 0d);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}
	
	public Map<Vertex, Double> getDistances() {
		return this.distances;
	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(node) + getDistance(node, target) < getShortestDistance(target)) {
				distances.put(target, getShortestDistance(node) + getDistance(node, target));
				unSettledNodes.add(target);
			}
		}
	}

	private Double getDistance(Vertex node, Vertex target) {
		for (Road edge : edges)
			if (edge.getSource().equals(node) && edge.getDestination().equals(target))
				return edge.getPeso();
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Road edge : edges)
			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) 
				neighbors.add(edge.getDestination());
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null)
				minimum = vertex;
			else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private Double getShortestDistance(Vertex destination) {
		Double d = distances.get(destination);
		if (d == null)
			return Double.MAX_VALUE;
		else
			return d;
	}
}
