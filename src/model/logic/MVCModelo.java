package model.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.data_structures.ArregloDinamico;
import model.data_structures.Graph;
import model.data_structures.IArregloDinamico;

/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private IArregloDinamico datos;
	
	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public MVCModelo()
	{
		datos = new ArregloDinamico(7);
	}
	
	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public MVCModelo(int capacidad)
	{
		datos = new ArregloDinamico(capacidad);
	}
	
	public void cargarTxtGraph() throws IOException
	{
		// Abre el archivo utilizando un FileReader
		FileReader reader = new FileReader( "./data/Nodes_of_red_vial-wgs84_shp.txt"  );
		// Utiliza un BufferedReader para leer por líneas
		BufferedReader lector = new BufferedReader( reader );   
		// Lee línea por línea del archivo
		String linea = lector.readLine( );
		while(linea!=null)
		{
			// Parte la línea con cada coma
			String[] partes = linea.split( "," );
			Graph nodoAux= new Graph(partes[0], partes[1], partes[2]);
			int key=Integer.parseInt(partes[0]);
			nodos.put(key, nodoAux);
			linea=lector.readLine();
		}
		// Cierra los lectores y devuelve el resultado
		lector.close( );
		reader.close( );
		System.out.println("El numero actual de nodos cargados es: "+nodos.size());
	}
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return datos.darTamano();
	}

	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 */
	public void agregar(String dato)
	{	
		datos.agregar(dato);
	}
	
	/**
	 * Requerimiento buscar dato
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 */
	public String buscar(String dato)
	{
		return datos.buscar(dato);
	}
	
	/**
	 * Requerimiento eliminar dato
	 * @param dato Dato a eliminar
	 * @return dato eliminado
	 */
	public String eliminar(String dato)
	{
		return datos.eliminar(dato);
	}


}
