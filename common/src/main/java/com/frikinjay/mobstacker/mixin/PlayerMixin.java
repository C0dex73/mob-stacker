package com.frikinjay.mobstacker.mixin;

import com.frikinjay.mobstacker.MobStacker;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "interactOn", at = @At("HEAD"))
    private void mobstacker$onInteract(Entity entity, InteractionHand interactionHand, Vec3 vec3, CallbackInfoReturnable<InteractionResult> cir) {
        if(MobStacker.getEnableSeparator() && entity instanceof Mob && !entity.level().isClientSide()) {
            Player player = (Player) (Object) this;
            int stackSize = MobStacker.getStackSize((Mob) entity);
            ItemStack itemStack = player.getItemInHand(interactionHand);

            Identifier separatorIdentifier = mobstacker$getIdentifier();

            if (stackSize > 1 && itemStack.is(BuiltInRegistries.ITEM.getValue(separatorIdentifier))) {
                MobStacker.separateEntity((Mob) entity);
                if(!player.isCreative() && MobStacker.getConsumeSeparator()) {
                    itemStack.setCount(itemStack.getCount() - 1);
                }
            }
        }
    }

    @Unique
    private static @NotNull Identifier mobstacker$getIdentifier() {
        String separatorItemId = MobStacker.getSeparatorItem();
        Identifier id = Identifier.tryParse(separatorItemId);
        return id != null ? id : Identifier.parse("minecraft:" + separatorItemId);
    }

}
