package com.nekromant.zoo.telegram.bot;

public class TelegramUser {

    private boolean isUserChoosedRoom = false;
    private boolean isUserChoosedAnimal = false;

    public boolean isUserChoosedRoom() {
        return isUserChoosedRoom;
    }

    public void setChoosedRoom(boolean userChoosedRoom) {
        isUserChoosedRoom = userChoosedRoom;
    }

    public boolean isUserChoosedAnimal() {
        return isUserChoosedAnimal;
    }

    public void setChoosedAnimal(boolean userChoosedAnimal) {
        isUserChoosedAnimal = userChoosedAnimal;
    }
}
