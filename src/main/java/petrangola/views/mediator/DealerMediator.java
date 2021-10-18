package main.java.petrangola.views.mediator;

import javafx.scene.layout.Pane;
import main.java.petrangola.controllers.game.GameController;
import main.java.petrangola.controllers.player.DealerController;
import main.java.petrangola.models.cards.Cards;

public interface DealerMediator extends PlayerMediator {
  /**
   *
   * @param gameController
   * @param dealerController
   * @param boardCards
   */
  void initDealerView(final GameController gameController, final DealerController dealerController, final Cards boardCards);
  
  /**
   *
   * @param layout
   */
  void showDealerView(final Pane layout);
  
  /**
   *
   */
  void hideDealerView(final Pane layout);
  
  /**
   *
   */
  void onDealtCards();
}