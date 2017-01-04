#About
This project is a simulator for 2d twisty puzzles. Possible configurations include the Hex Shaper and Rashkey.

#How to Play
Clicking on the puzzle turns the circle with center closest to the click. Left-clicking makes a clockwise turn and right-clicking makes a counterclockwise turn.
The reset puzzle returns the puzzle to its original coloring.
The scramble button makes random turns equal to the number entered.
The sequence input takes a sequence of moves separated by spaces. Each turning circle has a number corresponding to its order in its setup file. Inputting the number turns the circle clockwise and inputting the number followed by a single quote turns the circle counterclockwise.

#Puzzle Creation
The first line of a puzzle file specifies what fraction of a rotation each circle turns by. All other lines create a turning circle in the puzzle from five entries separated by spaces. The first four entries are the x-coordinate and y-coordinate of the center, the radius, and the border width in pixels. The final entry is the color of the circle from the java default colors here: https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html. Pieces are colored corresponding to the first circle it is contained in.
