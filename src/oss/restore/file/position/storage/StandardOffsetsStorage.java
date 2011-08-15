package oss.restore.file.position.storage;

import java.util.Map;

import oss.restore.file.position.collection.CachedMap;

/**
 * This stores a set of offsets for file paths.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class StandardOffsetsStorage implements IOffsetsStorage {

	CachedMap<String, Integer> map;

	/**
	 * Constructs a temporary offsets storage.
	 * 
	 * @param capacity
	 *            The capacity of this storage, i.e. how many offsets to store.
	 */
	public StandardOffsetsStorage(int capacity) {
		map = new CachedMap<String, Integer>(capacity);
	}

	/**
	 * Constructs a temporary offsets storage.
	 * 
	 * @param capacity
	 *            The capacity of this storage, i.e. how many offsets to store.
	 * @param initialMap
	 *            Get the initial mapping to use. E.g. restored from a permanent
	 *            storage or similar.
	 */
	public StandardOffsetsStorage(int capacity, Map<String, Integer> initialmap) {
		map = new CachedMap<String, Integer>(initialmap);
		setCapacity(capacity);
	}

	@Override
	public int getCapacity() {
		return map.getCapacity();
	}

	@Override
	public void setCapacity(int newCapacity) {
		map.setCapacity(newCapacity);
	}

	@Override
	public int getOffset(String filePathUri) {
		Integer item = (Integer) map.get(filePathUri);
		return item == null ? 0 : item.intValue();
	}

	@Override
	public void setOffset(String filePathUri, int offset) {
		map.put(filePathUri, Integer.valueOf(offset));
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Map<String, Integer> getMap() {
		return map;
	}
}