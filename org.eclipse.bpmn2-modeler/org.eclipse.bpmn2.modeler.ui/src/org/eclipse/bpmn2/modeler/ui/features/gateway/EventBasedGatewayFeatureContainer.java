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
package org.eclipse.bpmn2.modeler.ui.features.gateway;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.EventBasedGatewayType;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.gateway.AbstractCreateGatewayFeature;
import org.eclipse.bpmn2.modeler.core.features.gateway.AddGatewayFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class EventBasedGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	static final String INSTANTIATE_PROPERTY = "instantiate"; //$NON-NLS-1$
	static final String EVENT_GATEWAY_TYPE_PROPERTY = "eventGatewayType"; //$NON-NLS-1$

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof EventBasedGateway;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateEventBasedGatewayFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventBasedGatewayFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(super.getUpdateFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateEventBasedGatewayFeature(fp));
		return multiUpdate;
	}

	public class AddEventBasedGatewayFeature extends AddGatewayFeature<EventBasedGateway> {
		public AddEventBasedGatewayFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, EventBasedGateway businessObject) {
			Ellipse outer = GraphicsUtil.createGatewayOuterCircle(containerShape);
			Ellipse inner = GraphicsUtil.createGatewayInnerCircle(outer);
			Polygon pentagon = GraphicsUtil.createGatewayPentagon(containerShape);
			pentagon.setFilled(false);
		}

		@Override
		public PictogramElement add(IAddContext context) {
			PictogramElement element = super.add(context);
			IPeService service = Graphiti.getPeService();
			EventBasedGateway gateway = getBusinessObject(context);
			service.setPropertyValue(element, INSTANTIATE_PROPERTY, Boolean.toString(gateway.isInstantiate()));
			service.setPropertyValue(element, EVENT_GATEWAY_TYPE_PROPERTY,
					EventBasedGatewayType.EXCLUSIVE.getName());
			return element;
		}
	}

	public static class CreateEventBasedGatewayFeature extends AbstractCreateGatewayFeature<EventBasedGateway> {

		public CreateEventBasedGatewayFeature(IFeatureProvider fp) {
			super(fp, Messages.EventBasedGatewayFeatureContainer_Name, Messages.EventBasedGatewayFeatureContainer_Description);
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_EVENT_BASED_GATEWAY;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getEventBasedGateway();
		}
		@Override
		public Object[] create(ICreateContext context) {
			String state=Selectstate(getDiagram().getName());
			if (state.compareTo("Working")==0)
			{EventBasedGateway element = createBusinessObject(context);
			//if (element instanceof Task)
				//System.out.println("selectTaskId()"+selectTaskId());
			
			if (element!=null) {
				changesDone = true;
				try {
					ModelHandler handler = ModelHandler.getInstance(getDiagram());
					if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
						((FlowNode) element).getLanes().add(
								(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
					}
					handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()), element);
				} catch (IOException e) {
					Activator.logError(e);
				}
				PictogramElement pe = null;
				pe = addGraphicalRepresentation(context, element);
				return new Object[] { element, pe };
			}
			else
				changesDone = false;
			return new Object[] { null };
		}
			else
			{System.out.println("01102015tttttttttttt");
			JOptionPane.showMessageDialog(null, "Operation is not allowed because the version of process "+ BPMNToolBehaviorProvider.id_v+" is Stable","Error" ,JOptionPane.ERROR_MESSAGE);
			
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