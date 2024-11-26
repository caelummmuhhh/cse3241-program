package Models;

public class EquipmentModel {
    public int ItemID;
    public String Name;
    public String Type;
    public String Size;
    public int Weight;
    public String Color;
    public int Year;
    public int WarehouseID;
    public int ManufacturerID;

    public EquipmentModel(int itemID, String name, String type, String size, int weight, String color, int year, int warehouseID, int manufacturerID) {
        this.ItemID = itemID;
        this.Name = name;
        this.Type = type;
        this.Size = size;
        this.Weight = weight;
        this.Color = color;
        this.Year = year;
        this.WarehouseID = warehouseID;
        this.ManufacturerID = manufacturerID;
    }

    public EquipmentModel() { }
}
