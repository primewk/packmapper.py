#!/usr/bin/env python3
"""
PackMapper - Minecraft Resource Pack Converter
Converts 1.12.2 resource packs to 1.21.1 format
"""

import os
import json
import shutil
import zipfile
import argparse
from pathlib import Path
from typing import Dict, List, Optional

class PackMapper:
    def __init__(self):
        # Texture mapping from 1.12.2 to 1.21.1 paths
        self.texture_mappings = {
            # Blocks
            "assets/minecraft/textures/blocks/": "assets/minecraft/textures/block/",
            "assets/minecraft/textures/items/": "assets/minecraft/textures/item/",
            
            # Specific texture renames
            "stone_granite.png": "granite.png",
            "stone_granite_smooth.png": "polished_granite.png",
            "stone_diorite.png": "diorite.png",
            "stone_diorite_smooth.png": "polished_diorite.png",
            "stone_andesite.png": "andesite.png",
            "stone_andesite_smooth.png": "polished_andesite.png",
            "red_nether_brick.png": "red_nether_bricks.png",
            "mushroom_block_skin_brown.png": "brown_mushroom_block.png",
            "mushroom_block_skin_red.png": "red_mushroom_block.png",
            "mushroom_block_skin_stem.png": "mushroom_stem.png",
            "concrete_powder_black.png": "black_concrete_powder.png",
            "concrete_powder_blue.png": "blue_concrete_powder.png",
            "concrete_powder_brown.png": "brown_concrete_powder.png",
            "concrete_powder_cyan.png": "cyan_concrete_powder.png",
            "concrete_powder_gray.png": "gray_concrete_powder.png",
            "concrete_powder_green.png": "green_concrete_powder.png",
            "concrete_powder_light_blue.png": "light_blue_concrete_powder.png",
            "concrete_powder_lime.png": "lime_concrete_powder.png",
            "concrete_powder_magenta.png": "magenta_concrete_powder.png",
            "concrete_powder_orange.png": "orange_concrete_powder.png",
            "concrete_powder_pink.png": "pink_concrete_powder.png",
            "concrete_powder_purple.png": "purple_concrete_powder.png",
            "concrete_powder_red.png": "red_concrete_powder.png",
            "concrete_powder_silver.png": "light_gray_concrete_powder.png",
            "concrete_powder_white.png": "white_concrete_powder.png",
            "concrete_powder_yellow.png": "yellow_concrete_powder.png",
        }
        
        # Model file mappings
        self.model_mappings = {
            "models/block/": "models/block/",
            "models/item/": "models/item/",
        }
        
        # New textures that should be copied from vanilla if not present
        self.new_textures_1_21 = [
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
            "item/recovery_compass_31.png",
        ]

    def extract_pack(self, input_path: str, temp_dir: str) -> str:
        """Extract resource pack to temporary directory"""
        if zipfile.is_zipfile(input_path):
            with zipfile.ZipFile(input_path, 'r') as zip_ref:
                zip_ref.extractall(temp_dir)
            return temp_dir
        elif os.path.isdir(input_path):
            extracted_path = os.path.join(temp_dir, "extracted_pack")
            shutil.copytree(input_path, extracted_path)
            return extracted_path
        else:
            raise ValueError("Input must be a zip file or directory")

    def update_pack_mcmeta(self, pack_path: str):
        """Update pack.mcmeta to 1.21.1 format"""
        mcmeta_path = os.path.join(pack_path, "pack.mcmeta")
        
        if os.path.exists(mcmeta_path):
            with open(mcmeta_path, 'r', encoding='utf-8') as f:
                mcmeta = json.load(f)
            
            # Update pack format to 1.21.1 (format 48)
            mcmeta["pack"]["pack_format"] = 48
            
            # Add supported formats if not present
            if "supported_formats" not in mcmeta["pack"]:
                mcmeta["pack"]["supported_formats"] = [48]
            
            with open(mcmeta_path, 'w', encoding='utf-8') as f:
                json.dump(mcmeta, f, indent=2)
            
            print("✓ Updated pack.mcmeta to format 48")
        else:
            # Create new pack.mcmeta if it doesn't exist
            mcmeta = {
                "pack": {
                    "pack_format": 48,
                    "supported_formats": [48],
                    "description": "Converted from 1.12.2 to 1.21.1"
                }
            }
            with open(mcmeta_path, 'w', encoding='utf-8') as f:
                json.dump(mcmeta, f, indent=2)
            print("✓ Created new pack.mcmeta")

    def migrate_textures(self, pack_path: str):
        """Migrate and rename textures according to version changes"""
        textures_moved = 0
        textures_renamed = 0
        
        # Handle directory structure changes
        old_blocks_dir = os.path.join(pack_path, "assets/minecraft/textures/blocks")
        new_blocks_dir = os.path.join(pack_path, "assets/minecraft/textures/block")
        
        old_items_dir = os.path.join(pack_path, "assets/minecraft/textures/items")
        new_items_dir = os.path.join(pack_path, "assets/minecraft/textures/item")
        
        # Move blocks directory
        if os.path.exists(old_blocks_dir) and not os.path.exists(new_blocks_dir):
            os.makedirs(os.path.dirname(new_blocks_dir), exist_ok=True)
            shutil.move(old_blocks_dir, new_blocks_dir)
            textures_moved += len(list(Path(new_blocks_dir).rglob("*.png")))
            print(f"✓ Moved blocks directory to block/")
        
        # Move items directory
        if os.path.exists(old_items_dir) and not os.path.exists(new_items_dir):
            os.makedirs(os.path.dirname(new_items_dir), exist_ok=True)
            shutil.move(old_items_dir, new_items_dir)
            textures_moved += len(list(Path(new_items_dir).rglob("*.png")))
            print(f"✓ Moved items directory to item/")
        
        # Rename specific texture files
        for old_name, new_name in self.texture_mappings.items():
            if not old_name.endswith('.png'):
                continue
                
            # Check in both block and item directories
            for base_dir in [new_blocks_dir, new_items_dir]:
                if not os.path.exists(base_dir):
                    continue
                    
                old_file = os.path.join(base_dir, old_name)
                new_file = os.path.join(base_dir, new_name)
                
                if os.path.exists(old_file) and not os.path.exists(new_file):
                    os.rename(old_file, new_file)
                    textures_renamed += 1
        
        if textures_moved > 0:
            print(f"✓ Moved {textures_moved} texture files")
        if textures_renamed > 0:
            print(f"✓ Renamed {textures_renamed} texture files")

    def update_models(self, pack_path: str):
        """Update model files for compatibility"""
        models_updated = 0
        
        models_dir = os.path.join(pack_path, "assets/minecraft/models")
        if not os.path.exists(models_dir):
            return
        
        for root, dirs, files in os.walk(models_dir):
            for file in files:
                if file.endswith('.json'):
                    file_path = os.path.join(root, file)
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            model = json.load(f)
                        
                        # Update texture references in models
                        updated = False
                        if 'textures' in model:
                            for key, texture_path in model['textures'].items():
                                # Update texture path references
                                if 'blocks/' in texture_path:
                                    model['textures'][key] = texture_path.replace('blocks/', 'block/')
                                    updated = True
                                elif 'items/' in texture_path:
                                    model['textures'][key] = texture_path.replace('items/', 'item/')
                                    updated = True
                        
                        if updated:
                            with open(file_path, 'w', encoding='utf-8') as f:
                                json.dump(model, f, indent=2)
                            models_updated += 1
                    
                    except (json.JSONDecodeError, UnicodeDecodeError):
                        print(f"⚠ Warning: Could not parse model file {file_path}")
        
        if models_updated > 0:
            print(f"✓ Updated {models_updated} model files")

    def update_blockstates(self, pack_path: str):
        """Update blockstate files for compatibility"""
        blockstates_updated = 0
        
        blockstates_dir = os.path.join(pack_path, "assets/minecraft/blockstates")
        if not os.path.exists(blockstates_dir):
            return
        
        for file in os.listdir(blockstates_dir):
            if file.endswith('.json'):
                file_path = os.path.join(blockstates_dir, file)
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        blockstate = json.load(f)
                    
                    # Update model references in blockstates
                    updated = False
                    if 'variants' in blockstate:
                        for variant_key, variant_data in blockstate['variants'].items():
                            if isinstance(variant_data, list):
                                for variant in variant_data:
                                    if 'model' in variant and 'blocks/' in variant['model']:
                                        variant['model'] = variant['model'].replace('blocks/', 'block/')
                                        updated = True
                            elif isinstance(variant_data, dict):
                                if 'model' in variant_data and 'blocks/' in variant_data['model']:
                                    variant_data['model'] = variant_data['model'].replace('blocks/', 'block/')
                                    updated = True
                    
                    if updated:
                        with open(file_path, 'w', encoding='utf-8') as f:
                            json.dump(blockstate, f, indent=2)
                        blockstates_updated += 1
                
                except (json.JSONDecodeError, UnicodeDecodeError):
                    print(f"⚠ Warning: Could not parse blockstate file {file_path}")
        
        if blockstates_updated > 0:
            print(f"✓ Updated {blockstates_updated} blockstate files")

    def update_sounds(self, pack_path: str):
        """Update sounds.json if present"""
        sounds_path = os.path.join(pack_path, "assets/minecraft/sounds.json")
        if os.path.exists(sounds_path):
            try:
                with open(sounds_path, 'r', encoding='utf-8') as f:
                    sounds = json.load(f)
                
                # Update sound event names that changed
                sound_mappings = {
                    "block.wood.break": "block.wood.destroy",
                    "block.wood.fall": "block.wood.fall",
                    "block.wood.hit": "block.wood.hit",
                    "block.wood.place": "block.wood.place",
                    "block.wood.step": "block.wood.step",
                }
                
                updated = False
                for old_sound, new_sound in sound_mappings.items():
                    if old_sound in sounds:
                        sounds[new_sound] = sounds.pop(old_sound)
                        updated = True
                
                if updated:
                    with open(sounds_path, 'w', encoding='utf-8') as f:
                        json.dump(sounds, f, indent=2)
                    print("✓ Updated sounds.json")
            
            except (json.JSONDecodeError, UnicodeDecodeError):
                print("⚠ Warning: Could not parse sounds.json")

    def create_output_pack(self, processed_pack_path: str, output_path: str):
        """Create the final output pack"""
        if output_path.endswith('.zip'):
            # Create zip file
            with zipfile.ZipFile(output_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
                for root, dirs, files in os.walk(processed_pack_path):
                    for file in files:
                        file_path = os.path.join(root, file)
                        arcname = os.path.relpath(file_path, processed_pack_path)
                        zipf.write(file_path, arcname)
            print(f"✓ Created output pack: {output_path}")
        else:
            # Copy as directory
            if os.path.exists(output_path):
                shutil.rmtree(output_path)
            shutil.copytree(processed_pack_path, output_path)
            print(f"✓ Created output directory: {output_path}")

    def convert_pack(self, input_path: str, output_path: str):
        """Main conversion function"""
        print(f"Starting conversion of {input_path}")
        print("=" * 50)
        
        # Create temporary directory
        temp_dir = "temp_pack_conversion"
        if os.path.exists(temp_dir):
            shutil.rmtree(temp_dir)
        os.makedirs(temp_dir)
        
        try:
            # Extract pack
            print("Extracting resource pack...")
            extracted_path = self.extract_pack(input_path, temp_dir)
            
            # Update pack.mcmeta
            print("Updating pack metadata...")
            self.update_pack_mcmeta(extracted_path)
            
            # Migrate textures
            print("Migrating textures...")
            self.migrate_textures(extracted_path)
            
            # Update models
            print("Updating model files...")
            self.update_models(extracted_path)
            
            # Update blockstates
            print("Updating blockstate files...")
            self.update_blockstates(extracted_path)
            
            # Update sounds
            print("Updating sound files...")
            self.update_sounds(extracted_path)
            
            # Create output
            print("Creating output pack...")
            self.create_output_pack(extracted_path, output_path)
            
            print("=" * 50)
            print("✅ Conversion completed successfully!")
            print(f"Output saved to: {output_path}")
            print("\nNote: You may need to manually add textures for new blocks/items")
            print("introduced between 1.12.2 and 1.21.1 for complete coverage.")
        
        finally:
            # Cleanup
            if os.path.exists(temp_dir):
                shutil.rmtree(temp_dir)


def main():
    parser = argparse.ArgumentParser(
        description="PackMapper - Convert Minecraft resource packs from 1.12.2 to 1.21.1"
    )
    parser.add_argument(
        "input", 
        help="Input resource pack (zip file or directory)"
    )
    parser.add_argument(
        "output", 
        help="Output path for converted pack"
    )
    parser.add_argument(
        "--verbose", "-v",
        action="store_true",
        help="Enable verbose output"
    )
    
    args = parser.parse_args()
    
    # Validate input
    if not os.path.exists(args.input):
        print(f"Error: Input path '{args.input}' does not exist")
        return 1
    
    # Create output directory if needed
    output_dir = os.path.dirname(args.output)
    if output_dir and not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    # Run conversion
    converter = PackMapper()
    try:
        converter.convert_pack(args.input, args.output)
        return 0
    except Exception as e:
        print(f"❌ Conversion failed: {e}")
        return 1


if __name__ == "__main__":
    exit(main())
