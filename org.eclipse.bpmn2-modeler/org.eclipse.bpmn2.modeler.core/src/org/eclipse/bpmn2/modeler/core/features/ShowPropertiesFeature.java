/*******************************************************************************
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 * All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 *
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramContainer;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class ShowPropertiesFeature extends AbstractCustomFeature {

	protected boolean changesDone = false;
	
	public ShowPropertiesFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return Messages.ShowPropertiesFeature_Name;
	}

	@Override
	public String getDescription() {
		return Messages.ShowPropertiesFeature_Title;
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pes[0]);
		if (pes.length==1) {
			return Bpmn2Preferences.getInstance(businessObject).hasPopupConfigDialog(businessObject);
		}
		return false;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		DiagramEditor editor = (DiagramEditor)getDiagramBehavior().getDiagramContainer();
		editor.setPictogramElementForSelection(pes[0]);
		getDiagramBehavior().refresh();
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pes[0]);
	Object b=	getBusinessObjectForPictogramElement(pes[0]);
	if (b instanceof Task)
	{Task t= (Task)b;
	int i=((Task)b).getId().indexOf("-");
	String j= (((Task)b).getId()).substring(i+1, ((Task)b).getId().length());
	//System.out.println("1210215"+j);
	if( SelectActivitystate(((Task)b).getId()).compareTo("Working")==0 && j.compareTo("1")==0)
	{
		ObjectEditingDialog dialog =
				new ObjectEditingDialog(editor, businessObject);
		if (dialog.open() == Window.OK)
			changesDone = dialog.hasDoneChanges();
		else
			changesDone = false;
	}
	else
		if( SelectActivitystate(((Task)b).getId()).compareTo("Working")==0 && j.compareTo("1")!=0)
		{	JOptionPane.showMessageDialog(null, "There are other versions of this task it could not edited", "Edit Version", JOptionPane.ERROR_MESSAGE);}	
		else
			{JOptionPane.showMessageDialog(null, "This is a stable version it could not be renamed", "Edit Version", JOptionPane.ERROR_MESSAGE);}	
	}
	else
	{ObjectEditingDialog dialog =
	new ObjectEditingDialog(editor, businessObject);
if (dialog.open() == Window.OK)
changesDone = dialog.hasDoneChanges();
else
changesDone = false;}
	}
	
	public String SelectActivitystate(String id_v)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	int i=0;
	 if (driver.isInitialized()==false)
	driver.init();  
    
  XhiveSessionIf session = driver.createSession("xqapi-test");  
  session.connect("Administrator", "imen", "vbpmn");  
  session.begin();  
  String s2="";
  try {  
    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
    // (1)
    int j=0;
    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
    while(result.hasNext()) {  
 	   s2=result.next().toString();
 	 //  System.out.println(j+"s2"+s2);
 	  s2=s2.substring(7);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
 	   j++;
 	   }
   
   		
    session.commit();  
		    } finally {  
		      session.rollback();  
		    } 
    if (s2.compareTo("")==0)
    	return "Working";
    else
	return s2;
		
		
	}
	@Override
	public boolean hasDoneChanges() {
		return changesDone;
	}

	@Override
	public String getImageId() {
		return IConstants.ICON_PROPERTIES_16;
	}

}
