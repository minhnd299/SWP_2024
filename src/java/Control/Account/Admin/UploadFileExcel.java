package Control.Account.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/uploadExcel")
@MultipartConfig
public class UploadFileExcel extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");

        // Get the uploaded file part
        Part filePart = request.getPart("file");
        
        // Get the file name and save it to a temporary location
        String fileName = filePart.getSubmittedFileName();
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // Read and process the Excel file
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder result = new StringBuilder();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                        case STRING:
                            result.append(cell.getStringCellValue()).append("\t");
                            break;
                        case NUMERIC:
                            result.append(cell.getNumericCellValue()).append("\t");
                            break;
                        case BOOLEAN:
                            result.append(cell.getBooleanCellValue()).append("\t");
                            break;
                        default:
                            result.append("\t");
                    }
                }
                result.append("\n");
            }

            // Set the result as a request attribute and forward to the result page
            request.setAttribute("result", result.toString());
            request.getRequestDispatcher("/result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } finally {
            // Delete the temporary file
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
