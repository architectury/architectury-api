package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

public interface ChunkEvent {
    
    Event<Save> SAVE = EventFactory.createLoop();
    
    Event<Load> LOAD = EventFactory.createLoop();
    
    interface Save {
        /**
         * Invoked when a chunk is saved, just before the data is written.
         * Add your own data to the {@link CompoundTag} parameter to get your data saved as well.
         * Equal to Forge's {@code ChunkDataEvent.Save}.
         *
         * @param chunk The chunk that is saved.
         * @param level The level the chunk is in.
         * @param nbt The chunk data that is written to the save file.
         */
        void save(ChunkAccess chunk, ServerLevel level, CompoundTag nbt);
    }
    
    interface Load {
        /**
         * Invoked just before a chunk is fully read.
         * You can read out your own data from the {@link CompoundTag} parameter, when you have saved one before.
         * Equal to Forge's {@code ChunkDataEvent.Load}.
         *
         * @param chunk The chunk that is loaded.
         * @param type The type of the chunk.
         * @param nbt THe chunk data that was read from the save file.
         */
        void load(ChunkAccess chunk, ChunkStatus.ChunkType type, CompoundTag nbt);
    }
    
}
