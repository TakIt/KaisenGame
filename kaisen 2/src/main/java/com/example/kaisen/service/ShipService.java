package com.example.kaisen.service;


import com.example.kaisen.model.EnemyField;
import com.example.kaisen.model.Enemyplay;
import com.example.kaisen.model.PlayerField;
import com.example.kaisen.model.Result;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShipService{

    private PlayerField myField;
    private PlayerField enemyField;

    private int turn;
    private Result result;

    private Random random=new Random();

    public void settingGame(int x,int y){
        this.turn=0;
        this.result= Result.none;

        myField=new PlayerField(Enemyplay.player);
        myField.setInfo(x,y, EnemyField.ship);

        enemyField= new PlayerField(Enemyplay.cpu);
        int enemyX=random.nextInt(5);
        int enemyY=random.nextInt(5);
        System.out.println(enemyX+","+enemyY);
        enemyField.setInfo(enemyX,enemyY,EnemyField.enemyShip);
    }



    public void turnCount(){
        turn++;
    }

    public EnemyField[][] getMyField() {
        return myField.getField();
    }

    public EnemyField[][] getEnemyField() {
        return enemyField.getField();
    }

    public Result getResult() {
        return result;
    }

    public int getTurn() {
        return turn;
    }
}