package model.logic;

public class Informacion {
	private int id;
	private double lat;
	private double lon;
	private int MOVEMENT_ID;

	public Informacion(int pId, double pLat, double pLon, int pMove)
	{
		id=pId;
		lat = pLat;
		lon = pLon;
		MOVEMENT_ID=pMove;
	}

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

	public int getMovementID()
	{
		return MOVEMENT_ID;
	}

	public boolean marked() {
		return true;
		// TODO Auto-generated method stub
		
	}

	public boolean isMarked() {
		boolean rta= true;
		while (marked()==false)
		{
			rta = false;
		}
		return rta;
		// TODO Auto-generated method stub
	}

	public String toString() {
		return "Vertice [longitud=" + lon + ", latitud=" + lat + ", MOVEMENT_ID=" + MOVEMENT_ID + "]";
	}
}