# RankUpAndroid
RankUp is a poker card game played by 4 players. I wanted to write an Android application for this game. The ultimate goal (beyond the scope of Udacity nd940) is to train some AI for human players to compete against. For the sake of the capstone project, a mock version of the application will be implemented that 
- leaves out the details of the game engine
- leaves out the actual connectivity among devices of all the human players.

The following summarizes the milestones.

## Activities and Screens
### Main activity/screen
The app will have a main screen, with two buttons, one of which allows the user to host a new game and the other allows the user to join a game hosted by other people.

### "host a game" screen and "friends" screen
The user has the option to send invitation to friends when hosting a game. All friends will be listed on a separate screen. The user doesn't have to fill up the spots with friends only. If the user creates a game with less than 4 players in total, the vacant spots will be filled up by strangers. (In this mock app, strangers will be automatically filled to make the game start. Also, confirmation from friends will also be omitted for simplicity.)

### "join a game" screen
The user will see games that are hosted waiting for players to join. If the user is invited (via the method described above), the game will show up at the top.

### game activity/screen
When the user either starts a host game or joins an invited game, they will be taken to the game screen. The user will have the option to add strangers to their firend list by clicking on their icons.

## Data
### Friend
A friend object consists of a profile image and a username, besides other metadata. Friends will be saved to the local database and be displayed through RecyclerView.

## Notification
The user is allowed to put the app in the background during a game. In that case, a notification will be sent when other players have made a move. Clicking on the notification takes the user back to the game. 
