package view.ui.service;

import view.client.connection.ClientConnectionFactoryManage;
import view.client.routes.GetClient;
import view.dto.OperationGetClientResult;
import view.ui.frame.Layout;

import javax.swing.*;
import java.io.IOException;

public class HttpGetClient implements HttpGetTime{
    @Override
    public void getTime() {
        ClientConnectionFactoryManage clientConnectionFactoryManage = new ClientConnectionFactoryManage();
        GetClient getClient = new GetClient(clientConnectionFactoryManage);

        OperationGetClientResult operationGetClientResult = null;
        try {
            operationGetClientResult = getClient.clientGetHandler();
        } catch (IOException error) {
            JOptionPane.showMessageDialog(null, "Erro ao requisitar dados: " + error.getMessage());
        }

        if (operationGetClientResult.response() != null) {
            System.out.println(operationGetClientResult);
        } else {
            System.out.println(operationGetClientResult.operationResult());
        }
    }
}
