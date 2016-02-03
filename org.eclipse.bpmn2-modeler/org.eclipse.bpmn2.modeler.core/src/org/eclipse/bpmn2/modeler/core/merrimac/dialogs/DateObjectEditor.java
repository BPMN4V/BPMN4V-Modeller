package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.features.ShowPropertiesFeature;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.features.impl.AbstractFeature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
//import  org.eclipse.swt.widgets.Control;

public class DateObjectEditor extends ObjectEditor {

	protected DateTime date;
	Date d= null;
	protected Composite buttons = null;
	protected Button createButton = null;
	// protected DiagramEditor editor;
		
		//protected EObject object;
	public DateObjectEditor(AbstractDetailComposite parent, EObject object,
			EStructuralFeature feature) {
		super(parent, object, feature);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createControl(Composite composite, String label, int style) {
		createLabel(composite,label);
		date = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN);
		//date = new DateTime(composite, SWT.DATE);
		//date = new DateTime(composite, SWT.CALENDAR);
		date.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
			//	ModelUtil.getValue(object, feature);
			
				Calendar editorValue = null;
				editorValue = Calendar.getInstance();
				editorValue.set(date.getYear(), date.getMonth(), date.getDay());
				//originalCanonicalValue= this.dateTime.get;
				
				setValue(editorValue.getTime());
			}
		
				
		});
		
		buttons = null;
		
			buttons =  getToolkit().createComposite(composite);
			buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			buttons.setLayout(new FillLayout(SWT.HORIZONTAL));

		
				createButton = getToolkit().createButton(buttons, null, SWT.PUSH);
				createButton.setImage( Activator.getDefault().getImage(IConstants.ICON_ADD_20));
				createButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// create a new target object
						//try {
							System.out.println("bouton Hello");
							//VersionDialog dialog = new VersionDialog(parent.getShell());
							//if ( dialog.open() == Window.OK)
								// dialog.createDialogArea(parent);
							//IFeatureProvider fp = null;
							//ICustomContext context=null;
							//ShowPropertiesFeature f	=new ShowPropertiesFeature(fp);
							//f.execute(context);
							/*
							ICustomContext context;
							PictogramElement[] pes = context.getPictogramElements();
							DiagramEditor editor = (DiagramEditor)getDiagramBehavior().getDiagramContainer();
							editor.setPictogramElementForSelection(pes[0]);
					
							EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pes[0]);
							VersionDialog dialog = new VersionDialog(editor, businessObject);
							if ( dialog.open() == Window.OK)
								 dialog.createDialogContent(parent);
						}
						catch (OperationCanceledException ex1) {
						}
						catch (Exception ex2) {
							Activator.logError(ex2);
						}*/
					}
				});
			
		
		return date;
	}
	
	public void setEditable(boolean editable) {
		date.setEnabled(editable);
	}
	
	@Override
	public void setObject(EObject object) {
		super.setObject(object);
		//updateDate();
	}
	
	@Override
	public void setObject(EObject object, EStructuralFeature feature) {
		super.setObject(object, feature);
		//updateDate();
	}
	public void notifyChanged(Notification notification) {
		
		
		if (notification.getEventType() == -1) {
			setValue(getValue());
			super.notifyChanged(notification);
		}
		else if (object == notification.getNotifier()) {
			if (notification.getFeature() instanceof EStructuralFeature) {
				EStructuralFeature f = (EStructuralFeature)notification.getFeature();
				if (f!=null && (f.getName().equals(feature.getName()) ||
						f.getName().equals("mixed")) ) { // handle the case of FormalExpression.body //$NON-NLS-1$
					setValue(getValue());
					super.notifyChanged(notification);
				}
			}
		}
	}
	
	protected void updateDate() {
		try {
			isWidgetUpdating = true;
			
			setDate(getDate());
			
				
			
		}
		finally {
			isWidgetUpdating = false;
		}
	}

	public String getDate() {
		/*Calendar cal = Calendar.getInstance();
		cal.set(
				this.date.getYear(), 
				this.date.getMonth(), 
				this.date.getDay());
		return cal;*/
		
		return ModelUtil.getDisplayName(object, feature);
	}
	SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);
	 public  Date stringToDate(String sDate) throws ParseException {
	        return formatter.parse(sDate);
	    }
	public void setDate(String value) {
		//in setCanonicalValue() we ensure that the value is of type Calendar
		//but an additional check to ensure type safety doesn't hurt
		
		Calendar cal= Calendar.getInstance();
			 try {
				cal.setTime((Date)stringToDate(value));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
								e.printStackTrace();
				
			}
			
			this.date.setDate(
					cal.get(Calendar.YEAR), 
					cal.get(Calendar.MONTH), 
					cal.get(Calendar.DATE));
			this.date.setTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}
	
	@Override
	protected boolean setValue(final Object result) {
		
		if (super.setValue(result)) {
			updateDate();
			return true;
		}
		// revert the change on error
		date.setData(getDate());
	
				return false;
	}
	@Override
	public Date getValue() {
		// TODO Auto-generated method stub
		Object v = object.eGet(feature);
		Calendar editorValue = null;
	
		if (v instanceof Date)
			{
			editorValue = Calendar.getInstance();
		editorValue.setTime((Date)v);
	//	date.setData((DateTime)v);
// date.set
		//editorValue=(Date)v;
		
			}
		else if (v instanceof Calendar)
			{editorValue=(Calendar)v		;
			
			date=(DateTime)v;
			}
	
	 //d=	(Date) editorValue.getTime();
	
		
		
		
		
	//	this.date.setDate(d.getYear(), editorValue.get(Calendar.MONTH), editorValue.get(Calendar.DATE));
	  
		return editorValue.getTime();
		
	}
	
	

}
