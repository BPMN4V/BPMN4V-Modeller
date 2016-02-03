package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import java.util.Iterator;

import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryQueryIf;
import com.xhive.query.interfaces.XhiveXQueryResultIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class CreateNewVersion extends AbstractCustomFeature {
	public static String activity_name="";
	private boolean ok=false;

private Create_activity a;
	public CreateNewVersion(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{
	
		PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
           Object  bo = getBusinessObjectForPictogramElement(pes[0]);
            if (bo instanceof Task || bo instanceof Process) {
            	if (BPMNToolBehaviorProvider.boutonentry==true && BPMNToolBehaviorProvider.menuentry==false)
        		{Shell shell = new Shell();
        		select_process_name();
        			int j=0; String s1;
        			s1=bo.toString();
        			j=s1.indexOf("(name: ");
        			s1= s1.substring(j+7);

        			j=s1.indexOf(")");
        			s1=s1.substring(0, j);
        		    VersionDialog dialog =new VersionDialog(shell);
        	    	dialog.taskname=s1;
        	//	System.out.println("task name sub"+s1);
        	    	dialog.createDialogArea(shell);
        	    	dialog.setBlockOnOpen(false);
       
        	    	dialog.open();
                	
                }
            	else 
                	if (BPMNToolBehaviorProvider.boutonentry==false && BPMNToolBehaviorProvider.menuentry==true)
                	{Shell shell = getShell();
                	//	String s=ExampleUtil.askString("imen","imen","imen");
                	CreateActivityDialog a =new CreateActivityDialog(shell);
                	int i=a.open();
                	if (i == Window.OK) {
                		System.out.println("activity name"+CreateActivityDialog.activity_name);
                		((Task) bo).setName(CreateActivityDialog.activity_name);
                		System.out.println("extesionattribute.."+((Task) bo).getAnyAttribute().toString());
                	}
                		/*ExtensionAttributeValue e;
                		Object o;
                		o.
                		e.setValueRef(arg0);
                		((Task) bo).getExtensionValues().set(0, e)
            		}
                	/* a.intialize();
                	String s=a.selectionAction();
               
                	((Task) bo).setName(s);*/
              
                	}
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
        return "Versions";
    }
 
    @Override
    public String getDescription() {
        return "Create Versions";
    }
	
    public void select_process_name()
	
    {
    				
    	XhiveDriverIf driver= XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	 driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin();  
    System.out.println("hello");
    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1) 
      XhiveXQueryQueryIf xquery = rootLibrary.createXQuery("for $i in doc('Processes.xml')//Process return $i/name"); 
     // String xquery="for $i in doc('Processes.xml')//Process return $i/name";
    //  Iterator result = rootLibrary.executeXQuery(xquery);
    //  XhiveXQueryResultIf result= xquery.execute();
    		IterableIterator<? extends XhiveXQueryValueIf>result = xquery.execute();  
    		for (XhiveXQueryValueIf value : result) {  
    		  System.out.println(value);  
    		}    
     // IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary.executeXQuery("for $i in doc('Processes.xml')//Process return $i/name"); 
      while (result.hasNext()) {  
    	//  list.add(result.next().toString());
    	 System.out.println(result.next());  
        
      }  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
	}
}
