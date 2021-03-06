/*  Copyright (C) 2009 Mobile Sorcery AB

    This program is free software; you can redistribute it and/or modify it
    under the terms of the Eclipse Public License v1.0.

    This program is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License v1.0 for
    more details.

    You should have received a copy of the Eclipse Public License v1.0 along
    with this program. It is also available at http://www.eclipse.org/legal/epl-v10.html
*/
package com.mobilesorcery.sdk.builder.s60.ui.properties;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.mobilesorcery.sdk.builder.s60.ui.properties.messages"; //$NON-NLS-1$
	public static String SymbianPropertyPage_GenerateTestUIDs;
	public static String SymbianPropertyPage_S60_UIDs;
	public static String SymbianPropertyPage_S60V2;
	public static String SymbianPropertyPage_S60V2_UID;
	public static String SymbianPropertyPage_S60V3;
	public static String SymbianPropertyPage_S60V3_UID;
	public static String SymbianPropertyPage_UIDPrefixError;
	public static String SymbianPropertyPage_UIDRangeError;
	public static String SymbianSigningPropertyPage_CertificateFile;
	public static String SymbianSigningPropertyPage_EnableProjectSpecific;
	public static String SymbianSigningPropertyPage_KeyFile;
	public static String SymbianSigningPropertyPage_Passkey;
	public static String SymbianSigningPropertyPage_Signing;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
