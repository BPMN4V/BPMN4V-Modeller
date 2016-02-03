package org.eclipse.bpmn2.modeler.ui.views;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Collaboration;
//import org.eclipse.datatools.connectivity.ui.templates.TreeObject;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
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

public class ActivityViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	private TreeParent invisibleRoot;
	private FlowElement [] Activities; 
	private FlowElement g;

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

		
		
		Definitions definitions = mh.getDefinitions();
 
		List<RootElement> rootElements = definitions.getRootElements();
		for (RootElement element : rootElements) {
			System.out.println("31-05-201555"+element.getId());
			
		if (element instanceof Process) {
				Process process = (Process) element;
	//	System.out.println("emploi"+process.getId()+process.getName());
				TreeParent proc = new TreeParent(SelectProcessName(process.getId())+" ***"+process.getId()+"*** ("+SelectProcessState(process.getId())+")"); //$NON-NLS-1$

			//	createLaneSets(proc, process.getLaneSets());
				createFlowElementTree(proc, process.getFlowElements());
				invisibleRoot.addChild(proc);
			}
		}
	
		
	}
	public void updateModel1(ModelHandler mh) {
		invisibleRoot.removeChildren();
		if (mh == null) {
			return;
		}

		
		
		Definitions definitions = mh.getDefinitions();
 
		List<RootElement> rootElements = definitions.getRootElements();
		for (RootElement element : rootElements) {
			System.out.println("31-05-201555"+element.getId());
			
			if (element instanceof Collaboration) {
				Collaboration c = (Collaboration) element;
	//	System.out.println("emploi"+process.getId()+process.getName());
				TreeParent proc = new TreeParent(SelectCollaborationName(c.getId())+" ***"+c.getId()+"*** ("+SelectCollaborationState(c.getId())+")"); //$NON-NLS-1$
				invisibleRoot.addChild(proc);
				List<Participant> p=	c.getParticipants();
			Iterator<Participant> i=p.iterator();
			while (i.hasNext())
			{
				Participant par=i.next();
				Process process=	par.getProcessRef();
				TreeParent treeObject = new TreeParent(SelectProcessName(process.getId())+" *"+process.getId()+"* ("+SelectProcessState(process.getId())+")");
				proc.addChild(treeObject);
				createFlowElementTree(treeObject, process.getFlowElements());
			}
			//	createLaneSets(proc, process.getLaneSets());
				//createFlowElementTree(proc, process.getFlowElements());
				
			}}
		
	
		
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
			
			if ((f instanceof Task)|| (f instanceof SubProcess)) {
				//Task task = (Task) f;
			
				//task.setName(f.getName()+"("+f.getId()+")");
			
				TreeObject treeObject = new TreeObject(f.getId(),SelectActivitystate(f.getId()),f);
				
				
				
				//System.out.println("f.getId()"+f.getId());
				proc.addChild(treeObject);
				/*f.getAnyAttribute();
				Flow fl;
				fl=(Flow) f;
				fl.setName("ff");*/
				
				
				
			}
			else
				if ((f instanceof Event)) {
					//Task task = (Task) f;
				
					//task.setName(f.getName()+"("+f.getId()+")");
				
					TreeObject treeObject = new TreeObject(f.getId(),SelectEventstate(f.getId()),f);
					
					
					
					//System.out.println("f.getId()"+f.getId());
					proc.addChild(treeObject);
					/*f.getAnyAttribute();
					Flow fl;
					fl=(Flow) f;
					fl.setName("ff");*/
					
					
					
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
	 	   System.out.println(id_v+"s2"+s2);
	 	  s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   
	 	   }
	   
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    if (s2!="")
		return s2;
	    else 
	    	return "Working";
			
			
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
	 	  s2=s2.substring(6);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   }
	   
	    if (s2.length()>6)
	    { }
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
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
	 	  s2=s2.substring(6);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   }
	   
	    if (s2.length()>6)
	    { }
	   		
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
	 	  // System.out.println(j+"s2"+s2);
	 	  s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   }
	   
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 public String SelectCollaborationState(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	  // System.out.println(j+"s2"+s2);
	 	  s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   }
	   
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    
		return s2;
			
			
		}
	 protected void 	SelectActivitiesid()
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
	    
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity for $i in $p/versions/version   return  $i/id_v");
	    		
	     while(result.hasNext()) {  
	     	String s2=result.next().toString();
	     	 s2=s2.substring(7);
	          j=s2.indexOf("<");
	         		 s2=s2.substring(0,j);
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
	 public String SelectEventstate(String id_v)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Events.xml')/Events/Event for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   System.out.println(id_v+"s2"+s2);
	 	  s2=s2.substring(7);
		    j=s2.indexOf("<");
		   		 s2=s2.substring(0,j);
	 	   
	 	   }
	   
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	    if (s2!="")
		return s2;
	    else 
	    	return "Working";
			
			
		}
	 	

}
