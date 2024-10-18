package nl.rocnijmegen.testing;

import javax.swing.*;
import java.text.NumberFormat;

public class App {

    public static final double INTEREST_1_YEAR = 2.0;
    public static final double INTEREST_5_YEAR = 3.0;
    public static final double INTEREST_10_YEAR = 3.5;
    public static final double INTEREST_20_YEAR = 4.5;
    public static final double INTEREST_30_YEAR = 5.0;
    public static final double STUDENT_LOAN_REDUCTION = 0.75;
    public static final int MAX_LOAN_MULTIPLIER = 5;

    public static void main(String[] args) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        String postcode = getPostcode();
        if (isPostcodeRestricted(postcode)) {
            showErrorMessage("Hypotheekberekeningen voor deze postcode zijn niet toegestaan vanwege het aardbevingsgebied.");
            return;
        }

        double inkomen = getIncome("Wat is uw maandinkomen? €");
        double partnerInkomen = getPartnerIncome();
        boolean heeftStudieschuld = hasStudentLoan();
        int jaren = getRentevastePeriode();

        if (jaren == -1) {
            showErrorMessage("Ongeldige rentevaste periode. Kies uit 1, 5, 10, 20 of 30 jaar.");
            return;
        }

        double totaalInkomen = calculateTotalIncome(inkomen, partnerInkomen);
        double maximaalLeenbedrag = calculateMaxLoan(totaalInkomen, heeftStudieschuld);
        double rente = getInterestRate(jaren);
        double maandlasten = calculateMonthlyMortgagePayment(maximaalLeenbedrag, rente, jaren);
        double totaalBetaling = calculateTotalPayment(maandlasten, jaren);
        double totaleRente = calculateTotalInterest(totaalBetaling, maximaalLeenbedrag);

        showResults(currencyFormat, maximaalLeenbedrag, maandlasten, totaleRente, jaren, totaalBetaling);
    }

    public static String getPostcode() {
        return JOptionPane.showInputDialog("Wat is uw postcode?");
    }

    public static boolean isPostcodeRestricted(String postcode) {
        return postcode.equals("9679") || postcode.equals("9681") || postcode.equals("9682");
    }

    public static double getIncome(String message) {
        return Double.parseDouble(JOptionPane.showInputDialog(message));
    }

    public static double getPartnerIncome() {
        int keuze = JOptionPane.showConfirmDialog(null, "Heeft u een partner?", "Partner", JOptionPane.YES_NO_OPTION);
        if (keuze == JOptionPane.YES_OPTION) {
            return getIncome("Wat is het maandinkomen van uw partner? €");
        }
        return 0;
    }

    public static boolean hasStudentLoan() {
        int keuze = JOptionPane.showConfirmDialog(null, "Heeft u een studieschuld?", "Studieschuld", JOptionPane.YES_NO_OPTION);
        return keuze == JOptionPane.YES_OPTION;
    }

    public static int getRentevastePeriode() {
        try {
            int jaren = Integer.parseInt(JOptionPane.showInputDialog("Kies een rentevaste periode: 1, 5, 10, 20 of 30 jaar"));
            if (jaren == 1 || jaren == 5 || jaren == 10 || jaren == 20 || jaren == 30) {
                return jaren;
            }
        } catch (NumberFormatException e) {
            // Invalid input handling
        }
        return -1;
    }

    public static double calculateTotalIncome(double inkomen, double partnerInkomen) {
        return inkomen + partnerInkomen;
    }

    public static double calculateMaxLoan(double totaalInkomen, boolean heeftStudieschuld) {
        double maxLoan = totaalInkomen * 12 * MAX_LOAN_MULTIPLIER;
        if (heeftStudieschuld) {
            maxLoan *= STUDENT_LOAN_REDUCTION;
        }
        return maxLoan;
    }

    public static double getInterestRate(int jaren) {
        switch (jaren) {
            case 1: return INTEREST_1_YEAR;
            case 5: return INTEREST_5_YEAR;
            case 10: return INTEREST_10_YEAR;
            case 20: return INTEREST_20_YEAR;
            case 30: return INTEREST_30_YEAR;
            default: throw new IllegalArgumentException("Ongeldige rentevaste periode.");
        }
    }

    public static double calculateMonthlyMortgagePayment(double maxLoan, double interestRate, int jaren) {
        double maandRente = interestRate / 100 / 12;
        int aantalBetalingen = jaren * 12;
        return maxLoan * (maandRente * Math.pow(1 + maandRente, aantalBetalingen)) /
                (Math.pow(1 + maandRente, aantalBetalingen) - 1);
    }

    public static double calculateTotalPayment(double maandlasten, int jaren) {
        return maandlasten * jaren * 12;
    }

    public static double calculateTotalInterest(double totaalBetaling, double maxLoan) {
        return totaalBetaling - maxLoan;
    }

    public static void showResults(NumberFormat currencyFormat, double maxLoan, double maandlasten, double totaleRente, int jaren, double totaalBetaling) {
        JOptionPane.showMessageDialog(null, "Op basis van uw inkomen kunt u maximaal lenen: " + currencyFormat.format(maxLoan)
                + "\nMaandelijkse lasten: " + currencyFormat.format(maandlasten)
                + "\nMaandelijks te betalen rente: " + currencyFormat.format(totaleRente / (jaren * 12))
                + "\nMaandelijks af te lossen bedrag: " + currencyFormat.format(maandlasten - (totaleRente / (jaren * 12)))
                + "\nTotale betaling over " + jaren + " jaar: " + currencyFormat.format(totaalBetaling));
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
