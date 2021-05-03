package com.tarefagameandroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture fundo;
	Texture passaro;

	private float larguraDispositivo;
	private float alturaDispositivo;

	private int movimentaY = 0;
	private int movimentaX = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//Pegando as texturas dos objetos//
		fundo = new Texture("fundo.png");
		passaro = new Texture("passaro1.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

	}

	@Override
	public void render () {
		batch.begin();
		//Instanciando eles no aplicativo//
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaro, movimentaX, movimentaX);

		//Mov provisório
		movimentaX++;
		movimentaY++;

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		fundo.dispose();
	}
}
