package com.Giant.GiantAI;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.TimerTask;

public class DisplayCountdown extends TimerTask {
    static int counter;

    public void run() {
        if (counter > 0) {
            counter--;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Counter: " + counter));
        }
    }
}
