package es.noobcraft.buildbattle.game.gui.banner;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;


public class Banner {
    @Getter
    private final DyeColor color;
    @Getter
    private final List<Pattern> patterns;

    public Banner(@NonNull DyeColor color, List<Pattern> patterns) {
        this.color = color;
        if (patterns == null) this.patterns = new ArrayList<>();
        else this.patterns = patterns;
    }

    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    public ItemStack toBanner() {
        ItemStack itemStack = new ItemStack(Material.BANNER);
        BannerMeta bannerMeta = (BannerMeta)itemStack.getItemMeta();
        //banner#setDyeColor() doesn't work, do  we add a base patter corresponding to the color
        bannerMeta.addPattern(new Pattern(color, PatternType.BASE));
        this.patterns.forEach(bannerMeta::addPattern);
        itemStack.setItemMeta(bannerMeta);
        return itemStack;
    }

    public void clearLastPattern() {
        if (patterns.size() > 1)
            patterns.remove(patterns.size()-1);
    }

    public Pattern getLastPattern() {
        return patterns.get(patterns.size()-1);
    }

    public void setColor(DyeColor color) {
        if (patterns.size() > 1)
            this.patterns.set(0, new Pattern(color, PatternType.BASE));
        else
            this.patterns.add(new Pattern(color, PatternType.BASE));
    }
}