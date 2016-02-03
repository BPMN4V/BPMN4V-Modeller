package org.eclipse.bpmn2.modeler.ui.wizards;

import java.awt.Container;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.bpmn2.Process;
//import org.eclipse.bpmn2.Resource;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
//import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.wizard.Wizard;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class UpdateMessageFlowNumber {
	/*public void InsertActivitymessage(String id_va, String nb) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();

		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			

			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
					.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity for $i in $a/versions/version let $o:='"
							+ nb
							+ "' where $i/id_v='"
							+ id_va
							+ "' return replace value of node $i/nb_msg_flow with $o");
			//imen + "' return replace value of node $i/nb_m with $o");
			session.commit();
		} finally {
			session.rollback();
		}
	}

	
	
	*/
	//      protected Resource diagram;

	//   public final Resource getDiagram()
    //   {
    //     return diagram;
    //  }
	
	   
	//   protected Diagram getDiagram() {
		   	//	WizardAddPattern synchronizationPage;
		//		return this..getDiagram();
		//   	}
	public void InsertActivitymessage( String nb, String id_va, String id_vp) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		int k = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();

		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			

			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
					.executeXQuery("for  $a in fn:doc('Activities.xml')/Activities/Activity for $i in $a/versions/version for $x in $i/nb_msg_flow let $o:='"
							+ nb
							+ "' where ($i/id_v='"+ id_va+ "') and ($x/id_vp='"+ id_vp+ "' ) return replace value of node $x/nb_m with $o");
			
			
			

			//imen + "' return replace value of node $i/nb_m with $o");
			session.commit();
		} finally {
			session.rollback();
		}
	}
	public static void UpdateTaskNumMsgFlow(String id) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();

		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)
			int j = 0;

			j = Integer.parseInt(id);
			j++;
			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
			// .executeXQuery("for  $a in fn:doc('Last_activity.xml')/Activities let $o:='"
			// + j
			// + "' return replace value of node $a/id with $o");

					.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"
							+ id
							+ "' let $s:='"
							+ j
							+ "' return replace value of node $o/id with $s");
			// return $i/nb_msg_flow");

			session.commit();
		} finally {
			session.rollback();
		}

	}

	public static String selectTaskNumMsgFlow(String s, String id_vp) {
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
			// requête corrigé par imen ben said
					.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version for $k in $i/nb_msg_flow where $i/id_v='"
							+ s
							+ "' and  $i/nb_msg_flow/id_vp='"
							+ id_vp
							+ "' return $k/nb_m");

			while (result.hasNext()) {
				s2 = result.next().toString();
				// j = s2.indexOf("<nb_m>");
				s2 = s2.substring(6);
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
	
	
	public static String selectIdVersionFromidvp(String name , String id_vp) {
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
				//	.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/nb_msg_flow/id_vp='"+ id_vp+ "' return $i/id_v");
					.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity where $o/name='"+name+"' and (for $i in $o/versions/version where $i/nb_msg_flow/id_vp='"+ id_vp+ "') return $i/id_v");

			while (result.hasNext()) {
				s2 = result.next().toString();
				// j = s2.indexOf("<nb_m>");
				s2 = s2.substring(6);
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
		String id_dr=	WizardSource2.activity_id;
	//	String data=SelectActivityInformation(id_dr);
		String data="";
    String idP=BPMN2Editor.currentInput.getName();
	String name=WizardSource2.activityName;
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
	
	
	

	public static void DeriveVersionActivityP3()
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
	
	
	

	/*
	 * public static String selectTaskNumMsgFlow(String s) { XhiveDriverIf
	 * driver = XhiveDriverFactory .getDriver("xhive://localhost:1235"); if
	 * (driver.isInitialized() == false) driver.init();
	 * 
	 * XhiveSessionIf session = driver.createSession("xqapi-test");
	 * session.connect("Administrator", "imen", "vbpmn"); session.begin();
	 * String s2 = ""; try { XhiveLibraryIf rootLibrary =
	 * session.getDatabase().getRoot(); // (1) int j = 0; IterableIterator<?
	 * extends XhiveXQueryValueIf> result = rootLibrary //
	 * .executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"
	 * .executeXQuery(
	 * "for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"
	 * + s + "' return $i/nb_msg_flow");
	 * 
	 * while (result.hasNext()) { s2 = result.next().toString(); // j =
	 * s2.indexOf("<nb_msg_flow>"); s2 = s2.substring(13); j = s2.indexOf("<");
	 * s2 = s2.substring(0, j); }
	 * 
	 * session.commit(); } finally { session.rollback(); }
	 * System.out.println("03-06-2015" + s2 + "fffffffff"); return s2;
	 * 
	 * }
	 */

	public static void InstertActivity(String ida, String name, String id_vp,
			String data) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();
		i = ida.indexOf("-");

		String s = ida.substring(1, i);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("s" + s);
		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)

			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
					.executeXQuery("let $i:=<Activity><id>"
							+ s
							+ "</id><name>"
							+ name
							+ "</name><versions><version><id_v>"
							+ ida
							+ "</id_v><nb_msg_flow><id_vp>"
							+ id_vp
							+ "</id_vp><nb_m>1</nb_m></nb_msg_flow><number>--</number><creator>Imen</creator><creation_date>"
							+ dateFormat.format(date)
							+ "</creation_date><derived_from><id_vd>nil</id_vd>  </derived_from><state>Working</state><data>"
							+ data
							+ "</data> </version></versions></Activity> return insert nodes  $i into fn:doc('Activities.xml')/Activities");

			IterableIterator<? extends XhiveXQueryValueIf> result2 = rootLibrary
					.executeXQuery("let $i:=  <versionnumber> <name>"
							+ name
							+ "</name><id_vs>V"
							+ s
							+ "-2</id_vs> <id_vn>2</id_vn> </versionnumber> return insert nodes $i into doc('Last_activity.xml')/Activities");
			session.commit();
		} finally {
			session.rollback();
		}

	}
	
	
	
	
	public static void UpdateNbMsg(String id_vp, String id_v) {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();
		
		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)
		   //   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Collaboration.xml')/Collaborations/Collaboration  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>"+dateFormat.format(date)+"</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><processes></processes><path>"+path2+"</path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		  //    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity  let $i:=<nb_msg_flow> <id_vp>'"+id_vp+"'</id_vp><nb_m>1</nb_m></nb_msg_flow>where $p/id_v='"+id_v+"' return insert nodes  $i into $p/id_v");
		   //   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity/versions/version  let $i:=<nb_msg_flow> <id_vp>'"+id_vp+"'</id_vp><nb_m>1</nb_m></nb_msg_flow>where $p/id_v='"+id_v+"' return insert nodes  $i into $p/id_v");
		      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Activities.xml')/Activities/Activity/versions  let $i:=<nb_msg_flow> <id_vp>"+id_vp+"</id_vp><nb_m>1</nb_m></nb_msg_flow>where $p/version/id_v='"+id_v+"' return insert nodes  $i into $p/version");

			
			//for  $p in fn:doc('Activities.xml')/Activities/Activity/versions/version  let $i:=<nb_msg_flow> <id_vp>'"VC16-1"'</id_vp><nb_m>1</nb_m></nb_msg_flow>where $p/id_v='"VA145-1"' return insert nodes  $i into $p/id_v
			
			
				session.commit();
		} finally {
			session.rollback();
		}

	}
	
	
	public static String selectEtatProcess(String s) {
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
					.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+ s+ "'  return $i/state");
			//.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+ s+ "'  return $i/state");

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
	
	
	
	
	 public String DeriveProcess(String id_dr)
	    {
		 //String id_dr=	BPMNToolBehaviorProvider.id_v;
		//String name=BPMNToolBehaviorProvider.name_process;
		String name=WizardSource2.nameprocess;
		//String activities=SelectVersionProcessActivities(id_dr);
		String s2;
		
				XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		 if (driver.isInitialized()==false)
		driver.init();  
	      
	    XhiveSessionIf session = driver.createSession("xqapi-test");  
	    session.connect("Administrator", "imen", "vbpmn");  
	    session.begin(); 
	    int j=0; String s="";
	 s2=   SelectLastVProcess(name);
	int i= SelectLastVprocess(name);
	String next_id_s="";
	j=s2.indexOf("-");
	int f=i+1;
	next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
//	String path =select_version_process_path(id_dr);
	//String path2=path.replace(id_dr, s2);

	    try {  
	      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	      // (1)
	     
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>16/04/2014</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities></activities><path></path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	     IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
	     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
	    
	     session.commit();  
			    } finally {  
			      session.rollback();  
			    }
	   
	 /*   try {
			ModelHandler handler = ModelHandler
					.getInstance(getDiagram());

			Process p =	 (Process) handler.findElement(BPMNToolBehaviorProvider.id_v);

	p.setId(s2);

		} catch (IOException e) {
			Activator.logError(e);
		}*/
	  return s2;
			
		
	    
	}
	 
	 
	 
	 
	 
	 public String deriveProcessP3(String id_dr)
	    {
		 //String id_dr=	BPMNToolBehaviorProvider.id_v;
		//String name=BPMNToolBehaviorProvider.name_process;
		String name=WizardTarget3.nameProcess;
		//String activities=SelectVersionProcessActivities(id_dr);
		String s2;
		
				XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
		 if (driver.isInitialized()==false)
		driver.init();  
	      
	    XhiveSessionIf session = driver.createSession("xqapi-test");  
	    session.connect("Administrator", "imen", "vbpmn");  
	    session.begin(); 
	    int j=0; String s="";
	 s2=   SelectLastVProcess(name);
	int i= SelectLastVprocess(name);
	String next_id_s="";
	j=s2.indexOf("-");
	int f=i+1;
	next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
//	String path =select_version_process_path(id_dr);
	//String path2=path.replace(id_dr, s2);

	    try {  
	      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
	      // (1)
	     
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>16/04/2014</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities></activities><path></path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	     IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
	     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
	    
	     session.commit();  
			    } finally {  
			      session.rollback();  
			    }
	   
	 /*   try {
			ModelHandler handler = ModelHandler
					.getInstance(getDiagram());

			Process p =	 (Process) handler.findElement(BPMNToolBehaviorProvider.id_v);

	p.setId(s2);

		} catch (IOException e) {
			Activator.logError(e);
		}*/
	  return s2;
			
		
	    
	}
	    public String SelectLastVProcess(String name)
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
	     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vs");
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
		public int SelectLastVprocess(String name)
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
	    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber where $p/name='"+name+"' return $p/id_vn");
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
	
	
	
		public String select_version_collaboration_state(String id)
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
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Collaboration.xml')/Collaborations/Collaboration for $i in $o/versions/version where $i/id_v='"+id+"' return $i/state");
			
				while(result.hasNext()) {  
			    	   s2=result.next().toString();
			    	  j=s2.indexOf("<state>");
			    	  s2=s2.substring(j+7);
			    	  j=s2.indexOf("</state>");
			    	  s2=s2.substring(0, j);
			    	 }
	        
	      
	      session.commit();  
	    } finally {  
	      session.rollback();  
	    }  
			return s2;
			
		}
	
	
	
		
		
		
		
		public static String selectNameProcess(String s) {
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
						.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+ s+ "'  return $o/name");
				//.executeXQuery("for $o in doc('Activities.xml')/Activities/Activity for $i in $o/versions/version where $i/id_v='"+ s+ "'  return $i/state");

				while (result.hasNext()) {
					s2 = result.next().toString();
					s2 = s2.substring(6);
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
		
		
		
		
		
		
		
		 public String DeriveProcessPattern(String id_dr,String name)
		    {//String id_dr=	BPMNToolBehaviorProvider.id_v;
			//String name=BPMNToolBehaviorProvider.name_process;
		//	String name=WizardSource2.nameprocess;
			//String activities=SelectVersionProcessActivities(id_dr);
			String s2;
			
					XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
			 if (driver.isInitialized()==false)
			driver.init();  
		      
		    XhiveSessionIf session = driver.createSession("xqapi-test");  
		    session.connect("Administrator", "imen", "vbpmn");  
		    session.begin(); 
		    int j=0; String s="";
		 s2=   SelectLastVProcess(name);
		int i= SelectLastVprocess(name);
		String next_id_s="";
		j=s2.indexOf("-");
		int f=i+1;
		next_id_s="'"+s2.substring(0,j)+"-"+f+"'";
//		String path =select_version_process_path(id_dr);
		//String path2=path.replace(id_dr, s2);

		    try {  
		      XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();  
		      // (1)
		     
		     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version> <id_v>"+s2+"</id_v><number>V"+i+"</number><creator>Imen</creator><creation_date>16/04/2014</creation_date><derived_from> <id_vd>"+id_dr+"</id_vd></derived_from><activities></activities><path></path><state>Working</state></version>where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		     IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:="+next_id_s+" where $p/name='"+name+"' return replace value of node $p/id_vs with $o");
		     IterableIterator<? extends XhiveXQueryValueIf>  result2 = rootLibrary.executeXQuery("for  $p in fn:doc('LastProcess.xml')/Processes/versionnumber let $o:='"+f+"' where $p/name='"+name+"' return replace value of node $p/id_vn with $o");
		    
		     session.commit();  
				    } finally {  
				      session.rollback();  
				    }
		   
		 
		 /*   
		   try {
				ModelHandler handler = ModelHandler
						.getInstance(getDiagram());

				Process p =	 (Process) handler.findElement(id_dr);

		p.setId(s2);

			} catch (IOException e) {
				Activator.logError(e);
			}*/
		  return s2;
				
			
		    
		}
	
	

}
