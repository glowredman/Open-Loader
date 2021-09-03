package net.darkhax.openloader;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.PackConstructor;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.loading.FMLPaths;

public class OpenLoaderPackFinder implements RepositorySource {
    
    private final Type type;
    
    public OpenLoaderPackFinder(Type type) {
        
        this.type = type;
    }
    
    @Override
    public void loadPacks (Consumer<Pack> consumer, PackConstructor factory) {
        
        for (final File candidate : this.type.getDirectory().listFiles()) {
            
            final boolean isFilePack = candidate.isFile() && candidate.getName().endsWith(".zip");
            final boolean isFolderPack = !isFilePack && candidate.isDirectory() && new File(candidate, "pack.mcmeta").isFile();
            
            if (isFilePack || isFolderPack) {
                
                final String packName = this.type.path + "/" + candidate.getName();
                OpenLoader.LOGGER.info("Loading pack {} from {}.", packName, candidate.getAbsolutePath());
                
                final Supplier<PackResources> packSupplier = candidate.isDirectory() ? () -> new FolderPackResources(candidate) : () -> new FilePackResources(candidate);
                final Pack profile = Pack.create(packName, true, packSupplier, factory, Position.TOP, PackSource.DEFAULT);
                
                if (profile != null) {
                    
                    consumer.accept(profile);
                    OpenLoader.LOGGER.info("Loaded pack {}.", packName);
                }
                
                else {
                    
                	OpenLoader.LOGGER.error("Failed to build pack profile {} from {}.", packName, candidate.getAbsolutePath());
                }
            }
            
            else {
                
            	OpenLoader.LOGGER.error("Skipping over {}. It is not a valid folder or archive/file pack.", candidate.getAbsolutePath());
            }
        }
    }
    
    public static enum Type {
        
        DATA("openloader/data"),
        RESOURCES("openloader/resources");
        
        final String path;
        
        Type(String path) {
            this.path = path;
        }
        
        public File getDirectory () {
            
            final File directory = FMLPaths.GAMEDIR.get().resolve(this.path).toFile();

            directory.mkdirs();
            
            return directory;
        }
    }
}