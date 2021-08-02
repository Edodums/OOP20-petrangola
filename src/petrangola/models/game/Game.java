package petrangola.models.game;

import petrangola.models.cards.Cards;
import petrangola.models.player.Dealer;
import petrangola.models.player.Player;

import java.util.List;

public interface Game {
   /**
    *
    * @return
    */
   List<GameObject> getPlayers();
   
   /**
    *
    * @return
    */
   List<Cards> getCards();
   
   /**
    *
    * @return
    */
   int getRound();
   
   /**
    *
    * @param round
    */
   void setRound(int round);
   
   /**
    *
    * @return the number associated to the player that is currently playing
    */
   int getCurrentTurnNumber();
   
   /**
    *
    * @param currentTurnNumber
    */
   void setCurrentTurnNumber(int currentTurnNumber);
   
   /**
    *
    * @return counter value of the times players have knocked, when it reaches n - 1 ( when n players are < 5 ) or 3 the game is finished
    */
   int getKnockerCount();
   
   /**
    *
    * @param knockerCount
    */
   void setKnockerCount(int knockerCount);
   
   /**
    *
    * @return the last one that
    */
   String getLastKnocker();
   
   /**
    *
    * @param lastKnocker
    */
   void setLastKnocker(String lastKnocker);
   
   /**
    *
    * @return the username of the player that is giving cards to other players
    */
   Dealer getDealer();
   
   /**
    *
     * @param dealer
    */
   void setDealer(Dealer dealer);
   
   /**
    *
    * @return the username of the winner
    */
   String getWinner();
   
   /**
    *
     * @param winner
    */
   void setWinner(String winner);
   
   /**
    *
    * @return true if the dealer has taken the board cards
    */
   boolean isOnlyOneRound();
   
}