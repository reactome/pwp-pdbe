package uk.ac.ebi.pwp.widgets.pdb.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.*;
import uk.ac.ebi.pwp.widgets.pdb.events.PdbStructureLoadedEvent;
import uk.ac.ebi.pwp.widgets.pdb.events.pdbStructureNotAvailableEvent;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureLoadedHandler;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureNotAvailableHandler;
import uk.ac.ebi.pwp.widgets.pdb.model.PDBObject;
import uk.ac.ebi.pwp.widgets.pdb.model.factory.PDBRetriever;

import java.util.List;

/**
 * This viewer
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-PDBViewer
 * <dd>the viewer itself
 * <dt>.pdb-PDBViewer .pdb-PDBVPanel
 * <dd>the panel containing a PDB element
 * </dl>
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("UnusedDeclaration")
public class PDBViewer extends Composite implements HasHandlers, OpenHandler<DisclosurePanel>, PDBRetriever.ResultHandler {
    private final VerticalPanel container;
    private final DisclosurePanel disclosurePanel;
    private boolean opened = false;
    private final String proteinAccession;
    private final String proteinName;
    private final PDBRetriever retriever;

    public PDBViewer(String proteinAccession, String proteinName) {
        this.container = new VerticalPanel();
        //noinspection GWTStyleCheck
        this.container.addStyleName("pdb-PDBViewer");
        this.container.setWidth("100%");

        this.disclosurePanel = new DisclosurePanel("All other structures for " + proteinAccession);
        this.disclosurePanel.setAnimationEnabled(true);
        this.disclosurePanel.setWidth("100%");
        this.disclosurePanel.addOpenHandler(this);

        this.proteinAccession = proteinAccession;
        this.proteinName = proteinName;
        this.retriever = new PDBRetriever(proteinAccession, this);

        this.initWidget(this.container);
        this.getBestStructure();
    }

    public HandlerRegistration addStructureLoadedHandler(PdbStructureLoadedHandler handler) {
        return this.addHandler(handler, PdbStructureLoadedEvent.TYPE);
    }

    public HandlerRegistration addStructureNotAvailableHandler(PdbStructureNotAvailableHandler handler) {
        return this.addHandler(handler, pdbStructureNotAvailableEvent.TYPE);
    }

    private void getBestStructure() {
        String msg = "Loading best structure for " + this.proteinAccession + ". Please wait...";
        this.container.clear();
        this.container.add(PDBViewer.getLoadingMessage(msg));
        this.retriever.getBestStructure();
    }

    public static Widget getLoadingMessage(String customMessage) {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);

        hp.add(new Image(Images.INSTANCE.getLoadingImage().getSafeUri()));

        InlineLabel label = new InlineLabel(customMessage);
        label.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        label.getElement().getStyle().setFloat(Style.Float.LEFT);
        hp.add(label);

        return hp;
    }

    private static String getOrCreateDivId(Panel panel) {
        String divId = panel.getElement().getId();
        if (divId.isEmpty()) {
            divId = HTMLPanel.createUniqueId();
            panel.getElement().setId(divId);
        }
        return divId;
    }

    @Override
    public void setPDBEAllResults(List<PDBObject> pdbObjectList) {
        if (!pdbObjectList.isEmpty()) {
            pdbObjectList.remove(0);
        }

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("99%");
        if (!pdbObjectList.isEmpty()) {
            for (PDBObject pdbObject : pdbObjectList) {
                vp.add(new PDBPanel(pdbObject, this.proteinName));
            }
            this.disclosurePanel.setContent(vp);
        } else {
            this.disclosurePanel.setContent(new Label("Not more structures found for " + this.proteinAccession));
        }
    }

    @Override
    public void setPDEBBestResult(PDBObject pdbObject) {
        this.container.clear();
        this.container.add(new PDBPanel(pdbObject, this.proteinName));
        this.container.add(this.disclosurePanel);
        fireEvent(new PdbStructureLoadedEvent(pdbObject));
    }

    @Override
    public void onEmptyResult() {
        this.container.clear();
        this.container.add(new HTMLPanel("No structures found for " + this.proteinAccession + " in PDBe"));
        fireEvent(new pdbStructureNotAvailableEvent(this.proteinAccession));
    }

    @Override
    public void onOpen(OpenEvent<DisclosurePanel> event) {
        if (!this.opened) {
            this.opened = true;
            this.getAllStructures();
        }
    }

    private void getAllStructures() {
        String msg = "Loading all PDB structures for " + this.proteinAccession;
        this.disclosurePanel.setContent(PDBViewer.getLoadingMessage(msg));
        this.retriever.getAllStructures();
    }

    @Override
    public void onPDBERetrieverError(String errorMessage) {
        FlowPanel flowPanel = new FlowPanel();
        //noinspection GWTStyleCheck
        flowPanel.setStyleName("pdb-PDBRetrievalError");
        flowPanel.add(new InlineLabel("It was not possible to retrieve the data."));
        Button tryAgain = new Button("Try again");
        flowPanel.add(tryAgain);

        if (this.opened) {
            //The best was found, so this error is for the "all"
            tryAgain.addClickHandler(event -> getAllStructures());
            this.disclosurePanel.setContent(flowPanel);
        } else {
            //The error is trying to retrieve the "best"
            tryAgain.addClickHandler(event -> getBestStructure());
            this.container.clear();
            this.container.add(flowPanel);
        }
    }
}
