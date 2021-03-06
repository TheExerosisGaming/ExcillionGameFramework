package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.packet.display.displayables.Title;
import me.exerosis.packet.injection.PacketPlayer;
import me.exerosis.packet.injection.handlers.PlayerHandler;
import me.exerosis.reflection.data.Pair;

public class TitleExtension extends CountdownExtension {
    private final Title _title;

    public TitleExtension(Countdown countdown, Game game) {
        super(game, countdown);
        _title = new Title(1, "", "", 0, 4, 0);
    }

    public Pair<String, String> mod(int time) {
        return Pair.of(Integer.toString(time), "");
    }

    @Override
    public void tick(int timeLeft) {
        Pair<String, String> display = mod(timeLeft);
        if (display != null) {
            _title.setTitle(display.getA());
            _title.setSubtitle(display.getB());
        }
    }

    @Override
    public void start(int time) {
        if (time == getCountdown().getTime())
            for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
                player.setDisplayed(_title, true);
    }

    @Override
    public void stop(int index) {
        if (index == 0)
            for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
                player.setDisplayed(_title, false);
    }
}