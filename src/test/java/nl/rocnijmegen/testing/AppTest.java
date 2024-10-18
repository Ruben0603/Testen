package nl.rocnijmegen.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AppTest {

    @Spy
    private App app;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        app = new App();
    }

    @Test
    public void getPostcodeTest() {
        String expectedPostcode = "1234AB";

        try (MockedStatic<JOptionPane> mockedJOptionPane = mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(anyString())).thenReturn(expectedPostcode);

            String actualPostcode = app.getPostcode();

            // Print expected and actual values
            System.out.println("Expected postcode: " + expectedPostcode);
            System.out.println("Actual postcode: " + actualPostcode);

            assertEquals(expectedPostcode, actualPostcode, "The postcode should match the expected value.");
        }
    }

    @Test
    public void testIsPostcodeRestricted() {
        String restrictedPostcode = "9679";
        String nonRestrictedPostcode = "1234AB";

        boolean expectedRestricted = true;
        boolean actualRestricted = app.isPostcodeRestricted(restrictedPostcode);

        boolean expectedNonRestricted = false;
        boolean actualNonRestricted = app.isPostcodeRestricted(nonRestrictedPostcode);

        // Print expected and actual values
        System.out.println("Restricted postcode - Expected: " + expectedRestricted + ", Actual: " + actualRestricted);
        System.out.println("Non-restricted postcode - Expected: " + expectedNonRestricted + ", Actual: " + actualNonRestricted);

        assertEquals(expectedRestricted, actualRestricted, "The postcode should be restricted.");
        assertEquals(expectedNonRestricted, actualNonRestricted, "The postcode should not be restricted.");
    }

    @Test
    public void testCalculateMaxLoan() {
        double income = 3000;
        double partnerIncome = 1500;
        boolean hasStudentLoan = true;

        double totalIncome = app.calculateTotalIncome(income, partnerIncome);
        double actualMaxLoan = app.calculateMaxLoan(totalIncome, hasStudentLoan);

        double expectedMaxLoan = (totalIncome * 12 * App.MAX_LOAN_MULTIPLIER) * App.STUDENT_LOAN_REDUCTION;

        System.out.println("Max Loan - Expected: " + expectedMaxLoan + ", Actual: " + actualMaxLoan);

        assertEquals(expectedMaxLoan, actualMaxLoan, "The maximum loan should be calculated correctly.");
    }

    @Test
    public void testInterestRate() {
        double expected1YearRate = App.INTEREST_1_YEAR;
        double actual1YearRate = app.getInterestRate(1);

        double expected5YearRate = App.INTEREST_5_YEAR;
        double actual5YearRate = app.getInterestRate(5);

        double expected10YearRate = App.INTEREST_10_YEAR;
        double actual10YearRate = app.getInterestRate(10);

        double expected20YearRate = App.INTEREST_20_YEAR;
        double actual20YearRate = app.getInterestRate(20);

        double expected30YearRate = App.INTEREST_30_YEAR;
        double actual30YearRate = app.getInterestRate(30);

        System.out.println("Interest Rate (1 year) - Expected: " + expected1YearRate + ", Actual: " + actual1YearRate);
        System.out.println("Interest Rate (5 years) - Expected: " + expected5YearRate + ", Actual: " + actual5YearRate);
        System.out.println("Interest Rate (10 years) - Expected: " + expected10YearRate + ", Actual: " + actual10YearRate);
        System.out.println("Interest Rate (20 years) - Expected: " + expected20YearRate + ", Actual: " + actual20YearRate);
        System.out.println("Interest Rate (30 years) - Expected: " + expected30YearRate + ", Actual: " + actual30YearRate);

        assertEquals(expected1YearRate, actual1YearRate, "Interest rate for 1 year should be correct.");
        assertEquals(expected5YearRate, actual5YearRate, "Interest rate for 5 years should be correct.");
        assertEquals(expected10YearRate, actual10YearRate, "Interest rate for 10 years should be correct.");
        assertEquals(expected20YearRate, actual20YearRate, "Interest rate for 20 years should be correct.");
        assertEquals(expected30YearRate, actual30YearRate, "Interest rate for 30 years should be correct.");
    }

    @Test
    public void testCalculateMonthlyMortgagePayment() {
        double maxLoan = 100000;
        double interestRate = 3.5;
        int years = 20;

        double expectedMonthlyPayment = app.calculateMonthlyMortgagePayment(maxLoan, interestRate, years);
        double actualMonthlyPayment = app.calculateMonthlyMortgagePayment(maxLoan, interestRate, years);

        System.out.println("Monthly Payment - Expected: " + expectedMonthlyPayment + ", Actual: " + actualMonthlyPayment);

        assertEquals(expectedMonthlyPayment, actualMonthlyPayment, "Monthly mortgage payment calculation should be correct.");
    }
}
