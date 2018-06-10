package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.Board;
import model.Player;
import model.Square;
import model.cards.Cards;

public class View {

	private final Scanner sc = new Scanner(System.in);

	public void window(Board b) {
		System.out.println(b.getP2().toString());
		System.out.print("  ");
		for (int showNumberCol = 0; showNumberCol < 5; showNumberCol++) {
			System.out.print(showNumberCol + " ");
		}
		System.out.print("\n" + "  ");
		for (int underscore = 0; underscore < 5; underscore++) {
			System.out.print("- ");
		}
		System.out.println();

		board(b);

		System.out.println(b.getP1().toString());
	}

	private void board(Board b) {
		for (int line = 0; line < 5; line++) {
			System.out.print(line + "|");
			for (int col = 0; col < 4; col++) {
				Square square = b.getSquare(line, col);
				Player p = square.getPlayer();
				/* TODO : A changer */
				if (p == Player.PLAYER1) {
					couleur(square.getCard().toString().substring(0, 1).toUpperCase(), p);
				} else if (p == Player.PLAYER2) {
					couleur(square.getCard().toString().substring(0, 1).toLowerCase(), p);
				} else {
					System.out.print("  ");
				}
				System.out.print('|');
			}
			System.out.println();
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
		Scanner sc = new Scanner(System.in);
		System.out.println("A votre tour :" + player.getName());
		System.out.println("Que souhaitez-vous faire ?");
		System.out.println("1 - Placer une carte.");
		System.out.println("2 - Afficher mes stats.");
		System.out.println("3 - Passer mon tour.");
		int choice = sc.nextInt();
		while (choice < 1 || choice > 3) {
			System.out.println("Erreur. Invalide parametre. Donnez un choix valide.");
			choice = sc.nextInt();
		}
		return choice;

	}

	public Cards selectCardOnHand(Player player) {
		StringBuilder selectCardOnHandSB = new StringBuilder();
		selectCardOnHandSB.append("Les cartes suivante sont disponibles :");
		ArrayList<Cards> handTmp = player.getHand();
		int index = 0;
		for (Cards card : handTmp) {
			selectCardOnHandSB.append(index).append('\t').append(card.toString()).append('\n');
			index++;
		}
		/*
		 * TODO : Test si aucune main mais presque impossible (seulement si on arrive à
		 * cours de carte dans notre deck)
		 */
		int selected = -1;
		do {
			selected = sc.nextInt();
		} while (selected < 0 || selected > index);
		return handTmp.get(selected);

	}

	public Square selectSquareOnBoard(Player player, Board board) {
		/* Prendre dans le deck */
		StringBuilder placeCardSB = new StringBuilder();
		placeCardSB.append("Où souhaitez-vous placer votre carte ?\nCes colonnes sont libres ([x,y]):\n");
		/* Afficher les col libre sur la premiere ligne */
		int line = 0;
		if (player == Player.PLAYER1) {
			line = 4;
		}
		ArrayList<Square> free = new ArrayList<Square>();
		int nbSquareFree = 0;
		for (int col = 0; col < 4; col++) {
			if (board.getSquare(line, col).getCard() == null) {
				free.add(new Square(line, col));
				placeCardSB.append(nbSquareFree).append("- [").append(line).append(',').append(col).append("] ; ");
				nbSquareFree++;
			}
		}
		/* TODO: Verifier si on peut placer sinon on passer notre tour (plus haut) */
		placeCardSB.append('\n');
		System.out.println(placeCardSB.toString());
		int selected = -1;
		do {
			selected = sc.nextInt();
		} while (selected < 0 || selected > nbSquareFree);
		return free.get(selected);
	}

	public void showStats(Player player) {

		StringBuilder showStatsSB = new StringBuilder();
		showStatsSB.append(player.toString()).append('\n').append('\n').append("HAND :");

		StringBuilder handSB = new StringBuilder();
		for (Cards card : player.getDeck())
			handSB.append(card.toString()).append('\n');

		showStatsSB.append(handSB.toString()).append('\n').append("DECK :");

		StringBuilder deckSB = new StringBuilder();
		for (Cards card : player.getDeck())
			deckSB.append(card.toString()).append('\n');
		showStatsSB.append(deckSB.toString()).append("\n");

		System.out.println(showStatsSB.toString());

	}
}