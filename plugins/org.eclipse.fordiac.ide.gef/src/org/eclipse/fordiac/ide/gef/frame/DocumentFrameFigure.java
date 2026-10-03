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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;

/**
 * Draw2D Figure that renders an IEC 61082-1 Document Frame (border, grid
 * columns 1..N, rows A..H, and title block), positioned to hug the actual
 * network content rather than a fixed absolute location - the network is not
 * guaranteed to start at (0,0), so anchoring the frame there could leave it
 * far outside the area the user is actually looking at.
 */
public class DocumentFrameFigure extends FreeformLayer {

	private static final int MARGIN = 24;
	private static final int TITLE_BLOCK_WIDTH = 240;
	private static final int TITLE_BLOCK_HEIGHT = 60;
	private static final int FRAME_DOUBLE_LINE_GAP = 4;
	/** Gap between the actual network content and the frame's paper edge. */
	private static final int CONTENT_PADDING = 40;

	private DocumentFrame frame;

	public DocumentFrameFigure() {
		this(new DocumentFrame());
	}

	public DocumentFrameFigure(final DocumentFrame frame) {
		this.frame = frame;
		setEnabled(false);
	}

	public DocumentFrame getFrame() {
		return frame;
	}

	public void setFrame(final DocumentFrame frame) {
		this.frame = frame;
		fireExtentChanged();
		repaint();
	}

	/**
	 * A FreeformLayer with no children otherwise reports a (0,0,0,0) freeform
	 * extent, so a FreeformLayeredPane would stretch this layer only to the
	 * actual diagram content's bounds - clipping away the frame border/grid
	 * whenever the network doesn't already fill the whole page. Reporting the
	 * paper size here ensures it always gets bounds large enough to paint into.
	 */
	@Override
	public Rectangle getFreeformExtent() {
		if (frame == null) {
			return super.getFreeformExtent();
		}
		final Point origin = getFrameOrigin();
		return new Rectangle(origin.x, origin.y, frame.getPaperSize().getWidth(), frame.getPaperSize().getHeight());
	}

	/**
	 * Where the paper's top-left corner should sit: hugging the actual network
	 * content's bounding box (minus a fixed padding) rather than always literal
	 * (0,0), since the network is free to be positioned anywhere by the user.
	 * Falls back to (0,0) if there is no content yet (or no parent to look it up
	 * from, e.g. before this figure has been added to the layer tree).
	 */
	private Point getFrameOrigin() {
		final Rectangle contentBounds = getContentBounds();
		if (contentBounds == null) {
			return new Point(0, 0);
		}
		return new Point(contentBounds.x - CONTENT_PADDING,
				contentBounds.y - CONTENT_PADDING);
	}

	private Rectangle getContentBounds() {
		if (!(getParent() instanceof final LayeredPane pane)) {
			return null;
		}
		Rectangle union = null;
		for (final Object layerKey : new Object[] { LayerConstants.PRIMARY_LAYER, LayerConstants.CONNECTION_LAYER }) {
			final IFigure layer = pane.getLayer(layerKey);
			if (layer == null) {
				continue;
			}
			for (final Object childObj : layer.getChildren()) {
				final Rectangle childBounds = ((IFigure) childObj).getBounds();
				union = (union == null) ? childBounds.getCopy() : union.union(childBounds);
			}
		}
		return union;
	}

