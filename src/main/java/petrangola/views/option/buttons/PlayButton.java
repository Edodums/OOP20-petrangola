package petrangola.views.option.buttons;

import petrangola.controllers.option.OptionController;
import petrangola.models.option.Option;
import petrangola.utlis.UserAction;
import petrangola.utlis.ViewConstants;
import petrangola.views.components.button.AbstractButtonFX;

import java.util.Objects;

public class PlayButton extends AbstractButtonFX {
  private static final String PLAY = "Play";
  private final OptionController optionController;
  
  public PlayButton(OptionController optionController) {
    super(PLAY);
    this.optionController = optionController;
    this.setDisable(true);
  }
  
  @Override
  public void handleStyle() {
    super.getStyleBuilder()
          .addStyle("-fx-padding: 8 15 15 15;" +
                          "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                          "-fx-background-radius: 8;" +
                          "-fx-background-color:" +
                          "linear-gradient(from 0% 93% to 0% 100%, #a34212 0%, #502b12 100%), #9d4024, #d86e3a," +
                          "radial-gradient(center 50% 50%, radius 100%, #a35e3a, #a54e2c);" +
                          "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                          "-fx-font-weight: bold;" +
                          "-fx-font-size: 1.1em;" +
                          "-fx-text-fill: white;" +
                          "-fx-text-effect: dropshadow( gaussian , #a30000 , 0,0,0,2 );", UserAction.NOTHING)
          .addStyle("-fx-background-color:" +
                          "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a," +
                          "radial-gradient(center 50% 50%, radius 100%, #ea7f4b, #c54e2c);", UserAction.HOVER)
          .addStyle("-fx-padding: 10 15 13 15;-fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;", UserAction.PRESS);
    
    super.handleStyle();
    
    super.get().setMinWidth(ViewConstants.WIDTH.getLength() * 0.35);
    // super.get().setWidth(ViewConstants.WIDTH.getLength() * 0.4);
    super.get().setMaxWidth(ViewConstants.WIDTH.getLength() * 0.5);
  }
  
  @Override
  public void setListeners() {
    super.get().setOnMouseClicked(mouseEvent -> this.optionController.play((Option) super.getData()));
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlayButton)) return false;
    if (!super.equals(o)) return false;
    PlayButton that = (PlayButton) o;
    return optionController.equals(that.optionController);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), optionController);
  }
}