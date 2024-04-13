package view.client.routes;

import view.dto.OperationGetClientResult;
import view.dto.OperationPostClientResult;

import java.io.IOException;

interface ClientGet {
    OperationGetClientResult clientGetHandler() throws IOException;
}

interface  ClientPost {
    OperationPostClientResult clientPostHandler(String data) throws IOException;
}
