package com.mobilesorcery.sdk.builder.iphoneos.launch;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobilesorcery.sdk.builder.iphoneos.Activator;
import com.mobilesorcery.sdk.builder.iphoneos.IPhoneOSPackager;
import com.mobilesorcery.sdk.builder.iphoneos.IPhoneSimulator;
import com.mobilesorcery.sdk.builder.iphoneos.SDK;
import com.mobilesorcery.sdk.builder.iphoneos.XCodeBuild;
import com.mobilesorcery.sdk.builder.iphoneos.ui.dialogs.ConfigureXcodeDialog;
import com.mobilesorcery.sdk.core.BuildVariant;
import com.mobilesorcery.sdk.core.IBuildVariant;
import com.mobilesorcery.sdk.core.MoSyncProject;
import com.mobilesorcery.sdk.core.Version;
import com.mobilesorcery.sdk.core.launch.AbstractEmulatorLauncher;
import com.mobilesorcery.sdk.core.launch.IEmulatorLauncher;
import com.mobilesorcery.sdk.core.launch.MoReLauncher;
import com.mobilesorcery.sdk.internal.launch.EmulatorLaunchConfigurationDelegate;

public class IPhoneEmulatorLauncher extends AbstractEmulatorLauncher {

	public final static String SDK_ATTR = "iphone.sdk";

	private static final String ID = "com.mobilesorcery.sdk.builder.iphoneos.launcher";

	public IPhoneEmulatorLauncher() {
		super("iPhone Simulator");
	}

	@Override
	public int isLaunchable(ILaunchConfiguration launchConfiguration, String mode) {
		if (!Util.isMac()) {
			return UNLAUNCHABLE;
		} else if (!isCorrectPackager(launchConfiguration, IPhoneOSPackager.ID)) {
			return IEmulatorLauncher.UNLAUNCHABLE;
		} else if (isIncorrectlyInstalled()) {
			return isAutoSelectLaunch(launchConfiguration, mode) && Activator.getDefault().shouldUseFallback() ?
					IEmulatorLauncher.UNLAUNCHABLE :
					IEmulatorLauncher.REQUIRES_CONFIGURATION;
		} else {
			return super.isLaunchable(launchConfiguration, mode);
		}
	}

	@Override
	public void launch(ILaunchConfiguration launchConfig, String mode,
			ILaunch launch, int emulatorId, IProgressMonitor monitor)
			throws CoreException {
		// If once again it's not properly conf'ed:
		Activator.getDefault().setUseFallback(false);

		// TODO: Incremental building if we change the SDK!?
		IProject project = EmulatorLaunchConfigurationDelegate.getProject(launchConfig);
		MoSyncProject mosyncProject = MoSyncProject.create(project);
		SDK sdk = Activator.getDefault().getSDK(mosyncProject, XCodeBuild.IOS_SIMULATOR_SDKS);
		Version sdkVersion = sdk == null ? null : sdk.getVersion();
		File pathToApp = getPackageToInstall(launchConfig, mode);
		String family = getFamily(getVariant(launchConfig, mode));
		IPhoneSimulator.getDefault().runApp(new Path(pathToApp.getAbsolutePath()), sdkVersion == null ? null : sdkVersion.toString(), family);
	}

	private String getFamily(IBuildVariant variant) {
		// Hard-coded, we may want to get this from device db instead.
		if (variant.getProfile().getName().contains("iPad")) {
			return "ipad";
		}
		return null;
	}

	@Override
	public IBuildVariant getVariant(ILaunchConfiguration launchConfig, String mode) throws CoreException {
		IBuildVariant prototype = super.getVariant(launchConfig, mode);
		BuildVariant modified = new BuildVariant(prototype);
		modified.setSpecifier(Activator.IOS_SIMULATOR_SPECIFIER, Activator.IOS_SIMULATOR_SPECIFIER);
		return modified;
	}

	@Override
	public String configure(ILaunchConfiguration config, String mode) {
		XCodeBuild.getDefault().refresh();

		Display d = PlatformUI.getWorkbench().getDisplay();
		// If we are not auto-select, don't fallback to MoRe.
		final boolean showFallbackAlternative = isAutoSelectLaunch(config, mode);

		final String[] result = new String[] { null };
		d.syncExec(new Runnable() {
			@Override
			public void run() {
				// OK, figure out after 2.6 release where to really put this ui stuff!
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ConfigureXcodeDialog configureDialog = new ConfigureXcodeDialog(shell);
				configureDialog.setShowFallback(showFallbackAlternative);
				int dialogResult = configureDialog.open();
				if (dialogResult == ConfigureXcodeDialog.FALLBACK_ID) {
					result[0] = MoReLauncher.ID;
				}
			}
		});
		return result[0];
	}

	protected boolean isIncorrectlyInstalled() {
		return !IPhoneSimulator.getDefault().isValid() || !XCodeBuild.getDefault().isValid() || XCodeBuild.getDefault().listSDKs(XCodeBuild.IOS_SIMULATOR_SDKS).size() == 0;
	}
}