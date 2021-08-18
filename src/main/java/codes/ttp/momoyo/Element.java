package codes.ttp.momoyo;

import net.minecraftforge.common.util.EnumHelper;

public class Element {
    public static void registerElements() {
        Element.registerElement("Mi", 122L, 153L, "Mithril");
    }

    /**
     * register a custom element in gtce
     * @param symbol the chemical symbol of the element
     * @param protons the number of protons of the element
     * @param neutrons the number of neutrons of the element
     * @param name the name of the element
     */
    private static void registerElement(String symbol, long protons, long neutrons, String name) {
        EnumHelper.addEnum(gregtech.api.unification.Element.class, symbol, new Class[]{long.class, long.class,
                long.class, String.class, String.class, boolean.class}, protons, neutrons, -1L, null, name, false);
    }
}
