package Helpers;

import java.util.Collection;

import Models.EquipmentModel;

public class PrintHelpers {
    public static void printHeader(String msg) {
        System.out.println(
            "\n------------------- " + msg + " -------------------"
        );
    }

    public static void printMenu(Collection<String> menuOptions) {
        System.out.println(
                "Please choose from one of the following options " +
                        "(enter the number corresponding to the desired option):");

        for (String option : menuOptions) {
            System.out.println("\t" + option);
        }
    }

        public static void printItem(EquipmentModel item) {
        System.out.println(
                "\tItem ID: " + item.ItemID + "\n" +
                        "\tName: " + item.Name + "\n" +
                        "\tType: " + item.Type + "\n" +
                        "\tSize: " + item.Size + "\n" +
                        "\tWeight: " + item.Weight + "\n" +
                        "\tColor: " + item.Color + "\n" +
                        "\tYear: " + item.Year + "\n" +
                        "\tWarehouse ID: " + item.WarehouseID + "\n" +
                        "\tManufacturer ID: " + item.ManufacturerID);
    }
}
