package com.example.transportcontrol.handler;

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
        result.append("Закрепленные водители: ").append(dataModel.getDrivers()).append("\n\n");
        result.append("Тех. Осмотр: ").append(dataModel.getTechnicalInspection()).append("\n\n");
        result.append("Страховка: ").append(dataModel.getInsurance()).append("\n\n");
        result.append("Аптечка: ").append(dataModel.getFirstAidKit()).append("\n\n");
        result.append("Огнетушитель: ").append(dataModel.getExtinguisher()).append("\n\n");
        result.append("Дата проведения Предыдущего ТО: ").append(dataModel.getPreviousTechnicalInspection()).append("\n\n");
        result.append("Номера колес: ").append(dataModel.getWheelNumbers()).append("\n\n");
        result.append("Замечания: ").append(dataModel.getComments()).append("\n\n");
        result.append("Дата устранения: ").append(dataModel.getEliminationDate()).append("\n\n");
        result.append("Плотность ОЖ: ").append(dataModel.getCoolantDensity()).append("\n\n");
        result.append("Состояние электрооборудования: ").append(dataModel.getElectricalEquipmentState()).append("\n\n");
        result.append("Достаточное давление в огнетушителе: ").append(dataModel.getSufficientPressureInTheFireExtinguisher()).append("\n\n");
        result.append("Плотность электролита (Состояние АКБ): ").append(dataModel.getElectrolyteDensity()).append("\n\n");
        result.append("Предложение: ").append(dataModel.getOffers()).append("\n\n");
        return result.toString();
    }
}
