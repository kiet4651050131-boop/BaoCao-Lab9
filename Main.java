import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        SalesDataSource source = SalesDataSource.sample();
        Path outDir = Path.of("output");
        String title = "Báo cáo doanh thu tháng 11/2026";

        ReportExporter[] exporters = {
            new CsvReportExporter(source),
            new HtmlReportExporter(source),
            new JsonReportExporter(source)
        };

        int caseNo = 1;
        for (ReportExporter exporter : exporters) {
            System.out.println("=== TRƯỜNG HỢP " + caseNo++ + " ===");
            Path file = exporter.export(title, "bao-cao-doanh-thu", outDir);
            System.out.println("  -> Kích thước tệp: " + Files.size(file) + " byte\n");
        }
    }
}
