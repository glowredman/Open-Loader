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
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;

@Mod("openloader")
public class OpenLoader {
	
	public static final Logger LOGGER = LogManager.getLogger("OpenLoader");
	
	private PackRepository serverPackRepository;
	
	public OpenLoader() {
		
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
		
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LOGGER.info("Adding resource pack finder.");
			Minecraft.getInstance().getResourcePackRepository().addPackFinder(new OpenLoaderPackFinder(Type.RESOURCES));
		}
	}
	
	private void serverAboutToStart(FMLServerAboutToStartEvent event) {
		LOGGER.info("Adding data pack finder.");
		this.serverPackRepository = event.getServer().getPackRepository();
		this.serverPackRepository.addPackFinder(new OpenLoaderPackFinder(Type.DATA));
	}
	
	private void serverStarted(FMLServerStartedEvent event) {
		LOGGER.info("Reloading data packs.");
		this.serverPackRepository.reload();
	}

}
