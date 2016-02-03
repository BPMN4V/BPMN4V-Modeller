package org.eclipse.bpmn2.modeler.examples.customtask;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TaskVersionDetailComposite extends DefaultDetailComposite {

	public TaskVersionDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public TaskVersionDetailComposite(Composite parent, int style) {
		super(parent, style);
		Label label = new Label(parent, SWT.NONE); 
	   	 label.setText("Activity Name");
	}

	@Override
	public void createBindings(EObject be) {
		bindProperty(be,"Version_Id");
		bindProperty(be,"Version_Name");
		bindProperty(be,"Version_Creator");
		bindProperty(be,"Creation_Date");
	
		//bindProperty(be,"Version_Status");
	}
}


	