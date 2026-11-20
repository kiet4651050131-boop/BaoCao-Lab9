import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class ReportExporter {
    private final SalesDataSource dataSource;
    private final List<String> executionLog = new ArrayList<>();

    protected ReportExporter(SalesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final Path export(String title, String baseFileName, Path outputDir) throws IOException {
        executionLog.clear();
        log("Bắt đầu xuất báo cáo định dạng " + getFormatName());

        List<SalesRecord> data = loadData();
        validateData(data);
        data = filterData(data);
        ReportSummary summary = calculateSummary(data);
        String content = formatContent(title, data, summary);
        Path file = saveToFile(content, baseFileName, outputDir);
        afterExport(file);

        log("Hoàn tất!");
        return file;
    }

    private List<SalesRecord> loadData() {
        List<SalesRecord> data = dataSource.fetchAll();
        log("Bước 1: Đã tải " + data.size() + " bản ghi từ nguồn dữ liệu");
        return data;
    }

    private void validateData(List<SalesRecord> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalStateException("Không có dữ liệu để xuất báo cáo");
        }
        for (SalesRecord r : data) {
            if (r.quantity() <= 0 || r.unitPrice() < 0) {
                throw new IllegalStateException("Dữ liệu không hợp lệ ở đơn " + r.orderId());
            }
        }
        log("Bước 2: Dữ liệu hợp lệ");
    }

    private ReportSummary calculateSummary(List<SalesRecord> data) {
        int qty = 0;
        int count = 0;
        long revenue = 0;
        Map<String, Integer> soldByProduct = new LinkedHashMap<>();
        for (SalesRecord r : data) {
            if (r.isCancelled()) continue;
            count++;
            qty += r.quantity();
            revenue += r.total();
            soldByProduct.merge(r.product(), r.quantity(), Integer::sum);
        }
        String top = Collections.max(soldByProduct.entrySet(),
                Map.Entry.comparingByValue(Comparator.naturalOrder())).getKey();
        log("Bước 4: Tổng hợp xong - " + count + " đơn hoàn tất, doanh thu " + formatMoney(revenue));
        return new ReportSummary(count, qty, revenue, top);
    }

    private Path saveToFile(String content, String baseFileName, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Path file = outputDir.resolve(baseFileName + "." + getFileExtension());
        Files.writeString(file, content, StandardCharsets.UTF_8);
        log("Bước 6: Đã ghi tệp " + file);
        return file;
    }

    protected List<SalesRecord> filterData(List<SalesRecord> data) {
        log("Bước 3: Không lọc dữ liệu (giữ nguyên " + data.size() + " bản ghi)");
        return data;
    }

    protected void afterExport(Path file) {
        log("Bước 7: Không có thao tác bổ sung");
    }

    protected abstract String getFormatName();

    protected abstract String getFileExtension();

    protected abstract String formatContent(String title, List<SalesRecord> data, ReportSummary summary);

    protected final void log(String message) {
        executionLog.add(message);
        System.out.println("  " + message);
    }

    public final List<String> getExecutionLog() {
        return Collections.unmodifiableList(executionLog);
    }

    protected static String formatMoney(long amount) {
        return String.format(Locale.of("vi", "VN"), "%,d đ", amount);
    }
}
