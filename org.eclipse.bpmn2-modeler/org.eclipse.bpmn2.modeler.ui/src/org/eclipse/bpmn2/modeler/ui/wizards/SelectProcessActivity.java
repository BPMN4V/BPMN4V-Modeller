package org.eclipse.bpmn2.modeler.ui.wizards;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class SelectProcessActivity {
	public static String[] VersionActivitiesId = new String[20];
	public static String[] VersionActivitiesname = new String[20];

	public void SelectProcessTask() {
		Document document = null;
		Element racine;
		Element element;
		int com = 0;
		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			// System.out.println("E:/Data/Travaux de thèse/Impémentation/BPMN4V/Process/"+path);
			String path2 = select_version_collaboration_path(BPMNToolBehaviorProvider.editor.currentInput.getName());
			File fileToSelect = new File(path2);
			document = sxb.build(new File(fileToSelect.toString()));
		} catch (Exception e) {
		}
		racine = document.getRootElement();
		List listFlow = racine.getChildren().subList(1, 2);

		// On crée un Iterator sur notre liste
		Iterator i = listFlow.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			List list = courant.getChildren();

			Iterator j = list.iterator();
			while (j.hasNext()) {
				Element courant2 = (Element) j.next();
				if (courant2.getName().compareTo("task") == 0
						|| courant2.getName().compareTo("subProcess") == 0) {
					VersionActivitiesId[com] = courant2.getAttributeValue("id");
					VersionActivitiesname[com] = courant2
							.getAttributeValue("name");
					com++;
				}
			}

		}
	}

	public String select_version_process_path(String s) {
		String dervivé = null;
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
					.executeXQuery("for $a in doc('Processes.xml')//Process/versions/version where $a/id_v='"
							+ s + "' return <id> {$a/path} </id>");

			while (result.hasNext()) {
				String s1 = result.next().toString();

				j = s1.indexOf("<path>");

				s1 = s1.substring(j + 6);

				j = s1.indexOf("<");
				dervivé = s1.substring(0, j);

			}

			session.commit();
		} finally {
			session.rollback();
		}
		return dervivé;
	}
	public String select_version_collaboration_path(String s)
	{String dervivé=null; 
		XhiveDriverIf driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");  
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
	      IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $a in doc('Collaboration.xml')//Collaboration/versions/version where $a/id_v='"+s+"' return <id> {$a/path} </id>");
			
				while(result.hasNext()) {  
			    	  String  s1=result.next().toString();
			    	  
						
							 j=s1.indexOf("<path>");
							
							 s1= s1.substring(j+6);
							
							j=s1.indexOf("<");
							dervivé=s1.substring(0, j);
						
							
					
						
						}	  
			
				
				
	        
	      
	      session.commit();  
	    } finally {  
	      session.rollback();  
	    }  
	    return dervivé;
	}

}
