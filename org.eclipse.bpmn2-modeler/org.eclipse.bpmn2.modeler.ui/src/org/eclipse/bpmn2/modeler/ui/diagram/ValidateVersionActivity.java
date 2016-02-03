package org.eclipse.bpmn2.modeler.ui.diagram;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.views.ActivityVersionView;
import org.eclipse.bpmn2.modeler.ui.views.ActivityViewContentProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class ValidateVersionActivity extends AbstractCustomFeature {

public ValidateVersionActivity(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{
		updateState(BPMNToolBehaviorProvider.id_va);
		JOptionPane.showMessageDialog(null, "The version of activity "+ BPMNToolBehaviorProvider.id_va+" became Stable", "Validate version of activity", JOptionPane.INFORMATION_MESSAGE);
		ModelHandler modelHandler;
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(BPMNToolBehaviorProvider.editor.getDiagramTypeProvider()
					.getDiagram().eResource());
			TreeViewer viewer = null;
			ActivityViewContentProvider contentProvider =(ActivityViewContentProvider) ActivityVersionView.viewer.getContentProvider();
			
			contentProvider.updateModel(modelHandler);
			ActivityVersionView.viewer.refresh(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        
		
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
            
            if (bo  instanceof Task && BPMNToolBehaviorProvider.state.compareTo("Working")==0 ) {
                ret = true;
            }
        }
        return ret;
    }
	
	
	@Override
    public String getName() {
        return "Validate";
    }
 
    @Override
    public String getDescription() {
        return "Validate Version";
    }
	 public void updateState(String id_v)
	 {
		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		 
		 
		
		 
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity for $i in $a/versions/version let $o:='Stable' where $i/id_v='"+id_v+"' return replace value of node $i/state with $o");
		  
		 
		   		
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
	 }
    
    
}

