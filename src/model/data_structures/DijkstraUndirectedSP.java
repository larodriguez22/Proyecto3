package model.data_structures;
import model.logic.Vertex;

/******************************************************************************
 *  Compilation:  javac DijkstraUndirectedSP.java
 *  Execution:    java DijkstraUndirectedSP input.txt s
 *  Dependencies: EdgeWeightedGraph.java IndexMinPQ.java Stack.java Edge.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *  % java DijkstraUndirectedSP tinyEWG.txt 6
 *  6 to 0 (0.58)  6-0 0.58000
 *  6 to 1 (0.76)  6-2 0.40000   1-2 0.36000
 *  6 to 2 (0.40)  6-2 0.40000
 *  6 to 3 (0.52)  3-6 0.52000
 *  6 to 4 (0.93)  6-4 0.93000
 *  6 to 5 (1.02)  6-2 0.40000   2-7 0.34000   5-7 0.28000
 *  6 to 6 (0.00)
 *  6 to 7 (0.74)  6-2 0.40000   2-7 0.34000
 *
 *  % java DijkstraUndirectedSP mediumEWG.txt 0
 *  0 to 0 (0.00)
 *  0 to 1 (0.71)  0-44 0.06471   44-93  0.06793  ...   1-107 0.07484
 *  0 to 2 (0.65)  0-44 0.06471   44-231 0.10384  ...   2-42  0.11456
 *  0 to 3 (0.46)  0-97 0.07705   97-248 0.08598  ...   3-45  0.11902
 *  ...
 *
 *  % java DijkstraUndirectedSP largeEWG.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (0.78)  0-460790 0.00190  460790-696678 0.00173   ...   1-826350 0.00191
 *  0 to 2 (0.61)  0-15786  0.00130  15786-53370   0.00113   ...   2-793420 0.00040
 *  0 to 3 (0.31)  0-460790 0.00190  460790-752483 0.00194   ...   3-698373 0.00172
 *
 ******************************************************************************/


import java.util.Stack;

import model.data_structures.IndexMinPQ;
import model.logic.Vertex;
	
public class DijkstraUndirectedSP
{

	private Edge[] edgeTo;
	private double[] distTo;
	private IndexMinPQ<Double> pq; 
	private String tipoPeso;
	private double tEsti = 0;


	public DijkstraUndirectedSP(Grafo G, int s, String pTipoPeso)
	{      
		tipoPeso = pTipoPeso;
		edgeTo = new Edge[G.V()+1];  
		distTo = new double[G.V()+1]; 
		pq = new IndexMinPQ<Double>(G.V()); 
		
		
		for (int v = 0; v < G.V()+1; v++)         
		{
			distTo[v] = Double.POSITIVE_INFINITY; 
			edgeTo[v] = null;
		}
		
		distTo[s] = 0.0;  
		
		pq.insert(s, distTo[s]); 
		while (!pq.isEmpty())   
		{
			int v = pq.delMin();	
			for(int i=0; i<G.darVertices().length;i++)
			for(Edge e : G.darVertices()[i].darArcos())
			{
				relax(e);
			}
		}
			
	}  	
	
	
	private void relax(Edge e) {
		
        int v = e.either(), w = e.other(e.either());
        if(tipoPeso.equals("distancia"))
        {
        	
        	if (distTo[w] > distTo[v] + e.weightHarvesineDistance()) 
        	{
                distTo[w] = distTo[v] + e.weightHarvesineDistance();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
        else if(tipoPeso.equals("tiempo"))
        {
        	if (distTo[w] > distTo[v] + e.weightTiempoPromedio()) {
                distTo[w] = distTo[v] + e.weightTiempoPromedio();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
        else if(tipoPeso.equals("velocidad"))
        {
        	if (distTo[w] > distTo[v] + e.weightVelocidadDelArco()) {
                distTo[w] = distTo[v] + e.weightVelocidadDelArco();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
        
    }


    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int s) {
        int V = distTo.length;
        if (s < 0 || s >= V)
            throw new IllegalArgumentException("vertex " + s + " is not between 0 and " + (V-1));
    }


	public double distTo(int idVertice) 
	{   
		return distTo[idVertice];   
	} 
	
	public boolean hasPathTo(int idVertice) 
	{   
		return distTo[idVertice] < Double.POSITIVE_INFINITY;  
	} 

	public Edge buscarArcoA(int pIDV)
	{
		boolean seEncontro = false;
		Edge aRetornar = null;
		for(int i = 0; i < edgeTo.length&&!seEncontro; i++)
		{
			if(edgeTo[i]!=null)
			{
				if(edgeTo[i].other(edgeTo[i].either())==pIDV)
				{
					aRetornar = edgeTo[i];
				}
			}
			
		}
		return aRetornar;
	}

	public Grafo grafoDistanciaMinima(int idVertice, Grafo g)
	{   
		Grafo camino =null;
		if (!hasPathTo(idVertice)) return camino;
		else
		{
			camino = new Grafo<>(idVertice);
		}
		 
		for(Edge e = edgeTo[idVertice]; e != null; e = edgeTo[e.either()]) 
		{
			Vertex x = g.getVertex(e.other(e.either()));
			Vertex y = g.getVertex(e.either());
			camino.addVertex(x.darId(), x);
			camino.addVertex(y.darId(), y);
			camino.addEdge(y.darId(), x.darId(), e.weightHarvesineDistance());
			
		}
			
		return camino; 
	}
	public Iterable<Edge> pathTo(int idVertice, Grafo g)
	{   
		Stack camino = null;
		if (!hasPathTo(idVertice))
		{
			return camino;
		}
		else
		{
			camino = new Stack<>();
		}
		 
		for (Edge e = edgeTo[idVertice]; e != null; e = edgeTo[e.either()]) 
		{
			camino.push(e);
		}
			
		return camino; 
	}
	public double tiempoEsti(int v)
	{
		Iterable<Edge> arcos = pathTo(v, null);
		for (Edge e: arcos) 
		{
			tEsti = tEsti + e.weightTiempoPromedio();
		}
		return tEsti;
	}
    public Iterable<Edge> edges() {
    	Iterable pl=null;
        return pl;
    }

}
