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
package org.eclipse.fordiac.ide.gef.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Figure that renders a visible Coordinate Origin cross (+) at coordinate (0,0)
 * per IEC 61082-1 / Discussion #2655 requirements.
 */
public class CoordinateOriginFigure extends FreeformLayer {

	private static final int CROSS_SIZE = 20;
	private static final int CIRCLE_RADIUS = 6;
	private static final int LABEL_MARGIN = 20;

	public CoordinateOriginFigure() {
		setEnabled(false);
	}

	/**
	 * A FreeformLayer with no children otherwise reports a (0,0,0,0) freeform
	 * extent, so a FreeformLayeredPane would stretch this layer only to the
	 * actual diagram content's bounds - clipping away the cross whenever it
	 * doesn't already overlap the network's own bounding box.
	 */
	@Override
	public Rectangle getFreeformExtent() {
		return new Rectangle(-CROSS_SIZE, -CROSS_SIZE, (CROSS_SIZE + LABEL_MARGIN) * 2, (CROSS_SIZE + LABEL_MARGIN) * 2);
	}

	@Override
	protected void paintFigure(final Graphics graphics) {
		super.paintFigure(graphics);
		graphics.pushState();

		graphics.setLineWidth(2);
		graphics.setForegroundColor(ColorConstants.darkGray);

		final Point origin = new Point(0, 0);

		// Horizontal axis line (-CROSS_SIZE to +CROSS_SIZE)
		graphics.drawLine(origin.x - CROSS_SIZE, origin.y, origin.x + CROSS_SIZE, origin.y);

		// Vertical axis line (-CROSS_SIZE to +CROSS_SIZE)
		graphics.drawLine(origin.x, origin.y - CROSS_SIZE, origin.x, origin.y + CROSS_SIZE);

		// Circle at center
		graphics.drawOval(origin.x - CIRCLE_RADIUS, origin.y - CIRCLE_RADIUS, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

		// Axis Labels: X and Y
		graphics.setFont(graphics.getFont());
		graphics.drawString("0,0", origin.x + 8, origin.y + 8); //$NON-NLS-1$

		graphics.popState();
	}
}
