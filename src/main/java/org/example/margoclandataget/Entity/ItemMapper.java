package org.example.margoclandataget.Entity;

import org.example.margoclandataget.Enums.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemMapper {
    private static final Map<Item, String> classNames = new HashMap<>();

    static {
        classNames.put(Item.NONE, "Wszystkie");
        classNames.put(Item.ONEHANDED, "Jednoręczne");
        classNames.put(Item.TWOHANDED, "Dwuręczne");
        classNames.put(Item.ONEANDAHALFHANDED, "Półtoraręczne");
        classNames.put(Item.RANGED, "Dystansowe");
        classNames.put(Item.SECONDARY, "Pomocnicze");
        classNames.put(Item.WAND, "Różdżki");
        classNames.put(Item.ORB, "Orby");
        classNames.put(Item.ARMOR, "Zbroje");
        classNames.put(Item.HELMET, "Hełmy");
        classNames.put(Item.BOOTS, "Buty");
        classNames.put(Item.GLOVES, "Rękawice");
        classNames.put(Item.RING, "Pierścienie");
        classNames.put(Item.NECKLACE, "Naszyjniki");
        classNames.put(Item.SHIELD, "Tarcze");
        classNames.put(Item.NEAUTRAL, "Neutralne");
        classNames.put(Item.CONSUMABLE, "Konsumpcyjne");
        classNames.put(Item.GOLD, "Złoto");
        classNames.put(Item.KEY, "Klucze");
        classNames.put(Item.QUEST, "Questowe");
        classNames.put(Item.RENEWABLE, "Odnawialne");
        classNames.put(Item.ARROW, "Strzały");
        classNames.put(Item.CHARM, "Talizmany");
        classNames.put(Item.BOOK, "Książki");
        classNames.put(Item.BAG, "Torby");
        classNames.put(Item.BLESSING, "Błogosławieństwa");
        classNames.put(Item.ENHANCEMENT, "Ulepszenia");
        classNames.put(Item.RECIPE, "Recepty");
        classNames.put(Item.CURRENCY, "Waluta");
        classNames.put(Item.QUIVER, "Strzały");
        classNames.put(Item.OUTFIT, "Stroje");
        classNames.put(Item.PET, "Maskotki");
        classNames.put(Item.TELEPORT, "Teleporty");
    }
    public static String getClassName(int classNumber) {
        Item itemClass = Item.valueOf(classNumber);
        return classNames.get(itemClass);
    }
}