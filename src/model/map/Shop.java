package model.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import game.data.GameDataClick;
import model.Backpack;
import model.Item;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.item.common.Sword;

/**
 * Represents a Shop room in the floor.
 * The hero can buy and sell items here.
 */
public final class Shop implements Room {
    /** Number of items in the shop */
    private final int SHOP_SIZE = 5;

    /** Floor number where the shop is located */
    private int floor;

    /** List of coordinates of rooms that can be accessed from this shop */
    private final List<XY> accessible = new ArrayList<>();

    /** Current items in the shop with their prices */
    private final Map<Item, Integer> currentShop = new LinkedHashMap<>();

    /** Log message for shop interactions */
    private String logShop = "Bienvenue au shop !";

    /**
     * Constructor for the Shop
     * 
     * @param floor2 floor number of the shop
     */
    public Shop(int floor2) {
        floor = floor2;
        createShop();
    }

    /**
     * Initialize the shop with random items, avoiding duplicates
     */
    private void createShop() {
        boolean alreadyThere = false;
        while (currentShop.size() != SHOP_SIZE) {
            var item = RandomItem.generate(floor);
            for (var itemBag : currentShop.keySet()) {
                if (itemBag.info().ID() == item.info().ID()) {
                    alreadyThere = true;
                }
            }
            if (!alreadyThere) currentShop.put(item, item.info().score());
            alreadyThere = false;
        }
    }

    /**
     * Buy the first item in the shop if the hero has enough gold
     * 
     * @param backpack hero's backpack
     */
    public void buy(Backpack backpack) {
        var map = currentShop;
        Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
        var item = it.next();
        if (backpack.getGoldInBag() >= item.getValue()) {
            logShop = setLog(item.getKey());
            backpack.subGoldInBag(item.getValue());
            it.remove();
            GameDataClick.addDragItem(item.getKey());
        } else {
            logShop = "T'as pas la thune pour acheter ça";
        }
        if (map.isEmpty()) {
            logShop = "Y a plus rien à acheter, reviens plus tard";
        }
    }

    /**
     * Set the log message when selling an item
     * 
     * @param item item being sold
     */
    public void setSellItemPrice(Item item) {
        Objects.requireNonNull(item);
        logShop = switch (item) {
            case Gold _ -> "Tu me vends de l'or contre de l'or ?";
            default -> "Je te rachète " + item.toString() + " pour " + item.info().score() / 2 + " gold";
        };
    }

    /**
     * Set the log message after a successful transaction
     * 
     * @param item item involved in the transaction
     */
    public void setSellItem(Item item) {
        Objects.requireNonNull(item);
        logShop = "Transaction effectuée pour " + item.toString();
    }

    /**
     * Returns the shop's message for the purchased item
     * 
     * @param item bought item
     * @return message string
     */
    private String setLog(Item item) {
        return switch (item) {
            case Sword _ -> "T'achètes ça vraiment ?";
            default -> "Très bon achat mon frère";
        };
    }

    /**
     * Get current items in the shop with their prices
     * 
     * @return map of items and their prices
     */
    public Map<Item, Integer> getCurrentShop() {
        return currentShop;
    }

    /**
     * Shift the last item to the front of the shop
     */
    public void rightShiftShop() {
        var map = currentShop;
        Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
        Map.Entry<Item, Integer> last = null;
        while (it.hasNext()) {
            last = it.next();
        }
        map.remove(last.getKey());
        LinkedHashMap<Item, Integer> copy = new LinkedHashMap<>();
        copy.put(last.getKey(), last.getValue());
        copy.putAll(map);
        map.clear();
        map.putAll(copy);
    }

    /**
     * Shift the first item to the end of the shop
     */
    public void leftShiftShop() {
        var map = currentShop;
        Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
        Map.Entry<Item, Integer> first = it.next();
        it.remove();
        map.put(first.getKey(), first.getValue());
    }

    @Override
    public List<XY> getAccessible() {
        return accessible;
    }

    @Override
    public void addAccessible(XY coord) {
        accessible.add(coord);
    }

    /**
     * Get the current shop log message
     * 
     * @return log string
     */
    public String getLogShop() {
        return logShop;
    }
}
