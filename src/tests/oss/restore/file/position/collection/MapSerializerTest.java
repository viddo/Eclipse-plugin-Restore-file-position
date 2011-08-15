package tests.oss.restore.file.position.collection;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import oss.restore.file.position.collection.MapSerializer;

/**
 * Test the map serializer.
 * 
 * @author Nicklas Gummesson
 */
public class MapSerializerTest extends TestCase {

	/**
	 * Test that serializing a empty map is not breaking.
	 */
	public void testSerializeEmpyMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String emptyString = MapSerializer.serializeMap(map);
		assertEquals("", emptyString);
	}

	/**
	 * Test serializing a simple hashmap with only one item.
	 */
	public void testSerializeSimpleHashMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("foobar", 1337);
		String querystring = MapSerializer.serializeMap(map);
		assertEquals("foobar=1337", querystring);
	}

	/**
	 * Test serializing a typical hashmap with some items.
	 */
	public void testSerializeTypicalHashMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("foobar", 1337);
		map.put("nyancat", 42);
		map.put("l0lc4tz", 123);
		String querystring = MapSerializer.serializeMap(map);
		assertEquals("foobar=1337&nyancat=42&l0lc4tz=123", querystring);
	}

	/**
	 * Test de-serializing a typical hashmap with some items.
	 */
	public void testDeserializeTypicalHashMap() {
		String querystring = "foobar=1337&nyancat=42&l0lc4tz=123";
		Map<String, Integer> map = MapSerializer.deserializeMap(querystring);

		assertEquals(new Integer(1337), map.get("foobar"));
		assertEquals(new Integer(42), map.get("nyancat"));
		assertEquals(new Integer(123), map.get("l0lc4tz"));
	}

	/**
	 * Test de-serializing malformed input is not breaking.
	 */
	public void testDeserializeMalformedInput() {
		// Completely malformed.
		Map<String, Integer> map = MapSerializer.deserializeMap("malformed!");
		assertEquals(0, map.size());

		// Correct format but value is not an Integer.
		map = MapSerializer.deserializeMap("foo=bar");
		assertEquals(0, map.size());
		map = MapSerializer.deserializeMap("foo=bar&abc=def");
		assertEquals(0, map.size());
	}

	/**
	 * Test de-serializing party malformed input.
	 */
	public void testDeserializePartlyMalformedInput() {
		// Partly valid input, should only get valid data.
		Map<String, Integer> map = MapSerializer
				.deserializeMap("foo=bar&abc=123");
		assertEquals(1, map.size());
		assertEquals(new Integer(123), map.get("abc"));
		assertEquals(null, map.get("foo"));
	}
}
