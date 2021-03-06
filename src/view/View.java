package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.Board;
import model.Player;
import model.Square;
import model.cards.Cards;
import model.cards.Unit;

public class View {

	private final Scanner sc = new Scanner(System.in);

	public void window(Board b) {
		/* TODO : Effacer la console */
		System.out.println(b.getP2().toString());
		System.out.print("  ");
		for (int showNumberCol = 0; showNumberCol < 4; showNumberCol++) {
			System.out.print("    " + showNumberCol + "   ");
		}
		System.out.print("\n" + "  ");
		for (int underscore = 0; underscore < 4; underscore++) {
			System.out.print(" -------");
		}
		System.out.println();

		board(b);

		System.out.println(b.getP1().toString());
	}

	private void board(Board b) {
		for (int line = 0; line < 5; line++) {
			System.out.print(line + " |");
			for (int col = 0; col < 4; col++) {
				Square square = b.getSquare(line, col);
				Player p = square.getPlayer();
				/* TODO : A changer */
				if (square.getCard() != null) {
					if (p.equals(Player.PLAYER1)) {
						couleur(" " + square.getCard().getName().substring(0, 1).toUpperCase() + " : "
								+ ((Unit) square.getCard()).getStrength(), p);
					} else {
						couleur(" " + square.getCard().getName().substring(0, 1).toLowerCase() + " : "
								+ ((Unit) square.getCard()).getStrength(), p);
					}
				} else {
					System.out.print("       ");
				}
				System.out.print("|");
			}
			// System.out.print("\t");
			// if (line == b.getFrontLineP2())
			// couleur("<=", Player.PLAYER2);
			// if (line == b.getFrontLineP1())
			// couleur("<=", Player.PLAYER1);
			System.out.println("");
		}
		System.out.println();
	}

	/**
	 * Donne une couleur à notre affichage.
	 * 
	 * @param s
	 *            L'affichage que l'on souhaite.
	 * @param p
	 *            Le joueur que l'on affichera.
	 */
	public void couleur(String s, Player p) {
		if (p == Player.PLAYER1)
			System.out.print((char) 27 + "[31m" + s + " " + (char) 27 + "[0m");
		if (p == Player.PLAYER2)
			System.out.print((char) 27 + "[34m" + s + " " + (char) 27 + "[0m");
	}

	public int choix(Player player) {
		System.out.println("\nPlayer :" + player.getName());
		StringBuilder handSB = new StringBuilder();
		handSB.append("\nHAND :\n");
		for (Cards card : player.getHand())
			handSB.append(card.toString()).append('\n');
		System.out.println(handSB.toString());
		System.out.println("Que souhaitez-vous faire ?");
		System.out.println("1 - Placer une carte.");
		System.out.println("2 - Echanger une carte.");
		System.out.println("3 - Passer mon tour.");
		int choice = sc.nextInt();
		while (choice < 1 || choice > 3) {
			System.out.println("Erreur. Invalide parametre. Donnez un choix valide.");
			choice = new Scanner(System.in).nextInt();
		}
		return choice;

	}

	public Cards selectCardOnHand(Player player) throws IllegalStateException {
		if (player.getHand().isEmpty()) {
			throw new IllegalStateException();
		}
		StringBuilder selectCardOnHandSB = new StringBuilder();
		selectCardOnHandSB.append("Les cartes suivante sont disponibles :\n");
		ArrayList<Cards> handTmp = player.getHand();
		int index = 0;
		for (Cards card : handTmp) {
			if (card.getMana() <= player.getMana())
				selectCardOnHandSB.append(index).append('\t').append(card.toString()).append('\n');
			index++;
		}
		selectCardOnHandSB.append('\n');
		/*
		 * TODO : Test si aucune main mais presque impossible (seulement si on arrive à
		 * cours de carte dans notre deck)
		 */
		System.out.println(selectCardOnHandSB.toString());
		int selected = -1;
		do {
			selected = sc.nextInt();
		} while (selected < 0 || selected > index);
		return handTmp.get(selected);

	}

