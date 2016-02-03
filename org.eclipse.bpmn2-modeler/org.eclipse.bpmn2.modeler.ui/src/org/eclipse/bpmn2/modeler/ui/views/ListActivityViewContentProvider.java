package org.eclipse.bpmn2.modeler.ui.views;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServer;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.impl.TaskImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.ui.internal.parts.ContainerShapeEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class ListActivityViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	private TreeParent invisibleRoot;
	private FlowElement [] Activities; 
	private String[] ActivitiesName =new String[100];
	private   List<String> Activitiesversionid = new LinkedList();

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
		if (child instanceof TreeObject) {
			return ((TreeObject) child).getParent();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).hasChildren();
		}
		return false;
	}

	/*
	 * We will set up a dummy model to initialize tree heararchy. In a real code, you will connect to a real model and
	 * expose its hierarchy.
	 */
	private void initialize() {
		invisibleRoot = new TreeParent(""); //$NON-NLS-1$
		
		//TreeObject t=new TreeObject("salut");
	//	invisibleRoot.addChild(t);
	}

	public void updateModel(ModelHandler mh) {
		invisibleRoot.removeChildren();
		if (mh == null) {
			return;
		}

		SelectActivitiesName();
		
		
		Definitions definitions = mh.getDefinitions();
 
		List<RootElement> rootElements = definitions.getRootElements();
		
		Flow f = null;
		int j=0;
		while (ActivitiesName[j]!=null)
		{
		TreeParent proc2 = new TreeParent(ActivitiesName[j]); //$NON-NLS-1$
		invisibleRoot.addChild(proc2);
		
		SelectActivitiesid(ActivitiesName[j]);
		for (int i=0;i<Activitiesversionid.size();i++)
		{TaskImp t2 =new TaskImp();
			
			
			
	//	ModelUtil.addAnyAttribute(t2, "anyAttribute", rootElements.get(0).getAnyAttribute());
		t2.setTaskName(Activitiesversionid.get(i));
		t2.setId(Activitiesversionid.get(i));
		
		
	//t2.eSet(eFeature, newValue);
		TreeObject t=new TreeObject(t2);
		//invisibleRoot.addChild(t);
		proc2.addChild(t);
		}
			
		j++;
		//VObject v =new VObject("salut");
		
		//TaskImp t2 =new TaskImp();
	//	ModelUtil.addAnyAttribute(t2, "anyAttribute", value);
		/*t2.setTaskName("imen");
		TreeObject t=new TreeObject(t2);
		invisibleRoot.addChild(proc2);
		invisibleRoot.addChild(t);
		
		TreeParent proc3 = new TreeParent("of Activities Versions  ");
		TaskImp t3 =new TaskImp();
		//	ModelUtil.addAnyAttribute(t2, "anyAttribute", value);
			t3.setTaskName("imen");
			TreeObject t4=new TreeObject(t3);
			invisibleRoot.addChild(proc3);
			invisibleRoot.addChild(t4);*/
		
	}
		/*TaskImp t3 =new TaskImp();
		Pe p = null;
		
		EObject container = BusinessObjectUtil.getBusinessObjectForPictogramElement(p);
		Resource resource = container.eResource();
		EClass eclass = t3.eStaticClass();
		ExtendedPropertiesAdapter adapter = ExtendedPropertiesAdapter.adapt(eclass);
		Task businessObject = (Task)adapter.getObjectDescriptor().createObject(resource,eclass);
			
		businessObject.setName("ffff");
		TreeObject t4=new TreeObject(businessObject);
		invisibleRoot.addChild(t4);*/
		 
		}

	private void createLaneSets(TreeParent proc, List<LaneSet> laneSets) {
		for (LaneSet laneSet : laneSets) {
			createLaneSetTree(proc, laneSet);

		}
	}

	private void createLaneSetTree(TreeParent proc, LaneSet laneSet) {
		if (laneSet == null) {
			return;
		}
		for (Lane lane : laneSet.getLanes()) {
			TreeParent parent = new TreeParent(lane);
			proc.addChild(parent);
			createLaneSetTree(parent, lane.getChildLaneSet());
		}
	}

	private void createFlowElementTree(TreeParent proc, List<FlowElement> flowElements)  {
		for (FlowElement f : flowElements) {
			
			if ((f instanceof Task)) {
				//Task task = (Task) f;
			
				//task.setName(f.getName()+"("+f.getId()+")");
				TreeObject treeObject = new TreeObject(f.getId(),SelectActivitystate(f.getId()),f);
				
				
				System.out.println("f.eClass()"+f.eClass().getName());
				//System.out.println("f.getId()"+f.getId());
				proc.addChild(treeObject);
			}
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
			TreeObject[] children = invisibleRoot.getChildren();
			ArrayList<TreeObject> list = getSelectionFromList(businessObjects, children);
			return list.toArray();
		}
		return null;
	}

	private ArrayList<TreeObject> getSelectionFromList(EList<EObject> businessObjects, TreeObject[] children) {
		ArrayList<TreeObject> list = new ArrayList<TreeObject>();
		for (TreeObject treeObject : children) {
			if (treeObject instanceof TreeParent) {
				if (businessObjects.contains(treeObject.getBaseElement())) {
					list.add(treeObject);
				}
				list.addAll(getSelectionFromList(businessObjects, ((TreeParent) treeObject).getChildren()));
			} else if (businessObjects.contains(treeObject.getBaseElement())) {
				list.add(treeObject);
			}
		}
		return list;
	}
	
	 public String SelectActivitystate(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(j+"s2"+s2);
	 	   j++;
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
	    s2=s2.substring(6);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectProcessState(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(j+"s2"+s2);
	 	   j++;
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
	 protected void 	SelectActivitiesid(String activity_name)
	 {
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
	       IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity  where $p/name='"+activity_name+"' return  $p/versions/version/id_v");
	     Activitiesversionid.removeAll(Activitiesversionid);
	     while(result.hasNext()) {  
	     	String s2=result.next().toString();
	     	 s2=s2.substring(6);
	          j=s2.indexOf("<");
	         		 s2=s2.substring(0,j);
	         		Activitiesversionid.add(s2);
	     	i++;
	     }
	     
	     session.commit();  
	 		    } finally {  
	 		      session.rollback();  
	 		    } 
	 	
	 	
	 		

	 }
	 
	 protected void 	SelectActivitiesName()
	 {
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
	    
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity  return  $p/name");
	    		
	     while(result.hasNext()) {  
	     	String s2=result.next().toString();
	     	System.out.println("s2"+s2);
	     	 s2=s2.substring(6);
	          j=s2.indexOf("<");
	         		 s2=s2.substring(0,j);
	         		//System.out.println("s2"+s2);
	         		 ActivitiesName[i]=s2;
	         		//final IFeatureProvider fp = getFeatureProvider();
	         	//	Activities[i]=new FlowElement();
	         		// Activities[i].setId(s2);
	     	//System.out.println("ActivitiesId[i]"+ActivitiesId[i]);
	     	i++;
	     }
	     
	     session.commit();  
	 		    } finally {  
	 		      session.rollback();  
	 		    } 
	 	
	 	
	 		

	 }
	 	
	 	

}

