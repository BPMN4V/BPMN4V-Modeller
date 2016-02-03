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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateActivityDialog;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import com.xhive.XhiveDriverFactory;  
import com.xhive.core.interfaces.XhiveDriverIf;  
import com.xhive.core.interfaces.XhiveSessionIf;  
import com.xhive.dom.interfaces.XhiveLibraryIf;  
import com.xhive.query.interfaces.XhiveXQueryValueIf;  
import com.xhive.util.interfaces.IterableIterator;

public class BPMN2DiagramWizardPage2 extends WizardPage {
	private Text containerText;

	private Text fileText;
	private Text targetNamespaceText;
    public Text state;
	private ISelection selection;

	private IResource diagramContainer;
	private  Database database;
	private Collection col;
	private ResourceSet result;
	private XPathQueryService service=null;
	private ResourceSet result1;
	private XPathQueryService service1=null;
	private Connection con;
	private List list;
	private Label label1, label2,label3,label4,label5, label6, label7;
	private Tree VH=null;
	private TreeItem item=null;
	private TreeItem subitem=null;
	public Button radio;
	public Button radio1;
	private String dervivé="";
	private Button button, button1;
	private URI modelUri;
	public static String ss, process_name="", process_version_number="", collaboration_name, process1_name, process2_name, process1_idvp, process2_idvp;
	
	
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public BPMN2DiagramWizardPage2(ISelection selection) {
		super("wizardPage2"); //$NON-NLS-1$
		setTitle(Messages.BPMN2DiagramWizardPage2_Title);
		setDescription(Messages.BPMN2DiagramWizardPage2_Description);
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		radio= new Button(container, SWT.RADIO);
		radio.setText("Create New ")	;
		radio.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false, 3, 1));
		 radio1= new Button(container, SWT.RADIO);
		radio1.setText("Derive")	;	
		radio1.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false, 3, 1));
		
		 label1 = new Label(container, SWT.NULL);
		label1.setText(Messages.BPMN2DiagramWizardPage2_Location_Label);
	label1.setEnabled(false);
		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.setEditable(false);
		containerText.setEnabled(false);
		containerText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			//	targetNamespaceText.setText(""); //$NON-NLS-1$
				//dialogChanged(true);
			}
		});

		 button = new Button(container, SWT.PUSH);
		button.setEnabled(false);
		button.setText(Messages.BPMN2DiagramWizardPage2_Browse_Button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		 label2 = new Label(container, SWT.NULL);
		
		label2.setText(Messages.BPMN2DiagramWizardPage2_File_Name_Label);
label2.setEnabled(false);
		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		fileText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		fileText.setEnabled(false);
		 
		fileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			
				dialogChanged(false);
				
			}
		});

		 label3 = new Label(container, SWT.NULL);
		label3.setText(Messages.BPMN2DiagramWizardPage2_TargetNamespace_Label);
