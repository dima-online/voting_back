package kz.bsbnb.common.model;

/**
 * Created by Olzhas.Pazyldayev on 21.09.2016.
 */
public enum Step {
    NEW, //Новая
    CDCB_CHECK_ACCOUNT, //проверка аккаунта в цдцб
    CDCB_REGISTRATION, //регистрация аккаунта в цдцб
    EZKT_TRANSFER_MONEY, //перевод екзт в кцмр
    EZKT_REGISTRATION, //регистрация кошелька в кцмр
    BLOCK_TRANSFER_SECURITIES, //перевод нот в блокчейне
    BLOCK_REGISTRATION, //регистрация пользователя в блокчейне
    SUCCESS, //успешная
    ERROR; //ошибка

    public static Step getStep(String step) {
        return valueOf(step);
    }

}
