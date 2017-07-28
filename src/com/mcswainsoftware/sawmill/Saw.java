package com.mcswainsoftware.sawmill;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

public class Saw extends Task {

    public int planksMade = 0;
    private boolean active = false;

    public Saw(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        active = true;
        final GameObject sawmill = ctx.objects.select().id(Sawmill.PORTABLE_SAWMILL_OBJECT).nearest().peek();
        if (!sawmill.inViewport()) {
            ctx.movement.step(sawmill);
            ctx.camera.turnTo(sawmill);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return sawmill.inViewport();
                }
            }, 200, 20);
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return sawmill.interact("Make Planks");
            }
        }, 1000, 35);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.input.sendln(""+ctx.backpack.select().id(Sawmill.MAHOGANY_LOG).count()) && ctx.input.send("1");
            }
        }, 1000, 35);

        planksMade+=ctx.backpack.select().id(Sawmill.MAHOGANY_PLANK).count();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return playerIsIdle();
            }
        }, 10000, 35);
        active = false;
    }

    @Override
    public boolean activate() {
        return !active &&
            sawmillAround() &&
            playerIsIdle() &&
            (ctx.backpack.select().id(Sawmill.MAHOGANY_LOG).count() >= 1) &&
            ((ctx.backpack.select().id(Sawmill.MAHOGANY_LOG).count() * Sawmill.SAW_FEE - ctx.backpack.moneyPouchCount()) >= 0);
    }

    private boolean sawmillAround() {
        return !ctx.objects.select().id(Sawmill.PORTABLE_SAWMILL_OBJECT).nearest().isEmpty();
    }
}