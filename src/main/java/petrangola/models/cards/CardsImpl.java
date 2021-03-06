package petrangola.models.cards;

import petrangola.models.board.Board;
import petrangola.models.game.GameObject;
import petrangola.models.player.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.Optional;

public class CardsImpl implements Cards {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final GameObject gameObject;
  private Combination combination;
  
  
  public CardsImpl(final Combination combination, final GameObject gameObject) {
    this.combination = combination;
    this.combination.addPropertyChangeListener(this);
    
    this.gameObject = gameObject;
  }
  
  @Override
  public Combination getCombination() {
    return this.combination;
  }
  
  @Override
  public void setCombination(Combination combination) {
    this.combination.removePropertyChangeListener(this);
    this.combination = combination;
    this.combination.addPropertyChangeListener(this);
    firePropertyChange("updatedCombination", null, combination);
  }
  
  @Override
  public boolean isCommunity() {
    return getBoard().isPresent();
  }
  
  @Override
  public boolean isPlayerCards() {
    return getPlayer().isPresent();
  }
  
  @Override
  public Optional<Player> getPlayer() {
    return gameObject instanceof Player ?  Optional.of((Player) gameObject) : Optional.empty();
  }
  
  @Override
  public Optional<Board> getBoard() {
    return gameObject instanceof Board ?  Optional.of((Board) gameObject) : Optional.empty();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CardsImpl)) return false;
    CardsImpl cards = (CardsImpl) o;
    return gameObject.equals(cards.gameObject) && getCombination().equals(cards.getCombination());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getSupport(), gameObject, getCombination());
  }
  
  @Override
  public PropertyChangeSupport getSupport() {
    return this.support;
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.setCombination((Combination) evt.getNewValue());
  }
}
