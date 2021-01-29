package com.example.laba3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laba3.Board.Board;
import com.example.laba3.Board.BoardSize;
import com.example.laba3.Board.BoardStatus;
import com.example.laba3.Model.Coordinate;
import com.example.laba3.Ship.Ship;
import com.example.laba3.Ship.ShipDirection;
import com.example.laba3.DrawableBoard.DrawableBoard;

import com.example.laba3.DrawableBoard.DrawableBoardPlacing;
import com.example.laba3.DrawableBoard.DrawableSquare;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShipRotateActivity extends AppCompatActivity {

    private DrawableBoardPlacing drawableBoardPlacing;
    private Board board;
    private DrawableBoard drawableBoard;
    private DrawableBoard targetDrawableBoard;
    private boolean gameInProgress = true;
    private boolean myTurn = false;
    private boolean canTarget = false;
    private int timeRemaining = -1;
    private int placeShipsTime = 20;
    private int shipsSunk = 0;
    private int currentScreen = -1;
    TextView tvPlayer1, tvPlayer2;

    private static final int[] SCREENS = {
            R.id.screen_place_ships,
            R.id.screen_target_board,
            R.id.screen_my_board };

    String playerSession = "";
    String userName = "";
    String otherPlayer = "";
    String loginUID = "";
    String requestType = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    DatabaseReference namesRef = ref.child("playing").child(playerSession).child("turn");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_rotate);

        userName = getIntent().getExtras().get("user_name").toString();
        loginUID = getIntent().getExtras().get("login_uid").toString();
        otherPlayer = getIntent().getExtras().get("other_player").toString();
        requestType = getIntent().getExtras().get("request_type").toString();
        playerSession = getIntent().getExtras().get("player_session").toString();

        List<String> message = new ArrayList<>();

        ref.child("playing").child(playerSession).child("turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                String mess = "";
                String value [] = new String[0];
                if(map != null){

                    value[0] = (String) map.get("id");
                    mess = value[0];
                }

                if (mess == "0")
                {
                    int x = (int) map.get("xid");
                    int y = (int) map.get("xid");

                    drawableBoard.colorCrosshair(x, y);

                    if (board.getStatus(x, y) == BoardStatus.HIDDEN_SHIP)
                    {
                        board.setStatus(x, y, BoardStatus.HIT);

                        if (board.shipToSink() != null)
                        {
                            Ship ship = board.shipToSink();
                            displaySunkShip(board, drawableBoard);
                            sendSinkShip(ship);

                            if (board.allShipsSunk())
                            {
                                gameInProgress = false;
                                //showEndGamePopup(false);
                            }
                        }
                        else
                        {
                            drawableBoard.squares[x][y].setImage(R.drawable.hit);
                            sendSquareStatus(x, y, 1);
                        }

                        myTurn = true;
                        canTarget = true;

                    }

                    else {

                        board.setStatus(x, y, BoardStatus.MISS);
                        drawableBoard.squares[x][y].setImage(R.drawable.miss);
                        sendSquareStatus(x, y, 0);
                        myTurn = true;
                        canTarget = true;
                    }
                }

                else if (mess == "1")
                {
                    int x = (int)map.get("idx");
                    int y = (int)map.get("idy");
                    int status = (int)map.get("idstatus");

                    if (status == 1)
                    {
                        targetDrawableBoard.squares[x][y].setImage(R.drawable.hit);
                    }

                    else if (status == 0)
                    {
                        targetDrawableBoard.squares[x][y].setImage(R.drawable.miss);
                        myTurn = false;
                    }
                }

                else if (mess == "2")
                {
                    int x = (int) map.get("xid");
                    int y = (int) map.get("yid");
                    int direction = (int) map.get("directionid");
                    int length = (int) map.get("lengthid");
                    int type = (int) map.get("typeid");

                    if (direction == 0)
                    {
                        for (int i = x; i < x + length; i++)
                        {
                            targetDrawableBoard.squares[i][y].setImage(R.drawable.sunk);
                        }
                    }
                    else if (direction == 1)
                    {
                        for (int i = y; i < y + length; i++)
                        {
                            targetDrawableBoard.squares[x][i].setImage(R.drawable.sunk);
                        }
                    }

                    switch(type)
                    {
                        case 1:
                            ((ImageView)findViewById(R.id.image_target_carrier)).setImageResource(R.drawable.carrier_sunk);
                            break;
                        case 2:
                            ((ImageView)findViewById(R.id.image_target_battleship)).setImageResource(R.drawable.battleship_sunk);
                            break;
                        case 3:
                            ((ImageView)findViewById(R.id.image_target_cruiser)).setImageResource(R.drawable.cruiser_sunk);
                            break;
                        case 4:
                            ((ImageView)findViewById(R.id.image_target_submarine)).setImageResource(R.drawable.submarine_sunk);
                            break;
                        case 5:
                            ((ImageView)findViewById(R.id.image_target_destroyer)).setImageResource(R.drawable.destroyer_sunk);
                            break;
                    }

                    shipsSunk += 1;

                    if (shipsSunk == 5)
                    {
                        gameInProgress = false;
                        //showEndGamePopup(true);
                    }
                    else
                    {
                        myTurn = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void buttonRotateShip(View view) {
        if (drawableBoardPlacing.getActiveShip() != null) {
            drawableBoardPlacing.getActiveShip().rotate();
            drawableBoardPlacing.colorShips();
        }
    }

    public void buttonPlaceShipsRandomly(View view) {
        board.placeShipsRandom();
        drawableBoardPlacing.setNoActiveShip();
        drawableBoardPlacing.colorShips();
    }

    public void buttonConfirmShips(View view) {

        if (board.isValidBoard()) {
            board.confirmShipLocations();
            switchToScreen(R.id.screen_target_board);
            displayDrawableBoards();
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayDrawableBoards() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int buttonSize = displayWidth / (BoardSize.COLUMNS + 1);

        targetDrawableBoard = new DrawableBoard(this, buttonSize);

        for (int i = 0; i < BoardSize.ROWS; i++) {
            for (int j = 0; j < BoardSize.COLUMNS; j++) {
                final DrawableSquare square = targetDrawableBoard.squares[i][j];

                square.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (gameInProgress && myTurn && canTarget) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                ClipData data = ClipData.newPlainText("", "");
                                View.DragShadowBuilder shadowBuilder = new DragBuilder();
                                view.startDrag(data, shadowBuilder, view, 0);

                                return true;
                            }
                        }

                        return false;
                    }
                });

                square.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
                        switch (dragEvent.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                DrawableSquare squareEnter = (DrawableSquare) view;
                                Coordinate squareEnterCoordinate = squareEnter.getCoordinate();
                                targetDrawableBoard.colorCrosshair(squareEnterCoordinate.getX(), squareEnterCoordinate.getY());
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                targetDrawableBoard.colorReset();
                                break;
                            case DragEvent.ACTION_DROP:
                                DrawableSquare square = (DrawableSquare) view;

                                if (!square.isClicked()) {
                                    targetCoordinate(square);
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
            }
        }
    }

    private void targetCoordinate(DrawableSquare square)
    {
        canTarget = false;
        Coordinate coordinate = square.getCoordinate();
        int x = coordinate.getX();
        int y = coordinate.getY();

        targetDrawableBoard.colorCrosshair(x, y);

        square.setClicked(true);
        sendTargetCoordinate(x, y);
    }

    private void sendTargetCoordinate(int x, int y)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("xid", x);
        map.put("yid", y);
        namesRef.updateChildren(map);

    }

    private void sendSquareStatus(int x, int y, int status)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("xid", x);
        map.put("yid", y);
        map.put("statusid", status);
        namesRef.updateChildren(map);
    }

    private void sendSinkShip(Ship ship)
    {
        byte[] message = new byte[6];
        message[0] = 2;
        message[1] = (byte) ship.getCoordinate().getX();
        message[2] = (byte) ship.getCoordinate().getY();

        int direction = -1;

        if (ship.getDirection() == ShipDirection.HORIZONTAL)
        {
            direction = 0;
        }
        else if (ship.getDirection() == ShipDirection.VERTICAL)
        {
            direction = 1;
        }

        message[3] = (byte) direction;
        message[4] = (byte) ship.getType().getLength();
        message[5] = (byte) ship.getType().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("xid", ship.getCoordinate().getX());
        map.put("yid", ship.getCoordinate().getY());
        map.put("directionid", direction);
        map.put("lengthid", ship.getType().getLength());
        map.put("typeid", ship.getType().getId());
        namesRef.updateChildren(map);
    }

    private void switchToScreen(int screen)
    {
        currentScreen = screen;


        for (int s : SCREENS)
        {
            if (s == screen)
            {
                findViewById(s).setVisibility(View.VISIBLE);
            }
            else
            {
                findViewById(s).setVisibility(View.GONE);
            }
        }

        boolean showInvitePopup = false;

        if (currentScreen == R.id.screen_place_ships || currentScreen == R.id.screen_target_board
                || currentScreen == R.id.screen_my_board)
        {
            showInvitePopup = true;
        }

    }


    public void buttonExitGame(View view)
    {

        if(Build.VERSION.SDK_INT >= 21)
        {
            finishAndRemoveTask();
        }
        else
        {
            finish();
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void displayDrawableBoardPlacing()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int buttonSize = displayWidth / (BoardSize.COLUMNS + 1);

        drawableBoardPlacing = new DrawableBoardPlacing(this, board, buttonSize);

        LinearLayout BattleshipGridPlacing = findViewById(R.id.battleship_grid_placing);
        BattleshipGridPlacing.removeAllViewsInLayout();
        BattleshipGridPlacing.addView(drawableBoardPlacing);
    }

    private void playersTurn()
    {
        if(requestType.equals("From")){
            tvPlayer1.setText("Your turn");
            tvPlayer2.setText("Your turn");
            ref.child("playing").child(playerSession).child("turn").setValue(userName);
        }
        else{
            tvPlayer1.setText(otherPlayer + "\'s turn");
            tvPlayer2.setText(otherPlayer + "\'s turn");
            ref.child("playing").child(playerSession).child("turn").setValue(otherPlayer);
        }
    }

    private void displaySunkShip(Board myboard, DrawableBoard dBoard)
    {
        if (board.shipToSink() != null)
        {
            Ship ship = board.shipToSink();

            for (Coordinate c : ship.getListCoordinates())
            {
                dBoard.squares[c.getX()][c.getY()].setImage(R.drawable.sunk);
            }

            if (myboard == board) {
                switch (ship.getType()) {
                    case CARRIER:
                        ((ImageView) findViewById(R.id.image_my_carrier)).setImageResource(R.drawable.carrier_sunk);
                        break;
                    case BATTLESHIP:
                        ((ImageView) findViewById(R.id.image_my_battleship)).setImageResource(R.drawable.battleship_sunk);
                        break;
                    case CRUISER:
                        ((ImageView) findViewById(R.id.image_my_cruiser)).setImageResource(R.drawable.cruiser_sunk);
                        break;
                    case SUBMARINE:
                        ((ImageView) findViewById(R.id.image_my_submarine)).setImageResource(R.drawable.submarine_sunk);
                        break;
                    case DESTROYER:
                        ((ImageView) findViewById(R.id.image_my_destroyer)).setImageResource(R.drawable.destroyer_sunk);
                        break;
                }
            }

            board.sinkShips();
        }
    }


}