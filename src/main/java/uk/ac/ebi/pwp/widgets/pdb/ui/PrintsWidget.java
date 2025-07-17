package uk.ac.ebi.pwp.widgets.pdb.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("UnusedDeclaration")
public class PrintsWidget extends Composite {
    private SimplePanel container;
    private PrintsWidgetWrapper p;

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private Orientation orientation;
    private final String pdbId;

    public PrintsWidget(String pdbId) {
        this.orientation = Orientation.HORIZONTAL;
        this.pdbId = pdbId;
        initialize();
    }

    public PrintsWidget(String pdbId, Orientation orientation) {
        this.orientation = orientation;
        this.pdbId = pdbId;
        initialize();
    }

    private void initialize() {
        this.container = new SimplePanel();
        this.container.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        this.container.getElement().setId(Document.get().createUniqueId());
        this.container.add(PDBViewer.getLoadingMessage("Loading PDB Viewer..."));
        initWidget(this.container);
    }

    /**
     * This method is called immediately after a widget becomes attached to the
     * browser's document.
     */
    @Override
    protected void onLoad() {
        if (this.p == null) {
            String placeHolder = this.container.getElement().getId(); //this.getOrCreateDivId();
            String orientation = this.orientation.toString().toLowerCase();
            this.container.clear();
            this.p = PrintsWidgetWrapper.create(placeHolder, pdbId, orientation);
            this.p.show();
        }
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;

    }
}
