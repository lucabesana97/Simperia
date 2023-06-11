package game.inventory;

public class Inventory {
    public static int NUMBER_OF_SLOTS = 12;
    public ItemStack[] slots;

    public Inventory() {
        slots = new ItemStack[NUMBER_OF_SLOTS];
    }

    /**
     * Uses an item in the inventory
     *
     * @param position The position of the item to use in the slots array.
     */
    public void useItem(int position) {
        if (slots[position] != null && slots[position].amount > 0) {
            slots[position].amount--;
            slots[position].item.use();

            if (slots[position].amount <= 0) {
                removeItem(position);
            }
        }
    }

    /**
     * Removes an item from the inventory
     *
     * @param position The position of the item to remove in the slots array.
     */
    public void removeItem(int position) { // Also for dropping items
        slots[position] = null;
    }

    /**
     *
     * @param itemStack
     * @return the remainder after adding the stack if there's no more free available space.
     *  null if all the items were added successfully.
     */
    public ItemStack addStack(ItemStack itemStack) {

        ItemStack remainder = null;
        boolean added = false;
        int firstAvailableSlot = -1;

        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            if (slots[i] == null) {
                firstAvailableSlot = i;
            } else if (slots[i].item == itemStack.item && slots[i].amount <= ItemStack.MAX_STACK) {
                slots[i].amount += itemStack.amount;
                added = true;
                adjustStackAmount(slots[i]);
            }

        }

        // If it's full, it returns
        if (firstAvailableSlot < 0) {
            itemStack.inInventory = false;
            return itemStack;
        }

        //If it isn't full, it adds the items in the first new slot available
        slots[firstAvailableSlot] = itemStack;
        adjustStackAmount(slots[firstAvailableSlot]);

        return remainder;

    }

    public void adjustStackAmount(ItemStack itemStack) {

        if (itemStack.amount < ItemStack.MAX_STACK) {
            itemStack.inInventory = true;
        } else {
            int remainder = itemStack.amount - ItemStack.MAX_STACK;
            itemStack.amount = ItemStack.MAX_STACK;
            ItemStack newStack = new ItemStack(itemStack.item, remainder, true);
            addStack(newStack);
        }
    }

}



