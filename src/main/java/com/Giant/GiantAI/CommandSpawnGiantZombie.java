package com.Giant.GiantAI;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.server.MinecraftServer;

public class CommandSpawnGiantZombie extends CommandBase {

    @Override
    public String getName() {
        return "spawnGiantZombie";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/spawnGiantZombie";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GiantZombie zombie = new GiantZombie(sender.getEntityWorld());
        zombie.setPosition(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
        zombie.onInitialSpawn(server.getWorld(0).getDifficultyForLocation(zombie.getPosition()), null);

        sender.getEntityWorld().spawnEntity(zombie);
    }
}

