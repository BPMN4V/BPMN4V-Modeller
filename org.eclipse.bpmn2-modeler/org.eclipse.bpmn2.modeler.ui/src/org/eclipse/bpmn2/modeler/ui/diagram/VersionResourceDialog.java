package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class VersionResourceDialog extends Dialog{
	public Button radio;
	public Button radio1;
	public Button radio2;
	public IFeatureProvider fp ;
	public ICustomContext con;
	
		public VersionResourceDialog(Shell shell) {
			super(shell);
			// TODO Auto-generated constructor stub
		}
		@Override
		public Control createDialogArea(Composite parent) {
			Composite composite = (Composite)super.createDialogArea(parent);
			 radio= new Button(composite, SWT.RADIO);
			radio.setText("Replace resource ")	;	
			 radio1= new Button(composite, SWT.RADIO);
			radio1.setText("Replace version of resource")	;	
			radio2= new Button(composite, SWT.RADIO);
			radio2.setText("Remouve resource ")	;	
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
			{
				this.close();
		}
			else
				if (radio1.getSelection()==true)
				{
			
			
		}
		
		}

}
