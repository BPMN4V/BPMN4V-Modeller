package org.eclipse.bpmn2.modeler.ui.wizards;

import java.awt.Container;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.help.IHelpContexts;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class WizardAddPattern extends Wizard implements INewWizard {
	public static WizardSource page1;
	private WizardTarget page2;
	public static boolean a = false;
	private boolean b = false;
	private ISelection selection;
	public static String type = "derive";
	public static String processStableT = "";
	
	
	

	/**
	 * Constructor for BPMN2DiagramWizard.
	 */
	public WizardAddPattern() {
		super();
		setNeedsProgressMonitor(true);
		
	}

	/**
	 * Adding the page2 to the wizard.
	 */

	@Override
	public void addPages() {
		page1 = new WizardSource(selection);
		addPage(page1);
		page2 = new WizardTarget(selection);
		addPage(page2);

	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(getShell(), IHelpContexts.New_File_Wizard);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		a = true;
		return true;
	}
	
	
	
	/**
	 * Gets the diagram.
	 * 
	 * @return the diagram
	 */
	

	public boolean canFinish() {

		if (WizardTarget.afficheFinish == true) {

		/*	UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
			String stateP = st.selectEtatProcess(WizardTarget.idvp);
			if (stateP.compareTo("Stable") == 0) {

				processStableT = WizardTarget.idvp;
				int response = JOptionPane
						.showConfirmDialog(
								null,
								"Do you want to derive the version of process?",
								"Do you want to derive version of process",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					// System.out.println("No button clicked");
					// ws.dispose();

					return false;
					// WizardAddPattern.page1.dispose();

				} else if (response == JOptionPane.YES_OPTION) {
					System.out.println("Yes button clicked");
					
					
							return true;

				} else if (response == JOptionPane.CLOSED_OPTION) {
					System.out.println("JOptionPane closed");
					return false;
				}

			}
*/
			return true;
		}
		if (getContainer().getCurrentPage() == page1
				|| getContainer().getCurrentPage() == page2) {

			return false;
		}

		else
			return true;
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}