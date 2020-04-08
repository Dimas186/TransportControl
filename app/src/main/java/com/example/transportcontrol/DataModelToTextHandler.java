package com.example.transportcontrol;

import com.example.transportcontrol.model.DataModel;

public class DataModelToTextHandler {
    public static String handle(DataModel dataModel) {
        StringBuilder result = new StringBuilder();
        result.append("Автоколонна: ").append(dataModel.getMotorcade()).append("\n\n");
        result.append("Тип ТС: ").append(dataModel.getVehicleType()).append("\n\n");
        result.append("Марка: ").append(dataModel.getBrand()).append("\n\n");
        result.append("Гос. Номер: ").append(dataModel.getPlateNumber()).append("\n\n");
        result.append("Инвентарный номер: ").append(dataModel.getInventoryNumber()).append("\n\n");
        result.append("Гаражный номер: ").append(dataModel.getGarageNumber()).append("\n\n");
        result.append("Закрепленные водители: ").append(dataModel.getMotorcade()).append("\n\n");
        result.append("Тех. Осмотр: ").append(dataModel.getTechnicalInspection()).append("\n\n");
        result.append("Страховка: ").append(dataModel.getInsurance()).append("\n\n");
        result.append("Аптечка: ").append(dataModel.getFirstAidKit()).append("\n\n");
        return result.toString();
    }
}
