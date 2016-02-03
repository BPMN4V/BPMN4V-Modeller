package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class ResourceRoleVersionHierarchyDialog extends Dialog {
	public Tree VH=null;
	private TreeItem item=null;
	private TreeItem subitem=null;
	private  Database database;
	private Collection col;
	private ResourceSet result;
	private XPathQueryService service=null;
	private ResourceSet result1;
	private XPathQueryService service1=null;
	private Connection con;
	public String resource;
	private String dervivé;
	public ResourceRoleVersionHierarchyDialog(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.NONE); 
	   	 label.setText(""); 
	   	 //label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		
		 VH=new Tree(composite, SWT.NONE);
		 //VH.layout(true);
		   VH.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		   Label label2 = new Label(composite, SWT.NONE); 
		   	 label2.setText(""); 
		//   	 label2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, true, 2, 1));
if (resource!="")
select_resourcerole_version(resource);
		    
	return composite;
	}
	public void select_resourcerole_version(String s)
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
				result = service.query("for $a in doc('Resources.xml')//Resource where $a/name='"+s+"' return <id_v> {$a/versions/version/id_v} </id_v>");
				ResourceIterator i;
				i = result.getIterator();
				String str0[]= new String [10];
				String str1[]=new String[10];
				
				int j=0;
				int l=0;
				while(i.hasMoreResources()) {
					
					Resource r = i.nextResource();
					
					String s1=r.getContent().toString();
					
					while (s1.indexOf("VR")!=-1)
					{
						 j=s1.indexOf("VR");
						 s1= s1.substring(j);
						 
						j=s1.indexOf("<");
						str0[l]=s1.substring(0, j);
						
						l++;
						s1= s1.substring(j);
						
						
					}}
					int k=0;
					while(str0[k]!=null)
					{System.out.println("versions"+str0[k]);
					
				result1 = service.query("for $a in doc('Resources.xml')//Resource/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>");
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
}
