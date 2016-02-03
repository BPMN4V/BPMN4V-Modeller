package org.eclipse.bpmn2.modeler.ui.wizards;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
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

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class WizardTarget extends WizardPage implements IConstants {

	private final ISelection selection;
	private Label vprocess;
	private Label vide;
	private Label vide1;
	private Label listTask;
	private Label versionTask;
	private Label nameTask;
	public static Label iddt;
	private Combo containerText;
	private Text nameText;
	public Button button1;
	public Button button2;
	private boolean a;
	private Tree VH = null;
	private TreeItem item = null;
	private TreeItem subitem = null;
	public List list;
	private String dervivé = "";
	private Boolean nextText = false;
	public static String activityNameTarget;
	public static String existingActivityNameTarget;
	public static String activity_name = "", activity_id = "",
			activity_type = "";
	public static Boolean newInteraction = false;
	public static String idt;
	public static Boolean afficheFinish = false;
	public static CreateContext context = null;
	public static Diagram diagram = null;
	public static Participant participant;
	public static String idvp;
	private String processStableT;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public WizardTarget(ISelection selection) {
		super("wizardPageT"); //$NON-NLS-1$
		setTitle("Create or Select the target of interaction");
		setDescription("The target of interaction");
		this.selection = selection;
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

		final Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		nextText = false;
		newInteraction = false;
		afficheFinish = false;
		container.setLayout(layout);
		Button button = new Button(container, SWT.BORDER);
	  	
		  
		button.setImage(Activator.getDefault().getImage(IConstants.ICON_S2));
			button.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true, 3, 1));
		ContainerShape cp = context.getTargetContainer();
		PictogramElement sourcep = cp.getGraphicsAlgorithm()
				.getPictogramElement();
		System.out.println("cp.getGraphicsAlgorithm().getPictogramElement()"
				+ cp.getGraphicsAlgorithm().getPictogramElement());
		Object b[] = Graphiti.getLinkService()
				.getAllBusinessObjectsForLinkedPictogramElement(
						cp.getGraphicsAlgorithm().getPictogramElement());
		int i = 0;
		Participant p = (Participant) b[0];

		Collection<PictogramElement> c = Graphiti.getPeService()
				.getAllContainedPictogramElements(
						diagram.getGraphicsAlgorithm().getPictogramElement());
		Iterator<PictogramElement> ii = c.iterator();
		PictogramElement[] ciblep = new PictogramElement[4];
		while (ii.hasNext())

		{
			PictogramElement pp = ii.next();
			System.out.println("26-05-2015pp.to" + pp.toString());
			if (pp.toString().contains("ContainerShapeImpl") && sourcep != pp
					&& pp.eContainer() == cp.eContainer()) {
				System.out
						.println("ii.next().getGraphicsAlgorithm().getPictogramElement()"
								+ pp.getGraphicsAlgorithm()
										.getPictogramElement());
				ciblep[i] = pp;
				i++;
			}
		}
		// ligne1
		vprocess = new Label(container, SWT.NONE);
		vprocess.setText("Process and version of process");
		// Graphiti.getLinkService().
		containerText = new Combo(container, SWT.BORDER);
		int j = 0;
		final Participant[] par = new Participant[4];
		while (ciblep[j] != null) {
			Object bp[] = Graphiti.getLinkService()
					.getAllBusinessObjectsForLinkedPictogramElement(ciblep[j]);
			System.out.println("25-05-2015ffffffffffff" + bp[0].toString());
			if (bp[0].toString().contains("Participant_")) {
				Participant pt = (Participant) bp[0];
				containerText.add(pt.getProcessRef().getName() + "("
						+ pt.getProcessRef().getId() + ")");
				par[j] = pt;
			}
			j++;
		}
		// containerText.setEditable(false);
		containerText.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				// containerText.sets par[0];
				// containerText.setText(par[0].getName()+"("+par[0].getId()+")");
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				participant = par[containerText.getSelectionIndex()];
				idvp = participant.getProcessRef().getId();
				// System.out.println("25-05-2015"+participant.getId());
				
				
				UpdateMessageFlowNumber st = new UpdateMessageFlowNumber();
				String stateP = st.selectEtatProcess(idvp);
				if (stateP.compareTo("Stable") == 0) {

					processStableT = idvp;
					UpdateMessageFlowNumber upm = new UpdateMessageFlowNumber();
					String nameT = upm.selectNameProcess(idvp);
				
					int response = JOptionPane
							.showConfirmDialog(
									null,
									"The version of process target of interaction: "+nameT+"  is Stable. Do you want to derive it?",
									 "Derive the version of process before using Pattern",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) {
					
				container.dispose();
					

					} else if (response == JOptionPane.YES_OPTION) {
						System.out.println("Yes button clicked");
						//Changer l'affichage de l'id du processus en cas de dérivation
					
						 String idT;
					  idT=  upm.SelectLastVProcess(nameT);
					  containerText.setText(nameT + "("
								+ idT + ")");
						
							//	return true;

					} else if (response == JOptionPane.CLOSED_OPTION) {
						System.out.println("JOptionPane closed");
						//return false;
					}

				}

			}

		}

		);

		iddt = new Label(container, SWT.NONE);
		idt = selectTaskId();
		iddt.setText(idt);
		UpdateTaskId(idt);
		iddt.setVisible(false);

		// ligne2

		vide1 = new Label(container, SWT.NONE);
		vide1.setText("");
		vide1.setVisible(false);

		Group buttonGroup = new Group(container, SWT.NONE);
		buttonGroup.setLayout(layout);
		buttonGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Button button = ((Button) event.widget);

			};
		};

		Button button1 = new Button(buttonGroup, SWT.RADIO);
		button1.setText("New interaction node");
		button1.addSelectionListener(selectionListener);
		Button button2 = new Button(buttonGroup, SWT.RADIO);
		button2.setText("Existing interaction node");
		button2.addSelectionListener(selectionListener);

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		// ligne3

		nameTask = new Label(container, SWT.NONE);
		nameTask.setText("Interaction node name");
		nameTask.setVisible(false);

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setText("");
		nameText.setEditable(true);
		nameText.setVisible(false);

		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				System.out.println("Text after modification: "
						+ nameText.getText());
				nextText = true;
				activityNameTarget = nameText.getText();
				newInteraction = true;
				afficheFinish = true;
				// setPageComplete(canFlipToNextPage());
				setPageComplete(true);
				// performFinish();
			}
		});

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		// ligne4

		listTask = new Label(container, SWT.NONE);
		listTask.setText("Interaction node name");
		listTask.setVisible(false);

		list = new List(container, SWT.BORDER | SWT.SCROLL_PAGE | SWT.V_SCROLL);
		GridData gridData = new GridData(120, 90);
		list.setLayoutData(gridData);

		select_activity_name();
		list.setVisible(false);

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		// ligne5

		versionTask = new Label(container, SWT.NONE);
		versionTask.setText("Versions of interaction node");
		versionTask.setVisible(false);

		VH = new Tree(container, SWT.SCROLL_PAGE | SWT.BORDER | SWT.H_SCROLL);
		VH.setLayout(layout);
		VH.setSize(120, 90);
		VH.setVisible(false);

		VH.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				newInteraction = false;
				existingActivityNameTarget = VH.getSelection()[0].getText();
				afficheFinish = true;
				setPageComplete(true);

			}
		});

		vide = new Label(container, SWT.NONE);
		vide.setText("");
		vide.setVisible(false);

		list.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				activity_name = list.getItem(list.getSelectionIndex());
				VH.removeAll();
				select_activity_version(list.getItem(list.getSelectionIndex()));

			}
		});

		button1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				nextText = false;
				newInteraction = true;
				nameTask.setVisible(true);
				nameText.setVisible(true);
				listTask.setVisible(false);
				list.setVisible(false);
				versionTask.setVisible(false);
				VH.setVisible(false);

			}

		});

		button2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				newInteraction = false;
				nextText = false;
				listTask.setVisible(true);
				list.setVisible(true);
				versionTask.setVisible(true);
				VH.setVisible(true);
				nameTask.setVisible(false);
				nameText.setVisible(false);

			}

		});

		newInteraction = false;

		setControl(container);
	}

	// @Override
	// public boolean isPageComplete() {
	//
	// if (nextText = false) {
	// return false;
	// } else
	// return true;
	// }

	// public boolean canFlipToNextPage() {
	//
	// return false;
	// }

	// public boolean PerformFinish() {
	// // return true;
	// if (nextText = true && isPageComplete()) {
	// return true;
	// } else
	// return false;
	//
	// }

	public void select_activity_name() {
		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
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
					.executeXQuery(" for  $p in fn:doc('Activities.xml')/Activities/Activity  return  $p/name");
			while (result.hasNext()) {
				s2 = result.next().toString();
				s2 = s2.substring(6, s2.length() - 7);

				list.add(s2);
			}
			session.commit();
		} finally {
			session.rollback();
		}

	}

	public void select_activity_version(String s) {

		XhiveDriverIf driver = XhiveDriverFactory
				.getDriver("xhive://localhost:1235");
		int i = 0;
		if (driver.isInitialized() == false)
			driver.init();

		XhiveSessionIf session = driver.createSession("xqapi-test");
		session.connect("Administrator", "imen", "vbpmn");
		session.begin();
		String s1 = "";
		try {
			XhiveLibraryIf rootLibrary = session.getDatabase().getRoot();
			// (1)

			IterableIterator<? extends XhiveXQueryValueIf> result = rootLibrary
					.executeXQuery("for $a in doc('Activities.xml')//Activity where $a/name='"
							+ s
							+ "' return <id_v> {$a/versions/version/id_v} </id_v>");
			String str0[] = new String[10];
			String str1[] = new String[10];

			int j = 0;
			int l = 0;
			while (result.hasNext()) {
				s1 = result.next().toString();

				while (s1.indexOf("VA") != -1) {
					j = s1.indexOf("VA");
					s1 = s1.substring(j);

					j = s1.indexOf("<");
					str0[l] = s1.substring(0, j);

					l++;
					s1 = s1.substring(j);

				}
			}
			int k = 0;
			while (str0[k] != null) {
				System.out.println("versions" + str0[k]);

				IterableIterator<? extends XhiveXQueryValueIf> result1 = rootLibrary
						.executeXQuery("for $a in doc('Activities.xml')//Activity/versions/version where $a/id_v='"
								+ str0[k]
								+ "' return <id> {$a/derived_from/id_vd} </id>");

				while (result1.hasNext()) {

					String s2 = result1.next().toString();
					j = 0;
					l = 0;

					while (s2.indexOf("<id_vd>") != -1) {
						j = s2.indexOf("<id_vd>");
						System.out.println("j " + j);
						s2 = s2.substring(j + 7);
						System.out.println("s2" + s2);
						j = s2.indexOf("<");
						dervivé = s2.substring(0, j);
						l++;
						s2 = s2.substring(j);

					}
					System.out.println("dervivé " + dervivé);
					if (dervivé.compareTo("nil") == 0) {

						item = new TreeItem(VH, SWT.NONE);

						item.setText(str0[k]);
					} else

						createchild2(VH.getItems(), dervivé, str0[k]);

				}
				k++;
			}

			session.commit();
		} finally {
			session.rollback();
		}

	}

	public void createchild(Tree t, String derf, String der) {
		TreeItem[] items;
		items = t.getItems();

		boolean trouve = false;
		int r = 0;
		for (int i = 0; i < items.length; i++)
			System.out.println("items" + items[i].getText());
		while (trouve == false && r < items.length) {

			if (items[r].getText().compareTo(derf) == 0) {
				System.out.println("items" + items[r].getText());
				subitem = new TreeItem(items[r], SWT.NONE);
				subitem.setText(der);
				trouve = true;
			}
			r++;
		}

	}

	public void createchild2(TreeItem[] items, String derf, String der) {
		if (items.length == 0)
			return;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText().compareTo(derf) == 0) {
				subitem = new TreeItem(items[i], SWT.NONE);
				subitem.setText(der);
				return;

			}
			createchild2(items[i].getItems(), derf, der);
		}

	}

	public static String selectTaskId() {
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
					.executeXQuery("for  $a in fn:doc('Last_activity.xml')/Activities return $a/id");

			while (result.hasNext()) {
				s2 = result.next().toString();
				j = s2.indexOf("<id>");
				s2 = s2.substring(j + 4);
				j = s2.indexOf("</id>");
				s2 = s2.substring(0, j);
			}

			session.commit();
		} finally {
			session.rollback();
		}
		return s2;

	}

	public static void UpdateTaskId(String id) {
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
					.executeXQuery("for  $a in fn:doc('Last_activity.xml')/Activities let $o:='"
							+ j
							+ "' return replace value of node $a/id with $o");

			session.commit();
		} finally {
			session.rollback();
		}

	}
}
