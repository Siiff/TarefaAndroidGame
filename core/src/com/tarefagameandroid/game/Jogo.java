package com.tarefagameandroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Jogo extends ApplicationAdapter {
	//Instancias das texturas//
	SpriteBatch batch;
	Texture fundo;
	Texture[] passaros;
	Texture canoBaixo;
	Texture canoAlto;
	//Floats do Dispositivo//
	private float larguraDispositivo;
	private float alturaDispositivo;
	//Floats Gravidade//
	private float variacao= 0;
	private int gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;
	//Canos//
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private Random random;
	//Passou pelo Cano//
	private boolean passouCano = false;
	//Pontuação//
	private int pontos = 0;
	//Imprimindo coisas na tela//
	BitmapFont textoPontuacao;
	private ShapeRenderer shapeRenderer;
	//colisão do passaro//
	private Circle circuloPassaro;
	//Colisão do cano//
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	@Override
	public void create () {
		inicializaTexturas();
		inicializaObjetos();

	}

	private void inicializaObjetos() {
		random = new Random();
		batch = new SpriteBatch();

		//Pegando a largura e altura do dispositivo//
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		//Setando posição inicial do passaro//
		posicaoInicialVerticalPassaro = alturaDispositivo /2;
		//Canos//
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 350;
		//Pontuação//
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);
		//Colisores//
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoCima = new Rectangle();
	}

	private void inicializaTexturas() {
		//Sprites//
		batch = new SpriteBatch();
		//Sprites Passaros//
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//Fundo//
		fundo = new Texture("fundo.png");
		//Canos//
		canoBaixo = new Texture("cano_baixo_maior");
		canoBaixo = new Texture("cano_topo_maior");
	}


	@Override
	public void render () {

		verificaEstadoJogo();
		desenharTexturas();
		detectarColisao();
		validaPontos();

	}

	private void validaPontos() {
		if(posicaoCanoHorizontal < 50 - passaros[0].getWidth())
		{//Passou cano, pontua//
			if(!passouCano){
				pontos++;
				passouCano = true;
			}
		}
	}

	private void detectarColisao() {
		//Setando colisor do passaro//
		circuloPassaro.set(50 + passaros[0].getWidth() / 2 , posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth()/2);
		//Setando colisor dos canos//
		retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());
		retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoAlto.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoAlto.getWidth(), canoAlto.getHeight());
		//Booleans da colisão//
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(bateuCanoBaixo || bateuCanoCima){
			Gdx.app.log("log", "bateu");
		}
	}

	private void desenharTexturas() {
		batch.begin();
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],50,posicaoInicialVerticalPassaro, 150, 150);
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical );
		batch.draw(canoAlto, posicaoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos /2 +posicaoCanoVertical);
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2, alturaDispositivo - 100 );
		batch.end();
	}

	private void verificaEstadoJogo() {
		boolean toqueTela = Gdx.input.justTouched(); //Pegando toques na tela//
		if(Gdx.input.justTouched()){ //Se tocar na tela, seta gravidade para -25 para subir o passaro//
			gravidade = -25;
		}
		if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}
		//Movimentação do Cano//
		posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime()*200;
		if(posicaoCanoHorizontal <- canoBaixo.getHeight()){
			posicaoCanoHorizontal = larguraDispositivo;
			posicaoCanoVertical = random.nextInt(400) -200;
			passouCano = false;
		}
		//Movimentação do passaro//
		if(variacao >3 ){
			variacao = 0;
		}
		//setando velocidade da batida da asa usando o DeltaTime * 10//
		variacao += Gdx.graphics.getDeltaTime() * 10;
		//Movimentação//
		gravidade++;
	}

	@Override
	public void dispose () {
		batch.dispose();
		fundo.dispose();
	}
}
