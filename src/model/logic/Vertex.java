package model.logic;
import java.util.LinkedList;

import model.data_structures.Queue;

public class Vertex<I> implements Comparable<Vertex>
{
	private Queue<Edge> edgeTo;
	private Informacion info;
	private boolean marked;
	private int id;
	private Queue<Vertex> adj = new Queue<Vertex>(); 

	public Vertex(Informacion info, int id)
	{
		this.info = info;
		marked = false;
		this.id = id;
	}
	
	public Queue<Vertex> getAdj(){
		return adj;
	}
	
	public Queue<Edge> getEdgeTos(){
		return edgeTo;
	}
	
	public String toString() {
		return "Vertice [id=" + id + ", longitud=" + info.getLon() + ", latitud=" + info.getLat()+ ", MOVEMENT_ID=" + info.getMovementID() + "]";
	}

	@Override
	public int compareTo(Vertex o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Informacion getInfo()
	{
		return info;
	}

	public void setInfo(Informacion pInfo)
	{
		info = pInfo;
	}

	public void desmarked()
	{
		marked = false;
	}
	public void marked()
	{
		marked = true;
	}
	public boolean isMarked()
	{
		return marked;
	}
	
	public int getId()
	{
		return id;
	}
}