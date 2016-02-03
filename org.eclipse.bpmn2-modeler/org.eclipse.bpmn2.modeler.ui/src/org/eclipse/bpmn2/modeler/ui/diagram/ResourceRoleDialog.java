package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.Messages;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ResourceRoleListComposite;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class ResourceRoleDialog extends Dialog {

	protected ToolBarManager tableToolBarManager;
	protected ToolBar toolbar;
	protected ToolBarManager detailToolBarManager;
	protected Action addAction, removeAction, deriveAction;
	public String taskname;
	public String taskversionid;
	private  Database database;
	private Collection col;
	private ResourceSet result;
	private XPathQueryService service=null;
	private ResourceSet result1;
	private XPathQueryService service1=null;
	private Connection con;
	private  String []resourcename = new String [10];
	private  String []resourceversionid = new String [10];
	private  String []resourceversionnumber = new String [10];
	protected Table table;
	private Boolean changedone=false;
	protected ResourceRoleDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		
		layout.numColumns = 3;
		
	layout.verticalSpacing = 9;
	composite.setLayout(layout);
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	   
	    // Lets make a layout for the first section of the screen
	    
	    // Creating the Screen
	    Section section = toolkit.createSection(composite, ExpandableComposite.TWISTIE |
				ExpandableComposite.COMPACT |
				ExpandableComposite.TITLE_BAR |Section.DESCRIPTION
	        | Section.TITLE_BAR);
	    section.setText("Resource Role versions"); //$NON-NLS-1$
	    section.setDescription("Task Resource Role versions");
	    section.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
	  
	    tableToolBarManager = new ToolBarManager(SWT.FLAT);
	    ToolBar toolbar = tableToolBarManager.createControl(section);
	    
		
	   
	    ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/add.png"); 
	    addAction = new Action("Add", id) {
	    	 public void run() {
	    	        System.out.println("tok");                                                     
	    	      }
	    };
			
//addAction.setText("+");
	    addAction.setId("add"); //$NON-NLS-1$
		tableToolBarManager.add(addAction);
		id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/delete.png"); //$NON-NLS-1$
	
	removeAction = new Action("Delete", id) {
		@Override
		public void run() {
		changedone=true;
		table.remove(table.getSelectionIndex());
		}
	};
	removeAction.setId("Delete"); //$NON-NLS-1$
	tableToolBarManager.add(removeAction);
/*id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/import.png"); //$NON-NLS-1$
	
	deriveAction = new Action("Derive", id) {
		@Override
		public void run() {
			
			
		}
	};
	deriveAction.setId("derive"); //$NON-NLS-1$
	tableToolBarManager.add(deriveAction);*/
		
		tableToolBarManager.update(true);
		section.setTextClient(toolbar);
		final Composite tableComposite = toolkit.createComposite(section, SWT.NONE);
		section.setClient(tableComposite);
		tableComposite.setLayout(new GridLayout(1, false));
		
		
		
		
		 table = new Table(composite, SWT.BORDER );
			    
			   

			  /*  for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			      TableColumn column = new TableColumn(table, SWT.NULL);
			      column.setText(titles[loopIndex]);
			    }*/
			    TableColumn column = new TableColumn(table, SWT.CENTER);
			    column.setText("ResourceRole Name");
			    TableColumn column2 = new TableColumn(table, SWT.CENTER);
			    column2.setText("ResourceRole Version ID");
			    TableColumn column3 = new TableColumn(table, SWT.CENTER);
			    column3.setText("ResourceRole Version number");
			    table.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			   
			    select_role(taskname,taskversionid);
			    for (int loopIndex = 0; loopIndex < resourcename.length; loopIndex++) {
			        TableItem item = new TableItem(table, SWT.NONE);
			       System.out.println("str[loopIndex]"+resourcename[loopIndex]);
			       if (resourcename[loopIndex]!=null)
			       { item.setText(0, resourcename[loopIndex] );
			       item.setText(1, resourceversionid[loopIndex] );
			        item.setText(2, resourceversionnumber[loopIndex] );}
			        
			      }
			    table.getColumn(0).pack();
			    table.getColumn(1).pack();
			    table.getColumn(2).pack();
			    table.setHeaderVisible(true);
			    table.addSelectionListener(new SelectionAdapter() {
					
					public void widgetSelected(SelectionEvent e) {
						Shell shell = new Shell();
						ResourceRoleVersionHierarchyDialog v=new ResourceRoleVersionHierarchyDialog(shell);
						v.resource=table.getItem(table.getSelectionIndex()).getText(0);
						//v.VH.removeAll();
						
						v.open();
						v.select_resourcerole_version(table.getItem(table.getSelectionIndex()).getText(0));
						
						System.out.println("item.getText(table.getSelectionIndex())"+table.getItem(table.getSelectionIndex()).getText());
						System.out.println("item.getText(table.getSelectionIndex())"+table.getItem(table.getSelectionIndex()).getText(0));
					}});
			    
			    
		return composite;
	}
	
	public void select_role(String taskname, String taskidv)
	{
		
			String driver = "org.exist.xmldb.DatabaseImpl";
			String []str = new String [10];
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
result = service.query("for $ac  in doc('Activities.xml')//Activity  for $a in doc('Activities.xml')//Activity/versions/version for $r in doc('Resources.xml')//Resource let $vr := $r/versions/version/id_v let $o:= $a/resources/id_vr where $a/id_v='"+taskidv+"'and $vr=$o and $ac/name='"+taskname+"' return <id> {$r/name, $r/versions/version/number, $r/versions/version/id_v}  </id>");
								ResourceIterator i;
								i = result.getIterator();
								while(i.hasMoreResources()) {
								Resource r = i.nextResource();
								String s1=r.getContent().toString();
								int j=0;
								int l=0;
								
							String resource="";
								while (s1.indexOf("<name>")!=-1)
								{
									 j=s1.indexOf("<name>");
									
									 s1= s1.substring(j+6);
									 System.out.println("s1"+s1);
									j=s1.indexOf("<");
									resource=s1.substring(0, j);
									resourcename[l]=resource;
									s1= s1.substring(j);
									j=s1.indexOf("<number>");
									s1=s1.substring(j+8);
									j=s1.indexOf("<");
									resource=s1.substring(0, j);
									System.out.println("resource"+resource);
									resourceversionnumber[l]=resource;
									j=s1.indexOf("<id_v>");
									s1=s1.substring(j+6);
									j=s1.indexOf("<");
									resource=s1.substring(0, j);
									System.out.println("resource"+resource);
									resourceversionid[l]=resource;
									str[l]=resource;
									l++;
								}
								l=0;
								
								
							
								
							
								}
											 } catch (XMLDBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("m7"+e.getMessage());
							}
										
		}
	protected void okPressed() {
		if (changedone=true)
		{Shell s=new Shell();
		MessageBox dialog = 
				  new MessageBox(s, SWT.ICON_QUESTION |SWT.OK|SWT.CANCEL);
				dialog.setText("Resource Role version");
				dialog.setMessage("Would you like to derive a new version?");
				 dialog.open(); }
		
	}
	
	
}
