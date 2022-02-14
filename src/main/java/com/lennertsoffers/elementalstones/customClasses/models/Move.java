package com.lennertsoffers.elementalstones.customClasses.models;

public class Move {

    private Runnable move;
    private int cooldown;
    private long blockedUntil;

    public Runnable getMove() {
        return move;
    }

    public void setMove(Runnable move) {
        this.move = move;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean activateMove() {
        if (this.move != null) {
            if (!this.isOnCooldown()) {
                this.move.run();
                this.block();
                return true;
            }
        }
        return false;
    }

    public void block() {
        this.blockedUntil = System.currentTimeMillis() + cooldown;
    }

    public void clearMove() {
        this.move = null;
    }

    public boolean isNull() {
        return this.move != null;
    }

    public boolean isOnCooldown() {
        return this.blockedUntil <= System.currentTimeMillis();
    }

    public long secondsUntilActive() {
        long miliseconds = this.blockedUntil - System.currentTimeMillis();
        miliseconds = miliseconds < 0 ? 0 : miliseconds;
        miliseconds *= 1000;
        return miliseconds;
    }
}
