package online.stemlink.skillmentor.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.stemlink.skillmentor.dtos.StudentDTO;
import online.stemlink.skillmentor.entities.Student;
import online.stemlink.skillmentor.security.UserPrincipal;
import online.stemlink.skillmentor.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import static online.stemlink.skillmentor.constants.UserRoles.ROLE_ADMIN;
import static online.stemlink.skillmentor.constants.UserRoles.ROLE_STUDENT;

@RestController
@RequestMapping(path = "/api/v1/students")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class StudentController extends AbstractController{

    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return sendOkResponse(students);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return sendOkResponse(student);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "', '" + ROLE_STUDENT + "')")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDTO studentDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Student student = modelMapper.map(studentDTO, Student.class);
        student.setStudentId(userPrincipal.getId());
        student.setFirstName(userPrincipal.getFirstName());
        student.setLastName(userPrincipal.getLastName());
        student.setEmail(userPrincipal.getEmail());

        Student createdStudent = studentService.createNewStudent(student);
        return sendCreatedResponse(createdStudent);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "', '" + ROLE_STUDENT + "')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO updatedStudentDTO) {
        Student student = modelMapper.map(updatedStudentDTO, Student.class);
        Student updatedStudent = studentService.updateStudentById(id, student);
        return sendOkResponse(updatedStudent);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return sendNoContentResponse();
    }
}