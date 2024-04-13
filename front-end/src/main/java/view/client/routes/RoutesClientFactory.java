package view.client.routes;

import view.dto.OperationGetClientResult;
import view.dto.OperationPostClientResult;
import view.dto.OperationResult;

import java.io.IOException;

interface ClientGet {
    OperationGetClientResult clientGetHandler() throws IOException;
}

interface  ClientPost {
    OperationResult clientPostHandler(String data) throws IOException;
}
