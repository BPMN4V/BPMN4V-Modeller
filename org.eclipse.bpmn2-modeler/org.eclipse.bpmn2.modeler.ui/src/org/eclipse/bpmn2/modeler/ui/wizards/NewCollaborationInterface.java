package org.eclipse.bpmn2.modeler.ui.wizards;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.AbstractObjectEditingDialog;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.SelectActivityInformationDialog;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateVersionActivity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class NewCollaborationInterface extends FormDialog {
	public NewCollaborationInterface(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}

	private String [] data =new String[20];
	public static boolean updatemenu;
	public static  Text 	list, list2, list3;
public static String collaboration_name, process1_name, process2_name;
	

	@Override
	protected Composite createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		layout.numColumns = 3;
		
		//layout.horizontalSpacing
		//layout.verticalSpacing = 4;
		// TODO Auto-generated method stub
		Label label = new Label(parent, SWT.NONE); 
	   	 label.setText("Collaboration Name ");
	   	// label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
	   		list = new Text(parent, SWT.BORDER);
		  list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		 
		 
	   	Label label3 = new Label(parent, SWT.NONE); 
	   	 label3.setText("Process Name");
	 //	 label3.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
 	list2 = new Text(parent, SWT.BORDER);
		  list2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		 
		  Label label4 = new Label(parent, SWT.NONE); 
		   	 label4.setText("Process Name");
		// 	 label4.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
	 	list3 = new Text(parent, SWT.BORDER);
			  list3.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2,1 ));
			 
		return parent;
	}
	


	@Override
	protected void okPressed() {
		//cancel = false;
		//dialogContent.dispose();
		collaboration_name=list.getText();
				process1_name=list2.getText();
				 process2_name=list3.getText();
				 this.close();
		
	}
	
}
