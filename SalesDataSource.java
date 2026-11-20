import java.time.LocalDate;
import java.util.List;

public interface SalesDataSource {
    List<SalesRecord> fetchAll();

    static SalesDataSource sample() {
        return () -> List.of(
            new SalesRecord("DH001", LocalDate.of(2026, 11, 1), "Laptop Dell XPS 13", 1, 28_500_000, SalesRecord.COMPLETED),
            new SalesRecord("DH002", LocalDate.of(2026, 11, 2), "Chuột Logitech MX", 3, 1_450_000, SalesRecord.COMPLETED),
            new SalesRecord("DH003", LocalDate.of(2026, 11, 4), "Bàn phím cơ Keychron", 2, 2_300_000, SalesRecord.CANCELLED),
            new SalesRecord("DH004", LocalDate.of(2026, 11, 5), "Màn hình LG 27 inch", 2, 5_900_000, SalesRecord.COMPLETED),
            new SalesRecord("DH005", LocalDate.of(2026, 11, 8), "Chuột Logitech MX", 5, 1_450_000, SalesRecord.COMPLETED),
            new SalesRecord("DH006", LocalDate.of(2026, 11, 10), "Tai nghe Sony WH-1000XM5", 1, 7_800_000, SalesRecord.CANCELLED),
            new SalesRecord("DH007", LocalDate.of(2026, 11, 12), "Laptop Dell XPS 13", 1, 28_500_000, SalesRecord.COMPLETED),
            new SalesRecord("DH008", LocalDate.of(2026, 11, 15), "Bàn phím cơ Keychron", 4, 2_300_000, SalesRecord.COMPLETED)
        );
    }
}
