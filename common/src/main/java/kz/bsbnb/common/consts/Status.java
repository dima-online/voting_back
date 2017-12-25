package kz.bsbnb.common.consts;

/**
 * Created by Olzhas.Pazyldayev on 15.09.2016.
 */
public enum Status {
    ACTIVE, //активная
    BLOCKED, //заблокированная
    ARCHIVED, //архивны
    PENDING, //в ожидании
    APPROVED, //Одобрена
    DELETED, //Удалена
    ERROR, // Ошибка
    NEW, // Новый
    CLOSED, //закрыта
    CLOSED_CANCELED, //закрыт по отмене
    SUCCESS; //успешна

    public static Status getStatus(String status) {
        return valueOf(status);
    }

}
