package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.longbow;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.Weapon;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LongbowBow extends Weapon {
    private HashMap<Player, Integer> _drawing = new HashMap<>();
    private List<Player> _players = new ArrayList<>();
    private int _bowSlot;
    private int _arrowSlot;
    private int _mod;
    private int x = 0;

    public LongbowBow(Game game, SpectateComponent spectateComponent) {
        super("longbow", game, spectateComponent);
    }

    public void addArcher(Player player) {
        _players.add(player);
        addArrow(player, 5);
        player.getInventory().setItem(_bowSlot, getItemStack());
    }

    public void removeArcher(Player player) {
        _players.remove(player);
    }

    public boolean isArcher(Player player) {
        return _players.contains(player);
    }

    //Primary Methods
    private void removePlayer(Player player) {
        if (!_drawing.containsKey(player))
            return;
        _drawing.remove(player);
        player.setWalkSpeed(0.2F);
    }

    public void addArrow(Player player, int amount) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItem(_arrowSlot);
        if (item == null)
            inventory.setItem(_arrowSlot, new ItemStack(Material.ARROW, amount));
        else {
            item.setAmount(item.getAmount() + amount);
            inventory.setItem(_arrowSlot, item);
        }
    }

    private void addArrow(Player player) {
        addArrow(player, 1);
    }

    //Listeners
    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME))
            for (Player player : _players) {
                player.getInventory().setItem(_bowSlot, getItemStack());
                addArrow(player, 4);
            }
        if (event.getNewGameState().equals(GameState.POST_GAME))
            _players.forEach(this::removePlayer);
    }

    @EventHandler
    public void onBowClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
            if (player.getItemInHand().getType().equals(Material.BOW))
                if (player.getInventory().contains(Material.ARROW))
                    if (!_drawing.containsKey(player))
                        _drawing.put(player, 1);
    }

    @Override
    public void onRightClick(Player player) {
        if (player.getInventory().contains(Material.ARROW))
            if (player.getItemInHand().getType().equals(Material.BOW))
                if (_drawing.containsKey(player))
                    _drawing.put(player, 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isArcher(event.getPlayer()))
            removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        if (isArcher(event.getPlayer()))
            removePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLaunch(EntityShootBowEvent event) {
        if (event.getBow() == null)
            return;
        if (!event.getEntityType().equals(EntityType.PLAYER))
            return;
        Player player = (Player) event.getEntity();
        if (!isArcher(player))
            return;
        if (!_drawing.containsKey(player))
            return;

        int mod = _drawing.get(player) / _mod;
        removePlayer(player);

        Vector vector = player.getLocation().getDirection().multiply(mod);
        Arrow arrow = player.launchProjectile(Arrow.class, vector);
        arrow.setCritical(true);
        arrow.setVelocity(vector);
        event.setProjectile(arrow);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!event.getCause().equals(DamageCause.PROJECTILE))
            return;
        if (!_players.contains(event.getDamager()))
            return;
        Player player = (Player) event.getDamager();
        if (player.getItemInHand().getType().equals(getMaterial()))
            event.setDamage(getDamage());
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (isArcher(event.getPlayer()))
            removePlayer(event.getPlayer());
    }

    @Override
    public void run() {
        x++;
        for (Player player : _drawing.keySet()) {
            int level = _drawing.get(player);
            if (level < 50)
                _drawing.put(player, level + 1);
            if (level >= 25)
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, (level - 25) / 4));
        }
        if (x % 400 == 0)
            if (getGameState().equals(GameState.IN_GAME))
                _players.forEach(this::addArrow);
    }

    @Override
    public void onEnable() {
        _bowSlot = getConfigValue("bowSlot", Integer.class);
        _arrowSlot = getConfigValue("arrowSlot", Integer.class);
        _mod = getConfigValue("mod", Integer.class);
        startTask(1, 1);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopTask();
        super.onDisable();
    }
}