package org.eclipse.bpmn2.modeler.ui.diagram;

import java.io.IOException;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.CreateSubProcessFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.AbstractTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ManualTaskFeatureContainer.AddManualTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ManualTaskFeatureContainer.CreateManualTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.Messages;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ReceiveTaskFeatureContainer.AddReceiveTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ReceiveTaskFeatureContainer.CreateReceiveTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.SendTaskFeatureContainer.AddSendTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.SendTaskFeatureContainer.CreateSendTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.UserTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.UserTaskFeatureContainer.AddUserTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.UserTaskFeatureContainer.CreateUserTaskFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class UpdateVersionProcessC extends AbstractCreateTaskFeature<Task> {

	private PictogramElement pe;
	private IFeatureProvider ifp;
	public UpdateVersionProcessC(IFeatureProvider fp) {
	//	super(fp, Messages.TaskFeatureContainer_Name, Messages.TaskFeatureContainer_Description);
		super(fp, "imen", "imen");
		ifp=fp;
	}

	@Override
	public EClass getBusinessObjectClass() {
		// TODO Auto-generated method stub
		return Bpmn2Package.eINSTANCE.getTask();
	}

	@Override
	protected String getStencilImageId() {
		// TODO Auto-generated method stub
		return ImageProvider.IMG_16_USER_TASK;
	}
	
	/*@Override
	public Object[] create(ICreateContext context) {
	
	Task element;
		
		  Shell shell = getShell();
	       	//	String s=ExampleUtil.askString("imen","imen","imen");
	           UpdateProcessDialog a =new UpdateProcessDialog(shell);
	       	int i=a.open();
	       	
	       	
	       	if (i == Window.OK) {
	       		
	      
	       		
	       	element =  createBusinessObject(context);
	       	element.setName(a.getactivityname());
	      element.setId(a.getactivityid());
	      
	    		if (element!=null) {
	    			changesDone = true;
	    			try {
	    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
	    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
	    					((FlowNode) element).getLanes().add(
	    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
	    				}
	    				
	    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
	    			} catch (IOException e) {
	    				Activator.logError(e);
	    			}
	    			pe = null;
	    			pe = addGraphicalRepresentation(context, element);
	    			CreateUserTaskFeature cjj=new CreateUserTaskFeature(ifp);
	    			UserTask userTask = cjj.createBusinessObject(context);
	    			AddContext c=new AddContext();
	    			AddUserTaskFeature add =new AddUserTaskFeature(ifp);
	    			add.putBusinessObject(c, userTask);
	    			
	    			//IPeService peService = Graphiti.getPeService();
	    			//Shape textShape = peService.createShape(pe, false);
	    			/*GraphicsAlgorithmContainer ga = pe.getGraphicsAlgorithm();
	    					//pe.getGraphicsAlgorithm();
	    		
	    			IGaService service = Graphiti.getGaService();
	    			Image img2 = service.createImage(ga, "org.eclipse.bpmn2.modeler.ui.usertask.16");
	    			System.out.print("ImageProvider.IMG_16_VERSIONS"+ImageProvider.IMG_16_USER_TASK);
	    			service.setLocation(img2, 20, 30);

	    			
	    			//pe.setGraphicsAlgorithm(img2.getParentGraphicsAlgorithm());
	    			//link(pe, img2);
	    			link(pe, img2);
	    			
	    			return new Object[] { element, pe };
	    		}
	    		else
	    			changesDone = false;
	    		return new Object[] { null };
	       		
	       	}
	       	return new Object[] { null };
	
	
	}*/
	@Override
	public Object[] create(ICreateContext context) {
	
		
		
		  Shell shell = getShell();
	       	//	String s=ExampleUtil.askString("imen","imen","imen");
	           UpdateProcessDialog a =new UpdateProcessDialog(shell);
	       	int i=a.open();
	       	
	       	
	       	if (i == Window.OK) {
	       		
	      if (a.getactivitytype().compareTo("User Task")==0)
	       		
	      {UserTask element = null;
	    			pe = null;
	    			
	    			CreateUserTaskFeature cjj=new CreateUserTaskFeature(ifp);
	    			 element = cjj.createBusinessObject(context);
	    			 	element.setName(a.getactivityname());
	    			      element.setId(a.getactivityid());
	    			      //element.s
	    			//AddContext c=new AddContext();
	    		//	AddUserTaskFeature add =new AddUserTaskFeature(ifp);
	    		//	add.putBusinessObject(c, element);
	    		//	pe = addGraphicalRepresentation(context, element);
	    			if (element!=null) {
		    			changesDone = true;
		    			try {
		    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
		    				
		    				
		    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
		    			} catch (IOException e) {
		    				Activator.logError(e);
		    			}
		    			pe = null;
		    			pe = addGraphicalRepresentation(context, element);
		    			
		    			
		    			
		    			
		    			return new Object[] { element, pe };
		    		}
		    		else
		    			changesDone = false;
		    		return new Object[] { null };
	    		}
	      else 
	    	  if (a.getactivitytype().compareTo("Manual Task")==0)
		       		
		      {ManualTask element = null;
		    			pe = null;
		    			
		    			CreateManualTaskFeature cjj=new CreateManualTaskFeature(ifp);
		    			 element = cjj.createBusinessObject(context);
		    			 	element.setName(a.getactivityname());
		    			      element.setId(a.getactivityid());
		    			/*AddContext c=new AddContext();
		    			AddManualTaskFeature add =new AddManualTaskFeature(ifp);
		    			add.putBusinessObject(c, element);*/
		    			pe = addGraphicalRepresentation(context, element);
		    			if (element!=null) {
			    			changesDone = true;
			    			try {
			    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
			    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
			    					((FlowNode) element).getLanes().add(
			    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			    				}
			    				
			    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
			    			} catch (IOException e) {
			    				Activator.logError(e);
			    			}
			    			pe = null;
			    			pe = addGraphicalRepresentation(context, element);
			    			
			    			
			    			
			    			
			    			return new Object[] { element, pe };
			    		}
			    		else
			    			changesDone = false;
			    		return new Object[] { null };
		    		
		    		}
	    	  else
	      if (a.getactivitytype().compareTo("Send Task")==0)
	       		
	      {SendTask element = null;
	    			pe = null;
	    			
	    			CreateSendTaskFeature cjj=new CreateSendTaskFeature(ifp);
	    			 element = cjj.createBusinessObject(context);
	    			 	element.setName(a.getactivityname());
	    			      element.setId(a.getactivityid());
	    			/*AddContext c=new AddContext();
	    			AddSendTaskFeature add =new AddSendTaskFeature(ifp);
	    			add.putBusinessObject(c, element);*/
	    			pe = addGraphicalRepresentation(context, element);
	    			
	    			if (element!=null) {
		    			changesDone = true;
		    			try {
		    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
		    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
		    					((FlowNode) element).getLanes().add(
		    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		    				}
		    				
		    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
		    			} catch (IOException e) {
		    				Activator.logError(e);
		    			}
		    			pe = null;
		    			pe = addGraphicalRepresentation(context, element);
		    			
		    			
		    			
		    			
		    			return new Object[] { element, pe };
		    		}
		    		else
		    			changesDone = false;
		    		return new Object[] { null };
	    		
	    		}
	      else
	      if (a.getactivitytype().compareTo("Receive Task")==0)
	       		
	      {ReceiveTask element = null;
	    			pe = null;
	    			
	    			CreateReceiveTaskFeature cjj=new CreateReceiveTaskFeature(ifp);
	    			 element = cjj.createBusinessObject(context);
	    			 	element.setName(a.getactivityname());
	    			      element.setId(a.getactivityid());
	    			/*AddContext c=new AddContext();
	    			AddReceiveTaskFeature add =new AddReceiveTaskFeature(ifp);
	    			add.putBusinessObject(c, element);*/
	    			pe = addGraphicalRepresentation(context, element);
	    			
	    			
	    			if (element!=null) {
		    			changesDone = true;
		    			try {
		    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
		    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
		    					((FlowNode) element).getLanes().add(
		    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		    				}
		    				
		    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
		    			} catch (IOException e) {
		    				Activator.logError(e);
		    			}
		    			pe = null;
		    			pe = addGraphicalRepresentation(context, element);
		    			
		    			
		    			
		    			
		    			return new Object[] { element, pe };
		    		}
		    		else
		    			changesDone = false;
		    		return new Object[] { null };
	    		
	    		}
	      else
		      if (a.getactivitytype().compareTo("Sub Process")==0)
		       		
		      {SubProcess element = null;
		    			pe = null;
		    			
		    			CreateSubProcessFeature cjj=new CreateSubProcessFeature(ifp);
		    			 element = cjj.createBusinessObject(context);
		    			 	element.setName(a.getactivityname());
		    			      element.setId(a.getactivityid());
		    			
		    			pe = addGraphicalRepresentation(context, element);
		    			
		    			
		    			if (element!=null) {
			    			changesDone = true;
			    			try {
			    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
			    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
			    					((FlowNode) element).getLanes().add(
			    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			    				}
			    				
			    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
			    			} catch (IOException e) {
			    				Activator.logError(e);
			    			}
			    			pe = null;
			    			pe = addGraphicalRepresentation(context, element);
			    			
			    			
			    			
			    			
			    			return new Object[] { element, pe };
			    		}
			    		else
			    			changesDone = false;
			    		return new Object[] { null };
		    		
		    		}
		      else
		      {
		    		Task element;
			       	element =  createBusinessObject(context);
			       	element.setName(a.getactivityname());
			      element.setId(a.getactivityid());
			      
			    		if (element!=null) {
			    			changesDone = true;
			    			try {
			    				ModelHandler handler = ModelHandler.getInstance(getDiagram());
			    				if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
			    					((FlowNode) element).getLanes().add(
			    							(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			    				}
			    				
			    				handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()),  element);
			    			} catch (IOException e) {
			    				Activator.logError(e);
			    			}
			    			pe = null;
			    			pe = addGraphicalRepresentation(context, element);
			    			
			    			
			    			
			    			
			    			return new Object[] { element, pe };
			    		}
			    		else
			    			changesDone = false;
			    		return new Object[] { null };
		      }
	       	}
	    		
	       		
	       	
	       	return new Object[] { null };
	
	
	}
	
	
	
	
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	protected static GraphicsAlgorithmContainer getGraphicsAlgorithm(ContainerShape containerShape) {
		if (containerShape.getGraphicsAlgorithm() instanceof RoundedRectangle)
			return containerShape.getGraphicsAlgorithm();
		if (containerShape.getChildren().size()>0) {
			Shape shape = containerShape.getChildren().get(0);
			return shape.getGraphicsAlgorithm();
		}
		return null;
	}
	
	
	

}
