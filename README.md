# Othello Game

**Author:** _Dinh Thuy Nhat Vy_

**Created on:** _12/07/2023_

## **I. PROJECT OVERVIEW**

### **1. Introduction:**
- The Othello game project is a Java-based implementation of the classic Othello (Reversi) board game. Othello is a strategy game played on an 8x8 grid where two players, represented by different colored discs 
(typically black and white), compete to dominate the board by flipping the opponent's discs.
- You can consult the rules of the Othello game at: [Official rules for the game Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

### **2. Features:**
#### *2.1. Player options*
Players can choose to play against human opponents or AI players. The AI players implement different strategies, providing varying levels of difficulty for players.

a) **Naive Strategy:** experience a game against an AI player utilizing the Naive Strategy. The Naive AI makes moves based on simple rules, making it an ideal opponent for casual play and beginners.

b) **Smart Strategy:** for a more challenging experience, face off against the Smart AI. This intelligent opponent employs advanced algorithms to analyze the game state, anticipate moves, and make strategic decisions. 
The Smart AI provides a formidable adversary for players seeking a higher level of competition.
#### *2.2. Text-based user interface (TUI)*
The project provides a console-based TUI for users who prefer a text-based interface.

a) **Configuration options:** the TUI allows players to configure game settings seamlessly. From choosing opponents to setting up match parameters, users can customize their gaming experience without the need for a 
graphical interface.

b) **Continuity:** seamlessly transition between matches with the option to start a new game or exit, allowing for continuous and enjoyable gameplay sessions.
#### *2.3. Server-client architecture*
The project includes a server-client architecture that enables multiple players to connect and play games over a network.

a) **Communication protocol:** the server and clients communicate using a predefined protocol to ensure a standardized exchange of messages.

b) **Gameplay flow:**
- Connection establishment:
  - Clients initiate a connection to the server upon launching the game.
  - The server accepts incoming connections and establishes a communication channel with each client.
- Lobby interaction:
  - Players interact with the server to log in, view available opponents, and join game queues.
  - The server manages player states and facilitates interactions in the game lobby.
- Game initialization:
  - When players join a game queue or request to start a new game, the server initializes a new Othello game session.
  - Players are matched based on preferences, and the game board is set up.
- Gameplay:
  - The server orchestrates the gameplay, handling player moves and updating the game state.
  - Clients receive updates from the server and reflect the changes in the user interface.
- Game conclusion:
  - When a game concludes, the server announces the winner or a draw.
  - Players have the option to start a new game or exit.
#### *2.4. Graphical user interface (GUI) with sound effects*
The GUI enhances the Othello gaming experience by providing a visually appealing and interactive interface. The addition of sound effects further immerses players in the gameplay, creating a 
dynamic and engaging atmosphere.

### **3. Project structures:**
#### *3.1. "src"*
Contains the source code for the Othello game project. This directory is structured into packages that organize classes based on their functionalities:

a) **"images":** this package contains image resources used for the GUI.

b) **"networking":** this package contains classes for networking functionality.

c) **"othellogame":**
- "ai": this package encompasses classes associated with artificial intelligence strategies.
- "model": this package includes classes responsible for defining the game model, such as the game board, player marks, moves, and the main game logic.
- "ui": here, you'll find classes related to user interfaces.

d) **"sound":** this package includes audio files.
#### *3.2. "test"*
The directory containing test classes for both the game logic and networking.
#### *3.3. "javadoc"*
Contains the JavaDoc documentation for the project classes and methods.

## **II. PREREQUISITES**

