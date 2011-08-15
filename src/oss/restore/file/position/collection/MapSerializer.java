package oss.restore.file.position.collection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Map serializer which expects to get a map containing String keys and values
 * as Integers.
 * 
 * @author Nicklas Gummesson
 */
public class MapSerializer {

	/**
	 * Serialize a map to a string on querystring format, e.g.
	 * key=value&key2=otherValue&keyN=valueN.
	 * 
	 * @param map
	 *            The map to serialize.
	 * @return Serialized string.
	 */
	public static String serializeMap(Map<String, Integer> map) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String key : map.keySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}
			String value = map.get(key).toString();
			if (key != null && value != null) {
				try {
					stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
					stringBuilder.append("=");
					stringBuilder.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("UTF-8 support is required", e);
				}
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * De-serialize a string on the format of a querystring. A key-value pair
	 * that do not follow the expected output format of <String, Integer> it is
	 * simply ignored.
	 * 
	 * @param querystring
	 *            E.g. key=value&key2=otherValue&keyN=valueN.
	 * @return A map representation of the querystring.
	 */
	public static Map<String, Integer> deserializeMap(String querystring) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		String[] items = querystring.split("&");
		for (String item : items) {
			String[] keyValue = item.split("=");
			if (keyValue.length == 2) {
				String key = keyValue[0];
				Integer value = null;
				try {
					value = Integer.parseInt(keyValue[1]);
				} catch (NumberFormatException e) {
				}

				// If value is null it was because the value was malformed,
				// simply skip.
				if (value != null) {
					try {
						map.put(URLDecoder.decode(key, "UTF-8"), value);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException("UTF-8 support is required",
								e);
					}
				}
			}
		}

		return map;
	}
}
