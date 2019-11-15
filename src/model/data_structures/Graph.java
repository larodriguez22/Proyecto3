package model.data_structures;

import java.util.Iterator;

import model.logic.Edge;
import model.logic.Informacion;
import model.logic.Vertex;

public class Graph <K extends Comparable<K>, Val> 
{
	//Codigo tomado de: https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/Graph.java
	//					https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/DepthFirstPaths.java
	//Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.

	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;
	private int E;
	private LinearProbingHashST<K, Vertex> adj;
	private LinearProbingHashST<K, Boolean> marked;
	private LinearProbingHashST<K, Integer> cc;
	private int[] id;           // id[v] = id of connected component containing v
	private int[] size;         // size[id] = number of vertices in given component
	private int count;          // number of connected components


	/**
	 * Initializes an empty graph with {@code V} vertices and 0 edges.
	 * param V the number of vertices
	 *
	 * @param  V number of vertices
	 * @throws IllegalArgumentException if {@code V < 0}
	 */
	public Graph(int V) {
		this.V = V;
		this.E = 0;
		adj = new LinearProbingHashST<>(V);
		marked = new LinearProbingHashST<>(V);
		cc = new LinearProbingHashST<>(V);
	}

	/**
	 * Returns the number of vertices in this graph.
	 *
	 * @return the number of vertices in this graph
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the number of edges in this graph.
	 *
	 * @return the number of edges in this graph
	 */
	public int E() {
		return E;
	}

	/**
	 * Adds the undirected edge v-w to this graph.
	 *
	 * @param  v one vertex in the edge
	 * @param  w the other vertex in the edge
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
	 */
	public void addEdge(K idVertexIni, K idVertexFin, double cost) {
		E++;
		Vertex v1 = (Vertex) adj.get(idVertexIni);
		Vertex v2 = (Vertex) adj.get(idVertexFin);
		String costoArco = String.valueOf(cost);
		String idv1 = String.valueOf(idVertexIni);
		String idv2 = String.valueOf(idVertexFin);
		Edge edge = new Edge(idv1, idv2, costoArco);
		v1.getAdj().enqueue(v2);
		v2.getAdj().enqueue(v1);
		v1.getEdgeTos().enqueue(edge);
		v2.getEdgeTos().enqueue(edge);
	}


	/**
	 * 
	 *
	 * @param  v one vertex in the edge
	 */
	public void addVertex(K idVertex, Val infoVertex) {
		String id = String.valueOf(idVertex);
		Vertex nuevo = new Vertex((Informacion)infoVertex, Integer.parseInt(id));
		adj.put(idVertex, nuevo);
	}

	/**
	 * Returns the vertices adjacent to vertex {@code v}.
	 *
	 * @param  v the vertex
	 * @return the vertices adjacent to vertex {@code v}, as an iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<K> adj(K idVertex) {
		Queue<K> ids = new Queue<K>();
		Vertex aux = (Vertex)adj.get(idVertex);
		Queue<Vertex> adyacentes = aux.getAdj();
		int tam = adyacentes.size();
		for (int i = 0; i < tam; i++) {
			Vertex actual = adyacentes.dequeue();
			K idAct = (K)(Object)actual.getId();
			ids.enqueue(idAct);
			adyacentes.enqueue(actual);
		}
		Queue<K> adya = (Queue<K>) ids.iterator();
		return adya;
	}

	/**
	 * Computes the connected components of the undirected graph {@code G}.
	 *
	 * @param G the undirected graph
	 */
	public void CC() {
		Iterator<K> marcados = (Iterator<K>) marked.keys();
		while(marcados.hasNext()) {
			K llave = marcados.next();
			boolean valor = (boolean)marked.get(llave);
			if (!valor) {
				dfs(llave);
				count++;	
			}
		}
	}

	public void dfs(K v) {
		marked.put(v, true);
		Vertex vAct = (Vertex) adj.get(v);
		Queue<Vertex> adyacentes = vAct.getAdj();
		int tam = adyacentes.size();
		for (int i = 0; i < tam; i++) {
			Vertex actual = adyacentes.dequeue();
			if (marked.get((K) actual) == null) {
				marked.put((K) actual, true);
			}
			else {
				boolean valor = marked.get((K) actual);
				if (!valor) {
					K id = (K)(Object)actual.getId();
					dfs(id);
				}
			}
			adyacentes.enqueue(actual);
		}
	}

	/**
	 * Returns the number of connected components in the graph {@code G}.
	 *
	 * @return the number of connected components in the graph {@code G}
	 */
	public int ccn() {
		return count;
	}

	public Val getInfoVertex(K idVertex) {
		Vertex v1 = (Vertex)adj.get(idVertex);
		Val infoV = (Val)v1.getInfo();
		return infoV;
	}

	public void setInfoVertex(K idVertex, Val infoVertex) {
		Vertex v1 =(Vertex)adj.get(idVertex);
		v1.setInfo((Informacion)infoVertex);

	}

	public double getCostArc(K idVertexIni, K idVertexFin) {
		double costo = -1;
		Vertex vIn = (Vertex)adj.get(idVertexIni);
		Queue<Edge> edges = vIn.getEdgeTos();
		int tam = edges.size();
		boolean encontrado = false;
		for (int i = 0; i < tam && !encontrado; i++) {
			Edge actual = edges.dequeue();
			if (actual.getIdInicio() == (Integer)idVertexIni && actual.getIdFinal() == (Integer)idVertexFin) {
				costo =  actual.getCosto();
			}
		}
		return costo;
	}

	public void setCostArc(K idVertexIni, K idVertexFin, double cost) {
		Vertex vIn = (Vertex)adj.get(idVertexIni);
		Queue<Edge> arcos = vIn.getEdgeTos();
		int tam = arcos.size();
		boolean encontrado = false;
		for (int i = 0; i < tam && !encontrado; i++) {
			Edge actual = arcos.dequeue();
			if (actual.getIdInicio() == (Integer)idVertexIni && actual.getIdFinal() == (Integer)idVertexFin) {
				actual.setCostArc(cost);
			}
			arcos.enqueue(actual);
		}

	}

	public void uncheck() {
		Iterator<K> marca = (Iterator<K>) marked.keys();
		while (marca.hasNext()) {
			K actual = marca.next();
			marked.put(actual, false);
		}
	}

	public Iterable<K> getCC(K idVertex) {
		Queue<K> queue = new Queue<K>();
		int ccID = cc.get(idVertex);
		Iterator<K> ccs = (Iterator<K>) cc.keys();
		while (ccs.hasNext()) {
			K llave = ccs.next();
			int valorAct = cc.get(llave);
			if(valorAct == ccID){
				queue.enqueue(llave);
			}
		}
		return queue;
	}
	
	public LinearProbingHashST<K, Boolean> marcados(){
		return marked;
	}
}