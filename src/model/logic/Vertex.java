package model.logic;
import java.util.LinkedList;

import model.data_structures.Edge;
import model.data_structures.Queue;

public class Vertex<K, V> {

	private K id;
	private int CanArcos;
	private boolean check;
	private V info;
	private Vertex<K, V> conectado;
	private Edge[] arcos;

	public Vertex(K pid, V pinfo,Edge[] parcos)
	{
		id = pid;
		info = pinfo;
		arcos = parcos;
	}

	public int darCantidadArcos()
	{
		if(arcos == null)
		{
			return 0;
		}
		return CanArcos;
	}

	public Edge[] darArcos()
	{
		return arcos;
	}

	public void agregarArco(Edge i)
	{
		if(arcos == null)
		{
			arcos[0] = i;
			CanArcos++;
		}
		else
		{
			for(int j = 1; j<6 ; j++)
			{
				if(arcos[j] == null)
				{
					arcos[j] = i;
				}
			}
			CanArcos++;
		}
	}

	public K darId()
	{
		return id;
	}

	public void cambiarInformacion(V newInfo)
	{
		info = newInfo;
	}

	public void marcar()
	{
		check = true;
	}

	public void desmarcar()
	{
		check = false;
	}

	public V darInfo()
	{
		return info;
	}

	public boolean estaMarcado()
	{
		return check;
	}

	public void conectadoA(Vertex a)
	{
		conectado = a;
	}

	public Vertex darConexion()
	{
		return conectado;
	}
}