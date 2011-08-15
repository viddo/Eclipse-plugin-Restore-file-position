package oss.restore.file.position.storage;

import java.util.Map;

/**
 * A storage that holds a set of offsets for file paths. To not lead to out of
 * memory issues the implementing class should make sure that underlying data
 * structure is bounded with a maximum capacity of items it can hold.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public interface IOffsetsStorage {

	/**
	 * Change the capacity of this storage. Setting a newCapacity that is
	 * smaller than the previous capacity does not have to cut the storage
	 * directly but rather use lazy-delete to adjust the size of the list as new
	 * offsets gets added.
	 * 
	 * @param newCapacity
	 *            The new capacity of the storage.
	 */
	public abstract void setCapacity(int newCapacity);

	/**
	 * Get the current capacity of this storage.
	 * 
	 * @return The capacity.
	 */
	public abstract int getCapacity();

	/**
	 * Get offset from a file path. If the offset is not available this should
	 * return 0.
	 * 
	 * @param filePathUri
	 *            The file path URI for which to get the offset.
	 * @return int The offset
	 */
	public abstract int getOffset(String filePathUri);

	/**
	 * Set the offset for a certain file path. If the capacity of this storage
	 * is reached this should silently add the new offset and remove the eldest
	 * one.
	 * 
	 * @param filePathUri
	 *            The file path URI for which to save the offset.
	 * @param int offset The offset.
	 * @return void
	 */
	public abstract void setOffset(String filePathUri, int offset);

	/**
	 * Get the count of offsets in the current storage.
	 * 
	 * @return The count.
	 */
	public abstract int size();

	/**
	 * Get a map representation of this offsets storage.
	 * 
	 * @return A map where the key are the file path URIs and the values the
	 *         corresponding offsets.
	 */
	public abstract Map<String, Integer> getMap();
}
