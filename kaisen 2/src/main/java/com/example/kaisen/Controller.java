package com.example.kaisen;

import com.example.kaisen.model.EnemyField;
import com.example.kaisen.model.PlayerField;
import com.example.kaisen.model.Result;
import com.example.kaisen.model.Resultinfo;
import com.example.kaisen.service.ShipService;
import com.example.kaisen.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Random;

@org.springframework.stereotype.Controller
public class Controller {
    private Random random=new Random();
    private PlayerField myField;
    private PlayerField enemyField;
    private Result result;
    @Autowired
    ShipService ShipService;
    @Autowired
    ResultService resultService;

    @GetMapping("HomePage")
    public String get(Model model){
        return "HomePage.html";
    }
    @PostMapping("HomePage")
    public String post(Model model,int width,int length){
        ShipService.settingGame(width,length);

        model.addAttribute("player", ShipService.getMyField());
        model.addAttribute("cpu", ShipService.getEnemyField());

        return "atack.html";
    }
    @PostMapping("atack")
    public String atack(Model model,int width,int length){

        String page= update(width,length);

        model.addAttribute("player", ShipService.getMyField());
        model.addAttribute("cpu", ShipService.getEnemyField());

        return page;
    }
    @PostMapping("Result")
    public String result(Model model){
        List<Resultinfo> previousResults=resultService.findAll();
        model.addAttribute("previousResults",previousResults);
        resultService.register(ShipService.getResult(),ShipService.getTurn());
        model.addAttribute("result",ShipService.getResult().toString()+":"+ShipService.getTurn()+"手目");

        return "result";
    }

    public String update(int x,int y){


        ShipService.turnCount();

        int enemyX=random.nextInt(5);
        int enemyY=random.nextInt(5);
        System.out.println("cpu/"+"y:"+enemyY+"x:"+enemyX);
        if(hitJudge(x,y,enemyField)&&hitJudge(enemyX,enemyY,myField)){
            myField.setInfo(enemyX,enemyY, EnemyField.breaked);
            enemyField.setInfo(x,y,EnemyField.breaked);
            result= Result.draw;
            return "draw.html";
        } else if(hitJudge(x,y,enemyField)&&!hitJudge(enemyX,enemyY,myField)){
            myField.setInfo(enemyX,enemyY,EnemyField.attacked);
            enemyField.setInfo(x,y,EnemyField.breaked);
            result= Result.win;
            return "win.html";
        } else if(!hitJudge(x,y,enemyField)&&hitJudge(enemyX,enemyY,myField)){
            myField.setInfo(enemyX,enemyY,EnemyField.breaked);
            enemyField.setInfo(x,y,EnemyField.attacked);
            result= Result.lose;
            return "lose.html";
        } else {
            myField.setInfo(enemyX,enemyY,EnemyField.attacked);
            enemyField.setInfo(x,y,EnemyField.attacked);
            return "game.html";
        }
    }
    boolean hitJudge(int x, int y, PlayerField field){
        return field.getInfo(x,y)==EnemyField.ship || field.getInfo(x,y)==EnemyField.enemyShip;
    }
}