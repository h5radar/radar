package com.h5radar.radar.domain.tenant;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.h5radar.radar.domain.AbstractControllerTests;

@WebMvcTest(TenantController.class)
public class TenantControllerTests extends AbstractControllerTests {
  @MockitoBean
  private TenantService tenantService;

  @Test
  @WithMockUser(value = "My sub")
  public void shouldGetTenants() throws Exception {
    final TenantDto tenantDto = new TenantDto(10L, "My title", "My description");
    Page<TenantDto> tenantPage = new PageImpl<>(Arrays.asList(tenantDto));
    Mockito.when(tenantService.findAll(any(), any())).thenReturn(tenantPage);

    mockMvc.perform(get("/api/v1/tenants").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(tenantPage.getContent().size())))
        .andExpect(jsonPath("$[0].id", equalTo(tenantDto.getId()), Long.class))
        .andExpect(jsonPath("$[0].title", equalTo(tenantDto.getTitle())))
        .andExpect(jsonPath("$[0].description", equalTo(tenantDto.getDescription())));
  }
}
