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
    // Tick counter pour savoir depuis combien de ticks l'animation tourne
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
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

        if (heldItem.isEmpty() || !(heldItem.getItem() instanceof ItemParasiteTracker)) 
        {
            animationTick = 0;
            lastState = 0;
            return;
        }

        if (!heldItem.hasTagCompound()) return;

        int currentState = heldItem.getTagCompound().getInteger("tracker_state");

        if (currentState != lastState)
        {
            animationTick = 0;
            lastState = currentState;
        }

        if (currentState == 1)
        {
            int frameCount = 24;
            int frame = (animationTick / 2) % frameCount;
            heldItem.getTagCompound().setInteger("animation_frame", frame);
        }
        else if (currentState == 2)
        {
            int frameCount = 31;
            int frame = (animationTick / 4) % frameCount;
            heldItem.getTagCompound().setInteger("animation_frame", frame);
        }
        else
        {
            heldItem.getTagCompound().setInteger("animation_frame", 0);
        }

        animationTick++;
    }
}