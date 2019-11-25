package test.data_structures;

import static org.junit.Assert.*;

import java.util.Iterator;

import model.data_structures.Grafo;
import model.data_structures.Graph;
import model.logic.Informacion;

import org.junit.Before;
import org.junit.Test;

public class TestGraph {

	private Grafo<Integer, Integer> grafo;
	private static int CAPACIDAD=6;
	private int llave1 = 1;
	private int llave2 = 2;
	private int llave3 = 3;
	private int llave4 = 4;
	private int llave5 = 5;
	private int llave6 = 6;


	@Before
	public void setUp1() {
		grafo = new Grafo<Integer, Integer>(CAPACIDAD);
		grafo.addVertex(llave1, 1);
		grafo.addVertex(llave2, 2);
		grafo.addVertex(llave3, 3);
		grafo.addVertex(llave4, 4);
		grafo.addVertex(llave5, 5);
	}

	@Test
	public void TestV()
	{
		setUp1();
		try {
			assertEquals("No se ha dado el valor adecuado", 5, grafo.V());
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método V");
		}
	}

	@Test
	public void TestE()
	{
		setUp1();
		grafo.setArc(llave1, llave2);
		grafo.setArc(llave3, llave4);
		try {
			assertEquals("No se ha dado el valor adecuado", 2, grafo.E());
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método E");
		}
	}

	@Test
	public void TestAddEdge()
	{
		setUp1();
		grafo.addEdge(llave1, llave2, 3);
		int val = (int) grafo.getCostArc(1, 2);
		try {
			assertEquals("No es el valor esperado", 3, val);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método Add Edge");
		}
	}

	@Test
	public void TestGetInfoVertex()
	{
		setUp1();
		Integer inter= grafo.getInfoVertex(llave1);

		try {
			assertEquals("No es el valor esperado: lat",inter== 1, inter);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método Get Info Vertex");
		}
	}

	@Test
	public void TestGetCostArc()
	{
		setUp1();
		grafo.addEdge(1, 2, 3);
		int costo = (int) ( grafo).getCostArc(llave1, llave2);
		try {
			assertEquals("No es el valor esperado: costo", 3, costo);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método Get Cost Arc");
		}
	}

	@Test
	public void TestSetArc()
	{
		setUp1();
		grafo.setArc(1, 2);
		Integer inter = grafo.getInfoVertex(llave1);
		int val1 = inter;
		try {
			assertEquals("No es el valor esperado", 2, val1);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método Set Arc");
		}
	}

	@Test
	public void TestAddVertex()
	{
		setUp1();
		grafo.addVertex(llave6, 7);
		Integer inter = grafo.getInfoVertex(llave6);
		
		try {
			assertEquals("No es el valor esperado: lat", inter==7, true);
		}
		catch (Exception e) {
			fail("Se ha generado un error en el método Add Vertex");
		}
	}

	@Test
	public void TestAdj()
	{
		setUp1();
		grafo.setArc(llave1, llave2);
		grafo.setArc(llave3, llave4);
		Integer inter1 = grafo.getInfoVertex(llave2);
		Integer inter2 = grafo.getInfoVertex(llave4);
		try
		{
			assertEquals("No es el valor esperado", llave1== inter1,true);
			assertEquals("No es el valor esperado", llave3== inter2,true);
		}
		catch (Exception e) {
			fail("Error al usar el método Adj");
		}
	}

	@Test
	public void TestDarVertices()
	{
		setUp1();
		assertEquals("No es el valor esperado: moveID",grafo.V()==6,true);
	}

	@Test
	public void TestCC()
	{
		setUp1();
		assertEquals("No es el valor esperado: moveID",grafo.cc()==6,true);
	}
	}