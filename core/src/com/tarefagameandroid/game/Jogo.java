package com.tarefagameandroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jogo extends ApplicationAdapter {
	//Instancias das texturas//
	SpriteBatch batch;
	Texture fundo;
	Texture[] passaros;
	//Floats do Dispositivo//
	private float larguraDispositivo;
	private float alturaDispositivo;
	//Floats Movimentação//
	private int movimentaY = 0;
	private int movimentaX = 0;
	//Floats Gravidade//
	private float variacao= 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;

	@Override
	public void create () {
		//Sprites//
		batch = new SpriteBatch();
		//Sprites Passaros//
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//Fundo//
		fundo = new Texture("fundo.png");
		//Pegando a largura e altura do dispositivo//
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		//Setando posição inicial do passaro//
		posicaoInicialVerticalPassaro = alturaDispositivo /2;

	}

	@Override
	public void render () {
		batch.begin();
		//Movimentação do passaro//
		if(variacao >3 ){
			variacao = 0;
		}
		boolean toqueTela = Gdx.input.justTouched(); //Pegando toques na tela//
		if(Gdx.input.justTouched()){ //Se tocar na tela, seta gravidade para -25 para subir o passaro//
			gravidade = -25;
		}
		if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}
		//Desenhando os sprites//
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],30,posicaoInicialVerticalPassaro, 150, 150);
		//setando velocidade da batida da asa usando o DeltaTime * 10//
		variacao += Gdx.graphics.getDeltaTime() * 10;
		//Movimentação//
		gravidade++;
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
