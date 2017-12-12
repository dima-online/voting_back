package kz.bsbnb.block.controller.voting;

/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
public interface IHLVotingController {
    Object deployChainCode(String path, String tableName);
    Object invokeChainCode(String tableName, String decisionDocumentHash, String voterIin, String accountNumber, String votingId, String chainCodeName);
    Object queryChainCode(String tableName, String decisionDocumentHash, String chainCodeName);
}
