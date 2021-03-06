package Poe.Engine.Utlities;

import Poe.Engine.Renderer;
import Poe.Engine.EventListener;
import Poe.World.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Debugging Utility Class
 */
public class DebuggerUtils {

    private static int totalDebugMsgs = 1;
    private static Map<Integer, String> debugMap = new HashMap<>();

    public static void addDebugMessage(String msg) {
        totalDebugMsgs++;
        debugMap.put(totalDebugMsgs, msg);
    }

    public static void writeToScreen() {
        if(World.debug){
            EventListener.textRenderer.beginRendering(Renderer.getWindowWidth(), Renderer.getWindowHeight());
            debugMap.forEach((index, message) -> EventListener.textRenderer.draw(message, 10, Renderer.getWindowHeight() - (16 * index)));
            EventListener.textRenderer.endRendering();
        }
        totalDebugMsgs = 1;
        debugMap = new HashMap<>();
    }
}
