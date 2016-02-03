package org.eclipse.bpmn2.modeler.ui.diagram;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class Intialize {
	public static   String [] Information = new String[20];
	public static Tree VH=null;
	public static TreeItem item=null;
	public static TreeItem subitem=null;
	private TreeItem subsubitem=null;
	private static String dervivé="";
	
	public static void select_data()
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
	   int j=0;
	
	   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Data.xml')/Data/data  return  $p/name");
							
							while(result.hasNext()) {
							
							 s1=result.next().toString();

							 s1=s1.substring(6);
								
							 
							 
								j=s1.indexOf("<");
								
							
								Information[i]=s1.substring(0, j);
										i++;
									
							
						
							}
										  session.commit();  
						} finally {  
	   session.rollback();  
	 }
	 
	}
	public static void select_data_version(String name)
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
	
	   IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Data.xml')/Data/data for $i in $a/versions/version where $a/name='"+name+"' return $i/id_v");
	 //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
	   String str0[]= new String [10];
		String str1[]=new String[10];
		
		int j=0;
		int l=0;
	   while(result.hasNext()) {  
		   s1=result.next().toString();
		   
			s1=s1.substring(6);
			
				 
				 
				j=s1.indexOf("<");
				str0[l]=s1.substring(0, j);
				
				l++;
				
				
				
			}
			int k=0;
			while(str0[k]!=null)
			{System.out.println("versions"+str0[k]);
			
			 IterableIterator<? extends XhiveXQueryValueIf>  result1 = rootLibrary.executeXQuery("for $a in doc('Data.xml')//data/versions/version where $a/id_v='"+str0[k]+"' return <id> {$a/derived_from/id_vd} </id>");
		
		
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
			
		   
	   session.commit();  
	 } finally {  
	   session.rollback();  
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
	public static void createchild2(TreeItem [] items, String derf, String der)
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
	
	public static String selectTaskId()
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
     IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $a in fn:doc('Last_activity.xml')/Activities return $a/id");
		
			while(result.hasNext()) {  
		    	   s2=result.next().toString();
		    	   j=s2.indexOf("<id>");
			    	  s2=s2.substring(j+4);
			    	  j=s2.indexOf("</id>");
			    	  s2=s2.substring(0, j);
		    	 }
       
     
     session.commit();  
   } finally {  
     session.rollback();  
   }  
		return s2;
		
	}
	

}
