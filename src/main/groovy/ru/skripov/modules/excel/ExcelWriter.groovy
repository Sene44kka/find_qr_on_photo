package ru.skripov.modules.excel

import com.sun.media.sound.InvalidFormatException
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
@Grab('org.apache.poi:poi:3.9')
@Grab('org.apache.poi:poi-ooxml:3.9')

import org.apache.poi.ss.usermodel.WorkbookFactory

class ExcelWriter {
    def workbook
    def labels
    def row

    FileOutputStream fos

    ExcelWriter(String fileName) {
        DataFormatter dataFormatter = new DataFormatter()

        Row.metaClass.getAt = { int idx ->
            Cell cell = delegate.getCell(idx)

            if (!cell) {
                return null
            }

            return dataFormatter.formatCellValue(cell)
        }

        workbook = WorkbookFactory.create(new File(fileName))
//        fos = new FileOutputStream(fileName)
    }

    void writeToExcel(int rowNum, int cellNum, String desc){
        try {
            Sheet sheet = workbook.sheets[0]
            Row row = sheet.getRow(rowNum)

            if (!row) {
                row = sheet.createRow(rowNum)
            }
            Cell cell = row.createCell(cellNum)
            cell.setCellValue(desc)
            FileOutputStream fos = new FileOutputStream("/home/skripov/git/myProjects/find_qr_on_photo/output.xls")
            workbook.write(fos)
            fos.close()
        } catch (FileNotFoundException e) {
            println "FileNotFoundException"
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            println "InvalidFormatException"
            e.printStackTrace();
        } catch (IOException e) {
            println "IOException"
            e.printStackTrace();
        }
    }
}
