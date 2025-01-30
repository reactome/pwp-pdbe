package uk.ac.ebi.pwp.widgets.pdb.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import uk.ac.ebi.pwp.widgets.pdb.model.PDBObject;
import uk.ac.ebi.pwp.widgets.pdb.model.Range;

/**
 * This panel contains...
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pdb-PDBVPanel
 * <dd>the panel containing a PDB element
 * </dl>
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class PDBPanel extends Composite {

    public PDBPanel(PDBObject pdbObject, String proteinName) {
        HorizontalPanel container = new HorizontalPanel();
        container.setWidth("100%");

        container.add(this.getSummaryLink(pdbObject.getPdbid()));

        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.add(this.getPropertiesPanel(pdbObject, proteinName));
        vp.add(new PrintsWidget(pdbObject.getPdbid()));
        container.add(vp);

        initWidget(container);

        //noinspection GWTStyleCheck
        addStyleName("pdb-PDBPanel");
        setWidth("100%");
    }

    private Widget getPropertiesPanel(PDBObject pdbObject, String proteinName) {
        FlowPanel properties = new FlowPanel();
        properties.setWidth("100%");
        properties.getElement().getStyle().setMarginTop(10, Style.Unit.PX);

//        Widget pdbeId = getPropertyWidget("PDB id", pdbObject.getPdbid());
//        properties.add(pdbeId);

        String[] aux = proteinName.split(":");
        Widget proteinAccession = getPropertyWidget(aux[0], aux[1]);
        properties.add(proteinAccession);

        Widget chain = getPropertyWidget("Chain", pdbObject.getChain());
        properties.add(chain);

        Widget resolution = getPropertyWidget("Resolution", pdbObject.getResolution());
        properties.add(resolution);

        Widget coverage = getPropertyWidget("Coverage", pdbObject.getCoverage());
        properties.add(coverage);

        Widget pdbeRange = getRangeWidget("PDB Range", pdbObject.getPdbRange());
        properties.add(pdbeRange);

        Widget uniprotRange = getRangeWidget("UniProt Range", pdbObject.getUniprotRange());
        properties.add(uniprotRange);

        return properties;
    }

    private Widget getPropertyWidget(String key, String value) {
        String aux = (value != null) ? value : "N/A";

        FlowPanel hp = new FlowPanel();
        hp.add(new InlineLabel(key + ": "));
        InlineLabel valueLabel = new InlineLabel(aux);
        valueLabel.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        hp.add(valueLabel);

        hp.getElement().getStyle().setMarginRight(20, Style.Unit.PX);
        hp.getElement().getStyle().setFloat(Style.Float.LEFT);

        return hp;
    }

    private Widget getRangeWidget(String key, Range value) {
        String aux = (value == null) ? "N/A" : "[" + value.getMin() + ", " + value.getMax() + "]";

        FlowPanel hp = new FlowPanel();
        hp.add(new InlineLabel(key + ": "));
        InlineLabel valueLabel = new InlineLabel(aux);
        valueLabel.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        hp.add(valueLabel);

        hp.getElement().getStyle().setMarginRight(20, Style.Unit.PX);
        hp.getElement().getStyle().setFloat(Style.Float.LEFT);

        return hp;
    }

    private Widget getSummaryLink(String pdbId) {
        //IMPORTANT: If the "cbc120" changes, maybe you also want to change the anchor width
//        String url = "//www.ebi.ac.uk/pdbe-srv/view/images/entry/" + pdbId + "_cbc120.png";
        String url = "//www.ebi.ac.uk/pdbe/static/entry/" + pdbId + "_deposited_chain_front_image-200x200.png";

        Image image = new Image(url);
        image.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        image.getElement().getStyle().setWidth(120, Style.Unit.PX);
        image.getElement().getStyle().setHeight(120, Style.Unit.PX);

        Anchor anchor = new Anchor("", "//www.ebi.ac.uk/pdbe-srv/view/entry/" + pdbId + "/summary", "_blank");
        DOM.insertBefore(anchor.getElement(), image.getElement(), DOM.getFirstChild(anchor.getElement()));
        anchor.getElement().getStyle().setWidth(150, Style.Unit.PX);
        return anchor;
    }
}
