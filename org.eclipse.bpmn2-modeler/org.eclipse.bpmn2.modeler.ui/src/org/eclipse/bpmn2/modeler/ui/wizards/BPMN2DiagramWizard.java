/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.wizards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.help.IHelpContexts;
import org.eclipse.bpmn2.modeler.ui.Bpmn2DiagramEditorInput;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.ResourceUtil;
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

public class BPMN2DiagramWizard extends Wizard implements INewWizard {
	private BPMN2DiagramWizardPage1 page1;
	private BPMN2DiagramWizardPage2 page2;
//	private BPMN2DiagramWizardPage3 page3;
	private ISelection selection;
	public static String type="derive";
	private Bpmn2DiagramEditorInput d;
	/**
	 * Constructor for BPMN2DiagramWizard.
	 */
	public BPMN2DiagramWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page2 to the wizard.
	 */

	@Override
	public void addPages() {
		page1 = new BPMN2DiagramWizardPage1(selection);
		addPage(page1);
		page2 = new BPMN2DiagramWizardPage2(selection);
		addPage(page2);
		//page3 = new BPMN2DiagramWizardPage3(selection);
		//addPage(page3);
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), IHelpContexts.New_File_Wizard);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
	 * wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		String s2="";
		d = null;
		if (page2.radio.getSelection()==true)
		{ if (page2.getDiagramType()!=null && page2.getDiagramType().toString().compareTo("Process")==0)
			{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i=0;
		 if (driver.isInitialized()==false)
		driver.init();  
	    
	  XhiveSessionIf session = driver.createSession("xqapi-test");  
	  session.connect("Administrator", "imen", "vbpmn");  
	  session.begin();  
	 
	  String f=page2.select_version_process_id(); int j=0;
	  j=i=Integer.parseInt(f);
	  i++;

		final String fileName = page2.getFileName();
	final IResource container = page2.getDiagramContainer();

	final String targetNamespace = page2.getTargetNamespace();
String path=container.getLocationURI().getRawPath()+"/"+page2.process_name+"/"+page2.process_version_number+".bpmn";
System.out.println("path"+path);
	  String [] tab= path.split("%20");
		String path2="";
		for (int ii=0; ii<tab.length; ii++)
			if (ii+1==tab.length)
				path2=path2+tab[ii];
			else
				path2=path2+tab[ii]+" ";
		System.out.println("path2"+path2);
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	   
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("let $i:=<Process><id>P"+j+"</id><name>"+page2.process_name+"</name><versions><version> <id_v>"+page2.process_version_number+"</id_v> <number>V1</number> <creator>Imen</creator> <creation_date>16/04/2014</creation_date> <derived_from> <id_vd>nil</id_vd>  </derived_from> <activities> </activities><path>"+path2+"</path> <state>Working</state> </version></versions></Process> return insert nodes  $i into fn:doc('Processes.xml')/Processes");
	  
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	 
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
		
		
			
			
			
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					IPath path = container.getFullPath().append(page2.process_name+"/"+fileName);
					//System.out.println("path"+path.toPortableString());
					URI uri = URI.createPlatformResourceURI(path.toString(), true);
					BPMN2DiagramCreator.createDiagram(uri, page1.getDiagramType(), targetNamespace);
					//System.out.println("BPMN2DiagramCreator.b.getName()"+BPMN2DiagramCreator.b.toString());
					} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
					
				}
			}
		};
		try {
			getContainer().run(true, false, op);
			//System.out.println("BPMN2DiagramCreator.b.getName()"+BPMN2DiagramCreator.b.toString());
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), Messages.BPMN2DiagramWizard_Error, realException.getMessage());
			return false;
		}
		XhiveDriverIf driver2 = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i2=0;
		 if (driver2.isInitialized()==false)
		driver2.init();  
	    
	  XhiveSessionIf session2 = driver2.createSession("xqapi-test");  
	  session2.connect("Administrator", "imen", "vbpmn");  
	  session2.begin();  
	 
	  String f2=page2.select_version_process_id(); int j2=0;
	  j2=i2=Integer.parseInt(f);
	  i2=i2+1;
	  i++;
	  try {  
	    XhiveLibraryIf rootLibrary = session2.getDatabase().getRoot();  
	    // (1)
	   
	   
	    IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes let $o:='"+i2+"' return replace value of node $p/id with $o");
	    IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("let $i:=  <versionnumber> <name>"+page2.process_name+"</name><id_vs>VP"+j2+"-2</id_vs> <id_vn>2</id_vn> </versionnumber> return insert nodes $i into doc('LastProcess.xml')/Processes");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	 
	   		
	    session2.commit();  
			    } finally {  
			      session2.rollback();  
			    } 
		
		
		
		}
		else
			if (page2.getDiagramType()!=null && page2.getDiagramType().toString().compareTo("Collaboration")==0)
			{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
				int i=0;
				File fileToOpen;
				if (driver.isInitialized()==false)
					driver.init();  
	    
					XhiveSessionIf session = driver.createSession("xqapi-test");  
					session.connect("Administrator", "imen", "vbpmn");  
					session.begin();  
	 
					String f=page2.select_version_collaboration_id(); int j=0;
					j=i=Integer.parseInt(f);
					i++;

					final String fileName = page2.getFileName();
					final IResource container = page2.getDiagramContainer();

	final String targetNamespace = page2.getTargetNamespace();
String path=container.getLocationURI().getRawPath()+"/"+page2.collaboration_name+"/"+page2.getFileName();
System.out.println("path"+path);
	  String [] tab= path.split("%20");
		String path2="";
		for (int ii=0; ii<tab.length; ii++)
			if (ii+1==tab.length)
				path2=path2+tab[ii];
			else
				path2=path2+tab[ii]+" ";
		System.out.println("path2"+path2);
		 fileToOpen = new File(path2);
	  try {  
	    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	    // (1)
	   
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("let $i:=<Collaboration><id>C"+j+"</id><name>"+page2.collaboration_name+"</name><versions><version> <id_v>"+page2.process_version_number+"</id_v> <number>V1</number> <creator>Imen</creator> <creation_date>16/04/2014</creation_date> <derived_from> <id_vd>nil</id_vd>  </derived_from> <path>"+path2+"</path> <state>Working</state> </version></versions></Collaboration> return insert nodes  $i into fn:doc('Collaboration.xml')/Collaborations");
	  
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	 
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    }
	String idvp1=  insertProcessCollaboration(page2.process1_name);
	String idvp2=  insertProcessCollaboration(page2.process2_name);
	page2.process1_idvp=idvp1;
	page2.process2_idvp=idvp2;
	  IRunnableWithProgress op = new IRunnableWithProgress() {
			

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					IPath path = container.getFullPath().append(page2.collaboration_name+"/"+fileName);
					//System.out.println("path"+path.toPortableString());
					URI uri = URI.createPlatformResourceURI(path.toString(), true);
					 d= BPMN2DiagramCreator.createDiagram(uri, page1.getDiagramType(), targetNamespace);
				
					 } catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					
					monitor.done();
					//System.out.println("21-05-2015"	+ d.bpmnDiagram.toString());
				
				}
			}
		};
		
		
		try {
			
			getContainer().run(true, false, op);
		
			//System.out.println("BPMN2DiagramCreator.b.getName()"+BPMN2DiagramCreator.b.toString());
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), Messages.BPMN2DiagramWizard_Error, realException.getMessage());
			return false;
		}
		
		

		//CorrectProcessId(path2,"VP5-1","imen");
		
		
		XhiveDriverIf driver2 = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		int i2=0;
		 if (driver2.isInitialized()==false)
		driver2.init();  
	    
	  XhiveSessionIf session2 = driver2.createSession("xqapi-test");  
	  session2.connect("Administrator", "imen", "vbpmn");  
	  session2.begin();  
	 
	  String f2=page2.select_version_collaboration_id(); int j2=0;
	  j2=i2=Integer.parseInt(f);
	  i2=i2+1;
	  i++;
	  try {  
	    XhiveLibraryIf rootLibrary = session2.getDatabase().getRoot();  
	    // (1)
	   
	   
	    IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes let $o:='"+i2+"' return replace value of node $p/id with $o");
	    IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("let $i:=  <versionnumber> <name>"+page2.collaboration_name+"</name><id_vs>VC"+j2+"-2</id_vs> <id_vn>2</id_vn> </versionnumber> return insert nodes $i into doc('LastCollaboration.xml')/Processes");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	 
	   		
	    session2.commit();  
			    } finally {  
			      session2.rollback();  
			    } 
		
		
		
		}
		}
		else
			if (page2.radio1.getSelection()==true) 
				
			{if (page2.getSelectedVersion()!=null && page2.getDiagramType()!=null && page2.getDiagramType().toString().compareTo("Process")==0)
				{if (page2.state.getText().compareTo("Stable")==0)
				{type ="derive";
			//	page2.select_process_name();
				
	String id_dr=	page2.getSelectedVersion();
		final String name=page2.getSelectedProcess();
		String activities=SelectVersionProcessActivities(id_dr);
		String Newligne=System.getProperty("line.separator"); 
		
		String str=Newligne+"<version> "+Newligne+ "<id_v>VP1-3</id_v>"+Newligne+ "<number>V3</number>"+Newligne+ "<creator>Imen</creator>"+Newligne+ "<creation_date>16/04/2014</creation_date>"+Newligne+ "<derived_from> <id_vd>"+id_dr+"</id_vd></derived_from>"+Newligne+ "<activities>"+Newligne+ "<activity></activity>"+Newligne+ "</activities>"	+Newligne+ "<path>E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/VP1-1.bpmn</path>"+Newligne+ "<state>Working</state>"+Newligne+ "</version>"+Newligne;
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
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	 Date date = new Date();
	 String path =select_version_process_path(id_dr);
	 String path2=path.replace(id_dr, s2);
	    try {  
	      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	      // (1)
	     
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities>"+activities+"</activities><path>"+path2+"</path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
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
			if (cible!=null)
			{File fileToOpen = new File(ciblefile.toString());
			 
			if (fileToOpen.exists() && fileToOpen.isFile()) {
			    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 
			    try {
			        IDE.openEditorOnFileStore( page, fileStore );
			    } catch ( PartInitException e1 ) {
			        //Put your exception handler here if you wish to
			    }
			} else {
			    //Do something if the file does not exist
			}}
		/*final String fileName = s2;
		final IResource container = page2.getDiagramContainer();
	
		final String targetNamespace = page2.getTargetNamespace();
		
		
		
		
					IPath path = container.getFullPath().append(name+"/"+fileName);
					//System.out.println("path"+path.toPortableString());
					URI uri = URI.createPlatformResourceURI(path.toString(), true);
					
					Bpmn2DiagramEditorInput	newInput = new Bpmn2DiagramEditorInput(uri, uri, "Process");	
					
					BPMN2DiagramCreator.openEditor(newInput);
				*/
				}
				
				else
				if (page2.state.getText().compareTo("Working")==0)
				
				{	type="derive";
				JOptionPane.showMessageDialog(null, "The version of process "+ page2.getSelectedVersion()+" is a Working version. Please choose a Stable one", "Derive working version", JOptionPane.ERROR_MESSAGE);
			return false;}
				}
				else 
					if (page2.getSelectedVersion()==null)
					{JOptionPane.showMessageDialog(null, "Select the version of process to derive", "Derive working version", JOptionPane.ERROR_MESSAGE);
					return false;}
					
			
			
			else
			if(	page2.getDiagramType()!=null && page2.getDiagramType().toString().compareTo("Collaboration")==0)
			{if (page2.getSelectedVersion()!=null)
				if (page2.state.getText().compareTo("Stable")==0)
				{type ="derive";
			//	page2.select_collaboration_name();
		
	String id_dr=	page2.getSelectedVersion();
		final String name=page2.getSelectedProcess();
	//	String activities=SelectVersionProcessActivities(id_dr);
		String Newligne=System.getProperty("line.separator"); 
		
		String str=Newligne+"<version> "+Newligne+ "<id_v>VP1-3</id_v>"+Newligne+ "<number>V3</number>"+Newligne+ "<creator>Imen</creator>"+Newligne+ "<creation_date>16/04/2014</creation_date>"+Newligne+ "<derived_from> <id_vd>"+id_dr+"</id_vd></derived_from>"+Newligne+ "<activities>"+Newligne+ "<activity></activity>"+Newligne+ "</activities>"	+Newligne+ "<path>E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/VP1-1.bpmn</path>"+Newligne+ "<state>Working</state>"+Newligne+ "</version>"+Newligne;
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
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	 Date date = new Date();
	 String path =select_version_collaboration_path(id_dr);
	 String path2=path.replace(id_dr, s2);
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
	    Path cible = Paths.get(ciblefile.toURI());
	
	    try {
			Files.copy(source, cible);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("message ex "+e.getMessage());
			e.printStackTrace();
		}
	    
	  CorrectCollaborationId(ciblefile.toString(),s2,name);
			if (cible!=null)
			{File fileToOpen = new File(ciblefile.toString());
			 
			if (fileToOpen.exists() && fileToOpen.isFile()) {
			    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 
			    try {
			        IDE.openEditorOnFileStore( page, fileStore );
			    } catch ( PartInitException e1 ) {
			        //Put your exception handler here if you wish to
			    }
			} else {
			    //Do something if the file does not exist
			}}}
				else
					if (page2.state.getText().compareTo("Working")==0)
					
					{type="derive";
					JOptionPane.showMessageDialog(null, "The version of collaboration "+ page2.getSelectedVersion()+" is a Working version. Please choose a Stable one", "Derive working version", JOptionPane.ERROR_MESSAGE);
					return false;}
			}
				else 
					{JOptionPane.showMessageDialog(null, "Select the version of collaboration to derive ", "Derive working version", JOptionPane.ERROR_MESSAGE);	
				return false;
				}
			
	}
				return true;
			
		
		
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
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
		catch(Exception e){System.out.println("e.n"+e.getMessage());}
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
			//System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("id"));
			//System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("name"));
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
			
		private String insertProcessCollaboration(String processname)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		 if (driver.isInitialized()==false)
		driver.init();  
	      String idvp1="";
	    XhiveSessionIf session = driver.createSession("xqapi-test");  
	    session.connect("Administrator", "imen", "vbpmn");  
	    session.begin();  
	    String f=page2.select_version_process_id(); int j=0;int i=0;
		  j=i=Integer.parseInt(f);
		  i++;
idvp1="VP"+f+"-1";
		  try {  
			    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
			    // (1)
			   
			    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("let $i:=<Process><id>P"+j+"</id><name>"+processname+"</name><versions><version> <id_v>"+idvp1+"</id_v> <number>V1</number> <creator>Imen</creator> <creation_date>16/04/2014</creation_date> <derived_from> <id_vd>nil</id_vd>  </derived_from> <activities> </activities><path></path> <state>Working</state> </version></versions></Process> return insert nodes  $i into fn:doc('Processes.xml')/Processes");
			  
			  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
			 
			   		
			    session.commit();  
					    } finally {  
					      session.rollback();  
					    }
		  XhiveDriverIf driver2 = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			 if (driver2.isInitialized()==false)
			driver2.init();  
		  XhiveSessionIf session2 = driver2.createSession("xqapi-test");  
		  session2.connect("Administrator", "imen", "vbpmn");  
		  session2.begin();  
		 
		   int j2=0;int i2=0;
		  j2=i2=Integer.parseInt(f);
		  i2=i2+1;
		
		  try {  
		    XhiveLibraryIf rootLibrary = session2.getDatabase().getRoot();  
		    // (1)
		   
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes let $o:='"+i2+"' return replace value of node $p/id with $o");
		    IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("let $i:=  <versionnumber> <name>"+processname+"</name><id_vs>VP"+j2+"-2</id_vs> <id_vn>2</id_vn> </versionnumber> return insert nodes $i into doc('LastProcess.xml')/Processes");
		  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		 
		   		
		    session2.commit();  
				    } finally {  
				      session2.rollback();  
				    } 
		return idvp1;
		}
	
}