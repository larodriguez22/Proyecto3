package model.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

import model.data_structures.Edge;
import model.data_structures.Grafo;
import model.data_structures.Graph;
import model.data_structures.Haversine;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.opencsv.CSVReader;


/**
 * Definicion del modelo del mundo
 *
 */

public class MVCModelo<K> {

	private SeparateChainingHashST<String, Viaje> viajes;
	private Grafo<Integer, Informacion> grafo;

	public MVCModelo() throws Exception
	{
		viajes= new SeparateChainingHashST<>(1000000);
	}
	public void cargar(){
		try {
			cargarTxtHash();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cargarViajes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cargarViajes() throws Exception
	{
		int contador = 0;
		String rutaViaje = "data/bogota-cadastral-2018-1-WeeklyAggregate.csv";
		CSVReader lector = new CSVReader(new FileReader(rutaViaje));
		String [] partes;
		while ((partes = lector.readNext()) != null) 
		{
			if(contador!=0)
			{
				Viaje viajeNuevo = new Viaje(partes[0], partes[1], partes[2], partes[3], partes[4]);
				viajes.put(partes[0]+"-"+partes[1], viajeNuevo);
			}
			contador++;
		}
		lector.close();
	}

	public void cargarTxtHash() throws IOException
	{
		String rutaE="./data/bogota_arcos.txt";
		String rutaV="./data/bogota_vertices.txt";
		try{
			// Abre el archivo utilizando un FileReader
			FileReader reader = new FileReader(rutaV);
			// Utiliza un BufferedReader para leer por l�neas
			BufferedReader lector = new BufferedReader( reader );   
			// Lee l�nea por l�nea del archivo
			String linea = lector.readLine( );
			while(linea!=null)
			{
				// Parte la l�nea con cada coma
				//id;long;lat;MOVEMENT_ID
				String[] partes = linea.split( ";" );
				Informacion inf= new Informacion(Integer.parseInt(partes[0]), Double.parseDouble(partes[1]), Double.parseDouble(partes[2]), Integer.parseInt(partes[3]));
				grafo.addVertex(Integer.parseInt(partes[0]), inf);
				linea=lector.readLine();
			}
			lector.close( );
			reader.close( );
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try{
			// Abre el archivo utilizando un FileReader
			FileReader reader = new FileReader(rutaE);
			// Utiliza un BufferedReader para leer por l�neas
			BufferedReader lector = new BufferedReader( reader );   
			// Lee l�nea por l�nea del archivo
			String linea = lector.readLine( );
			
			
			while(linea!=null)
			{
				// Parte la l�nea con cada coma
				String[] partes = linea.split( " " );
								
				for (int i = 1; i<partes.length;i++) {
					Informacion infoInicial= grafo.getInfoVertex(Integer.parseInt(partes[0]));
					Informacion infoFinal=grafo.getInfoVertex(Integer.parseInt(partes[i]));
					double cost=Haversine.distance(infoInicial.getLat(), infoInicial.getLon(), infoFinal.getLat(), infoFinal.getLon());
					Edge e=grafo.addEdge(Integer.parseInt(partes[0]), Integer.parseInt(partes[i]), cost);
					int moveID1=grafo.getInfoVertex(Integer.parseInt(partes[0])).getMovementID();
					int moveID2=grafo.getInfoVertex(Integer.parseInt(partes[i])).getMovementID();
					double costo2=calcularCosto2(moveID1, moveID2);
					e.setWeight2(costo2);
					double costo3=cost/costo2;
					e.setWeight3(costo3);
				}
				linea=lector.readLine();
			}
			lector.close( );
			reader.close( );
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("La cantidad total de arcos es "+ grafo.E());
		System.out.println("La cantidad total de vertices es "+grafo.V());
		// Cierra los lectores y devuelve el resultado
	}

	public void crearJson() {
		// TODO Auto-generated method stub

        Gson gson = new Gson();
        // Java objects to File
        try (FileWriter writer = new FileWriter("./data/data.json")) {
            gson.toJson(grafo, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	public void leerJson(){
		Gson gson = new Gson();
		String path = "./data/data.json";
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(path));
			grafo = gson.fromJson(reader, Informacion.class);
			System.out.println("numero Vertices:" +grafo.V());
			System.out.println("numero Arcos:" +grafo.E());
			System.out.println("Cantidad CC:" +grafo.cc());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public double calcularCosto2(int pMovementIdInicio, int pMovementIdFinal)
	{
		double rta = 0.0;
		if(pMovementIdInicio==pMovementIdFinal)
		{
			rta = calcularTiempoPromedioEntreZonas(pMovementIdInicio+"", pMovementIdFinal+"");
			if(rta == 0.0)
			{
				rta = 10;
			}
		}
		else
		{
			rta = calcularTiempoPromedioEntreZonas(pMovementIdInicio+"", pMovementIdFinal+"");
			if(rta == 0.0)
			{
				rta = 100;
			}
		}
		return rta;
	}
	
	public double calcularTiempoPromedioEntreZonas(String pIdZona1, String pIdZona2)
	{ 
		double rta = 0.0;
		int contador = 0;
		Iterator iter = (Iterator) viajes;
		while(iter.hasNext())
		{
			Viaje actual = (Viaje)iter.next();
			rta+=actual.getMean_travel_time();
			contador++;
		}
		if(contador<=0)
		{
			return 0.0;
		}
		return rta/contador;
	}
	
	public void hacerHTML() throws IOException
	{
		String ruta = "./data/mapa.html";
		int contador = 0;
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(ruta);
		}catch (Exception e) {
			e.printStackTrace();
		}
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">");
		writer.println("<meta charset=\"utf-8\">");
		writer.println("<title>Simple Polylines</title>");
		writer.println("<style>");
		writer.println("#map {");
		writer.println("height: 100%;");
		writer.println("}");
		writer.println("html,");
		writer.println("body {");
		writer.println("height: 100%;");
		writer.println("margin: 0;");
		writer.println("padding: 0;");
		writer.println("}");
		writer.println("</style>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<div id=\"map\"></div>");
		writer.println("<script>");
		writer.println("function initMap() {");
		writer.println("var map = new google.maps.Map(document.getElementById('map'), {");
		writer.println("zoom: 5,");
		writer.println("center: {");
		writer.println("lat: 40.162838,");
		writer.println("lng: -3.494526");
		writer.println("},");
		writer.println("mapTypeId: 'roadmap'");
		writer.println("});");
		writer.println("var line;");
		writer.println("var path;");
		for(Vertex<Integer,Informacion> inter: grafo.darVertices())
		{
			if(inter != null)
			{
				for(Edge arcos : inter.darArcos())
				{
					if(arcos != null)
					{
						Informacion llegada = grafo.getInfoVertex(arcos.other(0));
						if(llegada != null)
						{
							Informacion info = (Informacion) inter.darInfo();
							writer.println("line = [");
							writer.println("{");
							writer.println("lat: " + info.getLat() + ",");
							writer.println("lng: " + info.getLon());
							writer.println("},");
							writer.println("{");
							writer.println("lat: " + llegada.getLat()+ ",");
							writer.println("lng: " + llegada.getLon());
							writer.println("}");
							writer.println("];");
							writer.println("path = new google.maps.Polyline({");
							writer.println("path: line,");
							writer.println("strokeColor: '#FF0000',");
							writer.println("strokeWeight: 2");
							writer.println("});");
							writer.println("path.setMap(map);");
							contador++;
							System.out.println(contador);
						}
					}
				}
			}
		}
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();

	}
	public void darVerticeMasCercano(double latitud, double longitud) {
		// TODO Auto-generated method stub
		
	}
	public void encontrarCaminoMenorTiempoPromedio(double latitudO, double longitudO, double latitudD,
			double longitudD) {
		// TODO Auto-generated method stub
		
	}
	public void nVerticesMenorVelocidadPromedio(int n) {
		// TODO Auto-generated method stub
		
	}
	public void calcularMSTPrim() {
		// TODO Auto-generated method stub
		
	}
	public void caminoMenorCostoHaversine(double latitudO, double longitudO, double latitudD, double longitudD) {
		// TODO Auto-generated method stub
		
	}
	public void indicarVerticesAlcanzablesTiempoT(double latitud, double longitud, Double tiempo) {
		// TODO Auto-generated method stub
		
	}
	public void calcularMSTKruskal() {
		// TODO Auto-generated method stub
		
	}
	public void construirNuevoGrafo() {
		// TODO Auto-generated method stub
		
	}
	public void calcularDijkstra() {
		// TODO Auto-generated method stub
		
	}
	public void caminoMenorLongituDesdeLaZona(double latitudO, double longitudO) {
		// TODO Auto-generated method stub
		
	}
	
}