package Modelos;

import java.util.Arrays;
import java.util.Scanner;

public class Jogo {
    private static Scanner sc = new Scanner(System.in);
    private Tabuleiro tabuleiro;
    private Jogador[] jogadores;
    private int nJogadas;
    private int nPecasBrancas;
    private int nPecasPretas;
    private int lOrigem, cOrigem, lDestino, cDestino;
    private boolean deveCapturar = false;
    private char pecaBranca, damaBranca, pecaPreta, damaPreta;

    public Jogo() {
        this.jogadores = new Jogador[2];
        this.tabuleiro = new Tabuleiro(Peca.PecaBranca.peca, Peca.PecaPreta.peca);
        this.nJogadas = 0;
        this.nPecasBrancas = 12;
        this.nPecasPretas = 12;
        this.pecaBranca = this.jogadores[0].peca().peca;
        this.damaBranca = this.jogadores[0].dama().peca;
        this.pecaPreta = this.jogadores[1].peca().peca;
        this.damaPreta = this.jogadores[1].dama().peca;
    }

    public void jogar() {
        System.out.println("Bem vindo ao jogo de dama.");
        System.out.print("Caso queira iniciar, digite 1: ");
        String escolha = sc.nextLine();
        while (escolha.equals("1")) {
            // A variável nJogada já vale 0 (inicialmente) e cada mudança de jogador ela deve ser incrementada.
            setJogadores();
            while (nPecasBrancas != 0 && nPecasPretas != 0) {
                System.out.printf("Quem joga agora é %s, as peças podem ser: %s.%n", jogadores[nJogadas].getNome(), Arrays.toString(jogadores[nJogadas].getPecas()));
                defineOrigem(jogadores[nJogadas]);
                defineDestino(jogadores[nJogadas]);
                break;
            }
            System.out.print("Digite 1 para iniciar um novo jogo: ");
            escolha = sc.nextLine();
            if (!escolha.equals("1")) {
                System.out.println("Até a próxima.");
            }
        }
    }

    private void defineOrigem(Jogador j) {
        boolean equivalente;
        String pergunta;
        do {
            pergunta = String.format("%s, escolha uma linha de origem: ", j.getNome());
            this.lOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s, agora escolha uma coluna de destino: ", j.getNome());
            this.cOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro()[lOrigem].length);

            boolean pecaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[0].peca;
            boolean damaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[1].peca;
            equivalente = pecaOk || damaOk;
        } while (!equivalente);
        // Falta checar se a peça pode ser movida e se não puder, repetir a chamada desse método avisando o porquê.
//        while (pecaPresa()){
//            defineOrigem(j);
//        }
    }

    private void defineDestino(Jogador j) {
        String pergunta;
        boolean vago, movimentoOk;
        do {
            pergunta = String.format("%s escolha uma linha de destino: ", j.getNome());
            this.lDestino = validaPonto(pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s escolha uma coluna de destino: ", j.getNome());
            this.cDestino = validaPonto(pergunta, this.tabuleiro.getTabuleiro()[lDestino].length);
            vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
            movimentoOk = movimentoValido();
        } while (!(vago && movimentoOk));
    }

    private boolean movimentoValido() {
        if (andaUmaCasaDiagonal()) {
            return true; // Anda uma casa na diagonal
        } else {
            return possibleCapture();
        }
    }

    private boolean possibleCapture() {
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        boolean linhaSimples = lDestino == lOrigem + 2 || lDestino == lOrigem - 2;
        boolean colunaSimples = cDestino == cOrigem + 2 || cDestino == cOrigem - 2;
        if (linhaSimples && colunaSimples) { // Verificar captura simples
            // inicialmente vou assumir que a peça é branca...
            char oponentePeca = this.pecaPreta;
            char oponenteDama = this.damaPreta;
            if (pecaEscolhida == this.pecaPreta) {
                oponentePeca = this.pecaBranca;
                oponenteDama = this.damaBranca;
            }
            int vertical = lOrigem - 1, horizontal = cOrigem + 1; // Subindo para direita
            if (lDestino > lOrigem) {
                vertical = lDestino - 1; // Descendo
            }
            if (cDestino < cOrigem) {
                horizontal = cOrigem - 1; // Indo para esquerda
            }
            char oponente = tabuleiro.getTabuleiro()[vertical][horizontal];
            if (oponente == oponentePeca || oponente == oponenteDama) {
                deveCapturar = true;
                return true;
            } else {
                if (oponente != ' ') {
                    return false;
                }
                return (pecaEscolhida == this.damaBranca || pecaEscolhida == this.damaPreta);
            }
        } else {
            if (pecaEscolhida == this.damaBranca || pecaEscolhida == this.damaPreta) {// A peça de origem é uma dama
                return movimentoValidoDama(); // Aqui já sei que o movimento é longo e feito por uma dama.
            } else { // Aqui a peça não é dama e está querendo fazer um movimento de dama. Proibido.
                return false;
            }
        }
    }

    private boolean movimentoValidoDama() {
        boolean ret = false;

        return ret;
    }

    private boolean andaUmaCasaDiagonal() {
        var pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        int nextLine, passo = 1;
        if (pecaEscolhida == this.pecaBranca) {
            passo = -1;
        }
        nextLine = lOrigem + passo;

        return ((lDestino == nextLine) && (cDestino == cOrigem - passo || cDestino == cOrigem + passo));
    }

    private void setJogadores() {  // Pronto
        String name1, name2;
        System.out.print("Informe o nome do primeiro jogador: ");
        name1 = sc.nextLine();
        name1 = name1.substring(0, 1).toUpperCase() + name1.substring(1);
        System.out.print("Informe o nome do segundo jogador: ");
        name2 = sc.nextLine();
        name2 = name2.substring(0, 1).toUpperCase() + name2.substring(1);
        int sorteio = (int) Math.round(Math.random()) % 2;
        jogadores[sorteio] = new Jogador(name1, Peca.values()[sorteio], Peca.values()[sorteio + 2]);
        jogadores[(sorteio + 1) % 2] = new Jogador(name2, Peca.values()[(sorteio + 1) % 2], Peca.values()[(sorteio + 1) % 2 + 2]);
    }

    private int validaPonto(String mensagem, int limite) {
        int n = -1;
        if (limite >= 0) {
            while (n < 0 || n >= limite) {
                System.out.print(mensagem);
                n = sc.nextInt();
                if (n < 0 | n >= limite) {
                    System.out.println("Coordenada inválida, escolha de 0 até " + (limite - 1));
                }
            }
        }
        return n;
    }

    public String toString() {
        String result = "";
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[0].getNome(), jogadores[0].getPontos());
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[1].getNome(), jogadores[1].getPontos());
        return result;
    }
}