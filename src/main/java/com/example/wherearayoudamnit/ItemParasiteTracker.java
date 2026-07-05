package com.example.wherearayoudamnit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

// INFO IMPORTANTE POUR MOI MEME (yes im french i know) : LES ITEMS SONT ENREGISTRES DANS LA CLASSE ModItems.java
// CE FICHIER NE FAIT QUE DEFINIR LE COMPORTEMENT DE L'ITEM
public class ItemParasiteTracker extends Item
{
    private final String trackerType;

    // 0 = idle, 1 = searching, 2 = found
    private static final ResourceLocation TRACKER_STATE = new ResourceLocation("wherearayoudamnit", "tracker_state");

    public ItemParasiteTracker(String modid, String name, String trackerType)
    {
        this.trackerType = trackerType;
        this.setRegistryName(modid, name);
        this.setUnlocalizedName(modid + "." + name);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxStackSize(1);

        // Property of the tracker (idle/searching/found)
        this.addPropertyOverride(TRACKER_STATE, new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
            {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tracker_state"))
                {
                    return stack.getTagCompound().getInteger("tracker_state");
                }
                return 0;
            }
        });

        // Property for the current frame
        ResourceLocation ANIMATION_FRAME = new ResourceLocation("wherearayoudamnit", "animation_frame");
        this.addPropertyOverride(ANIMATION_FRAME, new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
            {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey("animation_frame"))
                {
                    return stack.getTagCompound().getInteger("animation_frame");
                }
                return 0;
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldItem = player.getHeldItem(hand);

        if (player.getCooldownTracker().hasCooldown(this)) return new ActionResult<>(EnumActionResult.PASS, heldItem);

        if (!world.isRemote)
        {
            player.getCooldownTracker().setCooldown(this, 600);

            if ("dispatcher".equals(trackerType))
            {
                handleDispatcherSearch(heldItem, player);
            }
            if ("beckon".equals(trackerType))
            {
                handleBeckonSearch(heldItem, player);
            }
            if ("rooter".equals(trackerType))
            {
                handleRooterSearch(heldItem, player);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    //Beckon
    private void handleBeckonSearch(ItemStack heldItem, EntityPlayer player)
    {
        setTrackerState(heldItem, 1);

        final ItemStack finalItem = heldItem;
        final EntityPlayer finalPlayer = player;
        final String finalType = trackerType;

        java.util.Timer timer = new java.util.Timer();

        timer.schedule(new java.util.TimerTask()
        {
            @Override
            public void run()
            {
                CommandFindParasite command = new CommandFindParasite();
                int nombreTrouve = command.countOnly(finalPlayer.getServer(), finalPlayer, new String[]{finalType});

                if (nombreTrouve > 0)
                {
                    setTrackerState(finalItem, 2);

                    timer.schedule(new java.util.TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            CommandFindParasite command = new CommandFindParasite();
                            command.executeAndCount(finalPlayer.getServer(), finalPlayer, new String[]{finalType});
                            setTrackerState(finalItem, 0);
                            timer.cancel();
                        }
                    }, 6400);
                }
                else
                {
                    setTrackerState(finalItem, 0);
                    timer.cancel();
                }
            }
        }, 2400);
    }


    //Dispatcher
    private void handleDispatcherSearch(ItemStack heldItem, EntityPlayer player)
    {
        setTrackerState(heldItem, 1);

        final ItemStack finalItem = heldItem;
        final EntityPlayer finalPlayer = player;
        final String finalType = trackerType;

        java.util.Timer timer = new java.util.Timer();

        timer.schedule(new java.util.TimerTask()
        {
            @Override
            public void run()
            {
                CommandFindParasite command = new CommandFindParasite();
                int nombreTrouve = command.countOnly(finalPlayer.getServer(), finalPlayer, new String[]{finalType});

                if (nombreTrouve > 0)
                {
                    setTrackerState(finalItem, 2);

                    timer.schedule(new java.util.TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            CommandFindParasite command = new CommandFindParasite();
                            command.executeAndCount(finalPlayer.getServer(), finalPlayer, new String[]{finalType});
                            setTrackerState(finalItem, 0);
                            timer.cancel();
                        }
                    }, 9000);
                }
                else
                {
                    setTrackerState(finalItem, 0);
                    timer.cancel();
                }
            }
        }, 5400);
    }

    //Rooter
    private void handleRooterSearch(ItemStack heldItem, EntityPlayer player)
    {
        setTrackerState(heldItem, 1);

        final ItemStack finalItem = heldItem;
        final EntityPlayer finalPlayer = player;
        final String finalType = trackerType;

        java.util.Timer timer = new java.util.Timer();

        timer.schedule(new java.util.TimerTask()
        {
            @Override
            public void run()
            {
                CommandFindParasite command = new CommandFindParasite();
                int nombreTrouve = command.countOnly(finalPlayer.getServer(), finalPlayer, new String[]{finalType});

                if (nombreTrouve > 0)
                {
                    setTrackerState(finalItem, 2);

                    timer.schedule(new java.util.TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            CommandFindParasite command = new CommandFindParasite();
                            command.executeAndCount(finalPlayer.getServer(), finalPlayer, new String[]{finalType});
                            setTrackerState(finalItem, 0);
                            timer.cancel();
                        }
                    }, 5600);
                }
                else
                {
                    setTrackerState(finalItem, 0);
                    timer.cancel();
                }
            }
        }, 2600);
    }

    public static void setTrackerState(ItemStack stack, int state)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new net.minecraft.nbt.NBTTagCompound());
        }
        stack.getTagCompound().setInteger("tracker_state", state);
    }
}