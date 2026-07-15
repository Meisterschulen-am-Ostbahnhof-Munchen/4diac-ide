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

/**
 * Figure that renders a visible Coordinate Origin cross (+) at coordinate (0,0)
 * per IEC 61082-1 / Discussion #2655 requirements.
 */
public class CoordinateOriginFigure extends FreeformLayer {

	private static final int CROSS_SIZE = 20;
	private static final int CIRCLE_RADIUS = 6;

	public CoordinateOriginFigure() {
		setEnabled(false);
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
