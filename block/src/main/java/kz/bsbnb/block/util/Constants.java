package kz.bsbnb.block.util;

/**
 * Created by Olzhas.Pazyldayev on 12.08.2016.
 */
public class Constants {
    public static final String DB_SCHEMA_CORE = "core";

    /* HyperLedger properties */
    public static final String HLHostUrl = "http://localhost:5000";

    public static final String HL_COMMAND_DEPLOY = "deploy";
    public static final String HL_COMMAND_INVOKE = "invoke";
    public static final String HL_COMMAND_QUERY = "query";

    public static final String HL_COMMAND_FUNCTION_INIT = "init";
    public static final String HL_COMMAND_FUNCTION_VOTE = "voting";
    public static final String HL_COMMAND_FUNCTION_DECISION = "decision";

    public static final String HL_COMMAND_FUNCTION_TRANSFER = "transfer";
    public static final String HL_COMMAND_FUNCTION_REGISTER = "register";
    public static final String HL_COMMAND_FUNCTION_TRANSFER_FINAL = "transfer_final";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_BLOCK = "securities_block";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_UNBLOCK = "securities_unblock";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_BLOCK_PAY = "securities_block_pay";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_UNBLOCK_PAY = "securities_unblock_pay";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_BLOCK_NONPAY = "securities_block_nonpay";
    public static final String HL_COMMAND_FUNCTION_SECURITIES_UNBLOCK_NONPAY = "securities_unblock_nonpay";
    public static final String HL_COMMAND_FUNCTION_USER_BLOCK = "user_block";
    public static final String HL_COMMAND_FUNCTION_USER_UNBLOCK = "user_unblock";
    public static final String HL_COMMAND_FUNCTION_BALANCE = "balance";
    public static final String HL_COMMAND_FUNCTION_INFO = "info";

    public static final String HL_COMMAND_FUNCTION_TRANSFER_FROM_TRANSIT = "transfer_from_transit";
    public static final String HL_COMMAND_FUNCTION_TRANSIT = "transit";
    public static final String HL_COMMAND_FUNCTION_TRANSIT_ACTION_TRANSFER = "transfer_to_transit";
    public static final String HL_COMMAND_FUNCTION_TRANSIT_ACTION_ROLLBACK = "rollback_from_transit";
    public static final String TRANSACTION_BUY_FINSEC = "buy";
    public static final String TRANSACTION_SELL_FINSEC = "sell";
    public static final String QUERY_MESSAGE_STATUS_OK = "OK";
    public static final String BLOCKED_SECURITY_TYPE_PAY = "PAY";
    public static final String BLOCKED_SECURITY_TYPE_NONPAY = "NONPAY";


}
