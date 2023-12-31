package com.carrental.agency.service;

import com.carrental.agency.mapper.EmployeeMapper;
import com.carrental.agency.mapper.EmployeeMapperImpl;
import com.carrental.agency.repository.EmployeeRepository;
import com.carrental.agency.util.TestUtils;
import com.carrental.document.model.Branch;
import com.carrental.document.model.Employee;
import com.carrental.dto.EmployeeDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BranchService branchService;

    @Spy
    private EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @Test
    void findAllEmployeesTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        List<Employee> employees = List.of(employee);

        when(employeeRepository.findAll()).thenReturn(Flux.fromIterable(employees));

        StepVerifier.create(employeeService.findAllEmployees())
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    void findAllEmployeesTest_errorOnFindAll() {
        when(employeeRepository.findAll()).thenReturn(Flux.error(new Throwable()));

        StepVerifier.create(employeeService.findAllEmployees())
                .expectError()
                .verify();
    }

    @Test
    void findEmployeeByIdTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        when(employeeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(employee));

        StepVerifier.create(employeeService.findEmployeeById("64f361caf291ae086e179547"))
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    void findEmployeeByIdTest_errorOnFindingById() {
        when(employeeRepository.findById(any(ObjectId.class))).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(employeeService.findEmployeeById("64f361caf291ae086e179547"))
                .expectError()
                .verify();
    }

    @Test
    void countEmployeesTest_success() {
        when(employeeRepository.count()).thenReturn(Mono.just(3L));

        StepVerifier.create(employeeService.countEmployees())
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void countEmployeesTest_errorOnCounting() {
        when(employeeRepository.count()).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(employeeService.countEmployees())
                .expectError()
                .verify();
    }

    @Test
    void saveEmployeeTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        Branch branch = TestUtils.getResourceAsJson("/data/Branch.json", Branch.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.just(employee));

        StepVerifier.create(employeeService.saveEmployee(employeeDto))
                .expectNext(employeeDto)
                .verifyComplete();

        verify(employeeMapper, times(1)).mapEntityToDto(any(Employee.class));
    }

    @Test
    void saveEmployeeTest_errorOnSave() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        Branch branch = TestUtils.getResourceAsJson("/data/Branch.json", Branch.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(employeeService.saveEmployee(employeeDto))
                .expectError()
                .verify();

        verify(employeeMapper, never()).mapEntityToDto(any(Employee.class));
    }

    @Test
    void updateEmployeeTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        Branch branch = TestUtils.getResourceAsJson("/data/Branch.json", Branch.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(employeeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.just(employee));

        StepVerifier.create(employeeService.updateEmployee("64f361caf291ae086e179547", employeeDto))
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    void updateEmployeeTest_errorOnSave() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        Branch branch = TestUtils.getResourceAsJson("/data/Branch.json", Branch.class);

        when(branchService.findEntityById(anyString())).thenReturn(Mono.just(branch));
        when(employeeRepository.findById(any(ObjectId.class))).thenReturn(Mono.just(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(employeeService.updateEmployee("64f361caf291ae086e179547", employeeDto))
                .expectError()
                .verify();
    }

    @Test
    void findEmployeesByBranchIdTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        List<Employee> employees = List.of(employee);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        when(employeeRepository.findAllEmployeesByBranchId(any(ObjectId.class))).thenReturn(Flux.fromIterable(employees));

        StepVerifier.create(employeeService.findEmployeesByBranchId("64f361caf291ae086e179547"))
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    void findEmployeesByBranchIdTest_errorOnFindingByBranchId() {
        when(employeeRepository.findAllEmployeesByBranchId(any(ObjectId.class))).thenReturn(Flux.error(new Throwable()));

        StepVerifier.create(employeeService.findEmployeesByBranchId("64f361caf291ae086e179547"))
                .expectError()
                .verify();
    }

    @Test
    void findEmployeeByFilterTest_success() {
        Employee employee = TestUtils.getResourceAsJson("/data/Employee.json", Employee.class);
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        when(employeeRepository.findAllByFilterInsensitiveCase(anyString())).thenReturn(Flux.just(employee));

        StepVerifier.create(employeeService.findEmployeeByFilterInsensitiveCase("search"))
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    void findEmployeeByFilterTest_errorWhileFindingByFilter() {
        when(employeeRepository.findAllByFilterInsensitiveCase(anyString())).thenReturn(Flux.error(new Throwable()));

        StepVerifier.create(employeeService.findEmployeeByFilterInsensitiveCase("search"))
                .expectError()
                .verify();
    }

    @Test
    void deleteEmployeeByIdTest_success() {
        when(employeeRepository.deleteById(any(ObjectId.class))).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.deleteEmployeeById("64f361caf291ae086e179547"))
                .expectComplete()
                .verify();
    }

    @Test
    void deleteEmployeeByIdTest_errorOnDeletingById() {
        when(employeeRepository.deleteById(any(ObjectId.class))).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(employeeService.deleteEmployeeById("64f361caf291ae086e179547"))
                .expectError()
                .verify();
    }

}
