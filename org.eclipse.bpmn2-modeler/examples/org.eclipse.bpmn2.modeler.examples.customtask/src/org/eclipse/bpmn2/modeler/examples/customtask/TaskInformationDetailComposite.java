package org.eclipse.bpmn2.modeler.examples.customtask;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.diagram.Intialize;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;

public class TaskInformationDetailComposite extends DefaultDetailComposite {
	public static List list;

	public TaskInformationDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public TaskInformationDetailComposite(Composite parent, int style) {
		super(parent, style);
		parent.getLayout();
		Label label = new Label(parent, SWT.NONE); 
	   	 label.setText("Information Name");
	  final List 	list = new List(parent, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	list.setLocation(200, 200);;
	  Intialize.select_data();int i=0;
	  while (Intialize.Information[i]!=null )
	  {list.add(Intialize.Information[i]);
	  i++;}
	  Label label2 = new Label(parent, SWT.NONE); 
	   	 label2.setText("Information Version Hierarchy");
	  Intialize.VH=  new Tree(parent, SWT.NO_SCROLL|SWT.MULTI);
	  list.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				//activity_name=list.getItem(list.getSelectionIndex());
				Intialize.VH.removeAll();
				Intialize.select_data_version(list.getItem(list.getSelectionIndex()));
			
			}});
	  
	  
	  //Intialize.select_data_version("Grid");
	}

	@Override
	public void createBindings(EObject be) {
		bindProperty(be,"Version_Id");
		/*bindProperty(be,"Version_Name");
		bindProperty(be,"Version_Creator");
		bindProperty(be,"Creation_Date");*/
	
		//bindProperty(be,"Version_Status");
	}
	
	
	
	
}


	