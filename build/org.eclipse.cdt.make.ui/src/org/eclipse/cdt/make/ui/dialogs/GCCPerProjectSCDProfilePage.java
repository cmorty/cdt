/***********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 ***********************************************************************/
package org.eclipse.cdt.make.ui.dialogs;

import java.io.File;

import org.eclipse.cdt.internal.ui.util.SWTUtil;
import org.eclipse.cdt.make.core.scannerconfig.IScannerConfigBuilderInfo2;
import org.eclipse.cdt.make.internal.core.scannerconfig.jobs.BuildOutputReaderJob;
import org.eclipse.cdt.make.internal.ui.MakeUIPlugin;
import org.eclipse.cdt.utils.ui.controls.ControlFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * SCD profile property/preference page
 * 
 * @author vhirsl
 */
public class GCCPerProjectSCDProfilePage extends AbstractDiscoveryPage {
    private static final int DEFAULT_HEIGHT = 160;

    private static final String PREFIX = "ScannerConfigOptionsDialog"; //$NON-NLS-1$
    private static final String BO_PROVIDER_GROUP_LABEL = PREFIX + ".boProvider.group.label"; //$NON-NLS-1$
    private static final String BO_PROVIDER_PARSER_ENABLED_BUTTON = PREFIX + ".boProvider.parser.enabled.button"; //$NON-NLS-1$
    private static final String BO_PROVIDER_OPEN_LABEL = PREFIX + ".boProvider.open.label"; //$NON-NLS-1$
    private static final String BO_PROVIDER_BROWSE_BUTTON = PREFIX + ".boProvider.browse.button"; //$NON-NLS-1$
    private static final String BO_PROVIDER_OPEN_FILE_DIALOG = PREFIX + ".boProvider.browse.openFileDialog"; //$NON-NLS-1$
    private static final String BO_PROVIDER_LOAD_BUTTON = PREFIX + ".boProvider.load.button"; //$NON-NLS-1$
    private static final String SI_PROVIDER_PARSER_ENABLED_BUTTON = PREFIX + ".siProvider.parser.enabled.button"; //$NON-NLS-1$
    private static final String SI_PROVIDER_COMMAND_LABEL = PREFIX + ".siProvider.command.label"; //$NON-NLS-1$
    private static final String SI_PROVIDER_BROWSE_BUTTON = PREFIX + ".siProvider.browse.button"; //$NON-NLS-1$
    private static final String SI_PROVIDER_COMMAND_DIALOG = PREFIX + ".siProvider.browse.runCommandDialog"; //$NON-NLS-1$
    private static final String SI_PROVIDER_COMMAND_ERROR_MESSAGE= PREFIX + ".siProvider.command.errorMessage"; //$NON-NLS-1$
    
    private static final String providerId = "specsFile";  //$NON-NLS-1$
    
    // thread syncronization
    //private static ILock lock = Platform.getJobManager().newLock();
//    private static Object lock = new Object();
    private static Object lock = GCCPerProjectSCDProfilePage.class;
    private Shell shell;
    private static GCCPerProjectSCDProfilePage instance;
    private static boolean loadButtonInitialEnabled = true;
    
    private Button bopEnabledButton;
    private Text bopOpenFileText;
    private Button bopLoadButton;
    private Button sipEnabledButton;
    private Text sipRunCommandText;
    
    private boolean isValid = true;

