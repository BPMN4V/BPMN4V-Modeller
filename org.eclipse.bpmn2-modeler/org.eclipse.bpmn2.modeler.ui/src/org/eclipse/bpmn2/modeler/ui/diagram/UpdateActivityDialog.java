package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.AbstractObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.Messages;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TreeItem;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class UpdateActivityDialog extends AbstractObjectEditingDialog {
	private String [] data =new String[20];
	public static boolean updatemenu;
	public static  List 	list;

	public UpdateActivityDialog(DiagramEditor editor, EObject object) {
		super(editor, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Composite createDialogContent(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		layout.numColumns = 4;
		
		//layout.horizontalSpacing
		//layout.verticalSpacing = 4;
		// TODO Auto-generated method stub
		Label label = new Label(parent, SWT.NONE); 
	   	 label.setText("Activity Information ");
	   		list = new List(parent, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
		  list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
		  Button b=new Button(parent,SWT.NONE);
		  //b.setText(">>");
		  b.setImage(Activator.getDefault().getImage(IConstants.ICON_ADD_20));
		  b.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 1, 1));
		  Button b1=new Button(parent,SWT.NONE);
		//  b1.setText(">>");
		  b1.setImage(Activator.getDefault().getImage(IConstants.ICON_REMOVE_20));
		  b1.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 1, 1));
		 
	   	Label label3 = new Label(parent, SWT.NONE); 
	   	 label3.setText("Activity Resources");
	   	final List 	list2 = new List(parent, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
		  list2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
		  Button b3=new Button(parent,SWT.NONE);
		  b3.setImage(Activator.getDefault().getImage(IConstants.ICON_ADD_20));
		  b3.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 1, 1));
		  Button b4=new Button(parent,SWT.NONE);
		  b4.setImage(Activator.getDefault().getImage(IConstants.ICON_REMOVE_20));
		  b4.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 1, 1));
		  SelectActivityInformation(BPMNToolBehaviorProvider.id_va);
int i=0;
while(data[i]!=null)
{
list.add(SelectInformationName(data[i])+"("+data[i]+")");
i++;
}

b.addSelectionListener(new SelectionAdapter() {
	
	public void widgetSelected(SelectionEvent e) {
		//activity_name=list.getItem(list.getSelectionIndex());
		updatemenu=true;
		SelectActivityInformationDialog d =new SelectActivityInformationDialog(BPMNToolBehaviorProvider.editor, UpdateVersionActivity.bo);
d.open();

	
	}});

b1.addSelectionListener(new SelectionAdapter() {
	
	public void widgetSelected(SelectionEvent e) {
		//activity_name=list.getItem(list.getSelectionIndex());
	if (list.getItem(list.getSelectionIndex())!=null)
		list.remove(list.getSelectionIndex());
	
	}});
		return parent;
	}
	

	@Override
	protected String getPreferenceKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void okPressed() {
		//cancel = false;
		//dialogContent.dispose();
		int i=0;String data="";
	while (i<list.getItemCount())
	{
		//System.out.println("valeur de liste aprés subsring"+getDataId(list.getItem(i)));
		data=data+"<id_vda>"+getDataId(list.getItem(i))+"</id_vda>";
		i++;
	}
	DeleteActivityData(BPMNToolBehaviorProvider.id_va);
	InstertActivityData(data,BPMNToolBehaviorProvider.id_va);
		this.close();
		
		//super.okPressed();
		
	}
	private String getDataId(String valeurliste)
	{String id="";
	int i,j=0;
	j=valeurliste.indexOf("(");
	i=valeurliste.indexOf(")");
	id=valeurliste.substring(j+1,i);
	
		return id;
	}
	
	public void SelectActivityInformation(String idva)
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
   	s2=result.next().toString();
    s2=s2.substring(8);
    j=s2.indexOf("<");
   		 data[i]=s2.substring(0,j);
   		 i++;
	   }
  
  		
   session.commit();  
		    } finally {  
		      session.rollback();  
		    } }
	public String SelectInformationName(String idva)
	{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
	int i=0;String dataname="";
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
   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Data.xml')/Data/data for $i in $o/versions/version where $i/id_v='"+idva+"' return $o/name");
 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
   while(result.hasNext()) { 
   	s2=result.next().toString();
    s2=s2.substring(6);
    j=s2.indexOf("<");
    dataname=s2.substring(0,j);
   		
	   }
  
  		
   session.commit();  
		    } finally {  
		      session.rollback();  
		    }
return dataname; }
	
	protected void InstertActivityData(String idd, String ida)
	{
		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   System.out.println("for  $p in fn:doc('Activities.xml')/Activities/Activity for $i in $p/versions/version  let $o:=<data>"+idd+"</data> where $i/id_v='"+ida+"' return insert nodes $o into $i");
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity for $i in $p/versions/version  let $o:=<data>"+idd+"</data> where $i/id_v='"+ida+"' return insert nodes $o into $i");
		   		
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
	}
	
	protected void DeleteActivityData( String ida)
	{
		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in fn:doc('Activities.xml')/Activities/Activity for $v in $a/versions/version  where $v/id_v='"+ida+"' return   delete node $v/data");
		   		
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
	}

	@Override
	public int open() {
		title = "Define Information and Resources";
		
		return super.open();
	}

}
