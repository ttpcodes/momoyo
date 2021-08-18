package codes.ttp.momoyo;

import gregicadditions.GAMaterials;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.GemMaterial;
import gregtech.api.unification.material.type.IngotMaterial;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(
        modid = Momoyo.MOD_ID,
        name = Momoyo.MOD_NAME,
        version = Momoyo.VERSION,
        dependencies = Momoyo.DEPENDENCIES
)
public class Momoyo {
    public static final String MOD_ID = "momoyo";
    public static final String MOD_NAME = "Momoyo";
    public static final String VERSION = "0.0.1";
    public static final String DEPENDENCIES = "required-before:gregtech;required-before:gtadditions";

    private static Logger logger;

    @Mod.Instance(MOD_ID)
    public static Momoyo INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Logging started");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        logger.info("Modifying GregTech/Gregicality material harvest levels");

        setHarvestLevel(Materials.Aluminium, 6);
        setHarvestLevel(Materials.Apatite, 3);
        setHarvestLevel(Materials.BandedIron, 3);
        setHarvestLevel(Materials.BasalticMineralSand, 3);
        setHarvestLevel(Materials.Bornite, 2);
        setHarvestLevel(Materials.BrownLimonite, 3);
        setHarvestLevel(Materials.Calcite, 5);
        setHarvestLevel(Materials.Cassiterite, 2);
        setHarvestLevel(Materials.CassiteriteSand, 2);
        setHarvestLevel(Materials.Chalcocite, 2);
        setHarvestLevel(Materials.Chalcopyrite, 3);
        setHarvestLevel(Materials.Cinnabar, 3);
        setHarvestLevel(Materials.Copper, 2);
        setHarvestLevel(Materials.Cuprite, 2);
        setHarvestLevel(GAMaterials.Fluorite, 2);
        setHarvestLevel(Materials.FullersEarth, 4);
        setHarvestLevel(Materials.GraniticMineralSand, 3);
        setHarvestLevel(Materials.Gypsum, 5);
        setHarvestLevel(Materials.Iron, 3);
        setHarvestLevel(Materials.Lapis, 3);
        setHarvestLevel(Materials.Lazurite, 4);
        setHarvestLevel(Materials.Magnetite, 3);
        setHarvestLevel(Materials.Malachite, 3);
        setHarvestLevel(GAMaterials.PreciousMetal, 4);
        setHarvestLevel(Materials.Pyrite, 3);
        setHarvestLevel(Materials.Ruby, 3);
        setHarvestLevel(Materials.Redstone, 4);
        setHarvestLevel(Materials.Sodalite, 3);
        setHarvestLevel(Materials.Tenorite, 2);
        setHarvestLevel(Materials.Topaz, 4);
        setHarvestLevel(Materials.VanadiumMagnetite, 4);
        setHarvestLevel(Materials.YellowLimonite, 3);

        logger.info("Modification of GregTech/Gregicality material harvest levels complete");
    }

    private void setHarvestLevel(DustMaterial material, int level) {
        Class<? extends DustMaterial> materialClass = material.getClass();
        String name = materialClass.getSimpleName();

        Field harvestLevel;
        try {
            if (material instanceof GemMaterial || material instanceof IngotMaterial) {
                harvestLevel = materialClass.getSuperclass().getSuperclass().getField("harvestLevel");
            } else {
                harvestLevel = materialClass.getField("harvestLevel");
            }
        } catch (NoSuchFieldException e) {
            logger.fatal("Could not get harvestLevel field");
            throw new RuntimeException(e);
        }

        Field modifiers;
        try {
            modifiers = harvestLevel.getClass().getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            logger.fatal("Could not access modifiers of harvestLevel");
            throw new RuntimeException(e);
        }

        modifiers.setAccessible(true);
        try {
            modifiers.setInt(harvestLevel, harvestLevel.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            logger.fatal("Could not set harvestLevel as not final");
            throw new RuntimeException(e);
        }

        try {
            harvestLevel.set(material, level);
        } catch (IllegalAccessException e) {
            logger.fatal("Could not set harvestLevel of {}", name);
            throw new RuntimeException(e);
        }
        logger.info("harvestLevel of {} changed to {}", name, level);
    }
}
