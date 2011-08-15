package oss.restore.file.position;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.ITextEditor;

import oss.restore.file.position.storage.IOffsetsStorage;

/**
 * Responsible for listening to changes of the line position in an editor part
 * window.
 * 
 * Stores the line position when a window is closed and jumps to the same
 * position again if the same document is opened later on.
 * 
 * @author Nicklas Gummesson <nicklas@tuenti.com>
 */
public class LinePositionPartListener implements IPartListener2 {

	/**
	 * @param storage
	 *            Offsets storage to use for listener.
	 */
	public LinePositionPartListener(IOffsetsStorage storage) {
		this.storage = storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partActivated(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partBroughtToTop(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partClosed(IWorkbenchPartReference partRef) {
		ISelectionProvider selectionProvider = getSelectionProvided(partRef);
		if (selectionProvider != null) {
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof TextSelection) {
				String filePathUri = getFilePathURI(partRef);
				int offset = ((TextSelection) selection).getOffset();
				storage.setOffset(filePathUri, offset);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partDeactivated(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partHidden(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partInputChanged(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void partOpened(IWorkbenchPartReference partRef) {
		ISelectionProvider selectionProvider = getSelectionProvided(partRef);
		if (selectionProvider != null) {
			String filePathUri = getFilePathURI(partRef);
			if (filePathUri != null) {
				// Resores the offset if available.
				int offset = storage.getOffset(filePathUri);
				selectionProvider.setSelection(new TextSelection(offset, 0));
			}
		}
	}

	public void partVisible(IWorkbenchPartReference iworkbenchpartreference) {
	}

	/**
	 * Get current text marker selection, if any is available.
	 * 
	 * @param partRef
	 *            The current path reference.
	 * @return selection or null
	 */
	private ISelectionProvider getSelectionProvided(
			IWorkbenchPartReference partRef) {
		ISelectionProvider selectionProvider = null;

		ITextEditor editor = getTextEditor(partRef);
		if (editor instanceof ITextEditor) {
			selectionProvider = editor.getSelectionProvider();
		}

		return selectionProvider;
	}

	/**
	 * Get the file path URI of the current editor.
	 * 
	 * @param partRef
	 *            The current path reference.
	 * @return File path URI or null if it could not be retrieved.
	 */
	private String getFilePathURI(IWorkbenchPartReference partRef) {
		String filePathURI = null;

		ITextEditor editor = getTextEditor(partRef);
		if (editor instanceof ITextEditor) {
			org.eclipse.ui.IEditorInput input = editor.getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				filePathURI = file.getLocation().toOSString();
			}
		}

		return filePathURI;
	}

	/**
	 * Get the active editor if it is a text editor.
	 * 
	 * @param partRef
	 *            The current path reference.
	 * @return Text editor or null if the editor is not handling text.
	 */
	private ITextEditor getTextEditor(IWorkbenchPartReference partRef) {
		ITextEditor editor = null;

		// Get part if available, do not try to restore it if not available.
		IWorkbenchPart part = partRef.getPart(false);
		if (part instanceof ITextEditor) {
			editor = (ITextEditor) part;
		}

		return editor;
	}

	private IOffsetsStorage storage;
}
