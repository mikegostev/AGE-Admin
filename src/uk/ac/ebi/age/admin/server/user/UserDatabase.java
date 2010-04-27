package uk.ac.ebi.age.admin.server.user;

public interface UserDatabase
{

 UserProfile getAnonymousProfile();

 UserProfile getUserProfile(String userName);

}
