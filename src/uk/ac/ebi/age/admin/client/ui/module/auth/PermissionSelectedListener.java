package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.ext.authz.SystemAction;


public interface PermissionSelectedListener
{
 void permissionSelected(SystemAction act, boolean allow);
}
