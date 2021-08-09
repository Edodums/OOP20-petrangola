package main.java.petrangola.views;

import javafx.stage.Stage;
import main.java.petrangola.views.action.ActionView;
import main.java.petrangola.views.game.GameView;
import main.java.petrangola.views.option.OptionView;

public interface ViewFactory {
  /**
   *
   * @return
   */
  GameView createGameView();
  
  /**
   *
   */
  OptionView createOptionView();
  
  /**
   *
   * @param primaryStage
   */
  ActionView createActionView(Stage primaryStage);
}