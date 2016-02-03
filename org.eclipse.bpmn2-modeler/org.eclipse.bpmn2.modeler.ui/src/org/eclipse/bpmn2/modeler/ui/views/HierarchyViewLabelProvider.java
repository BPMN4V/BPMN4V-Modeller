package org.eclipse.bpmn2.modeler.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class HierarchyViewLabelProvider extends ColumnLabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof TreeStructure)
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	@Override
	public String getToolTipText(Object element)
	{if (element.toString().startsWith("VP", 0))
		return SelectVersionProcessActivities(element.toString());
	else
		return null;
	}
	
	
	protected String SelectVersionProcessActivities(String idvp)
	{
		
		

		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    int j=0;String s="Activities(";
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process for $i in $p/versions/version  where $i/id_v='"+idvp+"' return  $i/activities/id_va");
		   		
		    while(result.hasNext()) {  
		    	String s2=result.next().toString();
		    	 s2=s2.substring(7);
		         j=s2.indexOf("<");
		        		 s2=s2.substring(0,j);
		        		 s=s+select_activity_name(s2)+"("+s2+")"+"--";
		        		
		    	//System.out.println("ProcessVersionActivitiesId[i]"+ProcessVersionActivitiesId[i]);
		    	i++;
		    }
		    
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 
		s=s+")";
		return s;
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
	
	
}

