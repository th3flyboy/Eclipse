package com.mobilesorcery.sdk.update.internal;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RegistrationPerspectiveFactory implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.addView(RegistrationWebBrowserView.VIEW_ID, IPageLayout.TOP, 1.0f, IPageLayout.ID_EDITOR_AREA);
    }

}
