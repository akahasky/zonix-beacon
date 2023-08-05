package net.zonixmc.beacon.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.ToString;
import net.zonixmc.beacon.attributes.Attribute;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @ToString
public class CustomBeacon {

    private boolean active = false;

    private final Map<Attribute, Integer> attributes = Maps.newEnumMap(Attribute.class);
    private final Set<Integer> activeEffects = Sets.newLinkedHashSet();

    public void setActive(boolean aBoolean) {

        this.active = aBoolean;

    }

    public void putAttribute(Attribute attribute, int level) {

        if (level > attribute.getMaxLevel()) level = attribute.getMaxLevel();

        attributes.put(attribute, level);

    }

    public int getAttributeLevel(Attribute attribute) {

        if (!attributes.containsKey(attribute)) return 0;

        return attributes.get(attribute);

    }

    public boolean canActiveEffect(Attribute attribute) {

        if (activeEffects.size() >= getAttributeLevel(Attribute.MULTIPLE)) return false;

        return attributes.containsKey(attribute);

    }

    public void activeEffect(Attribute attribute) { activeEffects.add(attribute.ordinal()); }

    public void disableEffect(Attribute attribute) { activeEffects.remove(attribute.ordinal()); }

    public boolean isActive(Attribute attribute) { return activeEffects.stream().anyMatch(context -> context == attribute.ordinal()); }

    public static CustomBeacon fromObject(String jsonString) {

        JSONObject object = (JSONObject) JSONValue.parse(jsonString);
        CustomBeacon customBeacon = new CustomBeacon();

        customBeacon.setActive((Boolean) object.get("active"));

        JSONObject attributesObject = (JSONObject) object.get("attributes");

        attributesObject.forEach((key, value) -> {

            Attribute attribute = Attribute.get((String) key);
            int level = ((Long) value).intValue();

            customBeacon.putAttribute(attribute, level);

        });

        String activeEffectsString = ((String) object.get("activeEffects"));

        if (activeEffectsString != null)
            Arrays.stream(activeEffectsString.split("@"))
                    .forEach(context -> customBeacon.getActiveEffects().add(Integer.parseInt(context)));

        return customBeacon;

    }

    public static JSONObject toObject(CustomBeacon customBeacon) {

        JSONObject object = new JSONObject();

        object.put("active", customBeacon.isActive());
        object.put("attributes", new JSONObject(customBeacon.getAttributes()));

        Set<Integer> activeEffects = customBeacon.getActiveEffects();
        String serializedActiveEffects;

        if  (!activeEffects.isEmpty())
            serializedActiveEffects = Strings.join(activeEffects.stream().map(Object::toString).collect(Collectors.toList()), "@");

        else serializedActiveEffects = null;

        object.put("activeEffects", serializedActiveEffects);

        return object;

    }

}
