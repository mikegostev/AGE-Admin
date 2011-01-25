package uk.ac.ebi.age.admin.server.submission;

import uk.ac.ebi.age.admin.server.model.SubmissionMeta;

public abstract class SubmissionDB
{
 private static SubmissionDB instance;
 
 
 public static SubmissionDB getInstance()
 {
  return instance;
 }

 public static void setInstance( SubmissionDB db )
 {
  instance=db;
 }

 public abstract void init();

 public abstract void storeSubmission(SubmissionMeta sMeta);

 public abstract void shutdown();

}
