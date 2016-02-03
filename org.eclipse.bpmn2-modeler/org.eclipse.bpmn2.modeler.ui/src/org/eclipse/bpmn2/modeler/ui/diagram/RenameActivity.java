package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

public class RenameActivity extends AbstractCustomFeature {
	public static String activity_name="";
	

private Create_activity a;
	public RenameActivity(IFeatureProvider fp) {
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
           
         					//((Task) bo).setName(a.list.getItem(a.list.getSelectionIndex()));
         					((Task) bo).setName("imen");
         					
         					
                	
            }}
        
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
	
	
	
	

}



