# RankUpAndroid
RankUp is a poker card game played by 4 players. I wanted to write an Android application for this game. The ultimate goal (beyond the scope of Udacity nd940) is to train some AI for human players to compete against. For the sake of the capstone project, the application in this repo stands as a preliminary version, in which the UI and backends are handled but the actual game logic (i.e. how each player should play and how to determine the winner) is left out on purpose.

Here is the walkthrough of the application.

## The Welcome Screen
On the Welcome screen, a user sees two options
- `HOST A GAME`, the entry point for creating a game.
- `VIEW HISTORY`, where one can playback the games round by round, card by card. Initially it's empty.
We begin by clicking on `HOST A GAME`. **The app is restricted to landscape, because it's more convenient for the game UI elements to be arranged. See below for details.**

## Host A Game
On this screen, the four players of the game must be specified. The user is automatically registered under the player "Me" with an avatar in the team "My Team". The user chooses players either for their own team or the opponents' team by clicking corresponding FABs which show a plus. Clicking on a FAB takes the user to a list of "available players" they can select from. Once an "available player" is selected, the user is taken back to the previous screen, and the corresponding FAB will now show a cross, by clicking which the player is deselected. Once all four players are specified, the button at the bottom can start the game.
### Technicalities
- I created a custom view called `PlayerView` to handle displaying the name and avatar of a player, as well as controlling the look (i.e. plus or cross) and click listener of the FAB associated with that view.
- The list of "available players" is organized using RecyclerView.
- The list of "avalable players" is downloaded from online using Retrofit and the avatars are loaded using Glide. I used an online fake test data website and some free avatars to mimic the behavior of a remote server. As a result, there are altogether 10 fixed "available players".
- When a player is selected, they will disappear from the list so as to prevent double selection.
- The avater of "Me" is stored locally, and there is no deselection FAB for "Me", which makes sense as the user is the creator of the game.
- Once the four players are determined, they are stored in a shared viewmodel to be later retrieved in the Game Screen.

## The Game Screen
This is where the game and the underlying engine run. For this preliminary version, a game consists of two phases, the dealing and the playing. The dealing phase starts by shuffling (in the background) one set of poker cards, followed by each player taking turns to be dealt a card until they all have 3 cards in hand. 

Next the playing phase starts. In each round, the player "Me", i.e. the user, selects exactly one card to play, by clicking on the cards shown in the screen. Then the remaining players take turns to play their selected one card. It goes on until all cards have been played out, which marks the end of a game. 

During the game, the history of which player played which card at which round is recorded entirely in the background. When the game ends, this history is saved to a local database.
### Technicalities
- In the dealing phase, there is animation for dealing cards, implemented using MotionLayout. In order to achieve the effiect of dealing one by one, I made use of `TransitionListener` to implement a recursive transition scheme.
- The view of cards of "Me" is built using RecyclerView. Each card is standable, that is they will stand up once clicked. The card that stands is registered in the background so that when the user clickes the `PLAY` button, that card will show up in an area next to the player's avatar.
- To achieve the effect of players taking turns to play their card, I used coroutines to launch the main loop over the rounds of the game in the background. In particular, kotlin's `CompletableDeferred` is used to have the main loop listen on results from the `PLAY` button in the game UI. This can be in the future extended to listen on responses from other devices when the support for bluetooth is added.
- Because the main loop is launched in the background, the main UI thread is not blocked at all. That is, the user can clicking on the cards in hand while waiting for other players to play.
- I used `Room.Dao` to save the history into local database.

## View Histories
Once some game histories are saved, the user will be able to see them by clicking the `VIEW HISTORY` button on the Welcome screen. The histories are listed in the reverse chronological order. Each history consists of the time at which the game started, and cards that were played in each round.
### Technicalities
- The history list is implemented using RecyclerView, with support of headers added. Under the hood, serveral view types corresponding to sealed classes are used to dispatching the rendering of text views and image views.
