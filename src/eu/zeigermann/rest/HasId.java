package eu.zeigermann.rest;

import java.io.Serializable;

public interface HasId<K> extends Serializable {
	K getId();
	void setId(K id);
}
