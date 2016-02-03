package org.eclipse.bpmn2.modeler.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewSite;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class HierarchyViewContentProvider implements
		IStructuredContentProvider, ITreeContentProvider {

	private TreeStructure invisibleRoot;
	private FlowElement [] Activities; 
	private FlowElement g;
	private TreeStructure item=null;
	private TreeStructure firstitem=null;
	private TreeStructure subitem=null;
	
	private String dervivé="";

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		if (parent instanceof IViewSite) {
			if (invisibleRoot == null) {
				initialize();
				
			}
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof TreeStructure) {
			return ((TreeStructure) child).getParent();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeStructure) {
			return ((TreeStructure) parent).getChildren();
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeStructure) {
			return ((TreeStructure) parent).hasChildren();
		}
		return false;
	}

	/*
	 * We will set up a dummy model to initialize tree heararchy. In a real code, you will connect to a real model and
	 * expose its hierarchy.
	 */
	private void initialize() {
		invisibleRoot = new TreeStructure(""); //$NON-NLS-1$
		//TreeObject t=new TreeObject("salut");
	//	invisibleRoot.addChild(t);
	}

	public void updateModel(ModelHandler mh, String element, String type){
		initialize();
		invisibleRoot.removeChildren();
		if (mh == null) {
			return;
		}
		//getSelected(mh.g)
		if (type.compareTo("Process")==0)
		{String b=element.substring(0, 2);
		System.out.println("bbb"+b);
		 if (b.compareTo("VC")==0)
		 {
				String name=	SelectCollaborationName(element);
				
				firstitem = new TreeStructure(name);
						invisibleRoot.addChild(firstitem);
					
				
				
				select_collaboration_version(name);
		 }
		 else
		
		{String name=	SelectProcessName(element);
		
		firstitem = new TreeStructure(name);
				invisibleRoot.addChild(firstitem);
			
		
		
		select_process_version(name);}
		
			
		
		//TreeObject t=new TreeObject(name);
		//invisibleRoot.addChild(t);
		}
		else
			if (type.compareTo("Task")==0)
			{
			String name=	select_activity_name(element);
			
			firstitem = new TreeStructure(name);
					invisibleRoot.addChild(firstitem);
				
					select_activity_version(name);
			
		//	select_process_version(name);
			
			//TreeObject t=new TreeObject(name);
			//invisibleRoot.addChild(t);
			}
			else
				if (type.compareTo("Participant")==0)
				{
					String name=	SelectProcessName(element);
					
					firstitem = new TreeStructure(name);
							invisibleRoot.addChild(firstitem);
						
					
					
					select_process_version(name);
				
			//	select_process_version(name);
				
				//TreeObject t=new TreeObject(name);
				//invisibleRoot.addChild(t);
				}
			
		
	
		}
	
		/*TreeParent proc2 = new TreeParent("List of Activities Versions  "); //$NON-NLS-1$
		//MBeanServer  mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
		 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	      
		SelectActivitiesid();
		
		/*ObjectName target = null;
		try {
			target = new ObjectName("com.example:type=salt");
			target.setMBeanServer(mbs);
			
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//BaseElement b = BaseElement
				//b.setId("ok");*/
		
	
		//TreeObject t=new TreeObject(target);
		/*VObject v =new VObject("salut");
		
		TaskImp t2 =new TaskImp();
	//	ModelUtil.addAnyAttribute(t2, "anyAttribute", value);
		t2.setTaskName("imen");
		TreeObject t=new TreeObject(t2);
		invisibleRoot.addChild(proc2);
		invisibleRoot.addChild(t);*/
	

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
				
				
			item = new TreeStructure(str0[k]);
			
		firstitem.addChild(item);//+" ("+s2.toString()+")");
				}
			else
				
			createchild2(firstitem.getChildren(), dervivé,str0[k] );
			
		
		}
		k++;
		}
    	  
      session.commit();  
    } finally {  
      session.rollback();  
    }  
		
							 
	}
	
	public void createchild2(TreeStructure [] items, String derf, String der)
	{//TreeObject items[]=parent.getChildren();
		if (items.length==0)
			return;
		for(int i=0;i<items.length;i++)
		{
			if( items[i].getName().compareTo(derf)==0)
			{
				subitem=  new TreeStructure(der);
				
				items[i].addChild(subitem);
			//subitem.setParent(item);
			//item[].addChild(subitem);
			
			return;
				
			}
		createchild2(items[i].getChildren(),derf,der);
		}
		
	}

	
	@SuppressWarnings("restriction")
	public Object[] getSelected(ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection sel = (StructuredSelection) selection;
			List<Object> selected = Arrays.asList(sel.toArray());
			if (selected.size() == 0 || !(selected.get(0) instanceof ContainerShapeEditPart)) {
				return null;
			}

			PictogramLink link = ((ContainerShapeEditPart) selected.get(0)).getPictogramElement().getLink();
			if (link == null) {
				return null;
			}

			EList<EObject> businessObjects = link.getBusinessObjects();
			TreeStructure[] children = invisibleRoot.getChildren();
			
			ArrayList<TreeStructure> list = getSelectionFromList(businessObjects, children);
			
			return list.toArray();
		}
		return null;
	}

	public String[] getSelectedS(ISelection selection) {
		String []t =new String [10];
		if (selection instanceof StructuredSelection) {
			StructuredSelection sel = (StructuredSelection) selection;
			List<Object> selected = Arrays.asList(sel.toArray());
			if (selected.size() == 0 || !(selected.get(0) instanceof ContainerShapeEditPart)) {
				return null;
			}

			PictogramLink link = ((ContainerShapeEditPart) selected.get(0)).getPictogramElement().getLink();
			if (link == null) {
				return null;
			}

			EList<EObject> businessObjects = link.getBusinessObjects();
			TreeStructure[] children = invisibleRoot.getChildren();
			ArrayList<TreeStructure> list = getSelectionFromList(businessObjects, children);
			for (int i=0;i<list.size();i++)
			{t[i]=list.get(i).getName();
			System.out.println("list.get(i)"+list.get(i).getName());}
			return t;
		}
		return null;
		}
	private ArrayList<TreeStructure> getSelectionFromList(EList<EObject> businessObjects, TreeStructure[] children) {
		ArrayList<TreeStructure> list = new ArrayList<TreeStructure>();
		for (TreeStructure treeObject : children) {
			
				list.add(treeObject);
			}
		
		return list;
	}
	 public String SelectProcessName(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process where $o/versions/version/id_v='"+id_v+"' return $o/name");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   
	 	   }
	    if (s2!="")
	    { s2=s2.substring(6);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);}
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 
	 public String select_activity_name(String id)
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
	   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery(" for  $p in fn:doc('Activities.xml')/Activities/Activity for $i in $p/versions/version  where  $i/id_v='"+id+"' return $p/name");
	  // IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $p in doc('Processes.xml')/Processes/Process for $i in $p/versions/version for $o in $i/activities for $a in doc('Activities.xml')/Activities/Activity for $l in $a/versions/version where $o/id_va=$l/id_v and $p/name='"+BPMNToolBehaviorProvider.name_process+"' return $a/name");
	 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	   while(result.hasNext()) {  
		   s2=result.next().toString();
				   s2=s2.substring(6, s2.length()-7);
				 
				  
		   }
	   session.commit();  
	 } finally {  
	   session.rollback();  
	 } 
		return s2;				
								
		}
		public void select_activity_version(String s)
		{
			
				
			
			XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		   
		 XhiveSessionIf session = driver.createSession("xqapi-test");  
		 session.connect("Administrator", "imen", "vbpmn");  
		 session.begin();  
		 String s1="";
		 try {  
		   XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		   // (1)
		
		   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"+s+"' return <id_v> {$a/versions/version/id_v} </id_v>");
		 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		   String str0[]= new String [10];
			String str1[]=new String[10];
			
			int j=0;
			int l=0;
		   while(result.hasNext()) {  
			   s1=result.next().toString();
			   
				
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
				
				 IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for $a in doc('Activities.xml')//Activity/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>");
			
			
			while(result1.hasNext()) {
				
			
				
				String s2=result1.next().toString();
				j=0;
				l=0;
			
				while (s2.indexOf("<id_vd>")!=-1)
				{
					 j=s2.indexOf("<id_vd>");
					 System.out.println("j "+j);
					 s2= s2.substring(j+7);
					 System.out.println("s2"+s2);
					j=s2.indexOf("<");
					dervivé=s2.substring(0, j);
					l++;
					s2= s2.substring(j);
					
				}
				System.out.println("dervivé "+dervivé);
				if (dervivé.compareTo("nil")==0)
					{
					
					
				item = new TreeStructure(str0[k]);
			       
		 		firstitem.addChild(item);;
					}
				else
					//createchild(VH, dervivé,str0[k] );
				createchild2(firstitem.getChildren(), dervivé,str0[k] );
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
				
			   
		   session.commit();  
		 } finally {  
		   session.rollback();  
		 } }
	
		
			
		 public String SelectCollaborationName(String id_v)
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
						
						
					item = new TreeStructure(str0[k]);
					
				firstitem.addChild(item);//+" ("+s2.toString()+")");
						}
					else
						
					createchild2(firstitem.getChildren(), dervivé,str0[k] );
					
				
				}
				k++;
				}
		    	  
		      session.commit();  
		    } finally {  
		      session.rollback();  
		    }  
				
									 
			}
			
	
}