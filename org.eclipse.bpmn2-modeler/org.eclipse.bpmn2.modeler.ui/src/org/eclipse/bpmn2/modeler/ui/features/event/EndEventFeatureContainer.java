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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractCreateEventFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractUpdateEventFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AddEventFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.features.AbstractAppendNodeFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Image;
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

public class EndEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof EndEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateEndEventFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEndEventFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(super.getUpdateFeature(fp));
		updateFeature.addUpdateFeature(new UpdateEndEventFeature(fp));
		return updateFeature;
	}
	
	public class AddEndEventFeature extends AddEventFeature<EndEvent> {
		public AddEndEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, EndEvent businessObject) {
			Ellipse e = (Ellipse)getGraphicsAlgorithm(containerShape);
			e.setLineWidth(3);
			IPeService peService = Graphiti.getPeService();
			peService.setPropertyValue(containerShape,
					UpdateEndEventFeature.END_EVENT_MARKER,
					AbstractUpdateEventFeature.getEventDefinitionsValue((EndEvent)businessObject));
			GraphicsAlgorithmContainer ga = getGraphicsAlgorithm(containerShape);
			IGaService service = Graphiti.getGaService();
			Image img2 = service.createImage(ga, "org.eclipse.bpmn2.modeler.icons.version.16");
			//System.out.print("ImageProvider.IMG_16_VERSIONS"+ImageProvider.IMG_16_VERSIONS);
			service.setLocationAndSize(img2, 10, 10, GraphicsUtil.EVENT_SIZE, GraphicsUtil.EVENT_SIZE);
			
			
		}
	}

	public static class CreateEndEventFeature extends AbstractCreateEventFeature<EndEvent> {

		public CreateEndEventFeature(IFeatureProvider fp) {
			super(fp, Messages.EndEventFeatureContainer_0, Messages.EndEventFeatureContainer_1+Messages.EndEventFeatureContainer_2);
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
				{Event element = createBusinessObject(context);
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
					{JOptionPane.showMessageDialog(null, "Operation is not allowed because the version of process "+ BPMNToolBehaviorProvider.id_v+" is Stable","Error" ,JOptionPane.ERROR_MESSAGE);
				
				return new Object[] { null };}
		}
	

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_END_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getEndEvent();
		}
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

	protected static class UpdateEndEventFeature extends AbstractUpdateEventFeature {

		public static String END_EVENT_MARKER = "marker.end.event"; //$NON-NLS-1$

		/**
		 * @param fp
		 */
		public UpdateEndEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.activity.AbstractUpdateMarkerFeature#getPropertyKey()
		 */
		@Override
		protected String getPropertyKey() {
			return END_EVENT_MARKER;
		}
	}

	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		List<ICustomFeature> thisFeatures = new ArrayList<ICustomFeature>();
		int i;
		for (ICustomFeature f : superFeatures) {
			if (!(f instanceof AbstractAppendNodeFeature))
			thisFeatures.add(f);
		}
		return thisFeatures.toArray( new ICustomFeature[thisFeatures.size()] );
	}
}