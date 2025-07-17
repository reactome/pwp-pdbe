package uk.ac.ebi.pwp.widgets.pdb.ui;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
class PrintsWidgetWrapper extends JavaScriptObject {

    protected PrintsWidgetWrapper() {
    }

    public static native PrintsWidgetWrapper create(String placeHolder, String pdbId, String orientation) /*-{
        return new $wnd.YAHOO.PDBe.PrintsWidget(placeHolder, pdbId, "jslogos", orientation, 36, "embl_green", "_blank");
    }-*/;

    public final native void show() /*-{
        this.show();
    }-*/;
}
