package uk.ac.ebi.age.admin.server.util;

import java.io.File;

import uk.ac.ebi.age.admin.client.common.Directory;

public class DirectoryTools
{
 public static Directory imprintDirectory( File d )
 {
  if( ! d.isDirectory() )
   return null;
  
  Directory dir = new Directory(d.getName());
  
  for( File f : d.listFiles() )
   if( f.isDirectory() )
    dir.addSubdirectory( imprintDirectory(f) );
   else
    dir.addFile( f.getName() );
  
  return dir;
 }
}
