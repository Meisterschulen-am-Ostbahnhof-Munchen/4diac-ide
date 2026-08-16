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

import org.eclipse.fordiac.ide.gef.frame.DocumentFrame.PaperSize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for configuring IEC 61082-1 Document Frame settings (paper size and
 * title block fields).
 */
public class DocumentFrameDialog extends Dialog {

	private final DocumentFrame frame;

	private Combo paperSizeCombo;
	private Text projectTitleText;
	private Text documentTitleText;
	private Text companyNameText;
	private Text authorText;
	private Text dateText;

	public DocumentFrameDialog(final Shell parentShell, final DocumentFrame frame) {
		super(parentShell);
		this.frame = frame;
		setShellStyle(SWT.RESIZE | SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Configure Document Frame"); //$NON-NLS-1$
		newShell.setSize(450, 350);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createPaperSizeSection(area);
		createTitleBlockSection(area);

		return area;
	}

	private void createPaperSizeSection(final Composite parent) {
		final Group group = new Group(parent, SWT.NONE);
		group.setText("Paper Size"); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		new Label(group, SWT.NONE).setText("Size:"); //$NON-NLS-1$

		paperSizeCombo = new Combo(group, SWT.READ_ONLY);
		for (final PaperSize ps : PaperSize.values()) {
			paperSizeCombo.add(ps.getLabel());
		}
		// Select current paper size
		final PaperSize currentSize = frame.getPaperSize();
		final PaperSize[] sizes = PaperSize.values();
		for (int i = 0; i < sizes.length; i++) {
			if (sizes[i] == currentSize) {
				paperSizeCombo.select(i);
				break;
			}
		}
		paperSizeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	private void createTitleBlockSection(final Composite parent) {
		final Group group = new Group(parent, SWT.NONE);
		group.setText("Title Block"); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		new Label(group, SWT.NONE).setText("Project Title:"); //$NON-NLS-1$
		projectTitleText = new Text(group, SWT.BORDER);
		projectTitleText.setText(frame.getProjectTitle());
		projectTitleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(group, SWT.NONE).setText("Document Title:"); //$NON-NLS-1$
		documentTitleText = new Text(group, SWT.BORDER);
		documentTitleText.setText(frame.getDocumentTitle());
		documentTitleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(group, SWT.NONE).setText("Company:"); //$NON-NLS-1$
		companyNameText = new Text(group, SWT.BORDER);
		companyNameText.setText(frame.getCompanyName());
		companyNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(group, SWT.NONE).setText("Author:"); //$NON-NLS-1$
		authorText = new Text(group, SWT.BORDER);
		authorText.setText(frame.getAuthor());
		authorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(group, SWT.NONE).setText("Date:"); //$NON-NLS-1$
		dateText = new Text(group, SWT.BORDER);
		dateText.setText(frame.getDate());
		dateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	@Override
	protected void okPressed() {
		// Apply paper size
		final int selectedIndex = paperSizeCombo.getSelectionIndex();
		if (selectedIndex >= 0) {
			frame.setPaperSize(PaperSize.values()[selectedIndex]);
		}

		// Apply title block fields
		frame.setProjectTitle(projectTitleText.getText().trim());
		frame.setDocumentTitle(documentTitleText.getText().trim());
		frame.setCompanyName(companyNameText.getText().trim());
		frame.setAuthor(authorText.getText().trim());
		frame.setDate(dateText.getText().trim());

		super.okPressed();
	}
}
