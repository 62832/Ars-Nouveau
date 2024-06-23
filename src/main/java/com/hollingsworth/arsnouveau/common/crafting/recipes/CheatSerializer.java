package com.hollingsworth.arsnouveau.common.crafting.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Lazy serialization that takes any codec and creates a stream codec for it.
 * Not performant, but find for recipe types that are not used often.
 */
public class CheatSerializer {

    public static <T>StreamCodec<RegistryFriendlyByteBuf, T> create(MapCodec<T> codec){
        return StreamCodec.of(
                (buf, val) -> CheatSerializer.toNetwork(codec, buf, val), (buf) -> CheatSerializer.fromNetwork(codec, buf)
        );
    }

    public static <T> T fromNetwork(MapCodec<T> codec, RegistryFriendlyByteBuf friendlyByteBuf) {
        return friendlyByteBuf.readJsonWithCodec(codec.codec());
    }

    public static <T> void toNetwork(MapCodec<T> codec, RegistryFriendlyByteBuf friendlyByteBuf, T obj) {
        friendlyByteBuf.writeJsonWithCodec(codec.codec(), obj);
    }
}
