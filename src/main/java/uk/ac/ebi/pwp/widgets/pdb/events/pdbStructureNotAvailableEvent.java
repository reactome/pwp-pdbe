package uk.ac.ebi.pwp.widgets.pdb.events;

import com.google.gwt.event.shared.GwtEvent;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureNotAvailableHandler;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("UnusedDeclaration")
public class pdbStructureNotAvailableEvent extends GwtEvent<PdbStructureNotAvailableHandler> {
    @SuppressWarnings("Convert2Diamond")
    public static Type<PdbStructureNotAvailableHandler> TYPE = new Type<PdbStructureNotAvailableHandler>();

    private final String proteinAccession;

    public pdbStructureNotAvailableEvent(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    @Override
    public Type<PdbStructureNotAvailableHandler> getAssociatedType() {
        return TYPE;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    @Override
    protected void dispatch(PdbStructureNotAvailableHandler handler) {
        handler.onPdbStructureNotAvailable(this);
    }
}
