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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.views.ActivityVersionView;
import org.eclipse.bpmn2.modeler.ui.views.ActivityViewContentProvider;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
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

public class DervieVersionActivity extends AbstractCustomFeature {
	
	public static String new_activity_id, old_activity_id, id_drv;
	public static Boolean dervive_process_working=false;
public DervieVersionActivity(IFeatureProvider fp) {
		
		super(fp);
		
		
		// TODO Auto-generated constructor stub
	}
	@Override
public void execute( ICustomContext  context)
{DeriveVersionActivity();
if (BPMNToolBehaviorProvider.process_state.compareTo("Stable")==0)
	{if (BPMNToolBehaviorProvider.id_v.startsWith("VP"))
{DeriveProcess();
String path2=select_version_process_path(id_drv);
File fileToCorrect = new File(path2);
		 CorrectDerivedActivityid(fileToCorrect.toString(),new_activity_id,old_activity_id);
		 File fileToOpen = new File(fileToCorrect.toString());
		 
			if (fileToOpen.exists() && fileToOpen.isFile()) {
			    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 
			    try {
			        IDE.openEditorOnFileStore( page, fileStore );
			    } catch ( PartInitException e1 ) {
			        //Put your exception handler here if you wish to
			    }
			    
			}
//corriger la version du processus  de la base
		 correct_process_version(BPMNToolBehaviorProvider.id_v,   new_activity_id,  old_activity_id);}
	else
		if (BPMNToolBehaviorProvider.id_v.startsWith("VC"))
				{DeriveCollaboration();
			//	System.out.println("31-05-2015"+BPMNToolBehaviorProvider.editor.getBpmnDiagram().getId());}
String path2=select_version_collaboration_path(id_drv);
	File fileToCorrect = new File(path2);
			 CorrectDerivedActivityidC(fileToCorrect.toString(),new_activity_id,old_activity_id);
	
			   File ciblefile = new File(path2);
			 File fileToOpen = new File(ciblefile.toString());
				//	FileStore F;
					
					if (fileToOpen.exists() && fileToOpen.isFile()) {
					    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
					    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					 
					    try {
					        IDE.openEditorOnFileStore( page, fileStore );
					    } catch ( PartInitException e1 ) {
					        //Put your exception handler here if you wish to
					    }
					}
	//corriger la version du processus  de la base
			 }}
else
	
	if (BPMNToolBehaviorProvider.process_state.compareTo("Working")==0)
	{dervive_process_working=true;
	  try {
			ModelHandler handler = ModelHandler
					.getInstance(getDiagram());

			Task t =	 (Task) handler.findElement(old_activity_id);
	
	t.setId(new_activity_id);
	}
	 catch (IOException e) {
		Activator.logError(e);
	}
	}
/*try {
	BPMNToolBehaviorProvider.editor.getBpmnDiagram().getName();
	ModelHandler handler = ModelHandler
			.getInstance(BPMNToolBehaviorProvider.editor.getBpmnDiagram());

FlowElement f=	(FlowElement) handler.findElement(BPMNToolBehaviorProvider.id_va);
f.setId(new_activity_id);

} catch (IOException e) {
	Activator.logError(e);
}*/
//corriger le fichier xml .bpmn

ModelHandler modelHandler;
try {
	modelHandler = ModelHandlerLocator.getModelHandler(BPMNToolBehaviorProvider.editor.getDiagramTypeProvider()
			.getDiagram().eResource());
	TreeViewer viewer = null;
	ActivityViewContentProvider contentProvider =(ActivityViewContentProvider) ActivityVersionView.viewer.getContentProvider();
	
	contentProvider.updateModel(modelHandler);
//	ActivityVersionView.viewer.refresh(true);
	
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
            if (bo  instanceof Task && BPMNToolBehaviorProvider.state.compareTo("Stable")==0) {
                ret = true;
            }
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
	
    public void DeriveProcess()
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
 id_drv=s2;
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
    File sourcefile = new File(path);
    File ciblefile = new File(path2);
    Path source = Paths.get(sourcefile.toURI());
    Path cible = Paths.get(ciblefile.toURI());

    try {
		Files.copy(source, cible);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("message ex "+e.getMessage());
		e.printStackTrace();
	}
    CorrectProcessId(ciblefile.toString(),s2,name);
  
		/*if (cible!=null)
		{File fileToOpen = new File(ciblefile.toString());
		 
		if (fileToOpen.exists() && fileToOpen.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 
		    try {
		        IDE.openEditorOnFileStore( page, fileStore );
		    } catch ( PartInitException e1 ) {
		        //Put your exception handler here if you wish to
		    }
		    
		}}*/
    

    
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
	
	public static void CorrectDerivedActivityid(String path, String new_id, String old_id)
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
			
		List list= courant.getChildren();
		
		Iterator j = list.iterator();
		while(j.hasNext())
		{Element courant2 = (Element)j.next();
		if (courant2.getName().compareTo("task")==0 || courant2.getName().compareTo("subProcess")==0)
		{if (courant2.getAttribute("id").getValue().compareTo(old_id)==0)
			courant2.getAttribute("id").setValue(new_id);
			//com++;
		}
		if (courant2.getName().compareTo("sequenceFlow")==0)
		{if (courant2.getAttribute("sourceRef").getValue().compareTo(old_id)==0 )
			courant2.getAttribute("sourceRef").setValue(new_id);
		else
			if ( courant2.getAttribute("targetRef").getValue().compareTo(old_id)==0)
				courant2.getAttribute("targetRef").setValue(new_id);
			//com++;
		}
		
		List list2= courant2.getChildren();
		Iterator k = list2.iterator();
		while(k.hasNext())
		{Element courant3 = (Element)k.next();
		if (courant3.getName().compareTo("BPMNShape")==0)
		if (courant3.getAttribute("bpmnElement").getValue().compareTo(old_id)==0 )
			courant3.getAttribute("bpmnElement").setValue(new_id);
		
		}
		
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
	public static void CorrectDerivedActivityidC(String path, String new_id, String old_id)
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
			
		List list= courant.getChildren();
		
		Iterator j = list.iterator();
		while(j.hasNext())
		{Element courant2 = (Element)j.next();
		if (courant2.getName().compareTo("task")==0)
		{if (courant2.getAttribute("id").getValue().compareTo(old_id)==0)
			courant2.getAttribute("id").setValue(new_id);
			//com++;
		}
		if (courant2.getName().compareTo("sequenceFlow")==0)
		{if (courant2.getAttribute("sourceRef").getValue().compareTo(old_id)==0 )
			courant2.getAttribute("sourceRef").setValue(new_id);
		else
			if ( courant2.getAttribute("targetRef").getValue().compareTo(old_id)==0)
				courant2.getAttribute("targetRef").setValue(new_id);
			//com++;
		}
		if (courant2.getName().compareTo("messageFlow")==0)
		{if (courant2.getAttribute("sourceRef").getValue().compareTo(old_id)==0 )
			courant2.getAttribute("sourceRef").setValue(new_id);
		else
			if ( courant2.getAttribute("targetRef").getValue().compareTo(old_id)==0)
				courant2.getAttribute("targetRef").setValue(new_id);
			//com++;
		}
		
		
		List list2= courant2.getChildren();
		Iterator k = list2.iterator();
		while(k.hasNext())
		{Element courant3 = (Element)k.next();
		if (courant3.getName().compareTo("BPMNShape")==0)
		if (courant3.getAttribute("bpmnElement").getValue().compareTo(old_id)==0 )
			courant3.getAttribute("bpmnElement").setValue(new_id);
		
		}
		
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
	
	protected void DeriveVersionActivity()
	{
		String id_dr=	BPMNToolBehaviorProvider.id_va;
		String data=SelectActivityInformation(id_dr);
	String name=BPMNToolBehaviorProvider.name_activity;
	old_activity_id=id_dr;
		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		  String s2; int j;
		 new_activity_id= s2=   SelectLastVActivity(name);
		   i= SelectLastVactivity(name);
		  String next_id_s="";
		  j=s2.indexOf("-");
		  int f=i+1;
		  next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		  Date date = new Date();
		 // System.out.println(dateFormat.format(date));
		  //System.out.println("s"+s);
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity  let $i:=<version><id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from><id_vd>"+id_dr+"</id_vd>  </derived_from><state>Working</state> <data>"+data+"</data></version> where $a/name='"+name+"' return insert nodes  $i into $a/versions");
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
		     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 

		
		
		
	}
	 public String SelectLastVActivity(String name)
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
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber where $p/name='"+name+"' return $p/id_vs");
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
		public int SelectLastVactivity(String name)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber where $p/name='"+name+"' return $p/id_vn");
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
	
	protected void correct_process_version(String id_p, String  new_activity, String old_activity)
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
   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process for $v in $p/versions/version for $a in $v/activities/id_va where $v/id_v='"+id_p+"' and $a='"+old_activity+"' return  replace value of node $a with '"+new_activity+"'");
   System.out.println("for  $p in fn:doc('Processes.xml')/Processes/Process for $v in $p/versions/version for $a in $v/activities/id_va where $v/id_v='"+id_p+"' and $a='"+old_activity+"' return  replace value of node $a with '"+new_activity+"'");
 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
   
  		
   session.commit();  
		    } finally {  
		      session.rollback();  
		    } }
	public String SelectActivityInformation(String idva)
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
   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version for $d in $i/data/id_vda where $i/id_v='"+idva+"' return $d");
 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
   while(result.hasNext()) { 
   	s2=s2+result.next().toString();
    
	   }
  
  		
   session.commit();  
		    } finally {  
		      session.rollback();  
		    } 
 return s2;
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
	
	 public void DeriveCollaboration()
	    {String id_dr=	BPMNToolBehaviorProvider.id_v;
		String name=BPMNToolBehaviorProvider.name_process;
		//String activities=SelectVersionProcessActivities(id_dr);
		String s2;
		
				XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		 if (driver.isInitialized()==false)
		driver.init();  
	      
	    XhiveSessionIf session = driver.createSession("xqapi-test");  
	    session.connect("Administrator", "imen", "vbpmn");  
	    session.begin(); 
	    int j=0; String s="";
	 s2=   SelectLastVCollaboration1(name);
	 id_drv=s2;
	int i= SelectLastVCollaboration(name);
	String next_id_s="";
	j=s2.indexOf("-");
	int f=i+1;
	next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
	String path =select_version_collaboration_path(id_dr);
	String path2=path.replace(id_dr, s2);
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Date date = new Date();
	    try {  
	      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	      // (1)
	     
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Collaboration.xml')/Collaborations/Collaboration  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><processes></processes><path>"+path2+"</path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		     IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
		     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
	     session.commit();  
			    } finally {  
			      session.rollback();  
			    }
	    File sourcefile = new File(path);
	    File ciblefile = new File(path2);
	     Path source = Paths.get(sourcefile.toURI());
	    final Path cible = Paths.get(ciblefile.toURI());
	   /* try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(sourcefile);
	 
			try{
				java.io.FileOutputStream destinationFile = null;
	 
				try{
					destinationFile = new FileOutputStream(ciblefile);
	 
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
	 
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					//destinationFile.close();
				}
			} finally {
				//sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
		 // Erreur
		}

		//part = page.findEditor(editorInput);
		//part = page.openEditor(editorInput, BPMN2Editor.EDITOR_ID);
		
			//page.closeEditor(part, true);
		/*
		 IRunnableWithProgress content;
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					Path path = cible;
					//System.out.println("path"+path.toPortableString());
					URI uri = URI.createPlatformResourceURI(path.toString(), true);
					BPMN2DiagramCreator.createDiagram(uri, Bpmn2DiagramType.PROCESS, "http://org.eclipse.bpmn2.modeler.examples.customtask");
					//System.out.println("BPMN2DiagramCreator.b.getName()"+BPMN2DiagramCreator.b.toString());
					} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
					
				}
			}
		};
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		//	run(true, false, op);
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*
	    try {
			Files.copy(source, cible);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("message ex "+e.getMessage());
			e.printStackTrace();
		}*/
	    FileInputStream  lecture=null;
	  	BufferedInputStream tamponLecture = null;
	  	FileOutputStream copie=null;
	  	BufferedOutputStream  tamponCopie=null;
	    try
	    {
	    	  lecture = new FileInputStream(path);
	    	
	    	 tamponLecture = new BufferedInputStream(lecture);
	    	 InputStreamReader ipsr=new InputStreamReader(lecture);
	    	 BufferedReader flux=new BufferedReader(ipsr);
	    	 copie = new FileOutputStream(path2);
	    	  tamponCopie = new BufferedOutputStream(copie);
	         
	        while (true)
	        {//System.out.println("24-05-2015lll"+flux.readLine().contains("é"));
	            int valeurOctet = tamponLecture.read();
	             
	            if (valeurOctet == -1)
	                break;
	             
	            tamponCopie.write(valeurOctet);
	            
	        }
	    }
	    catch (IOException exception)
	    {
	        exception.printStackTrace();
	    }
	    finally
	    { 
	        try
	        {
	            tamponLecture.close();
	            lecture.close();
	            tamponCopie.flush();
	            tamponCopie.close();
	            copie.close();
	            
	        }
	        catch(IOException exception1)
	        {
	          exception1.printStackTrace();
	        }  
	    }
	    
	    CorrectCollaborationId(ciblefile.toString(),s2,name);
	  
		/*	if (cible!=null)
			{File fileToOpen = new File(ciblefile.toString());
		//	FileStore F;
			
			if (fileToOpen.exists() && fileToOpen.isFile()) {
			    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 
			    try {
			        IDE.openEditorOnFileStore( page, fileStore );
			    } catch ( PartInitException e1 ) {
			        //Put your exception handler here if you wish to
			    }
			}
			
		}*/
	    
	}
		public String select_version_collaboration_path(String s)
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
		      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Collaboration.xml')//Collaboration/versions/version where $a/id_v='"+s+"' return <id> {$a/path} </id>");
				
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
			
		
		
		public void CorrectCollaborationId(String path, String new_id, String new_name)
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
			catch(Exception e){System.out.println("e.n"+e.getMessage());}
			racine = document.getRootElement();
				
			
			List listFlow =racine.getChildren();
			// = element.getAttributes();
			
			//On crée un Iterator sur notre liste
			Iterator i = listFlow.iterator();
			while(i.hasNext())
			{
				Element courant = (Element)i.next();
				
			if (courant.getName().compareTo("collaboration")==0 )
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
		
		public String SelectLastVCollaboration1(String name)
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
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vs");
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
		public int SelectLastVCollaboration(String name)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vn");
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
}