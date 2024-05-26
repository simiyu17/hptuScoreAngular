package com.hptu.util;

import com.hptu.report.dto.CountySummaryDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelGenerator {

    private final Log log = LogFactory.getLog(ExcelGenerator.class);
    List<CountySummaryDto> countySummaryDtos;
    Map<String, List<CountySummaryDto>> categorySummary;

    String graphTitle;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List<CountySummaryDto> countySummaryDtos, Map<String, List<CountySummaryDto>> categorySummary, String graphTitle) {
        this.countySummaryDtos = countySummaryDtos;
        this.categorySummary = categorySummary;
        this.graphTitle = graphTitle;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("County HPTU Score Summary");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Assessment Pillar", style, false, 0);
        createCell(row, 1, "Max score", style, false, 0);
        createCell(row, 2, "Score Attained", style, false, 0);
        createCell(row, 3, "% Score", style, false, 0);
        createCell(row, 4, "Remarks", style, false, 0);
    }

    private void writePillarHeading(String pillar, int rowNum) {
        Row pillarRow = sheet.createRow(rowNum);
        pillarRow.setHeight((short) 500);
        CellStyle pillarStyle = workbook.createCellStyle();
        pillarStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        pillarStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        pillarStyle.setAlignment(HorizontalAlignment.CENTER);
        pillarStyle.setWrapText(true);
        XSSFFont pillarFont = workbook.createFont();
        pillarFont.setBold(true);
        pillarFont.setFontHeight(16);
        pillarFont.setUnderline(Font.U_SINGLE);
        pillarStyle.setFont(pillarFont);
        createCell(pillarRow, 0, pillar, pillarStyle, true, rowNum);
    }

    private void writeCategoryHeader(int rowNum) {
        Row row = sheet.createRow(rowNum);
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        createCell(row, 0, "Category Name", style, false, 0);
        createCell(row, 1, "Score Attained", style, false, 0);
        createCell(row, 2, "Max score", style, false, 0);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style, boolean mergeCells, int rowNum) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        switch (valueOfCell) {
            case Integer i -> cell.setCellValue(i);
            case Long l -> cell.setCellValue(l);
            case String s -> cell.setCellValue(s);
            case BigDecimal bigDecimal -> cell.setCellValue(bigDecimal.doubleValue());
            case null, default -> cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
        if (mergeCells){
            sheet.addMergedRegion(new CellRangeAddress(rowNum, // first row (0-based)
                    rowNum, // last row (0-based)
                    0, // first column (0-based)
                    2 // last column (0-based)
            ));
        }
    }

    private void write() throws IOException {
        writeCountyMainData();
        writeCountyPillarData();
    }

    private void writeCountyMainData() throws IOException {
        int rowCount = 1;
        int colCount = 0;
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.BOTTOM);
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        for (CountySummaryDto countySummary: countySummaryDtos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, countySummary.getPillarName(), style, false, 0);
            createCell(row, columnCount++, countySummary.getMaxScore(), style, false, 0);
            createCell(row, columnCount++, countySummary.getChoiceScore(), style, false, 0);
            createCell(row, columnCount++, countySummary.getScorePercent(), style, false, 0);
            createCell(row, columnCount++, countySummary.getRemark(), style, false, 0);
            colCount = columnCount;
        }

        chart(createCountySummaryDataset(), rowCount, ++colCount, new ChartDimension("Pillars", "% Score", PlotOrientation.VERTICAL, (250 * countySummaryDtos.size()), 800, this.graphTitle));
    }

    private void writeCountyPillarData() throws IOException {
        int rowCount = 50;
        int colCount = 0;
        int graphRowStart = 0;
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.BOTTOM);
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        for (Map.Entry<String, CategoryDataset> entry: this.createCategorySummaryDatasets().entrySet()) {
            writePillarHeading(entry.getKey(), rowCount++);
            writeCategoryHeader(rowCount++);
            List<CountySummaryDto> cats = this.categorySummary.get(entry.getKey());
            for (CountySummaryDto countySummary: cats) {
                graphRowStart = rowCount;
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, countySummary.getCategory(), style, false, 0);
                createCell(row, columnCount++, countySummary.getChoiceScore(), style, false, 0);
                createCell(row, columnCount++, countySummary.getMaxScore(), style, false, 0);
                colCount = columnCount + 2;
            }

            chart(entry.getValue(), graphRowStart, colCount, new ChartDimension("Categories", "Scores", PlotOrientation.HORIZONTAL, 2000, 800, entry.getKey()));
            rowCount += 50;
            colCount = 0;


        }
    }

    private void chart(CategoryDataset dataset, int row, int col, ChartDimension chartDimension) throws IOException {
        JFreeChart chart = createChart(dataset, chartDimension);
        /* Write chart as JPG to Output Stream */
        ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
        BufferedImage objBufferedImage=chart.createBufferedImage(chartDimension.chartWidth(),chartDimension.chartHeight());
        try {
            ImageIO.write(objBufferedImage, "png", chartOut);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        /* We now read from the output stream and frame the input chart data */
        InputStream feedChartToExcel=new ByteArrayInputStream(chartOut.toByteArray());
        byte[] bytes = IOUtils.toByteArray(feedChartToExcel);
        /* Add picture to workbook */
        int myChartPictureId = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        /* We can close Piped Input Stream. We don't need this */
        feedChartToExcel.close();
        /* Close PipedOutputStream also */
        chartOut.close();
        /* Create the drawing container */
        XSSFSheet mySheet = workbook.getSheetAt(0);
        XSSFDrawing drawing = mySheet.createDrawingPatriarch();
        /* Create an anchor point */
        ClientAnchor myAnchor = new XSSFClientAnchor();
        /* Define top left corner, and we can resize picture suitable from there */
        myAnchor.setCol1(col);
        myAnchor.setRow1(row);
        /* Invoke createPicture and pass the anchor point and ID */
        XSSFPicture myChartPicture = drawing.createPicture(myAnchor, myChartPictureId);
        /* Call resize method, which resizes the image */
        myChartPicture.resize();
    }

    private CategoryDataset createCountySummaryDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (CountySummaryDto countySummary: countySummaryDtos) {
            dataset.setValue(countySummary.getScorePercent(), "Pillars Score", countySummary.getPillarName());
        }


        return dataset;
    }

    private Map<String, CategoryDataset> createCategorySummaryDatasets() {

        Map<String, CategoryDataset> datasets = new HashMap<>();
        for (Map.Entry<String, List<CountySummaryDto>> entry: this.categorySummary.entrySet()){
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (CountySummaryDto countySummary: this.categorySummary.get(entry.getKey())) {
                dataset.setValue(countySummary.getChoiceScore(), "Category Score", countySummary.getCategory());
            }
            datasets.put(entry.getKey(), dataset);
        }

        return datasets;
    }

    private JFreeChart createChart(CategoryDataset dataset, ChartDimension chartDimension) {
        return ChartFactory.createBarChart(
                chartDimension.chartTitle(),
                chartDimension.xLabel(),
                chartDimension.yLabel(),
                dataset,
                chartDimension.plotOrientation(),
                false, true, false);
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public String generateExcelBase64String() throws IOException {
        writeHeader();
        write();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        String encodedString = Base64.getEncoder().encodeToString(bos.toByteArray());
        workbook.close();
        bos.close();
        return encodedString;
    }

    record ChartDimension(String xLabel, String yLabel, PlotOrientation plotOrientation, int chartWidth,
                          int chartHeight, String chartTitle) {
    }
}
