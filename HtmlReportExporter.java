import java.nio.file.Path;
import java.util.List;

public class HtmlReportExporter extends ReportExporter {
    public HtmlReportExporter(SalesDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getFormatName() {
        return "HTML";
    }

    @Override
    protected String getFileExtension() {
        return "html";
    }

    @Override
    protected String formatContent(String title, List<SalesRecord> data, ReportSummary s) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"vi\">\n<head>\n<meta charset=\"UTF-8\">\n")
          .append("<title>").append(escape(title)).append("</title>\n")
          .append("<style>body{font-family:Arial,sans-serif;margin:32px}")
          .append("table{border-collapse:collapse;width:100%}")
          .append("th,td{border:1px solid #ccc;padding:8px;text-align:left}")
          .append("th{background:#f0f0f0}tfoot td{font-weight:bold}</style>\n</head>\n<body>\n")
          .append("<h1>").append(escape(title)).append("</h1>\n")
          .append("<p>Sản phẩm bán chạy nhất: <b>").append(escape(s.topProduct())).append("</b></p>\n")
          .append("<table>\n<thead><tr><th>Mã đơn</th><th>Ngày</th><th>Sản phẩm</th>")
          .append("<th>SL</th><th>Đơn giá</th><th>Thành tiền</th><th>Trạng thái</th></tr></thead>\n<tbody>\n");
        for (SalesRecord r : data) {
            sb.append("<tr><td>").append(r.orderId()).append("</td><td>").append(r.date())
              .append("</td><td>").append(escape(r.product())).append("</td><td>").append(r.quantity())
              .append("</td><td>").append(formatMoney(r.unitPrice())).append("</td><td>")
              .append(formatMoney(r.total())).append("</td><td>").append(r.status()).append("</td></tr>\n");
        }
        sb.append("</tbody>\n<tfoot><tr><td colspan=\"3\">TỔNG (").append(s.orderCount()).append(" đơn hoàn tất)</td><td>")
          .append(s.totalQuantity()).append("</td><td></td><td>").append(formatMoney(s.totalRevenue()))
          .append("</td><td></td></tr></tfoot>\n</table>\n</body>\n</html>\n");
        log("Bước 5: Đã định dạng nội dung HTML (bảng " + data.size() + " dòng)");
        return sb.toString();
    }

    @Override
    protected void afterExport(Path file) {
        log("Bước 7: Hãy mở tệp bằng trình duyệt để xem: " + file);
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
