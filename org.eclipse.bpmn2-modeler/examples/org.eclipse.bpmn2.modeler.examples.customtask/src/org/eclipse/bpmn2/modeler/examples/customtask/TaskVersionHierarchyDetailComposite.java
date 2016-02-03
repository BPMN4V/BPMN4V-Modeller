package org.eclipse.bpmn2.modeler.examples.customtask;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class TaskVersionHierarchyDetailComposite extends DefaultDetailComposite {

	public TaskVersionHierarchyDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public TaskVersionHierarchyDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings(EObject be) {
		System.out.println("coucou");
		bindProperty(be,"Version_Hierarchy");
		
	}
}

