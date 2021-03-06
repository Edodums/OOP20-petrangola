package petrangola.models.cards;


import petrangola.models.ObservableModel;
import petrangola.utlis.Pair;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface Combination extends Replaceable, ObservableModel, PropertyChangeListener {
  
  /**
   *
   */
  void addPropertyChangeListener();
  
  /**
   * @return the best combination of cards ( the cards with the bigger sum )
   */
  Pair<List<Card>, Integer> getBest();
  
  /**
   * @return simply the list of cards
   */
  List<Card> getCards();
  
  /**
   * @return
   */
  List<Card> getChosenCards();
}
