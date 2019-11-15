package view;

import model.logic.MVCModelo;

public class MVCView 
{
	    /**
	     * Metodo constructor
	     */
	    public MVCView()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Cargar");
			System.out.println("2. Esquema JSON para persistir el grafo");
			System.out.println("3. Cantidad de componentes conectados en el grafo construido utilizando los métodos de búsqueda por profundidad (dfs) y componentes conectados (cc) provistos por el API");
			System.out.println("4. Grafica con ayuda de Google Maps la parte del grafo resultante de la carga de la zona delimitada por las siguientes coordenadas: Longitud min= -74.094723, Longitud max= -74.062707, Latitud min= 4.597714 y Latitud max= 4.621360");
			System.out.println("5. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}		
		
		public void printModelo(MVCModelo modelo)
		{
			// TODO implementar
		}
}
