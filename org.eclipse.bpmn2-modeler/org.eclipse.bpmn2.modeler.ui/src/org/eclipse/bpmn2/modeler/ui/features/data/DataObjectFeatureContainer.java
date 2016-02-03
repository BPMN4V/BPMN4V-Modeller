/******************************************************************************* 
 * Copyright (c) 2011, 2012 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.impl.DataObjectImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.data.AddDataFeature;
import org.eclipse.bpmn2.modeler.core.features.label.UpdateLabelFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNToolBehaviorProvider;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;

public class DataObjectFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return o instanceof DataObject;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataObjectFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateDataObjectFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateLabelFeature(fp));
		return multiUpdate;
	}

	public class AddDataObjectFeature extends AddDataFeature<DataObject> {
		public AddDataObjectFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public String getName(DataObject t) {
			return t.getName();
		}
		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, DataObject businessObject) {
			final IFeatureProvider fp = getFeatureProvider();
			VersionDataObjectFeatureContainer k= new VersionDataObjectFeatureContainer(fp);
			k.decorateShape(context, containerShape, businessObject);
		}
	}

	public static class CreateDataObjectFeature extends AbstractCreateFlowElementFeature<FlowElement> {

		private static ILabelProvider labelProvider = new ILabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {

			}

			public void addListener(ILabelProviderListener listener) {

			}

			public String getText(Object element) {
				if (((DataObject) element).getId() == null)
					return ((DataObject) element).getName();
				return NLS.bind(Messages.DataObjectFeatureContainer_Ref, ((DataObject) element).getName());
			}

			public Image getImage(Object element) {
				return null;
			}

		};

		public CreateDataObjectFeature(IFeatureProvider fp) {
			super(fp, Messages.DataObjectFeatureContainer_Name, Messages.DataObjectFeatureContainer_Description);
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			return FeatureSupport.isValidDataTarget(context);
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_OBJECT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataObject();
		}

		@Override
		public FlowElement createBusinessObject(ICreateContext context) {
			String state=Selectstate(getDiagram().getName());
			//System.out.println("0710/2015"+getDiagram().getName());
			if (state.compareTo("Working")==0)
			{changesDone = true;
			FlowElement bo = null;
			try {
				DataObjectReference dataObjectReference = null;
				DataObject dataObject = null;
				ModelHandler mh = ModelHandler.getInstance(getDiagram());
				dataObjectReference = Bpmn2ModelerFactory.create(DataObjectReference.class);
				dataObject = Bpmn2ModelerFactory.create(DataObject.class);
				String oldName = dataObject.getName();
				dataObject.setName(Messages.DataObjectFeatureContainer_New);
				dataObject.setId(null);
				EObject targetBusinessObject = (EObject)getBusinessObjectForPictogramElement(context.getTargetContainer());
				
				// NOTE: this code removed. A DataObjectReference may reference DataObjects
				// from any other container (Process) in the BPMN file, not just those defined
				// within the same container as the target (context.getTargetContainer())
//				Object containerBO = container;
//				if (container instanceof BPMNDiagram) {
//					containerBO = ((BPMNDiagram)container).getPlane().getBpmnElement();
//				}
//				if (container instanceof Lane) {
//					EObject laneSet = ((Lane)container).eContainer();
//					if (laneSet instanceof LaneSet)
//						containerBO = laneSet.eContainer();
//				}

				List<DataObject> dataObjectList = new ArrayList<DataObject>();
				dataObjectList.add(dataObject);
				TreeIterator<EObject> iter = ModelUtil.getDefinitions(targetBusinessObject).eAllContents();
				while (iter.hasNext()) {
					EObject obj = iter.next();
					if (obj instanceof DataObject) // removed, see above: && obj.eContainer() == containerBO)
						dataObjectList.add((DataObject) obj);
				}

				DataObject result = dataObject;
				if (dataObjectList.size() > 1) {
					PopupMenu popupMenu = new PopupMenu(dataObjectList, labelProvider);
					changesDone = popupMenu.show(Display.getCurrent().getActiveShell());
					if (changesDone) {
						result = (DataObject) popupMenu.getResult();
					}
					else {
						EcoreUtil.delete(dataObject);
						EcoreUtil.delete(dataObjectReference);
					}
				}
				if (changesDone) {
					if (result == dataObject) { // the new one
						mh.addFlowElement(targetBusinessObject,dataObject);
						ModelUtil.setID(dataObject);
						dataObject.setIsCollection(false);
						dataObject.setName(oldName);
						bo = dataObject;
					} else {
						mh.addFlowElement(targetBusinessObject,dataObjectReference);
						ModelUtil.setID(dataObjectReference);
						dataObjectReference.setName(
							NLS.bind(
								Messages.DataObjectFeatureContainer_Default_Name,
								result.getName()
							)
						);
						dataObjectReference.setDataObjectRef(result);
						dataObject = result;
						bo = dataObjectReference;
					}
					putBusinessObject(context, bo);
				}

			} catch (IOException e) {
				Activator.showErrorWithLogging(e);
			}
			return bo;}
			else
			{JOptionPane.showMessageDialog(null, "Operation is not allowed because the version of process "+ BPMNToolBehaviorProvider.id_v+" is Stable","Error" ,JOptionPane.ERROR_MESSAGE);
			
			return null;}
		}
		 public static String Selectstate(String id_v)
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
		    IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for $o in doc('Processes.xml')/Processes/Process for $i in $o/versions/version where $i/id_v='"+id_v+"' return $i/state");
		  //  IterableIterator<? extends XhiveXQueryValueIf>  result = rootLibrary.executeXQuery("for  $p in fn:doc('Processes.xml')/Processes/Process  let $i:=<version></version> where $p/name='"+name+"' return insert nodes  $i into $p/versions");
		    while(result.hasNext()) {  
		 	   s2=result.next().toString();
		 	   System.out.println(j+"s2"+s2);
		 	   j++;
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
		
	}
}