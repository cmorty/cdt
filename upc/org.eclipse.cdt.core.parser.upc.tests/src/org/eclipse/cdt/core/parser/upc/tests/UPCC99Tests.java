/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.core.parser.upc.tests;

import junit.framework.TestSuite;

import org.eclipse.cdt.core.dom.lrparser.BaseExtensibleLanguage;
import org.eclipse.cdt.core.dom.upc.UPCLanguage;
import org.eclipse.cdt.core.lrparser.tests.c99.C99Tests;
/**
 * Run the C99 tests against the UPC parser
 *
 */
public class UPCC99Tests extends C99Tests {

	public static TestSuite suite() {
    	return suite(UPCC99Tests.class);
    }
	
	public UPCC99Tests(String name) {
		super(name);
	}

	@Override
	protected BaseExtensibleLanguage getC99Language() {
		return UPCLanguage.getDefault();
	}
	
}
