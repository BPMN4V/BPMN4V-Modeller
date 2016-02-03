package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.help.IHelpContexts;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.dialogs.Dialog;
//import java.awt.Label;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;

import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.Connection;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
public class VersionDialog1 extends Dialog {
	
	public VersionDialog1(Shell shell) {
		super(shell);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Control createDialogArea(Composite parent) {
		 String driver = "org.exist.xmldb.DatabaseImpl";
		
		 Database database;
		 Collection col;
		 ResourceSet result;
		 XPathQueryService service=null;
		 Connection con;
		 
		 Composite area = (Composite)super.createDialogArea(parent);
		 
		    Label label = new Label(area, SWT.RIGHT|SWT.CENTER);
		   
	      label.setText("Activity Name ");
	      label.setBounds(new Rectangle(8, 102, 92, 34));
	       		    List list = new List(area, SWT.BORDER | SWT.MULTI);
	       		list.setBounds(new Rectangle(129, 92, 124, 64));
	       		 Label label1 = new Label(area, SWT.RIGHT|SWT.CENTER);
	 	       label1.setText("Derivation Hierarchy");
	 	     
	 	      label1.setBounds(new Rectangle(263, 101, 45, 23));
	 	      
		//list.setLocation(0, 0);
		System.out.println("ok");
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
				System.out.println((String)r.getContent());
				list.add((String)r.getContent());
				}
							 } catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("m7"+e.getMessage());
			}
			
		
			
				
				
						
		
	    // We define a minimum width for the list
	  //  final GridData gridData = new GridData();
	    //gridData.widthHint = 200;
	    //list.setLayoutData(gridData);
		
	return area;}
	/*public Composite createDialogContent(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		composite.getChildren(); 
	Composite composite =	PropertiesCompositeFactory.INSTANCE.createDialogComposite(
				featureEType, parent, SWT.NONE);
		Label label = new Label(parent, SWT.WRAP);
         label.setText("ok");
         PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), IHelpContexts.Property_Dialog);
		return parent;*/
	
}
	

//}
