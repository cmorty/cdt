package org.eclipse.remote.internal.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionType;

public class ServicePropertyTester extends PropertyTester {

	@Override
	@SuppressWarnings("unchecked")
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IRemoteConnection) {
			IRemoteConnection connection = (IRemoteConnection) receiver;
			if (property.equals("hasConnectionTypeService")) { //$NON-NLS-1$
				if (args.length > 0 && args[0] instanceof String) {
					String serviceName = (String) args[0];
					try {
						Class<?> service = Class.forName(serviceName);
						return connection.getConnectionType().hasService((Class<IRemoteConnectionType.Service>) service);
					} catch (ClassNotFoundException e) {
						return false;
					}
				}
			} else if (property.equals("hasConnectionService")) { //$NON-NLS-1$
				if (args.length > 0 && args[0] instanceof String) {
					String serviceName = (String) args[0];
					try {
						Class<?> service = Class.forName(serviceName);
						return connection.hasService((Class<IRemoteConnection.Service>) service);
					} catch (ClassNotFoundException e) {
						return false;
					}
				}
			} else if (property.equals("canDelete")) { //$NON-NLS-1$
				return (connection.getConnectionType().getCapabilities() & IRemoteConnectionType.CAPABILITY_REMOVE_CONNECTIONS) != 0;
			}
		}
		return false;
	}

}
