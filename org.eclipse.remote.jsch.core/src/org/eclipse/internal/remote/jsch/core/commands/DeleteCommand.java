package org.eclipse.internal.remote.jsch.core.commands;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.internal.remote.jsch.core.JSchConnection;
import org.eclipse.internal.remote.jsch.core.messages.Messages;
import org.eclipse.remote.core.exception.RemoteConnectionException;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class DeleteCommand extends AbstractRemoteCommand<Void> {

	private final IPath fRemotePath;

	public DeleteCommand(JSchConnection connection, IPath path) {
		super(connection);
		fRemotePath = path;
	}

	@Override
	public Void getResult(IProgressMonitor monitor) throws RemoteConnectionException {
		final SubMonitor subMon = SubMonitor.convert(monitor, 10);
		SftpCallable<Void> c = new SftpCallable<Void>() {
			@Override
			public Void call() throws JSchException, SftpException {
				getChannel().rm(fRemotePath.toString());
				return null;
			}
		};
		try {
			subMon.subTask(Messages.DeleteCommand_Remove_file);
			c.getResult(subMon.newChild(10));
		} catch (SftpException e) {
			throw new RemoteConnectionException(e.getMessage());
		}
		return null;
	}
}
