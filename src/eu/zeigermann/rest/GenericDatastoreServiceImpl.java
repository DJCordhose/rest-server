package eu.zeigermann.rest;

import java.util.Collection;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("all")
public class GenericDatastoreServiceImpl implements GenericDataService<String, StringData<String>> {
	private static final String DATA_PROPERTY = "data";
	private final String name;
	private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public GenericDatastoreServiceImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public Collection<StringData<String>> getAll() {
		throw new RuntimeException();
	}

	@Override
	public StringData<String> get(String id) {
		Key key = KeyFactory.createKey(name, id);
		Entity entity;
		try {
			entity = datastore.get(key);
			if (entity == null) {
				return null;	
			}
			Text text = (Text) entity.getProperty(DATA_PROPERTY);
			return new StringData<String>(id, text.getValue());
		} catch (EntityNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public StringData save(StringData<String> object) {
		Key key = KeyFactory.createKey(name, object.getId());
		Entity entity = new Entity(key);
		entity.setProperty(DATA_PROPERTY, new Text(object.getData()));
		datastore.put(entity);
		return object;
	}
	
	@Override
	public void delete(String id) {
		Key key = KeyFactory.createKey(name, id);
		datastore.delete(key);
	}
	
}
