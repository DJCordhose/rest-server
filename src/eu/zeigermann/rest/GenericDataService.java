package eu.zeigermann.rest;

import java.util.Collection;

public interface GenericDataService<K, T extends HasId<K>> {

	public T get(K id);
	
	public Collection<T> getAll();

	public T save(T mortgage);
	
	public void delete(K id);

}