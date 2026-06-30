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
import java.util.ArrayList;
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
        executeAndCount(server, sender, args);
    }

    // This method executes the command and returns the number of entities found. FOR TESTING.
    public int executeAndCount(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(new TextComponentString("\u00A7cUsage : /findparasite <beckon|dispatcher>"));
            return 0;
        }

        String argument = args[0].toLowerCase();
        List<String> IdsToSearch;
        String NameOfTheEntity;

        if (argument.equals("beckon"))
        {
            IdsToSearch = IDS_BECKONS;
            NameOfTheEntity = "Beckon";
        }
        else if (argument.equals("dispatcher"))
        {
            IdsToSearch = IDS_DISPATCHERS;
            NameOfTheEntity = "Dispatcher";
        }
        else
        {
            sender.sendMessage(new TextComponentString("\u00A7cUnknown argument : " + argument + ". Use beckon or dispatcher. Thank you :D"));
            return 0;
        }

        World world = server.getEntityWorld();
        List<Entity> AllEntitys = world.loadedEntityList;
        BlockPos positionPlayer = sender.getPosition();

        List<EntityResult> results = new ArrayList<>();

        for (Entity entity : AllEntitys)
        {
            ResourceLocation cleentity = EntityRegistry.getEntry(entity.getClass()) != null
                ? EntityRegistry.getEntry(entity.getClass()).getRegistryName()
                : null;

            if (cleentity != null && IdsToSearch.contains(cleentity.toString()))
            {
                BlockPos positionEntity = entity.getPosition();

                int rangeX = positionEntity.getX() - positionPlayer.getX();
                int rangeZ = positionEntity.getZ() - positionPlayer.getZ();
                int rangeY = positionEntity.getY() - positionPlayer.getY();
                int range = (int) Math.sqrt(rangeX * rangeX + rangeY * rangeY + rangeZ * rangeZ);

                results.add(new EntityResult(cleentity.getResourcePath(), positionEntity.getX(), positionEntity.getY(), positionEntity.getZ(), range));
            }
        }

        results.sort((a, b) -> Integer.compare(a.range, b.range));

        for (EntityResult result : results)
        {
            sender.sendMessage(new TextComponentString(
                "\u00A7e[" + result.name + "] " +
                "\u00A77-> \u00A7aX:" + result.x +
                " Y:" + result.y +
                " Z:" + result.z +
                " \u00A77(\u00A7cYou are " + result.range + " blocks away\u00A77)"
            ));
        }

        if (results.isEmpty())
        {
            sender.sendMessage(new TextComponentString("\u00A7cNo " + NameOfTheEntity + " found in your world."));
        }
        else
        {
            String accord = results.size() > 1 ? "s" : "";
            sender.sendMessage(new TextComponentString("\u00A77Total : \u00A7e" + results.size() + " " + NameOfTheEntity + accord + " found."));
        }

        return results.size();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    private static class EntityResult
    {
        String name;
        int x, y, z, range;

        EntityResult(String name, int x, int y, int z, int range)
        {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.range = range;
        }
    }
}