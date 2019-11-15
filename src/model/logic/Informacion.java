package model.logic;

public class Informacion {
	private double lat;
	private double lon;
	private int MOVEMENT_ID;

	public Informacion(double plat, double plon, int pmove)
	{
		lat = plat;
		lon = plon;
		MOVEMENT_ID=pmove;
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