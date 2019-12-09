package controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import model.data_structures.Edge;
import model.data_structures.Grafo;
import model.logic.MVCModelo;
import model.logic.Vertex;
import view.MVCView;

public class Controller {

	/* Instancia del Modelo*/
	private MVCModelo modelo;

	/* Instancia de la Vista*/
	private MVCView view;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new MVCView();
		try {
			modelo = new MVCModelo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
			case 1:
				System.out.println("--------- \nCargar datos ");
				int capacidad = lector.nextInt();

				try {
					modelo = new MVCModelo();
					modelo.cargarTxtHash();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}					
				break;

			case 2:
				System.out.println("--------- \nEsquema JSON para persistir el grafo: (cargar)");
				dato = lector.next();
				modelo.crearJson();		
				break;
				
			case 3: 
				System.out.println("--------- \n Esquema JSON para persistir el grafo: (leer) \n---------"); 
				try {
					modelo.hacerHTMLInicial();dato = lector.next();
					modelo.leerJson();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;	
				
			case 4: 
				System.out.println("--------- \n Cargar HTML \n---------"); 
				try {
					modelo.hacerHTMLInicial();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;	

			case 5: 
				System.out.println("--------- \n Encontrar el Id del Vértice de la malla vial más cercano por distancia Haversine !! \n---------"); 
				System.out.println("Escriba la latitud de la zona que desea consultar:");
				double latitud=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona que desea consultar:");
				double longitud=lector.nextDouble();
				int id=modelo.darVerticeMasCercano( latitud, longitud);
				System.out.println("El ID del vertice mas cercano a la zona dada es: "+id);
				
				break;	
				
			case 6: 
				System.out.println("--------- \n Encontrar el camino de costo mínimo para un viaje entre dos localizaciones geográficas de la ciudad \n---------"); 
				System.out.println("Escriba la latitud de la zona de origen que desea consultar:");
				double latitudO=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona de origen que desea consultar:");
				double longitudO=lector.nextDouble();
				System.out.println("Escriba la latitud de la zona de destino que desea consultar:");
				double latitudD=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona de destino que desea consultar:");
				double longitudD=lector.nextDouble();
				modelo.encontrarCaminoTiempoMinimo(latitudO, longitudO, latitudD, longitudD);
				
				Scanner lector6 = new Scanner(System.in);
				view.printMessage("Ingrese latitud origen");
				double latOrigen = lector6.nextDouble();
				view.printMessage("Ingrese longitud origen");
				double longOrigen = lector6.nextDouble();
				view.printMessage("Ingrese latitud destino");
				double latDestino = lector6.nextDouble();
				view.printMessage("Ingrese longitud destino");
				double longDestino = lector6.nextDouble();
				try {
					Grafo tiempoMinimo = modelo.encontrarGrafoTiempoMinimo(latOrigen, longOrigen, latDestino, longDestino);
					Iterable caminoDistanciaMinima = modelo.encontrarCaminoTiempoMinimo(latOrigen, longOrigen, latDestino, longDestino);
					
					double totalTiempo = 0;
					double totalDistancia = 0;
					int cantidadVertices=0;
					if(caminoDistanciaMinima==null)
					{
						view.printMessage("No existe un camino entre las coordenadas dadas.");
					}
					else
					{
						Iterator it = caminoDistanciaMinima.iterator();
						view.printMessage("El camino a seguir es:");
						Edge a;
						while(it.hasNext())
						{					
							a = (Edge) it.next();
							if(a!=null)
							{
								
								view.printMessage(a.either()+"->"+a.other(a.either()));
								totalTiempo+=a.weightTiempoPromedio();
								totalDistancia+=a.weightHarvesineDistance();
								cantidadVertices++;

							}
							
						}
						view.printMessage("El total de vertices es: "+cantidadVertices);
						view.printMessage("Sus vertices son: ");
						Vertex[] vertices = tiempoMinimo.darVertices();
						for(Vertex v : vertices)
						{
							if(v!=null)
							{
								view.printMessage("ID: "+ v.darId());
								view.printMessage("Latitud: "+ tiempoMinimo.getInfoVertex(v));
								view.printMessage("Longitud: " +tiempoMinimo.getInfoVertex(v));

							}
						}
						view.printMessage("El tiempo estimado es: "+totalTiempo);
						view.printMessage("La distancia total del recorrido es: "+ totalDistancia);
						modelo.crearArchivoHTML("caminoMenorDistancia", tiempoMinimo, 1, 6, -80, -70, true);	
					}

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				lector6.close();
				
				break;	
				
			case 7: 
				System.out.println("--------- \n Determinar los n vértices con menor velocidad promedio en la ciudad de Bogotá \n---------"); 
				System.out.println("Escriba la cantidad de vertices que desea consultar:");
				int N=lector.nextInt();
				modelo.nVerticesMenorVelocidadPromedio(N);
				break;	
				
			case 8: 
				System.out.println("--------- \n Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Prim \n---------"); 
				modelo.calcularMSTPrim();
				
				break;	
				
			case 9: 
				System.out.println("--------- \n Encontrar el camino de menor costo (menor distancia Haversine) para un viaje entre dos localizaciones geográficas de la ciudad \n---------"); 
				System.out.println("Escriba la latitud de la zona de origen que desea consultar:");
				latitudO=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona de origen que desea consultar:");
				longitudO=lector.nextDouble();
				System.out.println("Escriba la latitud de la zona de destino que desea consultar:");
				latitudD=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona de destino que desea consultar:");
				longitudD=lector.nextDouble();
				modelo.caminoMenorCostoHaversine(latitudO,longitudO,latitudD,longitudD);
				
				break;	
				
			case 10: 
				System.out.println("--------- \n A partir de las coordenadas de una localización geográfica de la ciudad (lat, lon) de origen, indique cuáles vértices son alcanzables para un tiempo T  \n---------"); 
				System.out.println("Escriba la latitud de la zona que desea consultar:");
				latitud=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona que desea consultar:");
				longitud=lector.nextDouble();
				System.out.println("Escriba el tiempo que desea consultar:");
				Double tiempo=lector.nextDouble();
				modelo.indicarVerticesAlcanzablesTiempoT(latitud,longitud,tiempo);
				
				break;	
				
			case 11: 
				System.out.println("--------- \n Calcular un árbol de expansión mínima (MST) con criterio distancia, utilizando el algoritmo de Kruskal !! \n---------"); 
				modelo.calcularMSTKruskal();
				
				break;	
				
			case 12: 
				System.out.println("--------- \n Construir un nuevo grafo simplificado No dirigido de las zonas Uber \n---------"); 
				modelo.construirNuevoGrafo();
				
				break;	
				
			case 13: 
				System.out.println("--------- \n Calcular el camino de costo mínimo (algoritmo de Dijkstra) \n---------"); 
				modelo.calcularDijkstra();
				
				break;	
				
			case 14: 
				System.out.println("--------- \n Calcular los caminos de menor longitud (cantidad de arcos) a todas sus zonas alcanzables \n---------"); 
				System.out.println("Escriba la latitud de la zona de origen que desea consultar:");
				latitudO=lector.nextDouble();
				System.out.println("Escriba la longitud de la zona de origen que desea consultar:");
				longitudO=lector.nextDouble();
				modelo.caminoMenorLongituDesdeLaZona(latitudO,longitudO);
				
				break;	
				
			case 15: 
				System.out.println("--------- \n Hasta pronto !! \n---------"); 
				lector.close();
				fin = true;
				break;	

			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}

	}	
}
