package com.example.wherearayoudamnit;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import org.apache.logging.log4j.Logger;

@Mod(modid = WhereAreYouDamnit.MODID, name = WhereAreYouDamnit.NAME, version = WhereAreYouDamnit.VERSION)
public class WhereAreYouDamnit
{
    public static final String MODID = "wherearayoudamnit";
    public static final String NAME = "Scape and Run: WhereAreYouDamnit";
    public static final String VERSION = "1.30";

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
        event.registerServerCommand(new CommandFindParasite());
    }

    @EventBusSubscriber(value = Side.CLIENT, modid = WhereAreYouDamnit.MODID)
    public static class ClientProxy
    {
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            ModelLoader.setCustomModelResourceLocation(
                ModItems.BECKON_TRACKER, 0,
                new ModelResourceLocation(WhereAreYouDamnit.MODID + ":beckon_tracker", "inventory")
            );
            ModelLoader.setCustomModelResourceLocation(
                ModItems.DISPATCHER_TRACKER, 0,
                new ModelResourceLocation(WhereAreYouDamnit.MODID + ":dispatcher_tracker", "inventory")
            );
            ModelLoader.setCustomModelResourceLocation(
                ModItems.ROOTER_TRACKER, 0,
                new ModelResourceLocation(WhereAreYouDamnit.MODID + ":rooter_tracker", "inventory")
            );
        }
    }
}