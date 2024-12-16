package org.example.HTTPSRequestsHandler;

import org.example.Exceptions.EmptyUserListException;
import org.example.Exceptions.VariablesNotMatching;
import org.example.User;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.port;
public abstract class Initializer {


    public static List<User> runInit() {
        port(8081);

        CommandHandler.usernamesList();

        List<User> users = new ArrayList<>();

        while (!CommandHandler.isGotLobbyInfo()) {
            if (CommandHandler.isReceivedUsernames()) {
                CommandHandler.setGotLobbyInfo(true);

                List<String> usernames = CommandHandler.getUsernames();
                if (usernames.isEmpty())
                    throw new EmptyUserListException();

                int noOfPlayers = usernames.size();
                System.out.println("no of players in init: " + noOfPlayers);

                for (int i = 0; i < noOfPlayers; i++) {
                    String username = usernames.get(i);
                    String userToken = generateUserToken(username);
                    User user = new User(i + 1, username, userToken); // Pass playerId, username, and token
                    users.add(user);
                    System.out.println("User: " + user.getUsername() + ", Token: " + user.getUserToken());
                }

            }
        }
        return users;
    }

    // Helper method to generate a userToken
    private static String generateUserToken(String username) {
        // Use a combination of username and a random UUID for uniqueness
        return username + "-" + java.util.UUID.randomUUID().toString();
    }

}
