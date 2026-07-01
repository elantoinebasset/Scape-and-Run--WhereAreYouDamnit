package com.example.wherearayoudamnit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModItems
{
    public static Item BECKON_TRACKER;
    public static Item DISPATCHER_TRACKER;

    public static void register()
    {
        // Item to find the Beckons
        BECKON_TRACKER = new ItemParasiteTracker(WhereAreYouDamnit.MODID, "beckon_tracker", "beckon");
        ForgeRegistries.ITEMS.register(BECKON_TRACKER);

        registerDispatcherTracker();
    }

    private static void registerDispatcherTracker()
    {
        // Item to find the Dispatchers
        DISPATCHER_TRACKER = new ItemParasiteTracker(WhereAreYouDamnit.MODID, "dispatcher_tracker", "dispatcher");
        ForgeRegistries.ITEMS.register(DISPATCHER_TRACKER);
    }
}