package codes.ttp.momoyo;

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
    public static final String DEPENDENCIES = "required-before:gregtech";

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
        Class<?> gregicalityMaterials;
        try {
            gregicalityMaterials = Class.forName("gregicadditions.GAMaterials");
        } catch (ClassNotFoundException e) {
            logger.fatal("Gregicality materials were not detected");
            throw new RuntimeException(e);
        }

        Class<?> gregtechMaterials;
        try {
            gregtechMaterials = Class.forName("gregtech.api.unification.material.Materials");
        } catch (ClassNotFoundException e) {
            logger.fatal("GregTech Community Edition materials were not detected");
            throw new RuntimeException(e);
        }

        setHarvestLevel("Aluminium", 70, gregtechMaterials);
        setHarvestLevel("Trinium", 70, gregicalityMaterials);

        logger.info("Modification of GregTech/Gregicality material harvest levels complete");
    }

    private void setHarvestLevel(String name, int level, Class<?> materials) {
        Field materialField;
        try {
            materialField = materials.getField(name);
        } catch (NoSuchFieldException e) {
            logger.fatal("{} was not detected as a material", name);
            throw new RuntimeException(e);
        }

        Object material;
        try {
            material = materialField.get(materials);
        } catch (IllegalAccessException e) {
            logger.fatal("Momoyo is forbidden from accessing {} :(", name);
            throw new RuntimeException(e);
        }

        Field harvestLevel;
        Class<?> materialClass = material.getClass();
        try {
            switch (materialClass.getSimpleName()) {
                case "DustMaterial":
                    harvestLevel = materialClass.getField("harvestLevel");
                    break;
                case "GemMaterial":
                case "IngotMaterial":
                    harvestLevel = materialClass.getSuperclass().getSuperclass().getField("harvestLevel");
                    break;
                default:
                    logger.fatal("{} was not recognized as a dust, gem, or ingot", name);
                    throw new RuntimeException("Unhandled material type");
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
