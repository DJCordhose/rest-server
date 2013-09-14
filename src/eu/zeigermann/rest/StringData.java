package eu.zeigermann.rest;

@SuppressWarnings("serial")
public class StringData<K> implements HasId<K> {

	private K id;
	private String data;

	public StringData(K id, String data) {
		super();
		this.id = id;
		this.data = data;
	}

	public K getId() {
		return id;
	}

	public void setId(K id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
