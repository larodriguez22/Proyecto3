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
			System.out.println("2. Esquema JSON para persistir el grafo (cargar)");
			System.out.println("3. Esquema JSON para persistir el grafo (leer)");
			System.out.println("4. Cargar HTML");
			System.out.println("5. Grafica con ayuda de Google Maps la parte del grafo resultante de la carga de la zona delimitada por las siguientes coordenadas: Longitud min= -74.094723, Longitud max= -74.062707, Latitud min= 4.597714 y Latitud max= 4.621360");
			System.out.println("6. Encontrar el Id del Vértice de la malla vial más cercano por distancia Haversine");
			System.out.println("7. Encontrar el camino de costo mínimo para un viaje entre dos localizaciones geográficas de la ciudad");
			System.out.println("8. Determinar los n vértices con menor velocidad promedio en la ciudad de Bogotá");
			System.out.println("9. Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Prim");
			System.out.println("10. A partir de las coordenadas de una localización geográfica de la ciudad (lat, lon) de origen, indique cuáles vértices son alcanzables para un tiempo T ");
			System.out.println("11. Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Kruskal");
			System.out.println("12. Construir un nuevo grafo simplificado No dirigido de las zonas Uber");
			System.out.println("13. Calcular el camino de costo mínimo (algoritmo de Dijkstra)");
			System.out.println("14. Calcular los caminos de menor longitud (cantidad de arcos) a todas sus zonas alcanzables");
			System.out.println("15. Exit");
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
