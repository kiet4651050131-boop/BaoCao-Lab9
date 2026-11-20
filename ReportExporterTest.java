import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReportExporterTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        Path tmp = Files.createTempDirectory("report-test");
        SalesDataSource source = SalesDataSource.sample();

        CsvReportExporter csv = new CsvReportExporter(source);
        Path csvFile = csv.export("Test", "t", tmp);
        List<String> log = csv.getExecutionLog();
        check("TC1 Thứ tự bước 1->7 đúng", orderIsCorrect(log));

        List<String> csvLines = Files.readAllLines(csvFile);
        check("TC2 CSV có 8 dòng dữ liệu, tổng doanh thu 89.600.000",
                csvLines.stream().filter(l -> l.startsWith("DH")).count() == 8
                && csvLines.stream().anyMatch(l -> l.contains("TỔNG") && l.contains("89600000")));

        JsonReportExporter json = new JsonReportExporter(source);
        String jsonText = Files.readString(json.export("Test", "t", tmp));
        check("TC3 JSON không chứa đơn đã hủy (DH003, DH006)",
                !jsonText.contains("DH003") && !jsonText.contains("DH006"));
        check("TC4 JSON tổng doanh thu = 89.600.000", jsonText.contains("\"totalRevenue\": 89600000"));

        HtmlReportExporter html = new HtmlReportExporter(source);
        String htmlText = Files.readString(html.export("Test", "t", tmp));
        check("TC5 HTML chứa <table> và sản phẩm bán chạy",
                htmlText.contains("<table>") && htmlText.contains("Chuột Logitech MX"));

        boolean thrown = false;
        try {
            new CsvReportExporter(List::of).export("Test", "t", tmp);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        check("TC6 Dữ liệu rỗng bị từ chối", thrown);

        System.out.println("\nKẾT QUẢ: " + passed + " đạt, " + failed + " lỗi");
        if (failed > 0) System.exit(1);
    }

    private static boolean orderIsCorrect(List<String> log) {
        String[] prefixes = {"Bước 1", "Bước 2", "Bước 3", "Bước 4", "Bước 5", "Bước 6", "Bước 7"};
        int pos = -1;
        for (String p : prefixes) {
            int idx = -1;
            for (int i = 0; i < log.size(); i++) {
                if (log.get(i).startsWith(p)) { idx = i; break; }
            }
            if (idx <= pos) return false;
            pos = idx;
        }
        return true;
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + name);
        if (ok) passed++; else failed++;
    }
}
