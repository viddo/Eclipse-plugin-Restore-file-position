package tests.oss.restore.file.position.storage;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import oss.restore.file.position.storage.IOffsetsStorage;
import oss.restore.file.position.storage.StandardOffsetsStorage;

/**
 * Test for the StandardOffsetsStorage used as data collection of editor
 * positions.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class StandardOffsetsStorageTest extends TestCase {

	/**
	 * Tests constructing a standard storage with a already existing map.
	 */
	public void testConstructorWithMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("foobar", 1337);
		map.put("nyancat", 42);
		
		IOffsetsStorage storage = new StandardOffsetsStorage(3, map);
		assertEquals(1337, storage.getOffset("foobar"));
		assertEquals(42, storage.getOffset("nyancat"));
	}

	/**
	 * Test that non-existing file path URI returns 0 by default.
	 */
	public void testNonExisting() {
		IOffsetsStorage storage = new StandardOffsetsStorage(3);
		String filePathUri = "/foo/bar.java";
		assertEquals(0, storage.getOffset(filePathUri));
	}

	/**
	 * Test setting a offset.
	 */
	public void testSetOffset() {
		String filePathUri = "/foo/bar.java";
		IOffsetsStorage storage = new StandardOffsetsStorage(3);
		storage.setOffset(filePathUri, 1024);
		assertEquals(1024, storage.getOffset(filePathUri));
	}

	/**
	 * Test that the offset collection works as expected when the number of
	 * items is reaching the capacity limit.
	 */
	public void testCapacityLimit() {
		int capacity = 3;
		IOffsetsStorage storage = new StandardOffsetsStorage(capacity);

		// Create entries.
		for (int i = 1; i <= capacity; i++) {
			storage.setOffset(getFilePathUri(i), i);
		}

		// Assert expected entries existing.
		for (int i = 1; i <= capacity; i++) {
			assertEquals(i, storage.getOffset(getFilePathUri(i)));
		}

		// Add one extra and assure that the first ones are not present anymore.
		storage.setOffset("foo", 1337);
		assertEquals(3, storage.size());
		assertEquals(0, storage.getOffset(getFilePathUri(1))); // just deleted.
		assertEquals(2, storage.getOffset(getFilePathUri(2)));
		assertEquals(3, storage.getOffset(getFilePathUri(3)));
		assertEquals(1337, storage.getOffset("foo"));

		// Add another one and assure the size don't change.
		storage.setOffset("bar", 42);
		assertEquals(3, storage.size());
		assertEquals(0, storage.getOffset(getFilePathUri(1))); // deleted
																// previously.
		assertEquals(0, storage.getOffset(getFilePathUri(2))); // just deleted.
		assertEquals(3, storage.getOffset(getFilePathUri(3)));
		assertEquals(1337, storage.getOffset("foo"));
		assertEquals(42, storage.getOffset("bar"));
	}

	/**
	 * Test changing the capacity.
	 */
	public void testChangeCapacity() {
		int initialCapacity = 3;
		IOffsetsStorage storage = new StandardOffsetsStorage(initialCapacity);

		// Try to add more items than the capacity would allow, assert capacity
		// is not breached.
		for (int i = 1; i <= 25; i++) {
			storage.setOffset(getFilePathUri(i), i);
		}
		assertEquals(initialCapacity, storage.size());

		// Change capacity to more and assert again.
		int newCapacity = 10;
		storage.setCapacity(newCapacity);
		for (int i = 1; i <= 25; i++) {
			storage.setOffset(getFilePathUri(i), i);
		}
		assertEquals(newCapacity, storage.size());
	}

	/**
	 * Get a fake file path URI based on an offset.
	 * 
	 * @param offset
	 *            A number.
	 * @return The URI
	 */
	private String getFilePathUri(int offset) {
		return "/path/" + offset;
	}
}
