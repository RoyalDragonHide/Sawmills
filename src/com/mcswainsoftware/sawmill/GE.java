package com.mcswainsoftware.sawmill;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import java.util.concurrent.Callable;

public class GE extends Task {

    private boolean active = false;

    public GE(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        active = true;

        active = false;
    }

    @Override
    public boolean activate() {
        return !active &&
                bankAround() &&
                playerIsIdle() &&
                (ctx.backpack.select().id(Sawmill.MAHOGANY_PLANK).count() >= 1 ||
                        ctx.backpack.select().count() == 0);
    }

    private boolean bankAround() {
        return !ctx.bank.select().id(Sawmill.BANKER).isEmpty();
    }
}