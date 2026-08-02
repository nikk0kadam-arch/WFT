package com.ems.controller;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.model.EmployeeType;
import com.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest loads only the web layer (controller + security filter
 * chain), with the service layer mocked out via @MockBean. This lets
 * us verify routing, status codes, validation, and access control
 * without needing a real database.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getAllEmployees_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllEmployees_asUser_isAllowed() throws Exception {
        EmployeeResponseDTO dto = sampleResponse();
        when(employeeService.getAllEmployees()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aditi Sharma"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createEmployee_asPlainUser_isForbidden() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_withInvalidPayload_returns400WithFieldErrors() throws Exception {
        EmployeeRequestDTO invalid = new EmployeeRequestDTO(
                "", "not-an-email", "", new BigDecimal("-5"), null, LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_asAdmin_isCreated() throws Exception {
        when(employeeService.createEmployee(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Aditi Sharma"));
    }

    private EmployeeRequestDTO sampleRequest() {
        return new EmployeeRequestDTO("Aditi Sharma", "aditi.sharma@example.com", "Engineering",
                new BigDecimal("75000.00"), EmployeeType.FULL_TIME, LocalDate.of(2019, 3, 15));
    }

    private EmployeeResponseDTO sampleResponse() {
        return new EmployeeResponseDTO(1L, "Aditi Sharma", "aditi.sharma@example.com", "Engineering",
                new BigDecimal("75000.00"), EmployeeType.FULL_TIME, LocalDate.of(2019, 3, 15), 7,
                LocalDateTime.now());
    }
}
