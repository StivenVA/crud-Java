package org.project.spring.service;

import lombok.RequiredArgsConstructor;
import org.project.spring.DTO.ImageDTO;
import org.project.spring.entity.Employee;
import org.project.spring.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final EmployeeRepository employeeRepository;

    public List<ImageDTO> getAllImages(){
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(
                        employee ->
                                new ImageDTO(
                                        employee.getCreatedAt().toLocalDateTime().toLocalDate(),
                                        employee.getImage(),
                                        employee.getCreatedAt().toLocalDateTime().toLocalTime()
                                )
                )
                .filter(imageDTO->imageDTO.getImage()!=null)
                .sorted(
                        Comparator
                                .comparing(ImageDTO::getCreationDate)
                                .thenComparing(ImageDTO::getCreationTime)
                ).collect(Collectors.toList());
    }

}
