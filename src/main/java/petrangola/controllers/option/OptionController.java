package main.java.petrangola.controllers.option;

import main.java.petrangola.utlis.DifficultyLevel;

public interface OptionController {
  /**
   *
   * @param opponentsSize
   */
  void setOpponentsSize(int opponentsSize);
  
  /**
   *
   * @return
   */
  int getOpponentsSize();
  
  /**
   *
   * @param difficulty
   */
  void setDifficulty(DifficultyLevel difficulty);
  
  /**
   *
   * @return
   */
  DifficultyLevel geDifficulty();
  
  /**
   *
   * @param username
   */
  void setUsername(String username);
  
  /**
   *
   * @return
   */
  String getUsername();
  
}