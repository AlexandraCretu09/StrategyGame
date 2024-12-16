package org.example;

public class CommandsLogic {
    private String command;
    private GameMap gameMap;
    private User user;

        public CommandsLogic(String command, GameMap gameMap, String userToken) {
            this.command = command;
            this.gameMap = gameMap;
            this.user = gameMap.getUserByToken(userToken);

            if (this.user == null) {
                throw new IllegalArgumentException("Invalid user token.");
            }
        }

        public void processCommand() {
            switch (this.command) {
                case "showResources" -> {
                    System.out.printf(
                            "Wood: %d, Stone: %d, Gold: %d%n",
                            user.getWood(), user.getStone(), user.getGold()
                    );
                }
                default -> System.out.println("Invalid command: " + command);
            }
        }
    }
