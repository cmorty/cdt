/*******************************************************************************
 * Copyright (c) 2015 QNX Software Systems and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.qt.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.launchbar.core.ILaunchDescriptor;

public interface IQtLaunchDescriptor extends ILaunchDescriptor {

	IProject getProject();

}