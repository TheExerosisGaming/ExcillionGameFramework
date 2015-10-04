package me.exerosis.game.engine.implementation.old.game.spleef;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.cooldown.CooldownManager;
import me.exerosis.game.engine.implementation.old.core.pause.PauseCompoent;
import me.exerosis.game.engine.implementation.old.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpleefBlockBreakCompoent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private PauseCompoent _pauseComponent;
    @Depend
    private CooldownManager _cooldownManager;

    public SpleefBlockBreakCompoent() {
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL))
            return;
        if (!getArena().getGameState().equals(GameState.IN_GAME))
            return;
        if (_pauseComponent.isPaused())
            return;
        if (!_spectateComponent.getGamePlayers().contains(event.getPlayer()))
            return;

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            if (block.getType().equals(Material.BEDROCK))
                return;
            int newFoodLevel = player.getFoodLevel() == 20 ? 20 : player.getFoodLevel() + 1;

            player.setFoodLevel(newFoodLevel);
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
            block.setType(Material.AIR);
        }
    }
}