package com.tonilopezmr.login;

/**
 * @author Antonio López.
 */
public interface SignInView {
    void userIsLogged();
    void errorOnConnect();
    void userIsntLogged();
    void onConnectionComplete(UserProfile person);
}
