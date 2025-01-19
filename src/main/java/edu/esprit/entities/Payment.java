package edu.esprit.entities;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class Payment {
    public static void main(String[] args) {
// Set your secret key here
        Stripe.apiKey = "sk_test_51OqhXjJva8icsVFnMSIRaC96mFFkuxUSauUF3Sm3j1vWBQ4MCFvACCWiBfbPeH7660Dy4DZz87lDeMlk6rmlxsNI009Kw7CrJn";

        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}