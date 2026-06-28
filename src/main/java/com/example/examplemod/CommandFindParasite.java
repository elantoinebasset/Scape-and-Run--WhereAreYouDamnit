package com.example.wherearayoudamnit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Arrays;
import java.util.List;

public class CommandFindParasite extends CommandBase
{
    private static final List<String> IDS_BECKONS = Arrays.asList(
        "srparasites:beckon_si",
        "srparasites:beckon_sii",
        "srparasites:beckon_siii",
        "srparasites:beckon_siv"
    );

    private static final List<String> IDS_DISPATCHERS = Arrays.asList(
        "srparasites:dispatcher_si",
        "srparasites:dispatcher_sii",
        "srparasites:dispatcher_siii",
        "srparasites:dispatcher_siv"
    );

    @Override
    public String getName()
    {
        return "findparasite";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/findparasite <beckon|dispatcher> - Show coordinates of the entity that you are looking for";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        // Si aucun argument fourni, on affiche l'aide
        if (args.length == 0)
        {
            sender.sendMessage(new TextComponentString("\u00A7cUsage : /findparasite <beckon|dispatcher>"));
            return;
        }

        String argument = args[0].toLowerCase();
        List<String> idsAChercher;
        String nomAffiche;

        if (argument.equals("beckon"))
        {
            idsAChercher = IDS_BECKONS;
            nomAffiche = "Beckon";
        }
        else if (argument.equals("dispatcher"))
        {
            idsAChercher = IDS_DISPATCHERS;
            nomAffiche = "Dispatcher";
        }
        else
        {
            sender.sendMessage(new TextComponentString("\u00A7cUnknown argument : " + argument + ". Use beckon or dispatcher. Thank you :D"));
            return;
        }

        World monde = server.getEntityWorld();
        List<Entity> toutesLesEntites = monde.loadedEntityList;
        int nombreTrouve = 0;

        for (Entity entite : toutesLesEntites)
        {
            ResourceLocation cleEntite = EntityRegistry.getEntry(entite.getClass()) != null
                ? EntityRegistry.getEntry(entite.getClass()).getRegistryName()
                : null;

            if (cleEntite != null && idsAChercher.contains(cleEntite.toString()))
            {
                BlockPos position = entite.getPosition();
                sender.sendMessage(new TextComponentString(
                    "\u00A7e[" + nomAffiche + "] \u00A7f" + cleEntite.getResourcePath() +
                    " \u00A77-> \u00A7aX:" + position.getX() +
                    " Y:" + position.getY() +
                    " Z:" + position.getZ()
                ));
                nombreTrouve++;
            }
        }

        if (nombreTrouve == 0)
        {
            sender.sendMessage(new TextComponentString("\u00A7cNo " + nomAffiche + " found in you world."));
        }
        else
        {
            sender.sendMessage(new TextComponentString("\u00A77Total : \u00A7e" + nombreTrouve + " " + nomAffiche + "(s) found."));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}