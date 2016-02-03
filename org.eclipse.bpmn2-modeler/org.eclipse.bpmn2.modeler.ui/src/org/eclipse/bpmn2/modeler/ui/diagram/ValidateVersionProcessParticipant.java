package org.eclipse.bpmn2.modeler.ui.diagram;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.Activator;
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

public class ValidateVersionProcessParticipant extends AbstractCustomFeature {
	private String [] ProcessVersionActivitiesId=new String[20];

public ValidateVersionProcessParticipant(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{
		updateState(BPMNToolBehaviorProvider.id_v);
		/*SelectVersionProcessActivities(BPMNToolBehaviorProvider.id_v);
		int j=0;
		while (ProcessVersionActivitiesId[j]!=null)
		{
			updateActivityState(ProcessVersionActivitiesId[j])	;
			j++;
		}*/
		try {
			ModelHandler handler = ModelHandler
					.getInstance(getDiagram());

			Process p =	 (Process) handler.findElement(BPMNToolBehaviorProvider.id_v);

List<FlowElement> lf=p.getFlowElements();
Iterator<FlowElement> f=lf.iterator();
while (f.hasNext())
{FlowElement flow=f.next();
updateActivityState(flow.getId());
	}
	
		} catch (IOException e) {
			Activator.logError(e);
		}
		
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
		
		JOptionPane.showMessageDialog(null, "The version of process "+ BPMNToolBehaviorProvider.id_v+" became Stable. This version can be instantiated", "Validate version of Process", JOptionPane.INFORMATION_MESSAGE);
		
		
        }		
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}


	
	@Override
    public boolean canExecute(ICustomContext context) {
        // allow rename if exactly one pictogram element
        // representing a EClass is selected
        boolean ret = false;
     //   boolean b=  TrouveProcessActivities(BPMNToolBehaviorProvider.id_v);
        PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            
            if ( BPMNToolBehaviorProvider.state.compareTo("Working")==0)  {
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
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process for $i in $p/versions/version let $o:='Stable' where $i/id_v='"+id_v+"' return replace value of node $i/state with $o");
		  
		 
		   		
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
	 }
	 public void updateActivityState(String id_v)
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
	 protected void SelectVersionProcessActivities(String idvp)
		{
			
			

			 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
				int i=0;
				 if (driver.isInitialized()==false)
				driver.init();  
			    int j=0;
			  XhiveSessionIf session = driver.createSession("xqapi-test");  
			  session.connect("Administrator", "imen", "vbpmn");  
			  session.begin();  
			  try {  
			    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
			    // (1)
			   
			    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process for $i in $p/versions/version  where $i/id_v='"+idvp+"' return  $i/activities/id_va");
			   		
			    while(result.hasNext()) {  
			    	String s2=result.next().toString();
			    	 s2=s2.substring(7);
			         j=s2.indexOf("<");
			        		 s2=s2.substring(0,j);
			        		 
			        		 ProcessVersionActivitiesId[i]=s2;
			    	//System.out.println("ProcessVersionActivitiesId[i]"+ProcessVersionActivitiesId[i]);
			    	i++;
			    }
			    
			    session.commit();  
					    } finally {  
					      session.rollback();  
					    } 
			
		}
	 protected boolean TrouveProcessActivities(String idvp)
		{
	boolean trouve=false;
			 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
				int i=0;
				 if (driver.isInitialized()==false)
				driver.init();  
			    int j=0;
			  XhiveSessionIf session = driver.createSession("xqapi-test");  
			  session.connect("Administrator", "imen", "vbpmn");  
			  session.begin();  
			  try {  
			    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
			    // (1)
			   
			    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process for $i in $p/versions/version  where $i/id_v='"+idvp+"' return  $i/activities/id_va");
			   		
			    while(result.hasNext()) {  
			    	String s2=result.next().toString();
			    	trouve=true;
			    }
			    
			    session.commit();  
					    } finally {  
					      session.rollback();  
					    } 
			  return trouve;
			
		}
		
	    
}
