package net.darkhax.openloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

import com.google.common.base.Supplier;

import net.minecraft.resources.FilePack;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackInfo.IFactory;

public final class OpenLoaderPackFinder implements IPackFinder {
    
    public static final OpenLoaderPackFinder DATA = new OpenLoaderPackFinder("Data Pack", new File("openloader/data"));
    public static final OpenLoaderPackFinder RESOUCE = new OpenLoaderPackFinder("Resource Pack", new File("openloader/resources"));
    
    private final String type;
    private final File loaderDirectory;
    
    private OpenLoaderPackFinder(String type, File loaderDirectory) {
        
    	this.type = type;
        this.loaderDirectory = loaderDirectory;
        
        try {
            
            Files.createDirectories(loaderDirectory.toPath());
        }
        
        catch (final IOException e) {
            
            OpenLoader.LOGGER.error("Failed to initialize loader.", e);
        }
    }
    

	@Override
	public <T extends ResourcePackInfo> void func_230230_a_(Consumer<T> packs, IFactory<T> factory) {

        for (final File packCandidate : getFilesFromDir(this.loaderDirectory)) {
            
            final boolean isFilePack = packCandidate.isFile() && packCandidate.getName().endsWith(".zip");
            final boolean isFolderPack = !isFilePack && packCandidate.isDirectory() && new File(packCandidate, "pack.mcmeta").isFile();
            
            if (isFilePack || isFolderPack) {
                
                final String packName = "openloader/" + packCandidate.getName();
                
                OpenLoader.LOGGER.info("Loading {} {}.", this.type, packName);
                final T packInfo = ResourcePackInfo.createResourcePack(packName, true, this.getAsPack(packCandidate), factory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.field_232625_a_);
                
                if (packInfo != null) {
                    
                    packs.accept(packInfo);
                }
            }
            
            else {
                
                OpenLoader.LOGGER.error("Failed to load {} from {}. Archive packs must be zips. Folder packs must have a valid pack.mcmeta file.", this.type, packCandidate.getAbsolutePath());
            }
        }
	}
    
    private Supplier<IResourcePack> getAsPack (File file) {
        
        return file.isDirectory() ? () -> new FolderPack(file) : () -> new FilePack(file);
    }
    
    private static File[] getFilesFromDir (File file) {
        
        File[] files = new File[0];
        
        if (file == null) {
            
            OpenLoader.LOGGER.error("Attempted to read from a null file.");
        }
        
        else if (!file.isDirectory()) {
            
            OpenLoader.LOGGER.error("Can not read from {}. It's not a directory.", file.getAbsolutePath());
        }
        
        else {
            
            try {
                
                final File[] readFiles = file.listFiles();
                
                if (readFiles == null) {
                    
                    OpenLoader.LOGGER.error("Could not read from {} due to a system error. This is likely an issue with your computer.", file.getAbsolutePath());
                }
                
                else {
                    
                    files = readFiles;
                }
            }
            
            catch (final SecurityException e) {
                
                OpenLoader.LOGGER.error("Could not read from {}. Blocked by system level security. This is likely an issue with your computer.", file.getAbsolutePath(), e);
            }
        }
        
        return files;
    }
}