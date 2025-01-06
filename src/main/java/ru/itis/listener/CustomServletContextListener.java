package ru.itis.listener;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.itis.repositories.*;
import ru.itis.service.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/CRM+Service";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Fvbh";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);

        WorkRepository workRepository = new WorkRepositoryJdbcImpl(dataSource);

        RequestRepository requestRepository = new RequestRepositoryJdbcImpl(dataSource);
        servletContext.setAttribute("requestRepository", requestRepository);

        IndexService indexService = new IndexServiceImpl(requestRepository);
        servletContext.setAttribute("indexService", indexService);

        UserRepository userRepository = new UserRepositoryJdbcImpl(dataSource);
        AuthService authService = new AuthServiceImpl(userRepository);
        servletContext.setAttribute("authService", authService);

        EmployeeRepository employeeRepository = new EmployeeRepositoryJdbcImpl(dataSource);
        PassportRepository passportRepository = new PassportRepositoryJdbcImpl(dataSource);
        SignUpService signUpService = new SignUpServiceImpl(employeeRepository, passportRepository, userRepository);
        servletContext.setAttribute("signUpService", signUpService);

        ClientRepository clientRepository = new ClientRepositoryJdbcImpl(dataSource);
        CreateClientService createClientService = new CreateClientServiceImpl(clientRepository, passportRepository, userRepository);
        servletContext.setAttribute("createClientService", createClientService);

        TypeDeviceRepository typeDeviceRepository = new TypeDeviceRepositoryJdbcImpl(dataSource);
        ManufacturerRepository manufacturerRepository = new ManufacturerRepositoryJdbcImpl(dataSource);
        SpecificationRepository specificationRepository = new SpecificationRepositoryJdbcImpl(dataSource);
        DeviceRepository deviceRepository = new DeviceRepositoryJdbcImpl(dataSource);
        FileRepository fileRepository = new FileRepositoryJdbcImpl(dataSource);
        StatusRepository statusRepository = new StatusRepositoryJdbcImpl(dataSource);
        RequestService requestService = new RequestServiceImpl(clientRepository, typeDeviceRepository, manufacturerRepository,
                specificationRepository, deviceRepository, requestRepository, workRepository, fileRepository, employeeRepository,
                statusRepository);
        servletContext.setAttribute("requestService", requestService);

        DeviceService deviceService = new DeviceServiceImpl(deviceRepository);
        servletContext.setAttribute("createDeviceService", deviceService);

        EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository, passportRepository);
        servletContext.setAttribute("employeeService", employeeService);

        TypeWorkRepository typeWorkRepository = new TypeWorkRepositoryJdbcImpl(dataSource);
        WorkService workService = new WorkServiceImpl(typeWorkRepository, workRepository, specificationRepository);
        servletContext.setAttribute("workService", workService);

        ManufacturerService manufacturerService = new ManufacturerServiceImpl(manufacturerRepository, typeDeviceRepository);
        servletContext.setAttribute("manufacturerService", manufacturerService);

        SpecificationService specificationService = new SpecificationServiceImpl(specificationRepository, manufacturerRepository);
        servletContext.setAttribute("specificationService", specificationService);

        FileService fileService = new FileServiceImpl(fileRepository);
        servletContext.setAttribute("fileService", fileService);

        TypeWorkService typeWorkService = new TypeWorkServiceImpl(typeWorkRepository);
        servletContext.setAttribute("typeWorkService", typeWorkService);

        TypeDeviceService typeDeviceService = new TypeDeviceServiceImpl(typeDeviceRepository);
        servletContext.setAttribute("typeDeviceService", typeDeviceService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