    /* (non-Javadoc)
     * @see org.eclipse.cdt.ui.dialogs.AbstractCOptionPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        // Add the profile UI contribution.
        Group profileGroup = ControlFactory.createGroup(parent,
                MakeUIPlugin.getResourceString(BO_PROVIDER_GROUP_LABEL), 3);
        
        GridData gd = (GridData) profileGroup.getLayoutData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = GridData.FILL;
//        PixelConverter converter = new PixelConverter(parent);
//        gd.heightHint = converter.convertHorizontalDLUsToPixels(DEFAULT_HEIGHT);
        ((GridLayout) profileGroup.getLayout()).makeColumnsEqualWidth = false;
        
        // Add bop enabled checkbox
        bopEnabledButton = ControlFactory.createCheckBox(profileGroup,
                MakeUIPlugin.getResourceString(BO_PROVIDER_PARSER_ENABLED_BUTTON));
//        bopEnabledButton.setFont(parent.getFont());
        ((GridData)bopEnabledButton.getLayoutData()).horizontalSpan = 3;
        ((GridData)bopEnabledButton.getLayoutData()).grabExcessHorizontalSpace = true;
        bopEnabledButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                handleModifyOpenFileText();
            }
            
        });
        
        // load label
        Label loadLabel = ControlFactory.createLabel(profileGroup,
                MakeUIPlugin.getResourceString(BO_PROVIDER_OPEN_LABEL));
        ((GridData) loadLabel.getLayoutData()).horizontalSpan = 3;

        // text field
        bopOpenFileText = ControlFactory.createTextField(profileGroup, SWT.SINGLE | SWT.BORDER);
        bopOpenFileText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                handleModifyOpenFileText();
            }
        });
        
        // browse button
        Button browseButton = ControlFactory.createPushButton(profileGroup,
                MakeUIPlugin.getResourceString(BO_PROVIDER_BROWSE_BUTTON));
        ((GridData) browseButton.getLayoutData()).widthHint = 
                SWTUtil.getButtonWidthHint(browseButton);
        browseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                handleBOPBrowseButtonSelected();
            }

            private void handleBOPBrowseButtonSelected() {
                FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
                dialog.setText(MakeUIPlugin.getResourceString(BO_PROVIDER_OPEN_FILE_DIALOG)); //$NON-NLS-1$
                String fileName = bopOpenFileText.getText().trim();
                IPath filterPath;
                if (fileName.length() == 0 && getContainer().getProject() != null) {
                	filterPath = getContainer().getProject().getLocation();
                }
                else {
                    IPath filePath = new Path(fileName);
                    filterPath = filePath.removeLastSegments(1).makeAbsolute();
                }
                dialog.setFilterPath(filterPath.toOSString());
                String res = dialog.open();
                if (res == null) {
                    return;
                }
                bopOpenFileText.setText(res);
            }
        });

        // load button
        bopLoadButton = ControlFactory.createPushButton(profileGroup,
                MakeUIPlugin.getResourceString(BO_PROVIDER_LOAD_BUTTON));
        ((GridData) bopLoadButton.getLayoutData()).widthHint = 
                SWTUtil.getButtonWidthHint(bopLoadButton);
        bopLoadButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent event) {
                handleBOPLoadFileButtonSelected();
            }

        });
        bopLoadButton.setEnabled(loadButtonInitialEnabled);
        if (getContainer().getProject() == null) {  // project properties
            bopLoadButton.setVisible(false);
        }
        
        ControlFactory.createSeparator(profileGroup, 3);
        
        // si provider enabled checkbox
        sipEnabledButton = ControlFactory.createCheckBox(profileGroup,
                MakeUIPlugin.getResourceString(SI_PROVIDER_PARSER_ENABLED_BUTTON));
//        sipEnabledButton.setFont(parent.getFont());
        ((GridData)sipEnabledButton.getLayoutData()).horizontalSpan = 3;
        ((GridData)sipEnabledButton.getLayoutData()).grabExcessHorizontalSpace = true;
        sipEnabledButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
//                bopLoadButton.setEnabled(sipEnabledButton.getSelection());
            }
            
        });
        
        // si command label
        Label siCommandLabel = ControlFactory.createLabel(profileGroup,
                MakeUIPlugin.getResourceString(SI_PROVIDER_COMMAND_LABEL));
        ((GridData) siCommandLabel.getLayoutData()).horizontalSpan = 3;

        // text field
        sipRunCommandText = ControlFactory.createTextField(profileGroup, SWT.SINGLE | SWT.BORDER);
        //((GridData) sipRunCommandText.getLayoutData()).horizontalSpan = 2;
        sipRunCommandText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                handleModifyRunCommandText();
            }
        });
        
        // si browse button
        Button siBrowseButton = ControlFactory.createPushButton(profileGroup,
                MakeUIPlugin.getResourceString(SI_PROVIDER_BROWSE_BUTTON));
        ((GridData) siBrowseButton.getLayoutData()).widthHint = 
                SWTUtil.getButtonWidthHint(browseButton);
        siBrowseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                handleSIPBrowseButtonSelected();
            }

            private void handleSIPBrowseButtonSelected() {
                FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
                dialog.setText(MakeUIPlugin.getResourceString(SI_PROVIDER_COMMAND_DIALOG)); //$NON-NLS-1$
                String fileName = sipRunCommandText.getText().trim();
                int lastSeparatorIndex = fileName.lastIndexOf(File.separator);
                if (lastSeparatorIndex != -1) {
                    dialog.setFilterPath(fileName.substring(0, lastSeparatorIndex));
                }
                String res = dialog.open();
                if (res == null) {
                    return;
                }
                sipRunCommandText.setText(res);
            }
        });


        setControl(parent);
        // set the shell variable; must be after setControl
        //lock.acquire();
        synchronized (lock) {
            shell = getShell();
            instance = this;
        }
        //lock.release();
        initializeValues();
    }

    /**
     * 
     */
    protected void handleModifyOpenFileText() {
        String fileName = bopOpenFileText.getText().trim();
        bopLoadButton.setEnabled(bopEnabledButton.getSelection() &&
                                 fileName.length() > 0 &&
                                 new File(fileName).exists());
    }

