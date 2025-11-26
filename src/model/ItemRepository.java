package model;

import java.util.HashMap;
import java.util.Map;

public class ItemRepository {

    // Stocke les armes par ID
    private static final Map<Integer, Item> item_lst = new HashMap<>();

    // Méthode pour enregistrer une arme
    public static void registerWeapon(Item item) {
        item_lst.put(item.id(), item);
    }

    // Récupérer une arme par ID
    public static Item getWeapon(int id) {
        return item_lst.get(id);
    }


}

