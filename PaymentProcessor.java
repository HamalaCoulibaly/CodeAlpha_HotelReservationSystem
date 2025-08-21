class PaymentProcessor {
    public static boolean processPayment(double amount, String paymentMethod) {
        System.out.println("\n=== Processing Payment ===");
        System.out.printf("Amount: $%.2f\n", amount);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Processing...");
        
        // simulate payment processing delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // simulate 95% success rate
        boolean success = Math.random() > 0.05;
        
        if (success) {
            System.out.println(" Payment successful!");
            System.out.println("Transaction ID: TXN" + (int)(Math.random() * 1000000));
        } else {
            System.out.println(" Payment failed. Please try again.");
        }
        
        return success;
    }
}