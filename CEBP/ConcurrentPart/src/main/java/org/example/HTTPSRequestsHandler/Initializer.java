package org.example.HTTPSRequestsHandler;

import org.example.Exceptions.VariablesNotMatching;
import org.example.User;

import java.util.ArrayList;
import java.util.List;

import static org.example.HTTPSRequestsHandler.CommandHandler.usernamesList;
import static spark.Spark.port;
public abstract class Initializer {


    public static List<User> runInit() {


        port(8081);

        CommandHandler.numberOfPlayer();
        usernamesList();

        List<User> users = new ArrayList<>();
        int noOfPlayers;


        while (!CommandHandler.isGotLobbyInfo()) {
            if (CommandHandler.isReceivedNumberOfPlayers() && CommandHandler.isReceivedUsernames()) {
                CommandHandler.setGotLobbyInfo(true);


                noOfPlayers = CommandHandler.getNoOfPlayers();
                List<String> usernames = CommandHandler.getUsernames();

                if(noOfPlayers != usernames.size())
                    throw new VariablesNotMatching();

                for(int i=0; i < noOfPlayers; i++){
                    users.add(new User(i, usernames.get(i)));
                }
            }
        }
        return users;
    }
}
