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
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.Bpmn2DiagramEditorInput;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2MultiPageEditor;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.internal.services.GraphitiInternal;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.GraphitiUIPlugin;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
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

public class BPMN2DiagramCreator {

	public static  BPMNDiagram b;
private static  Object bb=null;
public static BPMN2Editor otherEditor;
	public static Bpmn2DiagramEditorInput createDiagram(URI uri, Bpmn2DiagramType diagramType, String targetNamespace) throws CoreException {
		Bpmn2DiagramEditorInput o=null;
		if (diagramType.toString().compareTo("Process")==0)
		o= createDiagram(null, uri, diagramType, targetNamespace, null);
		else
			if (diagramType.toString().compareTo("Collaboration")==0)
				o= createDiagramC(null, uri, diagramType, targetNamespace, null);
	
		return o;
	}

	public static Bpmn2DiagramEditorInput createDiagram(IEditorInput oldInput, URI modelUri, Bpmn2DiagramType diagramType, String targetNamespace, BPMN2Editor diagramEditor) {

		// Should we create a new Graphiti Diamgra file or reuse the one
		// from an already open editor window?
		boolean createNew = true;
		URI diagramUri = null;
		BPMN2Editor otherEditor = BPMN2Editor.findOpenEditor(diagramEditor, oldInput);
		
		String modelName = modelUri.trimFragment().trimFileExtension().lastSegment();
		//System.out.println("modelName"+modelName);
		// We still need to create a Diagram object for this editor
		final Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true); //$NON-NLS-1$
		//final BPMNDiagram diagram = (BPMNDiagram) Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true); //$NON-NLS-1$
		
		//System.out.println("diagram.getDiagramTypeId()"+diagram);
		//System.out.println("diagram.eClass().getName()"+diagram.eClass().getName());
		//	((BPMNDiagram) diagram).setName("production");
		
//		diagram.setVerticalGridUnit(0);
//		diagram.setGridUnit(0);

