package oss.restore.file.position;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import oss.restore.file.position.collection.MapSerializer;
import oss.restore.file.position.storage.IOffsetsStorage;
import oss.restore.file.position.storage.StandardOffsetsStorage;

/**
 * The plug-in entry point.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class Activator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "oss.restore.file.position"; //$NON-NLS-1$

	// The shared singleton instance.
	private static Activator plugin;

	private PartListenersHandler partListenerHandler;

	private IOffsetsStorage storage;

	private static int DEFAULT_STORAGE_CAPACITY = 100;
	private static String STORE_KEY_SERIALIZED_OFFSETS_STORAGE = "serializedOffsetsStorage";

	/**
	 * Constructs the Activator.
	 */
	public Activator() {
	}

	@Override
	public void earlyStartup() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		if (partListenerHandler == null) {
			// Deserialize a offsets storage map, if available.
			IPreferenceStore preferenceStore = getPreferenceStore();
			String serializedOffsetsStorageMap = preferenceStore
					.getString(STORE_KEY_SERIALIZED_OFFSETS_STORAGE);
			if (serializedOffsetsStorageMap.isEmpty()) {
				storage = new StandardOffsetsStorage(DEFAULT_STORAGE_CAPACITY);
			} else {
				Map<String, Integer> offsetsStorageMap = MapSerializer
						.deserializeMap(serializedOffsetsStorageMap);
				storage = new StandardOffsetsStorage(DEFAULT_STORAGE_CAPACITY,
						offsetsStorageMap);
			}

			partListenerHandler = new PartListenersHandler(storage);
			partListenerHandler.addListeners();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// Serialize and store the offsets storage for next time Eclipse is
		// started. Has to be done BEFORE super() is called for this to actually
		// be stored.
		Map<String, Integer> offsetsMap = storage.getMap();
		String serializedOffsetsStorageMap = MapSerializer
				.serializeMap(offsetsMap);
		IPreferenceStore preferenceStore = getPreferenceStore();
		preferenceStore.setValue(STORE_KEY_SERIALIZED_OFFSETS_STORAGE,
				serializedOffsetsStorageMap);

		super.stop(context);
		partListenerHandler.removeListeners();
		partListenerHandler = null;

		storage = null;
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