    /**
     * 
     */
    protected void handleModifyRunCommandText() {
        String cmd = sipRunCommandText.getText().trim();
        isValid = (cmd.length() > 0) ? true : false;

        getContainer().updateContainer();
    }

    /**
     * 
     */
    private void initializeValues() {
        bopEnabledButton.setSelection(getBuildInfo().isBuildOutputParserEnabled());
        bopOpenFileText.setText(getBuildInfo().getBuildOutputFilePath());
        sipEnabledButton.setSelection(getBuildInfo().isProviderOutputParserEnabled(providerId));
        sipRunCommandText.setText(getBuildInfo().getProviderRunCommand(providerId));
    }

    private void handleBOPLoadFileButtonSelected() {
        loadButtonInitialEnabled = false;
        bopLoadButton.setEnabled(false);
        
        // populate buildInfo to be used by the reader job
        populateBuildInfo(getBuildInfo(), null);
        IProject project = getContainer().getProject();
        Job readerJob = new BuildOutputReaderJob(project, getBuildInfo());
        readerJob.setPriority(Job.LONG);
        readerJob.addJobChangeListener(new JobChangeAdapter() {
            
            public void done(IJobChangeEvent event) {
                //lock.acquire();
                synchronized (lock) {
                    if (!instance.shell.isDisposed()) {
                        instance.shell.getDisplay().asyncExec(new Runnable() {
        
                            public void run() {
                                if (!instance.shell.isDisposed()) {
                                    instance.bopLoadButton.setEnabled(instance.bopEnabledButton.getSelection());
                                }
                                loadButtonInitialEnabled = instance.bopEnabledButton.getSelection();//true;
                            }
                            
                        });
                    }
                    else {
                        loadButtonInitialEnabled = instance.bopEnabledButton.getSelection();//true;
                    }
                }
                //lock.release();
            }
            
        });
        readerJob.schedule();
    }

    /* (non-Javadoc)
     * @see org.eclipse.cdt.ui.dialogs.ICOptionPage#isValid()
     */
    public boolean isValid() {
        return isValid;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#getErrorMessage()
     */
    public String getErrorMessage() {
        return (isValid) ? null : MakeUIPlugin.getResourceString(SI_PROVIDER_COMMAND_ERROR_MESSAGE);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.cdt.ui.dialogs.AbstractCOptionPage#performApply(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void performApply(IProgressMonitor monitor) throws CoreException {
        IProject project = getContainer().getProject();
        // Create new build info in case of new C++ project wizard
        IScannerConfigBuilderInfo2 buildInfo = createBuildInfo(project);
        
        if (buildInfo != null) {
            populateBuildInfo(buildInfo, monitor);
            buildInfo.store();
        }
    }

    private void populateBuildInfo(IScannerConfigBuilderInfo2 buildInfo, IProgressMonitor monitor) {
        buildInfo.setBuildOutputFileActionEnabled(true);
        buildInfo.setBuildOutputFilePath(bopOpenFileText.getText().trim());
        buildInfo.setBuildOutputParserEnabled(bopEnabledButton.getSelection());
        
        buildInfo.setProviderOutputParserEnabled(providerId, sipEnabledButton.getSelection());
        buildInfo.setProviderRunCommand(providerId, sipRunCommandText.getText().trim());
    }

    /* (non-Javadoc)
     * @see org.eclipse.cdt.ui.dialogs.AbstractCOptionPage#performDefaults()
     */
    public void performDefaults() {
        // Create buildInfo with defaults
        IScannerConfigBuilderInfo2 buildInfo = createBuildInfo();
        
        restoreFromBuildinfo(buildInfo);
    }

    private void restoreFromBuildinfo(IScannerConfigBuilderInfo2 buildInfo) {
        bopOpenFileText.setText(buildInfo.getBuildOutputFilePath());
        bopEnabledButton.setSelection(buildInfo.isBuildOutputParserEnabled());
        
        sipEnabledButton.setSelection(buildInfo.isProviderOutputParserEnabled(providerId));
        sipRunCommandText.setText(buildInfo.getProviderRunCommand(providerId));
    }

}
