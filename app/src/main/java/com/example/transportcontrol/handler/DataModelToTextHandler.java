package com.example.transportcontrol.handler;

import com.example.transportcontrol.model.DataModel;

public class DataModelToTextHandler {
    public static String handle(DataModel dataModel) {
        return "Автоколонна: " + dataModel.getMotorcade() + "\n\n" +
                "Марка: " + dataModel.getBrand() + "\n\n" +
                "Модель: " + dataModel.getVehicleType() + "\n\n" +
                "Гос. Номер: " + dataModel.getPlateNumber() + "\n\n" +
                "Инвентарный номер: " + dataModel.getInventoryNumber() + "\n\n" +
                "Гаражный номер: " + dataModel.getGarageNumber() + "\n\n" +
                "Закрепленные водители: " + dataModel.getDrivers() + "\n\n" +
                "Тех. Осмотр: " + dataModel.getTechnicalInspection() + "\n\n" +
                "Страховка: " + dataModel.getInsurance() + "\n\n" +
                "Аптечка: " + dataModel.getFirstAidKit() + "\n\n" +
                "Огнетушитель: " + dataModel.getExtinguisher() + "\n\n" +
                "Дата проведения Предыдущего ТО: " + dataModel.getPreviousTechnicalInspection() + "\n\n" +
                "Номера колес: " + dataModel.getWheelNumbers() + "\n\n" +
                "Замечания: " + dataModel.getComments() + "\n\n" +
                "Дата устранения: " + dataModel.getEliminationDate() + "\n\n" +
                "Плотность ОЖ: " + dataModel.getCoolantDensity() + "\n\n" +
                "Состояние электрооборудования: " + dataModel.getElectricalEquipmentState() + "\n\n" +
                "Достаточное давление в огнетушителе: " + dataModel.getSufficientPressureInTheFireExtinguisher() + "\n\n" +
                "Плотность электролита (Состояние АКБ): " + dataModel.getElectrolyteDensity() + "\n\n" +
                "Предложение: " + dataModel.getOffers() + "\n\n";
    }
}
