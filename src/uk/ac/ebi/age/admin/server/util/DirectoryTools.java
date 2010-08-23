package uk.ac.ebi.age.admin.server.util;

import java.io.File;

import uk.ac.ebi.age.admin.client.common.Directory;

public class DirectoryTools
{
 public static Directory createDirectory( File d )
 {
  if( ! d.isDirectory() )
   return null;
  
  Directory dir = new Directory(d.getName());
  
  for( File f : d.listFiles() )
   if( f.isDirectory() )
    dir.addSubdirectory( createDirectory(f) );
   else
    dir.addFile( f.getName() );
  
  return dir;
 }
}
