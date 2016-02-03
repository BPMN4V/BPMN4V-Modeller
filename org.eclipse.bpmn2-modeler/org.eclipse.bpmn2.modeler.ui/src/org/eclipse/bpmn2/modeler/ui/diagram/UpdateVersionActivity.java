package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class UpdateVersionActivity extends AbstractCustomFeature {
	public static EObject  bo;
public UpdateVersionActivity(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{	 IFeatureProvider featureProvider = getFeatureProvider();
PictogramElement[] pes = context.getPictogramElements();

  bo = (EObject) featureProvider.getBusinessObjectForPictogramElement(pes[0]);
UpdateActivityDialog d =new UpdateActivityDialog(BPMNToolBehaviorProvider.editor, bo);

d.open();
       	}//
       
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
           
            if (bo  instanceof Task && BPMNToolBehaviorProvider.state.compareTo("Working")==0 ) {
                ret = true;
            }
        }
        return ret;
    }
	
	
	@Override
    public String getName() {
        return "Update";
    }
 
    @Override
    public String getDescription() {
        return "Update Version";
    }
	
  //cherche idva dans activityxml
  
    
}

