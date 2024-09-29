package me.athlaeos.valhallammo.trading;
import me.athlaeos.valhallammo.ValhallaMMO;
import me.athlaeos.valhallammo.gui.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class VirtualMerchant {
    private final Merchant merchant;
    private final UUID villager;
    private final PlayerMenuUtility playerMenuUtility;
    private List<MerchantRecipe> recipes;
    private int expToGrant = 0;

    private boolean changeRecipes = true;

    public VirtualMerchant(PlayerMenuUtility utility, Villager villager, List<MerchantRecipe> recipes){
        this.playerMenuUtility = utility;
        this.villager = villager.getUniqueId();
        this.merchant = Bukkit.createMerchant(getMenuName());
        this.recipes = recipes;
    }

    public Villager getVillager() {
        return (Villager) ValhallaMMO.getInstance().getServer().getEntity(villager);
    }

    public void setExpToGrant(int expToGrant) {
        this.expToGrant = Math.max(0, expToGrant);
    }

    public int getExpToGrant() {
        return expToGrant;
    }

    public List<MerchantRecipe> getRecipes() {
        if (!changeRecipes) return new ArrayList<>(recipes);
        return recipes;
    }

    public void setRecipes(List<MerchantRecipe> recipes) throws IllegalAccessException {
        if (!changeRecipes) throw new IllegalAccessException("You are not allowed to change the merchant's recipes at this time!");
        this.recipes = recipes;
    }

    public abstract void onClose();
    public abstract void onOpen();

    public abstract String getMenuName();

    public void open(){
        changeRecipes = false;
        this.merchant.setRecipes(recipes);
        VirtualMerchant oldMenu = MerchantListener.getCurrentActiveVirtualMerchant(playerMenuUtility.getOwner());
        if (oldMenu != null) MerchantListener.virtualMerchantClose(playerMenuUtility.getOwner(), oldMenu);
        MerchantListener.setActiveTradingMenu(playerMenuUtility.getOwner(), this);
        playerMenuUtility.getOwner().openMerchant(merchant, true);
        onOpen();
    }

    public Merchant getMerchant(){
        return merchant;
    }
}
