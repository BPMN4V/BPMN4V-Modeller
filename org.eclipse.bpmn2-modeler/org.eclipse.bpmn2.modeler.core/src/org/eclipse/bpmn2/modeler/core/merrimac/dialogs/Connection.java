package org.eclipse.bpmn2.modeler.core.merrimac.dialogs;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class Connection {
	String driver = "org.exist.xmldb.DatabaseImpl";
	Class cl;
	 Database database;
	 Collection col;
	 ResourceSet result;
	 XPathQueryService service=null;
	 private static Connection con;
	private Connection() throws ClassNotFoundException, InstantiationException,
	IllegalAccessException, XMLDBException {
	super();
	 cl = Class.forName(driver);
	Database database = (Database) cl.newInstance();
	DatabaseManager.registerDatabase(database);}
	public static Connection getInstance()
	{
		if (null == con) { // Premier appel
			try {
			con = new Connection();
			} catch (ClassNotFoundException e) {
			// TODO Bloc catch auto-généré
				System.out.println("e "+e.getMessage());
			e.printStackTrace();
			} catch (InstantiationException e) {
			// TODO Bloc catch auto-généré
			e.printStackTrace();
			} catch (IllegalAccessException e) {
			// TODO Bloc catch auto-généré
			e.printStackTrace();
			} catch (XMLDBException e) {
			// TODO Bloc catch auto-généré
			e.printStackTrace();
			}
			}
			return con;
	}

}
