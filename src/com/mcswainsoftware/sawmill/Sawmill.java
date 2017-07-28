package com.mcswainsoftware.sawmill;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="Sawmill", description="P2P: Uses mahogany planks on portable sawmills for profit", properties = "client = 6;")

public class Sawmill extends PollingScript<ClientContext> implements PaintListener {

    public static final Font TAHOMA_TITLE = new Font("Tahoma", Font.BOLD, 20);
    public static final Font TAHOMA_HEADER = new Font("Tahoma", Font.BOLD | Font.ITALIC, 16);
    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    public static final int PORTABLE_SAWMILL = 31043;
    public static final int PORTABLE_SAWMILL_OBJECT = 89769;
    public static final int MAHOGANY_PLANK = 8782;
    public static final int MAHOGANY_LOG = 6332;
    public static final int GE_CLERK = 2241;
    public static final int BANKER = 2718;

    public static final int SAW_FEE = 1350;

    public static final int MAHOGANY_PLANK_PRICE = new GeItem(MAHOGANY_PLANK).price;

    //Portable sawmills buy at +10%
    public static final int PORTABLE_SAWMILL_PRICE = new GeItem(PORTABLE_SAWMILL).price;
    public static final int MAHOGANY_LOG_PRICE = new GeItem(MAHOGANY_LOG).price;

    private List<Task> taskList = new ArrayList<>();
    //private GE ge = new GE(ctx);
    private Saw saw = new Saw(ctx);
    private Bank bank = new Bank(ctx);

    @Override
    public void start() {
        super.start();
        //taskList.add(ge);
        taskList.add(bank);
        taskList.add(saw);
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        int planksMade = saw.planksMade;
        int planksHr = (int) ((planksMade * 3600000D) / getRuntime());
        int gpEarned = (planksMade*Sawmill.MAHOGANY_PLANK_PRICE) - (planksMade*Sawmill.SAW_FEE) -
                (planksMade*Sawmill.MAHOGANY_LOG_PRICE) - (bank.sawsUsed*Sawmill.PORTABLE_SAWMILL_PRICE);
        int gpHr = (int) ((gpEarned * 3600000D) / getRuntime());

        g.setColor(new Color(0f,0f,0f,.75f));
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alphaComposite);
        g.fillRect(0, 0, 435, 320);
        g.setColor(Color.WHITE);

        g.setFont(TAHOMA_TITLE);
        g.drawString("Sawmills by RoyalDragonHide", 10, 20);

        g.setFont(TAHOMA_HEADER);
        g.drawString("Bot Stats:", 10, 50);

        g.setFont(TAHOMA);
        g.drawString(String.format("Planks Made: %,d (%,d /Hr)", planksMade, planksHr), 10, 65);
        g.drawString(String.format("GP Earned: %,d (%,d /Hr)", gpEarned, gpHr), 10, 80);
    }
}