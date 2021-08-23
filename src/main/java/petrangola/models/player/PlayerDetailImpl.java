package main.java.petrangola.models.player;

import java.beans.PropertyChangeSupport;
import java.util.Objects;
import main.java.petrangola.models.cards.Card;

public class PlayerDetailImpl implements PlayerDetail {
  private static final int INITIAL_LIVES = 3;
  
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final int turnNumber;
  private final Player player;
  
  private int playerLives = INITIAL_LIVES;
  private Card highCard;
  
  public PlayerDetailImpl(final Player player, final int turnNumber) {
    this.player = player;
    this.turnNumber = turnNumber;
  }
  
  @Override
  public Card getHighCard() {
    return this.highCard;
  }
  
  @Override
  public void setHighCard(Card highCard) {
    this.highCard = highCard;
    firePropertyChange("highCard", null, highCard);
  }
  
  @Override
  public int getPlayerLives() {
    return this.playerLives;
  }
  
  @Override
  public void takeLife() {
    final int oldValue = this.playerLives;
    this.playerLives--;
    firePropertyChange("playerLives", oldValue , playerLives);
  }
  
  @Override
  public int getTurnNumber() {
    return this.turnNumber;
  }
  
  @Override
  public Player getPlayer() {
    return this.player;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlayerDetailImpl)) return false;
    PlayerDetailImpl that = (PlayerDetailImpl) o;
    return getTurnNumber() == that.getTurnNumber() && getPlayerLives() == that.getPlayerLives() && getPlayer().equals(that.getPlayer()) && getHighCard().equals(that.getHighCard());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getTurnNumber(), getPlayer(), getPlayerLives(), getHighCard());
  }
  
  @Override
  public PropertyChangeSupport getSupport() {
    return this.support;
  }
}
