package codes.ttp.momoyo;

import com.google.common.collect.ImmutableList;
import gregtech.api.unification.material.IMaterialHandler;
import gregtech.api.unification.material.MaterialIconSet;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.FluidMaterial;
import gregtech.api.unification.material.type.IngotMaterial;
import gregtech.api.unification.material.type.SolidMaterial;

@IMaterialHandler.RegisterMaterialHandler
public class Materials {
    public static final IngotMaterial Mithril = new IngotMaterial(479, "mithril", 0x317BEB, MaterialIconSet.SHINY, 12,
            ImmutableList.of(), IngotMaterial.MatFlags.GENERATE_BOLT_SCREW | DustMaterial.MatFlags.GENERATE_PLATE |
            SolidMaterial.MatFlags.GENERATE_GEAR | DustMaterial.MatFlags.GENERATE_ORE |
            FluidMaterial.MatFlags.GENERATE_PLASMA, gregtech.api.unification.Element.valueOf("Mi"));
}
