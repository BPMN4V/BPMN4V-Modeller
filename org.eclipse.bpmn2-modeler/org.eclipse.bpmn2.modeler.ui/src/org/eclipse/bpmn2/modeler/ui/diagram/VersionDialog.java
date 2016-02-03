package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.modeler.core.features.ShowPropertiesFeature;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

public class VersionDialog extends Dialog   {
public Button radio;
public Button radio1;
public Button radio2;
public IFeatureProvider fp ;
public ICustomContext con;
public String taskname="";
public String taskname1="";
public static Boolean entry=false;
	public VersionDialog(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		 radio= new Button(composite, SWT.RADIO);
		radio.setText("Replace activity ")	;	
		 radio1= new Button(composite, SWT.RADIO);
		radio1.setText("Replace version of activity")	;	
		radio2= new Button(composite, SWT.RADIO);
		radio2.setText("Remouve activity ")	;	
		radio.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
			radio1.setSelection(false);
			radio.setSelection(true);
			radio2.setSelection(false);
			}});
			
		radio1.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
			radio.setSelection(false);
			radio1.setSelection(true);
			radio2.setSelection(false);
			}});
radio2.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
			radio.setSelection(false);
			radio1.setSelection(false);
			radio2.setSelection(true);
			}});
	return composite;}
	@Override
	  protected void okPressed() {
		if (radio.getSelection()==true)
		{/*Shell shell=new Shell();
			
			CreateActivityDialog d= new CreateActivityDialog(shell);
			if(entry==false)
			{
			d.open();}
		else 
			if (entry==true)
				{taskname1=d.getactivityname();
				this.close();
				
				}*/
			this.close();
	Create_activity a =new Create_activity();
	
	a.intialize();
			}
		else
			if (radio1.getSelection()==true)
			{this.close();
			Create_version_activity a =new Create_version_activity();
			a.intialize();
			//System.out.println("taskname"+taskname);
			a.select_activity_version(taskname);
		
		
	}
	
	}
		
		
}
