package main.java.petrangola.views.player;

import main.java.petrangola.views.game.GameObjectView;

public interface PlayerView extends GameObjectView {
  /**
   *
   */
  void showName();
  
  /**
   *
   * @param lifeView
   */
  void showLives(LifeView lifeView);
  
}
