package org.eclipse.bpmn2.modeler.ui.diagram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DeriveVersionProcessParticipant extends AbstractCustomFeature {
public DeriveVersionProcessParticipant(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{
	Derive();
	
        
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
            if ( BPMNToolBehaviorProvider.state.compareTo("Stable")==0)  {
                ret = true;}
        }
        return ret;
    }
	
	
	@Override
    public String getName() {
        return "Derive";
    }
 
    @Override
    public String getDescription() {
        return "Derive Version";
    }
    protected String SelectVersionProcessActivities(String idvp)
	{
		
		String activities="";

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
		    	activities=activities+result.next().toString();
		    	
		    }
		    
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
		return activities;
	}
    
    
	
    public void Derive()
    {String id_dr=	BPMNToolBehaviorProvider.id_v;
	String name=BPMNToolBehaviorProvider.name_process;
	String activities=SelectVersionProcessActivities(id_dr);
	String s2;
	
			XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	 if (driver.isInitialized()==false)
	driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin(); 
    int j=0; String s="";
 s2=   SelectLastVProcess(name);
int i= SelectLastVprocess(name);
String next_id_s="";
j=s2.indexOf("-");
int f=i+1;
next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
String path =select_version_process_path(id_dr);
String path2=path.replace(id_dr, s2);

    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1)
     
     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>16/04/2014</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities>"+activities+"</activities><path>"+path2+"</path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
     IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
    
     session.commit();  
		    } finally {  
		      session.rollback();  
		    }
   
    try {
		ModelHandler handler = ModelHandler
				.getInstance(getDiagram());

		Process p =	 (Process) handler.findElement(BPMNToolBehaviorProvider.id_v);

p.setId(s2);

	} catch (IOException e) {
		Activator.logError(e);
	}
  
		
	
    
}
    public String SelectLastVProcess(String name)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vs");
   //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
     while(result.hasNext()) {  
  	   s2=result.next().toString();
  	   }
     s2=s2.substring(7);
     j=s2.indexOf("<");
    		 s2=s2.substring(0,j);
    		
     session.commit();  
		    } finally {  
		      session.rollback();  
		    } 

	return s2;
	}
	public int SelectLastVprocess(String name)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vn");
  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
    while(result.hasNext()) {  
 	   s2=result.next().toString();
 	   }
    s2=s2.substring(7);
    j=s2.indexOf("<");
   		 s2=s2.substring(0,j);
   		
    session.commit();  
		    } finally {  
		      session.rollback();  
		    } 
     i=Integer.parseInt(s2);
	return i;
		
		
	}
	public void CorrectProcessId(String path, String new_id, String new_name)
	{
		 Document document = null;
		 Element racine;
		Element element;
		int com=0;
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
		//On crée un nouveau document JDOM avec en argument le fichier XML
		//Le parsing est terminé ;)
		//	System.out.println("E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/"+path);
		document = sxb.build(new File(path));
		}
		catch(Exception e){}
		racine = document.getRootElement();
			
		
		List listFlow =racine.getChildren();
		// = element.getAttributes();
		
		//On crée un Iterator sur notre liste
		Iterator i = listFlow.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			
		if (courant.getName().compareTo("process")==0 )
		{
			System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("id"));
			System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("name"));
			courant.getAttribute("id").setValue(new_id);
			courant.getAttribute("name").setValue(new_name);
			com++;
		}
		
		
		}	
		XMLOutputter xmlOutput = new XMLOutputter();
		 
		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(document, new FileWriter(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		
		
	}
	
	public String select_version_process_path(String s)
	{String dervivé=null; 
		XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Processes.xml')//Process/versions/version where $a/id_v='"+s+"' return <id> {$a/path} </id>");
			
				while(result.hasNext()) {  
			    	  String  s1=result.next().toString();
			    	  
						
							 j=s1.indexOf("<path>");
							
							 s1= s1.substring(j+6);
							
							j=s1.indexOf("<");
							dervivé=s1.substring(0, j);
						
							
					
						
						}	  
			
				
				
	        
	      
	      session.commit();  
	    } finally {  
	      session.rollback();  
	    }  
	    return dervivé;
	}

}
	
