package net.supervision.mcpserver.tools;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpTools {
    @McpTool(name="getEmployee", description = "Get information on employee")
    public Employee getEmployee(@McpArg(description="name of employee") String name) {
        return new Employee(name,12300,4);
    }
    @McpTool(description = "Get All employees")
    public List<Employee> getAllEmployees(){
        return List.of(
                new Employee("Hassan",10000,4),
                new Employee("Imane",12300,4),
                new Employee("Mohamed",34000,10)
        );

    }

}
record Employee(String name, double salary, int seniority) {}

