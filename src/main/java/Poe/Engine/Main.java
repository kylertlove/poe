package Poe.Engine;

import Poe.Engine.Utlities.PoeLogger;
import Poe.World.World;

public class Main {

    public static void main(String[] args) {
        PoeLogger.logger.info("World Init:");
        World.init();  //Build the Objects
        PoeLogger.logger.info("GameBoard Init:");
        Renderer.init(); //Build the Window
        PoeLogger.logger.info("GameLoop Start:");
        GameLoop.start(); //Start the Game
    }
}