label3.setEnabled(false);
		targetNamespaceText = new Text(container, SWT.BORDER | SWT.SINGLE);
		targetNamespaceText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		targetNamespaceText.setEnabled(false);
		targetNamespaceText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged(false);
			}
		});
		
		label4 = new Label(container, SWT.NULL);
		label4.setText("Process Name");
		label4.setEnabled(false);
		 list = new List(container, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
		 list.setEnabled(false);
		 select_process_name();
	
		 label5 = new Label(container, SWT.NULL);
		 label5.setText("Version hierarchy");
		 label5.setEnabled(false);
		list.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		 VH=new Tree(container, SWT.NO_SCROLL|SWT.MULTI);
		 
		  VH.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		  VH.setEnabled(false);
		  label6 = new Label(container, SWT.NULL);
			 label6.setText("Version State");
			 label6.setVisible(false);
			 state = new Text(container, SWT.BORDER | SWT.SINGLE);
				GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
				state.setLayoutData(gd1);
				state.setEditable(false);
				state.setEnabled(false);
				state.setVisible(false);
		    list.addSelectionListener(new SelectionAdapter() {
		    	
			public void widgetSelected(SelectionEvent e) {
				VH.removeAll();
			//	select_process_version(list.getItem(list.getSelectionIndex()));
				if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )
		select_process_version(list.getItem(list.getSelectionIndex()));
				else
					if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
		select_collaboration_version(list.getItem(list.getSelectionIndex()));
			}});
		    VH.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					label6.setVisible(true);
			
					
					TreeItem[] items=VH.getSelection();
					String path="";
					for(int i=0;i<items.length;i++)
					{
						
				state.setVisible(true);
				state.setText("Stable");
				if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )
					state.setText(select_version_process_state(items[i].getText()));
				else
					if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
						state.setText(select_version_collaboration_state(items[i].getText()));
				if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )		
				path=	select_version_process_path(items[i].getText());
				if  (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
					path=	select_version_collaboration_path(items[i].getText());
					
					}
					if (path!=null)
					{File fileToOpen = new File(path);
					 
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
					
				}});
		 //   label7.setText("imen"+state);
		    radio.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
				radio1.setSelection(false);
				radio.setSelection(true);
				label1.setEnabled(true);
				label2.setEnabled(true);
				label3.setEnabled(true);
				button.setEnabled(true);
			//	containerText.setEnabled(true);
				//fileText.setEnabled(true);
				//targetNamespaceText.setEnabled(true);
				label4.setEnabled(false);
				label5.setEnabled(false);
				list.setEnabled(false);
				VH.setVisible(true);
				VH.setEnabled(false);
				label6.setVisible(false);
				state.setVisible(false);
				fileText.setText("");
				
				
				if (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )
					{process_version_number= "VP"+select_version_process_id()+"-1";
					 process_name=JOptionPane.showInputDialog(null, "Process Name", "New Process", JOptionPane.QUESTION_MESSAGE);
					}
				
				else
					if (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
					{ 
							NewCollaborationInterface b= new NewCollaborationInterface(getShell());
							b.open();
							collaboration_name=b.collaboration_name;
							process1_name=b.process1_name;
							process2_name=b.process2_name;
						process_version_number= "VC"+select_version_collaboration_id()+"-1";}
				//	process_version_number="VP"+select_version_process_id()+"-1";
					//fileText.setText(process_version_number+".bpmn");
				fileText.setText(getFileName());
				//containerText.setText(containerText.getText()+"/"+process_name);
				//System.out.println("Text"+fileText.getText()+" "+ containerText.getText());
				//File dir = new File("E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/"+process_name);
				//dir.mkdir();
				/*  try {
					FileInputStream fileStream = new FileInputStream(
						         "E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/"+process_name);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				}});
				
			radio1.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					if (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )
						select_process_name();
					else
						if (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
							select_collaboration_name();
				radio.setSelection(false);
				radio1.setSelection(true);
				label1.setEnabled(false);
				label2.setEnabled(false);
				label3.setEnabled(false);
				button.setEnabled(false);
				containerText.setEnabled(false);
				fileText.setEnabled(false);
				targetNamespaceText.setEnabled(false);
				label4.setEnabled(true);
				label5.setEnabled(true);
				list.setEnabled(true);
				VH.setEnabled(true);
				label6.setVisible(true);
			//state.setVisible(true);
				}});
		
		
		updatePageDescription();
		updateFilename();
		dialogChanged(true);
		setControl(container);
	}
	
	
	public Bpmn2DiagramType getDiagramType() {
		BPMN2DiagramWizardPage1 page1 = (BPMN2DiagramWizardPage1)getWizard().getPage("wizardPage1"); //$NON-NLS-1$
		return page1.getDiagramType();
	}
		
	/**
	 * Tests if the current workbench selection is a suitable diagramContainer to use.
	 */

	private void updatePageDescription() {
		BPMN2DiagramWizardPage1 page1 = (BPMN2DiagramWizardPage1)getWizard().getPage("wizardPage1"); //$NON-NLS-1$
		String descriptionType = Messages.BPMN2DiagramWizardPage2_Error_Unknown_Type;
		switch (page1.getDiagramType()) {
		case PROCESS:
			descriptionType = Messages.BPMN2DiagramWizardPage2_Process_Diagram;
			break;
		case COLLABORATION:
			descriptionType = Messages.BPMN2DiagramWizardPage2_Collaboration_Diagram;
			break;
		case CHOREOGRAPHY:
			descriptionType = Messages.BPMN2DiagramWizardPage2_Choreography_Diagram;
			break;
		default:
			break;
		}
		setDescription(Messages.BPMN2DiagramWizardPage2_Filename_Prompt+descriptionType);
	}
	
	private void updateFilename() {
		BPMN2DiagramWizardPage1 page1 = (BPMN2DiagramWizardPage1)getWizard().getPage("wizardPage1"); //$NON-NLS-1$
		String fileType = "unknown"; //$NON-NLS-1$
		String filename = fileType+Messages.BPMN2DiagramWizardPage2_BPMN_Extension;
		switch (page1.getDiagramType()) {
		case PROCESS:
			fileType = "process"; //$NON-NLS-1$
			break;
		case COLLABORATION:
			fileType = "collaboration"; //$NON-NLS-1$
			break;
		case CHOREOGRAPHY:
			fileType = "choreography"; //$NON-NLS-1$
			break;
		default:
			return;
		}
		
		IContainer container = getFileContainer();
		if (container!=null) {
			String text = container.getFullPath().toString();
			if (text!=null && !text.equals(getContainerName()))
				containerText.setText(text);
			for (int i=1; ; ++i) {
				filename = fileType+"_" + i + Messages.BPMN2DiagramWizardPage2_BPMN_Extension; //$NON-NLS-1$
				IResource file = container.findMember(filename);
				if (file==null) {
					break;
				}
			}
		}

		String oldFileText = fileText.getText();
		if (filename!=null && !filename.equals(oldFileText))
			fileText.setText(filename);
	}

	private IContainer getFileContainer() {
		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
			
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() == 1) {
				Object obj = ssel.getFirstElement();
				if (obj instanceof IAdaptable) {
					Object res = ((IAdaptable)obj).getAdapter(IResource.class);
					if (res!=null)
						obj = res;
				}
				if (obj instanceof Path) {
					obj = ResourcesPlugin.getWorkspace().getRoot().findMember((Path)obj);
				}
				if (obj instanceof IResource) {
					if (obj instanceof IContainer) {
						return (IContainer) obj;
					} else {
						return ((IResource) obj).getParent();
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			updatePageDescription();
			updateFilename();
		}
		super.setVisible(visible);
	}

	/**
	 * Uses the standard diagramContainer selection dialog to choose the new value for the diagramContainer field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace()
				.getRoot(), false, Messages.BPMN2DiagramWizardPage2_Select_Folder_Title);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				selection = new TreeSelection(new TreePath(result));
				containerText.setText(((Path) result[0]).toString()+"/"+process_name);
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged(boolean initialize) {
		boolean complete = false;
		if (validateContainer()) {
			diagramContainer = getFileContainer();
		
			if (initialize) {
				
				TargetRuntime rt = Bpmn2Preferences.getInstance(diagramContainer.getProject()).getRuntime();
				String targetNamespace = rt.getRuntimeExtension().getTargetNamespace(getDiagramType());
				if (targetNamespace==null)
					targetNamespace = ""; //$NON-NLS-1$
				
				if (rt!=TargetRuntime.getDefaultRuntime() && !targetNamespace.isEmpty()) {
					// Target Runtime will provide its own target namespace
					if (!targetNamespaceText.getText().equals(targetNamespace)) {
						targetNamespaceText.setText(targetNamespace);
						updateFilename();
					}
				}
				else {
					// The default "None" Target Runtime's target namespace may be edited by user.
					String text = targetNamespaceText.getText();
					if (text==null || text.isEmpty()) {
						targetNamespaceText.setText(targetNamespace);
						updateFilename();
					}
				}

			}
			if (validateFileName() && validateTargetNamespace()) {
				updateStatus(null);
				complete = true;
			}
		}
		setPageComplete(complete);
	}

	private boolean validateContainer() {
		IContainer container = getFileContainer();
		if (container==null) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_No_Container);
			return false;
		}
		if ((container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_No_Folder);
			return false;
		}
		if (!container.isAccessible()) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_Container_Readonly);
			return false;
		}
		return true;
	}
	
	private boolean validateFileName() {
		if (!validateContainer())
			return false;
		
		IContainer container = getFileContainer();
		String fileName = getFileName();
		if (fileName.length() == 0) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_No_Filename);
			return false;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_Filename_Invalid);
			return false;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("bpmn") == false && ext.equalsIgnoreCase("bpmn2") == false) { //$NON-NLS-1$ //$NON-NLS-2$
				setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_Extension_Invalid);
				return false;
			}
		}
		else {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_No_Extension);
			return false;
		}
		IResource file = container.findMember(fileName);
		if (file!=null) {
			setErrorMessage(NLS.bind(Messages.BPMN2DiagramWizardPage2_Error_Duplicate_File,fileName));
			return false;
		}
		return true;
	}

	private boolean validateTargetNamespace() {
		String targetNamespace = targetNamespaceText.getText();
		if (targetNamespace==null || targetNamespace.isEmpty()) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_No_TargetNamespace);
			return false;
		}
		URI uri = URI.createURI(targetNamespace);
		if (!(uri.hasAuthority() && uri.scheme()!=null)) {
			setErrorMessage(Messages.BPMN2DiagramWizardPage2_Error_Invalid_TargetNamespace);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isPageComplete() {
		return validateContainer() &&
				validateFileName() &&
				validateTargetNamespace();
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		//return fileText.getText();
		String name="p";
		//System.out.println("allo"+getDiagramType().toString());
		if (getDiagramType()!=null && getDiagramType().toString().compareTo("Process")==0 )
		name= "VP"+select_version_process_id()+"-1.bpmn";
		else
			if (getDiagramType()!=null && getDiagramType().toString().compareTo("Collaboration")==0 )
		name= "VC"+select_version_collaboration_id()+"-1.bpmn";
		return name;
	}

	public IResource getDiagramContainer() {
		return diagramContainer;
		
	}

	public String getTargetNamespace() {
		return targetNamespaceText.getText();
	}
	public void select_process_name()
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
    if (driver.isInitialized()==false)
	driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin();  
  
    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1)
      int j=0;
      IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary.executeXQuery("for $i in doc('Processes.xml')//Process return $i/name"); 
      list.removeAll();
      while (result.hasNext()) {  
    	String s1=result.next().toString();
    	 
				 
				 s1= s1.substring(6);
				 j=s1.indexOf("/name");
			s1=s1.substring(0,j-1);
    	  
    	  list.add(s1);
      
      }  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
  		
	
		
}
	
	public void select_collaboration_name()
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
    if (driver.isInitialized()==false)
	driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin();  
  
    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1)
      int j=0;
      IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary.executeXQuery("for $i in doc('Collaboration.xml')//Collaboration return $i/name"); 
    list.removeAll();
      while (result.hasNext()) {  
    	String s1=result.next().toString();
    	 
				 
				 s1= s1.substring(6);
				 j=s1.indexOf("/name");
			s1=s1.substring(0,j-1);
    	  
    	  list.add(s1);
      
      }  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
  		
	
		
}
	public void select_process_version(String s)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	 if (driver.isInitialized()==false)
	driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin();  
  int l=0;String str0[]= new String[10];int	j=0;
	
    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1)  
      IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary.executeXQuery("for $a in doc('Processes.xml')//Process where $a/name='"+s+"' return <id_v> {$a/versions/version/id_v} </id_v>"); 
      while (result.hasNext()) {  
    	  String s1=result.next().toString();
			
			while (s1.indexOf("VP")!=-1)
			{
				 j=s1.indexOf("VP");
				 s1= s1.substring(j);
				
				j=s1.indexOf("<");
				str0[l]=s1.substring(0, j);
				
				l++;
				s1= s1.substring(j);
				
				
			}}
			int k=0;
			while(str0[k]!=null)
			{
			result = rootLibrary.executeXQuery("for $a in doc('Processes.xml')//Process/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>"); 
			 
	
		
		
		while(result.hasNext()) {  
	    	  String s1=result.next().toString();
			
		
		
			while (s1.indexOf("<id_vd>")!=-1)
			{
				 j=s1.indexOf("<id_vd>");
				 
				 s1= s1.substring(j+7);
				
				j=s1.indexOf("<");
				dervivé=s1.substring(0, j);
				l++;
				s1= s1.substring(j);
				
			}
		
			if (dervivé.compareTo("nil")==0)
				{
				
				
			item = new TreeItem(VH, SWT.NONE);
			
			item.setText(str0[k]);//+" ("+s2.toString()+")");
				}
			else
				
			createchild2(VH.getItems(), dervivé,str0[k] );
			
		
		}
		k++;
		}
    	  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		
							 
	}
	public void createchild(Tree t, String derf, String der )
	{ TreeItem [] items;
		items=t.getItems();
		
		boolean trouve=false;
		int r=0;
		for (int i=0; i<items.length; i++)
			
		while (trouve==false && r<items.length)
		{ 
		
		
		if (items[r].getText().compareTo(derf)==0)
			{
			subitem=  new TreeItem(items[r], SWT.NONE);
				subitem.setText(der);
				trouve=true;}
		r++;
		}
		
     
	}
	public void createchild2(TreeItem [] items, String derf, String der)
	{
		if (items.length==0)
			return;
		for(int i=0;i<items.length;i++)
		{
			if( items[i].getText().compareTo(derf)==0)
			{subitem=  new TreeItem(items[i], SWT.NONE);
			subitem.setText(der);
			return;
				
			}
		createchild2(items[i].getItems(),derf,der);
		}
		
	}
	public void select_collaboration_version(String s)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	 if (driver.isInitialized()==false)
	driver.init();  
      
    XhiveSessionIf session = driver.createSession("xqapi-test");  
    session.connect("Administrator", "imen", "vbpmn");  
    session.begin();  
  int l=0;String str0[]= new String[10];int	j=0;
	
    try {  
      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
      // (1)  
      IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary.executeXQuery("for $a in doc('Collaboration.xml')//Collaboration where $a/name='"+s+"' return <id_v> {$a/versions/version/id_v} </id_v>"); 
      while (result.hasNext()) {  
    	  String s1=result.next().toString();
			
			while (s1.indexOf("VC")!=-1)
			{
				 j=s1.indexOf("VC");
				 s1= s1.substring(j);
				
				j=s1.indexOf("<");
				str0[l]=s1.substring(0, j);
				
				l++;
				s1= s1.substring(j);
				
				
			}}
			int k=0;
			while(str0[k]!=null)
			{
			result = rootLibrary.executeXQuery("for $a in doc('Collaboration.xml')//Collaboration/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>"); 
			 
	
		
		
		while(result.hasNext()) {  
	    	  String s1=result.next().toString();
			
		
		
			while (s1.indexOf("<id_vd>")!=-1)
			{
				 j=s1.indexOf("<id_vd>");
				 
				 s1= s1.substring(j+7);
				
				j=s1.indexOf("<");
				dervivé=s1.substring(0, j);
				l++;
				s1= s1.substring(j);
				
			}
		
			if (dervivé.compareTo("nil")==0)
				{
				
				
			item = new TreeItem(VH, SWT.NONE);
			
			item.setText(str0[k]);//+" ("+s2.toString()+")");
				}
			else
				
			createchild2(VH.getItems(), dervivé,str0[k] );
			
		
		}
		k++;
		}
    	  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		
							 
	}
	public String select_version_process_path(String s)
	{
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
		
		
		
		
		
		/*String driver = "org.exist.xmldb.DatabaseImpl";
	try {
		
		Class cl = Class.forName(driver);
		database = (Database)cl.newInstance();
		DatabaseManager.registerDatabase(database);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("m1"+e.getMessage());
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (XMLDBException e) {
		// TODO Auto-generated catch block
		System.out.println("me"+e.getMessage());
		e.printStackTrace();
	}
	
		try {
			 col = DatabaseManager.getCollection(
					"xmldb:exist://localhost:8080/exist/xmlrpc/db/vbpmn" );
			 service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
				service.setProperty("indent", "yes");
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("m5"+e.getMessage());
		}
		
						 try {
			result = service.query("for $a in doc('Processes.xml')//Process/versions/version where $a/id_v='"+s+"' return <id> {$a/path} </id>");
			ResourceIterator i;
			i = result.getIterator();
			while(i.hasMoreResources()) {
			Resource r = i.nextResource();
			int j=0;
			
			String s1=r.getContent().toString();
			
				 j=s1.indexOf("<path>");
				
				 s1= s1.substring(j+6);
				 System.out.println("s1"+s1);
				j=s1.indexOf("<");
				dervivé=s1.substring(0, j);
			
				
				
			
		
			System.out.println("process path "+dervivé);
			
			}
						 } catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("m7"+e.getMessage());
		}
						 try {
								col.close();
							} catch (XMLDBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
						 return dervivé;
						 
		
	}
	public String select_version_process_state(String id)
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
      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+id+"' return $i/state");
		
			while(result.hasNext()) {  
		    	   s2=result.next().toString();
		    	  j=s2.indexOf("<state>");
		    	  s2=s2.substring(j+7);
		    	  j=s2.indexOf("</state>");
		    	  s2=s2.substring(0, j);
		    	 }
        
      
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		return s2;
		
	}
	public String select_version_collaboration_state(String id)
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
      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id+"' return $i/state");
		
			while(result.hasNext()) {  
		    	   s2=result.next().toString();
		    	  j=s2.indexOf("<state>");
		    	  s2=s2.substring(j+7);
		    	  j=s2.indexOf("</state>");
		    	  s2=s2.substring(0, j);
		    	 }
        
      
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		return s2;
		
	}
	public String getSelectedVersion()
	{TreeItem[] items=VH.getSelection();
	if (items.length!=0)
	return items[0].getText();
	else return null;}
	
	public String getSelectedProcess()
	{
		return list.getItem(list.getSelectionIndex());
		
	}
	public String select_version_process_id()
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
      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes return $p/id");
		
			while(result.hasNext()) {  
		    	   s2=result.next().toString();
		    	   j=s2.indexOf("<id>");
			    	  s2=s2.substring(j+4);
			    	  j=s2.indexOf("</id>");
			    	  s2=s2.substring(0, j);
		    	 }
        
      
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		return s2;
		
	}
	
	public String select_version_collaboration_id()
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
      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastCollaboration.xml')/Processes return $p/id");
		
			while(result.hasNext()) {  
		    	   s2=result.next().toString();
		    	   j=s2.indexOf("<id>");
			    	  s2=s2.substring(j+4);
			    	  j=s2.indexOf("</id>");
			    	  s2=s2.substring(0, j);
		    	 }
        
      
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		return s2;
		
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
	
	
}