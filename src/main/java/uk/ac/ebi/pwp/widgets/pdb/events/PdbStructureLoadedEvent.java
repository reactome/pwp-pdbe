package uk.ac.ebi.pwp.widgets.pdb.events;

import com.google.gwt.event.shared.GwtEvent;
import uk.ac.ebi.pwp.widgets.pdb.handlers.PdbStructureLoadedHandler;
import uk.ac.ebi.pwp.widgets.pdb.model.PDBObject;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("UnusedDeclaration")
public class PdbStructureLoadedEvent extends GwtEvent<PdbStructureLoadedHandler> {
    @SuppressWarnings("Convert2Diamond")
    public static Type<PdbStructureLoadedHandler> TYPE = new Type<PdbStructureLoadedHandler>();

    PDBObject pdbObject;

    public PdbStructureLoadedEvent(PDBObject pdbObject) {
        this.pdbObject = pdbObject;
    }

    @Override
    public Type<PdbStructureLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    public PDBObject getPdbObject() {
        return pdbObject;
    }

    @Override
    protected void dispatch(PdbStructureLoadedHandler handler) {
        handler.onPdbStructureLoaded(this);
    }
}
