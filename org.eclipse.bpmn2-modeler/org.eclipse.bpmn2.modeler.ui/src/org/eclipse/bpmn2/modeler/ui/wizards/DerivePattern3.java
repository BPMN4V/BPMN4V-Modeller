
package org.eclipse.bpmn2.modeler.ui.wizards;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DerivePattern3 {
	

	
	public static String selectEtatActivity(String s) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();
		String s2 = "";
		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)
			int j = 0;
			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
			// .executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"
					.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+ s+ "'  return $i/state");

			while (result.hasNext()) {
				s2 = result.next().toString();
				s2 = s2.substring(7);
				j = s2.indexOf("<");
				s2 = s2.substring(0, j);
			}

			session.commit();
		} finally {
			session.rollback();
		}
		System.out.println("03-06-2015" + s2 + "fffffffff");
		return s2;

	}
	
	 public static String SelectLastVActivity(String name)
		{XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber where $p/name='"+name+"' return $p/id_vs");
	   //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	     while(result.hasNext()) {  
	  	   s2=result.next().toString();
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
		public static  int SelectLastVactivity(String name)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber where $p/name='"+name+"' return $p/id_vn");
	  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	    while(result.hasNext()) {  
	 	   s2=result.next().toString();
	 	   }
	    s2=s2.substring(7);
	    j=s2.indexOf("<");
	   		 s2=s2.substring(0,j);
	   		
	    session.commit();  
			    } finally {  
			      session.rollback();  
			    } 
	     i=Integer.parseInt(s2);
		return i;
			
			
		}
	
	

	
	
	
	public static void DeriveVersionActivity()
	{
		String id_dr=	WizardTarget3.activity_id;
	//	String data=SelectActivityInformation(id_dr);
		String data="";
    String idP=BPMN2Editor.currentInput.getName();
	String name=WizardTarget3.activity_name;
	 String old_activity_id=id_dr;
		 XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			int i=0;
			 if (driver.isInitialized()==false)
			driver.init();  
		    
		  XhiveSessionIf session = driver.createSession("xqapi-test");  
		  session.connect("Administrator", "imen", "vbpmn");  
		  session.begin();  
		  String s2; int j;
		String  new_activity_id= s2=  SelectLastVActivity(name);
		   i= SelectLastVactivity(name);
		  String next_id_s="";
		  j=s2.indexOf("-");
		  int f=i+1;
		  next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		  Date date = new Date();
		
		  try {  
		    XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		    // (1)
		   
		    //IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity  let $i:=<version><id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from><id_vd>"+id_dr+"</id_vd>  </derived_from><state>Working</state> <data>"+data+"</data></version> where $a/name='"+name+"' return insert nodes  $i into $a/versions");
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity  let $i:=<version><id_v>"+s2+"</id_v><nb_msg_flow><id_vp>"+idP+"</id_vp><nb_m>1</nb_m></nb_msg_flow><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from><id_vd>"+id_dr+"</id_vd>  </derived_from><state>Working</state> <data>"+data+"</data></version> where $a/name='"+name+"' return insert nodes  $i into $a/versions");

		    IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
		     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('Last_activity.xml')/Activities/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
		    session.commit();  
				    } finally {  
				      session.rollback();  
				    } 

		
		
		
	}
	
	
	
	
	

}
