package com.example.wherearayoudamnit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(value = Side.CLIENT, modid = WhereAreYouDamnit.MODID)
public class ClientEventHandler
{
    private static int animationTick = 0;
    private static int lastState = 0;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        EntityPlayer player = mc.player;

        // Look for the tracker item in the player's inventory or hands
        ItemStack trackerItem = getTrackerFromHandOrInventory(player);

        if (trackerItem == null)
        {
            animationTick = 0;
            lastState = 0;
            return;
        }

        if (!trackerItem.hasTagCompound()) return;

        int currentState = trackerItem.getTagCompound().getInteger("tracker_state");

        // Reset animationTick if the state has changed
        if (currentState != lastState)
        {
            animationTick = 0;
            lastState = currentState;
        }

        String itemName = trackerItem.getItem().getRegistryName() != null
                ? trackerItem.getItem().getRegistryName().getResourcePath()
                : "";

        if ("dispatcher_tracker".equals(itemName))
        {
            if (currentState == 1)
            {
                int frame = (animationTick / 2) % 57;
                trackerItem.getTagCompound().setInteger("animation_frame", frame);
            }
            else if (currentState == 2)
            {
                int frame = (animationTick / 4) % 47;
                trackerItem.getTagCompound().setInteger("animation_frame", frame);
            }
            else
            {
                trackerItem.getTagCompound().setInteger("animation_frame", 0);
            }
        }

        if ("beckon_tracker".equals(itemName))
        {
            if (currentState == 1)
            {
                int frame = (animationTick / 2) % 24;
                trackerItem.getTagCompound().setInteger("animation_frame", frame);
            }
            else if (currentState == 2)
            {
                int frame = (animationTick / 4) % 31;
                trackerItem.getTagCompound().setInteger("animation_frame", frame);
            }
            else
            {
                trackerItem.getTagCompound().setInteger("animation_frame", 0);
            }
        }

        animationTick++;
    }

    private static ItemStack getTrackerFromHandOrInventory(EntityPlayer player)
    {
        // Look FOR the tracker item in the player's inventory or hands
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemParasiteTracker)
            {
                if (stack.hasTagCompound() && stack.getTagCompound().getInteger("tracker_state") != 0)
                {
                    return stack;
                }
            }
        }

        // If not, this will return the item in the main hand (even if state = 0)
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemParasiteTracker)
        {
            return heldItem;
        }

        // Check the off-hand (in case)
        ItemStack offhandItem = player.getHeldItem(EnumHand.OFF_HAND);
        if (!offhandItem.isEmpty() && offhandItem.getItem() instanceof ItemParasiteTracker)
        {
            return offhandItem;
        }

        return null;
    }
}