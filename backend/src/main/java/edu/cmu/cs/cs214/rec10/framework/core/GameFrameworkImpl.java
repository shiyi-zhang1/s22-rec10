package edu.cmu.cs.cs214.rec10.framework.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The framework core implementation.
 */
public class GameFrameworkImpl implements GameFramework {
    private final String NO_GAME_NAME = "A game framework";
    private final String DEFAULT_FOOTER = "No ongoing game";
    private final int DEFAULT_HEIGHT = 1;
    private final int DEFAULT_WIDTH = 1;
    private String[][] gameGrid;
    private GamePlugin currentPlugin;
    private String footer;
    private List<GamePlugin>  registeredPlugins;

    public GameFrameworkImpl() {
        gameGrid = new String[DEFAULT_WIDTH][DEFAULT_HEIGHT];
        footer = DEFAULT_FOOTER;
        registeredPlugins = new ArrayList<GamePlugin>();
    }



    /**
     * Registers a new {@link GamePlugin} with the game framework
     */
    public void registerPlugin(GamePlugin plugin) {
        plugin.onRegister(this);
        registeredPlugins.add(plugin);
    }

    /**
     * Starts a new game for the provided {@link GamePlugin}
     */
    public void startNewGame(GamePlugin plugin) {
        final int width = plugin.getGridWidth();
        final int height = plugin.getGridHeight();

        if (currentPlugin != plugin) {
            // If currentPlugin != plugin, then we are switching to a new
            // plug-in's game. This requires a bit more work to setup since
            // different games can have different grid widths and grid heights.
            if (currentPlugin != null)
                currentPlugin.onGameClosed();
            currentPlugin = plugin;
            gameGrid = new String[height][width];
        }

        // Reset the game's internal state.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                gameGrid[y][x] = null;
            }
        }
        currentPlugin.onNewGame();
        currentPlugin.onNewMove();

    }

    /**
     * Performs a move a specified location
     *
     * @param x The x coordinate of the grid square.
     * @param y The y coordinate of the grid square.
     */
    public void playMove(int x, int y) {
        if (!currentPlugin.isMoveValid(x, y)) {
            return;
        }

        currentPlugin.onMovePlayed(x, y);

        if (currentPlugin.isGameOver()) {
            //startNewGame(currentPlugin);
            return;
        }

        if (currentPlugin.isMoveOver()) {
            currentPlugin.onNewMove();
        }
    }

    /* GameFramework methods. */

    @Override
    public String getCurrentPlayerName() {
        if (currentPlugin != null) {
            return currentPlugin.currentPlayer().toString();
        } else {
            return null;
        }
    }

    @Override
    public String getSquare(int x, int y) {
        return gameGrid[y][x];
    }

    @Override
    public void setSquare(int x, int y, String obj) {
        gameGrid[y][x] = obj;
    }

    @Override
    public void setFooterText(String text) {
        footer = text;
    }

    /* GameState methods: getter for Gui purposes*/
    public String getGameName(){
        if (currentPlugin == null){
            return NO_GAME_NAME;
        } else {
            return currentPlugin.getGameName();
        }
    }

    public int getGridWidth(){
        if (currentPlugin == null){
            return DEFAULT_WIDTH;
        } else {
            return currentPlugin.getGridWidth();
        }
    }

    public int getGridHeight(){
        if (currentPlugin == null){
            return DEFAULT_HEIGHT;
        } else {
            return currentPlugin.getGridHeight();
        }
    }

    public String getFooter(){
        return footer;
    }

    public List<String> getRegisteredPluginName(){
        return registeredPlugins.stream().map(GamePlugin::getGameName).collect(Collectors.toList());
    }

    public String getGameOverMsg(){
        if (currentPlugin==null){
            return null;
        }
        if (currentPlugin.isGameOver()) {
            return currentPlugin.getGameOverMessage();
        } else {
            return null;
        }
    }

    public String isSquarePlayable(int x, int y){
        if (currentPlugin == null){
            return "";
        } else if (currentPlugin.isMoveValid(x,y)){
            return "playable";
        } else {
            return "";
        }
    }

    public boolean hasGame(){
        return currentPlugin != null;
    }
}