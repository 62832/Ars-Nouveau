package com.hollingsworth.arsnouveau.api.event;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public class EffectResolveEvent extends Event {
    public Level world;
    public @Nullable LivingEntity shooter;
    public HitResult rayTraceResult;
    public Spell spell;
    public SpellContext context;
    public AbstractEffect resolveEffect;
    public SpellStats spellStats;
    public SpellResolver resolver;

    private EffectResolveEvent(Level world, LivingEntity shooter, HitResult result, Spell spell, SpellContext spellContext, AbstractEffect resolveEffect, SpellStats spellStats, SpellResolver spellResolver) {
        this.world = world;
        this.shooter = shooter;
        this.rayTraceResult = result;
        this.spell = spell;
        this.context = spellContext;
        this.resolveEffect = resolveEffect;
        this.spellStats = spellStats;
        this.resolver = spellResolver;
    }

    /**
     * Fired before a glyph is resolved. Can be cancelled to stop resolving.
     */
    public static class Pre extends EffectResolveEvent {
        public Pre(Level world, LivingEntity shooter, HitResult result, Spell spell, SpellContext spellContext, AbstractEffect resolveEffect, SpellStats spellStats, SpellResolver spellResolver) {
            super(world, shooter, result, spell, spellContext, resolveEffect, spellStats, spellResolver);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    /**
     * Fired after a glyph has resolved. Cannot be canceled.
     */
    public static class Post extends EffectResolveEvent {

        public Post(Level world, LivingEntity shooter, HitResult result, Spell spell, SpellContext spellContext, AbstractEffect resolveEffect, SpellStats spellStats, SpellResolver spellResolver) {
            super(world, shooter, result, spell, spellContext, resolveEffect, spellStats, spellResolver);
        }

        @Override
        public boolean isCancelable() {
            return false;
        }
    }
}
