package com.dochub;

import com.dochub.common.BatchImportDto;
import com.dochub.common.CreateBatchImportDataDto;
import com.dochub.common.PlaceHolderKey;
import com.dochub.common.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dochub.service.DocHubService;
import org.apache.commons.lang3.RandomStringUtils;

public class Main {
    public static void main(String[] args) throws IOException {
        String username = "";
        String password = "";
        String comId = "164";
        String baseUrl = "http://10.70.100.213:2036";

        int documentTemplateId = 1141; // lấy từ api danh sách mẫu chứng từ (/api/document-templates)
        int documentTypeId = 1089;// lấy từ api danh sách loại chứng từ (/api/document-types)
        int departmentId = 33;// lấy từ api danh sách bộ phận (/api/departments)

        DocHubService docHubService = new DocHubService();

        // Xác thực
        Result<String> authResult = docHubService.AuthenticateAsync(username, password, comId, baseUrl);
        System.out.println(authResult.getMessages()[0]);

        // Tạo lô chứng từ
        CreateBatchImportDataDto batchImportData = new CreateBatchImportDataDto();
        //batchImportData.setName("Ten lo");
        batchImportData.setDocumentTemplateId(documentTemplateId);
        batchImportData.setDocumentTypeId(documentTypeId);
        batchImportData.setDepartmentId(departmentId);

        // parameters - các tham số - các tham số phải đúng thứ tự
        // 6 tham số đầu là bắt buộc - ko được thay đổi thứ tự
        List<String> parameters = new ArrayList<>();
        parameters.addAll(Arrays.asList(
                PlaceHolderKey.FileName,
                PlaceHolderKey.No,
                PlaceHolderKey.Subject,
                PlaceHolderKey.ExpiryDate,
                PlaceHolderKey.Description,
                PlaceHolderKey.IsOrder));

        // danh sách người nhận - không được thay đổi thứ tự - gồm cặp người xử lý + quyền truy cập
        // VD: quy trình gồm 2 người xử lý
        for (int i = 1; i <= 2; i++)
        {
            parameters.addAll(Arrays.asList(PlaceHolderKey.Code, PlaceHolderKey.AccessPermission));
        }

        // tham số phụ => lấy từ api lấy thông tin mẫu chứng từ (/api/document-templates/{id})
        parameters.add("{{day}}");
        parameters.add("{{month}}");
        parameters.add("{{year}}");
        parameters.add("{{ben_a}}");
        parameters.add("{{ben_b}}");
        parameters.add("{{dien_tich_dat}}");
        parameters.add("{{dien_tich_nha}}");

        List<List<String>> rows = new ArrayList<List<String>>();
        String rdDocumentNo = RandomStringUtils.random(4, true, true);
        for (int i = 1; i <= 2; i++)
        {
            List<String> row = new ArrayList<>();
            row.addAll(Arrays.asList(
                    rdDocumentNo + i, // tên tệp tin
                    rdDocumentNo + i, // mã chứng từ không được phép trùng
                    "Hợp đồng " + rdDocumentNo + i, //tên chứng từ
                    "", //ngày hết hạn - định dạng dd/mm/yyyy (vd: 20/11/2022)
                    "Chứng từ thử nghiệm", //mô tả (nếu có)
                    "Y" //xử lý tuần tự (Y/N)
            ));

            // quy trình ký - số lượng người phải khớp với tham số phía trên - quy trình gồm 2 người thì phải thêm đủ 2 người
            row.addAll(Arrays.asList("baoth", "DR"));
            row.addAll(Arrays.asList("baoth", "D"));

            // Dữ liệu fill => truyền đúng vị trí ứng với tham số bên trên
            row.add("23");
            row.add("11");
            row.add("2022");
            row.add("Nguyễn Văn A_" + i);
            row.add("Nguyễn Văn B_" + i);
            row.add("1000");
            row.add("700");

            rows.add(row);
        }

        batchImportData.setParameters(parameters);
        batchImportData.setRows(rows);

        Result<BatchImportDto> createResult = docHubService.CreateBatchImportAsync(batchImportData);
        System.out.println(createResult.getMessages()[0]);

        if (createResult.getSuccess() && createResult.getData() != null){
            int batchId = createResult.getData().getId();
            Result<BatchImportDto> sendResult = docHubService.SendBatchDocumentAsync(batchId);
            System.out.println(sendResult.getMessages()[0]);
        }
    }
}
