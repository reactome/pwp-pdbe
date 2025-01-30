package uk.ac.ebi.pwp.widgets.pdb.handlers;

import com.google.gwt.event.shared.EventHandler;
import uk.ac.ebi.pwp.widgets.pdb.events.pdbStructureNotAvailableEvent;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public interface PdbStructureNotAvailableHandler extends EventHandler {

    void onPdbStructureNotAvailable(pdbStructureNotAvailableEvent event);

}
