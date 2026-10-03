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

import java.time.LocalDate;
import java.util.Objects;

/**
 * Data model for IEC 61082-1 Document Frames.
 */
public class DocumentFrame {

	/**
	 * Curated set of DIN/Letter paper sizes. Dimensions are in pixels at 96 DPI;
	 * column/row zone counts are chosen so a zone is roughly the same physical
	 * size (~45-50mm) across formats, per IEC 61082-1 practice.
	 */
	public enum PaperSize {
		A5_PORTRAIT(559, 793, 3, 5, "A5 Portrait"), //$NON-NLS-1$
		A5_LANDSCAPE(793, 559, 5, 3, "A5 Landscape"), //$NON-NLS-1$
		A4_PORTRAIT(793, 1122, 5, 6, "A4 Portrait"), //$NON-NLS-1$
		A4_LANDSCAPE(1122, 793, 6, 5, "A4 Landscape"), //$NON-NLS-1$
		A3_PORTRAIT(1122, 1587, 6, 9, "A3 Portrait"), //$NON-NLS-1$
		A3_LANDSCAPE(1587, 1122, 9, 6, "A3 Landscape"), //$NON-NLS-1$
		LETTER_PORTRAIT(816, 1056, 5, 6, "Letter Portrait"), //$NON-NLS-1$
		LETTER_LANDSCAPE(1056, 816, 6, 5, "Letter Landscape"); //$NON-NLS-1$

		private final int width;
		private final int height;
		private final int columns;
		private final int rows;
		private final String label;

		PaperSize(final int width, final int height, final int columns, final int rows, final String label) {
			this.width = width;
			this.height = height;
			this.columns = columns;
			this.rows = rows;
			this.label = label;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getColumns() {
			return columns;
		}

		public int getRows() {
			return rows;
		}

		public String getLabel() {
			return label;
		}
	}

	private PaperSize paperSize = PaperSize.A4_LANDSCAPE;
	private String projectTitle = "4diac Project"; //$NON-NLS-1$
	private String documentTitle = "FB Network Diagram"; //$NON-NLS-1$
	private String companyName = "Eclipse 4diac"; //$NON-NLS-1$
	private String author = "4diac User"; //$NON-NLS-1$
	private String date = LocalDate.now().toString();

	public DocumentFrame() {
	}

	public DocumentFrame(final PaperSize paperSize) {
		this.paperSize = paperSize;
	}

	public PaperSize getPaperSize() {
		return paperSize;
	}

	public void setPaperSize(final PaperSize paperSize) {
		this.paperSize = Objects.requireNonNull(paperSize, "paperSize must not be null"); //$NON-NLS-1$
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(final String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(final String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}
}
