package kz.bsbnb.util.ERCBService;

import javax.xml.rpc.ServiceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruslan on 1/24/17.
 */
public class ERCBClient {
    public static void main(String[] args) throws ServiceException {
//        IERCBVotingServicesservice service = new IERCBVotingServicesserviceLocator();
//        IERCBVotingServices iercbVotingServicesPort = service.getIERCBVotingServicesPort();
//
//        try {
//            String chief = iercbVotingServicesPort.getChief("990640000421");
//            System.out.println(chief);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            int i = iercbVotingServicesPort.existsRegistry("990640000421", "26.01.2017");
//            System.out.println("i="+i);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            TResponseRegistry registry = iercbVotingServicesPort.getRegistry("990640000421", "07.06.2016");
//            System.out.println("registry.getErrorCode()="+registry.getErrorCode());
//            System.out.println("getIssuerName()="+registry.getRegistry().getIssuerName());
//            for (int i = 0; i<registry.getRegistry().getShareholders().length; i++) {
//                System.out.println(""+registry.getRegistry().getShareholders()[i].getShareholderName());
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date truncatedDate = null;
        try {
            truncatedDate = dateFormat.parse("21/01/2017");
            System.out.println("date="+truncatedDate.toString());
        } catch (ParseException e) {
            System.out.println("error");;
        }

    }
}
