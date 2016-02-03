package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DefineProcessActivities extends AbstractCustomFeature {

	public DefineProcessActivities(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		final IFeatureProvider fp = getFeatureProvider();
		UpdateVersionProcessC d= new 	UpdateVersionProcessC(fp);
		CreateContext c=new CreateContext();
		c.setTargetContainer(getDiagram());
		
		c.setLocation(context.getX(), context.getY());
	
		d.create(c);

	}
	
	@Override
    public boolean canExecute(ICustomContext context) {
        // allow rename if exactly one pictogram element
        // representing a EClass is selected
        boolean ret = false;
        PictogramElement[] pes = context.getPictogramElements();
      boolean b=  TrouveProcessActivities(BPMNToolBehaviorProvider.id_v);
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo  instanceof BPMNDiagram && b==false) {
                ret = true;
            }
        }
        return ret;
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
