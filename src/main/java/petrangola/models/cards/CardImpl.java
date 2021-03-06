package petrangola.models.cards;

import petrangola.utlis.Delimiter;
import petrangola.utlis.Name;
import petrangola.utlis.Suit;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public class CardImpl implements Card {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final Name name;
  private final Suit suit;
  private boolean isCovered = false;
  private boolean isHidden = false;
  private boolean isChosen = false;
  
  public CardImpl(final Name name, final Suit suit) {
    this.name = name;
    this.suit = suit;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Name getName() {
    return this.name;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getFullName() {
    String value;
    
    switch (getName()) {
      case RE:
        value = "10";
        break;
      case CAVALLO:
        value = "9";
        break;
      case FANTE:
        value = "8";
        break;
      case ASSO:
        value = "1";
        break;
      default:
        value = String.valueOf(getValue());
    }
    
    
    return getSuit().toString().toLowerCase()
                 .concat(Delimiter.UNDERSCORE.getText())
                 .concat(value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Suit getSuit() {
    return this.suit;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getValue() {
    return getName().getValue();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHidden() {
    return this.isHidden;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setHidden(boolean hidden) {
    this.isHidden = hidden;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCovered() {
    return this.isCovered;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCovered(boolean covered) {
    this.isCovered = covered;
  }
  
  @Override
  public boolean isChosen() {
    return this.isChosen;
  }
  
  @Override
  public void setChosen(boolean chosen) {
    boolean oldValue = this.isChosen;
    this.isChosen = chosen;
    firePropertyChange("isChosen", oldValue, chosen);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CardImpl)) return false;
    CardImpl card = (CardImpl) o;
    return getName() == card.getName() && getSuit() == card.getSuit();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(getName(), getSuit());
  }
  
  @Override
  public PropertyChangeSupport getSupport() {
    return this.support;
  }
}
