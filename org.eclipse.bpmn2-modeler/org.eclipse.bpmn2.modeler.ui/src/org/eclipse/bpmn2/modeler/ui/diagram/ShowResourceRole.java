package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.BusinessRuleTaskFeatureContainer;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


public class ShowResourceRole extends AbstractCustomFeature {

	public ShowResourceRole(IFeatureProvider fp) {
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{
	
		PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
           Object  bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo instanceof Task) {
            	int j=0; String s1;
            	
            	
    			s1=bo.toString();
    			j=s1.indexOf("mm:Version_Id=");
    			s1= s1.substring(j+14);

    			j=s1.indexOf("]");
    			s1=s1.substring(0, j);
    			System.out.println("version id"+s1);
            	//ResourceRoleDialog r=new ResourceRoleDialog(getShell());
            	//r.taskname=((Task) bo).getName();
            	//r.taskversionid=s1;
            	//r.open();
    			BusinessRuleTaskFeatureContainer b=new BusinessRuleTaskFeatureContainer();
    			IFeatureProvider fp = getFeatureProvider();
    			b.getAddFeature(fp);
    		
            }}
        
        }		
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}


	
	@Override
    public boolean canExecute(ICustomContext context) {
        // allow rename if exactly one pictogram element
        // representing a EClass is selected
        boolean ret = false;
        PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo  instanceof Task) {
                ret = true;
            }
        }
        return ret;
    }
	
	
	@Override
    public String getName() {
        return "ResourceRole Versions";
    }
 
    @Override
    public String getDescription() {
        return "ResourceRole Versions";
    }
	
	

}

