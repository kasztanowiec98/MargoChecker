package org.example.margoclandataget.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Item {
    NONE(0),
    ONEHANDED(1),
    TWOHANDED(2),
    ONEANDAHALFHANDED(3),
    RANGED(4),
    SECONDARY(5),
    WAND(6),
    ORB(7),
    ARMOR(8),
    HELMET(9),
    BOOTS(10),
    GLOVES(11),
    RING(12),
    NECKLACE(13),
    SHIELD(14),
    NEAUTRAL(15),
    CONSUMABLE(16),
    GOLD(17),
    KEY(18),
    QUEST(19),
    RENEWABLE(20),
    ARROW(21),
    CHARM(22),
    BOOK(23),
    BAG(24),
    BLESSING(25),
    ENHANCEMENT(26),
    RECIPE(27),
    CURRENCY(28),
    QUIVER(29),
    OUTFIT(30),
    PET(31),
    TELEPORT(32);

    private final int value;

    private static final Map<Integer, Item> map = new HashMap<>();

    static {
        for (Item item : Item.values()) {
            map.put(item.value, item);
        }
    }
    public static Item valueOf(int value) {
        return map.get(value);
    }
}
