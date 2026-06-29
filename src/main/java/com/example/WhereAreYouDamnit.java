package com.example.wherearayoudamnit;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WhereAreYouDamnit.MODID, name = WhereAreYouDamnit.NAME, version = WhereAreYouDamnit.VERSION)
public class WhereAreYouDamnit
{
    public static final String MODID = "wherearayoudamnit";
    public static final String NAME = "Scape and Run: WhereAreYouDamnit";
    public static final String VERSION = "1.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ModItems.register();

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        // To save the command inside of the game
        event.registerServerCommand(new CommandFindParasite());
    }
}