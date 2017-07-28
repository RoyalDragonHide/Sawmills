package com.mcswainsoftware.sawmill;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import java.util.concurrent.Callable;

public class Bank extends Task {

    private boolean active = false;
    public int sawsUsed = 0;

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        active = true;
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.open();
            }
        }, 1000, 35);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.presetGear(1, true);
            }
        }, 1000, 35);
        if(ctx.objects.select().id(Sawmill.PORTABLE_SAWMILL_OBJECT).nearest().isEmpty()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.open();
                }
            }, 1000, 35);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.deposit(Sawmill.MAHOGANY_LOG, 1);
                }
            }, 1000, 35);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.withdraw(Sawmill.PORTABLE_SAWMILL, 1);
                }
            }, 1000, 35);
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.close();
            }
        }, 1000, 35);
        if(ctx.objects.select().id(Sawmill.PORTABLE_SAWMILL_OBJECT).nearest().isEmpty()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.backpack.select().id(Sawmill.PORTABLE_SAWMILL).peek().click();
                }
            }, 1000, 35);
            sawsUsed++;
            // MOVE TO THE PROPER AREA
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.input.send("1");
                }
            }, 1000, 35);
        }
        active = false;
    }

    @Override
    public boolean activate() {
        System.out.println(ctx.backpack.select().count());
        return !active &&
                playerIsIdle() &&
                (ctx.backpack.select().id(Sawmill.MAHOGANY_PLANK).count() >= 1 ||
                ctx.backpack.select().count() == 0);
    }


}