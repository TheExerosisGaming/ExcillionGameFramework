package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.extensions.ScoreboardExtension;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.TitleExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.extensions.LobbyExtension;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import org.bukkit.event.Listener;

public class LobbyCountdown extends Countdown implements Listener {

    public LobbyCountdown(Game game, SpawnpointComponent spawnpointComponent, ScoreboardComponent scoreboardComponent) {
        super(game.getGameConfigValue("lobbyCountDownTime", Integer.class));
        addExtension(new SetGameStateExtension(this, game, GameState.PRE_GAME));
        addExtension(new LobbyExtension(game, this, spawnpointComponent));
        addExtension(new TitleExtension(this, game));
        addExtension(new ScoreboardExtension(this, game, scoreboardComponent, "time", "status", "Entering in:"));
    }
}