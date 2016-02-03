package org.eclipse.bpmn2.modeler.ui.wizards;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.Create_activity;
import org.eclipse.bpmn2.modeler.ui.diagram.Create_version_activity;
import org.eclipse.bpmn2.modeler.ui.diagram.SelectActivityInformationDialog;
import org.eclipse.bpmn2.modeler.ui.diagram.UpdateVersionActivity;
import org.eclipse.draw2d.ButtonGroup;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class WizardTarget3 extends WizardPage implements IConstants {

	private final ISelection selection;
	private Label vide;
	private Label vprocess;
	private Label listTask;
	public Button button1;
	public Button button2;
	private Combo containerText;
	public static List list;
	public static List listId;
	public static String activity_name = "";
	public static String activity_id = "", activity_type = "";
	public static String newActivityNameSource;
	public static String existingActivityNameSource;
	public static Boolean existingInteraction = false;
	public static Boolean afficheFinishT3 = false;
	public static CreateContext context=null;
	public static Diagram diagram=null;
public static Participant participant;
public static String idvp;
private String processStableT3;
public static String nameProcess;

	public WizardTarget3(ISelection selection) {
		super("wizardPageT3"); //$NON-NLS-1$
		setTitle("Select the target of interaction");
		setDescription("The target of interaction");
		this.selection = selection;
		
	
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		afficheFinishT3=false;
		setPageComplete(false);
		Button button = new Button(container, SWT.BORDER);
	  	
		  
		button.setImage(Activator.getDefault().getImage(IConstants.ICON_S2));
			button.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true, 3, 1));
		
		// ligne1
		ContainerShape cp= context.getTargetContainer();
		PictogramElement sourcep=cp.getGraphicsAlgorithm().getPictogramElement();
		//System.out.println("cp.getGraphicsAlgorithm().getPictogramElement()"+cp.getGraphicsAlgorithm().getPictogramElement());
		Object b[]=
			Graphiti.getLinkService().getAllBusinessObjectsForLinkedPictogramElement(cp.getGraphicsAlgorithm().getPictogramElement());
		int i=0;
		Participant p=(Participant) b[0];
	
		Collection<PictogramElement> c=	Graphiti.getPeService().getAllContainedPictogramElements(diagram.getGraphicsAlgorithm().getPictogramElement());
	Iterator<PictogramElement> ii=	c.iterator();
	PictogramElement[] ciblep = new PictogramElement[4] ;
		while (ii.hasNext())
			
		{PictogramElement pp=ii.next();
		//System.out.println("26-05-2015pp.to"+ pp.toString());
		if (pp.toString().contains("ContainerShapeImpl") && sourcep!=pp &&pp.eContainer()==cp.eContainer())
			{//System.out.println("ii.next().getGraphicsAlgorithm().getPictogramElement()"+pp.getGraphicsAlgorithm().getPictogramElement());
	ciblep[i]=pp;
	i++;}
		}
		// ligne1
		vprocess = new Label(container, SWT.NONE);
		vprocess.setText("Process and version of process");
//Graphiti.getLinkService().
		containerText = new Combo(container, SWT.BORDER);
		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		listTask = new Label(container, SWT.NONE);
		listTask.setText("List of interaction node");
		listTask.setVisible(true);
		list = new List(container, SWT.BORDER | SWT.SCROLL_PAGE | SWT.V_SCROLL);
		listId = new List(container, SWT.BORDER | SWT.SCROLL_PAGE
				| SWT.V_SCROLL);
		GridData gridData = new GridData(120, 90);
		list.setLayoutData(gridData);
		listId.setLayoutData(gridData);

		list.setVisible(true);
		listId.setVisible(false);
int j=0;
final Participant [] par =new Participant [4];
while (ciblep[j]!=null)
{ 
	Object bp[]=
			Graphiti.getLinkService().getAllBusinessObjectsForLinkedPictogramElement(ciblep[j]);
	//System.out.println("25-05-2015ffffffffffff"+bp[0].toString());
	if (bp[0].toString().contains("Participant_"))
		{Participant pt=(Participant) bp[0];
		containerText.add(pt.getProcessRef().getName()+"("+pt.getProcessRef().getId()+")");
		par[j]=pt;}
		j++;
	}
		//containerText.setEditable(false);
containerText.addSelectionListener(new SelectionListener(){

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		//containerText.sets par[0];
	//containerText.setText(par[0].getName()+"("+par[0].getId()+")");
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		participant =par[containerText.getSelectionIndex()];
		idvp=participant.getProcessRef().getId();
		
		nameProcess=participant.getProcessRef().getName();
		
		
		
		UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
		String stateP = st.selectEtatProcess(idvp);
		if (stateP.compareTo("Stable") == 0) {

			processStableT3 = idvp;
			UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
			String nameT3 = upm.selectNameProcess(idvp);
		
			int response = JOptionPane
					.showConfirmDialog(
							null,
							"The version of process target of interaction: "+nameT3+"  is Stable. Do you want to derive it?",
							 "Derive the version of process before using Pattern",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
			
		container.dispose();
			

			} else if (response == JOptionPane.YES_OPTION) {
				System.out.println("Yes button clicked");
				//Changer l'affichage de l'id du processus en cas de dérivation
			
				 String idT;
			  idT=  upm.SelectLastVProcess(nameT3);
			  containerText.setText(nameT3 + "("
						+ idT + ")");
				
					//	return true;

			} else if (response == JOptionPane.CLOSED_OPTION) {
				System.out.println("JOptionPane closed");
				//return false;
			}

		}
		
	Iterator<FlowElement> ii=	participant.getProcessRef().getFlowElements().iterator();
	while (ii.hasNext())
		{FlowElement f=ii.next();
		if (f instanceof Task)
		{list.add(f.getName());
		listId.add(f.getId());}
		}
	
		//System.out.println("25-05-2015"+participant.getId());
		
	}
	
});

		
		

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		// ligne2

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		list.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				activity_name = list.getItem(list.getSelectionIndex());
				activity_id = listId.getItem(list.getSelectionIndex());
				afficheFinishT3=true;
				setPageComplete(true);

			}
		});

		setControl(container);
	}

	public boolean performFinish() {
		if (isPageComplete()) {
		return true;
	} else
			return false;
	}

	public boolean canFlipToNextPage() {

		return false;
	}



}

