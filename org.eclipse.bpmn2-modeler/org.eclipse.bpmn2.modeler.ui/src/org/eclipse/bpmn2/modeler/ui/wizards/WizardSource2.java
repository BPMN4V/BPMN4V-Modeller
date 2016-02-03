package org.eclipse.bpmn2.modeler.ui.wizards;

import java.io.File;
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
import org.eclipse.graphiti.mm.pictograms.Shape;
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

public class WizardSource2 extends WizardPage implements IConstants {

	private final ISelection selection;
	private Label vprocess;
	private Label vide;
	private Label listTask;
	
	public Button button1;
	public Button button2;
	private boolean a;
	public static List list;
	public static List listId;
	private String dervivé = "";
	public static String activity_name = "";
	public static String activityName="";
	public static String activity_id = "", activity_type = "",processStableS2="";
	public static String newActivityNameSource;
	public static String existingActivityNameSource;
	public static Boolean newInteraction = false;
	public static Boolean existingInteraction = false;
	private Text containerText;
	public static String idprocess;
	public static String nameprocess;
	public static CreateContext context=null;
	public static Diagram diagram=null;
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public WizardSource2(ISelection selection) {
		super("wizardPageS2"); //$NON-NLS-1$
		setTitle("Select the source of interaction");
		setDescription("The source of interaction");
		this.selection = selection;
		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		newInteraction = false;

		container.setLayout(layout);
		Button button = new Button(container, SWT.BORDER);
	  	
		  
		button.setImage(Activator.getDefault().getImage(IConstants.ICON_S1));
			button.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true, 3, 1));

		// ligne1
		ContainerShape cp= context.getTargetContainer();
		Object b[]=
			Graphiti.getLinkService().getAllBusinessObjectsForLinkedPictogramElement(cp.getGraphicsAlgorithm().getPictogramElement());
		
		Participant p=(Participant) b[0];
		 
		
			System.out.println("24-05-2015llllll"+p.getProcessRef().getId());
			
			vprocess = new Label(container, SWT.NONE);
			vprocess.setText("Process and version of process");

			containerText = new Text(container, SWT.BORDER | SWT.SINGLE);

			containerText.setText(p.getProcessRef().getName()+"("+p.getProcessRef().getId()+")");
			nameprocess=p.getProcessRef().getName();
			idprocess=p.getProcessRef().getId();
			containerText.setEditable(false);
			Label	vide1 = new Label(container, SWT.NONE);
			vide1.setText("");
			vide1.setVisible(false);
			
		
		
			
		listTask = new Label(container, SWT.NONE);
		listTask.setText("List of interaction node");
		listTask.setVisible(true);
		SelectProcessActivity s = new SelectProcessActivity();
		s.SelectProcessTask();

		list = new List(container, SWT.BORDER | SWT.SCROLL_PAGE | SWT.V_SCROLL);
		listId = new List(container, SWT.BORDER | SWT.SCROLL_PAGE
				| SWT.V_SCROLL);
		GridData gridData = new GridData(120, 90);
		list.setLayoutData(gridData);
		listId.setLayoutData(gridData);
		list.setVisible(true);
		listId.setVisible(false);
		int i = 0;
/*	while (s.VersionActivitiesname[i] != null) {
			list.add(s.VersionActivitiesname[i]);
			listId.add(s.VersionActivitiesId[i]);
		i++;
		}*/
		Iterator<FlowElement> shape=p.getProcessRef().getFlowElements().iterator();
		
		while (shape.hasNext())
			
		{ FlowElement shape1=shape.next();
			System.out.println("26-05-2015shape"+shape1.getName());
			if (shape1 instanceof Task)
			{list.add(shape1.getName()+"("+shape1.getId()+")");
			listId.add(shape1.getId());}
			}
		

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
				
				int j=0;
				j= activity_name.indexOf("(");
				
				activityName=activity_name.substring(0,j);
				setPageComplete(true);

			}
		});

		setControl(container);
		
		
		
		UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();

		  String stateP = st.selectEtatProcess(idprocess); 

		  if(stateP.compareTo("Stable")==0 ) {

			  processStableS2=idprocess;
				UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
				String nameS2 = upm.selectNameProcess(p.getProcessRef().getId());
				
			  int response = JOptionPane
						.showConfirmDialog(null, "The version of process source of interaction: "+nameS2+"  is Stable. Do you want to derive it?",
								 "Derive the version of process before using Pattern", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					container.dispose();
				
				
				
				} else if (response == JOptionPane.YES_OPTION) {
					System.out.println("Yes button clicked");
					
					 String idS;
				  idS=  upm.SelectLastVProcess(nameS2);
				  containerText.setText(p.getProcessRef().getName() + "("
							+ idS + ")");
					
				
					
				} else if (response == JOptionPane.CLOSED_OPTION) {
					System.out.println("JOptionPane closed");
					container.dispose();
				} 
		  
		  }
	}

	
	

	 public boolean canFlipToNextPage() {
	
	 if (isPageComplete()) {
	
	 return true;
	 } else
	 return false;
	 }

	public IWizardPage getNextPage(IWizardPage WizardTarget2) {
		return WizardTarget2;
	}

}
