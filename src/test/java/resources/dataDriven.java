package resources;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class dataDriven
{

    public ArrayList<String> getData(String testcaseName) throws IOException
    {
        ArrayList<String> a = new ArrayList<String>();
        FileInputStream fis = new FileInputStream("C:\\Users\\gregm\\IdeaProjects\\DataDriven\\src\\test\\demoData.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        int sheets = workbook.getNumberOfSheets();
        for (int i=0; i < sheets; i++)
        {
            if (workbook.getSheetName(i).equalsIgnoreCase("testdata"))
            {
                XSSFSheet sheet = workbook.getSheetAt(i);

                Iterator<Row> rows = sheet.iterator();
                Row firstRow =  rows.next(); //control is now on the first row

                //Now iterate the cells (columns) in the first row
                Iterator<Cell> ce = firstRow.cellIterator();

                int k =0;
                int column=0;

                while (ce.hasNext())
                {
                    //Find the column of cell that contains "Testcases"
                    Cell value = ce.next();
                    if ( value.getStringCellValue().equalsIgnoreCase("Testcases"))
                    {
                        //Desired column located
                        column = k; //The column number that contains desired text.

                    }
                    k++;
                }

                //Iterate through the testcase column to find purchase row
                while(rows.hasNext())
                {
                    Row r = rows.next();
                    //get the value of the cell for row = r and column = column (variable)
                    if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testcaseName))
                    {
                        //After you find puchrase testcase row, pull all the data from that row and feed into test.
                        Iterator<Cell> cv = r.cellIterator();

                        while (cv.hasNext())
                        {
                            //String value = cv.next().getStringCellValue();
                            Cell c = cv.next();

                            if (c.getCellTypeEnum()== CellType.STRING)
                            {
                                a.add(c.getStringCellValue());
                            }
                            else
                            {
                                a.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                            //System.out.println(value);

                        }
                    }

                }
            }
        }
        return a;
    }

    public static void main (String[] args) throws IOException
    {

    }
}
