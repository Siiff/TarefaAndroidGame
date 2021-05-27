package com.tarefagameandroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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
	Texture[] passaros;
	Texture fundo;
	Texture canoBaixo;
	Texture canoTopo;
	Texture textoGameOver;
	SpriteBatch batch;

	//Floats do Dispositivo//
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Floats Gravidade//
	private float variacao= 0;
	private int gravidade = 0;
	private float posicaoInicialVerticalPassaro= 0;
	private float posicaoInicialHorizontalPassaro = 0;

	//Canos//
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private Random random;

	//Passou pelo Cano//
	private boolean passouCano = false;

	//Pontuação//
	private int pontos = 0;
	private int pontuacaoMaxima = 0;

	//Imprimindo coisas na tela//
	ShapeRenderer shapeRenderer;

	//colisão do passaro//
	private Circle circuloPassaro;

	//Colisão do cano//
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	//Estado do jogo//
	private int estadoJogo = 0;

	//Textos//

	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	//Sons//
	Sound somAsas;
	Sound somBatida;
	Sound somPontos;

	//Preferencias//
	Preferences preferencias;


	@Override
	public void create () {
		inicializaTexturas();
		inicializaObjetos();
	}

	private void inicializaObjetos() {
		//Inicializando Random e Batch//
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
		textoPontuacao.getData().setScale(8);

		//Melhor Pontuação//
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.GREEN);
		textoMelhorPontuacao.getData().setScale(3);

		//Reiniciar//
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.RED);
		textoReiniciar.getData().setScale(3);

		//Colisores//
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoCima = new Rectangle();

		//Sons//
		somAsas = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somBatida = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontos = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

		//Preferencias//
		preferencias = Gdx.app.getPreferences("flappybird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);

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

		//GameOver//
		fundo = new Texture("fundo.png");
		textoGameOver = new Texture("game_over.png");

		//Canos//
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
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
				//Som pontos//
				somPontos.play();
				passouCano = true;
			}
		}
		//setando velocidade da batida da asa usando o DeltaTime * 10//
		variacao += Gdx.graphics.getDeltaTime() * 10;

		//Movimentação do passaro//
		if(variacao >3 ){
			variacao = 0;
		}
	}

	private void detectarColisao() {
		//Setando colisor do passaro//
		circuloPassaro.set(50 + passaros[0].getWidth() / 2 , posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth()/2);
		//Setando colisor dos canos//
		retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());
		retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoTopo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());
		//Booleans da colisão//
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(bateuCanoBaixo || bateuCanoCima){
			Gdx.app.log("log", "bateu");
			if(estadoJogo == 1){
				//Som batida//
				somBatida.play();
				estadoJogo = 2;
			}
		}
	}

	private void desenharTexturas() {
		batch.begin();
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],50+ posicaoInicialHorizontalPassaro,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical );
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos / 2 + posicaoCanoVertical );

		//Pontuacao//
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2 - 100, alturaDispositivo - 100 );
		if(estadoJogo == 2){
			//Gameover//
			batch.draw(textoGameOver, larguraDispositivo/2 - textoGameOver.getWidth()/2, alturaDispositivo/2 );
			//Melhor pontuacao e reiniciar//
			textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!",larguraDispositivo/2 - 350, alturaDispositivo/2 - textoGameOver.getHeight()/2 - 100);
			textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUACAO É: "+pontuacaoMaxima+" PONTOS!" ,larguraDispositivo/2 - 440, alturaDispositivo/2 + textoGameOver.getHeight() +150);
		}

		batch.end();
	}

	private void verificaEstadoJogo() {


		//Pegando toques na tela//
		boolean toqueTela = Gdx.input.justTouched();
		//Se tocar na tela, seta gravidade para -25 para subir o passaro//
		if(estadoJogo == 0){
			if(toqueTela){
				gravidade = -15;
				estadoJogo = 1;
				somAsas.play();
			}
		}

		else if(estadoJogo == 1){
			if(toqueTela) {
				gravidade = -15;
				somAsas.play();
			}
			//Movimentação do Cano//
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime()*200;
			if(posicaoCanoHorizontal <- canoBaixo.getHeight()){
				posicaoCanoHorizontal = larguraDispositivo;
				posicaoCanoVertical = random.nextInt(400) -200;
				passouCano = false;
			}
			//Gravidade//
			if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
			}
			gravidade++;
		}
		else if(estadoJogo == 2){
			//Resetar o jogo//
			if(pontos > pontuacaoMaxima) {
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}
			posicaoInicialHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;

			//Reiniciar//
			if(toqueTela){
				estadoJogo = 0;
				pontos= 0;
				gravidade = 0;
				posicaoInicialHorizontalPassaro = 0;
				posicaoInicialVerticalPassaro = alturaDispositivo/2;
				posicaoCanoHorizontal = larguraDispositivo;
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		fundo.dispose();
	}
}
