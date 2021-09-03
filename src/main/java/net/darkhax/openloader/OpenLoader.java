package net.darkhax.openloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.openloader.OpenLoaderPackFinder.Type;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

@Mod("openloader")
public class OpenLoader {
	
	public static final Logger LOGGER = LogManager.getLogger("OpenLoader");
	
	public OpenLoader() {
		
		MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
		
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LOGGER.info("Adding resource pack finder.");
			Minecraft.getInstance().getResourcePackRepository().addPackFinder(new OpenLoaderPackFinder(Type.RESOURCES));
		}
	}
	
	private void onServerStart(FMLServerAboutToStartEvent event) {
		event.getServer().getPackRepository().addPackFinder(new OpenLoaderPackFinder(Type.DATA));
		LOGGER.info("Adding data pack finder.");
	}

}
