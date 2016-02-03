/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.activity.task;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.DirectEditBaseElementFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DirectEditTaskFeature extends DirectEditBaseElementFeature {

	public DirectEditTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		Object b=	getBusinessObjectForPictogramElement(context.getPictogramElement());
		int i=((Task)b).getId().indexOf("-");
		String j= (((Task)b).getId()).substring(i+1, ((Task)b).getId().length());
		System.out.println("1210215"+j);
		if( SelectActivitystate(((Task)b).getId()).compareTo("Working")==0 && j.compareTo("1")==0)
		{if (value.length() < 1) {
		
			return Messages.DirectEditTaskFeature_Invalid_Empty;
		} else if (value.contains("\n")) {  //$NON-NLS-1$
			return Messages.DirectEditTaskFeature_Invalid_Linebreak;
		}}
		else
			if( SelectActivitystate(((Task)b).getId()).compareTo("Working")==0 && j.compareTo("1")!=0)
				return "There are other versions of this task it could not renamed";
		else
			return "This is a stable version it could not be renamed";
		return null;
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
}