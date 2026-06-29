package com.example.wherearayoudamnit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


//INFO IMPORTANTE POUR MOI MEME (yes im french i know) :LES ITEMS SONT ENREGISTRES DANS LA CLASSE ModItems.java (le jeu enregistre par lui meme)
// CE FICHIER NE FAIT QUE DEFINIR LE COMPORTEMENT DE L'ITEM
public class ItemParasiteTracker extends Item
{
    // Type of tracker, either "beckon" or "dispatcher"
    private final String trackerType;

    public ItemParasiteTracker(String name, String trackerType)
    {
        this.trackerType = trackerType;
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote)
        {
            // Execute the command to find the parasite and this is based on the tracker type (trackerType: beckon or dispatcher)
            CommandFindParasite command = new CommandFindParasite();
            command.execute(
                player.getServer(),
                player,
                new String[]{trackerType}
            );
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}