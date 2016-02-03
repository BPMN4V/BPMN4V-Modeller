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
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.ui.wizards.WizardSource;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
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

public class DeriveVersionCollaboration extends AbstractCustomFeature {
public DeriveVersionCollaboration(IFeatureProvider fp) {
		
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
            if (bo  instanceof BPMNDiagram && BPMNToolBehaviorProvider.state.compareTo("Stable")==0) {
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
    public String SelectNameC(String id_v)
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
    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration where $o/versions/version/id_v='"+id_v+"' return $o/name");
  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
    while(result.hasNext()) {  
 	   s2=result.next().toString();
 	   
 	   }
    s2=s2.substring(6);
    j=s2.indexOf("<");
   		 s2=s2.substring(0,j);
   		
    session.commit();  
		    } finally {  
		      session.rollback();  
		    } 
    
	return s2;
		
		
	}
    
	
    public void Derive()
    {String id_dr=	getDiagram().getName();
	String name=SelectNameC(id_dr);
	System.out.println("03-01-2016"+id_dr+" "+name);
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
  
		if (cible!=null)
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
		
	}
		copiederived(s2, id_dr);
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
		else
			if (courant.getName().compareTo("BPMNDiagram")==0 )
			{
				
				courant.getAttribute("name").setValue(new_id);
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
	protected void copiederived(String id_vc_derivé, String id_vc_sourced)
	{
		ModelHandler handler;
		try {
			handler = ModelHandler
					.getInstance(getDiagram());
			Collaboration c =	 (Collaboration) handler.findElement(id_vc_sourced);
			List<Participant> lo=c.getParticipants();
			Iterator<Participant> i=lo.iterator();
			while (i.hasNext())
			{Participant f=i.next();
				Process p= f.getProcessRef();
				Iterator<FlowElement> j=p.getFlowElements().iterator();
				while (j.hasNext())
				{FlowElement l=j.next();
				if (l instanceof Task)
				System.out.println("03-01-2016"+l.getName());
				String s=selectActivitiesMs(l.getId(),id_vc_sourced);
				InsertActivitymessage(s,l.getId(),id_vc_derivé);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		
	}
	public void InsertActivitymessage( String nb, String id_va, String id_vp) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		int k = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();

		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			//System.out.println("06-01-2015"+"for  $a in fn:doc('Activities.xml')/Activities/Activity for $i in $a/versions/version  let $o:='<nb_msg_flow><id_vp>'"+id_vp+"'</id_vp><nb_m>'"+nb+"'</nb_m></nb_msg_flow>' where $i/id_v='"+id_va+"' return  insert nodes  $o into $i");


			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
					.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity for $i in $a/versions/version  let $o:=<nb_msg_flow><id_vp>"+id_vp+"</id_vp><nb_m>"+nb+"</nb_m></nb_msg_flow> where $i/id_v='"+id_va+"' return  insert nodes  $o into $i");
			
			
			//imen + "' return replace value of node $i/nb_m with $o");
			session.commit();
		} finally {
			session.rollback();
		}
	}
	protected String selectActivitiesMs (String ida, String idp)
	{String s="";
	XhiveDriverIf driver = XhiveDriverFactory
			.getDriver("xhive://localhost:1235");
	if (driver.isInitialized() == false)
		driver.init();

	XhiveSessionIf session = driver.createSession("xqapi-test");
	session.connect("Administrator", "imen", "vbpmn");
	session.begin();
	String s2 = "";
	try {
		XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
		// (1)
		int j = 0;
		IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
		// .executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"
				.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity "
						+ "for $i in $o/versions/version for $k in $i/nb_msg_flow where"
						+ " $i/id_v='"+ida+"'"
								+ " and  $k/id_vp='"+idp+"'"+
								 " return $k/nb_m");

		while (result.hasNext()) {
			s2 = result.next().toString();
			// j = s2.indexOf("<nb_m>");
			s2 = s2.substring(6);
			j = s2.indexOf("<");
			s2 = s2.substring(0, j);
		}

		session.commit();
	} finally {
		session.rollback();
	}
	
	return s2;

	
		
	}
}
	

