package me.exerosis.game.engine.implementation.trialtwo.components.countdown.countdowns;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.countdown.StartGameStateCountdown;
import me.exerosis.game.engine.core.GameState;
import me.exerosis.game.engine.util.TimeUtil;
import me.exerosis.reflection.data.Pair;
import org.bukkit.entity.Player;

public class GameCountdown extends StartGameStateCountdown {
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private ScoreboardCompoent _scoreboardCompoent;
    private int _time;

    public GameCountdown() {
    }

    @Override
    public void onEnable() {
        _time = _gameFolderManager.getGameConfigValue("gameCountDownTime", Integer.class);
        super.onEnable();
    }

    @Override
    public int getTime() {
        return _time;
    }

    @Override
    public GameState getStartGameState() {
        return GameState.IN_GAME;
    }

    @Override
    public GameState getNextGameState() {
        return GameState.POST_GAME;
    }

    @Override
    public Pair<String, String> mod(int timeLeft) {
        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboardCompoent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardCompoent.blueBold("Ends in:"), "status");
                scoreboard.editLine(ScoreboardCompoent.gray(TimeUtil.formatTime(timeLeft)), "time");
            }
        }
        return null;
    }
}