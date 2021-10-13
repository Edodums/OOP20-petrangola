package main.java.petrangola.views.player;

import javafx.scene.layout.Pane;
import main.java.petrangola.controllers.player.PlayerController;
import main.java.petrangola.models.game.Game;
import main.java.petrangola.models.player.PlayerDetail;

public class UserViewImpl extends AbstractPlayerViewImpl implements UserView {
  public UserViewImpl(final PlayerController playerController, final Game game, final PlayerDetail playerDetail, final Pane layout) {
    super(playerController, game, playerDetail, layout);
  }
}
