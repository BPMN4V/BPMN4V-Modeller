package org.eclipse.bpmn2.modeler.examples.customtask;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.swt.widgets.Composite;

public class TaskVersionHierarchyPropertySection extends DefaultPropertySection {

	public TaskVersionHierarchyPropertySection() {
		super();
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		// This constructor is used to create the detail composite for use in the Property Viewer.
		return new TaskVersionHierarchyDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		// This constructor is used to create the detail composite for use in the popup Property Dialog.
		return new TaskVersionHierarchyDetailComposite(parent, style);
	}
}

