package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

//import org.eclipse.bpmn2.modeler.ui.property.dialogs.Messages;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;

import org.eclipse.emf.ecore.EObject;
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

public class VersionDialog extends Dialog   {
public Button radio;
public Button radio1;
	
	public VersionDialog(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		 radio= new Button(composite, SWT.RADIO);
		radio.setText("Create new activity ")	;	
		 radio1= new Button(composite, SWT.RADIO);
		radio1.setText("Create new version of activity From existing one")	;	
		radio.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
			radio1.setSelection(false);
			radio.setSelection(true);
			}});
			
		radio1.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
			radio.setSelection(false);
			radio1.setSelection(true);
			}});
	return composite;}
	@Override
	  protected void okPressed() {
		if (radio1.getSelection()==true)
		{
			System.out.println("salut");
	//Create_activity a =new Create_activity();
	//a.intialize();}
		
		
	}
	
		
}
}