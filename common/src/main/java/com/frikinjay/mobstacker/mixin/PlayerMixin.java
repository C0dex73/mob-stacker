package com.frikinjay.mobstacker.mixin;
import com.frikinjay.mobstacker.MobStacker;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
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
        Player player = (Player) (Object) this;
        int stackSize = MobStacker.getStackSize((Mob) entity);
        ItemStack itemStack = player.getItemInHand(interactionHand);

	if(MobStacker.getEnableSeparator() && entity instanceof Mob && !entity.level().isClientSide()) {
            Identifier separatorIdentifier = mobstacker$getIdentifier();

            if (stackSize > 1 && itemStack.is(BuiltInRegistries.ITEM.getValue(separatorIdentifier))) {
                MobStacker.separateEntity((Mob) entity);
                if(!player.isCreative() && MobStacker.getConsumeSeparator()) {
                    itemStack.setCount(itemStack.getCount() - 1);
                }
            }
        }
	if(MobStacker.getEnableBreeding() && entity instanceof Animal animal && !entity.level().isClientSide()) {
		if (animal.isFood(itemStack) && animal.canFallInLove()) {
			int childCount = Math.min(stackSize, itemStack.getCount()) / 2;
			if (childCount > 0) {
				if (!player.isCreative()) { itemStack.setCount(itemStack.getCount() - childCount); }
				MobStacker.setStackSize((Mob) entity, stackSize + childCount);
				/*System.out.println("Hello, World!");
				Level level = animal.level();

				for (int i = 0; i < 7; i++) {
					double dx = level.getRandom().nextGaussian() * 0.02;
					double dy = level.getRandom().nextGaussian() * 0.02;
					double dz = level.getRandom().nextGaussian() * 0.02;

					level.addParticle(
				        	ParticleTypes.HEART,
				        	animal.getX(),
				        	animal.getY() + 0.5,
				        	animal.getZ(),
				        	dx, dy, dz
					);
					System.out.println("Spawning");
				}
				animal.setAge(12000);*/
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
