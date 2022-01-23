package com.dappsindia.telegrammoviebot;

public class MovieAndChannelUpdater {

    private static MovieAndChannelUpdater movieAndChannelUpdater = null;
    private static boolean isMovie = false;
    private CurrentMenu currentMenu;

    public static MovieAndChannelUpdater getInstance() {
        if (movieAndChannelUpdater == null) {
            movieAndChannelUpdater = new MovieAndChannelUpdater();
        }
        return movieAndChannelUpdater;
    }

    public void setMenuListener(CurrentMenu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void updateMenu() {
        isMovie = !isMovie;
        currentMenu.menu(isMovie);
    }

}
