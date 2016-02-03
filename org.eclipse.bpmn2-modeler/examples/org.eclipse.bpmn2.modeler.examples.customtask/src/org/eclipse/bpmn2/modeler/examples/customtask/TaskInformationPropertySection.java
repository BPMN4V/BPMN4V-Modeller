package org.eclipse.bpmn2.modeler.examples.customtask;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TaskInformationPropertySection extends DefaultPropertySection {

	public TaskInformationPropertySection() {
		super();
	}

	
	@Override
	protected AbstractDetailComposite createSectionRoot() {
		// This constructor is used to create the detail composite for use in the Property Viewer.
		return new TaskInformationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		// This constructor is used to create the detail composite for use in the popup Property Dialog.
		return new TaskInformationDetailComposite(parent, style);
	}
	
	
	
}
