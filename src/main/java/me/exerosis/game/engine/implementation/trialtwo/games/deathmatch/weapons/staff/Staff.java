package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.staff;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.Weapon;
import me.exerosis.game.engine.util.EntityUtil;
import me.exerosis.game.engine.util.VectorUtil;
import me.exerosis.game.engine.util.particles.ProjectileLineEffect;
import me.exerosis.game.engine.util.particles.Shockwave;
import me.exerosis.packet.utils.location.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Staff extends Weapon {
    private FireJavelinCooldown _javelinCooldown;
    private MoltenShockwaveCooldown _shockwaveCooldown;

    public Staff(Game game, SpectateComponent spectateComponent, FireJavelinCooldown javelinCooldown, MoltenShockwaveCooldown shockwaveCooldown) {
        super("staff", game, spectateComponent);
        _javelinCooldown = javelinCooldown;
        _shockwaveCooldown = shockwaveCooldown;
    }


    @Override
    public void onLeftClick(Player player) {
        if (_javelinCooldown.isCooling(player))
            return;
        Location target = LocationUtils.getTarget(player, getRange());
        if (target != null) {
            _javelinCooldown.addPlayer(player);
            LivingEntity entity = LocationUtils.getLivingTarget(player, 30, false);
            if (entity != null) {
                EntityUtil.damage(entity, player, getDamage());
                VectorUtil.knockback(entity, player.getLocation(), getKnockback());
            }
            new ProjectileLineEffect(player.getEyeLocation(), target, 100, 19, 0.01);
        }
    }


    @Override
    public void onRightClick(Player player) {
        if (_shockwaveCooldown.isCooling(player))
            return;
        new Shockwave(5, player);
        _shockwaveCooldown.addPlayer(player);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = stack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Mage's Staff");
        List<String> lore = new ArrayList<>();
        lore.add("(Left Click) Flame Javelin");
        lore.add("Damage: " + getDamage());
        lore.add("Range: " + getRange());
        lore.add(" ");
        lore.add("Shoots a white hot piercing");
        lore.add("javelin of fire at the target.");
        lore.add(ChatColor.STRIKETHROUGH + "-------------------");
        lore.add("(Right Click) Molten Shockwave");
        lore.add("Damage: 1");
        lore.add("Range: 5");
        lore.add(" ");
        lore.add("Creates a shockwave of");
        lore.add("flaming embers that throw");
        lore.add("back adversaries in its path.");
        lore.add(ChatColor.STRIKETHROUGH + "-------------------");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}