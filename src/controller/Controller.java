package controller;

import java.io.IOException;
import java.util.Scanner;

import model.logic.MVCModelo;
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
					modelo.cargarTxtHash();
					modelo = new MVCModelo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}					
				break;

			case 2:
				System.out.println("--------- \nEsquema JSON para persistir el grafo: ");
				dato = lector.next();
				modelo.crearJson();

				break;

			case 3:
				System.out.println("--------- \nCantidad de componentes conectados en el grafo: ");
				modelo.cantidadComponentesConectados();					
				break;

			case 4:
				System.out.println("--------- \nGrafica con ayuda de Google Maps: ");
				modelo.graficaGoogleMaps();			
				break;

			case 5: 
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
