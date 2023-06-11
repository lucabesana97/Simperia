package game.inventory;

public class ItemStack {
    public static int MAX_STACK = 16;

    // False for floating items waiting to be picked up, true for items in inventory
    public boolean inInventory;

    public Item item;
    public int amount;

    public ItemStack(Item item, int amount, boolean inInventory) {
        this.item = item;
        this.amount = amount;
        this.inInventory = inInventory;
    }

    public boolean isFull() {
        return (amount >= MAX_STACK);
    }

    /**
     *
     * @param itemStack
     * @return if the slot is full and there are still items left to add,
     * it returns a slot with the amount left. If the slot is not full,
     * it returns null.
     */
//    public ItemStack adjustAmount(ItemStack itemStack) {
//
//
//
////        if (slot.amount > MAX_STACK) {
////            int remainder = slot.amount - MAX_STACK;
////            if (remainder <= 0)
////                return null;
////
////            slot.amount = MAX_STACK;
////            Slot newSlot = new Item(slot.item.name, slot.description, slot.inInventory, slot.coordinates, remainder);
////            return newSlot;
////        } else {
////            slot.amount = 0;
////        }
//
//    }

}
