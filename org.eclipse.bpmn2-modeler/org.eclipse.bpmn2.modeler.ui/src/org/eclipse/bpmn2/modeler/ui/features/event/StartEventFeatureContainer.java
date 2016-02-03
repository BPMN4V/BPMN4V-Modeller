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
package org.eclipse.bpmn2.modeler.ui.features.event;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractCreateEventFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractUpdateEventFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AddEventFeature;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class StartEventFeatureContainer extends AbstractEventFeatureContainer {

	static final String INTERRUPTING = "interrupting"; //$NON-NLS-1$

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof StartEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateStartEventFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddStartEventFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(super.getUpdateFeature(fp));
		updateFeature.addUpdateFeature(new UpdateSubProcessEventFeature(fp));
		updateFeature.addUpdateFeature(new UpdateStartEventFeature(fp));
		return updateFeature;
	}

	public class AddStartEventFeature extends AddEventFeature<StartEvent> {
		public AddStartEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, StartEvent businessObject) {
			Ellipse e = (Ellipse)getGraphicsAlgorithm(containerShape);
			Graphiti.getPeService().setPropertyValue(containerShape, INTERRUPTING, Boolean.toString(true));
			IPeService peService = Graphiti.getPeService();
			peService.setPropertyValue(containerShape,
					UpdateStartEventFeature.START_EVENT_MARKER,
					AbstractUpdateEventFeature.getEventDefinitionsValue((StartEvent)businessObject));
			GraphicsAlgorithmContainer ga = getGraphicsAlgorithm(containerShape);
			IGaService service = Graphiti.getGaService();
			Image img2 = service.createImage(ga, "org.eclipse.bpmn2.modeler.icons.version.16");
			//System.out.print("ImageProvider.IMG_16_VERSIONS"+ImageProvider.IMG_16_VERSIONS);
			service.setLocationAndSize(img2, 10, 10, GraphicsUtil.EVENT_SIZE, GraphicsUtil.EVENT_SIZE);
		}
	}

	public class CreateStartEventFeature extends AbstractCreateEventFeature<StartEvent> {

		public CreateStartEventFeature(IFeatureProvider fp) {
			super(fp, Messages.StartEventFeatureContainer_1, Messages.StartEventFeatureContainer_2+Messages.StartEventFeatureContainer_3);
		}
		 @Override
			public Object[] create(ICreateContext context) {
			 String id=getDiagram().getName();
				String state="";
				if (id.startsWith("VP"))
				 state=Selectstate(getDiagram().getName());
				else
					if (id.startsWith("VC"))
					 state=SelectstateC(getDiagram().getName());
				//System.out.println("0710/2015"+getDiagram().getName());
				if (state.compareTo("Working")==0)
				{
				Event element = createBusinessObject(context);
				//if (element instanceof Task)
					//System.out.println("selectTaskId()"+selectTaskId());
				 id=selectEventId();
					element.setId("VE"+id+"-1");
					UpdateEventId(id);
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
				{
	JOptionPane.showMessageDialog(null, "Operation is not allowed because the version of process "+ BPMNToolBehaviorProvider.id_v+" is Stable","Error" ,JOptionPane.ERROR_MESSAGE);
					
					return new Object[] { null };
				}}
			
		 public String SelectstateC(String id_v)
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
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
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
			 public  String Selectstate(String id_v)
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
			
		

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_START_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getStartEvent();
		}
		  public  String selectEventId()
			{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
		     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Last_event.xml')/Events return $a/id");
				
					while(result.hasNext()) {  
				    	   s2=result.next().toString();
				    	   j=s2.indexOf("<id>");
					    	  s2=s2.substring(j+4);
					    	  j=s2.indexOf("</id>");
					    	  s2=s2.substring(0, j);
				    	 }
		       
		     
		     session.commit();  
		   } finally {  
		     session.rollback();  
		   }  
				return s2;
				
			}
		  public  void UpdateEventId(String id)
			{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			 if (driver.isInitialized()==false)
			driver.init();  
		     
		   XhiveSessionIf session = driver.createSession("xqapi-test");  
		   session.connect("Administrator", "imen", "vbpmn");  
		   session.begin();  
		  
		   try {  
		     XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		     // (1)
		     int j=0;
		     
		     j=Integer.parseInt(id);
		     j++;
		     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Last_event.xml')/Events let $o:='"+j+"' return replace value of node $a/id with $o");
				
					
		       
		     
		     session.commit();  
		   } finally {  
		     session.rollback();  
		   }  
				
				
			}
		
		
	}

	protected static class UpdateStartEventFeature extends AbstractUpdateEventFeature {

		public static String START_EVENT_MARKER = "marker.start.event"; //$NON-NLS-1$

		/**
		 * @param fp
		 */
		public UpdateStartEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.activity.AbstractUpdateMarkerFeature#getPropertyKey()
		 */
		@Override
		protected String getPropertyKey() {
			return START_EVENT_MARKER;
		}
	}

	private class UpdateSubProcessEventFeature extends AbstractUpdateFeature {

		public UpdateSubProcessEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
			return o != null && o instanceof StartEvent;
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			PictogramElement element = context.getPictogramElement();

			String prop = peService.getPropertyValue(element, INTERRUPTING);
			if (prop == null) {
				return Reason.createFalseReason();
			}

			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(element);
			boolean interrupting = Boolean.parseBoolean(prop);
			IReason reason = event.isIsInterrupting() == interrupting ? Reason.createFalseReason() : Reason
					.createTrueReason();
			return reason;
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(container);

			GraphicsAlgorithm ga = peService.getAllContainedShapes(container).iterator().next().getGraphicsAlgorithm();
			if (ga instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) ga;
				LineStyle style = event.isIsInterrupting() ? LineStyle.SOLID : LineStyle.DASH;
				ellipse.setLineStyle(style);
			}
			
			peService.setPropertyValue(container, INTERRUPTING, Boolean.toString(event.isIsInterrupting()));
			return true;
		}
	}
}