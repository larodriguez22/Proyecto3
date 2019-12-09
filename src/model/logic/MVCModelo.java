package model.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import model.data_structures.DepthFirstPaths;
import model.data_structures.DijkstraUndirectedSP;
import model.data_structures.Edge;
import model.data_structures.Grafo;
import model.data_structures.Graph;
import model.data_structures.Haversine;
import model.data_structures.IndexMinPQ;
import model.data_structures.KruskalMST;
import model.data_structures.LazyPrimMST;
<<<<<<< HEAD
import model.data_structures.MinPQ;
=======
import model.data_structures.MaxPQ;
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;
import model.data_structures.StdOut;

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
			// Utiliza un BufferedReader para leer por líneas
			BufferedReader lector = new BufferedReader( reader );   
			// Lee línea por línea del archivo
			String linea = lector.readLine( );
			while(linea!=null)
			{
				// Parte la línea con cada coma
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
			// Utiliza un BufferedReader para leer por líneas
			BufferedReader lector = new BufferedReader( reader );   
			// Lee línea por línea del archivo
			String linea = lector.readLine( );


			while(linea!=null)
			{
				// Parte la línea con cada coma
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

	public void hacerHTMLInicial() throws IOException
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
	public int darVerticeMasCercano(double latitud, double longitud) {
		// TODO Auto-generated method stub
		double distanciaFinal=0;
		int idVertice=-1;
		for(int i=0; i<grafo.V();i++)
		{
			double distanciaActual=Haversine.distance(latitud, longitud, grafo.getInfoVertex(i).getLat(), grafo.getInfoVertex(i).getLon());
			if(distanciaActual>distanciaFinal)
			{
				distanciaFinal=distanciaActual;
				idVertice=i;
			}
		}
		return idVertice;

	}
	//YO
	public Grafo encontrarGrafoTiempoMinimo(double pLatOrigen,double pLongOrigen, double pLatDestino, double pLongDestino)
	{
		//TODO : metodo
		int vIdOrigen = darVerticeMasCercano(pLatOrigen, pLongOrigen);
		int vIdDestino = darVerticeMasCercano(pLatDestino, pLongDestino);
		Grafo camino = grafo.grafoMenorDistanciaA(vIdOrigen, vIdDestino);
		return camino;
	}
	public Iterable encontrarCaminoTiempoMinimo(double pLatOrigen,double pLongOrigen, double pLatDestino, double pLongDestino)
	{
		int vIdOrigen = darVerticeMasCercano(pLatOrigen, pLongOrigen);
		int vIdDestino = darVerticeMasCercano(pLatDestino, pLongDestino);
		Iterable camino = grafo.caminoMenorDistanciaA(vIdOrigen, vIdDestino);
		return camino;
	}
	
	//YO
	public void nVerticesMenorVelocidadPromedio(int n) {
<<<<<<< HEAD
		Vertex []auxV=grafo.darVertices();
		ArrayList <Double> velocidades=new ArrayList <Double>();
		int mayorcc=0;
		
		for(int i=0;i<auxV.length;i++){
			Edge[]auxE=auxV[i].darArcos();
			double suma=0;
			for (int j=0;j<auxE.length;j++){
				suma=auxE[0].weightVelocidadDelArco()+suma;
						
			}
			double division=suma/auxE.length;
			velocidades.add(division);
			
		}
		int mayor=0;
		for(int i=0;i<n;i++){
			double menor=velocidades.get(0);
			int index=0;
			for(int j=0;j<velocidades.size();j++){
				if(menor>velocidades.get(j)){
					menor=velocidades.get(j);
					index=j;
				}
			}
			
			Informacion e=(Informacion) auxV[index].darInfo();
			System.out.println("--------"+i+"---------");
			System.out.println("Latitud:"+e.getLat());
			System.out.println("Longitud:"+e.getLon());
			System.out.println("Identificador:"+e.getMovementID());
			marcarGmaps(e.getLat(),e.getLon());
			Iterator ite =(Iterator) grafo.getCC(index);
			int cantidad=0;
			while(ite.hasNext()){
				Informacion a=(Informacion)ite.next();
				System.out.println("ID componentes conectados:"+a.getMovementID());
				cantidad++;
			}
			System.out.println("Cantidad CC");
			velocidades.remove(index);
			if (mayor<cantidad){
				mayor=cantidad;
				mayorcc=e.getMovementID();
			}
			
			
		}
		crearCCmapa(auxV[mayorcc]);	
	}
	private void crearCCmapa(Vertex vertex) {
		// TODO Auto-generated method stub
		
		
	}
	public void calcularMSTPrim() {
		// TODO Auto-generated method stub
			long startTime= System.currentTimeMillis();
				LazyPrimMST mst=grafo.mstPrim();
				long endTime= System.currentTimeMillis();
				System.out.println( "Tiempo que se demora el algoritmo Lazyprim:" + (endTime-startTime));
				System.out.println("El numero de vertices es "+ mst.V());
				System.out.println("Los arcos son: ");
				for (Edge e : mst.edges()) {
		            System.out.println("Arco: "+e.toString());
		        }
				System.out.println("El peso es: "+mst.weight());
				
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
=======
		Queue<String> cola = new Queue<String>();
		int tamanio = n;
		MaxPQ<Vertex> menores = new MaxPQ<>();

		for(Vertex<Integer,Informacion> inter : grafo.darVertices())
		{
			double promedioactual;
			int cantidad = 0;
			int suma = 0;
			if(inter != null)
			{
				for(Edge e : inter.darArcos()) 
				{
					if(e != null)
					{
						cantidad++;
						suma += e.weightTiempoPromedio();
					}
				}
				promedioactual = suma/cantidad;
				Vertex obj1 = inter;
				boolean caso = false;
				if(menores.size() < tamanio)
				{
					menores.insert(obj1);
				}

			}
		}

		cola.enqueue("Estos son los " + tamanio + " vértices con menor tiempo de viaje");
		for(int i=0 ; i<menores.size() ; i++)
		{
			Vertex v=menores.delMax();
			Informacion info = (Informacion) v.darInfo();
			cola.enqueue("ID: " + v.darId() + " Latitud: " + info.getLat() + " Longitud: " + info.getLon());
		}
	}
	public void calcularMSTPrim() {
		// TODO Auto-generated method stub
		LazyPrimMST mst=grafo.mstPrim();
		//NO SE COMO PONER CUANTO SE DEMORA EL ALGORITMO
		System.out.println("El numero de vertices es "+ mst.V());
		System.out.println("Los arcos son: ");
		for (Edge e : mst.edges()) {
			System.out.println("Arco: "+e.toString());
		}
		System.out.println("El peso es: "+mst.weight());

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
				for(Edge arcos : mst.edges())
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
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
	public void caminoMenorCostoHaversine(double latitudO, double longitudO, double latitudD, double longitudD) {
		// TODO Auto-generated method stub
		double directa=Haversine.distance( latitudO, longitudO, latitudD, longitudD);
		double distanciahaversine=0;
		double tiempo=0;
		int vertice1=darVerticeMasCercano(latitudO, longitudO);
		int vertice2=darVerticeMasCercano(latitudD, longitudD);
			DijkstraUndirectedSP sp=grafo.mstPrimD(vertice1);
			Iterator ite=(Iterator)sp.pathTo(vertice2);
			int vertice0=vertice1;
			while(ite.hasNext()){
				Edge aux=(Edge) ite.next();
				distanciahaversine=aux.weightHarvesineDistance()+distanciahaversine;
				tiempo=aux.weightTiempoPromedio();
				vertice0=aux.other(vertice0);
				Informacion h=grafo.getInfoVertex(vertice0);
				System.out.println("Latitud: "+h.getLat());
				System.out.println("Longitud: "+h.getLon());
				System.out.println("ID: "+h.getMovementID());
			}
			System.out.println("Tiempo: "+tiempo);
			System.out.println("Distancia Haversine: "+distanciahaversine);
		
		
	}
	public void indicarVerticesAlcanzablesTiempoT(double latitud, double longitud, Double tiempo) {
		// TODO Auto-generated method stub
		int vertice1=darVerticeMasCercano(latitud, longitud);
		Vertex []auxil=grafo.darVertices();
				
		for (int i=0;i<viajes.size();i++){
			if (viajes.get(vertice1+"-"+i)!=null){
				if(viajes.get(vertice1+"-"+i).getMean_travel_time()<tiempo){
				@SuppressWarnings("rawtypes")
				Vertex aux=auxil[(int)viajes.get(vertice1+"-"+i).getDstid()];
				Informacion a=(Informacion) aux.darInfo();
				
				System.out.println("latitud:"+a.getLat());
				System.out.println("longitud:"+a.getLon());
				System.out.println("ID:"+(int)viajes.get(vertice1+"-"+i).getDstid());
				marcarGmaps(latitud,longitud);
				}
			}
			
		}

	}
	public void calcularMSTKruskal() {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		long startTime= System.currentTimeMillis();
		KruskalMST mst=grafo.mstPrimk();
		long endTime= System.currentTimeMillis();
		System.out.println( "Tiempo que se demora el algoritmo Kruskal:" + (endTime-startTime));
		for (Edge e : mst.edges()) {
            System.out.println("Arco: "+e.toString());
        }
		System.out.println("El peso es: "+mst.weight());
		System.out.println("El peso es: "+mst.weight());
		
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
				for(Edge arcos : mst.edges())
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
		
=======
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5

	}
	public void construirNuevoGrafo() {
		// TODO Auto-generated method stub

	}
	public void calcularDijkstra() {
		// TODO Auto-generated method stub

	}
	public void caminoMasDistanteDeLaZonaCorto(double latitudO, double longitudO) {
		// TODO Auto-generated method stub
		int vertice=darVerticeMasCercano(latitudO, longitudO);

	}
<<<<<<< HEAD
	//metodos extra
	public void marcarGmaps(double longitud, double latitud)
	{
		String ruta = "./data/mapa.html";
=======
	public void crearArchivoHTML(String pNombreArchivo, Grafo G, double pLatMin, double pLatMax, double pLongMin, double pLongMax, boolean pColor) {
		// TODO Auto-generated method stub
		Grafo nuevo;
		if(G==null)
		{
			nuevo = grafo;
		}
		else
		{
			nuevo = G;
		}
		String ruta = "./data/"+pNombreArchivo+".html";
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
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
<<<<<<< HEAD
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
=======
		writer.println("  <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">");
		writer.println("  <meta charset=\"utf-8\">");
		writer.println("  <title>"+pNombreArchivo+"</title>");
		writer.println("  <style>");
		writer.println("    #map {");
		writer.println("      height: 100%;");
		writer.println("    }");
		writer.println("    html,");
		writer.println("    body {");
		writer.println("      height: 100%;");
		writer.println("      margin: 0;");
		writer.println("      padding: 0;");
		writer.println("    }");
		writer.println("  </style>");
		writer.println("</head>");



		writer.println("<body>");
		writer.println("  <div id=\"map\"></div>");
		writer.println("  <script>");
		writer.println("    function initMap() {");
		writer.println("      var map = new google.maps.Map(document.getElementById('map'), {");
		writer.println("        zoom: 15.5,");
		writer.println("center: {");
		writer.println("lat: 4.609537,");
		writer.println("lng: -74.078715");
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
		writer.println("},");
		writer.println("mapTypeId: 'roadmap'");
		writer.println("});");
		writer.println("var line;");
		writer.println("var path;");
<<<<<<< HEAD
		for(Vertex<Integer,Informacion> inter: grafo.darVertices())
		{
			if(inter != null)
			{
				/*for(Edge arcos : mst.edges())
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
=======

		boolean color = pColor;
		for(Vertex vertice: nuevo.darVertices())
		{
			if(vertice!=null)
			{

				double latV= ((Informacion) nuevo.getInfoVertex(vertice)).getLat();
				double longV= ((Informacion) nuevo.getInfoVertex(vertice)).getLon();
				//Default values: 4.621360||4.597714||-74.062707||-74.094723

				double latMin = 0;
				double latMax = 0;
				double longMin = 0;
				double longMax = 0;

				if(pLatMax == -1&&pLatMin==-1&&pLongMax==-1&&pLongMin==-1)
				{
					latMin = 4.597714;
					latMax = 4.621360;
					longMin = -74.094723;
					longMax = -74.062707;
				}
				else
				{
					latMin = pLatMin;
					latMax = pLatMax;
					longMin = pLongMin;
					longMax = pLongMax;
				}

				if(latV<=latMax&&latV>=latMin&&longV>=longMin&&longV<=longMax)
				{

					writer.println("	  var circle = new google.maps.Circle ({");
					writer.println("		map: map,");
					writer.println("		center: new google.maps.LatLng("+latV+","+longV+"),");
					writer.println("		radius : 10,");
					writer.println("		strokeColor : '#000000',");
					if(color)
					{
						writer.println("		fillColor : 'red'");
						color = false;
					}
					else
					{
						writer.println("		fillColor : 'blue'");
					}
					writer.println("		});");

					Edge[] arcos = vertice.darArcos();




					for(Edge arco : arcos)
					{



						Informacion vDestino = (Informacion) nuevo.getInfoVertex(arco.either());

						double latVDest= vDestino.getLat();
						double longVDest= vDestino.getLon();

						if(!arco.isMarked()&&(latVDest<=latMax&&latVDest>=latMin&&longVDest>=longMin&&longVDest<=longMax))
						{
							writer.println("line = [{");
							writer.println("lat: " + latV + ",");
							writer.println("lng: " + longV);
							writer.println("},");
							writer.println("{");
							writer.println("lat: " + latVDest + ",");
							writer.println("lng: " + longVDest);
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
							writer.println("}");
							writer.println("];");
							writer.println("path = new google.maps.Polyline({");
							writer.println("path: line,");
							writer.println("strokeColor: '#FF0000',");
<<<<<<< HEAD
							writer.println("strokeWeight: 2");
							writer.println("});");
							writer.println("path.setMap(map);");
							contador++;
							System.out.println(contador);
						}
					}
				}*/
			}
		}
=======
							writer.println("strokeWeight: 1");
							writer.println("});");
							writer.println("path.setMap(map);");
							contador++;
							if(arco.other((int) vertice.darId())!=-1)
							{
								arco.marcar();
							}
						}				
					}
				}
			}

		}

>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
		writer.println("}");
		writer.println("</script>");
		writer.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=&callback=initMap\">");
		writer.println("</script>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();
<<<<<<< HEAD
		
	}
}
=======
		System.out.println("Se genero el archivo, lo podrá encontrar en la carpeta data.");

	}
}
>>>>>>> cc1412037a3cdeea52b561a86c487d6e11ca55f5