		if (otherEditor!=null) {
			// reuse the temp Diagram File from other editor
			diagramUri = otherEditor.getDiagramUri();
			createNew = false;
		}
		else {
			// delete old temp file if necessary
			if (oldInput instanceof Bpmn2DiagramEditorInput) {
				URI oldUri = ((Bpmn2DiagramEditorInput)oldInput).getUri();
				final File oldTempFile = new File(oldUri.toFileString());
				if (oldTempFile!=null && oldTempFile.exists()) {
					// If two or more editor windows are open on the same file
					// when the workbench is first starting up, then deleting
					// the old temp file before all copies of the editor are
					// initialized can cause problems.
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								oldTempFile.delete();
							} catch (Exception e) {
							}
						}
					});
				}
			}
			String diagramName = FileService.createTempName(modelName);
			diagramUri = URI.createFileURI(diagramName);
			FileService.createEmfFileForDiagram(diagramUri, diagram, diagramEditor);
		}

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		
		//System.out.println("providerId"+providerId);
	//	System.out.println("diagram.tostring()"+diagram.toString());
		// No need to create a new one if old input is already a Bpmn2DiagramEditorInput,
		// just update it
		Bpmn2DiagramEditorInput newInput;
		if (oldInput instanceof Bpmn2DiagramEditorInput) {
			newInput = (Bpmn2DiagramEditorInput)oldInput;
			newInput.updateUri(diagramUri);
		}
		else if (createNew) {
			newInput = new Bpmn2DiagramEditorInput(modelUri, diagramUri, providerId);
		}
		else {
			newInput = (Bpmn2DiagramEditorInput) otherEditor.getEditorInput();
		}
		
		newInput.setInitialDiagramType(diagramType);
		newInput.setTargetNamespace(targetNamespace);
		//newInput.bpmnDiagram.setName("pro");
		/*b = null;
		bb=new Object();
		((BPMNDiagram)bb).setName("production"); 
		//b.setName("Production");
		newInput.setBpmnDiagram((BPMNDiagram) bb);
		System.out.println("new"+newInput.getBpmnDiagram().getName());*/
		if (diagramEditor==null) {
			openEditor(newInput);
			
		}
		//System.out.println("new"+newInput.getBpmnDiagram().getName());
		//newInput.bpmnDiagram.setName("pro");
		
		return newInput;
	}
	
	public static Bpmn2DiagramEditorInput createDiagramC(IEditorInput oldInput, URI modelUri, Bpmn2DiagramType diagramType, String targetNamespace, BPMN2Editor diagramEditor) {

		// Should we create a new Graphiti Diamgra file or reuse the one
		// from an already open editor window?
		boolean createNew = true;
		URI diagramUri = null;
		 otherEditor = BPMN2Editor.findOpenEditor(diagramEditor, oldInput);
		
		String modelName = modelUri.trimFragment().trimFileExtension().lastSegment();
	
		// We still need to create a Diagram object for this editor
		final Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", modelName, true); //$NON-NLS-1$
	/*	
		try {
			ModelHandler handler = ModelHandler.getInstance(diagram);
		
				
	org.eclipse.bpmn2.Process p=	
				handler.getOrCreateProcess(null);
	
			p.setName("iii");
			
			//srcAnchor.
		} catch (IOException e) {
			Activator.logError(e);
		} 
		*/
		
		
		
		if (otherEditor!=null) {
			// reuse the temp Diagram File from other editor
			diagramUri = otherEditor.getDiagramUri();
			createNew = false;
		}
		else {
			// delete old temp file if necessary
			if (oldInput instanceof Bpmn2DiagramEditorInput) {
				URI oldUri = ((Bpmn2DiagramEditorInput)oldInput).getUri();
				final File oldTempFile = new File(oldUri.toFileString());
				if (oldTempFile!=null && oldTempFile.exists()) {
					// If two or more editor windows are open on the same file
					// when the workbench is first starting up, then deleting
					// the old temp file before all copies of the editor are
					// initialized can cause problems.
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								oldTempFile.delete();
							} catch (Exception e) {
							}
						}
					});
				}
			}
			String diagramName = FileService.createTempName(modelName);
			diagramUri = URI.createFileURI(diagramName);
			FileService.createEmfFileForDiagram(diagramUri, diagram, diagramEditor);
		}

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		
		//System.out.println("providerId"+providerId);
	//	System.out.println("diagram.tostring()"+diagram.toString());
		// No need to create a new one if old input is already a Bpmn2DiagramEditorInput,
		// just update it
		Bpmn2DiagramEditorInput newInput;
		if (oldInput instanceof Bpmn2DiagramEditorInput) {
			newInput = (Bpmn2DiagramEditorInput)oldInput;
			newInput.updateUri(diagramUri);
		}
		else if (createNew) {
			newInput = new Bpmn2DiagramEditorInput(modelUri, diagramUri, providerId);
		}
		else {
			newInput = (Bpmn2DiagramEditorInput) otherEditor.getEditorInput();
		}
		
		newInput.setInitialDiagramType(diagramType);
		newInput.setTargetNamespace(targetNamespace);
	
		
		// Diagram diagrami = Graphiti.getPeCreateService().createDiagram("VP", "gg", 1, true);
	//	newInput.setBpmnDiagram(bpmnDiagram);
		
		if (diagramEditor==null) {
			openEditorC(newInput);
		//	otherEditor.closeEditor();
			
			// GraphitiUiInternal.getEmfService().getDiagramFromFile(file, resourceSet)
		}
	
		//System.out.println("newInput.getUriString()"+newInput.getModelUri().toString());
		return newInput;
	}

	
	public static IEditorPart openEditorC(final DiagramEditorInput editorInput) {
		final Object result[] = { new Object() };
	String path="";
	System.out.println("	editorInput.getUriString()"+	editorInput.getName());
	System.out.println("	editorInput.getUri().lastSegment()"+		editorInput.getUri().lastSegment());
	
	
	
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart part = null;
			
					part = page.findEditor(editorInput);
					
					if (part!=null) {
						page.activate(part);
					}
					else {
						part = page.openEditor(editorInput, BPMN2Editor.EDITOR_ID);
					
						page.closeEditor(part, true);
					String p=	select_version_collaboration_path(editorInput.getName());
					CorrectCollaborationId(p, BPMN2DiagramWizardPage2.process_version_number, BPMN2DiagramWizardPage2.collaboration_name,BPMN2DiagramWizardPage2.process1_idvp,BPMN2DiagramWizardPage2.process1_name, BPMN2DiagramWizardPage2.process2_idvp, BPMN2DiagramWizardPage2.process2_name);
					File fileToOpen = new File(p);
					 
					if (fileToOpen.exists() && fileToOpen.isFile()) {
					    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
					   
					 
					    try {
					        IDE.openEditorOnFileStore( page, fileStore );
					    } catch ( PartInitException e1 ) {
					        //Put your exception handler here if you wish to
					    }
					} else {
					    //Do something if the file does not exist
					}
					//part= page.openEditor(editorInput, BPMN2Editor.EDITOR_ID);
						//part.
						//String  path=select_version_collaboration_path(editorInput.getName());
						
					}
					result[0] = part;
					
					//System.out.println("result"+result[0].toString());
				} catch (PartInitException e) {
					String error = Messages.BPMN2DiagramCreator_Create_Error;
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
				
			}
		});
		return (IEditorPart)result[0];
	}
	public static IEditorPart openEditor(final DiagramEditorInput editorInput) {
		final Object result[] = { new Object() };
	String path="";
	System.out.println("	editorInput.getUriString()"+	editorInput.getName());
	System.out.println("	editorInput.getUri().lastSegment()"+		editorInput.getUri().lastSegment());
	
	
	
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IEditorPart part = null;
			
					part = page.findEditor(editorInput);
					
					if (part!=null) {
						page.activate(part);
					}
					else 
						{part = page.openEditor(editorInput, BPMN2Editor.EDITOR_ID);
					
						page.closeEditor(part, true);
						String p=	select_version_process_path(editorInput.getName());
						
						CorrectProcessId(p, BPMN2DiagramWizardPage2.process_version_number, BPMN2DiagramWizardPage2.process_name);
						File fileToOpen = new File(p);
						 
						if (fileToOpen.exists() && fileToOpen.isFile()) {
						    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
						   
						 
						    try {
						        IDE.openEditorOnFileStore( page, fileStore );
						    } catch ( PartInitException e1 ) {
						        //Put your exception handler here if you wish to
						    }
						} else {
						    //Do something if the file does not exist
						}
				
					result[0] = part;
						}
					//System.out.println("result"+result[0].toString());
				} catch (PartInitException e) {
					String error = Messages.BPMN2DiagramCreator_Create_Error;
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
				
			}
		});
		return (IEditorPart)result[0];
	}
	public static void CorrectProcessId(String path, String new_id, String new_name)
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
		catch(Exception e){ System.out.println("e0"+e.getMessage());}
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
	public static String select_version_collaboration_path(String s)
	{String dervivé="";
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
	public static void CorrectCollaborationId(String path, String new_idcollaboration, String new_namecollaboration, String new_idprocess1, String new_nameprocess1, String new_idprocess2, String new_nameprocess2)
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
				courant.getAttribute("id").setValue(new_idcollaboration);
				courant.getAttribute("name").setValue(new_namecollaboration);
				List l=courant.getChildren();
				Iterator j=l.iterator();
				while (j.hasNext())
				{
					Element courant2 = (Element)j.next();
					if (courant2.getAttribute("id").getValue().compareTo("Participant_1")==0)
					{courant2.getAttribute("processRef").setValue(new_idprocess1);
					courant2.getAttribute("name").setValue(new_nameprocess1);
					}
					else 
						if (courant2.getAttribute("id").getValue().compareTo("Participant_2")==0)
							{courant2.getAttribute("processRef").setValue(new_idprocess2);
							courant2.getAttribute("name").setValue(new_nameprocess2);}
				}
			}
			else
		if (courant.getName().compareTo("process")==0 )
		{
			System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("id"));
			System.out.println("courant2.getAttributeValue(name)"+courant.getAttributeValue("name"));
			if (courant.getAttribute("id").getValue().compareTo("Process_1")==0)
			{courant.getAttribute("id").setValue(new_idprocess1);
			courant.getAttribute("name").setValue(new_nameprocess1);
			courant.getAttribute("definitionalCollaborationRef").setValue(new_idcollaboration);
			}
			else
				if (courant.getAttribute("id").getValue().compareTo("Process_2")==0)
					{courant.getAttribute("id").setValue(new_idprocess2);
			//courant.getAttribute("name").setValue(new_name);
					courant.getAttribute("name").setValue(new_nameprocess2);
					courant.getAttribute("definitionalCollaborationRef").setValue(new_idcollaboration);}
			
		}
		else
			if (courant.getName().compareTo("BPMNDiagram")==0 )
			{
				
				
				courant.getAttribute("name").setValue(new_idcollaboration);
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
	public static String select_version_process_path(String s)
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
