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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(value = Side.CLIENT, modid = WhereAreYouDamnit.MODID)
public class ClientEventHandler
{
    // IMPORTANT : chaque ItemStack a maintenant SON PROPRE compteur d'animation
    // et SON PROPRE dernier state, au lieu d'une seule variable globale partagee
    // entre tous les trackers. C'est ca qui causait le bug ou un tracker "gelait"
    // quand l'autre etait actif.
    private static final Map<ItemStack, Integer> animationTicks = new HashMap<>();
    private static final Map<ItemStack, Integer> lastStates = new HashMap<>();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        EntityPlayer player = mc.player;

        List<ItemStack> activeTrackers = getAllActiveTrackers(player);

        if (activeTrackers.isEmpty())
        {
            animationTicks.clear();
            lastStates.clear();
            return;
        }

        for (ItemStack trackerItem : activeTrackers)
        {
            if (!trackerItem.hasTagCompound()) continue;

            int currentState = trackerItem.getTagCompound().getInteger("tracker_state");
            Integer lastState = lastStates.get(trackerItem);
            int tick = animationTicks.getOrDefault(trackerItem, 0);

            // Reset le compteur d'animation seulement si CE TRACKER a change de state
            if (lastState == null || currentState != lastState)
            {
                tick = 0;
                lastStates.put(trackerItem, currentState);
            }

            String itemName = trackerItem.getItem().getRegistryName() != null
                    ? trackerItem.getItem().getRegistryName().getResourcePath()
                    : "";

            if ("dispatcher_tracker".equals(itemName))
            {
                if (currentState == 1)
                {
                    int frame = (tick / 2) % 57;
                    trackerItem.getTagCompound().setInteger("animation_frame", frame);
                }
                else if (currentState == 2)
                {
                    int frame = (tick / 4) % 47;
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
                    int frame = (tick / 2) % 24;
                    trackerItem.getTagCompound().setInteger("animation_frame", frame);
                }
                else if (currentState == 2)
                {
                    int frame = (tick / 4) % 31;
                    trackerItem.getTagCompound().setInteger("animation_frame", frame);
                }
                else
                {
                    trackerItem.getTagCompound().setInteger("animation_frame", 0);
                }
            }

            if ("rooter_tracker".equals(itemName))
            {
                if (currentState == 1)
                {
                    int frame = (tick / 2) % 26;
                    trackerItem.getTagCompound().setInteger("animation_frame", frame);
                }
                else if (currentState == 2)
                {
                    int frame = (tick / 2) % 56;
                    trackerItem.getTagCompound().setInteger("animation_frame", frame);
                }
                else
                {
                    trackerItem.getTagCompound().setInteger("animation_frame", 0);
                }
            }

            animationTicks.put(trackerItem, tick + 1);
        }

        // Nettoyage : on retire les entrees des trackers qui ne sont plus actifs
        // pour eviter que les Maps grossissent indefiniment
        animationTicks.keySet().removeIf(stack -> !activeTrackers.contains(stack));
        lastStates.keySet().removeIf(stack -> !activeTrackers.contains(stack));
    }

    // Retourne TOUS les trackers (main hand, off-hand, inventaire) qui ont
    // un tracker_state different de 0, c'est a dire en cours d'animation.
    private static List<ItemStack> getAllActiveTrackers(EntityPlayer player)
    {
        List<ItemStack> found = new ArrayList<>();

        ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!mainHand.isEmpty() && mainHand.getItem() instanceof ItemParasiteTracker)
        {
            found.add(mainHand);
        }

        ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
        if (!offHand.isEmpty() && offHand.getItem() instanceof ItemParasiteTracker && !found.contains(offHand))
        {
            found.add(offHand);
        }

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemParasiteTracker && !found.contains(stack))
            {
                if (stack.hasTagCompound() && stack.getTagCompound().getInteger("tracker_state") != 0)
                {
                    found.add(stack);
                }
            }
        }

        return found;
    }
}