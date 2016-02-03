package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
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

public class UpdateVersionProcessParticipant extends AbstractCustomFeature {
public UpdateVersionProcessParticipant(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{	final IFeatureProvider fp = getFeatureProvider();
		UpdateVersionProcessC d= new 	UpdateVersionProcessC(fp);
		CreateContext c=new CreateContext();
		c.setTargetContainer(getDiagram());
		
		c.setLocation(context.getX(), context.getY());
		//AddTaskFeature a=new AddTaskFeature(fp);
		
		d.create(c);
		/*PictogramElement[] pes = context.getPictogramElements();
        if (pes != null && pes.length == 1) {
           Object  bo = getBusinessObjectForPictogramElement(pes[0]);
           Task T = null;
           Shell shell = getShell();
       	//	String s=ExampleUtil.askString("imen","imen","imen");
           UpdateProcessDialog a =new UpdateProcessDialog(shell);
       	int i=a.open();
       	if (i == Window.OK) {
       		//this.addGraphicalRepresentation((IAreaContext) context, T);
       		final IFeatureProvider fp = getFeatureProvider();
       		
       		TaskFeatureContainer t=new TaskFeatureContainer();
       		
       		t.getCreateFeature(fp);
       		t.getAddFeature(fp);
       		/*CreateContext co=new CreateContext();
       		CreateTaskFeature c= new CreateTaskFeature(fp);
       		//co.setTargetContainer(getDiagram().getContainer());
      //c.create(co);
     
      ContainerShape targetContainer = this.getDiagram();
      AddTaskFeature<Task> add= new AddTaskFeature<Task>(fp);
      AddContext addc=new AddContext();
     
      addc.setTargetContainer(targetContainer);
      System.out.println("addc"+addc.getTargetContainer().toString());
      addc.setSize(10,50);
      addc.setLocation(200, 400);
      addc.putProperty(DIImport.IMPORT_PROPERTY, Boolean.TRUE);
      addc.setNewObject(T);
      add.add(addc);*/
       		//Task element = c.createBusinessObject(co);
       		//AddTaskFeature<Task> add= new AddTaskFeature<Task>(fp);
       		//AddContext c=new AddContext();
       		//IAreaContext c=new AreaContext();
      	//c.setTargetContainer(getDiagram());
       		//this.addGraphicalRepresentation(co, element);
       	//	c.setTargetContainer(this.getDiagramEditor().);
       		//
			//c.setTargetContainer(targetContainer);
       //	add.add(c);
       		
       	//	c.execute(context);
       		//c.createBusinessObject(context);
       		//System.out.println("c.getCreateName()"+c.getCreateName());
       		//c.execute(context);
       		
       		//ICreateContext con= (ICreateContext)context;
       		
       		//c.create(con);
       		//c.create(context);
       	}//
       
	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}


	
	@Override
    public boolean canExecute(ICustomContext context) {
        // allow rename if exactly one pictogram element
        // representing a EClass is selected
        boolean ret = false;
       // boolean b=  TrouveProcessActivities(BPMNToolBehaviorProvider.id_v);
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
        return "Update";
    }
 
    @Override
    public String getDescription() {
        return "Update Version";
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

