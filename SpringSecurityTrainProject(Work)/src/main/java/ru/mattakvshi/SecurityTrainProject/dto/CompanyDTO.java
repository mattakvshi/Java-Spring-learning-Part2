package ru.mattakvshi.SecurityTrainProject.dto;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import ru.mattakvshi.SecurityTrainProject.company.ITCompany;
import ru.mattakvshi.SecurityTrainProject.company.employee.Employee;
import ru.mattakvshi.SecurityTrainProject.company.employee.ITRole;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CompanyDTO {
    private String name;
    private ITEmployeeDTO director;
    private List<ITEmployeeDTO> employees;

    public static CompanyDTO from(ITCompany company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setDirector(ITEmployeeDTO.from(company.getDirector()));
        companyDTO.setName(company.getName());

        List<ITEmployeeDTO> itEmployeeDTOList = company.getEmployees().stream()
                .map(ITEmployeeDTO::from)
                .collect(Collectors.toList());
        companyDTO.setEmployees(itEmployeeDTOList);
        return companyDTO;
    }

    public ITCompany toCompany(){
        ITCompany company = new ITCompany(this.name);
        company.setDirector(this.director.toEmployee());
        company.getDirector().setCompany(company);

        if(!CollectionUtils.isEmpty(this.employees)) {
            List<Employee<ITRole>> employees = this.employees.stream()
                    .map(ITEmployeeDTO::toEmployee)
                    .peek(e -> e.setCompany(company))
                    .collect(Collectors.toList());
            company.getEmployees().addAll(employees);
        }
        return company;
    }
}
