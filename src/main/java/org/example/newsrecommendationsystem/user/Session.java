package org.example.newsrecommendationsystem.user;

public class Session {
    private static User currentUser; // Holds the current user during the session

    // Set the current user (used when user logs in)
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Get the current user (used to fetch details like credentials)
    public static User getCurrentUser() {
        return currentUser;
    }

    // Clear session (log out)
    public static void clearSession() {
        currentUser = null;
    }
}

