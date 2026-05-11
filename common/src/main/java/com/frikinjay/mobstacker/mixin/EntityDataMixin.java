package com.frikinjay.mobstacker.mixin;

import com.frikinjay.mobstacker.ICustomDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.frikinjay.mobstacker.MobStacker.STACK_DATA_KEY;

@Mixin(Entity.class)
public class EntityDataMixin implements ICustomDataHolder {

    @Unique
    private CompoundTag mobstacker$stackData;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void mobstacker$onConstruct(CallbackInfo ci) {
        this.mobstacker$stackData = new CompoundTag();
    }

    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void mobstacker$onSaveWithoutId(ValueOutput output, CallbackInfo ci) {
        output.store(STACK_DATA_KEY, CompoundTag.CODEC, this.mobstacker$stackData);
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void mobstacker$onLoad(ValueInput input, CallbackInfo ci) {
        this.mobstacker$stackData = input.read(STACK_DATA_KEY, CompoundTag.CODEC).orElse(new CompoundTag());
    }

    @Override
    public CompoundTag mobstacker$getCustomData() {
        return this.mobstacker$stackData;
    }
}