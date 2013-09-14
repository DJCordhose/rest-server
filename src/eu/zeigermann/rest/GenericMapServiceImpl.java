package eu.zeigermann.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class GenericMapServiceImpl<T extends HasId<Object>> implements GenericDataService<Object, T> {
	private final Map<Object, T> data = new ConcurrentHashMap<Object, T>();
	
	@Override
	public Collection<T> getAll() {
		return data.values();
	}
	
	@Override
	public T save(T object) {
		data.put(object.getId(), object);
		return object;
	}
	
	@Override
	public void delete(Object id) {
		data.remove(id);
	}

	@Override
	public T get(Object id) {
		return data.get(id);
	}

}
