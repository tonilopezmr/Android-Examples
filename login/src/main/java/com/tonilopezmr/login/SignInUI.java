package com.tonilopezmr.login;

/**
 * @author Antonio López.
 */
public interface SignInUI {
    void userIsLogged();
    void errorOnConnect();
    void userIsntLogged();
    void onConnectionComplete(PersonProfile person);
}
