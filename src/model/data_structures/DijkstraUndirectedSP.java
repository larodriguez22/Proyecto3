package model.data_structures;


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

}