	public Square selectSquareOnBoard(Player player, Board board) throws IllegalStateException {
		/* Prendre dans le deck */
		StringBuilder placeCardSB = new StringBuilder();
		placeCardSB.append("Où souhaitez-vous placer votre carte ?\nCes colonnes sont libres ([x,y]):\n");
		/* Afficher les col libre sur la premiere ligne */
		int frontLinePlayer = board.getFrontLineP2();
		ArrayList<Square> freeSquares = new ArrayList<Square>();
		int numSquarefreeSquares = 0;
		for (int line = 0; line < 5; line++) {
			for (int col = 0; col < 4; col++) {
				if (player == Player.PLAYER1) {
					frontLinePlayer = board.getFrontLineP1();
					if (board.getSquare(line, col).getCard() == null && line >= frontLinePlayer) {
						freeSquares.add(new Square(line, col));
						placeCardSB.append(numSquarefreeSquares).append("- [").append(line).append(',').append(col)
								.append("] ;\n");
						numSquarefreeSquares++;
					}
				} else {
					if (board.getSquare(line, col).getCard() == null && line <= frontLinePlayer) {
						freeSquares.add(new Square(line, col));
						placeCardSB.append(numSquarefreeSquares).append("- [").append(line).append(',').append(col)
								.append("] ;\n");
						numSquarefreeSquares++;
					}
				}
			}
		}
		/* TODO: Verifier si on peut placer sinon on passer notre tour (plus haut) */
		if (numSquarefreeSquares == 0) {
			throw new IllegalStateException();
		}
		placeCardSB.append('\n');
		System.out.println(placeCardSB.toString());
		int selected = -1;
		do {
			selected = sc.nextInt();
		} while (selected < 0 || selected > numSquarefreeSquares);
		return freeSquares.get(selected);
	}

	/* Prendre dans le deck */
	// StringBuilder placeCardSB = new StringBuilder();
	// placeCardSB.append("Où souhaitez-vous placer votre carte ?\nCes colonnes
	// sont
	// libres ([x,y]):\n");
	// /* Afficher les col libre sur la premiere ligne */
	// int line = 0;
	// if (player == Player.PLAYER1) {
	// line = 4;
	// }
	// ArrayList<Square> freeSquares = new ArrayList<Square>();
	// int numSquarefreeSquares = 0;
	// for (int col = 0; col < 4; col++) {
	// if (board.getSquare(line, col).getCard() == null) {
	// freeSquares.add(new Square(line, col));
	// placeCardSB.append(numSquarefreeSquares).append("-
	// [").append(line).append(',').append(col)
	// .append("] ; ");
	// numSquarefreeSquares++;
	// }
	// }
	// /* TODO: Verifier si on peut placer sinon on passer notre tour (plus haut) */
	// if (numSquarefreeSquares == 0) {
	// throw new IllegalStateException();
	// }
	// placeCardSB.append('\n');
	// System.out.println(placeCardSB.toString());
	// int selected = -1;
	// do {
	// selected = sc.nextInt();
	// } while (selected < 0 || selected > numSquarefreeSquares);
	// return freeSquares.get(selected);
	// }

	/**
	 * Obsolete : Les stats doivent être visible dès le départ
	 * 
	 * @param player
	 */
	public void showStats(Player player) {

		StringBuilder showStatsSB = new StringBuilder();
		showStatsSB.append(player.toString()).append('\n').append('\n').append("HAND :");

		printHand(player);

		StringBuilder deckSB = new StringBuilder();
		for (Cards card : player.getDeck())
			deckSB.append(card.toString()).append('\n');
		showStatsSB.append(deckSB.toString()).append("\n");

		System.out.println(showStatsSB.toString());

	}

	public void printHand(Player player) {
		StringBuilder handSB = new StringBuilder();
		int index = 0;
		for (Cards card : player.getHand()) {
			handSB.append(index).append('\t').append(card.toString()).append('\n');
			index++;
		}
		System.out.println(handSB.toString());

	}

	public Cards exchange(Player player) {
		System.out.println("Quelle carte souhaitez-vous échanger ?");
		printHand(player);
		int indexCardSelected = -1;
		while (indexCardSelected < 0 || indexCardSelected > player.getHand().size())
			indexCardSelected = sc.nextInt();

		return player.getHand().get(indexCardSelected);
	}
}