### **1. Java Development Kit (JDK):**
- Version: Eclipse Temurin 19 (Version 19.0.2).
- You can download it from the official Adoptium website, which provides the AdoptOpenJDK distribution: [Eclipse Temurin 19](https://adoptium.net/temurin/releases/?version=19).

### **2. Integrated Development Environment (IDE):**
Any Java IDE of your choice (e.g., IntelliJ IDEA, Eclipse) that supports Java development.

### **3. Libraries:**
#### *3.1. JavaFX SDK*
- Version: 17.0.8
- Required for building the graphical user interface of the game.
- You can download it from the official Gluon website: [JavaFX](https://gluonhq.com/products/javafx/).
#### *3.2. JUnit Jupiter*
- Version: 5.8.2
- Used for writing and running tests for the Java code.
#### *3.3. Hamcrest*
- Version: 2.2
- Provides matchers for building test expressions. It's often used in conjunction with JUnit.

## **III. BUILDING AND RUNNING**

### **1. TUI:**
#### *1.1. Jar file installation (recommended)*
- **Step 1:** download the ZIP file and extract the contents of the ZIP file to a directory of your choice.
- **Step 2:** open File Explorer and navigate to the directory where the "TUI.jar" file is located. After that, click on the navigation bar, type "cmd", and press Enter. This will open a command prompt.
- **Step 3:** in the command prompt, start the TUI by running the command:
```bash
java -jar TUI.jar
```
### **1.2. Normal installation:**
- **Step 1:** clone or download the project repository to your local machine.
- **Step 2:** open your preferred Java development environment (e.g., IntelliJ IDEA, Eclipse). After that, navigate to the "Othello Game" project directory.
- **Step 3:** locate the "TUI.java" file in the "src/othellogame/ui" package. After that, open the "TUI.java" and run the "main" method to start the TUI.

### **2. Server-client architecture:**
#### *2.1. Jar file installation (recommended)*
- **Step 1:** download the ZIP file and extract the contents of the ZIP file to a directory of your choice.
- **Step 2:** open File Explorer and navigate to the directory where the "OthelloServer.jar" and "OthelloClient.jar" files are located. After that, click on the navigation bar, type "cmd", and press Enter. This will open 
a command prompt.
- **Step 3:** in the command prompt, start the Othello server by running the command:
```bash
java -jar OthelloServer.jar
```
and start the Othello client by running the command:
```bash
java -jar OthelloClient.jar
```
### **2.2. Normal installation:**
- **Step 1:** clone or download the project repository to your local machine.
- **Step 2:** open your preferred Java development environment (e.g., IntelliJ IDEA, Eclipse). After that, navigate to the "Othello Game" project directory.
- **Step 3:** locate the "OthelloServer.java" file in the "src/networking/server" package. After that, open the "OthelloServer.java" and run the "main" method to start the Othello server.
- **Step 4:** similarly, locate the "OthelloClient.java" file in the "src/networking/client" package. After that, open the "OthelloClient.java" and run the "main" method to start the Othello client.

### **3. GUI:**
- **Step 1:** set up JavaFX 17 library.
- **Step 2:** set VM options in Run > Edit Configurations:
  - Open the Run > Edit Configurations menu in your IDE.
  - In the VM options field, enter the following:
  ```
  --module-path [your JavaFX 17 lib directory] --add-modules javafx.controls,javafx.fxml,javafx.media
  ```
  **Note:** replace `[your JavaFX 17 lib directory]` with the path to your JavaFX 17 library.
- **Step 3:** execute the "GUI.java" file in the "src/othellogame/ui" package.

## **IV. CONTRIBUTING**
I welcome contributions to enhance and improve the Othello Game project. Before submitting a pull request, please follow these guidelines:
- **Open an issue:** before making major changes, please consider opening an issue to discuss the proposed modifications. This allows the community to provide input and reach a consensus on the changes.
- **Pull requests:** feel free to submit pull requests to address issues or introduce new features. Please ensure that your changes align with the project's goals and coding standards.
- **Testing:** please update or add tests relevant to the changes you've made. This helps maintain the integrity of the project and ensures that new features don't introduce regressions.
- **Documentation:** if your changes impact user-facing features or configurations, please update the documentation accordingly.
- **Coding standards:** please follow the project's coding standards and style guidelines. Consistent code formatting makes the codebase more maintainable.
By contributing to the Othello Game project, you help make it a better and more robust project for the entire community. Thank you so much for your valuable contributions!

## **V. ACKNOWLEDGEMENTS**
### **1. Game logic:**
[1]: [An Analysis of Heuristics in Othello](https://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf).

### **2. Images and sound effects:**
For the GUI design, I express my gratitude to the following resources for providing images that enhance the visual and auditory experience of the Othello game application:
#### *2.1. Images*
[1]: [Announcement Icon](https://www.freepik.com/icon/megaphone_2161397#fromView=search&term=purple+announcement&page=1&position=89).

[2]: [Back Button](https://www.vectorstock.com/royalty-free-vector/back-button-icon-vector-21881286).

[3]: [Bee Icon](https://www.freepik.com/premium-vector/bee-icon-isolated-white-background-honey-flying-bee-flat-style-vector-illustration_27111728.htm).

[4]: [Bird Frame](https://www.vectorstock.com/royalty-free-vector/label-frame-funny-birds-vector-621294).

[5]: [Calm Boy Icon](https://www.freepik.com/premium-vector/hand-drawn-business-people-man-woman-collection-character_24545348.htm#query=cute%20avatar&position=5&from_view=keyword&track=ais#position=5&query=cute%20avatar).

[6]: [Calm Girl Icon](https://www.freepik.com/premium-vector/hand-drawn-business-people-man-woman-collection-character_24545348.htm#query=cute%20avatar&position=5&from_view=keyword&track=ais#position=5&query=cute%20avatar).

[7]: [Classroom Background](https://www.vecteezy.com/vector-art/17733760-classroom-interior-empty-school-classroom-school-education-background).

[8]: [Close Icon](https://logowik.com/close-vector-icon-7970.html).

[9]: [Cool Boy Icon](https://www.freepik.com/premium-vector/hand-drawn-business-people-man-woman-collection-character_24545348.htm#query=cute%20avatar&position=5&from_view=keyword&track=ais#position=5&query=cute%20avatar).

[10]: [Cool Girl Icon](https://www.freepik.com/premium-vector/hand-drawn-business-people-man-woman-collection-character_24545348.htm#query=cute%20avatar&position=5&from_view=keyword&track=ais#position=5&query=cute%20avatar).

[11]: [Cute Animals Card](https://www.vecteezy.com/vector-art/19599868-cute-animal-greeting-card-doodle-banner-background-wallpaper-icon-cartoon-illustration).

[12]: [Flower Icon](https://www.flaticon.com/free-icon/flower_1669992).

[13]: [Flower Vine Icon](https://pngtree.com/freepng/flower-vine-branch-vine-plant-illustration_4741956.html).

[14]: [Home Icon](https://www.flaticon.com/free-icon/home_1010335).

[15]: [Left Arrow Icon](https://www.iconpacks.net/free-icon/arrow-left-3099.html).

[16]: [Log Out Icon](https://www.flaticon.com/free-icon/logout_7596566).

[17]: [Maroon Medal Icon](https://www.flaticon.com/free-icon/medal_1141817).

[18]: [Othello Board](https://bonaludo.com/2016/02/18/reversi-and-othello-two-different-games-do-you-know-their-different-rules/).

[19]: [Pink Gradient Button](https://www.vecteezy.com/png/10977621-gradient-button-button-for-website).

[20]: [Red Gradient Button](https://www.vecteezy.com/png/10977350-gradient-button-button-for-website).

[21]: [Replay Icon](https://www.flaticon.com/free-icon/turn-back_930893).

[22]: [Right Arrow Icon](https://www.iconpacks.net/free-icon/arrow-right-3098.html).

[23]: [Robot Icon](https://www.dreamstime.com/cartoon-robot-got-idea-image191439678).

[24]: [Shut Down Icon](https://www.iconsdb.com/white-icons/power-icon.html).

[25]: [Sky Background](https://www.freepik.com/free-vector/blue-sky-background_3708572.htm).

[26]: [Sound Off Icon](https://creazilla.com/nodes/3256257-sound-off-icon).

[27]: [Sound On Icon](https://www.clipartmax.com/middle/m2i8Z5G6A0m2i8N4_audio-icon-white-png/).

[28]: [Speech Bubble Icon](https://www.nicepng.com/ourpic/u2q8q8e6e6e6y3t4_speech-bubble-clipart-speech-bubble-for-powerpoint/).

[29]: [Tape Icon](https://cutewallpaper.org/24/masking-tape-png/masking-tape-png-tape-clipart-transparent-background-5790737--pinclipart.png).

[30]: [Teal Medal Icon](https://www.vectorstock.com/royalty-free-vector/medal-award-symbol-cute-kawaii-cartoon-vector-17512668).

[31]: [Warning Icon](https://www.pinterest.com/pin/410883166005688309/).

[32]: [Wooden Background](https://www.freepik.com/free-vector/wooden-texture-background-wood-material-pattern_21267281.htm).
#### *2.2. Sound effects*
[1]: [Menu Sound](https://www.zapsplat.com/music/cheeky-monkey-playful-cheeky-an-simple-music-loop-great-for-game-or-app-3/).

[2]: [Game Sound](https://www.youtube.com/watch?v=LtBpYXdr7fc&list=LL&index=408).

[3]: [Exit Sound](https://www.youtube.com/watch?v=ZaezHNLyogQ).

## **VI. LICENSE**
This project is licensed under the [MIT License](https://github.com/NhatZy/Othello-Game/blob/master/LICENSE.txt).
