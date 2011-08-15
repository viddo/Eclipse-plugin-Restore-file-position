package oss.restore.file.position;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import oss.restore.file.position.storage.IOffsetsStorage;

/**
 * Responsible for managing part listeners.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class PartListenersHandler {
	
	private IOffsetsStorage storage;

	/**
	 * Constructors this manager.
	 */
	public PartListenersHandler(IOffsetsStorage storage) {
		this.storage = storage;
	}

	/**
	 * Add listeners.
	 */
	public void addListeners() {
		IWorkbenchPage page = getPage();
		if (page != null) {
			partListener = new LinePositionPartListener(storage);
			page.addPartListener(partListener);
		}
	}

	/**
	 * Remove listeners.
	 */
	public void removeListeners() {
		IWorkbenchPage page = getPage();
		if (page != null) {
			page.removePartListener(partListener);
			partListener = null;
		}
	}

	/**
	 * Get the current workbench page of the IDE
	 * 
	 * @return A IWorkbenchPage instance or null if none available.
	 */
	private IWorkbenchPage getPage() {
		IWorkbenchPage page = null;

		// Get active page if available.
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			page = window.getActivePage();
		}

		// No active page? Iterate over the workbench's other windows until we
		// found an open page.
		if (page == null) {
			IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
					.getWorkbenchWindows();
			IWorkbenchWindow aiworkbenchwindow[];
			int j = (aiworkbenchwindow = windows).length;
			for (int i = 0; i < j; i++) {
				IWorkbenchWindow win = aiworkbenchwindow[i];
				page = win.getActivePage();
				if (page != null)
					break;
			}

		}

		return page;
	}

	private IPartListener2 partListener;
}
