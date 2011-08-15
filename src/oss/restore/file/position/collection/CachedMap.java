/**
 * 
 */
package oss.restore.file.position.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exact same behavior as a LinkedHashMap except that this behaves like a cycle
 * list, i.e. upon adding a new entry if the capacity is reached it removes the
 * oldest entry.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class CachedMap<K, V> extends LinkedHashMap<K, V> {

	private int capacity;

	/**
	 * Serializable id
	 */
	private static final long serialVersionUID = -1581411220838573474L;

	/**
	 * Constructs an empty insertion-ordered LinkedHashMap instance with a
	 * default capacity (16) and load factor (0.75).
	 */
	public CachedMap() {
		super();
		this.capacity = 16;
	}

	/**
	 * Constructs an empty insertion-ordered LinkedHashMap instance with the
	 * specified capacity and a default load factor (0.75).
	 * 
	 * @param capacity
	 *            the capacity.
	 */
	public CachedMap(int capacity) {
		super(capacity);
		this.capacity = capacity;
	}

	/**
	 * Constructs an insertion-ordered LinkedHashMap instance with the same
	 * mappings as the specified map.
	 * 
	 * @param map
	 *            the map whose mappings are to be placed in this map.
	 */
	public CachedMap(Map<? extends K, ? extends V> map) {
		super(map);
		this.capacity = map.size();
	}

	/**
	 * Constructs an empty insertion-ordered LinkedHashMap instance with the
	 * specified initial capacity and load factor.
	 * 
	 * @param capacity
	 *            the capacity.
	 * @param loadFactor
	 *            the load factor.
	 */
	public CachedMap(int capacity, float loadFactor) {
		super(capacity, loadFactor);
		this.capacity = capacity;
	}

	/**
	 * Constructs an empty LinkedHashMap instance with the specified initial
	 * capacity, load factor and ordering mode.
	 * 
	 * @param capacity
	 *            the capacity.
	 * @param loadFactor
	 *            the load factor.
	 * @param accessOrder
	 *            the ordering mode - true for access-order, false for
	 *            insertion-order.
	 */
	public CachedMap(int capacity, float loadFactor, boolean accessOrder) {
		super(capacity, loadFactor, accessOrder);
		this.capacity = capacity;
	}

	/**
	 * Returns true if this map should remove its eldest entry, i.e. when the
	 * capacity is reached.
	 * 
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 * @param eldest
	 *            The least recently inserted entry in the map, or if this is an
	 *            access-ordered map, the least recently accessed entry. This is
	 *            the entry that will be removed it this method returns true. If
	 *            the map was empty prior to the put or putAll invocation
	 *            resulting in this invocation, this will be the entry that was
	 *            just inserted; in other words, if the map contains a single
	 *            entry, the eldest entry is also the newest.
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > capacity;
	}

	/**
	 * Get the capacity of the cached list.
	 * 
	 * @return The capacity.
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Change the capacity of this map.
	 * 
	 * @param newCapacity
	 *            The new capacity of this map.
	 */
	public void setCapacity(int newCapacity) {
		capacity = newCapacity;
	}

}
