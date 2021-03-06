package petrangola.controllers.option;

import petrangola.models.ObservableModel;
import petrangola.models.option.Option;
import petrangola.utlis.DifficultyLevel;
import petrangola.views.ViewFactory;


public class OptionControllerImpl implements OptionController {
  private Option option;
  private ViewFactory viewFactory;
  
  public OptionControllerImpl() {
  }
  
  @Override
  public void setOpponentsSize(int opponentsSize) {
    this.getOption().setOpponentsSize(opponentsSize);
  }
  
  @Override
  public void setDifficulty(DifficultyLevel difficulty) {
    this.getOption().setDifficultyLevel(difficulty);
  }
  
  @Override
  public void setUsername(String username) {
    this.getOption().setUsername(username);
  }
  
  
  public void play(final Option option) {
    this.getViewFactory().createGameView(option);
  }
  
  @Override
  public void setModel(ObservableModel option) {
    this.option = (Option) option;
  }
  
  private ViewFactory getViewFactory() {
    return this.viewFactory;
  }
  
  @Override
  public void setViewFactory(ViewFactory viewFactory) {
    this.viewFactory = viewFactory;
  }
  
  private Option getOption() {
    return this.option;
  }
}
