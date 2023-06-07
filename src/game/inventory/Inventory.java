package game.inventory;

public class Inventory {
    public static int SLOTS = 12;
    public InventorySlot[] slots;

    public Inventory() {
        slots = new InventorySlot[SLOTS];
    }

    /**
     * Uses an item in the inventory
     *
     * @param position The position of the item to use in the slots array.
     */
    public void useItem(int position) {
        if (slots[position].item != null && slots[position].quantity > 0) {
            slots[position].item.use();
            slots[position].quantity--;

            if (slots[position].quantity <= 0) {
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
        slots[position].item = null;
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
    public boolean addItem(InventoryItem item) {
        boolean full = true;

        for (int i = 0; i < SLOTS; i++) {
            if (slots[i] == null) {
                full = false;
            }

            if (slots[i].item == item && slots[i].quantity < item.maxStack) {
                slots[i].quantity++;
                return true;
            }
        }

        if (!full) {
            for (int i = 0; i < SLOTS; i++) {
                if (slots[i] == null) {
                    slots[i].item = item;
                    slots[i].quantity = 1;
                    return true;
                }
            }
        }

        return false;
    }
}
