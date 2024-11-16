package org.example.HTTPSRequestsHandler;

import org.example.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
public class CommandHandler {

//    private static volatile boolean receivedNumberOfPlayers = false;
    private static volatile boolean receivedUsernames = false;
    private static volatile boolean gotLobbyInfo = false;
    private static List<String> usernames;
    public static void receiveUsernameAndCommand() {
        post("/api/commands", (request, response) -> {

            try {
                JSONObject json = new JSONObject(request.body());
                String username = json.optString("username");
                String command = json.optString("command");

                Main.storeUserAndCommand(username, command);


                response.status(200);
                return "Command processed";
            } catch( Exception e){
                response.status(400);
                return "Server error" + e.getMessage();
            }
        });
    }


    public static void usernamesList(){
        post("/api/usernames", (request, response) -> {

            try {
                JSONObject json = new JSONObject(request.body());
                JSONArray usernamesList = json.getJSONArray("usernames");

                List<String> users = new ArrayList<>();
                //System.out.println("Received the players: ");
                for (int i = 0; i < usernamesList.length(); i++) {
                    users.add(usernamesList.getString(i));
                }


                setReceivedUsernames(true);
                CommandHandler.setUsernames(users);


                response.status(200);
                return "UsersList processed";
            } catch (Exception e) {
                response.status(500);
                return "Server error: " + e.getMessage();
            }
        });
    }

    public static void endUserCommands(){
        post("/api/showUsersAndCommandsList", (request, response) -> {
            System.out.println("Show list was called");
            try{
               Main.printList();
               return "Finished command processed";

               }catch (Exception e) {
                response.status(500);
                return "Server error: " + e.getMessage();
            }
        });
    }

    public static void receiveOneCommand(){
        post("/api/sendOneCommand", (request, response) -> {
            JSONObject json = new JSONObject(request.body());

            try {
                String command = json.optString("command");
                Main.storeCommand(command);

                return "Finished sending command";

            } catch (Exception e) {
                response.status(500);
                return "Server error: " + e.getMessage();
            }

        });
    }


    public static boolean isReceivedUsernames() {
        return receivedUsernames;
    }

    public static void setReceivedUsernames(boolean receivedUsernames) {
        CommandHandler.receivedUsernames = receivedUsernames;
    }

    public static boolean isGotLobbyInfo() {
        return gotLobbyInfo;
    }

    public static void setGotLobbyInfo(boolean gotLobbyInfo) {
        CommandHandler.gotLobbyInfo = gotLobbyInfo;
    }


    public static List<String> getUsernames(){
        return CommandHandler.usernames;
    }

    public static void setUsernames(List<String> usernames) {
        CommandHandler.usernames = usernames;
    }}