package net.zonixmc.beacon.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@AllArgsConstructor @Getter
public enum Attribute {

    SPEED("Velocidade", Color.AQUA, PotionEffectType.SPEED, 3),
    STRENGTH("Força", Color.RED, PotionEffectType.INCREASE_DAMAGE, 2),
    RESISTANCE("Resistência", Color.BLUE, PotionEffectType.DAMAGE_RESISTANCE, 2),
    ABSORPTION("Absorção", Color.NAVY, PotionEffectType.ABSORPTION, 2),
    JUMP("Super Pulo", Color.GREEN, PotionEffectType.JUMP, 2),
    REGENERATION("Regeneração", Color.PURPLE, PotionEffectType.REGENERATION, 3),
    HASTE("Pressa", Color.ORANGE, PotionEffectType.FAST_DIGGING, 4),

    MULTIPLE("Múltiplos Efeitos", Color.YELLOW, null, 7),
    RANGE("Raio de Funcionamento", Color.WHITE, null, 10),
    DURABILITY("Durabilidade do Sinalizador", Color.BLACK, null, 10);

    private final String name;
    private final Color color;
    private final PotionEffectType effectType;
    private final int maxLevel;

    public boolean hasEffect() { return effectType != null; }

    public static Attribute get(String name) { return Arrays.stream(values()).filter(context -> context.name().equalsIgnoreCase(name)).findFirst().orElse(null); }

    public static Attribute getByName(String name) { return Arrays.stream(values()).filter(context -> context.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }

    public static Attribute getByOrdinal(int ordinal) { return Arrays.stream(values()).filter(context -> context.ordinal() == ordinal).findFirst().orElse(null); }

}