	@Override
	protected void paintFigure(final Graphics graphics) {
		super.paintFigure(graphics);
		if (frame == null) {
			return;
		}

		graphics.pushState();

		graphics.setLineWidth(1);
		graphics.setForegroundColor(ColorConstants.black);

		final Point origin = getFrameOrigin();
		final int width = frame.getPaperSize().getWidth();
		final int height = frame.getPaperSize().getHeight();

		// 1. Outer paper boundary rectangle
		graphics.setLineStyle(Graphics.LINE_DASH);
		graphics.drawRectangle(origin.x, origin.y, width, height);

		// 2. Inner Frame Border (Zeichnungsrahmen) around the print content: a red
		// double line, per IEC 61082-1 drawing frame convention.
		graphics.setLineStyle(Graphics.LINE_SOLID);
		graphics.setForegroundColor(ColorConstants.red);
		graphics.setLineWidth(2);
		final Rectangle inner = new Rectangle(origin.x + MARGIN, origin.y + MARGIN, width - (2 * MARGIN),
				height - (2 * MARGIN));
		graphics.drawRectangle(inner);
		graphics.setLineWidth(1);
		graphics.drawRectangle(inner.getCopy().shrink(FRAME_DOUBLE_LINE_GAP, FRAME_DOUBLE_LINE_GAP));
		graphics.setForegroundColor(ColorConstants.black);

		// 3. Grid Columns (1..N across top and bottom)
		final int cols = frame.getPaperSize().getColumns();
		final double colStep = (double) inner.width / cols;

		graphics.setLineWidth(1);
		for (int i = 0; i < cols; i++) {
			final int x = (int) (inner.x + i * colStep);
			// Top tick & label
			graphics.drawLine(x, inner.y - 6, x, inner.y);
			graphics.drawString(String.valueOf(i + 1), (int) (x + colStep / 2 - 3), inner.y - 18);

			// Bottom tick & label
			graphics.drawLine(x, inner.y + inner.height, x, inner.y + inner.height + 6);
			graphics.drawString(String.valueOf(i + 1), (int) (x + colStep / 2 - 3), inner.y + inner.height + 4);
		}
		// Closing tick for the trailing column boundary (cols cells have cols + 1
		// boundaries; the loop above only draws the leading edge of each cell).
		final int lastColX = inner.x + inner.width;
		graphics.drawLine(lastColX, inner.y - 6, lastColX, inner.y);
		graphics.drawLine(lastColX, inner.y + inner.height, lastColX, inner.y + inner.height + 6);

		// 4. Grid Rows (A..H down left and right)
		final int rows = frame.getPaperSize().getRows();
		final double rowStep = (double) inner.height / rows;

		for (int j = 0; j < rows; j++) {
			final int y = (int) (inner.y + j * rowStep);
			final char rowChar = (char) ('A' + j);

			// Left tick & label
			graphics.drawLine(inner.x - 6, y, inner.x, y);
			graphics.drawString(String.valueOf(rowChar), inner.x - 16, (int) (y + rowStep / 2 - 6));

			// Right tick & label
			graphics.drawLine(inner.x + inner.width, y, inner.x + inner.width + 6, y);
			graphics.drawString(String.valueOf(rowChar), inner.x + inner.width + 8, (int) (y + rowStep / 2 - 6));
		}
		// Closing tick for the trailing row boundary (rows cells have rows + 1
		// boundaries; the loop above only draws the leading edge of each cell).
		final int lastRowY = inner.y + inner.height;
		graphics.drawLine(inner.x - 6, lastRowY, inner.x, lastRowY);
		graphics.drawLine(inner.x + inner.width, lastRowY, inner.x + inner.width + 6, lastRowY);

		// 5. IEC 61082-1 Title Block (bottom-right corner)
		final int tbX = inner.x + inner.width - TITLE_BLOCK_WIDTH;
		final int tbY = inner.y + inner.height - TITLE_BLOCK_HEIGHT;

		graphics.setLineWidth(2);
		graphics.drawRectangle(tbX, tbY, TITLE_BLOCK_WIDTH, TITLE_BLOCK_HEIGHT);

		// Title Block Dividers
		graphics.setLineWidth(1);
		graphics.drawLine(tbX, tbY + 20, tbX + TITLE_BLOCK_WIDTH, tbY + 20);
		graphics.drawLine(tbX, tbY + 40, tbX + TITLE_BLOCK_WIDTH, tbY + 40);
		graphics.drawLine(tbX + 140, tbY + 20, tbX + 140, tbY + TITLE_BLOCK_HEIGHT);

		// Title Block Text Fields
		graphics.setFont(graphics.getFont());
		graphics.drawString(frame.getProjectTitle() + " - " + frame.getDocumentTitle(), tbX + 6, tbY + 4); //$NON-NLS-1$
		graphics.drawString("Author: " + frame.getAuthor(), tbX + 6, tbY + 24); //$NON-NLS-1$
		graphics.drawString("Date: " + frame.getDate(), tbX + 146, tbY + 24); //$NON-NLS-1$
		graphics.drawString("Company: " + frame.getCompanyName(), tbX + 6, tbY + 44); //$NON-NLS-1$

		graphics.popState();
	}
}
