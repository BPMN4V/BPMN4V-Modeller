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

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.features.data.AbstractCreateDataInputOutputFeature;
import org.eclipse.bpmn2.modeler.core.features.data.AddDataFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DataInputFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataInput;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataInputFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataInputFeature(fp);
	}

	public class AddDataInputFeature extends AddDataFeature<DataInput> {
		public AddDataInputFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected boolean isSupportCollectionMarkers() {
			return false;
		}

		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, DataInput businessObject) {
			Polygon p = (Polygon)getGraphicsAlgorithm(containerShape);
			Polygon arrow = GraphicsUtil.createDataArrow(p);
			arrow.setFilled(false);
			arrow.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			GraphicsAlgorithmContainer ga = getGraphicsAlgorithm(containerShape);
			IGaService service = Graphiti.getGaService();
			Image img2 = service.createImage(ga, "org.eclipse.bpmn2.modeler.icons.version.16");
			//System.out.print("ImageProvider.IMG_16_VERSIONS"+ImageProvider.IMG_16_VERSIONS);
			service.setLocationAndSize(img2, 10, 10, GraphicsUtil.DATA_WIDTH, GraphicsUtil.DATA_HEIGHT);
		}

		@Override
		public String getName(DataInput t) {
			return t.getName();
		}
	}

	public static class CreateDataInputFeature extends AbstractCreateDataInputOutputFeature<DataInput> {

		public CreateDataInputFeature(IFeatureProvider fp) {
			super(fp, Messages.DataInputFeatureContainer_Name, Messages.DataInputFeatureContainer_Description);
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_INPUT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataInput();
		}
		@Override
		public Object[] create(ICreateContext context) {
			String state=Selectstate(getDiagram().getName());
			//System.out.println("0710/2015"+getDiagram().getName());
			if (state.compareTo("Working")==0)
			{DataInput element = createBusinessObject(context);
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