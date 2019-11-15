package model.logic;

import model.logic.Viaje;

public class HTML <K ,V>
{

	private int capacidad;
	private int cantKeys;
	private Integer[] keys;
	private Vertex[] data;

	public HTML(int m)
	{
		capacidad = m;
		keys = new Integer[m];
		data = new Vertex[m];
	}

	public int hash(K key)
	{
		int hash = (key.hashCode()&0x7fffffff)%capacidad;
		return hash;
	}

	public void put(K Key,Informacion value)
	{
		if(verificarCapacidadCarga())
		{
			rehash(capacidad*2);
		}
		else
		{
			int hash =  hash(Key);
			int i;
			for(i = hash;keys[i] != null; i = (i+1)%capacidad)
			{
				if(keys[i].equals(Key))
				{				
					data[i] = new Vertex<>(value, hash);
					return;
				}
			}
			keys[i] = (Integer) Key;
			data[i] = new Vertex<>(value, hash);
			cantKeys++;


		}
	}


	public V get(K Key)
	{
		for(int i = hash(Key);keys[i] != null; i = (i+1)%capacidad )
		{
			if(keys[i].equals(Key))
			{
				return (V) data[i].getInfo();
			}
		}

		return null;
	}


	public V delete(K Key) throws Exception 
	{
		if(!contains(Key)) 
		{
			throw new Exception("No existe el elemento a eliminar");
		}
		int i = hash(Key);
		while (!Key.equals(keys[i]))
		{
			i = (i + 1) % capacidad;
		}
		keys[i] = null;
		data[i] = null;
		i = (i+1)% capacidad;
		while(keys[i] != null)
		{
			K keyChange = (K) keys[i];
			Informacion dataChange = data[i].getInfo();
			keys[i] = null;
			data[i] = null;
			cantKeys--;
			put(keyChange, dataChange);
			i = (i + 1) % capacidad;
		}
		cantKeys--;
		return null;
	}



	private boolean contains(K Key) {
		for(int i = hash(Key);keys[i] != null; i = (i+1)%capacidad )
		{
			if(keys[i].equals(Key))
			{
				return true;
			}
		}
		return false;
	}



	@SuppressWarnings("unchecked")
	public void rehash(int cap)
	{
		HTML<K, V> t;
		t = new HTML(cap);
		for (int i = 0; i < capacidad ; i++)
		{
			if(keys[i] != null)
			{
				K llave = (K) keys[i];
				Informacion valor = data[i].getInfo();
				t.put(llave, valor);
			}
		}
		keys = t.darKeys();
		data = t.darData();
		capacidad = t.darCapacidad(); 
	}

	public Integer[] darKeys()
	{
		return keys;
	}

	public Vertex[] darData()
	{
		return data;
	}

	public int darCapacidad()
	{
		return capacidad;
	}

	public boolean verificarCapacidadCarga()
	{
		double numKeysD = (double) cantKeys;
		double capacidadD = (double) capacidad;
		double factorCarga = numKeysD/capacidadD;
		if(factorCarga>0.75)
		{
			return true;
		}
		return false;
	}




}

