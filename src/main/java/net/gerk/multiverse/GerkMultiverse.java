package net.gerk.multiverse;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

final class FooCommand implements Command<ServerCommandSource>
{
	AtomicInteger n = new AtomicInteger(0);
	FileWriter file;

	public FooCommand()
	{
		try
		{
			file = new FileWriter("outputfile");
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		System.out.println(n.get());
		try
		{
			file.append(String.format("%d\n", n.getAndAdd(1)));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return SINGLE_SUCCESS;
	}
}


public class GerkMultiverse implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		System.out.println("Something here");
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager.literal("foo")
				.executes(new FooCommand()));
		});
	}
}
