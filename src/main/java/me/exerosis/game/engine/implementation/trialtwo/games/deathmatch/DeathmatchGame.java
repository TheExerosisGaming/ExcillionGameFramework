package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.old.core.CoreComponentBundle;
import me.exerosis.game.engine.implementation.old.core.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.core.world.WorldComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.StopMotionComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.corpse.CorpseComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.kits.bundles.DeathmatchKitComponentBundle;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;

public class DeathmatchGame extends Game {

    public DeathmatchGame(Plugin plugin, Arena arena) {
        super(plugin, arena);

        addInstance(new GameFolderManager("deathmatch"));
        addInstance(new WorldComponent("https://www.dropbox.com/s/vbkfazr7t0doi4k/Gladiator%20Arena.zip?dl=1"));
        addInstance(new CoreComponentBundle(GameMode.ADVENTURE));
        addInstance(new CorpseComponent());
        addInstance(new DeathmatchKitComponentBundle());
        addInstance(new StopMotionComponent());

        setDoesFollowDependencyInjection(true);
    }
}