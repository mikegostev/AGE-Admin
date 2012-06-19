package uk.ac.ebi.age.admin.shared;

public class Constants
{
 public static final String sessionKey="AGESESS";
 
 public static final String serviceHandlerParameter = "Subject";

 public static final String downloadHandlerParameter = "Subject";

 public static final String attachmentRequestSubject = "attachment";
 public static final String documentRequestSubject = "document";

 public static final String clusterIdParameter = "clusterId";
 public static final String fileIdParameter = "fileId";
 public static final String documentIdParameter = "modId";

 public static final String versionParameter = "version";

 public static final String dsServiceUrl="dataSourceService"; 
 public static final String dsServiceParam="service";

 public static final String userIdParam="_$userId";
 public static final String groupIdParam="_$groupId";
 public static final String profileIdParam="_$profileId";
 public static final String classifIdParam ="_$classifId";
 public static final String tagIdParam = "_$tagId";
 
 public static final String userListServiceName="userList";
 public static final String groupListServiceName="groupList";
 public static final String groupOfUserListServiceName = "groupOfUserList";
 public static final String groupPartsListServiceName = "groupPartsList";

 public static final String profileListServiceName="profileList";

 public static final String profilePermissionsListServiceName="profPermList";

 public static final String permissionListServiceName="permissionList";

 public static final String classifierListServiceName = "classifierList";

 public static final String tagTreeServiceName = "tagsTree";

 public static final String tagACLServiceName = "tagACL";
 public static final String sysACLServiceName = "sysACL";

 public static final String rootTagId = "__rootTag";

 public static final String beginJSONSign = "//REPORT BEGIN";
 public static final String endJSONSign = "//REPORT END";

 public static final int SUBMISSIONS_PER_PAGE = 30;

 public static final String SUBMISSON_COMMAND = "Submission";
 public static final String MAINTENANCE_MODE_COMMAND = "setMaintenanceMode";
 public static final String SUBMISSION_TAGS_COMMAND = "submissionTags";
 public static final String RPC_COMMAND = "rpc";

}
