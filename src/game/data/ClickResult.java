package game.data;

/**
 * Represents the result of a click action in the game.
 * 
 * This record stores the type of click performed and an associated value.
 * The value can be any object relevant to the click event (e.g., an item, 
 * a coordinate, a button identifier, etc.).
 * 
 * @param type  The type of click that occurred
 * @param value The object associated with the click, can be null depending on context
 */
public record ClickResult(ClickType type, Object value) {}

