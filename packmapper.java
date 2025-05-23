
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * PackMapper - Minecraft Resource Pack Converter
 * Converts 1.12.2 resource packs to 1.21.1 format
 */
public class PackMapper {
    
    private Map<String, String> textureMappings;
    private Map<String, String> modelMappings;
    private List<String> newTextures121;
    private Gson gson;
    
    public PackMapper() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeTextureMappings();
        initializeModelMappings();
        initializeNewTextures();
    }
    
    private void initializeTextureMappings() {
        textureMappings = new HashMap<>();
        
        // Blocks
        textureMappings.put("assets/minecraft/textures/blocks/", "assets/minecraft/textures/block/");
        textureMappings.put("assets/minecraft/textures/items/", "assets/minecraft/textures/item/");
        
        // Specific texture renames
        textureMappings.put("stone_granite.png", "granite.png");
        textureMappings.put("stone_granite_smooth.png", "polished_granite.png");
        textureMappings.put("stone_diorite.png", "diorite.png");
        textureMappings.put("stone_diorite_smooth.png", "polished_diorite.png");
        textureMappings.put("stone_andesite.png", "andesite.png");
        textureMappings.put("stone_andesite_smooth.png", "polished_andesite.png");
        textureMappings.put("red_nether_brick.png", "red_nether_bricks.png");
        textureMappings.put("mushroom_block_skin_brown.png", "brown_mushroom_block.png");
        textureMappings.put("mushroom_block_skin_red.png", "red_mushroom_block.png");
        textureMappings.put("mushroom_block_skin_stem.png", "mushroom_stem.png");
        textureMappings.put("concrete_powder_black.png", "black_concrete_powder.png");
        textureMappings.put("concrete_powder_blue.png", "blue_concrete_powder.png");
        textureMappings.put("concrete_powder_brown.png", "brown_concrete_powder.png");
        textureMappings.put("concrete_powder_cyan.png", "cyan_concrete_powder.png");
        textureMappings.put("concrete_powder_gray.png", "gray_concrete_powder.png");
        textureMappings.put("concrete_powder_green.png", "green_concrete_powder.png");
        textureMappings.put("concrete_powder_light_blue.png", "light_blue_concrete_powder.png");
        textureMappings.put("concrete_powder_lime.png", "lime_concrete_powder.png");
        textureMappings.put("concrete_powder_magenta.png", "magenta_concrete_powder.png");
        textureMappings.put("concrete_powder_orange.png", "orange_concrete_powder.png");
        textureMappings.put("concrete_powder_pink.png", "pink_concrete_powder.png");
        textureMappings.put("concrete_powder_purple.png", "purple_concrete_powder.png");
        textureMappings.put("concrete_powder_red.png", "red_concrete_powder.png");
        textureMappings.put("concrete_powder_silver.png", "light_gray_concrete_powder.png");
        textureMappings.put("concrete_powder_white.png", "white_concrete_powder.png");
        textureMappings.put("concrete_powder_yellow.png", "yellow_concrete_powder.png");
    }
    
    private void initializeModelMappings() {
        modelMappings = new HashMap<>();
        modelMappings.put("models/block/", "models/block/");
        modelMappings.put("models/item/", "models/item/");
    }
    
    private void initializeNewTextures() {
        newTextures121 = Arrays.asList(
            "block/bamboo.png",
            "block/bamboo_sapling.png",
            "block/scaffolding_top.png",
            "block/scaffolding_side.png",
            "block/scaffolding_bottom.png",
            "block/smooth_stone.png",
            "block/smooth_stone_slab_side.png",
            "item/trident.png",
            "item/phantom_membrane.png",
            "item/heart_of_the_sea.png",
            "item/nautilus_shell.png",
            "item/scute.png",
            "block/kelp.png",
            "block/kelp_plant.png",
            "block/dried_kelp_top.png",
            "block/dried_kelp_side.png",
            "block/turtle_egg.png",
            "block/turtle_egg_slightly_cracked.png",
            "block/turtle_egg_very_cracked.png",
            "block/sea_pickle.png",
            "block/conduit.png",
            "item/crossbow_standby.png",
            "item/crossbow_pulling_0.png",
            "item/crossbow_pulling_1.png",
            "item/crossbow_pulling_2.png",
            "item/crossbow_arrow.png",
            "item/crossbow_firework.png",
            "item/suspicious_stew.png",
            "item/sweet_berries.png",
            "block/sweet_berry_bush_stage0.png",
            "block/sweet_berry_bush_stage1.png",
            "block/sweet_berry_bush_stage2.png",
            "block/sweet_berry_bush_stage3.png",
            "item/honeycomb.png",
            "item/honey_bottle.png",
            "block/honey_block_top.png",
            "block/honey_block_side.png",
            "block/honey_block_bottom.png",
            "block/honeycomb_block.png",
            "block/bee_nest_top.png",
            "block/bee_nest_side.png",
            "block/bee_nest_front.png",
            "block/bee_nest_front_honey.png",
            "block/beehive_end.png",
            "block/beehive_side.png",
            "block/beehive_front.png",
            "block/beehive_front_honey.png",
            "item/netherite_ingot.png",
            "item/netherite_scrap.png",
            "block/ancient_debris_top.png",
            "block/ancient_debris_side.png",
            "block/netherite_block.png",
            "block/lodestone_top.png",
            "block/lodestone_side.png",
            "block/respawn_anchor_top_off.png",
            "block/respawn_anchor_top.png",
            "block/respawn_anchor_side0.png",
            "block/respawn_anchor_side1.png",
            "block/respawn_anchor_side2.png",
            "block/respawn_anchor_side3.png",
            "block/respawn_anchor_side4.png",
            "item/recovery_compass_16.png",
            "item/recovery_compass_17.png",
            "item/recovery_compass_18.png",
            "item/recovery_compass_19.png",
            "item/recovery_compass_20.png",
            "item/recovery_compass_21.png",
            "item/recovery_compass_22.png",
            "item/recovery_compass_23.png",
            "item/recovery_compass_24.png",
            "item/recovery_compass_25.png",
            "item/recovery_compass_26.png",
            "item/recovery_compass_27.png",
            "item/recovery_compass_28.png",
            "item/recovery_compass_29.png",
            "item/recovery_compass_30.png",
            "item/recovery_compass_31.png"
        );
    }
    
    /**
     * Extract resource pack to temporary directory
     */
    public String extractPack(String inputPath, String tempDir) throws IOException {
        File inputFile = new File(inputPath);
        
        if (isZipFile(inputPath)) {
            extractZipFile(inputPath, tempDir);
            return tempDir;
        } else if (inputFile.isDirectory()) {
            String extractedPath = Paths.get(tempDir, "extracted_pack").toString();
            copyDirectory(inputPath, extractedPath);
            return extractedPath;
        } else {
            throw new IllegalArgumentException("Input must be a zip file or directory");
        }
    }
    
    private boolean isZipFile(String filePath) {
        try (ZipFile zipFile = new ZipFile(filePath)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private void extractZipFile(String zipPath, String destDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File destFile = new File(destDir, entry.getName());
                
                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    destFile.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry);
                         FileOutputStream out = new FileOutputStream(destFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }
    
    private void copyDirectory(String sourceDir, String destDir) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        Path destPath = Paths.get(destDir);
        
        Files.walk(sourcePath).forEach(source -> {
            try {
                Path dest = destPath.resolve(sourcePath.relativize(source));
                if (Files.isDirectory(source)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Update pack.mcmeta to 1.21.1 format
     */
    public void updatePackMcmeta(String packPath) throws IOException {
        File mcmetaFile = new File(packPath, "pack.mcmeta");
        
        if (mcmetaFile.exists()) {
            try (FileReader reader = new FileReader(mcmetaFile, StandardCharsets.UTF_8)) {
                JsonObject mcmeta = gson.fromJson(reader, JsonObject.class);
                
                // Update pack format to 1.21.1 (format 48)
                JsonObject pack = mcmeta.getAsJsonObject("pack");
                pack.addProperty("pack_format", 48);
                
                // Add supported formats if not present
                if (!pack.has("supported_formats")) {
                    JsonArray supportedFormats = new JsonArray();
                    supportedFormats.add(48);
                    pack.add("supported_formats", supportedFormats);
                }
                
                try (FileWriter writer = new FileWriter(mcmetaFile, StandardCharsets.UTF_8)) {
                    gson.toJson(mcmeta, writer);
                }
                
                System.out.println("✓ Updated pack.mcmeta to format 48");
            }
        } else {
            // Create new pack.mcmeta if it doesn't exist
            JsonObject mcmeta = new JsonObject();
            JsonObject pack = new JsonObject();
            pack.addProperty("pack_format", 48);
            JsonArray supportedFormats = new JsonArray();
            supportedFormats.add(48);
            pack.add("supported_formats", supportedFormats);
            pack.addProperty("description", "Converted from 1.12.2 to 1.21.1");
            mcmeta.add("pack", pack);
            
            try (FileWriter writer = new FileWriter(mcmetaFile, StandardCharsets.UTF_8)) {
                gson.toJson(mcmeta, writer);
            }
            System.out.println("✓ Created new pack.mcmeta");
        }
    }
    
    /**
     * Migrate and rename textures according to version changes
     */
    public void migrateTextures(String packPath) throws IOException {
        int texturesMoved = 0;
        int texturesRenamed = 0;
        
        // Handle directory structure changes
        File oldBlocksDir = new File(packPath, "assets/minecraft/textures/blocks");
        File newBlocksDir = new File(packPath, "assets/minecraft/textures/block");
        
        File oldItemsDir = new File(packPath, "assets/minecraft/textures/items");
        File newItemsDir = new File(packPath, "assets/minecraft/textures/item");
        
        // Move blocks directory
        if (oldBlocksDir.exists() && !newBlocksDir.exists()) {
            newBlocksDir.getParentFile().mkdirs();
            if (oldBlocksDir.renameTo(newBlocksDir)) {
                texturesMoved += countPngFiles(newBlocksDir);
                System.out.println("✓ Moved blocks directory to block/");
            }
        }
        
        // Move items directory
        if (oldItemsDir.exists() && !newItemsDir.exists()) {
            newItemsDir.getParentFile().mkdirs();
            if (oldItemsDir.renameTo(newItemsDir)) {
                texturesMoved += countPngFiles(newItemsDir);
                System.out.println("✓ Moved items directory to item/");
            }
        }
        
        // Rename specific texture files
        for (Map.Entry<String, String> entry : textureMappings.entrySet()) {
            String oldName = entry.getKey();
            String newName = entry.getValue();
            
            if (!oldName.endsWith(".png")) {
                continue;
            }
            
            // Check in both block and item directories
            File[] baseDirs = {newBlocksDir, newItemsDir};
            for (File baseDir : baseDirs) {
                if (!baseDir.exists()) {
                    continue;
                }
                
                File oldFile = new File(baseDir, oldName);
                File newFile = new File(baseDir, newName);
                
                if (oldFile.exists() && !newFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        texturesRenamed++;
                    }
                }
            }
        }
        
        if (texturesMoved > 0) {
            System.out.println("✓ Moved " + texturesMoved + " texture files");
        }
        if (texturesRenamed > 0) {
            System.out.println("✓ Renamed " + texturesRenamed + " texture files");
        }
    }
    
    private int countPngFiles(File directory) {
        int count = 0;
        try {
            Files.walk(directory.toPath())
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(path -> {});
            count = (int) Files.walk(directory.toPath())
                .filter(path -> path.toString().endsWith(".png"))
                .count();
        } catch (IOException e) {
            // Ignore counting errors
        }
        return count;
    }
    
    /**
     * Update model files for compatibility
     */
    public void updateModels(String packPath) throws IOException {
        int modelsUpdated = 0;
        
        File modelsDir = new File(packPath, "assets/minecraft/models");
        if (!modelsDir.exists()) {
            return;
        }
        
        Files.walk(modelsDir.toPath())
            .filter(path -> path.toString().endsWith(".json"))
            .forEach(path -> {
                try {
                    File file = path.toFile();
                    JsonObject model;
                    
                    try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                        model = gson.fromJson(reader, JsonObject.class);
                    }
                    
                    // Update texture references in models
                    boolean updated = false;
                    if (model.has("textures")) {
                        JsonObject textures = model.getAsJsonObject("textures");
                        for (Map.Entry<String, JsonElement> textureEntry : textures.entrySet()) {
                            String texturePath = textureEntry.getValue().getAsString();
                            
                            // Update texture path references
                            if (texturePath.contains("blocks/")) {
                                textures.addProperty(textureEntry.getKey(), texturePath.replace("blocks/", "block/"));
                                updated = true;
                            } else if (texturePath.contains("items/")) {
                                textures.addProperty(textureEntry.getKey(), texturePath.replace("items/", "item/"));
                                updated = true;
                            }
                        }
                    }
                    
                    if (updated) {
                        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                            gson.toJson(model, writer);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Warning: Could not parse model file " + path);
                }
            });
        
        if (modelsUpdated > 0) {
            System.out.println("✓ Updated " + modelsUpdated + " model files");
        }
    }
    
    /**
     * Update blockstate files for compatibility
     */
    public void updateBlockstates(String packPath) throws IOException {
        int blockstatesUpdated = 0;
        
        File blockstatesDir = new File(packPath, "assets/minecraft/blockstates");
        if (!blockstatesDir.exists()) {
            return;
        }
        
        File[] jsonFiles = blockstatesDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null) return;
        
        for (File file : jsonFiles) {
            try {
                JsonObject blockstate;
                try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                    blockstate = gson.fromJson(reader, JsonObject.class);
                }
                
                // Update model references in blockstates
                boolean updated = false;
                if (blockstate.has("variants")) {
                    JsonObject variants = blockstate.getAsJsonObject("variants");
                    for (Map.Entry<String, JsonElement> variantEntry : variants.entrySet()) {
                        JsonElement variantData = variantEntry.getValue();
                        
                        if (variantData.isJsonArray()) {
                            JsonArray variantArray = variantData.getAsJsonArray();
                            for (JsonElement variant : variantArray) {
                                if (variant.isJsonObject() && variant.getAsJsonObject().has("model")) {
                                    JsonObject variantObj = variant.getAsJsonObject();
                                    String model = variantObj.get("model").getAsString();
                                    if (model.contains("blocks/")) {
                                        variantObj.addProperty("model", model.replace("blocks/", "block/"));
                                        updated = true;
                                    }
                                }
                            }
                        } else if (variantData.isJsonObject()) {
                            JsonObject variantObj = variantData.getAsJsonObject();
                            if (variantObj.has("model")) {
                                String model = variantObj.get("model").getAsString();
                                if (model.contains("blocks/")) {
                                    variantObj.addProperty("model", model.replace("blocks/", "block/"));
                                    updated = true;
                                }
                            }
                        }
                    }
                }
                
                if (updated) {
                    try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                        gson.toJson(blockstate, writer);
                    }
                    blockstatesUpdated++;
                }
            } catch (Exception e) {
                System.out.println("⚠ Warning: Could not parse blockstate file " + file.getPath());
            }
        }
        
        if (blockstatesUpdated > 0) {
            System.out.println("✓ Updated " + blockstatesUpdated + " blockstate files");
        }
    }
    
    /**
     * Update sounds.json if present
     */
    public void updateSounds(String packPath) throws IOException {
        File soundsFile = new File(packPath, "assets/minecraft/sounds.json");
        if (soundsFile.exists()) {
            try {
                JsonObject sounds;
                try (FileReader reader = new FileReader(soundsFile, StandardCharsets.UTF_8)) {
                    sounds = gson.fromJson(reader, JsonObject.class);
                }
                
                // Update sound event names that changed
                Map<String, String> soundMappings = new HashMap<>();
                soundMappings.put("block.wood.break", "block.wood.destroy");
                soundMappings.put("block.wood.fall", "block.wood.fall");
                soundMappings.put("block.wood.hit", "block.wood.hit");
                soundMappings.put("block.wood.place", "block.wood.place");
                soundMappings.put("block.wood.step", "block.wood.step");
                
                boolean updated = false;
                for (Map.Entry<String, String> mapping : soundMappings.entrySet()) {
                    String oldSound = mapping.getKey();
                    String newSound = mapping.getValue();
                    
                    if (sounds.has(oldSound)) {
                        JsonElement soundData = sounds.remove(oldSound);
                        sounds.add(newSound, soundData);
                        updated = true;
                    }
                }
                
                if (updated) {
                    try (FileWriter writer = new FileWriter(soundsFile, StandardCharsets.UTF_8)) {
                        gson.toJson(sounds, writer);
                    }
                    System.out.println("✓ Updated sounds.json");
                }
            } catch (Exception e) {
                System.out.println("⚠ Warning: Could not parse sounds.json");
            }
        }
    }
    
    /**
     * Create the final output pack
     */
    public void createOutputPack(String processedPackPath, String outputPath) throws IOException {
        if (outputPath.endsWith(".zip")) {
            // Create zip file
            try (FileOutputStream fos = new FileOutputStream(outputPath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                
                Files.walk(Paths.get(processedPackPath))
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            Path relativePath = Paths.get(processedPackPath).relativize(path);
                            ZipEntry zipEntry = new ZipEntry(relativePath.toString().replace('\\', '/'));
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
            System.out.println("✓ Created output pack: " + outputPath);
        } else {
            // Copy as directory
            File outputDir = new File(outputPath);
            if (outputDir.exists()) {
                deleteDirectory(outputDir);
            }
            copyDirectory(processedPackPath, outputPath);
            System.out.println("✓ Created output directory: " + outputPath);
        }
    }
    
    private void deleteDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }
    
    /**
     * Main conversion function
     */
    public void convertPack(String inputPath, String outputPath) throws IOException {
        System.out.println("Starting conversion of " + inputPath);
        System.out.println("=".repeat(50));
        
        // Create temporary directory
        String tempDir = "temp_pack_conversion";
        File tempDirFile = new File(tempDir);
        if (tempDirFile.exists()) {
            deleteDirectory(tempDirFile);
        }
        tempDirFile.mkdirs();
        
        try {
            // Extract pack
            System.out.println("Extracting resource pack...");
            String extractedPath = extractPack(inputPath, tempDir);
            
            // Update pack.mcmeta
            System.out.println("Updating pack metadata...");
            updatePackMcmeta(extractedPath);
            
            // Migrate textures
            System.out.println("Migrating textures...");
            migrateTextures(extractedPath);
            
            // Update models
            System.out.println("Updating model files...");
            updateModels(extractedPath);
            
            // Update blockstates
            System.out.println("Updating blockstate files...");
            updateBlockstates(extractedPath);
            
            // Update sounds
            System.out.println("Updating sound files...");
            updateSounds(extractedPath);
            
            // Create output
            System.out.println("Creating output pack...");
            createOutputPack(extractedPath, outputPath);
            
            System.out.println("=".repeat(50));
            System.out.println("✅ Conversion completed successfully!");
            System.out.println("Output saved to: " + outputPath);
            System.out.println("\nNote: You may need to manually add textures for new blocks/items");
            System.out.println("introduced between 1.12.2 and 1.21.1 for complete coverage.");
        } finally {
            // Cleanup
            if (tempDirFile.exists()) {
                deleteDirectory(tempDirFile);
            }
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("PackMapper - Convert Minecraft resource packs from 1.12.2 to 1.21.1");
            System.out.println("Usage: java PackMapper <input> <output> [--verbose]");
            System.out.println("  input:  Input resource pack (zip file or directory)");
            System.out.println("  output: Output path for converted pack");
            System.out.println("  --verbose: Enable verbose output");
            System.exit(1);
        }
        
        String input = args[0];
        String output = args[1];
        boolean verbose = args.length > 2 && "--verbose".equals(args[2]) || 
                         args.length > 2 && "-v".equals(args[2]);
        
        // Validate input
        File inputFile = new File(input);
        if (!inputFile.exists()) {
            System.out.println("Error: Input path '" + input + "' does not exist");
            System.exit(1);
        }
        
        // Create output directory if needed
        File outputFile = new File(output);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        // Run conversion
        PackMapper converter = new PackMapper();
        try {
            converter.convertPack(input, output);
            System.exit(0);
        } catch (Exception e) {
            System.out.println("❌ Conversion failed: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
}

