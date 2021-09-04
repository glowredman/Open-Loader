package net.darkhax.openloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.openloader.OpenLoaderPackFinder.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

@Mod("openloader")
public class OpenLoader {
	
	public static final Logger LOGGER = LogManager.getLogger("OpenLoader");
	
	public OpenLoader() {
		
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
		
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LOGGER.info("Adding resource pack finder.");
			Minecraft.getInstance().getResourcePackRepository().addPackFinder(new OpenLoaderPackFinder(Type.RESOURCES));
		}
	}
	
	private void serverAboutToStart(FMLServerAboutToStartEvent event) {
		LOGGER.info("Adding data pack finder.");
		PackRepository serverPackRepository = event.getServer().getPackRepository();
		serverPackRepository.addPackFinder(new OpenLoaderPackFinder(Type.DATA));
		serverPackRepository.reload();
	}

}
