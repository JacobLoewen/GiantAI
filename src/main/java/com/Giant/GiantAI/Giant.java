package com.Giant.GiantAI;

import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.logging.Logger;

@Mod(modid = Giant.MODID, name = Giant.NAME, version = Giant.VERSION)
public class Giant {

    public static final String MODID = "giantai";
    public static final String NAME = "Giant AI";
    public static final String VERSION = "0.0.1-1.12";
    @Mod.Instance(MODID)
    public static Giant instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EntityRegistry.registerModEntity(new ResourceLocation(Giant.MODID, "giant_zombie"), GiantZombie.class, "giant_zombie", 0, instance, 80, 3, true, 0x996600, 0x00FF00);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // Register our command with the server
        event.registerServerCommand(new CommandSpawnGiantZombie());
    }
}
