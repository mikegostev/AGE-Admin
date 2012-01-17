package uk.ac.ebi.age.admin.server.service.ds;

import uk.ac.ebi.age.admin.shared.ds.DSDef;

public interface DataSourceBackendService
{

 DataSourceResponse processRequest(DataSourceRequest dsr);

 DSDef getDSDefinition();

}
