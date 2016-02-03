package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TreeStructure;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class CreateActivityDialog extends Dialog   {
	
	private Tree VH=null;
	private TreeItem item=null;
	private TreeItem subitem=null;
	private TreeItem subsubitem=null;
	private  Database database;
	private Collection col;
	private ResourceSet result;
	private XPathQueryService service=null;
	private ResourceSet result1;
	private XPathQueryService service1=null;
	private Connection con;
	public List list;
	private List list1, list2;
	private Label label, label1, label2,label3,label4, label5;
	
	private Composite composite;
	private String dervivé="";
	private String activity_type="";
	public static String activity_name="";
	TreeStructure<String> root;
	
	public CreateActivityDialog(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Control createDialogArea(Composite parent) {
		VersionDialog.entry=true;
		Composite composite = (Composite)super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 9;
		label = new Label(composite, SWT.NONE); 
	   	 label.setText("Activity Name"); 
	   	list = new List(composite, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	     list.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
	     label1 = new Label(composite, SWT.NONE); 
	   	 label1.setText("Version hierarchy"); 
	     VH=new Tree(composite, SWT.NO_SCROLL|SWT.MULTI);
		   VH.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
	     label2 = new Label(composite, SWT.NONE); 
	   	 label2.setText("Version of Activity Type "); 
	   
	    label2.setVisible(false);
	    label5 = new Label(composite, SWT.NONE); 
	   	 label5.setText(""); 
	label5.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false,3, 1));
	    label5.setVisible(false);
	     label3 = new Label(composite, SWT.NONE); 
	   	 label3.setText("Version of Activity Resources "); 
	   
	    label3.setVisible(false);
	    list1 = new List(composite, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	    list1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
		 list1.setVisible(false);
	    label4 = new Label(composite, SWT.NONE); 
	   	 label4.setText("Version of Activity Data "); 
	  
	    label4.setVisible(false);
	 list2 = new List(composite, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	 list2.setVisible(false);
	 list2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
	 select_activity_name();
	   
	   
	    
	    
	 
	    
	    list.addSelectionListener(new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			activity_name=list.getItem(list.getSelectionIndex());
			VH.removeAll();
			select_activity_version(list.getItem(list.getSelectionIndex()));
		
		}});
	    
	    VH.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				list1.setVisible(true);
				label2.setVisible(true);
				label3.setVisible(true);
				label4.setVisible(true);
				list2.setVisible(true);
				label5.setVisible(true);
				TreeItem[] items=VH.getSelection();
				for(int i=0;i<items.length;i++)
				{System.out.println("selected item"+items[i].getText());
				select_activity_type(items[i].getText());
				list1.removeAll();list2.removeAll();
				select_activity_resources(items[i].getText());
				select_activity_data(items[i].getText());	
				  
				}
				
			
			}});
	    
	
	   // shell.setImage(image);

	    
	    
	   
	    return composite;
		
	}
	public void select_activity_name()
	{String driver = "org.exist.xmldb.DatabaseImpl";
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
				result = service.query("doc('Activities.xml')//Activity/name");
				ResourceIterator i;
				i = result.getIterator();
				while(i.hasMoreResources()) {
				Resource r = i.nextResource();
				String s= r.getContent().toString().substring(6, r.getContent().toString().length()-7);
			
				
				list.add(s);
				}
							 } catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("m7"+e.getMessage());
			}
							
	}
	public void select_activity_version(String s)
	{String driver = "org.exist.xmldb.DatabaseImpl";
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
				result = service.query("for $a in doc('Activities.xml')//Activity where $a/name='"+s+"' return <id_v> {$a/versions/version/id_v} </id_v>");
				ResourceIterator i;
				i = result.getIterator();
				String str0[]= new String [10];
				String str1[]=new String[10];
				
				int j=0;
				int l=0;
				while(i.hasMoreResources()) {
					
					Resource r = i.nextResource();
					
					String s1=r.getContent().toString();
					
					while (s1.indexOf("VA")!=-1)
					{
						 j=s1.indexOf("VA");
						 s1= s1.substring(j);
						 
						j=s1.indexOf("<");
						str0[l]=s1.substring(0, j);
						
						l++;
						s1= s1.substring(j);
						
						
					}}
					int k=0;
					while(str0[k]!=null)
					{System.out.println("versions"+str0[k]);
					
				result1 = service.query("for $a in doc('Activities.xml')//Activity/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>");
				i = result1.getIterator();
				
				while(i.hasMoreResources()) {
					
					Resource r = i.nextResource();
					
					String s1=r.getContent().toString();
					j=0;
					l=0;
				
					while (s1.indexOf("<id_vd>")!=-1)
					{
						 j=s1.indexOf("<id_vd>");
						 System.out.println("j "+j);
						 s1= s1.substring(j+7);
						 System.out.println("s1"+s1);
						j=s1.indexOf("<");
						dervivé=s1.substring(0, j);
						l++;
						s1= s1.substring(j);
						
					}
					System.out.println("dervivé "+dervivé);
					if (dervivé.compareTo("nil")==0)
						{
						
						
					item = new TreeItem(VH, SWT.NONE);
				       
			 		item.setText(str0[k]);
						}
					else
						//createchild(VH, dervivé,str0[k] );
					createchild2(VH.getItems(), dervivé,str0[k] );
					/*j=0;
					while (str1[j]!=null)
					{System.out.println("str1"+str1[j]);
					j++;}
					
					System.out.println("s1"+s1);
					String str[]=s1.split("<id_vd>");
					for(int m=0;m<str.length;m++)
					{if (str[m]!="")
						{String str2[]=str[m].split("</id_vd>");
						for(int p=0;p<str2.length;p++)
							System.out.println("valeur"+p+" "+str2[p]+"valeur");
					}}*/
				
				}
				k++;
				}
					
							 } catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("m7"+e.getMessage());
			}
	}
	public void createchild(Tree t, String derf, String der )
	{ TreeItem [] items;
		items=t.getItems();
		
		boolean trouve=false;
		int r=0;
		for (int i=0; i<items.length; i++)
			System.out.println("items"+items[i].getText()); 
		while (trouve==false && r<items.length)
		{ 
		
		
		if (items[r].getText().compareTo(derf)==0)
			{System.out.println("items"+items[r].getText()); 
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
	public void select_activity_type(String idv)
	{
		String driver = "org.exist.xmldb.DatabaseImpl";
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
							result = service.query("for $a in doc('Activities.xml')//Activity/versions/version  where $a/id_v='"+idv+"' return <id> {$a/type} </id>");
							ResourceIterator i;
							i = result.getIterator();
							while(i.hasMoreResources()) {
							Resource r = i.nextResource();
							String s1=r.getContent().toString();
							int j=0;
							int l=0;
						
							while (s1.indexOf("<type>")!=-1)
							{
								 j=s1.indexOf("<type>");
								
								 s1= s1.substring(j+6);
								 System.out.println("s1"+s1);
								j=s1.indexOf("<");
								activity_type=s1.substring(0, j);
								l++;
								s1= s1.substring(j);
								
							}
							label5.setText(activity_type);
							
						
							
						
							}
										 } catch (XMLDBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("m7"+e.getMessage());
						}	
	}
	public void select_activity_resources(String idv)
	{
		String driver = "org.exist.xmldb.DatabaseImpl";
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
result = service.query("for $a in doc('Activities.xml')//Activity/versions/version for $r in doc('Resources.xml')//Resource let $vr := $r/versions/version/id_v let $o:= $a/resources/id_vr where $a/id_v='"+idv+"'and $vr=$o return <id> {$r/name, $r/versions/version/number}  </id>");
							ResourceIterator i;
							i = result.getIterator();
							while(i.hasMoreResources()) {
							Resource r = i.nextResource();
							String s1=r.getContent().toString();
							int j=0;
							int l=0;
							String []str = new String [10];
						String resource="";
							while (s1.indexOf("<name>")!=-1)
							{
								 j=s1.indexOf("<name>");
								
								 s1= s1.substring(j+6);
								 System.out.println("s1"+s1);
								j=s1.indexOf("<");
								resource=s1.substring(0, j);
								
								s1= s1.substring(j);
								j=s1.indexOf("<number>");
								s1=s1.substring(j+8);
								j=s1.indexOf("<");
								resource=resource+" ("+s1.substring(0, j)+")";
								System.out.println("resource"+resource);
								str[l]=resource;
								l++;
							}
							l=0;
							for (l=0;l<str.length;l++)
								if (str[l]!=null)
								list1.add(str[l]);
							
						
							
						
							}
										 } catch (XMLDBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("m7"+e.getMessage());
						}	
	}
	public void select_activity_data(String idv)
	{
		String driver = "org.exist.xmldb.DatabaseImpl";
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
result = service.query("for $a in doc('Activities.xml')//Activity/versions/version for $r in doc('Data.xml')//data let $vr := $r/versions/version/id_v let $o:= $a/data/id_vda where $a/id_v='"+idv+"' and $vr=$o return <id> {$r/name, $r/versions/version/number}  </id>");
							ResourceIterator i;
							i = result.getIterator();
							while(i.hasMoreResources()) {
							Resource r = i.nextResource();
							String s1=r.getContent().toString();
							int j=0;
							int l=0;
							String []str = new String [10];
						String resource="";
							while (s1.indexOf("<name>")!=-1)
							{
								 j=s1.indexOf("<name>");
								
								 s1= s1.substring(j+6);
								 System.out.println("s1"+s1);
								j=s1.indexOf("<");
								resource=s1.substring(0, j);
								
								s1= s1.substring(j);
								j=s1.indexOf("<number>");
								s1=s1.substring(j+8);
								j=s1.indexOf("<");
								resource=resource+" ("+s1.substring(0, j)+")";
								System.out.println("resource"+resource);
								str[l]=resource;
								l++;
							}
							l=0;
							for (l=0;l<str.length;l++)
								if (str[l]!=null)
								list2.add(str[l]);
							
						
							
						
							}
										 } catch (XMLDBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("m7"+e.getMessage());
						}	
	}
	public String getactivityname()
	{return activity_name;}
}
