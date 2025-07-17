package uk.ac.ebi.pwp.widgets.pdb.model.factory;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.*;
import uk.ac.ebi.pwp.widgets.pdb.model.PDBObject;
import uk.ac.ebi.pwp.widgets.pdb.model.QueryResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Guilherme VITERI (gviteri@ebi.ac.uk)
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public class PDBRetriever {
    private static final String PDBE_HOST = "https://www.ebi.ac.uk/";
    private static final String PDBE_APPS_URL_ALL = "pdbe/api/mappings/best_structures/";
    private final ResultHandler handler;
    private final String proteinAccession;

    public PDBRetriever(String proteinAccession, ResultHandler handler) {
        this.proteinAccession = proteinAccession;
        this.handler = handler;
    }

    public void getBestStructure() {
        String url = PDBE_HOST + PDBE_APPS_URL_ALL + this.proteinAccession + "/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()) {
                        case Response.SC_OK:
                            QueryResult result = QueryResult.buildQueryResult(response.getText());
                            JsArray<PDBObject> pdbs = result.getPDBObject(proteinAccession);
                            if (pdbs.length() > 0) {
                                handler.setPDEBBestResult(pdbs.get(0));
                            } else {
                                handler.onEmptyResult();
                            }
                            break;
                        case Response.SC_NOT_FOUND:
                            handler.onEmptyResult();
                            break;
                        default:
                            handler.onPDBERetrieverError("Server not reachable");
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onPDBERetrieverError(exception.getMessage());
                }
            });
        } catch (RequestException e) {
            handler.onPDBERetrieverError(e.getMessage());
        }
    }

    public void getAllStructures() {
        String url = PDBE_HOST + PDBE_APPS_URL_ALL + this.proteinAccession + "/";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        QueryResult result = QueryResult.buildQueryResult(response.getText());
                        JsArray<PDBObject> pdbs = result.getPDBObject(proteinAccession);
                        List<PDBObject> all = new LinkedList<>();
                        for (int i = 0; i < pdbs.length(); i++) {
                            PDBObject pdbObject = pdbs.get(i);
                            if (!pdbObject.isEmpty()) {
                                all.add(pdbs.get(i));
                            }
                        }
                        if (all.isEmpty()) {
                            handler.onEmptyResult();
                        } else {
                            handler.setPDBEAllResults(all);
                        }
                    } else {
                        handler.onEmptyResult();
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onPDBERetrieverError(exception.getMessage());
                }
            });
        } catch (RequestException e) {
            handler.onPDBERetrieverError(e.getMessage());
        }
    }

    public interface ResultHandler {
        void setPDBEAllResults(List<PDBObject> pdbObjectList);

        void setPDEBBestResult(PDBObject pdbObject);

        void onEmptyResult();

        void onPDBERetrieverError(String errorMessage);
    }
}
