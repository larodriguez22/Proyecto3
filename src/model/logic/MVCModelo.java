package model.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;

import model.data_structures.Edge;
import model.data_structures.Grafo;
import model.data_structures.Graph;
import model.data_structures.Haversine;
import model.data_structures.Queue;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


/**
 * Definicion del modelo del mundo
 *
 */

public class MVCModelo<K> {

	private Grafo<Integer, Informacion> grafo;

	private Vertex vertices;
	private Edge arcos;
	private Informacion infos;

	private Queue<Integer> ids = new Queue<>();
	private Queue<String> values = new Queue<>();

	public MVCModelo() throws Exception
	{
		ids=new Queue<>();
		values=new Queue<>();
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
			FileReader reader = new FileReader(rutaV);
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
					grafo.addEdge(Integer.parseInt(partes[0]), Integer.parseInt(partes[i]), cost);
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
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public void cantidadComponentesConectados() {
		// TODO Auto-generated method stub
		grafo.CC();
		//usa los metodos de cc y dfs juntos
		System.out.println("Cantidad de componentes conexos: "+ grafo.ccn());
		
	}

	public void graficaGoogleMaps() {
		// TODO Auto-generated method stub
		
	}
}