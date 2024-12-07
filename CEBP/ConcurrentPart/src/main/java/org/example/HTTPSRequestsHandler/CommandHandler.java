package org.example.HTTPSRequestsHandler;

import org.example.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static spark.Spark.*;
public class CommandHandler {

    //    private static volatile boolean receivedNumberOfPlayers = false;
    private static volatile boolean receivedUsernames = false;
    private static volatile boolean gotLobbyInfo = false;
    private static List<String> usernames;


    public static void receiveUsernameAndCommand() {
        post("/api/commands", (request, response) -> {
            try {
                String commandJson = request.body(); // Get the full JSON string
                JSONObject json = new JSONObject(commandJson); // Parse the JSON string
                String username = json.optString("username"); // Extract the username
                Main.storeUserAndCommand(username, commandJson); // Pass the full JSON string
                response.status(200);
                return "Command processed.";
            } catch (Exception e) {
                response.status(500);
                return "Server error: " + e.getMessage();
            }
        });
    }





    public static void usernamesList(){
        post("/api/usernames", (request, response) -> {

            try {
                JSONArray usernamesList = new JSONArray(request.body());

                List<String> users = new ArrayList<>();
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
                //Main.printList();
                return "Finished command processed";

            }catch (Exception e) {
                response.status(500);
                return "Server error: " + e.getMessage();
            }
        });
    }

//    public static void receiveOneCommand(){
//        post("/api/sendOneCommand", (request, response) -> {
//            JSONObject json = new JSONObject(request.body());
//
//            try {
//                String command = json.optString("command");
//                Main.storeCommand(command);
//
//                return "Finished sending command";
//
//            } catch (Exception e) {
//                response.status(500);
//                return "Server error: " + e.getMessage();
//            }
//
//        });
//    }


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