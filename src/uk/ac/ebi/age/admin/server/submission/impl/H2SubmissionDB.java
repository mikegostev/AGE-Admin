package uk.ac.ebi.age.admin.server.submission.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.server.submission.SubmissionDB;

public class H2SubmissionDB extends SubmissionDB
{

 @Override
 public void init()
 {
  try
  {
   Class.forName("org.h2.Driver");
   Connection conn = DriverManager.getConnection("jdbc:h2:"+conf.getDbDir().getAbsolutePath(), "sa", "");
   conf.setDbConnection(conn);
   
   initSubmissionDb();
  }
  catch(Exception e)
  {
   e.printStackTrace();
   
   throw new RuntimeException("Database initialization error: "+e.getMessage(),e);
  }

  

 }

 @Override
 public void storeSubmission(SubmissionMeta sMeta)
 {
  // TODO Auto-generated method stub

 }

 private void initSubmissionDb() throws SQLException
 {
  Statement stmt = configuration.getDbConnection().createStatement();
  
  stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+Configuration.submissionDB);

  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+Configuration.submissionDB+'.'+Configuration.submissionTable+" ("+
    "id VARCHAR PRIMARY KEY, desc VARCHAR, ctime BIGINT, mtime BIGINT, creator VARCHAR, modifier VARCHAR, object BINARY)");

  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS ctimeIdx ON "+Configuration.submissionDB+'.'+Configuration.submissionTable+"(ctime)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS mtimeIdx ON "+Configuration.submissionDB+'.'+Configuration.submissionTable+"(mtime)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS creatorIdx ON "+Configuration.submissionDB+'.'+Configuration.submissionTable+"(creator)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS modifierIdx ON "+Configuration.submissionDB+'.'+Configuration.submissionTable+"(modifier)");

  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+Configuration.submissionDB+'.'+Configuration.moduleTable+" ("+
    "id VARCHAR PRIMARY KEY, desc VARCHAR, mtime BIGINT, modifier VARCHAR, object BINARY, FOREIGN KEY(id) REFERENCES "
    +Configuration.submissionDB+'.'+Configuration.submissionTable+"(id) )");

  stmt.executeUpdate("CREATE ALIAS IF NOT EXISTS FTL_INIT FOR \"org.h2.fulltext.FullTextLucene.init\"");
  stmt.executeUpdate("CALL FTL_INIT()");

  stmt.close();
 }
}
