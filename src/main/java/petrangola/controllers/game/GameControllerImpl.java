package petrangola.controllers.game;

import petrangola.models.ObservableModel;
import petrangola.models.board.BoardImpl;
import petrangola.models.cards.Card;
import petrangola.models.cards.CardFactoryImpl;
import petrangola.models.game.Game;
import petrangola.models.player.*;
import petrangola.utlis.DifficultyLevel;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameControllerImpl implements GameController {
  private PlayerFactory playerFactory;
  private Game game;
  
  public GameControllerImpl() {
  
  }
  
  @Override
  public void setPlayerFactory(PlayerFactory playerFactory) {
    this.playerFactory = playerFactory;
  }
  
  @Override
  public void createPlayers(final String username, final DifficultyLevel level, final int size) {
    
    final List<Player> players = new ArrayList<>(this.playerFactory.createNPC(size, level));
    players.add(this.playerFactory.createUser(username));
    
    this.game.setPlayers(players);
  }
  
  @Override
  public void createBoard() {
    this.game.setBoard(new BoardImpl());
  }
  
  @Override
  public void createPlayersDetails() {
    this.game.setPlayersDetails(this.game.getPlayers().stream().map(PlayerDetailImpl::new).collect(Collectors.toList()));
  }
  
  @Override
  public void createHighCards() {
    final int playersSize = (int) this.game.getPlayersDetails().stream().filter(PlayerDetail::isStillAlive).count();
    final List<Card> cards = new CardFactoryImpl()
          .createDeck()
          .stream()
          .limit(playersSize)
          .collect(Collectors.toList());
    
    
    IntStream
          .range(0, playersSize)
          .boxed()
          .forEach(index -> this.game
                .getPlayersDetails()
                .stream()
                .filter(PlayerDetail::isStillAlive)
                .collect(Collectors.toList())
                .get(index)
                .setHighCard(cards.get(index)));
  }
  
  @Override
  public void setDealer() {
    this.game
          .getPlayersDetails()
          .stream()
          .filter(PlayerDetail::isStillAlive)
          .collect(Collectors.toList())
          .stream()
          .collect(Collectors.toMap(PlayerDetail::getPlayer, playerDetail -> playerDetail.getHighCard().getValue()))
          .entrySet()
          .stream()
          .max(Map.Entry.comparingByValue())
          .map(Map.Entry::getKey)
          .ifPresent(dealerPlayer -> {
            dealerPlayer.setIsDealer(true);
            
            final Dealer dealer = new DealerImpl(dealerPlayer);
            
            this.game.setDealer(dealer);
          });
  }
  
  @Override
  public void setTurnNumbers() {
    List<PlayerDetail> playersDetails = this.game.getPlayersDetails().stream().filter(PlayerDetail::isStillAlive).collect(Collectors.toList());
    
    int distance = 0;
    
    for (int index = 0; index <= playersDetails.size() - 1; index++) {
      if (playersDetails.get(index).getPlayer().isDealer()) {
        distance = index;
        break;
      }
    }
    
    Collections.rotate(playersDetails, -distance);
    
    IntStream.range(0, playersDetails.size()).boxed().forEachOrdered(index -> playersDetails.get(index).setTurnNumber(index));
  }
  
  @Override
  public void setWinner(String winner) {
    this.game.setWinner(winner);
  }
  
  @Override
  public boolean checkKnocks() {
    final int size = (int) this.game.getPlayersDetails().stream().filter(PlayerDetail::isStillAlive).count();
    final int knockerCount = this.game.getKnockerCount();
    
    if (size > 4) {
      return knockerCount == 3;
    }
    
    return knockerCount == (size - 1);
  }
  
  @Override
  public void nextTurnNumberHandler() {
    int nextTurnNumber = this.game.getCurrentTurnNumber() + 1;
    int playersListSize = (int) this.game.getPlayersDetails().stream().filter(PlayerDetail::isStillAlive).count();
    
    if (playersListSize - 1 == this.game.getCurrentTurnNumber()) {
      nextTurnNumber = 0;
    }
    
    this.game.setCurrentTurnNumber(nextTurnNumber);
  }
  
  @Override
  public void roundHandler() {
    int nextRound = this.game.getRound() + 1;
    this.game.setRound(nextRound);
  }
  
  @Override
  public void onlyOneRound() {
    this.game.setOnlyOneRound(true);
  }
  
  @Override
  public void addKnock(String username) {
    this.game.setKnockerCount(this.game.getKnockerCount() + 1);
    this.game.setLastKnocker(username);
  }
  
  @Override
  public boolean isLastKnockerPlayerTurn() {
    return this.game
          .getPlayersDetails()
          .stream()
          .filter(PlayerDetail::isStillAlive)
          .filter(playerDetail -> !playerDetail.getPlayer().getUsername().equals(this.game.getLastKnocker()))
          .anyMatch(playerDetail -> playerDetail.getTurnNumber() == this.game.getCurrentTurnNumber());
  }
  
  @Override
  public boolean isLastPlayerTurn() {
    return this.game.getCurrentTurnNumber() == 0;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameControllerImpl)) return false;
    GameControllerImpl that = (GameControllerImpl) o;
    return playerFactory.equals(that.playerFactory) && game.equals(that.game);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(playerFactory, game);
  }
  
  @Override
  public void setModel(ObservableModel game) {
    this.game = (Game) game;
  }
}
