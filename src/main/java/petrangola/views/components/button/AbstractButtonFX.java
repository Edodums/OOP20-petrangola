package petrangola.views.components.button;

import javafx.scene.control.Button;
import petrangola.dto.DTO;
import petrangola.utlis.UserAction;
import petrangola.views.components.AbstractComponentFX;
import petrangola.views.components.style.StyleBuilder;
import petrangola.views.components.style.StyleBuilderImpl;

import java.util.Objects;

public abstract class AbstractButtonFX extends AbstractComponentFX<Button> implements SimpleButton<Button> {
  private final StyleBuilder styleBuilder = new StyleBuilderImpl();
  private DTO data;
  
  public AbstractButtonFX(String label) {
    super(new Button(label));
    this.handleStyle();
    this.setListeners();
  }
  
  @Override
  public void handleStyle() {
    super.get().setStyle(this.getStyleBuilder().getStyles(UserAction.NOTHING));
    
    super.get().hoverProperty().addListener((observableValue, aBoolean, t1) -> {
      if (t1) {
        super.get().setStyle(this.getStyleBuilder().getStyles(UserAction.HOVER));
      } else {
        super.get().setStyle(this.getStyleBuilder().getStyles(UserAction.NOTHING));
      }
    });
    
    super.get().setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.isConsumed()) {
        super.get().setStyle(this.getStyleBuilder().getStyles(UserAction.NOTHING));
      } else {
        super.get().setStyle(this.getStyleBuilder().getStyles(UserAction.PRESS));
      }
    });
  }
  
  @Override
  public void setDisable(boolean isDisabled) {
    super.get().setDisable(isDisabled);
  }
  
  protected StyleBuilder getStyleBuilder() {
    return this.styleBuilder;
  }
  
  protected DTO getData() {
    return this.data;
  }
  
  @Override
  public void setData(DTO data) {
    this.data = data;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractButtonFX)) return false;
    if (!super.equals(o)) return false;
    AbstractButtonFX that = (AbstractButtonFX) o;
    return getStyleBuilder().equals(that.getStyleBuilder()) && Objects.equals(getData(), that.getData());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getStyleBuilder(), getData());
  }
}
