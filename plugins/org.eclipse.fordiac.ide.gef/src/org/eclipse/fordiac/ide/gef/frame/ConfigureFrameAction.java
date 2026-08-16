/*******************************************************************************
 * Copyright (c) 2026 HR Agrartechnik
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Ortmeier - initial API and implementation
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef.frame;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.fordiac.ide.gef.editparts.ZoomScalableFreeformRootEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Action that opens the Document Frame configuration dialog for the active FB
 * Network editor.
 */
public class ConfigureFrameAction extends Action {

	public ConfigureFrameAction() {
		setId("org.eclipse.fordiac.ide.gef.frame.configureFrame"); //$NON-NLS-1$
		setText("Configure Frame..."); //$NON-NLS-1$
		setToolTipText("Configure document frame (paper size, title block)"); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled() {
		return getDocumentFrameFigure() != null;
	}

	@Override
	public void run() {
		final DocumentFrameFigure frameFigure = getDocumentFrameFigure();
		if (frameFigure == null) {
			return;
		}

		final DocumentFrame frame = frameFigure.getFrame();
		if (frame == null) {
			return;
		}

		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final DocumentFrameDialog dialog = new DocumentFrameDialog(window.getShell(), frame);
		if (dialog.open() == org.eclipse.jface.dialogs.Dialog.OK) {
			// Repaint both the printable and scaled layers
			final GraphicalViewer viewer = getActiveViewer();
			if (viewer != null) {
				final LayerManager lm = (LayerManager) viewer.getEditPartForModel(LayerManager.ID);
				if (lm != null) {
					// Reprint the printable layers frame
					final LayeredPane printableLayers = (LayeredPane) lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
					if (printableLayers != null) {
						final IFigure frameLayer = printableLayers.getLayer(ZoomScalableFreeformRootEditPart.FRAME_LAYER);
						if (frameLayer != null) {
							frameLayer.repaint();
						}
					}
					// Repaint the scaled layers frame
					final LayeredPane scaledLayers = (LayeredPane) lm.getLayer(LayerConstants.SCALABLE_LAYERS);
					if (scaledLayers != null) {
						final IFigure frameLayer = scaledLayers.getLayer(ZoomScalableFreeformRootEditPart.FRAME_LAYER);
						if (frameLayer != null) {
							frameLayer.repaint();
						}
					}
				}
			}
		}
	}

	private static GraphicalViewer getActiveViewer() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null || window.getActivePage() == null) {
			return null;
		}
		final IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor != null) {
			return editor.getAdapter(GraphicalViewer.class);
		}
		return null;
	}

	private DocumentFrameFigure getDocumentFrameFigure() {
		final GraphicalViewer viewer = getActiveViewer();
		if (viewer == null) {
			return null;
		}
		final LayerManager lm = (LayerManager) viewer.getEditPartForModel(LayerManager.ID);
		if (lm == null) {
			return null;
		}
		final LayeredPane printableLayers = (LayeredPane) lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
		if (printableLayers == null) {
			return null;
		}
		final IFigure frameLayer = printableLayers.getLayer(ZoomScalableFreeformRootEditPart.FRAME_LAYER);
		if (frameLayer instanceof final DocumentFrameFigure frameFigure) {
			return frameFigure;
		}
		return null;
	}
}
