package com.shivanshu.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gamestate = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	float gap = 500;
	float maxtubeoffset;
	Random randomGenerator;
	Texture gameover;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	float tubeVelocity = 4;
	BitmapFont font;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeoffset = new float[numberOfTubes];
	float distaneBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxtubeoffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distaneBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameover = new Texture("gameover.png");

		startGame();

	}
	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;


		for(int i = 0; i< numberOfTubes;i++){
			tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap - 200);
			tubeX[i] =Gdx.graphics.getWidth()/2-topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distaneBetweenTubes ;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate == 1) {

			if(tubeX[scoringTube] <Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score","Info");
				if(scoringTube < numberOfTubes -1 ){
					scoringTube++;
				}else{
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()){

				velocity = -30;

			}
			for(int i = 0 ; i <numberOfTubes ; i++){
				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] += numberOfTubes * distaneBetweenTubes;
					tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap - 200);
				}
				else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}
					batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 2);
					batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i] / 2);
                    topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 2,topTube.getWidth(),topTube.getHeight());
                    bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i] / 2,bottomTube.getWidth(),bottomTube.getHeight());

			}



			if(birdY > 0) {
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}else{

				gamestate = 2;

			}
		}
		else if (gamestate == 0) {

			if(Gdx.input.justTouched()){
				gamestate = 1;

			}
		}else  if(gamestate == 2){

			batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 -gameover.getHeight()/2);
			if(Gdx.input.justTouched()){
				gamestate = 1;
				score = 0;
				scoringTube= 0;
				velocity = 0;
				startGame();


			}

		}
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}


		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);

		font.draw(batch,"Score "+String.valueOf(score),100,200);
		batch.end();


		birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i =0;i< numberOfTubes;i++){
           // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] / 2,topTube.getWidth(),topTube.getHeight());
           // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i] / 2,bottomTube.getWidth(),bottomTube.getHeight());

            if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){

            	gamestate = 2 ;

			}
        }
		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();

	}
}
