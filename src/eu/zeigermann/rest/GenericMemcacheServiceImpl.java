package eu.zeigermann.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.appengine.api.memcache.MemcacheService;

import static com.google.appengine.api.memcache.MemcacheServiceFactory.*;
@SuppressWarnings("all")
public class GenericMemcacheServiceImpl<K, T extends HasId<K>> implements GenericDataService<K, T> {
	private final String name;
	private final MemcacheService memcacheService = getMemcacheService();

	public GenericMemcacheServiceImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public Collection<T> getAll() {
		return getMap().values();
	}

	@Override
	public T get(K id) {
		Map<Object, T> map = getMap();
		return map.get(id);
	}
	
	@Override
	public synchronized T save(T object) {
		Map<Object, T> map = getMap();
		map.put(object.getId(), object);
		putMap(map);
		return object;
	}
	
	@Override
	public synchronized void delete(K id) {
		Map<Object, T> map = getMap();
		map.remove(id);
		putMap(map);
	}
	
	private synchronized Map<Object, T> getMap() {
		Map<Object, T> map = (Map<Object, T>) memcacheService.get(name);
		if (map == null) {
			map = new HashMap<Object, T>();
			memcacheService.put(name, map);
		}
		return map;
	}

	private synchronized void putMap(Map<Object, T> map) {
			memcacheService.put(name, map);
	}
}
