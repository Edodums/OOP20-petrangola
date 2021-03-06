package petrangola.views.mediator;

import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import petrangola.controllers.game.GameController;
import petrangola.models.cards.Cards;
import petrangola.models.game.Game;
import petrangola.models.player.Dealer;
import petrangola.models.player.Player;
import petrangola.models.player.PlayerDetail;
import petrangola.utlis.Pair;
import petrangola.utlis.position.Horizontal;
import petrangola.utlis.position.Vertical;
import petrangola.views.GameObjectViewFactory;
import petrangola.views.board.BoardView;
import petrangola.views.cards.*;
import petrangola.views.components.layout.LayoutBuilder;
import petrangola.views.game.GameStyleClass;
import petrangola.views.player.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardsMediatorImpl implements CardsMediator {
  private static final Pair<Vertical, Horizontal> BOARD_POSITION = new Pair<>(Vertical.CENTER, Horizontal.CENTER);
  private static final Pair<Vertical, Horizontal> USER_POSITION = new Pair<>(Vertical.CENTER, Horizontal.LEFT);
  private static final Pair<Vertical, Horizontal> DEALER_POSITION = new Pair<>(Vertical.CENTER, Horizontal.LEFT);
  private static final int THRESHOLD_NPC = 6;
  
  private final CardsExchanged cardsExchanged = new CardsExchangedImpl();
  private final List<GameObjectView> viewList = new CopyOnWriteArrayList<>();
  
  private final GameObjectViewFactory gameObjectViewFactory;
  private final CardsViewFactory cardsViewFactory;
  
  private final List<Cards> cardsList;
  private final List<PlayerDetail> playersDetails;
  
  private GameController gameController;
  private LayoutBuilder layoutBuilder;
  
  private int npcIndex = 0;
  
  public CardsMediatorImpl(GameObjectViewFactory gameObjectViewFactory, CardsViewFactory cardsViewFactory, List<Cards> cardsList, List<PlayerDetail> playersDetails) {
    this.gameObjectViewFactory = gameObjectViewFactory;
    this.cardsViewFactory = cardsViewFactory;
    this.cardsList = cardsList;
    this.playersDetails = playersDetails;
    
    this.init();
  }
  
  private void init() {
    this.createBoard();
    
    this.cardsList.forEach(cards -> {
      if (cards.isCommunity()) {
        return;
      }
      
      cards.getPlayer().ifPresent(player -> {
        this.playersDetails
              .stream()
              .filter(PlayerDetail::isStillAlive)
              .filter(playerDetail -> playerDetail.getPlayer().equals(player))
              .findFirst()
              .ifPresent(playerDetail -> {
                final Pair<GameObjectView, CardsView<Group>> pair;
                
                if (player.isNPC()) {
                  pair = this.createNPCs(playerDetail, cards);
                } else if (player.isDealer()) {
                  pair = this.createDealer(playerDetail, cards);
                } else {
                  pair = this.createUser(playerDetail, cards);
                }
                
                pair.getX().setCardsView(pair.getY());
                
                pair.getX().addListenerToModel(cards);
                pair.getX().addListenerToModel(playerDetail);
                pair.getX().addListenerToModel(player);
                
                this.getBoardView().addListenerToModel(player);
                
                this.getViewList().add(pair.getX());
              });
      });
    });
  }
  
  private void createBoard() {
    cardsList
          .stream()
          .filter(Cards::isCommunity)
          .findFirst()
          .ifPresent(cards -> {
            BoardView boardView = getGameObjectViewFactory().createBoardView();
            boardView.setCardsView(getCardsViewFactory().createBoardCards(cards, BOARD_POSITION));
            boardView.addListenerToModel(cards);
            boardView.setCardsExchanged(cardsExchanged);
            
            this.getViewList().add(boardView);
          });
  }
  
  private Pair<GameObjectView, CardsView<Group>> createUser(PlayerDetail playerDetail, Cards cards) {
    final UserView userView = getGameObjectViewFactory().createUserView(playerDetail);
    userView.setCardsExchanged(cardsExchanged);
    
    return new Pair<>(userView, getCardsViewFactory().createUserCards(cards, USER_POSITION));
  }
  
  private Pair<GameObjectView, CardsView<Group>> createDealer(PlayerDetail playerDetail, Cards cards) {
    final DealerView dealerView = getGameObjectViewFactory().createDealerView(playerDetail);
    dealerView.setCardsExchanged(cardsExchanged);
    
    return new Pair<>(dealerView, getCardsViewFactory().createDealerCards(cards, DEALER_POSITION));
  }
  
  private Pair<GameObjectView, CardsView<Group>> createNPCs(PlayerDetail playerDetail, Cards cards) {
    npcIndex++;
    return new Pair<>(getGameObjectViewFactory().createNPCView(playerDetail), getCardsViewFactory().createOpponentCards(cards, npcIndex, THRESHOLD_NPC));
  }
  
  @Override
  public void showBoardCards() {
    getBoardView().showCards();
  }
  
  private BoardView getBoardView() {
    return (BoardView) getViewList().get(0);
  }
  
  @Override
  public void showNPCCards() {
    getNPCViews().forEach(GameObjectView::showCards);
  }
  
  @Override
  public void showUserCards() {
    getViewList()
          .stream()
          .filter(gameObjectView -> {
            if (gameObjectView.getCardsView().getCards().isCommunity()) {
              return false;
            }
            
            Optional<Player> player = gameObjectView.getCardsView().getCards().getPlayer();
            
            return player.isEmpty() || !player.get().isNPC();
          })
          .findFirst()
          .ifPresent(GameObjectView::showCards);
  }
  
  @Override
  public void setCurrentPlayerCards(CurrentPlayer currentPlayer) {
    getPlayerView(currentPlayer).ifPresent(gameObjectView -> currentPlayer.setCards(gameObjectView.getCardsView().getCards()));
  }
  
  @Override
  public void hideDealerView(Pane layout) {
    getUserDealerView().ifPresent(dealerView -> {
      final DealerView tempDealerView = (DealerView) dealerView;
      tempDealerView.hideView(layout);
    });
  }
  
  @Override
  public void showDealerView(Pane layout) {
    getUserDealerView().ifPresent(dealerView -> {
      final DealerView tempDealerView = (DealerView) dealerView;
      tempDealerView.setGameController(getGameController());
      tempDealerView.init(getBoardView().getCardsView().getCards());
      tempDealerView.showView(layout);
    });
  }
  
  @Override
  public void register(Pane layout) {
    PlayerView userPlayerView = this.getUserPlayerView();
    userPlayerView.register(layout, this.getLayoutBuilder());
    
    this.getNPCViews().forEach(npcView -> npcView.register(layout, this.getLayoutBuilder()));
    
    this.getBoardView().register(layout, getLayoutBuilder());
    this.getBoardView().setExchangeButton(((UpdatableCombination) userPlayerView).getExchangeButton());
  }
  
  @Override
  public void unregister(Pane layout) {
    layout.lookupAll(GameStyleClass.NPC_CARDS.getAsStyleClass())
          .stream()
          .map(node -> (FlowPane) node)
          .forEach(sidePane -> sidePane.getChildren().clear());
    
    final Pane userCardsPane = (Pane) layout.lookup(GameStyleClass.USER_CARDS.getAsStyleClass());
    userCardsPane.getChildren().clear();
    
    final Pane boardPane = (Pane) layout.lookup(GameStyleClass.BOARD_CARDS.getAsStyleClass());
    boardPane.getChildren().clear();
  }
  
  @Override
  public List<GameObjectView> getViewList() {
    return this.viewList;
  }
  
  @Override
  public Pane getLayout() {
    return this.getLayoutBuilder().getLayout();
  }
  
  @Override
  public void updatePlayerViews(Game game, Player oldDealer, Player newDealer) {
    final List<PlayerDetail> tempPlayersDetails = game.getPlayersDetails().stream().filter(PlayerDetail::isStillAlive).collect(Collectors.toList());
    final List<GameObjectView> tempPlayerViews = getViewList().stream().filter(gameObjectView -> !gameObjectView.isBoardView()).collect(Collectors.toList());
    
    // remove cards and players listeners
    this.getViewList().forEach(view -> {
      view.removeListenerModel(view.getCardsView().getCards());
      game.getPlayers().forEach(view::removeListenerModel);
    });
    
    // removing playerDetails listeners
    tempPlayerViews.forEach(gameObjectView -> {
      if (gameObjectView.getCardsView().getCards().getPlayer().isPresent()) {
        gameObjectView.removeListenerModel(gameObjectView.getCardsView().getCards().getPlayer().get());
        tempPlayersDetails
              .stream()
              .filter(playerDetail -> gameObjectView
                                            .getCardsView()
                                            .getCards()
                                            .getPlayer()
                                            .get()
                                            .equals(playerDetail.getPlayer()))
              .findFirst()
              .ifPresent(gameObjectView::removeListenerModel);
      }
    });
    
    if (oldDealer.isNPC() && !newDealer.isNPC()) {
      getView(tempPlayerViews, GameObjectView.isUserView()).ifPresent(view -> {
        getPlayerDetail(tempPlayersDetails, newDealer).ifPresent(playerDetail -> {
          this.createDealerOrUserOnNewGame(view, playerDetail, game.getCards(), true);
        });
      });
    }
    
    if (!oldDealer.isNPC() && newDealer.isNPC()) {
      getView(tempPlayerViews, GameObjectView.isDealerView()).ifPresent(view -> {
        getPlayerDetail(tempPlayersDetails, oldDealer).ifPresent(playerDetail -> {
          this.createDealerOrUserOnNewGame(view, playerDetail, game.getCards(), false);
        });
      });
    }
  }
  
  private Optional<GameObjectView> getPlayerView(CurrentPlayer currentPlayer) {
    return getViewList()
                 .stream()
                 .filter(gameObjectView -> !gameObjectView.getCardsView().getCards().isCommunity())
                 .filter(gameObjectView -> gameObjectView.getCardsView().getCards().getPlayer().isPresent())
                 .filter(gameObjectView -> gameObjectView.getCardsView().getCards().getPlayer().get().equals(currentPlayer.getPlayer()))
                 .findFirst();
  }
  
  private PlayerView getUserPlayerView() {
    return this.getUserView().orElse(this.getUserDealerView().orElse(null));
  }
  
  private Optional<PlayerView> getUserView() {
    return getViewList()
                 .stream()
                 .filter(GameObjectView.isNotBoard())
                 .filter(GameObjectView.playerIsPresent())
                 .filter(GameObjectView.playerIsNotNPC())
                 .filter(GameObjectView.playerIsNotDealer())
                 .findFirst()
                 .map(gameObjectView -> (PlayerView) gameObjectView);
  }
  
  private Optional<PlayerView> getUserDealerView() {
    return getViewList()
                 .stream()
                 .filter(GameObjectView.isNotBoard())
                 .filter(GameObjectView.playerIsPresent())
                 .filter(GameObjectView.playerIsNotNPC())
                 .filter(GameObjectView.playerIsDealer())
                 .findFirst()
                 .map(gameObjectView -> (PlayerView) gameObjectView);
  }
  
  private List<NPCView> getNPCViews() {
    return getViewList()
                 .stream()
                 .filter(GameObjectView.isNotBoard())
                 .filter(GameObjectView.playerIsPresent())
                 .filter(GameObjectView.playerIsNPC())
                 .map(gameObjectView -> (NPCView) gameObjectView)
                 .collect(Collectors.toList());
  }
  
  private GameController getGameController() {
    return this.gameController;
  }
  
  @Override
  public void setGameController(GameController gameController) {
    this.gameController = gameController;
  }
  
  private LayoutBuilder getLayoutBuilder() {
    return this.layoutBuilder;
  }
  
  @Override
  public void setLayoutBuilder(LayoutBuilder layoutBuilder) {
    this.layoutBuilder = layoutBuilder;
  }
  
  @Override
  public void npcExchangeCards(Player currentPlayer) {
    getNPCViews()
          .stream()
          .filter(npcView -> npcView.getPlayer().equals(currentPlayer))
          .findFirst()
          .ifPresent(npcView -> {
            final Cards boardCards = getBoardView().getCardsView().getCards();
            final Cards npcCards = npcView.getCardsView().getCards();
            
            npcView.getPlayerController().exchangeCards(currentPlayer, boardCards, npcCards);
          });
  }
  
  private CardsViewFactory getCardsViewFactory() {
    return this.cardsViewFactory;
  }
  
  private GameObjectViewFactory getGameObjectViewFactory() {
    return this.gameObjectViewFactory;
  }
  
  private Optional<GameObjectView> getView(List<GameObjectView> playerViews, Predicate<? super GameObjectView> viewPredicate) {
    return playerViews.stream().filter(viewPredicate).findFirst();
  }
  
  private Optional<PlayerDetail> getPlayerDetail(List<PlayerDetail> playersDetails, Player dealer) {
    return playersDetails
                 .stream()
                 .filter(PlayerDetail::isStillAlive)
                 .filter(playerDetail -> playerDetail.getPlayer().getUsername().equals(dealer.getUsername()))
                 .findFirst();
  }
  
  private void createDealerOrUserOnNewGame(GameObjectView view, PlayerDetail playerDetail, List<Cards> cardsList, boolean isCreateDealer) {
    getViewList().remove(view);
    
    cardsList
          .stream()
          .filter(Cards::isPlayerCards)
          .filter(cards -> cards.getPlayer().isPresent())
          .filter(cards -> playerDetail.getPlayer().equals(cards.getPlayer().get()))
          .findFirst()
          .ifPresent(cards -> {
            Pair<GameObjectView, CardsView<Group>> pair;
            
            if (isCreateDealer) {
              pair = createDealer(playerDetail, cards);
            } else {
              pair = createUser(playerDetail, cards);
            }
            
            pair.getX().setCardsView(pair.getY());
            pair.getX().addListenerToModel(cards);
            pair.getX().addListenerToModel(playerDetail);
            pair.getX().addListenerToModel(playerDetail.getPlayer());
            
            getViewList().add(pair.getX());
          });
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CardsMediatorImpl)) return false;
    CardsMediatorImpl that = (CardsMediatorImpl) o;
    return npcIndex == that.npcIndex && cardsExchanged.equals(that.cardsExchanged) && getViewList().equals(that.getViewList()) && getGameObjectViewFactory().equals(that.getGameObjectViewFactory()) && getCardsViewFactory().equals(that.getCardsViewFactory()) && cardsList.equals(that.cardsList) && playersDetails.equals(that.playersDetails) && Objects.equals(getGameController(), that.getGameController()) && Objects.equals(getLayoutBuilder(), that.getLayoutBuilder());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(cardsExchanged, getViewList(), getGameObjectViewFactory(), getCardsViewFactory(), cardsList, playersDetails, getGameController(), getLayoutBuilder(), npcIndex);
  }
}
