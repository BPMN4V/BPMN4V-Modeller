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

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.AbstractUpdateBaseElementFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;

public class UpdateTaskFeature extends AbstractUpdateBaseElementFeature {
	IFeatureProvider fp;
	public UpdateTaskFeature(IFeatureProvider fp) {
		
		super(fp);
		this.fp=fp;
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
	Object b=	fp.getBusinessObjectForPictogramElement(context.getPictogramElement());
	if (b instanceof Task)
	{
		System.out.println("11-*10-2015"+((Task)b).getId());
	}
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Task;
	}
}
