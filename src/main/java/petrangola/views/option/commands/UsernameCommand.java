package main.java.petrangola.views.option.commands;

import main.java.petrangola.controllers.option.OptionController;
import main.java.petrangola.views.components.textFieldView.SimpleTextFieldView;

public class UsernameCommand extends AbstractOptionCommand {
  private final SimpleTextFieldView<String> usernameView;
  
  public UsernameCommand(final SimpleTextFieldView<String> usernameView, final OptionController optionController) {
    super(optionController);
    this.usernameView = usernameView;
  }
  
  @Override
  public void execute() {
    this.optionController.setUsername(this.usernameView.getValue());
  }
}
