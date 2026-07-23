/*******************************************************************************
 * Copyright (c) 2026 fortiss GmbH, Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Franz Höpfinger - initial implementation
 *******************************************************************************/
package org.eclipse.fordiac.ide.deployment.bootfile;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.Device;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.libraryElement.Resource;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateBootfileDirectHandler extends AbstractHandler {

	private static final String DEFAULT_TARGET_DIR = "C:\\git\\ms\\4diac_training1\\Ventilsteuerung\\boot-files";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!(selection instanceof final IStructuredSelection structuredSelection) || structuredSelection.isEmpty()) {
			return null;
		}

		final Shell shell = HandlerUtil.getActiveShell(event);
		final Map<Device, List<Object>> workLoad = new HashMap<>();
		String targetDir = DEFAULT_TARGET_DIR;

		for (final Object object : structuredSelection.toList()) {
			Object target = object;
			if (target instanceof final IFile file) {
				final org.eclipse.fordiac.ide.model.typelibrary.TypeEntry typeEntry = 
					org.eclipse.fordiac.ide.model.typelibrary.TypeLibraryManager.INSTANCE.getTypeEntryForFile(file);
				if (typeEntry instanceof final org.eclipse.fordiac.ide.model.typelibrary.SystemEntry systemEntry) {
					target = systemEntry.getType();
				}
			} else if (target instanceof final EObject eObj) {
				target = findDeployableElement(eObj);
			}

			if (target instanceof final Resource resource) {
				insertResource(workLoad, resource);
			} else if (target instanceof final Device device) {
				getWorkLoadEntryList(workLoad, device).add(device);
			} else if (target instanceof final AutomationSystem sys) {
				for (final Device device : sys.getDevices()) {
					getWorkLoadEntryList(workLoad, device).add(device);
					for (final Resource resource : device.getResources()) {
						insertResource(workLoad, resource);
					}
				}
			}

			// Try to find target directory dynamically based on selected resource's project
			IFile associatedFile = null;
			if (object instanceof final IFile file) {
				associatedFile = file;
			} else if (object instanceof final EObject eObj) {
				final EObject root = EcoreUtil.getRootContainer(eObj);
				if (root instanceof final LibraryElement libEl) {
					associatedFile = libEl.getTypeEntry().getFile();
				}
			}
			if (associatedFile != null && associatedFile.getProject() != null && associatedFile.getProject().getLocation() != null) {
				final File projectDir = associatedFile.getProject().getLocation().toFile();
				final File parent = projectDir.getParentFile();
				if (parent != null) {
					final File grandParent = parent.getParentFile();
					if (grandParent != null) {
						final File bootFilesDir = new File(grandParent, "boot-files");
						if (bootFilesDir.exists() && bootFilesDir.isDirectory()) {
							targetDir = bootFilesDir.getAbsolutePath();
						}
					}
				}
			}
		}

		if (workLoad.isEmpty()) {
			return null;
		}

		final File dir = new File(targetDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			final StringBuilder filesMsg = new StringBuilder();
			for (final Entry<Device, List<Object>> entry : workLoad.entrySet()) {
				final String fileName = MessageFormat.format(Messages.CreateBootfilesWizard_IProgressMonitorMonitor,
						targetDir, Character.valueOf(File.separatorChar),
						entry.getKey().getAutomationSystem().getName(), entry.getKey().getName());

				BootFileDeviceManagementCommunicationHandler.createBootFile(entry.getValue(), fileName, shell);
				if (filesMsg.length() > 0) {
					filesMsg.append(", ");
				}
				filesMsg.append(new File(fileName).getName());
			}

			// Report success to the Status Line
			final IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
			IStatusLineManager statusLine = null;
			if (activePart != null && activePart.getSite() != null) {
				IActionBars actionBars = null;
				if (activePart instanceof IViewPart) {
					actionBars = ((IViewPart) activePart).getViewSite().getActionBars();
				} else if (activePart instanceof IEditorPart) {
					actionBars = ((IEditorPart) activePart).getEditorSite().getActionBars();
				}
				if (actionBars != null) {
					statusLine = actionBars.getStatusLineManager();
				}
			}
			if (statusLine != null) {
				statusLine.setMessage("FORTE bootfile(s) generated successfully: " + filesMsg.toString());
			}
		} catch (final Exception e) {
			MessageDialog.openError(shell, "Create Bootfile Error", "Failed to generate bootfiles: " + e.getMessage());
		}

		return null;
	}

	private static EObject findDeployableElement(final EObject eObj) {
		EObject current = eObj;
		while (current != null) {
			if (current instanceof Resource || current instanceof Device || current instanceof AutomationSystem) {
				return current;
			}
			current = current.eContainer();
		}
		return null;
	}

	private static void insertResource(final Map<Device, List<Object>> workLoad, final Resource res) {
		final List<Object> resList = getWorkLoadEntryList(workLoad, res.getDevice());
		resList.add(res);
	}

	private static List<Object> getWorkLoadEntryList(final Map<Device, List<Object>> workLoad, final Device device) {
		return workLoad.computeIfAbsent(device, dev -> new ArrayList<>());
	}

}
