package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IContextMenuEntry;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DefineActivityInformation extends AbstractCustomFeature {

	public DefineActivityInformation(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		
		 IFeatureProvider featureProvider = getFeatureProvider();
		 PictogramElement[] pes = context.getPictogramElements();
		
		 EObject  bo = (EObject) featureProvider.getBusinessObjectForPictogramElement(pes[0]);
		SelectActivityInformationDialog d =new SelectActivityInformationDialog(BPMNToolBehaviorProvider.editor, bo);
d.open();
	}
	
	@Override
    public boolean canExecute(ICustomContext context) {
        // allow rename if exactly one pictogram element
        // representing a EClass is selected
        boolean ret = false;
        PictogramElement[] pes = context.getPictogramElements();
      boolean b=  Trouve_idva(BPMNToolBehaviorProvider.id_va);
        if (pes != null && pes.length == 1) {
            Object bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo  instanceof Task && b==false) {
                ret = true;
            }
        }
        return ret;
    }
	//cherche idva dans activityxml
	private boolean Trouve_idva(String idva)
	{boolean trouve=false;
	XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity where $o/versions/version/id_v='"+idva+"' return $o/name");
  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
    while(result.hasNext()) { 
    	s2=result.next().toString();
 	  trouve=true;
 	   }
   
   		
    session.commit();  
		    } finally {  
		      session.rollback();  
		    } 
    
	return trouve;
		
		
	}

		
	

}
