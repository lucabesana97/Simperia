package game.inventory;

public class Inventory {
    public static int SLOTS = 12;
    public Item[] items;

    public Inventory() {
        items = new Item[SLOTS];
    }

    /**
     * Uses an item in the inventory
     *
     * @param position The position of the item to use in the slots array.
     */
    public void useItem(int position) {
        if (items[position] != null && items[position].stackAmount > 0) {
            items[position].use();
            items[position].stackAmount--;

            if (items[position].stackAmount <= 0) {
                removeItem(position);
            }
        }
    }

    /**
     * Removes an item from the inventory
     *
     * @param position The position of the item to remove in the slots array.
     */
    public void removeItem(int position) {
        items[position] = null;
    }

    /**
     * Adds an item to the inventory
     * <p>
     * If the item is already in the inventory, it will increase the quantity of that item.
     * If the item is not in the inventory, it will add it to the first available slot.
     * If there are no available slots, it will not add the item.
     *
     * @param item The item to add to the inventory
     * @return true if the item was added, false if it was not.
     */
    public boolean addItem(Item item) {
        boolean full = true;

        for (int i = 0; i < SLOTS; i++) {
            if (items[i] == null) {
                full = false;
            }

            if (items[i] == item && items[i].stackAmount < Item.MAX_STACK) {
                items[i].stackAmount = items[i].stackAmount + item.stackAmount;

                if (items[i].stackAmount > Item.MAX_STACK) {
                    int remainder = items[i].stackAmount - Item.MAX_STACK;
                    if (remainder == 0)
                        return true;

                    items[i].stackAmount = Item.MAX_STACK;
                    item.stackAmount = remainder;
                    addItem(item);
                } else {
                    item.stackAmount = 0;
                }

                // Calculate the amount of items that can be added to the stack and add
                // only that amount to the current stack and keep adding int the next
                // stack until the whole amount is added.


                return true;
            }
        }

        if (!full) {
            for (int i = 0; i < SLOTS; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    items[i].stackAmount = item.stackAmount;
                    return true;
                }
            }
        }

        return false;
    }
}
