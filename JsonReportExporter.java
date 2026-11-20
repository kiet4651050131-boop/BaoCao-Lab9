import java.nio.file.Path;
import java.util.List;

public class JsonReportExporter extends ReportExporter {
    public JsonReportExporter(SalesDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getFormatName() {
        return "JSON";
    }

    @Override
    protected String getFileExtension() {
        return "json";
    }

    @Override
    protected List<SalesRecord> filterData(List<SalesRecord> data) {
        List<SalesRecord> result = data.stream().filter(r -> !r.isCancelled()).toList();
        log("Bước 3: Đã loại " + (data.size() - result.size()) + " đơn đã hủy, còn " + result.size() + " đơn");
        return result;
    }

    @Override
    protected String formatContent(String title, List<SalesRecord> data, ReportSummary s) {
        StringBuilder sb = new StringBuilder("{\n");
        sb.append("  \"title\": \"").append(esc(title)).append("\",\n");
        sb.append("  \"summary\": {\"orderCount\": ").append(s.orderCount())
          .append(", \"totalQuantity\": ").append(s.totalQuantity())
          .append(", \"totalRevenue\": ").append(s.totalRevenue())
          .append(", \"topProduct\": \"").append(esc(s.topProduct())).append("\"},\n");
        sb.append("  \"orders\": [\n");
        for (int i = 0; i < data.size(); i++) {
            SalesRecord r = data.get(i);
            sb.append("    {\"orderId\": \"").append(r.orderId())
              .append("\", \"date\": \"").append(r.date())
              .append("\", \"product\": \"").append(esc(r.product()))
              .append("\", \"quantity\": ").append(r.quantity())
              .append(", \"unitPrice\": ").append(r.unitPrice())
              .append(", \"total\": ").append(r.total()).append('}')
              .append(i < data.size() - 1 ? "," : "").append('\n');
        }
        sb.append("  ]\n}\n");
        log("Bước 5: Đã định dạng nội dung JSON (" + data.size() + " đơn)");
        return sb.toString();
    }

    @Override
    protected void afterExport(Path file) {
        log("Bước 7: Gửi thông báo tới hệ thống đích: tệp " + file.getFileName() + " sẵn sàng");
    }

    private static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
