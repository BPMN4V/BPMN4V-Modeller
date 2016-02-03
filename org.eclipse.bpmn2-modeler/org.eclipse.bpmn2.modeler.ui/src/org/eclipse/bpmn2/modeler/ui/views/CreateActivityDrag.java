package org.eclipse.bpmn2.modeler.ui.views;

import java.io.IOException;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateProcessDialog;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class CreateActivityDrag extends AbstractCreateTaskFeature<Task> {
	private String activity_name, activity_id;

	public CreateActivityDrag(IFeatureProvider fp) {
	//	super(fp, Messages.TaskFeatureContainer_Name, Messages.TaskFeatureContainer_Description);
		super(fp, "imen", "imen");
	}

	@Override
	public EClass getBusinessObjectClass() {
		// TODO Auto-generated method stub
		return Bpmn2Package.eINSTANCE.getTask();
	}

	@Override
	protected String getStencilImageId() {
		// TODO Auto-generated method stub
		return ImageProvider.IMG_16_TASK;
	}
	
	@Override
	public Object[] create(ICreateContext context) {
	
		Task element;
		
		 
	       	element = createBusinessObject(context);
	       	element.setName(activity_name);
	      element.setId(activity_id);
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
	    			return new Object[] { element };
	    		}
	    		else
	    			changesDone = false;
	    		return new Object[] { null };
	
	
	}
	

	public void setActivityName(String name)
	{activity_name=name;}
	public void setActivityId(String id)
	{activity_id=id;}
	
	

}
