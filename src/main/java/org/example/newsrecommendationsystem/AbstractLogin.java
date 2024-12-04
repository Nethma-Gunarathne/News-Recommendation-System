package org.example.newsrecommendationsystem;

public abstract class AbstractLogin {

    /**
     * Verifies the provided password against the expected password.
     *
     * @param inputPassword The password entered by the user.
     * @param correctPassword The correct password for verification.
     * @return true if the passwords match, false otherwise.
     */
    protected boolean verifyPassword(String inputPassword, String correctPassword) {
        return inputPassword != null && inputPassword.equals(correctPassword);
    }
}

