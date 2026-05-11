# MobStacker

Mod ID: `mobstacker`  
Group: `com.frikinjay.mobstacker`  
Minecraft: 26.1.2
Loader: Fabric 0.16.7  
Java: 25

## Project Structure

- `common/` — shared code (MobStacker.java, config, mixins, command, API, entity)
- `fabric/` — Fabric platform entrypoint (`com.frikinjay.mobstacker.fabric.MobStackerFabric`)

## Dependencies

- `almanac` (≥1.0.2) — loaded from `common/libs/*.jar` via `fileTree`
- `letmedespawn` (≥1.4.4)

## Build

```sh
./gradlew build
```

Built jars are in `fabric/build/libs/`.

## Key Porting Context (1.20.1 → 26.1.2)

### Entity Subpackage Changes
- Sheep → `net.minecraft.world.entity.animal.sheep.Sheep`
- Cat → `net.minecraft.world.entity.animal.cat.Cat`
- Fox → `net.minecraft.world.entity.animal.fox.Fox`
- Pig → `net.minecraft.world.entity.animal.pig.Pig`
- Cow → `net.minecraft.world.entity.animal.cow.Cow`
- MushroomCow → `net.minecraft.world.entity.animal.cow.MushroomCow`
- Villager → `net.minecraft.world.entity.npc.Villager`
- ZombieVillager → `net.minecraft.world.entity.monster.ZombieVillager`
- ZombifiedPiglin → `net.minecraft.world.entity.monster.ZombifiedPiglin`
- Axolotl → `net.minecraft.world.entity.animal.axolotl.Axolotl`

### API Replacements
- `ResourceLocation` → `Identifier` (net.minecraft.resources)
- `ResourceLocationArgument` → `IdentifierArgument`
- `MobSpawnType` → `EntitySpawnReason`
- `EntityType.create(...)` → `EntityType.create(ServerLevel, EntitySpawnReason)`
- `isClientSide` field → `isClientSide()` method
- `Registry.get()` → `Registry.getValue()` (returns `Optional<Holder.Reference<T>>`)
- `moveTo(x,y,z,yRot,xRot)` removed; use `setPos` + `setYRot` + `setXRot`
- `deathScore` removed; score-award loop was deleted
- `getCompound(key)` → `getCompoundOrEmpty(key)` (returns `CompoundTag`, never null)
- `getBoolean(key)` → `getBooleanOr(key, default)`
- `getInt(key)` → `getIntOr(key, default)`
- Use `TagValueOutput`/`TagValueInput` for entity NBT save/load
- `Entity#save(CompoundTag)` → `Entity#saveWithoutId(CompoundTag)`
- `Entity#load(CompoundTag)` → `Entity#load(CompoundTag)` (still works, TagValueInput for stack-specific data)
- `VillagerData.getVariant()` removed; use `getVillagerData().type()`
- `Axolotl.setVariant()` / `MushroomCow.setVariant()` are now private; variant is copied via the data tracker instead
- `Sheep.appendShearedAppearance()` → `appendExtraVariantData(ItemStack, ServerLevel, Consumer)`
- `convertTo(EntityType, boolean)` → `convertTo(EntityType, ServerLevel, boolean, EntitySpawnReason)`
- Permission check: `source.hasPermission(level)` → `source.permissions().hasPermission(Permission.HasCommandLevel(level))`
- `world` field → `level()` getter in mixins; `CommandSourceStack.getWorld()` → `CommandSourceStack.getLevel()`
- `InteractionResult.SUCCESS` → `ItemInteractionResult.SUCCESS` / `InteractionResult.SUCCESS` depending on context
- `ToIntegerFunction` → `ToIntFunction` for property codec mapping
