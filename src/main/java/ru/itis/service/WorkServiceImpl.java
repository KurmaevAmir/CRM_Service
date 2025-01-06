package ru.itis.service;

import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.Specification;
import ru.itis.models.TypeWork;
import ru.itis.models.Work;
import ru.itis.repositories.SpecificationRepository;
import ru.itis.repositories.TypeWorkRepository;
import ru.itis.repositories.WorkRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WorkServiceImpl implements WorkService {
    private TypeWorkRepository typeWorkRepository;
    private WorkRepository workRepository;
    private SpecificationRepository specificationRepository;

    public WorkServiceImpl(TypeWorkRepository typeWorkRepository, WorkRepository workRepository, SpecificationRepository specificationRepository) {
        this.typeWorkRepository = typeWorkRepository;
        this.workRepository = workRepository;
        this.specificationRepository = specificationRepository;
    }

    @Override
    public List<TypeWork> findAllTypesWork() throws SQLException {
        return typeWorkRepository.findAll();
    }

    @Override
    public Optional<Long> validateTypeWorkOperation(String typeWorkOperation) throws SQLException {
        return typeWorkRepository.findByOperation(typeWorkOperation);
    }

    @Override
    public Optional<Long> validateSpecification(String specificationIdStr) throws SQLException {
        Long specificationId = Long.parseLong(specificationIdStr);
        Optional<Specification> specification = specificationRepository.findById(specificationId);
        if (specification.isPresent()) {
            return Optional.of(specificationId);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void saveWork(Long typeWork, String priceStr, String warrantyStr, Long specification) throws NumberFormatException, SQLException {
        Double price = Double.parseDouble(priceStr);
        Integer warranty = Integer.parseInt(warrantyStr);

        Work work = Work.builder()
                .typeWork(typeWork)
                .price(price)
                .warranty(warranty)
                .specification(specification)
                .build();
        workRepository.save(work);
    }

    @Override
    public List<WorkListDto> findBySpecification(Long specificationId) throws SQLException {
        return workRepository.findBySpecification(specificationId);
    }

    @Override
    public List<WorkListDto> findAllWorks() throws SQLException {
        return workRepository.findAllWorkListDto();
    }

    @Override
    public void fire(String workId) throws SQLException, NumberFormatException {
        Long id = Long.parseLong(workId);
        workRepository.delete(id);
    }
}
