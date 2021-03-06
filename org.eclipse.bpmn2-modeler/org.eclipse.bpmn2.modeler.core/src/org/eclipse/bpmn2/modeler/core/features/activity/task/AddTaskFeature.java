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
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.activity.task;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddActivityFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class AddTaskFeature<T extends Task> extends AbstractAddActivityFeature<T> {

	public AddTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
	
		return super.canAdd(context)
		        || BusinessObjectUtil.containsElementOfType(context.getTargetContainer(), FlowElementsContainer.class);
	}

	@Override
	protected void decorateShape(IAddContext context, ContainerShape containerShape, T businessObject) {
		Shape textShape = peService.createShape(containerShape, false);
		MultiText text = gaService.createDefaultMultiText(getDiagram(), textShape, businessObject.getName());
		gaService.setLocationAndSize(text, 0, 0, context.getWidth(), context.getHeight());
		StyleUtil.applyStyle(text, businessObject);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
//		text.setFont(gaService.manageFont(getDiagram(), GaServiceImpl.DEFAULT_FONT, 8, false, true));
		GraphicsAlgorithmContainer ga = getGraphicsAlgorithm(containerShape);
		IGaService service = Graphiti.getGaService();
		Image img2 = service.createImage(ga, "org.eclipse.bpmn2.modeler.icons.version.16");
		//System.out.print("ImageProvider.IMG_16_VERSIONS"+ImageProvider.IMG_16_VERSIONS);
		service.setLocationAndSize(img2, 80, 30, GraphicsUtil.TASK_IMAGE_SIZE, GraphicsUtil.TASK_IMAGE_SIZE);
		link(textShape, businessObject);
	}

	@Override
	public int getWidth() {
		return GraphicsUtil.getActivitySize(getDiagram()).getWidth();
//		return GraphicsUtil.TASK_DEFAULT_WIDTH;
	}

	@Override
	public int getHeight() {
		return GraphicsUtil.getActivitySize(getDiagram()).getHeight();
//		return GraphicsUtil.TASK_DEFAULT_HEIGHT;
	}
}