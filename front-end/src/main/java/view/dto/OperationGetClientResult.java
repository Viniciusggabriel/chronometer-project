package view.dto;

import java.util.Map;

public record OperationGetClientResult(Map<String, Object> response, OperationResult operationResult) {
}
