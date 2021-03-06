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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.data;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.data.AddDataFeature;
import org.eclipse.bpmn2.modeler.core.features.label.UpdateLabelFeature;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DataObjectReferenceFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataObjectReference;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectReferenceFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataObjectReferenceFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateDataObjectFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateLabelFeature(fp));
		return multiUpdate;
	}

	public class AddDataObjectReferenceFeature extends AddDataFeature<DataObjectReference> {
		public AddDataObjectReferenceFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public String getName(DataObjectReference t) {
			return t.getName();
		}
	}

	public static class CreateDataObjectReferenceFeature extends AbstractCreateFlowElementFeature<DataObjectReference> {

		public CreateDataObjectReferenceFeature(IFeatureProvider fp) {
			super(fp, Messages.DataObjectReferenceFeatureContainer_Name, Messages.DataObjectReferenceFeatureContainer_Description);
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_OBJECT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataObjectReference();
		}
		@Override
		public Object[] create(ICreateContext context) {
			String state=Selectstate(getDiagram().getName());
			//System.out.println("0710/2015"+getDiagram().getName());
			if (state.compareTo("Working")==0)
			{DataObjectReference element = createBusinessObject(context);
			try {
				ModelHandler handler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				handler.addDataInputOutput(context.getTargetContainer(), element);
			} catch (IOException e) {
				Activator.logError(e);
			}
			addGraphicalRepresentation(context, element);
			return new Object[] { element };
		}
		else
			{JOptionPane.showMessageDialog(null, "Operation is not allowed because the version of process "+ BPMNToolBehaviorProvider.id_v+" is Stable","Error" ,JOptionPane.ERROR_MESSAGE);
		
		return new Object[] { null };}
}


		
	
		
	 public static String Selectstate(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(j+"s2"+s2);
	 	   j++;
	 	   }
	    s2=s2.substring(7);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	}
}