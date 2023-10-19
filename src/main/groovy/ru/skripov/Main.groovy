package ru.skripov

import ru.skripov.modules.excel.ExcelReader
import ru.skripov.modules.excel.ExcelWriter
import ru.skripov.modules.qr_codes.QrCodes

static void main(String[] args) {
    QrCodes qrCodes = new QrCodes()
    ExcelReader inputExcelReader = new ExcelReader("/home/skripov/git/myProjects/find_qr_on_photo/input.xls")
    ExcelWriter outputExcelWriter = new ExcelWriter("/home/skripov/git/myProjects/find_qr_on_photo/output.xls")

    inputExcelReader.eachLine {it ->
        int cellNum = 0
        for (int i = 0; i < (it.cells - null).size(); i++) {
            String url = inputExcelReader.cell(i).toString()
            if (!url) {
                println "СТРОКА НОМЕР: $it.rowNum, ЯЧЕЙКА НОМЕР: $i"
                continue
            }
            String result = qrCodes.findQrCode(url)

            switch (result) {
                case "UNKNOWN":
                    println "Не смог распознать, есть ли qr-код $url"
                    break
                case "NO_CODE":
                    outputExcelWriter.writeToExcel((it.rowNum + 1) as int, cellNum, url)
                    cellNum++
                    break
                default:
                    println "Был найден qr-код $url"
                    break
            }
        }
    }

    qrCodes.close()
}
