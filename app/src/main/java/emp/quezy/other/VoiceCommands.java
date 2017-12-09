package emp.quezy.other;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Urban on 4. 12. 2017.
 */

public class VoiceCommands {

    public static ArrayList<String> playCommands;
    public static ArrayList<String> settingsCommands;
    public static ArrayList<String> infoCommands;
    public static ArrayList<String> exitCommands;


    public static String[] playCom = {
            "play", "play the game", "start the game", "start to play", "play game",
            "start playing", "open up game", "begin", "open the game"
    };

    public static String[] settingsCom = {
            "settings", "open settings", "open up settings", "start settings", "go to the settings",
            "go to settings",
    };

    public static String[] infoCom = {
            "info", "open up information", "open up info", "start info", "go to info",
            "go to the info", "open information", "start information", "about",
            "start about", "start about the game", "open about the game", "open about",
    };

    public static String[] exitCom =  {
            "exit", "exit the app", "exit the application", "close the app", "close the application", "close app"
    };

    public static void initialize() {

        exitCommands = new ArrayList(Arrays.asList(exitCom));
        infoCommands = new ArrayList(Arrays.asList(infoCom));
        settingsCommands = new ArrayList(Arrays.asList(settingsCom));
        playCommands = new ArrayList(Arrays.asList(playCom));
    }



}
