package uk.ac.ebi.age.admin.server.submission;

import java.util.List;

import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.shared.submission.SubmissionImprint;
import uk.ac.ebi.age.admin.shared.submission.SubmissionQuery;

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

 public abstract List<SubmissionImprint> getSubmissions(SubmissionQuery q);

}
