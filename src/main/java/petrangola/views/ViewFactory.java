package petrangola.views;

import petrangola.models.option.Option;

public interface ViewFactory {
  /**
   *
   * @param option
   */
  void createGameView(final Option option);
  
  /**
   *
   */
  void createOptionView();
  
  /**
   *
   */
  void createActionView();
}
