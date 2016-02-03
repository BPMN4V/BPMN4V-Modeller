package org.eclipse.bpmn2.modeler.ui.diagram;

import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.AbstractObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class SelectActivityInformationDialog extends AbstractObjectEditingDialog {
	public static String [] SelectedInformationid=new String[20];
public static String activity_id;
private List list2;
	public SelectActivityInformationDialog(DiagramEditor editor, EObject object) {
		super(editor, object);
		activity_id=BPMNToolBehaviorProvider.id_va;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Composite createDialogContent(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		layout.numColumns = 3;
		
		//layout.horizontalSpacing
		//layout.verticalSpacing = 4;
		// TODO Auto-generated method stub
		Label label = new Label(parent, SWT.NONE); 
	   	 label.setText("Information Name");
	   	Label label3 = new Label(parent, SWT.NONE); 
	   	 label3.setText("");
	   	Label label4 = new Label(parent, SWT.NONE); 
	   	 label4.setText("Selected Information");
	  final List 	list = new List(parent, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	  list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
	  Intialize.select_data();int i=0;
	  while (Intialize.Information[i]!=null )
	  {list.add(Intialize.Information[i]);
	  i++;}
	  Button b=new Button(parent,SWT.NONE);
	  b.setText(">>");
	  b.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true, 1, 1));
	 
	list2 = new List(parent, SWT.BORDER|SWT.SCROLLBAR_OVERLAY);
	  list2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
	  Label label2 = new Label(parent, SWT.NONE); 
	   	 label2.setText("Information Version Hierarchy");
	   	Label label5 = new Label(parent, SWT.NONE); 
	   	 label5.setText("");
	 Label label6 = new Label(parent, SWT.NONE); 
	   	 label6.setText("");
	  Intialize.VH=  new Tree(parent, SWT.NO_SCROLL|SWT.BORDER);
	  Intialize.VH.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
	  list.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				//activity_name=list.getItem(list.getSelectionIndex());
				Intialize.VH.removeAll();
				Intialize.select_data_version(list.getItem(list.getSelectionIndex()));
			
			}});
	  final int j=0;
	  
	  b.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				//activity_name=list.getItem(list.getSelectionIndex());
			TreeItem[] i=	Intialize.VH.getSelection();
			if (i.length!=0 && list.getItem(list.getSelectionIndex())!=null)
				{list2.add(list.getItem(list.getSelectionIndex())+" ("+i[0].getText()+")");
				SelectedInformationid[j]=i[0].getText();}
			
			}});
		return parent;
		
	}

	@Override
	protected String getPreferenceKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int open() {
		title = "Define Activity Information";
		
		return super.open();
	}
	
	@Override
	protected void okPressed() {
		//cancel = false;
		//dialogContent.dispose();
		int i=0;
		InsertInformation_In_Tamporal_file();
		if (UpdateActivityDialog.updatemenu==true)
		{UpdateActivityDialog.updatemenu=false;
			while(i<list2.getItemCount())
			{UpdateActivityDialog.list.add(list2.getItem(i));
			i++;}}
			
		this.close();
		
		//super.okPressed();
		
	}
	
	public void InsertInformation_In_Tamporal_file()
	{String Information="";int k=0;
	while (SelectedInformationid[k]!=null)
	{Information=Information+"<id_vda>"+SelectedInformationid[k]+"</id_vda>";
	k++;}
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
	   
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("let $o:=<Activity><id_va>"+activity_id+"</id_va> <data>"+Information+"</data></Activity> return insert node $o into fn:doc('TemporyActivityData.xml')/Activities");
	   		
	   
	    
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	
		
		
	}

}
