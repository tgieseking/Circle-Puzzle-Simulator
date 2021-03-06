package circle_simulator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.PixelWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import java.util.concurrent.ThreadLocalRandom;
import javafx.stage.FileChooser;
import java.lang.reflect.Field;
import java.io.*;
import javafx.scene.control.TextArea;


public class CircleSim extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Puzzle currentPuzzle;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        root.getChildren().add(canvas);
        TextField scramblerField = new TextField("1000");
        Button scramblerButton = new Button("Scramble");
        HBox scramblerBox = new HBox(scramblerField,scramblerButton);
        Button fileButton = new Button("Choose Puzzle File");
        Button resetButton = new Button("Reset Puzzle");
        VBox initBox = new VBox(fileButton,resetButton);
        TextArea sequenceText = new TextArea("1 2 1' 2'");
        sequenceText.setPrefHeight(100);
        sequenceText.setPrefWidth(300);
        sequenceText.setWrapText(true);
        Button sequenceButton = new Button("Execute");
        VBox sequenceBox = new VBox(sequenceText,sequenceButton);
        HBox bottomBox = new HBox(75.0,initBox,scramblerBox,sequenceBox);
        VBox bigBox = new VBox(root,bottomBox);
        primaryStage.setScene(new Scene(bigBox));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle File");

        currentPuzzle = createPuzzle(4);
        System.out.println(currentPuzzle.getCircles().size());
        System.out.println(currentPuzzle.getPieces().size());
        setPositionPixels(currentPuzzle, 800, 600);
        drawPuzzle(currentPuzzle, pw, canvas);


        primaryStage.show();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
        new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                double clickX = me.getX();
                double clickY = me.getY();
                double squareDistance;
                double minSquareDistance = 1000000000.0;
                CutCircle closestCircle = null;
                for(CutCircle circle : currentPuzzle.getTurningCircles()) {
                    squareDistance = PMath.squareDistance(clickX, clickY, circle.getCenterX(), circle.getCenterY());
                    if(squareDistance < minSquareDistance) {
                        minSquareDistance = squareDistance;
                        closestCircle = circle;
                    }
                }
                if(me.getButton() == MouseButton.PRIMARY) currentPuzzle.turnCW(closestCircle);
                else if(me.getButton() == MouseButton.SECONDARY) currentPuzzle.turnCCW(closestCircle);
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawPuzzle(currentPuzzle, pw, canvas);
            }
        });
        scramblerButton.setOnAction(event -> {
            try {
                int turns = Integer.parseInt(scramblerField.getText());
                ArrayList<CutCircle> turningCircles = currentPuzzle.getTurningCircles();
                for(int i=0; i<turns; i++) {
                    if(ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                        currentPuzzle.turnCW(turningCircles.get(ThreadLocalRandom.current().nextInt(0,turningCircles.size())));
                    }
                    else {
                        currentPuzzle.turnCCW(turningCircles.get(ThreadLocalRandom.current().nextInt(0,turningCircles.size())));
                    }
                }
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawPuzzle(currentPuzzle, pw, canvas);
            }
            catch(Exception e) {

            }
        });
        fileButton.setOnAction(event ->{
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            currentPuzzle = parseCircles(selectedFile);
            System.out.println(currentPuzzle.getCircles().size());
            System.out.println(currentPuzzle.getPieces().size());
            setPositionPixels(currentPuzzle, 800, 600);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawPuzzle(currentPuzzle, pw, canvas);
        });
        sequenceButton.setOnAction(even -> {
            String sequence = sequenceText.getText();
            if(sequence != null) {
                String[] words = sequence.split(" ");
                String currentMove;
                for(int i=0; i<words.length; i++) {
                    try {
                        currentMove = words[i];
                        if(currentMove.charAt(currentMove.length()-1) == '\'') currentPuzzle.turnCCW(Integer.parseInt(currentMove.substring(0,currentMove.length()-1))-1);
                        else currentPuzzle.turnCW(Integer.parseInt(currentMove)-1);
                    }
                    catch(Exception e) {}
                }
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawPuzzle(currentPuzzle, pw, canvas);
            }
        });
        resetButton.setOnAction(event -> {
            currentPuzzle.reset();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawPuzzle(currentPuzzle, pw, canvas);
        });
    }

    public Puzzle createPuzzle(int selector) {
        //Creates a puzzle from a choice of presets
        //Eventually will probably be loading a puzzle from a file

        // if(selector == 0) {
        //     CutCircle leftCircle = new CutCircle(400, 500, 200, 2, true);
        //     CutCircle rightCircle = new CutCircle(600, 500, 200, 2, false);
        //     HashSet<CutCircle> p1cs = new HashSet<CutCircle>();
        //     p1cs.add(leftCircle);
        //     HashSet<CutCircle> p2cs = new HashSet<CutCircle>();
        //     p2cs.add(leftCircle);
        //     p2cs.add(rightCircle);
        //     //HashSet<CutCircle> p3cs = new HashSet<CutCircle>();
        //     //p3cs.add(rightCircle);
        //     Piece p1 = new Piece(p1cs, Color.RED);
        //     Piece p2 = new Piece(p2cs, Color.YELLOW);
        //     //Piece p3 = new Piece(p3cs, Color.BLUE);
        //     HashSet<Piece> ps = new HashSet<Piece>();
        //     ps.add(p1);
        //     ps.add(p2);
        //     //ps.add(p3);
        //     Puzzle puzz = new Puzzle(p2cs, ps);
        //     return puzz;
        // }
        // if(selector == 1) {
        //     CutCircle TCL = new CutCircle(250, 400, 200, 2, true);
        //     CutCircle TCR = new CutCircle(550, 400, 200, 2, true);
        //     CutCircle CCL = new CutCircle(-50, 400, 200, 2, false);
        //     CutCircle CCR = new CutCircle(850, 400, 200, 2, false);
        //     ArrayList<CutCircle> circles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCL, TCR, CCL, CCR}));
        //     TCL.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCR, CCL})));
        //     TCR.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{CCR, TCL})));
        //     Piece p1 = new Piece(new HashSet<CutCircle>(Arrays.asList(new CutCircle[]{CCL, TCL})), Color.RED);
        //     Piece p2 = new Piece(new HashSet<CutCircle>(Arrays.asList(new CutCircle[]{TCL})), Color.ORANGE);
        //     Piece p3 = new Piece(new HashSet<CutCircle>(Arrays.asList(new CutCircle[]{TCL, TCR})), Color.YELLOW);
        //     Piece p4 = new Piece(new HashSet<CutCircle>(Arrays.asList(new CutCircle[]{TCR})), Color.GREEN);
        //     Piece p5 = new Piece(new HashSet<CutCircle>(Arrays.asList(new CutCircle[]{TCR, CCR})), Color.BLUE);
        //     HashSet<Piece> pieces = new HashSet<Piece>(Arrays.asList(new Piece[]{p1, p2, p3, p4, p5}));
        //     Puzzle puzz = new Puzzle(circles, pieces);
        //     return puzz;
        // }
        // if(selector == 2) {
        //     //CutCircle[] bonusCircles = new CutCircle[10];
        //     //for(int i=0; i<10; i++) bonusCircles[i] = new CutCircle(100*i, 0, 10, 2, false);
        //     CutCircle TCL = new CutCircle(250, 400, 250, 2, true);
        //     CutCircle TCR = new CutCircle(550, 400, 250, 2, true);
        //     CutCircle C0  = new CutCircle(-50, 100, 250, 2, false);
        //     CutCircle C1  = new CutCircle(250, 100, 250, 2, false);
        //     CutCircle C2  = new CutCircle(550, 100, 250, 2, false);
        //     CutCircle C3  = new CutCircle(850, 100, 250, 2, false);
        //     CutCircle C4  = new CutCircle(850, 400, 250, 2, false);
        //     CutCircle C5  = new CutCircle(850, 700, 250, 2, false);
        //     CutCircle C6  = new CutCircle(550, 700, 250, 2, false);
        //     CutCircle C7  = new CutCircle(250, 700, 250, 2, false);
        //     CutCircle C8  = new CutCircle(-50, 700, 250, 2, false);
        //     CutCircle C9  = new CutCircle(-50, 400, 250, 2, false);
        //     TCL.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCR, C1, C9, C7})));
        //     TCL.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{C2, C0, C8, C6})));
        //     TCR.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{C4, C2, TCL, C6})));
        //     TCR.addCycle(new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{C3, C1, C7, C5})));
        //     ArrayList<CutCircle> circles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCL, TCR, C0, C1, C2, C3, C4, C5, C6, C7, C8, C9}));
        //     //circles.addAll(new ArrayList<CutCircle>(Arrays.asList(bonusCircles)));
        //     Puzzle puzz = puzzleFromCircles(circles, 800, 600, TCL);
        //     return puzz;
        // }
        if(selector == 3) {
            CutCircle TCL = new CutCircle(302.5, 300.0, 250.0, 2.0, true);
            CutCircle TCR = new CutCircle(497.5, 300.0, 250.0, 2.0, true);
            ArrayList<CutCircle> turningCircles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCL,TCR}));
            ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(new Color[]{Color.YELLOW,Color.BLUE}));
            Puzzle puzz = puzzleFromTurningCircles(turningCircles, 4, 800, 600, colors);
            return puzz;
        }
        if(selector == 4) {
            CutCircle TCL = new CutCircle(275.0, 400.0, 170.0, 2.0, true);
            CutCircle TCR = new CutCircle(525.0, 400.0, 170.0, 2.0, true);
            CutCircle TCT = new CutCircle(400.0, 400.0-125.0*Math.sqrt(3.0), 170.0, 2.0, true);
            System.out.println( 400.0-125.0*Math.sqrt(3.0));
            ArrayList<CutCircle> turningCircles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCT,TCL,TCR}));
            ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(new Color[]{Color.YELLOW,Color.BLUE,Color.RED}));
            Puzzle puzz = puzzleFromTurningCircles(turningCircles, 6, 800, 600, colors);
            return puzz;
        }
        if(selector == 5) {
            CutCircle TCL = new CutCircle(275.0, 300.0, 250.0, 2.0, true);
            CutCircle TCR = new CutCircle(525.0, 300.0, 250.0, 2.0, true);
            ArrayList<CutCircle> turningCircles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCL,TCR}));
            ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(new Color[]{Color.YELLOW,Color.BLUE}));
            Puzzle puzz = puzzleFromTurningCircles(turningCircles, 4, 800, 600, colors);
            return puzz;
        }
        if(selector == 6) {
            CutCircle TCL = new CutCircle(570.0, 300.0, 200.0, 2.0, true);
            CutCircle TCR = new CutCircle(230.0, 300.0, 200.0, 2.0, true);
            ArrayList<CutCircle> turningCircles = new ArrayList<CutCircle>(Arrays.asList(new CutCircle[]{TCL,TCR}));
            ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(new Color[]{Color.YELLOW,Color.BLUE}));
            Puzzle puzz = puzzleFromTurningCircles(turningCircles, 8, 800, 600, colors);
            return puzz;
        }

        return null;
    }

    private Puzzle puzzleFromCircles(ArrayList<CutCircle> circles, ArrayList<CutCircle> turningCircles, int width, int length, ArrayList<CutCircle> coloringCircles, ArrayList<Color> colors) {
        //Given some circles with turning behavior already set, generates a piece for each set of circles with nonempty intersection.

        HashSet<Position> positions = new HashSet<Position>();
        HashSet<CutCircle> pixelBoundingCircles;
        boolean validPosition, inPuzzle;
        Position newPosition;
        HashMap<CutCircle,Position> nextClockwise;
        for(int x=0; x<width; x++) { //This loop just creates a bunch of positions
            for(int y=0; y<length; y++) {
                pixelBoundingCircles = new HashSet<CutCircle>();
                validPosition = false;
                for(CutCircle circle : circles) { //Get the circles this pixel is in
                    if(circle.circlePosition(x,y) == 0) {
                        pixelBoundingCircles.add(circle);
                        if(circle.isTurningCircle()) validPosition = true;
                    }
                }
                if(validPosition) { //Inside at least one turning circle
                    inPuzzle = false;
                    for(Position position : positions) {
                        if(pixelBoundingCircles.equals(position.getBoundingCircles())) inPuzzle = true;
                    }
                    if(!inPuzzle) {
                        newPosition = new Position();
                        nextClockwise = newPosition.getNextClockwise();
                        for(CutCircle circle : pixelBoundingCircles) {
                            nextClockwise.put(circle,null);
                        }
                        positions.add(newPosition);
                    }
                }
            }
        }
        Set<CutCircle> positionBoudingCircles, clockwiseBoundingCircles, counterClockwiseBoudingCircles;
        HashMap<CutCircle,CutCircle> circleNextClockwise, circleNextCounterClockwise;
        HashMap<CutCircle,Position> positionNextClockwise, positionNextCounterClockwise;
        for(Position position : positions) { //This loop sets the nextClockwise and nextClockwise for all the positions
            positionBoudingCircles = position.getBoundingCircles();
            for(CutCircle turningCircle : positionBoudingCircles) {
                if(turningCircle.isTurningCircle()) {
                    clockwiseBoundingCircles = new HashSet<CutCircle>();
                    counterClockwiseBoudingCircles = new HashSet<CutCircle>();
                    circleNextClockwise = turningCircle.getNextClockwise();
                    circleNextCounterClockwise = turningCircle.getNextCounterClockwise();
                    for(CutCircle boundingCircle : positionBoudingCircles) {
                        if(circleNextClockwise.containsKey(boundingCircle)) {
                            clockwiseBoundingCircles.add(circleNextClockwise.get(boundingCircle));
                            counterClockwiseBoudingCircles.add(circleNextCounterClockwise.get(boundingCircle));
                        }
                        else {
                            clockwiseBoundingCircles.add(boundingCircle);
                            counterClockwiseBoudingCircles.add(boundingCircle);
                        }
                    }
                    positionNextClockwise = position.getNextClockwise();
                    positionNextCounterClockwise = position.getNextCounterClockwise();
                    for(Position nextPosition : positions) {
                        if(nextPosition.getBoundingCircles().equals(clockwiseBoundingCircles)) positionNextClockwise.put(turningCircle,nextPosition);
                        if(nextPosition.getBoundingCircles().equals(counterClockwiseBoudingCircles)) positionNextCounterClockwise.put(turningCircle,nextPosition);
                    }
                }
            }
        }
        HashSet<Piece> pieces = new HashSet<Piece>();
        Boolean colored;
        for(Position position : positions) {
            colored = false;
            for(int i=0; i<coloringCircles.size(); i++) {
                if(!colored && position.getBoundingCircles().contains(coloringCircles.get(i))) {
                    colored = true;
                    if(colors.size() > i) pieces.add(new Piece(position, colors.get(i)));
                    else pieces.add(new Piece(position, Color.GRAY));
                }
            }
        }
        Puzzle puzz = new Puzzle(circles, turningCircles, positions, pieces, colors);
        puzz.removeIncompleteCycles();
        return puzz;
    }

    private PixelStorer getPixelPosition(Puzzle puzzle, int x, int y) {
        //Given a pixel, this function determines which piece it is in
        HashSet<CutCircle> pixelCircles = new HashSet<CutCircle>();
        ArrayList<CutCircle> puzzleCircles = puzzle.getCircles();
        HashSet<Position> positions = puzzle.getPositions();
        Border border = puzzle.getBorder();
        boolean validBorder;
        for(CutCircle circle : puzzleCircles) {
            if(circle.circlePosition(x,y) == 1){
                validBorder = false;
                for(CutCircle turningCircle : puzzle.getTurningCircles()) {
                    if(turningCircle.circlePosition(x,y) < 2) validBorder = true;
                }
                if(validBorder) return border;
                else return null;
            }
            if(circle.circlePosition(x,y) == 0) pixelCircles.add(circle);
        }
        Position pixelPosition = null;
        for(Position position : positions) {
            if(pixelCircles.equals(position.getBoundingCircles())) pixelPosition = position;
        }
        return pixelPosition;
    }

    private void setPositionPixels(Puzzle puzzle, int width, int length) {
        //Sets the pixel array for each piece in a puzzle
        PixelStorer previousPosition, currentPosition;
        int yStart;
        for(int x=0; x<width; x++) {
            previousPosition = null;
            currentPosition = null;
            yStart = 0;
            for(int y=0; y<length; y++) {
                previousPosition = currentPosition;
                currentPosition = getPixelPosition(puzzle, x, y);
                if(previousPosition != null && !previousPosition.equals(currentPosition)) {
                    previousPosition.getPixels().add(new int[]{x,yStart,y});
                    yStart = y;
                }
                else if(currentPosition != null && previousPosition == null)
                {
                    yStart = y;
                }
            }
            if(previousPosition != null) {
                previousPosition.getPixels().add(new int[]{x,yStart,length});
            }
        }
    }

    private void drawPuzzle(Puzzle puzzle, PixelWriter writer, Canvas canvas) {
        //Draws puzzle pieces
        //Borders not yet implemented
        for(Piece p : puzzle.getPieces()){
            p.drawPiece(writer);
        }
        puzzle.getBorder().drawBorder(writer);
    }

    private Puzzle puzzleFromTurningCircles(ArrayList<CutCircle> turningCircles, int turnFrac, int width, int length, ArrayList<Color> colors) {
        HashSet<CutCircle> newCircles = new HashSet<CutCircle>();
        for(CutCircle circle : turningCircles) newCircles.add(circle);
        HashSet<CutCircle> previousCircles;
        ArrayList<CutCircle> newCycle;
        ArrayList<CutCircle> puzzleCircles = new ArrayList<CutCircle>();
        double newCenterX,newCenterY;
        boolean alreadyThere;
        CutCircle nearCircle = null;
        while(newCircles.size() > 0) {
        //for(int n=0; n<2; n++) {
            System.out.println(puzzleCircles.size());
            puzzleCircles.addAll(newCircles);
            previousCircles = newCircles;
            newCircles = new HashSet<CutCircle>();
            for(CutCircle turningCircle : turningCircles) {
                for(CutCircle rotatingCircle : previousCircles) {
                    if(PMath.distance(turningCircle.getCenterX(),turningCircle.getCenterY(),rotatingCircle.getCenterX(),rotatingCircle.getCenterY())
                    < turningCircle.getRadius() + rotatingCircle.getRadius()) {
                        newCycle = new ArrayList<CutCircle>();
                        for(int i=0; i<turnFrac; i++) {
                            newCenterX =  PMath.fracRotatedX(rotatingCircle.getCenterX(),rotatingCircle.getCenterY(),turningCircle.getCenterX(),turningCircle.getCenterY(),i,turnFrac);
                            newCenterY = PMath.fracRotatedY(rotatingCircle.getCenterX(),rotatingCircle.getCenterY(),turningCircle.getCenterX(),turningCircle.getCenterY(),i,turnFrac);
                            //System.out.println("(" + newCenterX + ", " + newCenterY + ")");
                            alreadyThere = false;
                            for(CutCircle testNearCircle : puzzleCircles) {
                                if(PMath.squareDistance(testNearCircle.getCenterX(),testNearCircle.getCenterY(),newCenterX,newCenterY)<PMath.EPSILON) {
                                    alreadyThere = true;
                                    nearCircle = testNearCircle;
                                }
                            }
                            for(CutCircle testNearCircle : newCircles) {
                                if(PMath.squareDistance(testNearCircle.getCenterX(),testNearCircle.getCenterY(),newCenterX,newCenterY)<PMath.EPSILON) {
                                    alreadyThere = true;
                                    nearCircle = testNearCircle;
                                }
                            }
                            if(alreadyThere) {
                                newCycle.add(nearCircle);
                                //System.out.println("already there");
                            }
                            else {
                                //System.out.println("new circle");
                                newCycle.add(new CutCircle(newCenterX,newCenterY,rotatingCircle.getRadius(),rotatingCircle.getBorderWidth(),false));
                            }
                        }
                        turningCircle.addCycle(newCycle);
                        for(CutCircle newCircle : newCycle) {
                            if(!puzzleCircles.contains(newCircle)) {
                                //System.out.println("added circle");
                                newCircles.add(newCircle);
                            }
                        }
                    }
                }
            }
        }
        return puzzleFromCircles(puzzleCircles, turningCircles, width, length, turningCircles, colors);
    }

    public void addCircles(String inputString, ArrayList<CutCircle> circles, ArrayList<Color> colors) {
        String[] words = inputString.split(" ");
        if(words.length >= 5) {
            try{
                double centerX = Double.parseDouble(words[0]);
                double centerY = Double.parseDouble(words[1]);
                double radius = Double.parseDouble(words[2]);
                double borderWidth = Double.parseDouble(words[3]);
                Color color;
                Field field = Class.forName("javafx.scene.paint.Color").getField(words[4].toUpperCase());
                color = (Color)field.get(null);
                circles.add(new CutCircle(centerX, centerY, radius, borderWidth, true));
                colors.add(color);
            }
            catch (Exception e) {}
        }
    }

    public Puzzle parseCircles(File inputFile) {
        try{
            Scanner input = new Scanner(inputFile);
            ArrayList<CutCircle> circles = new ArrayList<CutCircle>();
            ArrayList<Color> colors = new ArrayList<Color>();
            int turnFrac = 1;
            turnFrac = Integer.parseInt(input.nextLine());
            while(input.hasNextLine()){
                addCircles(input.nextLine(),circles,colors);
            }
            return puzzleFromTurningCircles(circles,turnFrac,800,600,colors);
        }
        catch(Exception e){
            return null;
        }
    }
}
