import java.util.List;

public class CsvReportExporter extends ReportExporter {
    public CsvReportExporter(SalesDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getFormatName() {
        return "CSV";
    }

    @Override
    protected String getFileExtension() {
        return "csv";
    }

    @Override
    protected String formatContent(String title, List<SalesRecord> data, ReportSummary s) {
        StringBuilder sb = new StringBuilder("\uFEFF");
        sb.append("# ").append(title).append('\n');
        sb.append("Mã đơn,Ngày,Sản phẩm,Số lượng,Đơn giá,Thành tiền,Trạng thái\n");
        for (SalesRecord r : data) {
            sb.append(r.orderId()).append(',')
              .append(r.date()).append(',')
              .append('"').append(r.product()).append('"').append(',')
              .append(r.quantity()).append(',')
              .append(r.unitPrice()).append(',')
              .append(r.total()).append(',')
              .append(r.status()).append('\n');
        }
        sb.append("TỔNG,,,").append(s.totalQuantity()).append(",,").append(s.totalRevenue()).append(",\n");
        log("Bước 5: Đã định dạng nội dung CSV (" + data.size() + " dòng)");
        return sb.toString();
    }
}
