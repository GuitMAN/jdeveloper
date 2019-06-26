package ru.app;

public class DVMMain {

    public static void main(String[] args) throws Exception {

        DVMTools tools = new DVMTools();

        Dvm dvm = tools.LoadDVMFile("D:\\Repos\\jdeveloper\\AppSE\\DVM_Tools\\cm.AccountingCategory.V1.dvm");

        for (int i = 0; i < dvm.columns.getColumn().size(); i++) {
            System.out.print("|      " + dvm.columns.getColumn().get(i).name + "          ");
        }
        System.out.println();
        for (int i = 0; i < dvm.rows.row.size(); i++)
         {
                System.out.println();
                for (int j = 0; j < dvm.rows.row.get(i).cell.size(); j++)
                    System.out.print("|" + dvm.rows.row.get(i).cell.get(j).toString() + "          ");
            }
        


        System.out.println();
        return;
    }


}
