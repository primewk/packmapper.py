# PackMapper
converts a 1.12.2 resource pack to 1.21.1 format. This will handle the major changes needed for compatibility.
## Key Features

**Pack Format Update**: Updates `pack.mcmeta` to use pack format 48 (1.21.1 compatible)

**Texture Migration**:

-   Moves `textures/blocks/` to `textures/block/`
-   Moves `textures/items/` to `textures/item/`
-   Renames specific textures that changed names between versions

**Model Updates**: Updates model files to reference the new texture paths

**Blockstate Updates**: Updates blockstate files to use correct model references

**Sound Updates**: Updates sound event names that changed

## Usage
```bash
# Convert a zip file
python packmapper.py "MyPack_1.12.2.zip" "MyPack_1.21.1.zip"

# Convert a directory
python packmapper.py "MyPack_1.12.2/" "MyPack_1.21.1/"

# Verbose output
python packmapper.py -v "input_pack.zip" "output_pack.zip"
```
## What it handles automatically:

-   Directory structure changes (blocks → block, items → item)
-   Texture file renames (like `stone_granite.png` → `granite.png`)
-   Model and blockstate file updates
-   Pack format version updates
-   Sound event name changes

## Limitations to note:

-   It doesn't add textures for new blocks/items introduced after 1.12.2
-   Complex custom models might need manual review
-   Some advanced features like custom fonts or shaders may need additional work
-   Animation metadata files might need manual checking

The program creates a temporary directory during conversion and cleans up afterward. It provides detailed progress output and error handling for common issues.
