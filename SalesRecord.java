import java.time.LocalDate;

public record SalesRecord(String orderId, LocalDate date, String product,
                          int quantity, long unitPrice, String status) {
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";

    public long total() {
        return quantity * unitPrice;
    }

    public boolean isCancelled() {
        return CANCELLED.equals(status);
    }
}
